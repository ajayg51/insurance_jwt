

package com.example.insurance.mis.mvp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.insurance.mis.mvp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
      Optional<Product> findByProductCode(String productCode);
}
