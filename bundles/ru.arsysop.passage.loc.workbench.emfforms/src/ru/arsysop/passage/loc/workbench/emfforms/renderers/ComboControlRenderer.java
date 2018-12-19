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
package ru.arsysop.passage.loc.workbench.emfforms.renderers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
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
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public abstract class ComboControlRenderer extends SimpleControlSWTControlSWTRenderer {

	private Combo combo;

	@Inject
	public ComboControlRenderer(VControl vElement, ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding, EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider) {
		super(vElement, viewContext, reportService, emfFormsDatabinding, emfFormsLabelProvider, vtViewTemplateProvider);

	}

	@Override
	protected Binding[] createBindings(Control control) throws DatabindingFailedException {
		DataBindingContext context = getDataBindingContext();
		final Binding binding = context.bindValue(WidgetProperties.selection().observe(control),
				getModelValue(), withPreSetValidation(new UpdateValueStrategy()), null);
		return new Binding[] { binding };
	}

	@Override
	protected Control createSWTControl(Composite parent) {
		combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).span(2,1).applyTo(combo);
		List<String> definedValues = getDefinedValues();
		String[] array = (String[]) definedValues.toArray(new String[definedValues.size()]);
		combo.setItems(array);
		String currentValue = getCurrentValue();
		if (currentValue == null || currentValue.isEmpty()) {
			combo.select(0);
		} else {
			int curIndex = definedValues.indexOf(currentValue);
			combo.select(curIndex);
		}

		combo.setBackground(Display.getDefault().getSystemColor(SWT.BACKGROUND));
		return combo;
	}

	@Override
	protected void dispose() {
		if (combo != null) {
			combo.dispose();
		}
		super.dispose();
	}

	protected abstract List<String> getDefinedValues();
	
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

}
