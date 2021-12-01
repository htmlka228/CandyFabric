package org.vaadin.example.service;


import org.vaadin.example.entity.Team;
import org.vaadin.example.entity.Tournament;
import org.vaadin.example.model.TopTeamRow;

import java.util.List;

public interface ITeamService {
    List<Team> getAll();
    Team getTeamById(Long id);
    List<Team> getTeamsByFilter(String filter);
    Team save(Team team);
    public List<Team> getTeamsByTournamentName(String name);
    List<TopTeamRow> getTopByTournament(Tournament tournament);
}
