/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.loc.edit.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.passage.lic.registry.FeaturesRegistry;
import org.eclipse.passage.lic.registry.LicensesRegistry;
import org.eclipse.passage.lic.registry.ProductsRegistry;
import org.eclipse.passage.lic.registry.UsersRegistry;
import org.eclipse.passage.loc.workbench.LocWokbench;
import org.eclipse.swt.widgets.Shell;

public class DomainRegistryCreateHandler {

	private static final String PERSPECTIVE_DASHBOARD_ID = "org.eclipse.passage.loc.dashboard.perspective.main";
	private static final String REGISTRY_RESOURCE_CREATE = "org.eclipse.passage.loc.edit.ui.commandparameter.domain.resource.create";
	private static final String REGISTRY_RESOURCE_CREATE_FEATURE = REGISTRY_RESOURCE_CREATE + ".feature";
	private static final String REGISTRY_RESOURCE_CREATE_PRODUCT = REGISTRY_RESOURCE_CREATE + ".product";
	private static final String REGISTRY_RESOURCE_CREATE_USER = REGISTRY_RESOURCE_CREATE + ".user";
	private static final String REGISTRY_RESOURCE_CREATE_LICENSE = REGISTRY_RESOURCE_CREATE + ".license";

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IEclipseContext context,
			@Named(REGISTRY_RESOURCE_CREATE) String domainRegistryId) {

		String domain = null;
		switch (domainRegistryId) {
		case REGISTRY_RESOURCE_CREATE_FEATURE:
			domain = FeaturesRegistry.DOMAIN_NAME;
			break;
		case REGISTRY_RESOURCE_CREATE_PRODUCT:
			domain = ProductsRegistry.DOMAIN_NAME;
			break;
		case REGISTRY_RESOURCE_CREATE_USER:
			domain = UsersRegistry.DOMAIN_NAME;
			break;
		case REGISTRY_RESOURCE_CREATE_LICENSE:
			domain = LicensesRegistry.DOMAIN_NAME;
			break;
		}

		if (domain != null) {
			LocWokbench.createDomainResource(context, domain, PERSPECTIVE_DASHBOARD_ID);
		}
	}
}
