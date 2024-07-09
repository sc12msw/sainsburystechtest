package uk.tojourn.sainsburystechtest.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.tojourn.sainsburystechtest.product.model.Category;

import java.util.List;

@Service
public class CategoryService {

    @Value("${api.categories.url}")
    private String categoriesUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CategoryService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<Category> fetchCategories() throws Exception {
        String categoriesResponse = restTemplate.getForObject(categoriesUrl, String.class);
        return objectMapper.readValue(categoriesResponse, new TypeReference<>() {
        });
    }
}

