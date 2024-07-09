package uk.tojourn.sainsburystechtest.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import uk.tojourn.sainsburystechtest.product.exception.ProductsApiException;
import uk.tojourn.sainsburystechtest.product.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static uk.tojourn.sainsburystechtest.product.Constants.PRODUCT_FAILURE;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private ProductService productService;

    private final String productsUrl = "http://localhost:8500/api/products";

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    // Need to use captors for mockito to handle the type reference type
    private ArgumentCaptor<TypeReference<List<Product>>> typeReferenceCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(restTemplate, objectMapper);
        // Inject the property manually
        ReflectionTestUtils.setField(productService, "productsUrl", productsUrl);
    }

    @Test
    void fetchProducts_Success() throws Exception, ProductsApiException {
        String jsonResponse = "[{\"id\":\"1\",\"name\":\"Apple\",\"price\":0.5,\"stock\":150}," +
                "{\"id\":\"2\",\"name\":\"Milk\",\"price\":1.2,\"stock\":50}]";
        List<Product> expectedProducts = List.of(
                new Product("1", "Apple", 0.5, 150),
                new Product("2", "Milk", 1.2, 50)
        );

        when(restTemplate.getForObject(productsUrl, String.class)).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.fetchProducts();

        assertEquals(expectedProducts, actualProducts);

        verify(restTemplate, times(1)).getForObject(productsUrl, String.class);
        verify(objectMapper, times(1)).readValue(stringCaptor.capture(), typeReferenceCaptor.capture());

        assertEquals(jsonResponse, stringCaptor.getValue());
    }

    @Test
    void fetchProducts_ApiException() throws Exception {
        when(restTemplate.getForObject(productsUrl, String.class)).thenThrow(new RuntimeException("API error"));

        ProductsApiException exception = assertThrows(ProductsApiException.class, () ->
                productService.fetchProducts()
        );

        assertEquals(PRODUCT_FAILURE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("API error", exception.getCause().getMessage());
        verify(restTemplate, times(1)).getForObject(productsUrl, String.class);
        verify(objectMapper, times(0)).readValue(anyString(), any(TypeReference.class));
    }
}