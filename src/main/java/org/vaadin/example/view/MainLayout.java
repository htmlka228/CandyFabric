package org.vaadin.example.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.security.SecurityService;

import javax.annotation.security.PermitAll;

@PermitAll
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        createHeader();
        createDrawer();
    }

    private void createHeader() {
        Button logout = new Button("logout", e -> securityService.logout());
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logout);
        header.setClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink playerLink = new RouterLink("Players", PlayerView.class);
        RouterLink teamLink = new RouterLink("Teams", TeamView.class);
        RouterLink gameLink = new RouterLink("Games", GameView.class);
        RouterLink tournamentLink = new RouterLink("Tournaments", TournamentView.class);
        RouterLink achievementLink = new RouterLink("Achievements", AchievementView.class);

        playerLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                playerLink,
                teamLink,
                gameLink,
                tournamentLink,
                achievementLink
        ));
    }
}
