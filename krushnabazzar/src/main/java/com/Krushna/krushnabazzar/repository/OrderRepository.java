package com.Krushna.krushnabazzar.repository;

import com.Krushna.krushnabazzar.entity.Order;
import com.Krushna.krushnabazzar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long>{
    List<Order> findByUser(User user);

    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.product.category = :category")
    int countByProductCategory(@Param("category") String category);


}
