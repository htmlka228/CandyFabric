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
import org.vaadin.example.entity.Team;
import org.vaadin.example.model.TopTeamRow;
import org.vaadin.example.service.IAchievementService;
import org.vaadin.example.service.ITeamService;

import javax.annotation.security.PermitAll;
import java.util.List;

@SpringComponent
@UIScope
@PermitAll
public class TopTeamsView extends VerticalLayout {
    private final ITeamService teamService;
    private final IAchievementService achievementService;

    private Grid<TopTeamRow> grid;

    private Dialog teamAchievementsDialog = new Dialog();
    private Button close = new Button("Close", VaadinIcon.CLOSE.create());

    @Autowired
    public TopTeamsView(ITeamService teamService, IAchievementService achievementService) {
        this.teamService = teamService;
        this.achievementService = achievementService;

        configureGrid();

        close.getElement().getStyle().set("margin-left", "auto");
        close.addClickListener(e -> teamAchievementsDialog.close());
    }

    private void configureGrid() {
        grid =  new Grid<>(TopTeamRow.class, false);

        grid.addColumn(TopTeamRow::getRank).setHeader("Rank");
        grid.addColumn(TopTeamRow::getTeamName).setHeader("Team Name");
        grid.addColumn(TopTeamRow::getTournamentName).setHeader("Tournament Name");
        grid.addColumn(TopTeamRow::getGameName).setHeader("Game Name");
    }

    public void showTopTeams(List<TopTeamRow> statistic) {
        grid.setItems(statistic);
        teamAchievementsDialog.add(new VerticalLayout(close, grid));
        teamAchievementsDialog.setWidth("calc(100vw - (48*var(--lumo-space-m)))");

        teamAchievementsDialog.open();
    }
}
