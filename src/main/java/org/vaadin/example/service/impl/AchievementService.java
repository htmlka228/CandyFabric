package org.vaadin.example.service.impl;

import org.vaadin.example.entity.Achievement;
import org.vaadin.example.repository.AchievementRepository;
import org.vaadin.example.service.IAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService implements IAchievementService {
    private final AchievementRepository achievementRepository;

    @Override
    public List<Achievement> getAll() {
        return achievementRepository.findAll();
    }

    @Override
    public Achievement getAchievementById(Long id) {
        return achievementRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Achievement> getAchievementsByFilter(String filter) {
        return achievementRepository.findByFilter(filter);
    }

    @Override
    public Achievement save(Achievement achievement) {
        return achievementRepository.save(achievement);
    }
}
