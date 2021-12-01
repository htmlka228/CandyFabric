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
import org.vaadin.example.component.GameEditor;
import org.vaadin.example.entity.Game;
import org.vaadin.example.service.IGameService;
import org.vaadin.example.service.ITeamService;
import org.vaadin.example.service.ITournamentService;

import javax.annotation.security.PermitAll;

@Route(value = "games", layout = MainLayout.class)
@PermitAll
public class GameView extends VerticalLayout {
    private final IGameService gameService;
    private final ITeamService teamService;
    private final ITournamentService tournamentService;
    private final GameEditor gameEditor;
    private final AvailableTournamentView availableTournamentView;

    private Grid<Game> grid;

    private final TextField filter = new TextField("", "Search Game");
    private final Button addNewBtn = new Button("New Game");
    private final Button changeBtn = new Button("Change Game");
    private final Button showAvailableTournaments = new Button("Available Tournaments");
    private HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn, changeBtn, showAvailableTournaments);

    @Autowired
    public GameView(IGameService gameService, ITournamentService tournamentService, ITeamService teamService, GameEditor gameEditor, AvailableTournamentView availableTournamentView) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.tournamentService =tournamentService;
        this.gameEditor = gameEditor;
        this.availableTournamentView = availableTournamentView;

        setSizeFull();
        configureGrid();

        add(toolbar, grid);
        addButtonListeners();
        gameEditor.setChangeHandler(() -> {
            gameEditor.setVisible(false);
            showGames(filter.getValue());
        });

        showGames("");
    }

    private void showGames(String name) {
        if (name.isEmpty()) {
            grid.setItems(gameService.getAll());
        } else {
            grid.setItems(gameService.getGamesByFilter(name));
        }
    }

    private void configureGrid() {
        grid = new Grid<>(Game.class);
        grid.setSizeFull();
        grid.setColumns("id", "name", "creator");
    }

    private void addButtonListeners() {
        addNewBtn.addClickListener(e -> gameEditor.editGame(new Game()));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showGames(e.getValue()));

        changeBtn.addClickListener(e -> {
            Game game = grid.asSingleSelect().getValue();

            if (game == null) {
                DefaultDialogs.generateErrorDialog("Game hasn't been selected").open();
            } else {
                gameEditor.editGame(game);
            }
        });

        showAvailableTournaments.addClickListener(e -> {
            Game game = grid.asSingleSelect().getValue();

            if (game == null) {
                DefaultDialogs.generateErrorDialog("Game hasn't been selected").open();
            } else {
                availableTournamentView.showAvailableTournaments(tournamentService.getTournamentsByGameId(game.getId()));
            }
        });
    }
}
