package com.Krushna.krushnabazzar.repository;

import com.Krushna.krushnabazzar.entity.Shoppingcart;
import com.Krushna.krushnabazzar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingcartRepository extends JpaRepository<Shoppingcart, Integer> {
    Shoppingcart findByUser(User user); // Useful for retrieving a user's cart
}
