package org.vaadin.example.service;

import org.vaadin.example.entity.Tournament;

import java.util.List;

public interface ITournamentService {
    List<Tournament> getAll();
    Tournament getTournamentById(Long id);
    List<Tournament> getTournamentsByFilter(String filter);
    List<Tournament> getTournamentsByGameId(Long id);
    Tournament save(Tournament tournament);
}
