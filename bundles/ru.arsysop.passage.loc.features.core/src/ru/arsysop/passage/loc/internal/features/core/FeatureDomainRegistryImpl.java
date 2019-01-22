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
package ru.arsysop.passage.loc.internal.features.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ru.arsysop.passage.lic.model.api.FeatureSet;
import ru.arsysop.passage.lic.model.core.LicModelCore;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.FeatureDescriptor;
import ru.arsysop.passage.lic.registry.FeatureRegistry;
import ru.arsysop.passage.lic.registry.FeatureSetDescriptor;
import ru.arsysop.passage.lic.registry.FeatureVersionDescriptor;
import ru.arsysop.passage.lic.registry.FeaturesEvents;
import ru.arsysop.passage.lic.registry.FeaturesRegistry;
import ru.arsysop.passage.lic.registry.Identified;
import ru.arsysop.passage.loc.edit.EditingDomainBasedRegistry;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + FeaturesRegistry.DOMAIN_NAME,
		DomainRegistryAccess.PROPERTY_FILE_EXTENSION + '=' + FeaturesRegistry.FILE_EXTENSION_XMI})
public class FeatureDomainRegistryImpl extends EditingDomainBasedRegistry
		implements FeatureRegistry, FeatureDomainRegistry, EditingDomainRegistry {

	private final Map<String, FeatureSetDescriptor> featureSetIndex = new HashMap<>();
	private final Map<String, FeatureDescriptor> featureIndex = new HashMap<>();
	private final Map<String, Map<String, FeatureVersionDescriptor>> featureVersionIndex = new HashMap<>();

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
		Collection<Map<String, FeatureVersionDescriptor>> values = featureVersionIndex.values();
		for (Map<String, FeatureVersionDescriptor> map : values) {
			map.clear();
		}
		featureVersionIndex.clear();
		featureIndex.clear();
		featureSetIndex.clear();
		super.deactivate(properties);
	}

	@Override
	public String getFileExtension() {
		return LicModelCore.FILE_EXTENSION_FEATURES;
	}

	@Override
	public Iterable<FeatureSetDescriptor> getFeatureSets() {
		return new ArrayList<>(featureSetIndex.values());
	}

	@Override
	public FeatureSetDescriptor getFeatureSet(String identifier) {
		return featureSetIndex.get(identifier);
	}

	@Override
	public Iterable<FeatureDescriptor> getFeatures() {
		return new ArrayList<>(featureIndex.values());
	}

	@Override
	public Iterable<FeatureDescriptor> getFeatures(String featureSetId) {
		FeatureSetDescriptor featureSet = featureSetIndex.get(featureSetId);
		if (featureSet == null) {
			return Collections.emptyList();
		}
		List<FeatureDescriptor> features = new ArrayList<>();
		featureSet.getFeatures().forEach(features::add);
		return features;
	}

	@Override
	public FeatureDescriptor getFeature(String identifier) {
		return featureIndex.get(identifier);
	}

	@Override
	public Iterable<FeatureVersionDescriptor> getFeatureVersions() {
		List<FeatureVersionDescriptor> list = new ArrayList<>();
		Collection<Map<String, FeatureVersionDescriptor>> values = featureVersionIndex.values();
		for (Map<String, FeatureVersionDescriptor> map : values) {
			list.addAll(map.values());
		}
		return list;
	}

	@Override
	public Iterable<FeatureVersionDescriptor> getFeatureVersions(String featureId) {
		Map<String, FeatureVersionDescriptor> map = featureVersionIndex.get(featureId);
		if (map == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(map.values());
	}

	@Override
	public FeatureVersionDescriptor getFeatureVersion(String featureId, String version) {
		Map<String, FeatureVersionDescriptor> map = featureVersionIndex.get(featureId);
		if (map == null) {
			return null;
		}
		return map.get(version);
	}

	@Override
	protected DomainContentAdapter<FeatureDomainRegistry> createContentAdapter() {
		return new FeatureDomainRegistryTracker(this);
	}

	@Override
	public void registerFeatureSet(FeatureSetDescriptor featureSet) {
		String identifier = featureSet.getIdentifier();
		FeatureSetDescriptor existing = featureSetIndex.put(identifier, featureSet);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(FeaturesEvents.FEATURE_SET_CREATE, featureSet));
		Iterable<? extends FeatureDescriptor> features = featureSet.getFeatures();
		for (FeatureDescriptor feature : features) {
			registerFeature(feature);
		}
	}

	@Override
	public void registerFeature(FeatureDescriptor feature) {
		String identifier = feature.getIdentifier();
		FeatureDescriptor existing = featureIndex.put(identifier, feature);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(FeaturesEvents.FEATURE_CREATE, feature));
		Iterable<? extends FeatureVersionDescriptor> featureVersions = feature.getFeatureVersions();
		for (FeatureVersionDescriptor featureVersion : featureVersions) {
			registerFeatureVersion(feature, featureVersion);
		}
	}

	@Override
	public void registerFeatureVersion(FeatureDescriptor feature, FeatureVersionDescriptor featureVersion) {
		String identifier = feature.getIdentifier();
		Map<String, FeatureVersionDescriptor> map = featureVersionIndex.computeIfAbsent(identifier,
				key -> new HashMap<>());
		String version = featureVersion.getVersion();
		FeatureVersionDescriptor existing = map.put(version, featureVersion);
		if (existing != null) {
			// FIXME: warning
		}
		eventAdmin.postEvent(createEvent(FeaturesEvents.FEATURE_VERSION_CREATE, featureVersion));
	}

	@Override
	public void unregisterFeatureSet(String featureSetId) {
		FeatureSetDescriptor removed = featureSetIndex.remove(featureSetId);
		if (removed != null) {
			eventAdmin.postEvent(createEvent(FeaturesEvents.FEATURE_SET_DELETE, removed));
			Iterable<? extends FeatureDescriptor> features = removed.getFeatures();
			for (FeatureDescriptor feature : features) {
				unregisterFeature(feature.getIdentifier());
			}
		}
	}

	@Override
	public void unregisterFeature(String featureId) {
		FeatureDescriptor removed = featureIndex.remove(featureId);
		if (removed != null) {
			eventAdmin.postEvent(createEvent(FeaturesEvents.FEATURE_DELETE, removed));
			Iterable<? extends FeatureVersionDescriptor> featureVersions = removed.getFeatureVersions();
			for (FeatureVersionDescriptor featureVersion : featureVersions) {
				unregisterFeatureVersion(featureId, featureVersion.getVersion());
			}
		}
	}

	@Override
	public void unregisterFeatureVersion(String featureId, String version) {
		Map<String, FeatureVersionDescriptor> map = featureVersionIndex.get(featureId);
		if (map != null) {
			FeatureVersionDescriptor removed = map.remove(version);
			if (removed != null) {
				eventAdmin.postEvent(createEvent(FeaturesEvents.FEATURE_VERSION_DELETE, removed));
			}
			if (map.isEmpty()) {
				featureVersionIndex.remove(featureId);
			}
		}
	}

	@Override
	public EClass getContentClassifier() {
		return LicPackage.eINSTANCE.getFeatureSet();
	}

	@Override
	public EStructuralFeature getContentIdentifierAttribute() {
		return LicPackage.eINSTANCE.getFeatureSet_Identifier();
	}

	@Override
	public EStructuralFeature getContentNameAttribute() {
		return LicPackage.eINSTANCE.getFeatureSet_Name();
	}

	@Override
	public void registerContent(Identified content) {
		if (content instanceof FeatureSet) {
			FeatureSet featureSet = (FeatureSet) content;
			registerFeatureSet(featureSet);
		} else {
			//TODO: warning
		}
	}

	@Override
	public void unregisterContent(String identifier) {
		unregisterFeatureSet(identifier);
	}

}
