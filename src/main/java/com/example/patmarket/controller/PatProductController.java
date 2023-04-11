package com.example.patmarket.controller;


import com.example.patmarket.entity.AnimalType;
import com.example.patmarket.entity.Category;
import com.example.patmarket.entity.PatProduct;
import com.example.patmarket.repository.AnimalTypeRepository;
import com.example.patmarket.repository.CategoryRepository;
import com.example.patmarket.repository.PatProductRepository;
import com.example.patmarket.security.FileService;
import com.example.patmarket.security.LinkDto;
import com.example.patmarket.security.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PatProductController {

    @Autowired
    private PatProductRepository patProductRepository;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LinkService linkService;

    @Autowired
    private FileService fileService;

    @GetMapping("/products")
    public String getProducts(Map<String, Object> model) {
        Iterable<PatProduct> products = patProductRepository.findAll();
        Iterable<AnimalType> animals = animalTypeRepository.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        model.put("products", products);
        model.put("animals", animals);
        model.put("categories", categories);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "products_admin";
    }

    @PostMapping("/products")
    public String addProduct(@RequestParam String name, @RequestParam int price, @RequestParam MultipartFile file, @RequestParam long animalId, @RequestParam long categoryId, Map<String, Object> model) {
        PatProduct product = new PatProduct(name, price);
        product.setAnimalType(animalTypeRepository.findById(animalId).orElseThrow());
        product.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        patProductRepository.save(product);
        fileService.addImageToProduct(product.getProductId(),file);
        getProducts(model);
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteById(@PathVariable final long id, Map<String, Object> model) {
        patProductRepository.deleteById(id);
        getProducts(model);
        return "redirect:/products";
    }

    @GetMapping("/products/update/{id}")
    public String updateProduct(@PathVariable final long id, Map<String, Object> model) {
        PatProduct product = patProductRepository.findById(id).orElseThrow();
        model.put("product",product);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "updateProduct";
    }

    @PostMapping("/products/update/{id}")
    public String updateById(@PathVariable final long id,@RequestParam String name,
                             @RequestParam int price,@RequestParam MultipartFile file,
                             Map<String, Object> model) {
        PatProduct product = patProductRepository.findById(id).orElseThrow();
        product.setProductName(name);
        product.setPrice(price);
        patProductRepository.save(product);
        if (!file.isEmpty()) {
            System.out.println("+");
            fileService.addImageToProduct(product.getProductId(),file);
        }
        return "redirect:/products";
    }


    @GetMapping("/categories/{id}/products")
    public String getProductsByCategory(@PathVariable final long id, Map<String, Object> model) {
        Iterable<PatProduct> products = patProductRepository.findAll()
                .stream()
                .filter(product -> product.getCategory().getCategoryId().equals(id))
                .collect(Collectors.toList());
        Iterable<AnimalType> animalTypeList = animalTypeRepository.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        model.put("products", products);
        model.put("animals", animalTypeList);
        model.put("categories", categories);
        List linkDtos = new ArrayList<>();
        model.put("categoryLink", linkDtos);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "products_user";
    }

    @GetMapping("/animal/{id}/products")
    public String getProductsByAnimal(@PathVariable final long id, Map<String, Object> model) {
        Iterable<PatProduct> products = patProductRepository.findAll()
                .stream()
                .filter(product -> product.getAnimalType().getAnimalId().equals(id))
                .collect(Collectors.toList());
        Iterable<AnimalType> animalTypeList = animalTypeRepository.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        model.put("products", products);
        model.put("animals", animalTypeList);
        model.put("categories", categories);
        List linkDtos = new ArrayList<>();
        model.put("categoryLink", linkDtos);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "products_user";
    }

}
