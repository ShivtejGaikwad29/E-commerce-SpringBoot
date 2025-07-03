package com.Krushna.krushnabazzar.controller;

import com.Krushna.krushnabazzar.Helper.Message;
import com.Krushna.krushnabazzar.entity.Product;
import com.Krushna.krushnabazzar.entity.User;
import com.Krushna.krushnabazzar.repository.OrderItemRepository;
import com.Krushna.krushnabazzar.repository.OrderRepository;
import com.Krushna.krushnabazzar.repository.ProductRepository;
import com.Krushna.krushnabazzar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/dashboard")
    public String Dashboard(){
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String Profile(Principal principal,Model model){
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        model.addAttribute("admin",user);
        return "admin/Profile";
    }

    @GetMapping("/reports")
    public String Reports(@RequestParam(defaultValue = "bar")String chart, Model model){
        List<String> categories = List.of("Grocery","Stationary","Health","Household","Baby");
        List<Integer> orderCounts = new ArrayList<>();
        for(String category : categories){
            int count = orderRepository.countByProductCategory(category);
            orderCounts.add(count);
        }
        model.addAttribute("categories",categories);
        model.addAttribute("orderCounts",orderCounts);
        model.addAttribute("chart",chart);
        return "admin/Reports";
    }

//    for adding products
    @GetMapping("/show-add-product")
    public String ShowAddProduct(Model model){
        model.addAttribute("product",new Product());
        return "admin/Add_product";
    }

    @PostMapping("/add-products")
    public String AddProduct(@ModelAttribute Product product,RedirectAttributes redirectAttributes){
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("message","Product is added successfully :) ");
        return "redirect:/admin/view-product";
    }

    @GetMapping("/edit-product/{id}")
    public String editProductForm(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        model.addAttribute("product", product);
        return "admin/Edit_product";
    }

    @PostMapping("/edit-product")
    public String saveEditedProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        productRepository.save(product); // Will update if ID exists
        redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
        return "redirect:/admin/view-product";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
            return "redirect:/admin/delete-product";
        }

        // Check if product is used in any order
        if (orderItemRepository.existsByProduct(product)) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete this product. It is associated with one or more orders.");
            return "redirect:/admin/view-product";
        }

        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        return "redirect:/admin/delete-product";
    }

    @GetMapping("/delete-product")
    public String showDeleteProductPage(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/Delete_product";
    }

    @GetMapping("/view-product")
    public String ViewProduct(Model model){
        model.addAttribute("products",productRepository.findAll());
        return "admin/View_products";
    }

    @GetMapping("/settings")
    public String openSettings() {
        return "admin/Settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldpassword") String oldpassword,
                                 @RequestParam("newpassword") String newpassword,
                                 Principal prin,
                                 RedirectAttributes redirectAttributes) {

        String username = prin.getName();
        User currentuser = this.userRepository.findByEmail(username);

        if (this.bCryptPasswordEncoder.matches(oldpassword, currentuser.getPassword())) {
            currentuser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
            this.userRepository.save(currentuser);
            redirectAttributes.addFlashAttribute("message", new Message("Password Is Successfully Changed", "alert-success"));
        } else {
            redirectAttributes.addFlashAttribute("message", new Message("Update failed! Please Enter Correct Old Password", "alert-danger"));
            return "redirect:/admin/settings";
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/users")
    public String Users(Principal principal,Model model){
        String username = principal.getName();
        System.out.println("Admin logged in"+username);
        List<User> allusers = userRepository.findAll();
        model.addAttribute("users",allusers);
        return "admin/Users";
    }
}
