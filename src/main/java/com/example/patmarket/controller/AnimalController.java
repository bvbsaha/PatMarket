package com.example.patmarket.controller;

import com.example.patmarket.entity.AnimalType;
import com.example.patmarket.repository.AnimalTypeRepository;
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

import java.util.Map;

@Controller
public class AnimalController {

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    @Autowired
    private LinkService linkService;

    @Autowired
    private FileService fileService;

    @GetMapping("/animal/find-all")
    public String getAnimalePage(Map<String, Object> model) {
        Iterable<AnimalType> animalTypes = animalTypeRepository.findAll();
        model.put("animals", animalTypes);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "animals";
    }


    @PostMapping("/animal/create")
    public String addAnimal(@RequestParam String animalName,@RequestParam MultipartFile file) {
        AnimalType animalType = new AnimalType(animalName);
        animalTypeRepository.save(new AnimalType(animalName));

        fileService.addImageToAnimal(animalTypeRepository.findAll().stream()
                .filter(animalType1 -> animalType1.getAnimalName().equals(animalName)).findFirst().orElseThrow()
                .getAnimalId(), file);
        return "redirect:/animal/find-all";
    }

    @PostMapping("/animal/delete/{id}")
    public String deleteById(@PathVariable final long id) {
        animalTypeRepository.deleteById(id);
        return "redirect:/animal/find-all";
    }

}
