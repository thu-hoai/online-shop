package com.example.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
