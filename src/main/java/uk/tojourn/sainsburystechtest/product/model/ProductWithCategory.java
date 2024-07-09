package uk.tojourn.sainsburystechtest.product.model;

public record ProductWithCategory(String id, String name, String category, double price, int stock, String description) {
}
