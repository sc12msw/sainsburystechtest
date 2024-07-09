package uk.tojourn.sainsburystechtest.product.service;

import org.springframework.stereotype.Service;
import uk.tojourn.sainsburystechtest.product.exception.ProductEmptyException;
import uk.tojourn.sainsburystechtest.product.model.Category;
import uk.tojourn.sainsburystechtest.product.model.Product;
import uk.tojourn.sainsburystechtest.product.model.ProductWithCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static uk.tojourn.sainsburystechtest.product.Constants.NO_CATEGORY;
import static uk.tojourn.sainsburystechtest.product.Constants.NO_PRODUCT;

@Service
public class ProductWithCategoryService {

    public List<ProductWithCategory> mergeData(List<Product> products, List<Category> categories) throws ProductEmptyException {
        if (products.isEmpty()) {
            throw new ProductEmptyException(NO_PRODUCT);
        }
        if (categories == null) {
            return products.stream().map(this::handleNullCategory).toList();
        }

        // Convert categories list to a map for easier lookup
        Map<String, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::id, category -> category));
        // Merge the data
        return products.stream().map(product -> {
            Category category = categoryMap.get(product.id());

            // Make sure we can still return if some categories are missing
            if (category == null) {
                return handleNullCategory(product);
            }
            return new ProductWithCategory(product.id(), product.name(), category.name(), product.price(), product.stock(), category.description());
        }).toList();
    }

    private ProductWithCategory handleNullCategory(Product product){
        return new ProductWithCategory(product.id(), product.name(), NO_CATEGORY, product.price(), product.stock(), NO_CATEGORY);
    }
}
