package com.Krushna.krushnabazzar.controller;

import com.Krushna.krushnabazzar.entity.*;
import com.Krushna.krushnabazzar.repository.OrderRepository;
import com.Krushna.krushnabazzar.repository.ProductRepository;
import com.Krushna.krushnabazzar.repository.ShoppingcartRepository;
import com.Krushna.krushnabazzar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingcartRepository shoppingcartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/order/place")
    public String placeOrder(@RequestParam("paymentMethod") String paymentMethod, Principal principal, RedirectAttributes redirectAttributes) {
        System.out.println(">> paymentMethod received: " + paymentMethod); // DEBUG LINE
        User user = userRepository.findByEmail(principal.getName());
        Shoppingcart cart = shoppingcartRepository.findByUser(user);

        if (cart == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
            return "redirect:/category/cart";
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setPaymentMethod(paymentMethod);

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        shoppingcartRepository.save(cart);

        redirectAttributes.addFlashAttribute("message", "Order placed successfully!");
        return "redirect:/order/confirm";
    }

    @GetMapping("/myorder")
    public String viewOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // or your custom login page
        }

        User user = userRepository.findByEmail(principal.getName());
        List<Order> orders = orderRepository.findByUser(user);

        model.addAttribute("orders", orders);
        model.addAttribute("user",user);
        return "normal/MyOrders";
    }

    @GetMapping("/order/confirm")
    public String ConfirmOrder(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        List<Order> orders = orderRepository.findByUser(user);

        if (!orders.isEmpty()) {
            Order latestOrder = orders.get(orders.size() - 1); // get last order
            model.addAttribute("order", latestOrder);
        }
        return "normal/Confirm";
    }
}
