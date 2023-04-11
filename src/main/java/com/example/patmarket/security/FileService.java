package com.example.patmarket.security;


import com.example.patmarket.entity.AnimalType;
import com.example.patmarket.entity.Category;
import com.example.patmarket.entity.PatProduct;
import com.example.patmarket.repository.AnimalTypeRepository;
import com.example.patmarket.repository.CategoryRepository;
import com.example.patmarket.repository.PatProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class FileService {

    @Autowired
    private PatProductRepository patProductRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    public void addImageToProduct(final long id, MultipartFile multipartFile) {
        String fileName = "image/product" + id + ".png";
        try {
            File file = new File("src/main/resources/static/" + fileName);
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("file exception");
        }
        PatProduct product = patProductRepository.findById(id).orElseThrow();
        product.setProductUrl(fileName);
        patProductRepository.save(product);
    }

    public void addImageToCategory(final long id, MultipartFile multipartFile) {
        String fileName = "image/category" + id + ".png";
        try {
            File file = new File("src/main/resources/static/" + fileName);
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("file exception");
        }
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setCategoryUrl(fileName);
        categoryRepository.save(category);
    }

    public void addImageToAnimal(final long id, MultipartFile multipartFile) {
        String fileName = "image/animal" + id + ".png";
        try {
            File file = new File("src/main/resources/static/" + fileName);
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("file exception");
        }
        AnimalType animalType = animalTypeRepository.findById(id).orElseThrow();
        animalType.setAnimalUrl(fileName);
        animalTypeRepository.save(animalType);
    }
}
