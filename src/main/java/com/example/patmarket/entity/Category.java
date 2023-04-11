package com.example.patmarket.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
public class Category {

    public Category(String categoryName) {
        this.categoryName = categoryName;
        this.categoryUrl = "no data";
    }

    public Category(String categoryName, String categoryUrl) {
        this.categoryName = categoryName;
        this.categoryUrl = categoryUrl;
    }

    public Category() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long categoryId;

    @Column(name = "name")
    private String categoryName;

    @Column(name = "url")
    private String categoryUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<PatProduct> patProducts = new ArrayList<>();
}
