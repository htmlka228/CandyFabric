package org.vaadin.example.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Game;
import org.vaadin.example.service.IGameService;

import java.util.Optional;

@SpringComponent
@UIScope
public class GameEditor extends VerticalLayout implements KeyNotifier {
    private IGameService gameService;
    private Game game;

    private final Dialog editorWindow = new Dialog();

    private final TextField gameNameField = new TextField("Name","Game Name");
    private final TextField creatorField = new TextField("Creator","Creator");

    private final Binder<Game> binder = new BeanValidationBinder<>(Game.class);
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public GameEditor(IGameService gameService) {
        this.gameService = gameService;

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

    public void editGame(Game game) {
        setItemsInComboBoxes();

        if (game == null) {
            return;
        } else if (game.getId() != null) {
            this.game = Optional.ofNullable(gameService.getGameById(game.getId())).orElse(game);
        } else {
            this.game = game;
        }

        binder.setBean(this.game);
        editorWindow.open();
        gameNameField.focus();
    }

    private void setWidthToTextField() {
        gameNameField.setWidthFull();
        creatorField.setWidthFull();
    }

    private void setItemsInComboBoxes() {
        //TODO insert items into combo if need
    }

    private void configureModalWindows() {
        editorWindow.setWidth("500px");
        editorWindow.add(new VerticalLayout(
                gameNameField,
                creatorField,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));
    }

    private void setBinderFields() {
        String emptyFieldMessage = "This field cannot be empty";

        binder.forField(gameNameField).asRequired(emptyFieldMessage).bind(
                Game::getName,
                Game::setName
        );
        binder.forField(creatorField).asRequired(emptyFieldMessage).bind(
                Game::getCreator,
                Game::setCreator
        );
    }

    private void save() {
        if (binder.isValid()) {
            gameService.save(game);
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
