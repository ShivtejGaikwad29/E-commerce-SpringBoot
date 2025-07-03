package com.Krushna.krushnabazzar.repository;

import com.Krushna.krushnabazzar.entity.Product;
import com.Krushna.krushnabazzar.entity.User;
import com.Krushna.krushnabazzar.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {

    List<Wishlist> findByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user,Product product);
}
