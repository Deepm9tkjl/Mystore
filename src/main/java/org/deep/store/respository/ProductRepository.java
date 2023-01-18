package org.deep.store.respository;

import java.util.List;

import org.deep.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByTitleContains(String keyword);
}
