package com.example.patmarket.controller;

import com.example.patmarket.entity.Item;
import com.example.patmarket.entity.Order;
import com.example.patmarket.entity.PatProduct;
import com.example.patmarket.entity.User;
import com.example.patmarket.repository.ItemRepository;
import com.example.patmarket.repository.OrderRepository;
import com.example.patmarket.repository.PatProductRepository;
import com.example.patmarket.repository.UserRepository;
import com.example.patmarket.security.LinkDto;
import com.example.patmarket.security.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PurchaseController {
    @Autowired
    private PatProductRepository productRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkService linkService;

    @PostMapping("/order/add-to-order/{productId}")
    public String addItemToOrder(@RequestParam int count, @PathVariable final long productId) {

        User user = userRepository.findByEmail(getCurrentUsername()).orElseThrow();

        if (user.getOrders().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .count() == 0) {
            orderRepository.save(new Order("Open", user));

            user = userRepository.findById(user.getUserId()).orElseThrow();
        }
        Order openPurshase = user.getOrders().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .findFirst()
                .orElseThrow();

        PatProduct product = productRepository.findById(productId).orElseThrow();
        itemRepository.save(new Item(count, product, openPurshase));
        return "redirect:/categories/" + product.getCategory().getCategoryId() + "/products";
    }

    @GetMapping("/order/{userId}")
    public String getUserOrder(@PathVariable long userId, Map<String, Object> model) {

        User user = userRepository.findByEmail(getCurrentUsername()).orElseThrow();

        user.getOrders().stream().forEach(Order::checkSum);

        if (user.getOrders().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .count() == 0) {
            orderRepository.save(new Order( "Open", user));

            user = userRepository.findById(user.getUserId())
                    .orElseThrow();
        }

        Iterable<Order> openPurchases = user.getOrders().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .collect(Collectors.toList());

        Iterable<Order> confirmedPurchases = user.getOrders().stream()
                .filter(p -> p.getStatus().equals("Confirmed"))
                .collect(Collectors.toList());

        Iterable<Order> sentPurchases = user.getOrders().stream()
                .filter(p -> p.getStatus().equals("Close"))
                .collect(Collectors.toList());

        model.put("openPurchases",openPurchases);
        model.put("confirmedPurchases",confirmedPurchases);
        model.put("sentPurchases",sentPurchases);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);

        return "order";
    }


    @PostMapping("/order/sent/{purchaseId}")
    public String sentOrder(@PathVariable final long purchaseId) {
        Order purchase = orderRepository.findById(purchaseId).orElseThrow();
        purchase.setStatus("Sent");
        orderRepository.save(purchase);
        return "redirect:/order/" + purchase.getUser().getUserId();
    }

    @PostMapping("/order/close/{purchaseId}")
    public String finishOrder(@PathVariable final long purchaseId) {
        Order order = orderRepository.findById(purchaseId).orElseThrow();
        order.setStatus("Close");
        orderRepository.save(order);
        return "redirect:/order/" + order.getUser().getUserId();
    }

    @PostMapping("/order/item/delete/{itemId}")
    public String deleteItem(@PathVariable final long itemId) {
        itemRepository.deleteById(itemId);
        return "redirect:/order/" + getCurrentUserId();
    }


    @GetMapping("/order/admin/find-all")
    public String getUserOrder(Map<String, Object> model) {

        List<Order> orders = orderRepository.findAll();

        orders.stream().forEach(Order::checkSum);

        Iterable<Order> sentPurchases = orders.stream().filter(p -> p.getStatus().equals("Sent"))
                .collect(Collectors.toList());


        Iterable<Order> confirmedPurchases = orders.stream()
                .filter(p -> p.getStatus().equals("Confirmed"))
                .collect(Collectors.toList());

        Iterable<Order> closePurchases = orders.stream()
                .filter(p -> p.getStatus().equals("Close"))
                .collect(Collectors.toList());

        model.put("closePurchases",closePurchases);
        model.put("confirmedPurchases",confirmedPurchases);
        model.put("sentPurchases",sentPurchases);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "order_admin";
    }

    @PostMapping("/order/admin/confirm/{purchaseId}")
    public String confirmOrder(@PathVariable final long purchaseId) {
        Order order = orderRepository.findById(purchaseId).orElseThrow();
        order.setStatus("Confirmed");
        orderRepository.save(order);
        return "redirect:/order/admin/find-all";
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public long getCurrentUserId() {
        return userRepository.findByEmail(getCurrentUsername())
                .orElseThrow()
                .getUserId();
    }
}
