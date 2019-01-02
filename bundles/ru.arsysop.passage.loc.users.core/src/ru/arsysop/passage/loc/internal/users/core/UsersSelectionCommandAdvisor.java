/*******************************************************************************
 * Copyright (c) 2018 ArSysOp
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

import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.SelectionCommandAdvisor;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.UsersRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + UsersRegistry.DOMAIN_NAME })
public class UsersSelectionCommandAdvisor implements SelectionCommandAdvisor {
	
	private UserDomainRegistry registry;
	
	@Reference
	public void bindDomainRegistry(UserDomainRegistry registry) {
		this.registry = registry;
	}

	public void unbindDomainRegistry(UserDomainRegistry registry) {
		this.registry = null;
	}

	@Override
	public String getSelectionTitle(String classifier) {
		if (LicPackage.eINSTANCE.getUserOrigin().getName().equals(classifier)) {
			return "Select User Origin";
		}
		if (LicPackage.eINSTANCE.getUser().getName().equals(classifier)) {
			return "Select User";
		}
		return null;
	}

	@Override
	public Iterable<?> getSelectionInput(String classifier) {
		if (registry == null) {
			return Collections.emptyList();
		}
		if (LicPackage.eINSTANCE.getUserOrigin().getName().equals(classifier)) {
			return registry.getUserOrigins();
		}
		if (LicPackage.eINSTANCE.getUser().getName().equals(classifier)) {
			return registry.getUsers();
		}
		return Collections.emptyList();
	}

}
