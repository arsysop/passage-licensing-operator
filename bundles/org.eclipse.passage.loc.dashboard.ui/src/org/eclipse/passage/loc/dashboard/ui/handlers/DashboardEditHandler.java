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
package org.eclipse.passage.loc.dashboard.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.passage.loc.dashboard.ui.DashboardUi;

public class DashboardEditHandler {

	@Execute
	public void execute(IEclipseContext eclipseContext,
			@Named(DashboardUi.COMMANDPARAMETER_EDIT_DOMAIN) String domain,
			@Named(DashboardUi.COMMANDPARAMETER_EDIT_CLASSIFIER) String classifier,
			@Named(DashboardUi.COMMANDPARAMETER_EDIT_PERSPECTIVE) String perspectiveId) {
		DashboardUi.editDomainResource(eclipseContext, domain, classifier, perspectiveId);
	}


	@CanExecute
	public boolean canExecute(@Named(DashboardUi.COMMANDPARAMETER_EDIT_DOMAIN) String domain) {
		return domain != null;
	}

}