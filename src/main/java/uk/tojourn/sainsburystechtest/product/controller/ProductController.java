package uk.tojourn.sainsburystechtest.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tojourn.sainsburystechtest.product.model.Category;
import uk.tojourn.sainsburystechtest.product.model.Product;
import uk.tojourn.sainsburystechtest.product.response.ProductResponse;
import uk.tojourn.sainsburystechtest.product.service.CategoryService;
import uk.tojourn.sainsburystechtest.product.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Value("${api.products.url}")
    private String productsUrl;

    @Value("${api.categories.url}")
    private String categoriesUrl;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductController(final CategoryService categoryService, final ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getMergedData() throws Exception {
        try {
            // Fetch products and categories
            List<Product> products = productService.fetchProducts();
            List<Category> categories = categoryService.fetchCategories();

            // Convert categories list to a map for easier lookup
            Map<Integer, Category> categoryMap = categories.stream()
                    .collect(Collectors.toMap(Category::id, category -> category));

            // Merge the data
            List<ProductResponse> mergedData = products.stream().map(product -> {

                Category category = categoryMap.get(product.id());
                if (category == null) {
                    logger.error("Category not found for product id {}", product.id());
                    throw new RuntimeException("Category not found for product id " + product.id());
                }

                return new ProductResponse(product.id(), product.name(), product.category(), product.price(), product.stock(), category.description());
            }).collect(Collectors.toList());

            return ResponseEntity.ok(mergedData);
        } catch (Exception e) {
            logger.error("Error occurred while merging product data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

