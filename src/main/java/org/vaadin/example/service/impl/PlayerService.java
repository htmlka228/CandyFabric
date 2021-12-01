package org.vaadin.example.service.impl;

import org.vaadin.example.entity.Player;
import org.vaadin.example.repository.PlayerRepository;
import org.vaadin.example.service.IPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService implements IPlayerService {
    private final PlayerRepository playerRepository;

    @Override
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Player> getPlayersByFilter(String filter) {
        return playerRepository.findByFilter(filter);
    }

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }
}
