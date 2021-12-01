package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.component.DefaultDialogs;
import org.vaadin.example.component.TeamEditor;
import org.vaadin.example.entity.Team;
import org.vaadin.example.service.ITeamService;

import javax.annotation.security.PermitAll;

@Route(value = "teams", layout = MainLayout.class)
@PermitAll
public class TeamView extends VerticalLayout {
    private final ITeamService teamService;
    private final TeamEditor teamEditor;

    private Grid<Team> grid;

    private final TextField filter = new TextField("", "Search Team");
    private final Button addNewBtn = new Button("New Team");
    private final Button changeBtn = new Button("Change Team");
    private HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn, changeBtn);

    @Autowired
    public TeamView(ITeamService teamService, TeamEditor teamEditor) {
        this.teamService = teamService;
        this.teamEditor = teamEditor;

        setSizeFull();
        configureGrid();

        add(toolbar, grid);
        addButtonListeners();
        teamEditor.setChangeHandler(() -> {
            teamEditor.setVisible(false);
            showTeams(filter.getValue());
        });

        showTeams("");
    }

    private void showTeams(String name) {
        if (name.isEmpty()) {
            grid.setItems(teamService.getAll());
        } else {
            grid.setItems(teamService.getTeamsByFilter(name));
        }
    }

    private void configureGrid() {
        grid = new Grid<>(Team.class);
        grid.setSizeFull();
        grid.setColumns("id", "name", "country", "active");
    }

    private void addButtonListeners() {
        addNewBtn.addClickListener(e -> teamEditor.editTeam(new Team()));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showTeams(e.getValue()));

        changeBtn.addClickListener(e -> {
            Team team = grid.asSingleSelect().getValue();

            if (team == null) {
                DefaultDialogs.generateErrorDialog("Team hasn't been selected").open();
            } else {
                teamEditor.editTeam(team);
            }
        });
    }
}
