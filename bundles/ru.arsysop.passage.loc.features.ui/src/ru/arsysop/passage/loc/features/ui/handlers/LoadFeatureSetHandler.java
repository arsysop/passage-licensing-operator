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
package ru.arsysop.passage.loc.features.ui.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.registry.FeaturesRegistry;
import ru.arsysop.passage.loc.features.ui.FeaturesUi;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class LoadFeatureSetHandler {

	@Execute
	public void execute(Shell shell, IEclipseContext eclipseContext, MWindow window) {
		String domain = FeaturesRegistry.DOMAIN_NAME;
		String perspectiveId = FeaturesUi.PERSPECTIVE_MAIN;
		LocWokbench.loadDomainResource(eclipseContext, domain, shell, perspectiveId, window);
	}


}