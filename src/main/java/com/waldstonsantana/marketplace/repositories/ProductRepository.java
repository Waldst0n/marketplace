package com.waldstonsantana.marketplace.repositories;

import com.waldstonsantana.marketplace.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
