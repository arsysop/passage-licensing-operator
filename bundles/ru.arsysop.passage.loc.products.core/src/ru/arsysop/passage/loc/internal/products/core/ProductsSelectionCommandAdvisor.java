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
package ru.arsysop.passage.loc.internal.products.core;

import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ru.arsysop.passage.lic.emf.edit.DomainRegistryAccess;
import ru.arsysop.passage.lic.emf.edit.SelectionCommandAdvisor;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.lic.registry.ProductsRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;

@Component(property = { DomainRegistryAccess.PROPERTY_DOMAIN_NAME + '=' + ProductsRegistry.DOMAIN_NAME })
public class ProductsSelectionCommandAdvisor implements SelectionCommandAdvisor {
	
	private ProductDomainRegistry registry;
	
	@Reference
	public void bindDomainRegistry(ProductDomainRegistry registry) {
		this.registry = registry;
	}

	public void unbindDomainRegistry(ProductDomainRegistry registry) {
		this.registry = null;
	}

	@Override
	public String getSelectionTitle(String classifier) {
		if (LicPackage.eINSTANCE.getProductLine().getName().equals(classifier)) {
			return "Select Product Line";
		}
		if (LicPackage.eINSTANCE.getProduct().getName().equals(classifier)) {
			return "Select Product";
		}
		if (LicPackage.eINSTANCE.getProductVersion().getName().equals(classifier)) {
			return "Select Product Version";
		}
		if (LicPackage.eINSTANCE.getProductVersionFeature().getName().equals(classifier)) {
			return "Select Product Version Feature";
		}
		return null;
	}

	@Override
	public Iterable<?> getSelectionInput(String classifier) {
		if (registry == null) {
			return Collections.emptyList();
		}
		if (LicPackage.eINSTANCE.getProductLine().getName().equals(classifier)) {
			return registry.getProductLines();
		}
		if (LicPackage.eINSTANCE.getProduct().getName().equals(classifier)) {
			return registry.getProducts();
		}
		if (LicPackage.eINSTANCE.getProductVersion().getName().equals(classifier)) {
			return registry.getProductVersions();
		}
		if (LicPackage.eINSTANCE.getProductVersionFeature().getName().equals(classifier)) {
			return registry.getProductVersionFeatures();
		}
		return Collections.emptyList();
	}

}