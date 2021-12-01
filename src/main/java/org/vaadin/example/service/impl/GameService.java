package org.vaadin.example.service.impl;

import org.vaadin.example.entity.Game;
import org.vaadin.example.repository.GameRepository;
import org.vaadin.example.service.IGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService implements IGameService {
    private final GameRepository gameRepository;

    @Override
    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    @Override
    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Game> getGamesByFilter(String filter) {
        return gameRepository.findByFilter(filter);
    }

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }
}
