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
package org.eclipse.passage.loc.workbench.emfforms;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.passage.lic.emf.edit.ClassifierInitializer;
import org.eclipse.passage.lic.emf.edit.DomainRegistryAccess;
import org.eclipse.passage.lic.emf.edit.EditingDomainRegistry;
import org.eclipse.passage.lic.jface.LicensingImages;
import org.eclipse.passage.loc.workbench.emfforms.wizards.CreateFormWizard;
import org.eclipse.swt.widgets.Shell;

public class LocWorkbenchEmfforms {

	public static final String BUNDLE_SYMBOLIC_NAME = "org.eclipse.passage.loc.workbench.emfforms"; //$NON-NLS-1$

	public static void createDomainContentObject(IEclipseContext context, String domain, String perspectiveId) {
		DomainRegistryAccess registryAccess = context.get(DomainRegistryAccess.class);
		LicensingImages images = context.get(LicensingImages.class);
	
		EditingDomainRegistry registry = registryAccess.getDomainRegistry(domain );
		ClassifierInitializer initializer = registryAccess.getClassifierInitializer(domain);
	
		EClass eClass = registry.getContentClassifier();
	
		Wizard wizard = new CreateFormWizard(context, domain, perspectiveId);
		Shell shell = context.get(Shell.class);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setTitle(initializer.newObjectTitle());
		dialog.setMessage(initializer.newFileMessage());
	
		Shell createdShell = dialog.getShell();
		createdShell.setText(initializer.newObjectMessage());
		createdShell.setImage(images.getImage(eClass.getName()));
	
		dialog.open();
	}
}
