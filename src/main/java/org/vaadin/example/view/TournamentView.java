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
import org.vaadin.example.component.TournamentEditor;
import org.vaadin.example.entity.Tournament;
import org.vaadin.example.service.IGameService;
import org.vaadin.example.service.ITeamService;
import org.vaadin.example.service.ITournamentService;

import javax.annotation.security.PermitAll;

@Route(value = "tournaments", layout = MainLayout.class)
@PermitAll
public class TournamentView extends VerticalLayout {
    private final IGameService gameService;
    private final ITeamService teamService;
    private final ITournamentService tournamentService;
    private final TournamentEditor tournamentEditor;
    private final TopTeamsView topTeamsView;

    private Grid<Tournament> grid;

    private final TextField filter = new TextField("", "Search Tournament");
    private final Button addNewBtn = new Button("New Tournament");
    private final Button changeBtn = new Button("Change Tournament");
    private final Button showTopTeams = new Button("Top teams");
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn, changeBtn, showTopTeams);

    @Autowired
    public TournamentView(ITeamService teamService, IGameService gameService, ITournamentService tournamentService, TournamentEditor tournamentEditor, TopTeamsView topTeamsView) {
        this.teamService = teamService;
        this.gameService = gameService;
        this.tournamentService = tournamentService;
        this.tournamentEditor = tournamentEditor;
        this.topTeamsView = topTeamsView;

        setSizeFull();
        configureGrid();

        add(toolbar, grid);
        addButtonListeners();
        tournamentEditor.setChangeHandler(() ->{
            tournamentEditor.setVisible(false);
            showTournaments(filter.getValue());
        });

        showTournaments("");
    }

    private void showTournaments(String name) {
        if (name.isEmpty()) {
            grid.setItems(tournamentService.getAll());
        } else {
            grid.setItems(tournamentService.getTournamentsByFilter(name));
        }
    }

    private void configureGrid() {
        grid = new Grid<>(Tournament.class);
        grid.setSizeFull();
        grid.setColumns("id", "name");
        grid.addColumn(tournament -> tournament.getGame().getName()).setHeader("Game");
    }

    private void addButtonListeners() {
        addNewBtn.addClickListener(e -> tournamentEditor.editTournament(new Tournament()));
        addNewBtn.addClickListener(e -> tournamentEditor.getEditorWindow().open());

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showTournaments(e.getValue()));

        changeBtn.addClickListener(e -> {
            Tournament tournament = grid.asSingleSelect().getValue();

            if (tournament == null) {
                DefaultDialogs.generateErrorDialog("Tournament hasn't been selected").open();
            } else {
                tournamentEditor.editTournament(tournament);
            }
        });

        showTopTeams.addClickListener(e -> {
            Tournament tournament = grid.asSingleSelect().getValue();

            if (tournament == null) {
                DefaultDialogs.generateErrorDialog("Tournament hasn't been selected").open();
            } else {
                topTeamsView.showTopTeams(teamService.getTopByTournament(tournament));
            }
        });
    }
}
