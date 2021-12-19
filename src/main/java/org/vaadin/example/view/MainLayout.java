package org.vaadin.example.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.example.security.SecurityService;

import javax.annotation.security.PermitAll;

@Secured("SHOP_ROLE")
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
        RouterLink shopLink = new RouterLink("Shop Panel", ShopView.class);
        RouterLink fabric = new RouterLink("Fabric Panel", CandyFabricView.class);
        RouterLink supplier = new RouterLink("Supplier Panel", SupplierView.class);
        RouterLink admin = new RouterLink("Admin Panel", AdminView.class);

        shopLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                shopLink,
                fabric,
                supplier,
                admin
        ));
    }
}
