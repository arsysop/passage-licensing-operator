/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package ru.arsysop.passage.loc.internal.users.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;

import ru.arsysop.passage.lic.emf.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.lic.emf.edit.DomainContentAdapter;
import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.EditingDomainRegistry;
import ru.arsysop.passage.lic.model.api.User;
import ru.arsysop.passage.lic.model.api.UserOrigin;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.Identified;
import ru.arsysop.passage.lic.registry.UserDescriptor;
import ru.arsysop.passage.lic.registry.UserOriginDescriptor;
import ru.arsysop.passage.lic.registry.UserRegistry;
import ru.arsysop.passage.lic.registry.UsersEvents;
import ru.arsysop.passage.lic.registry.UsersRegistry;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + UsersRegistry.DOMAIN_NAME,
		DomainRegistryAccess.PROPERTY_FILE_EXTENSION + '=' + UsersRegistry.FILE_EXTENSION_XMI })
public class UserDomainRegistryImpl extends EditingDomainBasedRegistry
		implements UserRegistry, UserDomainRegistry, EditingDomainRegistry {

	private final Map<String, UserOrigin> userOriginIndex = new HashMap<>();
	private final Map<String, User> userIndex = new HashMap<>();

	@Reference
	@Override
	public void bindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		super.bindEnvironmentInfo(environmentInfo);
	}

	@Override
	public void unbindEnvironmentInfo(EnvironmentInfo environmentInfo) {
		super.unbindEnvironmentInfo(environmentInfo);
	}

	@Reference
	@Override
	public void bindEventAdmin(EventAdmin eventAdmin) {
		super.bindEventAdmin(eventAdmin);
	}

	@Override
	public void unbindEventAdmin(EventAdmin eventAdmin) {
		super.unbindEventAdmin(eventAdmin);
	}

	@Reference
	@Override
	public void bindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		super.bindFactoryProvider(factoryProvider);
	}

	@Override
	public void unbindFactoryProvider(ComposedAdapterFactoryProvider factoryProvider) {
		super.unbindFactoryProvider(factoryProvider);
	}

	@Activate
	public void activate(Map<String, Object> properties) {
		super.activate(properties);
	}

	@Deactivate
	@Override
	public void deactivate(Map<String, Object> properties) {
		userIndex.clear();
		userOriginIndex.clear();
		super.deactivate(properties);
	}

	@Override
	public String getFileExtension() {
		return UsersRegistry.FILE_EXTENSION_XMI;
	}

	@Override
	public Iterable<UserOriginDescriptor> getUserOrigins() {
		return new ArrayList<>(userOriginIndex.values());
	}

	@Override
	public UserOriginDescriptor getUserOrigin(String identifier) {
		return userOriginIndex.get(identifier);
	}

	@Override
	public Iterable<UserDescriptor> getUsers() {
		return new ArrayList<>(userIndex.values());
	}

	@Override
	public Iterable<UserDescriptor> getUsers(String userOriginId) {
		UserOrigin userOrigin = userOriginIndex.get(userOriginId);
		if (userOrigin == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(userOrigin.getUsers());
	}

	@Override
	public UserDescriptor getUser(String identifier) {
		return userIndex.get(identifier);
	}

	@Override
	protected DomainContentAdapter<UserDomainRegistry> createContentAdapter() {
		return new UserDomainRegistryTracker(this);
	}

	@Override
	public void registerUserOrigin(UserOrigin userOrigin) {
		String identifier = userOrigin.getIdentifier();
		UserOrigin existing = userOriginIndex.put(identifier, userOrigin);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(UsersEvents.USER_ORIGIN_CREATE, userOrigin));
		EList<User> users = userOrigin.getUsers();
		for (User user : users) {
			registerUser(user);
		}
	}

	@Override
	public void registerUser(User user) {
		String identifier = user.getEmail();
		User existing = userIndex.put(identifier, user);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(UsersEvents.USER_CREATE, user));
	}

	@Override
	public void unregisterUserOrigin(String userOriginId) {
		UserOrigin removed = userOriginIndex.remove(userOriginId);
		if (removed != null) {
			eventAdmin.postEvent(createEvent(UsersEvents.USER_ORIGIN_DELETE, removed));
			EList<User> users = removed.getUsers();
			for (User user : users) {
				unregisterUser(user.getEmail());
			}
		}
	}

	@Override
	public void unregisterUser(String userId) {
		User removed = userIndex.remove(userId);
		if (removed != null) {
			eventAdmin.postEvent(createEvent(UsersEvents.USER_DELETE, removed));
		}
	}

	@Override
	public EClass getContentClassifier() {
		return LicPackage.eINSTANCE.getUserOrigin();
	}

	@Override
	public EStructuralFeature getContentIdentifierAttribute() {
		return LicPackage.eINSTANCE.getUserOrigin_Identifier();
	}

	@Override
	public EStructuralFeature getContentNameAttribute() {
		return LicPackage.eINSTANCE.getUserOrigin_Name();
	}

	@Override
	public void registerContent(Identified content) {
		if (content instanceof UserOrigin) {
			UserOrigin userOrigin = (UserOrigin) content;
			registerUserOrigin(userOrigin);
		} else {
			//TODO: warning
		}
	}

	@Override
	public void unregisterContent(String identifier) {
		unregisterUserOrigin(identifier);
	}

}
