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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import ru.arsysop.passage.loc.edit.ProductDomainRegistry;

public class ProductDomainRegistryTracker extends EContentAdapter {
	
	private final ProductDomainRegistry registry;
	
	public ProductDomainRegistryTracker(ProductDomainRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
	}

}
