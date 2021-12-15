package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.vaadin.example.entity.Shop;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Override
    @Query(
            value = "select * from shop " +
                    "where id = :id",
            nativeQuery = true
    )
    Optional<Shop> findById(@Param("id") Long id);
}
