package uk.tojourn.sainsburystechtest.product.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;

public class ProductRestTemplate  implements RestTemplateCustomizer {
    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.getInterceptors().add(new ProductHttpRequestInterceptor());
    }
}
