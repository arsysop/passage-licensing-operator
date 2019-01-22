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
package ru.arsysop.passage.loc.licenses.ui.handlers;

import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

import ru.arsysop.passage.lic.model.api.LicensePack;
import ru.arsysop.passage.lic.runtime.io.StreamCodec;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.licenses.core.LicensesCore;

public class LicenseExportHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) LicensePack licensePack, IEclipseContext context) {
		ProductDomainRegistry productRegistry = context.get(ProductDomainRegistry.class);
		LicenseDomainRegistry licenseRegistry = context.get(LicenseDomainRegistry.class);
		StreamCodec streamCodec = context.get(StreamCodec.class);
		Shell shell = context.get(Shell.class);
		try {
			String exportLicense = LicensesCore.exportLicensePack(licensePack, productRegistry , licenseRegistry , streamCodec );
			String format = "License pack exported succesfully: \n\n %s \n";
			String message = String.format(format, exportLicense);
			MessageDialog.openInformation(shell , "License Pack Export", message);
		} catch (CoreException e) {
			IStatus status = e.getStatus();
			Bundle bundle = Platform.getBundle(status.getPlugin());
			Platform.getLog(bundle).log(status);
			ErrorDialog.openError(shell, "Error", "Error during license pack export", e.getStatus());
		}
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional LicensePack licensePack) {
		return licensePack != null;
	}

}