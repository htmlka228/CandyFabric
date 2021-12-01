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
import org.vaadin.example.entity.Achievement;
import org.vaadin.example.entity.Tournament;
import org.vaadin.example.service.IAchievementService;
import org.vaadin.example.service.ITournamentService;

import java.util.Optional;

@SpringComponent
@UIScope
public class AchievementEditor extends VerticalLayout implements KeyNotifier {
    private IAchievementService achievementService;
    private ITournamentService tournamentService;
    private Achievement achievement;

    private final Dialog editorWindow = new Dialog();

    private final TextField achievementNameField = new TextField("Name","Achievement Name");
    private final ComboBox<Tournament> tournamentComboBox = new ComboBox<>("Tournament");

    private final Binder<Achievement> binder = new BeanValidationBinder<>(Achievement.class);
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    public AchievementEditor(IAchievementService achievementService, ITournamentService tournamentService) {
        this.achievementService = achievementService;
        this.tournamentService = tournamentService;

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

    public void editAchievement(Achievement achievement) {
        setItemsInComboBoxes();

        if (achievement == null) {
            return;
        } else if (achievement.getId() != null) {
            this.achievement = Optional.ofNullable(achievementService.getAchievementById(achievement.getId())).orElse(achievement);
        } else {
            this.achievement = achievement;
        }

        binder.setBean(this.achievement);
        editorWindow.open();
        achievementNameField.focus();
    }

    private void setWidthToTextField() {
        achievementNameField.setWidthFull();
        tournamentComboBox.setWidthFull();
    }

    private void setItemsInComboBoxes() {
        tournamentComboBox.setItems(tournamentService.getAll());
        tournamentComboBox.setItemLabelGenerator(gen -> gen.getName() + " (" + gen.getGame().getName() + ")");
    }

    private void configureModalWindows() {
        editorWindow.setWidth("500px");
        editorWindow.add(new VerticalLayout(
                achievementNameField,
                tournamentComboBox,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));
    }

    private void setBinderFields() {
        String emptyFieldMessage = "This field cannot be empty";

        binder.forField(achievementNameField).asRequired(emptyFieldMessage).bind(
                Achievement::getName,
                Achievement::setName
        );
        binder.forField(tournamentComboBox).asRequired(emptyFieldMessage).bind(
                Achievement::getTournament,
                Achievement::setTournament
        );
    }

    private void save() {
        if (binder.isValid()) {
            achievementService.save(achievement);
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
