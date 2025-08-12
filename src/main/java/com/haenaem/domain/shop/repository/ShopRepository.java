package com.haenaem.domain.shop.repository;

import com.haenaem.domain.shop.entity.Shop;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByIsActiveTrue();
    
}
