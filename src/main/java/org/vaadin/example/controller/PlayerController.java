package org.vaadin.example.controller;

import org.vaadin.example.entity.Player;
import org.vaadin.example.entity.Team;
import org.vaadin.example.model.TopTeamRow;
import org.vaadin.example.service.IPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.service.ITeamService;
import org.vaadin.example.service.ITournamentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {
    private final IPlayerService playerService;
    private final ITeamService teamService;
    private final ITournamentService tournamentService;

    @GetMapping
    public List<Player> getPlayers() {
        return playerService.getAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @PostMapping
    public Player savePlayer(@RequestBody Player player) {
        return playerService.save(player);
    }

    @GetMapping("/test")
    public List<TopTeamRow> getTestTeams() {
        return teamService.getTopByTournament(tournamentService.getTournamentById(1L));
    }
}
