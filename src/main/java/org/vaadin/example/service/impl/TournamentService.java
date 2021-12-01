package org.vaadin.example.service.impl;

import org.vaadin.example.entity.Tournament;
import org.vaadin.example.repository.TournamentRepository;
import org.vaadin.example.service.ITournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService implements ITournamentService {
    private final TournamentRepository tournamentRepository;

    @Override
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Tournament> getTournamentsByFilter(String filter) {
        return tournamentRepository.findByFilter(filter);
    }

    @Override
    public List<Tournament> getTournamentsByGameId(Long id) {
        return tournamentRepository.getTournamentsByGame_Id(id);
    }

    @Override
    public Tournament save(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
}
