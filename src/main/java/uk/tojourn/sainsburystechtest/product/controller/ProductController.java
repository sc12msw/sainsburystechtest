package uk.tojourn.sainsburystechtest.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tojourn.sainsburystechtest.product.exception.CategoryApiException;
import uk.tojourn.sainsburystechtest.product.exception.ProductEmptyException;
import uk.tojourn.sainsburystechtest.product.exception.ProductsApiException;
import uk.tojourn.sainsburystechtest.product.model.Category;
import uk.tojourn.sainsburystechtest.product.model.Product;
import uk.tojourn.sainsburystechtest.product.model.ProductWithCategory;
import uk.tojourn.sainsburystechtest.product.service.CategoryService;
import uk.tojourn.sainsburystechtest.product.service.ProductService;
import uk.tojourn.sainsburystechtest.product.service.ProductWithCategoryService;

import java.util.ArrayList;
import java.util.List;

import static uk.tojourn.sainsburystechtest.product.Constants.NO_PRODUCT;


@RestController
public class ProductController {

    @Value("${api.products.url}")
    private String productsUrl;

    @Value("${api.categories.url}")
    private String categoriesUrl;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductWithCategoryService productWithCategoryService;

    public ProductController(final CategoryService categoryService, final ProductService productService, final ProductWithCategoryService productWithCategoryService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.productWithCategoryService = productWithCategoryService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductWithCategory>> getMergedData() {
        List<Product> products = new ArrayList<>();
        List<Category> categories;
        try {
            // Fetch products and categories
           products = productService.fetchProducts();
           categories = categoryService.fetchCategories();
            return ResponseEntity.ok(productWithCategoryService.mergeData(products,categories));

            //TODO create exception handler class to keep this logic separate but fine for tech test example
        } catch (ProductsApiException e){
            // TODO increment failure metric
            logger.error("Error while trying to fetch product data so it cannot be merged", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (CategoryApiException e){
            //TODO increment partial success metric
            //TODO add some response body and code explaining that not all the data was returned
            return handlePartialSuccess(products);
        }
        catch(ProductEmptyException e){
            logger.error(NO_PRODUCT + " cannot merge");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e) {
            logger.error("Error occurred while merging product data, cannot recover", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<List<ProductWithCategory>> handlePartialSuccess(List<Product> products){
        try {
            return ResponseEntity.ok(productWithCategoryService.mergeData(products, null));
        }catch (ProductEmptyException productEmptyException){
            logger.error(NO_PRODUCT + " cannot merge");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}

