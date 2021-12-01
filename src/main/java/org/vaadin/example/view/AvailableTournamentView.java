package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Tournament;
import org.vaadin.example.service.IGameService;
import org.vaadin.example.service.ITournamentService;

import javax.annotation.security.PermitAll;
import java.util.List;

@SpringComponent
@UIScope
@PermitAll
public class AvailableTournamentView extends VerticalLayout {
    private final IGameService gameService;
    private final ITournamentService tournamentService;

    private Grid<Tournament> grid;

    private Dialog availableTournamentsDialog = new Dialog();
    private Button close = new Button("Close", VaadinIcon.CLOSE.create());

    @Autowired
    public AvailableTournamentView(IGameService gameService, ITournamentService tournamentService) {
        this.gameService = gameService;
        this.tournamentService = tournamentService;

        configureGrid();

        close.getElement().getStyle().set("margin-left", "auto");
        close.addClickListener(e -> availableTournamentsDialog.close());
    }

    private void configureGrid(){
        grid =  new Grid<>(Tournament.class);
        grid.setColumns("id", "name");
        grid.addColumn(tournament -> tournament.getGame().getName()).setHeader("Game");
    }

    public void showAvailableTournaments(List<Tournament> availableTournaments) {
        grid.setItems(availableTournaments);
        availableTournamentsDialog.add(new VerticalLayout(close, grid));
        availableTournamentsDialog.setWidth("calc(100vw - (48*var(--lumo-space-m)))");

        availableTournamentsDialog.open();
    }
}
