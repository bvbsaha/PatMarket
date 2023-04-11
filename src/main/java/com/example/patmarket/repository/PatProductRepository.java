package com.example.patmarket.repository;


import com.example.patmarket.entity.PatProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatProductRepository extends JpaRepository<PatProduct, Long> {
}
