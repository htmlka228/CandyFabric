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
import org.vaadin.example.entity.Team;
import org.vaadin.example.service.IPlayerService;
import org.vaadin.example.service.ITeamService;

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class TeamEditor extends VerticalLayout implements KeyNotifier {
    private final IPlayerService playerService;
    private final ITeamService teamService;
    private Team team;

    private final Dialog editorWindow = new Dialog();

    private final TextField teamNameField = new TextField("Team Name","Team Name");
    private final ComboBox<String> countryComboBox = new ComboBox<>("Team Country");
    private final Checkbox activeCheckBox = new Checkbox("Active", true);

    private final Binder<Team> binder = new BeanValidationBinder<>(Team.class);
    private PlayerEditor.ChangeHandler changeHandler;

    private List<String> countries;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public TeamEditor(IPlayerService playerService, ITeamService teamService, @Value("#{'${list.countries}'.split(',')}") List<String> countries) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.countries = countries;

        setWidthToTextField();
        setItemsInComboBoxes();
        configureModalWindows();
        setBinderFields();
        setSpacing(true);
        addKeyPressListener(Key.ENTER, e -> save());
    }

    public void setChangeHandler(PlayerEditor.ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public Dialog getEditorWindow() {
        return this.editorWindow;
    }

    public void editTeam(Team team) {
        setItemsInComboBoxes();

        if (team == null) {
            return;
        } else if (team.getId() != null) {
            this.team = Optional.ofNullable(teamService.getTeamById(team.getId())).orElse(team);
        } else {
            this.team = team;
        }

        binder.setBean(this.team);
        editorWindow.open();
        teamNameField.focus();
    }

    private void setWidthToTextField() {
        teamNameField.setWidthFull();
        countryComboBox.setWidthFull();
    }

    private void setItemsInComboBoxes() {
        countryComboBox.setItems(countries);
    }

    private void configureModalWindows() {
        editorWindow.setWidth("500px");
        editorWindow.add(new VerticalLayout(
                teamNameField,
                countryComboBox,
                activeCheckBox,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));
    }

    private void setBinderFields() {
        String emptyFieldMessage = "This field cannot be empty";

        binder.forField(teamNameField).asRequired(emptyFieldMessage).bind(
                Team::getName,
                Team::setName
        );
        binder.forField(countryComboBox).asRequired(emptyFieldMessage).bind(
                Team::getCountry,
                Team::setCountry
        );
        binder.forField(activeCheckBox).bind(
                Team::isActive,
                Team::setActive
        );
    }

    private void save() {
        if (binder.isValid()) {

            //TODO make change a player when changed a his team.

            teamService.save(team);
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
