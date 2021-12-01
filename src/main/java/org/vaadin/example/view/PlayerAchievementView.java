package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Achievement;
import org.vaadin.example.service.IAchievementService;
import org.vaadin.example.service.IPlayerService;

import javax.annotation.security.PermitAll;
import java.util.List;

@SpringComponent
@UIScope
@PermitAll
public class PlayerAchievementView extends VerticalLayout {
    private final IPlayerService playerService;
    private final IAchievementService achievementService;

    private Grid<Achievement> grid;

    private Dialog playerAchievementsDialog = new Dialog();
    private Button close = new Button("Close", VaadinIcon.CLOSE.create());

    @Autowired
    public PlayerAchievementView(IPlayerService playerService, IAchievementService achievementService) {
        this.playerService = playerService;
        this.achievementService = achievementService;

        configureGrid();

        close.getElement().getStyle().set("margin-left", "auto");
        close.addClickListener(e -> playerAchievementsDialog.close());
    }

    private void configureGrid(){
        grid =  new Grid<>(Achievement.class);

        grid.setColumns("id", "name");
        grid.addColumn(e -> e.getTournament().getName()).setHeader("Tournament");
        grid.addColumn(e -> e.getTournament().getGame().getName()).setHeader("Game");
    }

    public void showPlayerAchievements(List<Achievement> achievements) {
        grid.setItems(achievements);
        playerAchievementsDialog.add(new VerticalLayout(close, grid));
        playerAchievementsDialog.setWidth("calc(100vw - (48*var(--lumo-space-m)))");

        playerAchievementsDialog.open();
    }
}
