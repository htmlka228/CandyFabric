package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.component.AchievementEditor;
import org.vaadin.example.component.DefaultDialogs;
import org.vaadin.example.entity.Achievement;
import org.vaadin.example.service.IAchievementService;
import org.vaadin.example.service.IPlayerService;

import javax.annotation.security.PermitAll;

@Route(value = "achievements", layout = MainLayout.class)
@PermitAll
public class AchievementView extends VerticalLayout {
    private final IPlayerService playerService;
    private final IAchievementService achievementService;
    private AchievementEditor achievementEditor;

    private Grid<Achievement> grid;

    private final TextField filter = new TextField("", "Search Achievement");
    private final Button addNewBtn = new Button("New Achievement");
    private final Button changeBtn = new Button("Change Achievement");
    private HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn, changeBtn);

    @Autowired
    public AchievementView(IPlayerService playerService, IAchievementService achievementService, AchievementEditor achievementEditor) {
        this.playerService = playerService;
        this.achievementService = achievementService;
        this.achievementEditor = achievementEditor;

        setSizeFull();
        configureGrid();

        add(toolbar, grid);
        addButtonListeners();
        achievementEditor.setChangeHandler(() -> {
            achievementEditor.setVisible(false);
            showAchievements(filter.getValue());
        });

        showAchievements("");
    }

    private void showAchievements(String name) {
        if (name.isEmpty()) {
            grid.setItems(achievementService.getAll());
        } else {
            grid.setItems(achievementService.getAchievementsByFilter(name));
        }
    }

    private void configureGrid() {
        grid = new Grid<>(Achievement.class);
        grid.setSizeFull();
        grid.setColumns("id", "name");
        grid.addColumn(e -> e.getTournament().getName()).setHeader("Tournament");
        grid.addColumn(e -> e.getTournament().getGame().getName()).setHeader("Game");
    }

    private void addButtonListeners() {
        addNewBtn.addClickListener(e -> achievementEditor.editAchievement(new Achievement()));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showAchievements(e.getValue()));

        changeBtn.addClickListener(e -> {
            Achievement achievement = grid.asSingleSelect().getValue();

            if (achievement == null) {
                DefaultDialogs.generateErrorDialog("Achievement hasn't been selected").open();
            } else {
                achievementEditor.editAchievement(achievement);
            }
        });
    }
}
