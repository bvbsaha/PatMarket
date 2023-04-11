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
@Table(name = "animal_type")
public class AnimalType {

    public AnimalType(String animalName) {
        this.animalName = animalName;
        this.animalUrl = "no data";
    }


    public AnimalType() {
        this.animalUrl = "no data";
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long animalId;
    @Column(name = "name")
    private String animalName;

    @Column(name = "url")
    private String animalUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "animalType")
    private List<PatProduct> patProducts = new ArrayList<>();

}
