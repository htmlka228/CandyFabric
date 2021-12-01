package org.vaadin.example.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.example.entity.Achievement;
import org.vaadin.example.entity.Game;
import org.vaadin.example.entity.Player;
import org.vaadin.example.entity.Team;
import org.vaadin.example.service.IAchievementService;
import org.vaadin.example.service.IGameService;
import org.vaadin.example.service.IPlayerService;
import org.vaadin.example.service.ITeamService;

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class PlayerEditor extends VerticalLayout implements KeyNotifier {
    private final IPlayerService playerService;
    private final IGameService gameService;
    private final ITeamService teamService;
    private final IAchievementService achievementService;
    private Player player;

    private final Dialog editorWindow = new Dialog();
    private final Dialog playerAchievementEditorWindow = new Dialog();

    private final TextField nicknameField = new TextField("Name","Nickname");

    private final ComboBox<Game> gameComboBox = new ComboBox<>("Game");
    private final ComboBox<Team> teamComboBox = new ComboBox<>("Team");
    private final ComboBox<String> countryComboBox = new ComboBox<>("Primary country");

    private final ComboBox<Player> playerComboBox = new ComboBox<>("Player");
    private final ComboBox<Achievement> achievementComboBox = new ComboBox<>("Achievement");

    private final Checkbox activeCheckBox = new Checkbox("Active", true);

    private final Binder<Player> binder = new BeanValidationBinder<>(Player.class);
    private final Binder<Player> achievementsBinder = new BeanValidationBinder<>(Player.class);
    private ChangeHandler changeHandler;

    private final List<String> countries;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public PlayerEditor(IPlayerService playerService, IGameService gameService, ITeamService teamService, IAchievementService achievementService, @Value("#{'${list.countries}'.split(',')}") List<String> countries) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.teamService = teamService;
        this.achievementService = achievementService;
        this.countries = countries;

        setWidthToTextField();
        setItemsInComboBoxes();
        configureModalWindows();
        setBinderFields();
        setSpacing(true);

        addKeyPressListener(Key.ENTER, e -> save());
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public Dialog getEditorWindow() {
        return this.editorWindow;
    }

    public void editPlayer(Player player) {
        setItemsInComboBoxes();

        if (player == null) {
            return;
        } else if (player.getId() != null) {
            this.player = Optional.ofNullable(playerService.getPlayerById(player.getId())).orElse(player);
        } else {
            this.player = player;
        }

        binder.setBean(this.player);
        editorWindow.open();
        nicknameField.focus();
    }

    public void editPlayersAchievements() {
        setItemsInComboBoxes();
        playerAchievementEditorWindow.open();
    }

    private void setWidthToTextField() {
        nicknameField.setWidthFull();
        gameComboBox.setWidthFull();
        teamComboBox.setWidthFull();
        countryComboBox.setWidthFull();
        playerComboBox.setWidthFull();
        achievementComboBox.setWidthFull();
    }

    private void setItemsInComboBoxes() {
        teamComboBox.setItems(teamService.getAll());
        teamComboBox.setItemLabelGenerator(Team::getName);

        gameComboBox.setItems(gameService.getAll());
        gameComboBox.setItemLabelGenerator(Game::getName);

        countryComboBox.setItems(countries);

        playerComboBox.setItems(playerService.getAll());
        playerComboBox.setItemLabelGenerator(Player::getName);

        achievementComboBox.setItems(achievementService.getAll());
        achievementComboBox.setItemLabelGenerator(e -> e.getName() + " " + e.getTournament().getName() + " (" + e.getTournament().getGame().getName() + ")");
    }

    private void configureModalWindows() {
        editorWindow.setWidth("500px");
        editorWindow.add(new VerticalLayout(
                nicknameField,
                gameComboBox,
                teamComboBox,
                countryComboBox,
                activeCheckBox,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));

        playerAchievementEditorWindow.setWidth("500px");
        playerAchievementEditorWindow.add(new VerticalLayout(
                playerComboBox,
                achievementComboBox,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));
    }

    private void setBinderFields() {
        String emptyFieldMessage = "This field cannot be empty";

        binder.forField(nicknameField).asRequired(emptyFieldMessage).bind(
                Player::getName,
                Player::setName
        );
        binder.forField(gameComboBox).asRequired(emptyFieldMessage).bind(
                Player::getGame,
                Player::setGame
        );
        binder.forField(teamComboBox).asRequired(emptyFieldMessage).bind(
                Player::getTeam,
                Player::setTeam
        );
        binder.forField(countryComboBox).asRequired(emptyFieldMessage).bind(
                Player::getPrimaryCountry,
                Player::setPrimaryCountry
        );
        binder.forField(activeCheckBox).bind(
                Player::isActive,
                Player::setActive
        );

        achievementsBinder.forField(achievementComboBox).asRequired(emptyFieldMessage);
        achievementsBinder.forField(playerComboBox).asRequired(emptyFieldMessage);
    }

    private void save() {
        if (binder.isValid()) {
            if (player.getId() != null) {
                Player oldPlayer = playerService.getPlayerById(player.getId());

                if (!player.getTeam().getName().equals(oldPlayer.getTeam().getName()) && !player.getPreviousTeams().contains(oldPlayer.getTeam())) {
                    player.getPreviousTeams().add(oldPlayer.getTeam());
                }
            }

            playerService.save(player);
            changeHandler.onChange();
            editorWindow.close();
        } else if (achievementsBinder.isValid()) {
            this.player = playerComboBox.getValue();

            boolean containsGameAchievement = player.getAchievements().stream()
                    .anyMatch(e -> e.getTournament().getName().equals(achievementComboBox.getValue().getTournament().getName()));

            boolean otherGameAchievement = !achievementComboBox.getValue().getTournament().getGame().getName().equals(player.getGame().getName());

            if (containsGameAchievement) {
               DefaultDialogs.generateErrorDialog("Chosen player already has the same achievement").open();
            } else if (otherGameAchievement) {
                DefaultDialogs.generateErrorDialog("Player cannot have the achievement for other game").open();
            } else {
                player.getAchievements().add(achievementComboBox.getValue());

                if (!player.getTeam().getAchievements().contains(achievementComboBox.getValue())) {
                    player.getTeam().getAchievements().add(achievementComboBox.getValue());
                    teamService.save(player.getTeam());
                }

                playerService.save(player);
                changeHandler.onChange();

                playerAchievementEditorWindow.close();
            }
        } else {
            DefaultDialogs.generateErrorDialog("You entered an invalid or empty value").open();
        }
    }

    private void cancel() {
        playerAchievementEditorWindow.close();
        editorWindow.close();
    }
}
