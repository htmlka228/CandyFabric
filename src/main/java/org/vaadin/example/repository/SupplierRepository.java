package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.vaadin.example.entity.Supplier;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Override
    @Query(
            value = "select * from supplier" +
                    " where id = :id",
            nativeQuery = true
    )
    Optional<Supplier> findById(@Param("id") Long id);

    @Query(
            value = "select * from supplier" +
                    " where name = :name",
            nativeQuery = true
    )
    Optional<Supplier> findByName(@Param("name") String name);
}
