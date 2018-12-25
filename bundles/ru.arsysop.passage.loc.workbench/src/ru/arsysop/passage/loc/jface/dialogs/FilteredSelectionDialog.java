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
package ru.arsysop.passage.loc.jface.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ru.arsysop.passage.lic.base.ui.LicensingImages;

public class FilteredSelectionDialog extends ObjectSelectionStatusDialog {

	private static final int DIALOG_HEIGHT = 500;
	private static final int DIALOG_WIDTH = 400;
	private final boolean multi;
	private Text filteringField;
	private Text resultingField;
	private TableViewer tableViewItems;
	private final List<Object> input = new ArrayList<>();

	private LabelProvider labelProvider = new LabelProvider();
	private ViewerSearchFilter filter;

	public FilteredSelectionDialog(Shell parent, LicensingImages licensingImages, boolean multi) {
		super(parent, licensingImages);
		this.multi = multi;

	}

	public void setInput(Iterable<?> objects) {
		input.clear();
		objects.forEach(input::add);

	}

	public void setFilter(ViewerSearchFilter filter) {
		this.filter = filter;
	}

	public void setLabelProvider(LabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	@Override
	protected void computeResult() {
		List<?> selectedElements = tableViewItems.getStructuredSelection().toList();
		setResult(selectedElements);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		Composite content = new Composite(dialogArea, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lblFilteringField = new Label(content, SWT.NONE);
		{
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
			lblFilteringField.setLayoutData(data);
			lblFilteringField.setText("Matcher:");
		}

		filteringField = new Text(content, SWT.BORDER);
		{
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
			filteringField.setLayoutData(data);

		}

		filteringField.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (filter != null) {
					filter.setFilteringText(getfilteringField());
					tableViewItems.refresh();
					Table table = tableViewItems.getTable();
					if (table.getItemCount() > 0) {
						TableItem item = table.getItem(0);
						if (item != null) {
							resultingField.setText(item.getText());
							table.setSelection(item);
						}
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		content.setLayout(layout);

		Label lblListItems = new Label(content, SWT.NONE);
		{
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
			lblListItems.setLayoutData(data);
			lblListItems.setText("Matching items:");
		}

		tableViewItems = new TableViewer(content, (multi ? SWT.MULTI : SWT.SINGLE) | SWT.BORDER | SWT.V_SCROLL);
		tableViewItems.setContentProvider(ArrayContentProvider.getInstance());
		tableViewItems.setLabelProvider(labelProvider);
		tableViewItems.setInput(input);
		tableViewItems.setSelection(new StructuredSelection(getInitial().toArray()));
		tableViewItems.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = tableViewItems.getTable().getSelectionIndex();
				TableItem item = tableViewItems.getTable().getItem(selectionIndex);
				if (item != null) {
					resultingField.setText(item.getText());
				}
			}
		});
		if (filter != null) {
			tableViewItems.setFilters(filter);
		}

		GridData gd = new GridData(GridData.FILL_BOTH);
		applyDialogFont(tableViewItems.getTable());
		gd.heightHint = tableViewItems.getTable().getItemHeight() * 15;
		tableViewItems.getTable().setLayoutData(gd);
		tableViewItems.addDoubleClickListener(event -> handleDoubleClick());

		resultingField = new Text(content, SWT.BORDER);
		resultingField.setEditable(false);
		{
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
			resultingField.setLayoutData(data);

		}

		applyDialogFont(content);
		return dialogArea;
	}

	protected void handleDoubleClick() {
		okPressed();
	}

	public String getfilteringField() {
		return filteringField.getText();
	}

	@Override
	protected void configureShell(Shell shell) {
		shell.setMinimumSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		super.configureShell(shell);
	}
}
