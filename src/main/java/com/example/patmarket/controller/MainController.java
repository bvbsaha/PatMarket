package com.example.patmarket.controller;

import com.example.patmarket.entity.AnimalType;
import com.example.patmarket.entity.Category;
import com.example.patmarket.entity.Order;
import com.example.patmarket.entity.User;
import com.example.patmarket.repository.AnimalTypeRepository;
import com.example.patmarket.repository.CategoryRepository;
import com.example.patmarket.repository.OrderRepository;
import com.example.patmarket.repository.UserRepository;
import com.example.patmarket.security.LinkDto;
import com.example.patmarket.security.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.patmarket.security.LinkService.getCurrentUsername;


@Controller
public class MainController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LinkService linkService;

    @GetMapping("/main")
    public String getProducts(Map<String, Object> model) {

        User user = userRepository.findByEmail(getCurrentUsername()).orElseThrow();

        if (user.getOrders().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .count() == 0) {
            orderRepository.save(new Order("Open", user));
        }

        Iterable<Category> categories = categoryRepository.findAll();
        model.put("categories", categories);

        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);

        List<LinkDto> categoryLinksDtos = new ArrayList<>();
        for (Category element : categories) {
            categoryLinksDtos.add(new LinkDto(element.getCategoryName(),
                    "/categories/" + element.getCategoryId() + "/products", element.getCategoryUrl()));
        }
        model.put("categoryLink", categoryLinksDtos);

        List<LinkDto> animalLinksDtos = new ArrayList<>();
        for (AnimalType element : animalTypeRepository.findAll()) {
            animalLinksDtos.add(new LinkDto(element.getAnimalName(),
                    "/animal/" + element.getAnimalId() + "/products", element.getAnimalUrl()));
        }
        model.put("animalLink", animalLinksDtos);
        return "main";
    }
}