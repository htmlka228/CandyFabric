package org.vaadin.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Team;
import org.vaadin.example.entity.Tournament;
import org.vaadin.example.model.TopTeamRow;
import org.vaadin.example.repository.TeamRepository;
import org.vaadin.example.service.ITeamService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService implements ITeamService {
    private final TeamRepository teamRepository;

    @Override
    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Team> getTeamsByTournamentName(String name) {
        return teamRepository.getTeamsByTournamentName(name);
    }

    @Override
    public List<TopTeamRow> getTopByTournament(Tournament tournament) {
        List<Team> topTeams = getTeamsByTournamentName(tournament.getName());


        return topTeams.stream()
                .peek(team -> team.setAchievements(team.getAchievements().stream()
                                .filter(achievement -> achievement.getTournament().getName().equals(tournament.getName()))
                                .collect(Collectors.toList())
                        )
                )
                .filter(team -> !team.getAchievements().isEmpty())
                .map(team -> TopTeamRow.builder()
                        .rank(team.getAchievements().get(0).getName())
                        .teamName(team.getName())
                        .tournamentName(team.getAchievements().get(0).getTournament().getName())
                        .gameName(team.getAchievements().get(0).getTournament().getGame().getName())
                        .build()
                )
                .sorted(Comparator.comparing(TopTeamRow::getRank))
                .collect(Collectors.toList());
    }

    @Override
    public List<Team> getTeamsByFilter(String filter) {
        return teamRepository.findByFilter(filter);
    }

    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }
}
