package com.example.patmarket.controller;

import com.example.patmarket.entity.AnimalType;
import com.example.patmarket.entity.Category;
import com.example.patmarket.repository.AnimalTypeRepository;
import com.example.patmarket.repository.CategoryRepository;
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

@Controller
public class CategoryController {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    @Autowired
    private LinkService linkService;

    @Autowired
    private FileService fileService;

    @GetMapping("/categories")
    public String getCategory(Map<String, Object> model) {
        Iterable<Category> categories = categoryRepository.findAll();
        Iterable<AnimalType> animalTypes = animalTypeRepository.findAll();
        model.put("categories", categories);
        List linkDtos = new ArrayList<>();
        for (AnimalType element: animalTypes) {
            linkDtos.add(new LinkDto(element.getAnimalName(),"/animal/products/" + element.getAnimalId()));
        }
        model.put("animalLink", linkDtos);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "categories";
    }

    @PostMapping("/categories")
    public String addCar(@RequestParam String name, @RequestParam MultipartFile file, Map<String, Object> model) {
        Category category = new Category(name);
        categoryRepository.save(category);
        fileService.addImageToCategory(category.getCategoryId(),file);
        getCategory(model);
        return "redirect:/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCarById(@PathVariable final long id, Map<String, Object> model) {
        categoryRepository.deleteById(id);
        getCategory(model);
        return "redirect:/categories";
    }

    @GetMapping("/categories/update/{id}")
    public String updateCar(@PathVariable final long id, Map<String, Object> model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        model.put("category",category);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "updateCategory";
    }

    @PostMapping("/categories/update/{id}")
    public String updateById(@PathVariable final long id,@RequestParam MultipartFile file,
                             @RequestParam String name) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setCategoryName(name);
        categoryRepository.save(category);
        fileService.addImageToCategory(category.getCategoryId(),file);
        return "redirect:/categories";
    }
}
