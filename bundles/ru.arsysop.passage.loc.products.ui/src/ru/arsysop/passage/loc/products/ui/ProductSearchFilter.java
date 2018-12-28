package ru.arsysop.passage.loc.products.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.loc.jface.dialogs.ViewerSearchFilter;

public class ProductSearchFilter extends ViewerSearchFilter<Product> {

	public ProductSearchFilter() {
		super(Product.class);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchText.isEmpty()) {
			return true;
		}
		if (element instanceof Product) {
			String name = getNotNullValue(((Product) element).getName());
			String identifier = getNotNullValue(((Product) element).getIdentifier());
			Pattern pattern = getSearchPattern();
			Matcher matcherByName = pattern.matcher(name);
			Matcher matcherById = pattern.matcher(identifier);
			if (matcherByName.matches() || matcherById.matches()) {
				return true;
			}
			
		}

		return false;
	}
}
