package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.entity.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Override
    @Query(
            value = "select * from player " +
                    "join game on game.id = player.game_id " +
                    "join team on team.id = player.team_id ",
            nativeQuery = true
    )
    List<Player> findAll();

    @Override
    @Query(
            value = "select * from player " +
                    "join game on game.id = player.game_id " +
                    "join team on team.id = player.team_id " +
                    "where player.id = :id",
            nativeQuery = true
    )
    Optional<Player> findById(@Param("id") Long id);

    @Query("from Player el " +
            "where el.name like concat('%', :filter, '%')")
    List<Player> findByFilter(@Param("filter") String filter);
}
