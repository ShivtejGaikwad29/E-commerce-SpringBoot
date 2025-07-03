package com.Krushna.krushnabazzar.repository;

import com.Krushna.krushnabazzar.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    //  Search by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    //  Search by name and category (optional)
    List<Product> findByNameContainingIgnoreCaseAndCategory(String name, String category);

    //  Get all by category
    List<Product> findByCategory(String category);

}
