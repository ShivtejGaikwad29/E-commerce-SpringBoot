package com.Krushna.krushnabazzar.repository;

import com.Krushna.krushnabazzar.entity.OrderItem;
import com.Krushna.krushnabazzar.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProduct(Product product);
}
