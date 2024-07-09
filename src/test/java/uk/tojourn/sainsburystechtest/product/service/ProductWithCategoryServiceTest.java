package uk.tojourn.sainsburystechtest.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tojourn.sainsburystechtest.product.exception.ProductEmptyException;
import uk.tojourn.sainsburystechtest.product.model.Category;
import uk.tojourn.sainsburystechtest.product.model.Product;
import uk.tojourn.sainsburystechtest.product.model.ProductWithCategory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static uk.tojourn.sainsburystechtest.product.Constants.NO_CATEGORY;
import static uk.tojourn.sainsburystechtest.product.Constants.NO_PRODUCT;

class ProductWithCategoryServiceTest {

    private ProductWithCategoryService productWithCategoryService;

    @BeforeEach
    void setUp() {
        productWithCategoryService = new ProductWithCategoryService();
    }

    @Test
    void mergeData_Success() throws ProductEmptyException {
        List<Product> products = Arrays.asList(
                new Product("1", "Apple", 0.5, 150),
                new Product("2", "Milk", 1.2, 50)
        );

        List<Category> categories = Arrays.asList(
                new Category("1", "Fruit", "Fresh fruits"),
                new Category("2", "Dairy", "Milk and dairy products")
        );

        List<ProductWithCategory> result = productWithCategoryService.mergeData(products, categories);

        assertEquals(2, result.size());

        ProductWithCategory apple = result.get(0);
        assertEquals("Apple", apple.name());
        assertEquals("Fruit", apple.category());
        assertEquals("Fresh fruits", apple.description());

        ProductWithCategory milk = result.get(1);
        assertEquals("Milk", milk.name());
        assertEquals("Dairy", milk.category());
        assertEquals("Milk and dairy products", milk.description());
    }

    @Test
    void mergeData_CategoryNotFound() throws ProductEmptyException {
        List<Product> products = Arrays.asList(
                new Product("1", "Apple", 0.5, 150),
                new Product("2", "Milk", 1.2, 50)
        );

        List<Category> categories = Collections.singletonList(
                new Category("1", "Fruit", "Fresh fruits")
                // Missing category for product with id "2"
        );

        List<ProductWithCategory> result = productWithCategoryService.mergeData(products, categories);

        assertEquals(2, result.size());

        ProductWithCategory apple = result.get(0);
        assertEquals("Apple", apple.name());
        assertEquals("Fruit", apple.category());
        assertEquals("Fresh fruits", apple.description());

        ProductWithCategory milk = result.get(1);
        assertEquals("Milk", milk.name());
        assertEquals(NO_CATEGORY, milk.category());
        assertEquals(NO_CATEGORY, milk.description());
    }

    @Test
    void mergeData_EmptyProducts() {
        List<Product> products = Collections.emptyList();
        List<Category> categories = Collections.singletonList(
                new Category("1", "Fruit", "Fresh fruits")
        );

        ProductEmptyException exception = assertThrows(ProductEmptyException.class, () ->
                productWithCategoryService.mergeData(products, categories)
        );

        assertEquals(NO_PRODUCT, exception.getMessage());
    }

    @Test
    void mergeData_NullCategories() throws ProductEmptyException {
        List<Product> products = Arrays.asList(
                new Product("1", "Apple", 0.5, 150),
                new Product("2", "Milk", 1.2, 50)
        );

        List<ProductWithCategory> result = productWithCategoryService.mergeData(products, null);

        assertEquals(2, result.size());

        ProductWithCategory apple = result.get(0);
        assertEquals("Apple", apple.name());
        assertEquals(NO_CATEGORY, apple.category());
        assertEquals(NO_CATEGORY, apple.description());

        ProductWithCategory milk = result.get(1);
        assertEquals("Milk", milk.name());
        assertEquals(NO_CATEGORY, milk.category());
        assertEquals(NO_CATEGORY, milk.description());
    }
}