package org.vaadin.example.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Game;
import org.vaadin.example.entity.Tournament;
import org.vaadin.example.service.IGameService;
import org.vaadin.example.service.ITournamentService;

import java.util.Optional;

@SpringComponent
@UIScope
public class TournamentEditor extends VerticalLayout implements KeyNotifier {
    private final IGameService gameService;
    private final ITournamentService tournamentService;
    private Tournament tournament;

    private final Dialog editorWindow = new Dialog();

    private final TextField tournamentNameField = new TextField("Tournament Name","Tournament Name");
    private final ComboBox<Game> gameComboBox = new ComboBox<>("Game");

    private final Binder<Tournament> binder = new BeanValidationBinder<>(Tournament.class);
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public TournamentEditor(IGameService gameService, ITournamentService tournamentService) {
        this.gameService = gameService;
        this.tournamentService = tournamentService;;

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

    public void editTournament(Tournament tournament) {
        setItemsInComboBoxes();

        if (tournament == null) {
            return;
        } else if (tournament.getId() != null) {
            this.tournament = Optional.ofNullable(tournamentService.getTournamentById(tournament.getId())).orElse(tournament);
        } else {
            this.tournament = tournament;
        }

        binder.setBean(this.tournament);
        editorWindow.open();
        tournamentNameField.focus();
    }

    private void setWidthToTextField() {
        tournamentNameField.setWidthFull();
        gameComboBox.setWidthFull();
    }

    private void setItemsInComboBoxes() {
        gameComboBox.setItems(gameService.getAll());
        gameComboBox.setItemLabelGenerator(Game::getName);
    }

    private void configureModalWindows() {
        editorWindow.setWidth("500px");
        editorWindow.add(new VerticalLayout(
                tournamentNameField,
                gameComboBox,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));
    }

    private void setBinderFields() {
        String emptyFieldMessage = "This field cannot be empty";

        binder.forField(tournamentNameField).asRequired(emptyFieldMessage).bind(
                Tournament::getName,
                Tournament::setName
        );
        binder.forField(gameComboBox).asRequired(emptyFieldMessage).bind(
                Tournament::getGame,
                Tournament::setGame
        );
    }

    private void save() {
        if (binder.isValid()) {

            //TODO make change a player when changed a his team.

            tournamentService.save(tournament);
            changeHandler.onChange();
            editorWindow.close();
        } else {
            DefaultDialogs.generateErrorDialog("You entered an invalid or empty value").open();
        }
    }

    private void cancel() {
        editorWindow.close();
    }
}
