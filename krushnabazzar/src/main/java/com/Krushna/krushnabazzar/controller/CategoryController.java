package com.Krushna.krushnabazzar.controller;

import com.Krushna.krushnabazzar.entity.*;
import com.Krushna.krushnabazzar.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {


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
    private OrderRepository orderRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @GetMapping("/grocery")
    public String Grocery(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByEmail(username);
            model.addAttribute("user", user);
        }

        List<Product> groceryItems = productRepository.findByCategory("Grocery");
        model.addAttribute("products", groceryItems);

        return "normal/Grocery";
    }


    @GetMapping("/stationary")
    public String Stationary(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByEmail(username);
            model.addAttribute("user", user);
        }

        List<Product> groceryItems1 = productRepository.findByCategory("Stationary");
        model.addAttribute("products", groceryItems1);
        return "normal/Stationary";
    }

    @GetMapping("/household")
    public String Household(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByEmail(username);
            model.addAttribute("user", user);
        }

        List<Product> groceryItems = productRepository.findByCategory("Household");
        model.addAttribute("products", groceryItems);
        return "normal/Household";
    }

    @GetMapping("/health")
    public String Health(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByEmail(username);
            model.addAttribute("user", user);
        }

        List<Product> groceryItems = productRepository.findByCategory("Health");
        model.addAttribute("products", groceryItems);
        return "normal/Health&Hygine";
    }

    @GetMapping("/baby")
    public String BabyCare(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByEmail(username);
            model.addAttribute("user", user);
        }

        List<Product> groceryItems = productRepository.findByCategory("Babycare");
        model.addAttribute("products", groceryItems);
        return "normal/BabyCare";
    }

    @GetMapping("/addtocart/{id}")
    public String addToCart(
            @PathVariable("id") Integer productid,
            @RequestParam("redirect") String redirectCategory,
            Principal principal,
            RedirectAttributes redirectAttributes) {
//        1. get the logged in user
        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "User Not Found..!");
            return "redirect:/login";
        }

//        2.find the product
        Product product = productRepository.findById(productid).orElse(null);
        if (product == null) {
            redirectAttributes.addFlashAttribute("message", "Product not found");
            return "redirect:/category/" + redirectCategory;
        }

//        3.create a shopping cart
        Shoppingcart cart = shoppingcartRepository.findByUser(user);
        if (cart == null) {
            cart = new Shoppingcart(user);
        }

//        4.check if the item is already exist in the cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productid)
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            CartItem newItem = new CartItem(product, 1, cart);
            cart.addItem(newItem);
        }

        cart.recalculateTotalPrice();

//        5.save cart and items
        shoppingcartRepository.save(cart);

//        6.add success msg and redirect
        redirectAttributes.addFlashAttribute("message", "Item added to cart!");
        return "redirect:/category/" + redirectCategory.toLowerCase();
    }

    @GetMapping("/cart")
    public String MyCart(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        Shoppingcart cart = shoppingcartRepository.findByUser(user);
        if (cart == null) {
            cart = new Shoppingcart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            cart.setTotalPrice(0.0);
            shoppingcartRepository.save(cart);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("user",user);
        return "normal/Mycart";
    }

    @GetMapping("/removefromcart/{itemId}")
    public String removeFromCart(@PathVariable("itemId") Integer itemId, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "User not found.");
            return "redirect:/login";
        }

        Shoppingcart cart = shoppingcartRepository.findByUser(user);
        if (cart == null) {
            redirectAttributes.addFlashAttribute("message", "Cart not found.");
            return "redirect:/category/cart";
        }

        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null && item.getShoppingcart().getId() == cart.getId()) {
            cart.removeItem(item); // updates totalPrice
            cartItemRepository.delete(item); // delete item from DB
            shoppingcartRepository.save(cart); // save cart
            redirectAttributes.addFlashAttribute("message", "Item removed from cart!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Item not found in cart!");
        }

        return "redirect:/category/cart";
    }

    @GetMapping("/addtowishlist/{id}")
    public String AddToWishlist(@PathVariable("id") Integer product_id,
                                @RequestParam("redirect") String redirectcategory,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "User is not found..!");
            return "redirect:/login";
        }

        Product product = productRepository.findById(product_id).orElse(null);
        if (product == null) {
            redirectAttributes.addFlashAttribute("message", "Product Not Found");
            return "redirect:/category/" + redirectcategory.toLowerCase();
        }

        // Check if the item is already in wishlist
        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            redirectAttributes.addFlashAttribute("message", "Product already in wishlist!");
        } else {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlist.setProduct(product);
            wishlistRepository.save(wishlist);
            redirectAttributes.addFlashAttribute("message", "Product added to wishlist!");
        }

        return "redirect:/category/" + redirectcategory.toLowerCase();
    }

    @GetMapping("/remove/{productId}")
    @Transactional
    public String removeFromWishlist(@PathVariable("productId") Integer productId,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        Product product = productRepository.findById(productId).orElse(null);

        if (user != null && product != null) {
            wishlistRepository.deleteByUserAndProduct(user, product);
            redirectAttributes.addFlashAttribute("message", "Removed from wishlist");
        }

        return "redirect:/user/wishlist";
    }


}
