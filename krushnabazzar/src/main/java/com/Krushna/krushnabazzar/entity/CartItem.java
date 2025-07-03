package com.Krushna.krushnabazzar.entity;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Shoppingcart shoppingcart;

    public CartItem() {}

    public CartItem(int id, Product product, int quantity, Shoppingcart shoppingcart) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.shoppingcart = shoppingcart;
    }

    public CartItem(Product product, int quantity, Shoppingcart shoppingcart) {
        this.product = product;
        this.quantity = quantity;
        this.shoppingcart = shoppingcart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Shoppingcart getShoppingcart() {
        return shoppingcart;
    }

    public void setShoppingcart(Shoppingcart shoppingcart) {
        this.shoppingcart = shoppingcart;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", shoppingcart=" + shoppingcart +
                '}';
    }
}
