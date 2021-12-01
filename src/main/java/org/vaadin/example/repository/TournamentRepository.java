package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.entity.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    @Override
    @Query(
            value = "select * from tournament " +
                    "join game on tournament.game_id = game.id ",
            nativeQuery = true
    )
    List<Tournament> findAll();

    @Override
    @Query(
            value = "select * from tournament " +
                    "join game on tournament.game_id = game.id " +
                    "where tournament.id = :id",
            nativeQuery = true
    )
    Optional<Tournament> findById(@Param("id") Long id);

    @Query("from Tournament el " +
            "where el.name like concat('%', :filter, '%')")
    List<Tournament> findByFilter(@Param("filter") String filter);

    @Query(
            value = "select * from tournament " +
                    "join game on tournament.game_id = game.id " +
                    "where tournament.game_id = :id",
            nativeQuery = true
    )
    List<Tournament> getTournamentsByGame_Id(@Param("id") Long id);
}
