package uk.tojourn.sainsburystechtest.product.model;

public record Product(
         int id,
         String name,
         String category,
         double price,
         int stock) {
}
