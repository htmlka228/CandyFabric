package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.vaadin.example.entity.CandyFabric;

import java.util.Optional;

@Repository
public interface CandyFabricRepository extends JpaRepository<CandyFabric, Long> {
    @Override
    @Query(
            value = "select * from candy_fabric" +
                    " where id = :id",
            nativeQuery = true
    )
    Optional<CandyFabric> findById(@Param("id") Long id);
}
