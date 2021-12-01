package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Team;
import org.vaadin.example.service.IPlayerService;

import java.util.List;

@SpringComponent
@UIScope
public class PreviousTeamView extends VerticalLayout {
    private final IPlayerService playerService;
    private Grid<Team> grid;
    private Dialog previousTeamDialog = new Dialog();
    private Button close = new Button("Close", VaadinIcon.CLOSE.create());

    @Autowired
    public PreviousTeamView(IPlayerService playerService) {
        this.playerService = playerService;

        configureGrid();

        close.getElement().getStyle().set("margin-left", "auto");
        close.addClickListener(e -> previousTeamDialog.close());
    }

    private void configureGrid(){
        grid =  new Grid<>(Team.class);
        grid.setColumns("id", "name", "country", "active");
    }

    public void showPreviousTeams(List<Team> previousTeams) {
        grid.setItems(previousTeams);
        previousTeamDialog.add(new VerticalLayout(close, grid));
        previousTeamDialog.setWidth("calc(100vw - (48*var(--lumo-space-m)))");

        previousTeamDialog.open();
    }
}
