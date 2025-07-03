package com.Krushna.krushnabazzar.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cart")
public class Shoppingcart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shoppingcart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private double totalPrice;

    public Shoppingcart() {}

    public Shoppingcart(User user) {
        this.user = user;
        this.totalPrice = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void recalculateTotalPrice() {
        this.totalPrice = this.items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setShoppingcart(this);
        this.totalPrice += item.getProduct().getPrice() * item.getQuantity();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setShoppingcart(null);
        this.totalPrice -= item.getProduct().getPrice() * item.getQuantity();
    }

    @Override
    public String toString() {
        return "Shoppingcart{" +
                "id=" + id +
                ", user=" + user +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
