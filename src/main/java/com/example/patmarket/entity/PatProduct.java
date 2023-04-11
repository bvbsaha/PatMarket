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
@Table(name = "pat_product")
public class PatProduct {

    public PatProduct(String productName, int price) {
        this.productName = productName;
        this.price = price;
        this.productUrl = "no data";
    }

    public PatProduct() {
        this.productUrl = "no data";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;

    @Column(name = "name")
    private String productName;

    @Column(name = "price")
    private int price;

    @Column(name = "url")
    private String productUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patProduct")
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_type_id", nullable = false)
    private AnimalType animalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

}
