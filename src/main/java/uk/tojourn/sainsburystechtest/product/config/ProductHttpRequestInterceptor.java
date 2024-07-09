package uk.tojourn.sainsburystechtest.product.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ProductHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequestDetails(request);
        return execution.execute(request, body);
    }

    //Add some request logging for debugging
    private void logRequestDetails(HttpRequest request) {
        LOGGER.debug("Headers: {}", request.getHeaders());
        LOGGER.debug("Request Method: {}", request.getMethod());
        LOGGER.debug("Request URI: {}", request.getURI());
    }
}

