package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.entity.Team;

import java.util.List;
import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {
    @Override
    @Query(
            value = "select * from team",
            nativeQuery = true
    )
    List<Team> findAll();

    @Override
    @Query(
            value = "select * from team " +
                    "where team.id = :id",
            nativeQuery = true
    )
    Optional<Team> findById(@Param("id") Long id);

    @Query("from Team el " +
            "where el.name like concat('%', :filter, '%')")
    List<Team> findByFilter(@Param("filter") String filter);

    @Query(
            value = "select * from team " +
                    "where team.id in (select team.id from team_achievements where achievements_id in (select achievements_id from achievement where achievement.tournament_id = (select tournament_id from tournament where tournament.name = :name)))",
            nativeQuery = true
    )
    List<Team> getTeamsByTournamentName(String name);
}
