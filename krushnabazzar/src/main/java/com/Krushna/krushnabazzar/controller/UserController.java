package com.Krushna.krushnabazzar.controller;

import com.Krushna.krushnabazzar.Helper.Message;
import com.Krushna.krushnabazzar.entity.*;
import com.Krushna.krushnabazzar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingcartRepository shoppingcartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

//    dashboard
    @GetMapping("/index")
    public String dashboard(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);  // or .findByUsername
        model.addAttribute("user", user);
        return "normal/dashboard";
    }

    @GetMapping("/profile")
    public String Profile(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);  // or .findByUsername
        model.addAttribute("user", user);
        return "normal/Profile";
    }

    @GetMapping("/settings")
    public String openSettings(Principal principal,Model model) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);  // or .findByUsername
        model.addAttribute("user", user);
        return "normal/Settings";
    }

    @GetMapping("/wishlist")
    public String Wishlist(Principal principal,Model model){
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        List<Wishlist> wishlistitems = wishlistRepository.findByUser(user);
        model.addAttribute("user",user);
        model.addAttribute("wishlistitems",wishlistitems);
        return "normal/Mywishlist";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldpassword") String oldpassword,
                                 @RequestParam("newpassword") String newpassword,
                                 Principal prin,Model model,
                                 RedirectAttributes redirectAttributes) {

        String username = prin.getName();
        User currentuser = this.userRepository.findByEmail(username);
        model.addAttribute("user",currentuser);

        if (this.bCryptPasswordEncoder.matches(oldpassword, currentuser.getPassword())) {
            currentuser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
            this.userRepository.save(currentuser);
            redirectAttributes.addFlashAttribute("message", new Message("Password Is Successfully Changed", "alert-success"));
        } else {
            redirectAttributes.addFlashAttribute("message", new Message("Update failed! Please Enter Correct Old Password", "alert-danger"));
            return "redirect:/user/settings";
        }
        return "redirect:/user/index";
    }

    @GetMapping("/products/search/{query}")
    @ResponseBody
    public List<Product> searchProducts(@PathVariable("query") String query,
                                        @RequestParam(value = "category", required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory(query, category);
        } else {
            return productRepository.findByNameContainingIgnoreCase(query);
        }
    }

    @GetMapping("/products/{id}")
    public String viewSingleProd(@PathVariable int id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "normal/view-single";
    }

}
