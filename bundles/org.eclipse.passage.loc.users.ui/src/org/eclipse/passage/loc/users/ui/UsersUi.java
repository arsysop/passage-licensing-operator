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
package org.eclipse.passage.loc.users.ui;

import org.eclipse.passage.lic.jface.LicensingImages;
import org.eclipse.passage.lic.model.meta.LicPackage;
import org.eclipse.passage.lic.registry.UserDescriptor;
import org.eclipse.passage.loc.edit.UserDomainRegistry;
import org.eclipse.passage.loc.workbench.LocWokbench;
import org.eclipse.swt.widgets.Shell;

public class UsersUi {

	public static final String BUNDLE_SYMBOLIC_NAME = "org.eclipse.passage.loc.users.ui"; //$NON-NLS-1$

	public static final String PERSPECTIVE_MAIN = BUNDLE_SYMBOLIC_NAME + '.' + "perspective.main"; //$NON-NLS-1$

	public static UserDescriptor selectUserDescriptor(Shell shell, LicensingImages images, UserDomainRegistry registry,
			UserDescriptor initial) {
		String classifier = LicPackage.eINSTANCE.getUser().getName();
		String title = "Select User";
		Iterable<UserDescriptor> input = registry.getUsers();
		Class<UserDescriptor> clazz = UserDescriptor.class;
		return LocWokbench.selectClassifier(shell, images, registry, classifier, title, input, initial, clazz);
	}

}
