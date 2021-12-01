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
import org.vaadin.example.component.PlayerEditor;
import org.vaadin.example.entity.Player;
import org.vaadin.example.service.IPlayerService;

import javax.annotation.security.PermitAll;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class PlayerView extends VerticalLayout {
    private final IPlayerService playerService;
    private final PlayerEditor playerEditor;
    private final PreviousTeamView previousTeamView;
    private final PlayerAchievementView playerAchievementView;

    private Grid<Player> playerGrid;

    private final TextField filter = new TextField("", "Search Player");
    private final Button addNewBtn = new Button("New Player");
    private final Button changeBtn = new Button("Change Player");
    private final Button showPreviousTeam = new Button("Previous Team");
    private final Button addNewAchievement = new Button("New Achievement");
    private final Button showAchievements = new Button("Show Achievements");
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn, changeBtn, showPreviousTeam, addNewAchievement, showAchievements);

    @Autowired
    public PlayerView(IPlayerService playerService, PlayerEditor playerEditor, PreviousTeamView previousTeamView, PlayerAchievementView playerAchievementView) {
        this.playerService = playerService;
        this.playerEditor = playerEditor;
        this.previousTeamView = previousTeamView;
        this.playerAchievementView = playerAchievementView;

        setSizeFull();
        configureGrid();

        add(toolbar, playerGrid);
        addButtonListeners();
        playerEditor.setChangeHandler(() -> {
            playerEditor.setVisible(false);
            showPlayers(filter.getValue());
        });

        showPlayers("");
    }

    private void showPlayers(String name) {
        if (name.isEmpty()) {
            playerGrid.setItems(playerService.getAll());
        } else {
            playerGrid.setItems(playerService.getPlayersByFilter(name));
        }
    }

    private void configureGrid() {
        playerGrid = new Grid<>(Player.class);
        playerGrid.setSizeFull();
        playerGrid.setColumns("id", "name");

        playerGrid.addColumn(player -> player.getGame().getName()).setHeader("Game");
        playerGrid.addColumn(player -> player.getTeam().getName()).setHeader("Team");
        playerGrid.addColumn("primaryCountry");
        playerGrid.addColumn("active");
    }

    private void addButtonListeners() {
        addNewBtn.addClickListener(e -> playerEditor.editPlayer(new Player()));
        addNewBtn.addClickListener(e -> playerEditor.getEditorWindow().open());

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showPlayers(e.getValue()));

        changeBtn.addClickListener(e -> {
            Player player = playerGrid.asSingleSelect().getValue();

            if (player == null) {
                DefaultDialogs.generateErrorDialog("Player hasn't been selected").open();
            } else {
                playerEditor.editPlayer(player);
            }
        });

        showPreviousTeam.addClickListener(e -> {
            Player selectedPlayer = playerGrid.asSingleSelect().getValue();

            if (selectedPlayer == null) {
                DefaultDialogs.generateErrorDialog("Player hasn't been selected").open();
            } else {
                previousTeamView.showPreviousTeams(selectedPlayer.getPreviousTeams());
            }
        });

        addNewAchievement.addClickListener(e -> playerEditor.editPlayersAchievements());

        showAchievements.addClickListener(e -> {
            Player selectedPlayer = playerGrid.asSingleSelect().getValue();

            if (selectedPlayer == null) {
                DefaultDialogs.generateErrorDialog("Player hasn't been selected").open();
            } else {
                playerAchievementView.showPlayerAchievements(selectedPlayer.getAchievements());
            }
        });
    }
}
