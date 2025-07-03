package com.Krushna.krushnabazzar.repository;

import com.Krushna.krushnabazzar.entity.CartItem;
import com.Krushna.krushnabazzar.entity.Shoppingcart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByShoppingcart(Shoppingcart cart); // Get all items in a cart
}
