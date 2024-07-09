package uk.tojourn.sainsburystechtest.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.tojourn.sainsburystechtest.product.exception.CategoryApiException;
import uk.tojourn.sainsburystechtest.product.model.Category;

import java.util.List;

import static uk.tojourn.sainsburystechtest.product.Constants.CATEGORY_FAILURE;

@Service
public class CategoryService {

    @Value("${api.categories.url}")
    private String categoriesUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CategoryService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Category> fetchCategories() throws CategoryApiException {
        try {
            String categoriesResponse = restTemplate.getForObject(categoriesUrl, String.class);
            return objectMapper.readValue(categoriesResponse, new TypeReference<>() {
            });
        } catch (Exception e){
            throw new CategoryApiException(CATEGORY_FAILURE, e);
        }
    }
}

