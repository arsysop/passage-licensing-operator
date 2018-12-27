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
package ru.arsysop.passage.loc.dashboard;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import ru.arsysop.passage.lic.base.ui.LicensingImages;
import ru.arsysop.passage.lic.model.meta.LicPackage;
import ru.arsysop.passage.loc.edit.FeatureDomainRegistry;
import ru.arsysop.passage.loc.edit.LicenseDomainRegistry;
import ru.arsysop.passage.loc.edit.ProductDomainRegistry;
import ru.arsysop.passage.loc.edit.UserDomainRegistry;

public class DefaultDashboardAdvisor implements DashboardAdvisor {

	private LicensingImages licensingImages;

	private DashboardBlock featureSets;
	private DashboardBlock features;
	private DashboardBlock featureVersions;

	private DashboardBlock productLines;
	private DashboardBlock products;
	private DashboardBlock productVersions;
	private DashboardBlock productVersionFeatures;

	private DashboardBlock userOrigins;
	private DashboardBlock users;

	private DashboardBlock licensePacks;

	@Override
	public void init(IEclipseContext context) {
		licensingImages = context.get(LicensingImages.class);
	}

	@Override
	public void createHeaderInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		label.setText("Licensing data overview");
	}

	@Override
	public void createFeatureInfo(Composite parent, FeatureDomainRegistry featureRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(5).create());
		group.setText("Features");
		
		featureSets = createFeatureSetBlock(group);
		features = createFeatureBlock(group);
		featureVersions = createFeatureVersionBlock(group);
		
		updateFeatureInfo(featureRegistry);
	}

	protected DashboardBlock createFeatureSetBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Feature Sets:";
		Image image = getImage(LicPackage.eINSTANCE.getFeatureSet());
		block.createControl(parent, label, image);
		String info = "You have %s Feature Set(s) defined.\nUse it define the Features";
		String warning = "You have no Feature Sets defined.\nPlease create or load Feature Set definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardBlock createFeatureBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Features:";
		Image image = getImage(LicPackage.eINSTANCE.getFeature());
		block.createControl(parent, label, image);
		String info = "You have %s Feature(s) defined.\nUse it define the Feature Version(s)";
		String warning = "You have no Features defined.\nPlease create it for the Feature Set(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardBlock createFeatureVersionBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Feature Versions:";
		Image image = getImage(LicPackage.eINSTANCE.getFeatureVersion());
		block.createControl(parent, label, image);
		String info = "You have %s Feature Version(s) defined.\nUse it define the Product Version(s)";
		String warning = "You have no Feature Versions defined.\nPlease create it for the Feature(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateFeatureInfo(FeatureDomainRegistry featureRegistry) {
		featureSets.update(featureRegistry.getFeatureSets());
		features.update(featureRegistry.getFeatures());
		featureVersions.update(featureRegistry.getFeatureVersions());
	}

	@Override
	public void createProductInfo(Composite parent, ProductDomainRegistry productRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(5).create());
		group.setText("Products");
		
		productLines = createProductLineBlock(group);
		products = createProductBlock(group);
		productVersions = createProductVersionBlock(group);
		productVersionFeatures = createProductVersionFeatureBlock(group);

		updateProductInfo(productRegistry);
	}

	protected DashboardBlock createProductLineBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Product Lines:";
		Image image = getImage(LicPackage.eINSTANCE.getProductLine());
		block.createControl(parent, label, image);
		String info = "You have %s Product Line(s) defined.\nUse it define the Products";
		String warning = "You have no Product Lines defined.\nPlease create or load Product Line definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardBlock createProductBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Products:";
		Image image = getImage(LicPackage.eINSTANCE.getProduct());
		block.createControl(parent, label, image);
		String info = "You have %s Product(s) defined.\nUse it define the Product Versions";
		String warning = "You have no Products defined.\nPlease create it for the Product Line(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardBlock createProductVersionBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Product Versions:";
		Image image = getImage(LicPackage.eINSTANCE.getProductVersion());
		block.createControl(parent, label, image);
		String info = "You have %s Product Version (s) defined.\nUse it define the Product Version Features";
		String warning = "You have no Product Versions defined.\nPlease create it for the Product(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardBlock createProductVersionFeatureBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Product Features:";
		Image image = getImage(LicPackage.eINSTANCE.getProductVersionFeature());
		block.createControl(parent, label, image);
		String info = "You have %s Product Version Feature(s) defined.\nUse it define License Grants";
		String warning = "You have no Product Version Features defined.\nPlease create it for the Product Verion(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateProductInfo(ProductDomainRegistry productRegistry) {
		productLines.update(productRegistry.getProductLines());
		products.update(productRegistry.getProducts());
		productVersions.update(productRegistry.getProductVersions());
		productVersionFeatures.update(productRegistry.getProductVersionFeatures());
	}

	@Override
	public void createUserInfo(Composite parent, UserDomainRegistry userRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(5).create());
		group.setText("Users");
		
		userOrigins = createUserOriginBlock(group);
		users = createUserBlock(group);

		updateUserInfo(userRegistry);
	}

	protected DashboardBlock createUserOriginBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "User Origins:";
		Image image = getImage(LicPackage.eINSTANCE.getUserOrigin());
		block.createControl(parent, label, image);
		String info = "You have %s User Origin(s) defined.\nUse it define the Users";
		String warning = "You have no User Origins defined.\nPlease create or load User Origin definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	protected DashboardBlock createUserBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "Users:";
		Image image = getImage(LicPackage.eINSTANCE.getUser());
		block.createControl(parent, label, image);
		String info = "You have %s User(s) defined.\nUse it define the License Packs";
		String warning = "You have no Users defined.\nPlease create it for the User Origin(s)";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateUserInfo(UserDomainRegistry userRegistry) {
		userOrigins.update(userRegistry.getUserOrigins());
		users.update(userRegistry.getUsers());
	}

	@Override
	public void createLicenseInfo(Composite parent, LicenseDomainRegistry licenseRegistry) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(5).create());
		group.setText("Licenses");

		licensePacks = createLicensePackBlock(group);
		updateLicenseInfo(licenseRegistry);
	}

	protected DashboardBlock createLicensePackBlock(Composite parent) {
		DashboardBlock block = new DashboardBlock();
		String label = "License Packs:";
		Image image = getImage(LicPackage.eINSTANCE.getLicensePack());
		block.createControl(parent, label, image);
		String info = "You have %s License Pack(s) defined.\nUse it define the License Grants";
		String warning = "You have no License Packs defined.\nPlease create or load License Pack definitions";
		block.setInfo(info);
		block.setWarning(warning);
		return block;
	}

	@Override
	public void updateLicenseInfo(LicenseDomainRegistry licenseRegistry) {
		licensePacks.update(licenseRegistry.getLicensePacks());
	}

	@Override
	public void createFooterInfo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		label.setText("Licensing data summary");
	}

	protected Image getImage(EClass eClass) {
		return licensingImages.getImage(eClass.getName());
	}

	@Override
	public void dispose(IEclipseContext context) {
		licensingImages = null;
	}

}
