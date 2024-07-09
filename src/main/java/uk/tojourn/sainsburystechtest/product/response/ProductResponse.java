package uk.tojourn.sainsburystechtest.product.response;

public record ProductResponse( int id,
                               String name,
                               String category,
                               double price,
                               int stock,
                               String description) {
}
