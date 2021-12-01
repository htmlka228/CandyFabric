package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.entity.Game;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Override
    @Query(
            value = "select * from game",
            nativeQuery = true
    )
    List<Game> findAll();

    @Override
    @Query(
            value = "select * from game " +
                    "where game.id = :id",
            nativeQuery = true
    )
    Optional<Game> findById(@Param("id") Long id);

    @Query("from Game el " +
            "where el.name like concat('%', :filter, '%')")
    List<Game> findByFilter(@Param("filter") String filter);
}
