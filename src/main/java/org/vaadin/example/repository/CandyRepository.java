package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vaadin.example.entity.Candy;

import java.util.List;

@Repository
public interface CandyRepository extends JpaRepository<Candy, Long> {
    @Override
    @Query(
            value = "select * from candy",
            nativeQuery = true
    )
    List<Candy> findAll();
}
