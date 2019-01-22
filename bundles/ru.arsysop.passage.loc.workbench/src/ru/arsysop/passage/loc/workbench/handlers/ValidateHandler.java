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
package ru.arsysop.passage.loc.workbench.handlers;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.emf.edit.ComposedAdapterFactoryProvider;
import ru.arsysop.passage.lic.emf.edit.LabeledDiagnostician;

public class ValidateHandler {

	@Execute
	public void execute(ESelectionService selectionService, Shell shell, IEclipseContext context) {
		ComposedAdapterFactoryProvider provider = context.get(ComposedAdapterFactoryProvider.class);
		ComposedAdapterFactory adapterFactory = provider.getComposedAdapterFactory();
		Object selection = selectionService.getSelection();
		if (selection instanceof EObject) {
			EObject eObject = (EObject) selection;
			final AdapterFactory adapterFactory1 = adapterFactory;
			Diagnostician diagnostician = new LabeledDiagnostician(adapterFactory1);
			Diagnostic validate = diagnostician.validate(eObject);
			handleDiagnostic(validate, shell);
		}
	}

	@CanExecute
	public boolean canExecute(ESelectionService selectionService) {
		Object selection = selectionService.getSelection();
		if (selection instanceof EObject) {
			return true;
		}
		return false;
	}

	protected void handleDiagnostic(Diagnostic diagnostic, Shell shell) {
		int severity = diagnostic.getSeverity();
		String title = null;
		String message = null;

		if (severity == Diagnostic.ERROR || severity == Diagnostic.WARNING) {
			title = EMFEditUIPlugin.INSTANCE.getString("_UI_ValidationProblems_title"); //$NON-NLS-1$
			message = EMFEditUIPlugin.INSTANCE.getString("_UI_ValidationProblems_message"); //$NON-NLS-1$
		} else {
			title = EMFEditUIPlugin.INSTANCE.getString("_UI_ValidationResults_title"); //$NON-NLS-1$
			message = EMFEditUIPlugin.INSTANCE.getString(
					severity == Diagnostic.OK ? "_UI_ValidationOK_message" : "_UI_ValidationResults_message"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (diagnostic.getSeverity() == Diagnostic.OK) {
			MessageDialog.openInformation(shell, title, message);
		} else {
			IStatus status = BasicDiagnostic.toIStatus(diagnostic);
			ErrorDialog.openError(shell, title, message, status);
		}
	}
}
