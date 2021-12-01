package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.entity.Achievement;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    @Override
    @Query(
            value = "select * from achievement " +
                    "join tournament on achievement.tournament_id = tournament.id " +
                    "join game on tournament.game_id = game.id",
            nativeQuery = true
    )
    List<Achievement> findAll();

    @Override
    @Query(
            value = "select * from achievement " +
                    "join tournament on achievement.tournament_id = tournament.id " +
                    "join game on tournament.game_id = game.id " +
                    "where achievement.id = :id",
            nativeQuery = true
    )
    Optional<Achievement> findById(@Param("id") Long id);

    @Query("from Achievement el " +
            "where el.name like concat('%', :filter, '%')")
    List<Achievement> findByFilter(@Param("filter") String filter);
}
