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
package ru.arsysop.passage.licensing.operator.workbench.dialogs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.licensing.operator.workbench.dialogs.AbstractDialog;

public class AboutDialog extends AbstractDialog {

	private static final String ABOUT_TEXT = "%aboutText";
	private static final String ABOUT_IMAGE = "%aboutImage";
	private static final String ABOUT_DIALOG_TITLE = "%product.name";

	private static final String PRODUCT_BUNDLE_URL = "platform:/plugin/ru.arsysop.passage.loc.workbench";
	private static final String PRODUCT_BUNDLE_ABOUT_IMAGE = "%s//%s";
	private Image productLogo;

	@Inject
	private TranslationService translations;

	public AboutDialog(Shell parentShell, IEclipseContext context) {
		super(parentShell);
		setShellStyle(SWT.TITLE);
		if (translations == null) {
			translations = context.get(TranslationService.class);
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(translations.translate(ABOUT_DIALOG_TITLE, PRODUCT_BUNDLE_URL));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout workLayout = getLayoutDialogArea();
		Composite base = new Composite(parent, SWT.BORDER);
		base.setLayout(workLayout);
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		String productImg = translations.translate(ABOUT_IMAGE, PRODUCT_BUNDLE_URL);

		if (productImg != null) {
			URL url = getUrl(productImg);
			if (url != null) {
				ImageDescriptor imgDescriptor = ImageDescriptor.createFromURL(url);
				if (imgDescriptor != null) {
					productLogo = imgDescriptor.createImage();
				}
			}
		}

		GridData lblGridData = new GridData(SWT.FILL, SWT.FILL, true, true);

		Label lblProductLogo = new Label(base, SWT.BORDER);
		if (productLogo != null) {
			lblProductLogo.setImage(productLogo);
		}
		lblProductLogo.setLayoutData(lblGridData);

		GridData txtData = new GridData(SWT.FILL, SWT.FILL, true, true);
		String productDesription = translations.translate(ABOUT_TEXT, PRODUCT_BUNDLE_URL);

		StyledText txtProductDescription = new StyledText(base, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		txtProductDescription.setLayoutData(txtData);
		txtProductDescription.setText(productDesription);
		txtProductDescription.setEditable(false);

		return parent;
	}

	private GridLayout getLayoutDialogArea() {
		GridLayout workLayout = new GridLayout(2, false);
		workLayout.marginHeight = 0;
		workLayout.marginWidth = 0;
		workLayout.verticalSpacing = 0;
		workLayout.horizontalSpacing = 5;

		return workLayout;
	}

	private URL getUrl(String productImg) {
		URL url = null;
		try {
			url = new URL(String.format(PRODUCT_BUNDLE_ABOUT_IMAGE, PRODUCT_BUNDLE_URL, productImg));
		} catch (MalformedURLException e) {
			Logger.getLogger(AbstractDialog.class.getName()).log(Level.INFO, e.getMessage(), e);
		}
		return url;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData btnData = new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1);
		Button btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		btnOk.setLayoutData(btnData);
	}

	@Override
	public boolean close() {
		if (productLogo != null) {
			productLogo.dispose();
			productLogo = null;
		}
		return super.close();
	}
}