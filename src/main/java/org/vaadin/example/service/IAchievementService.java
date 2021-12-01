package org.vaadin.example.service;

import org.vaadin.example.entity.Achievement;
import java.util.List;

public interface IAchievementService {
    List<Achievement> getAll();
    Achievement getAchievementById(Long id);
    List<Achievement> getAchievementsByFilter(String filter);
    Achievement save(Achievement achievement);
}
