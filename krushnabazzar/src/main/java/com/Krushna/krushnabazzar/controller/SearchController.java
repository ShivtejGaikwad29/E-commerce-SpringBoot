package com.Krushna.krushnabazzar.controller;

import com.Krushna.krushnabazzar.entity.Product;
import com.Krushna.krushnabazzar.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/product/search/{query}")
    @ResponseBody
    public List<Product> searchProducts(@PathVariable("query") String query,
                                        @RequestParam(value = "category", required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory(query, category);
        } else {
            return productRepository.findByNameContainingIgnoreCase(query);
        }
    }

    @GetMapping("/product/{id}")
    public String viewSingleProduct(@PathVariable int id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "admin/view-single";
    }


}
