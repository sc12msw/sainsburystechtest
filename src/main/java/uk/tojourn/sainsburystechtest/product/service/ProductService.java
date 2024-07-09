package uk.tojourn.sainsburystechtest.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.tojourn.sainsburystechtest.product.exception.ProductsApiException;
import uk.tojourn.sainsburystechtest.product.model.Product;

import java.util.List;

import static uk.tojourn.sainsburystechtest.product.Constants.PRODUCT_FAILURE;

@Service
public class ProductService {

    @Value("${api.products.url}")
    private String productsUrl;

    //Could be replaced by a repository pattern here
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public ProductService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Product> fetchProducts() throws ProductsApiException {
        try {
            String productsResponse = restTemplate.getForObject(productsUrl, String.class);
            return objectMapper.readValue(productsResponse, new TypeReference<>() {});
        }catch (Exception e){
            throw new ProductsApiException(PRODUCT_FAILURE, e);
        }
    }
}
