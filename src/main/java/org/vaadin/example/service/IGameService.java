package org.vaadin.example.service;

import org.vaadin.example.entity.Game;

import java.util.List;

public interface IGameService {
    List<Game> getAll();
    Game getGameById(Long id);
    List<Game> getGamesByFilter(String filter);
    Game save(Game game);
}
