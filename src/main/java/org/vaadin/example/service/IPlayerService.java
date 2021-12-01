package org.vaadin.example.service;

import org.vaadin.example.entity.Player;

import java.util.List;

public interface IPlayerService {
    List<Player> getAll();
    Player getPlayerById(Long id);
    List<Player> getPlayersByFilter(String filter);
    Player save(Player player);
}
