package uk.tojourn.sainsburystechtest.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.tojourn.sainsburystechtest.product.model.Product;

import java.util.List;

@Service
public class ProductService {

    @Value("${api.products.url}")
    private String productsUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ProductService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<Product> fetchProducts() throws Exception {
        String productsResponse = restTemplate.getForObject(productsUrl, String.class);
        return objectMapper.readValue(productsResponse, new TypeReference<>() {
        });
    }
}
