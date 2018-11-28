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
package ru.arsysop.passage.loc.products.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.workbench.wizards.CreateFileWizard;

public class ProductCreateHandler {
	
	@Execute
	public void execute(Shell shell, ProductDomainRegistry registry) {
		EClass eClass = LicPackage.Literals.PRODUCT;
		WizardDialog dialog = new WizardDialog(shell, new CreateFileWizard(registry, eClass));
		dialog.create();
		dialog.setTitle("Product Line");
		dialog.setMessage("Please specify a file name to store product data");;
		dialog.getShell().setText("New Product Line");
		dialog.open();
	}
}