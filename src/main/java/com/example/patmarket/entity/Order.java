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
@Table(name = "purchase")
public class Order {

    public Order(String status) {
        this.status = status;
    }

    public Order(String status, User user) {
        this.status = status;
        this.user = user;
    }

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long purchaseId;

    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    private double sum;

    public void checkSum() {
        sum = 0;
        for (Item item: items) {
          sum += item.getPatProduct().getPrice();
        }
    }
}
