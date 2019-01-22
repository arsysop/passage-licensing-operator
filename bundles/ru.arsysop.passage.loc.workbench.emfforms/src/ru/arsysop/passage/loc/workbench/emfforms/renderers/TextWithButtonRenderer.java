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
package ru.arsysop.passage.loc.workbench.emfforms.renderers;

import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.jface.LicensingColors;
import ru.arsysop.passage.lic.jface.LicensingImages;
import ru.arsysop.passage.loc.jface.LocImages;

public abstract class TextWithButtonRenderer extends SimpleControlSWTControlSWTRenderer {

	protected Composite base;
	protected Text text;
	protected Button button;
	
	private final LicensingImages licensingImages;
	private final LicensingColors licensingColors;
	private final LocImages locImages;

	@Inject
	public TextWithButtonRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);
		this.licensingImages = viewContext.getService(LicensingImages.class);
		this.licensingColors = viewContext.getService(LicensingColors.class);
		this.locImages = viewContext.getService(LocImages.class);
	}
	
	public LicensingColors getLicensingColors() {
		return licensingColors;
	}
	
	public LicensingImages getLicensingImages() {
		return licensingImages;
	}
	
	public LocImages getLocImages() {
		return locImages;
	}

	@Override
	protected Binding[] createBindings(Control control) throws DatabindingFailedException {
		if (control instanceof Text) {
			ISWTObservableValue observe = WidgetProperties.text(SWT.Modify).observe(control);
			UpdateValueStrategy target2model = withPreSetValidation(new UpdateValueStrategy());
			final Binding binding = getDataBindingContext().bindValue(observe, getModelValue(),
					target2model, null);
			return new Binding[] { binding };
		}

		return new Binding[] {};
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		base = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		base.setLayout(layout);
		base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		base.setFont(parent.getFont());

		text = createText(base);
		button = createButton(base);
		return text;
	}

	protected Text createText(Composite parent) {
		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setText(getCurrentValue());
		text.setEditable(false);
		return text;
	}

	protected Button createButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Edit...");
		button.setImage(locImages.getImage(LocImages.IMG_TOOL_EDIT));
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		return button;
	}
	
	@Override
	protected void dispose() {
		if (base != null && !base.isDisposed()) {
			for (Control control : base.getChildren()) {
				if (control != null) {
					control.dispose();
				}
			}
		}
		super.dispose();
	}

	protected String getCurrentValue() {
		try {
			Object value = getModelValue().getValue();
			if (value instanceof String) {
				return (String) value;
			}
		} catch (DatabindingFailedException e) {
			getReportService().report(new DatabindingFailedReport(e));
		}
		return getUnsetText();
	}

	@Override
	protected void setValidationColor(Control control, Color validationColor) {
		if (control instanceof Text) {
			Text textControl = ((Text) control);
			if (textControl.getText().isEmpty()) {
				control.setBackground(licensingColors.getColor(LicensingColors.COLOR_VALIDATION_ERROR));
			} else {
				control.setBackground(licensingColors.getColor(LicensingColors.COLOR_VALIDATION_OK));
			}
		}
	}

}
