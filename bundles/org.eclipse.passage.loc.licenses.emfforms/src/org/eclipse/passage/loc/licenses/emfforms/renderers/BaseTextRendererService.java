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
package org.eclipse.passage.loc.licenses.emfforms.renderers;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.swt.core.AbstractSWTRenderer;
import org.eclipse.emfforms.spi.swt.core.di.EMFFormsDIRendererService;
import org.eclipse.passage.lic.model.meta.LicPackage;
import org.eclipse.passage.loc.workbench.emfforms.renderers.ValidatedTextRenderer;

public class BaseTextRendererService implements EMFFormsDIRendererService<VControl> {

	private EMFFormsDatabinding databindingService;
	private ReportService reportService;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.swt.core.EMFFormsRendererService#isApplicable(VElement,ViewModelContext)
	 */
	@Override
	public double isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		final VControl control = (VControl) vElement;
		if (control.getDomainModelReference() == null) {
			return NOT_APPLICABLE;
		}

		@SuppressWarnings("rawtypes")
		IValueProperty valueProperty;
		try {
			valueProperty = databindingService.getValueProperty(control.getDomainModelReference(),
					viewModelContext.getDomainModel());
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			return NOT_APPLICABLE;
		}
		Object valueType = valueProperty.getValueType();
		final EStructuralFeature eStructuralFeature = EStructuralFeature.class.cast(valueType);

		if (LicPackage.eINSTANCE.getProductLine_Identifier().equals(eStructuralFeature)
				|| LicPackage.eINSTANCE.getProduct_Identifier().equals(eStructuralFeature)
				|| LicPackage.eINSTANCE.getProduct_Name().equals(eStructuralFeature)
				|| LicPackage.eINSTANCE.getProductVersion_Version().equals(eStructuralFeature)
				|| LicPackage.eINSTANCE.getFeatureVersion_Version().equals(eStructuralFeature)
				) {
			return 10;
		}

		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emfforms.spi.swt.core.di.EMFFormsD
	 *      IRendererService#getRendererClass()
	 */
	@Override
	public Class<? extends AbstractSWTRenderer<VControl>> getRendererClass() {
		return ValidatedTextRenderer.class;
	}

	public void bindEMFFormsDatabinding(EMFFormsDatabinding databindingService) {
		this.databindingService = databindingService;
	}

	public void unbindEMFFormsDatabinding(EMFFormsDatabinding databindingService) {
		this.databindingService = null;
	}

	public void bindReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public void unbindReportService(ReportService reportService) {
		this.reportService = null;
	}
}
