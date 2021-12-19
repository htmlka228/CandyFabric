package org.vaadin.example.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;
import org.vaadin.example.component.ButtonRendererBuilder;
import org.vaadin.example.component.DefaultDialogs;
import org.vaadin.example.controller.UserController;
import org.vaadin.example.entity.CandyFabric;
import org.vaadin.example.entity.Supplier;
import org.vaadin.example.model.ComponentRequest;
import org.vaadin.example.model.ComponentRequestQueue;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.CandyFabricService;
import org.vaadin.example.service.SupplierService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.stream.Collectors;

@Route(value = "supplier", layout = MainLayout.class)
@RolesAllowed({"SUPPLIER_USER", "ADMIN"})
public class SupplierView extends VerticalLayout {
    private final SupplierService supplierService;
    private final CandyFabricService candyFabricService;
    private final ComponentRequestQueue componentRequestQueue;
    private final SecurityService securityService;
    private final UserDetails userDetails;
//    private final User user;

    private Grid<ComponentRequest> grid = new Grid<>(ComponentRequest.class, false);

    private Supplier supplier;

    @Autowired
    public SupplierView(SupplierService supplierService, CandyFabricService candyFabricService, ComponentRequestQueue componentRequestQueue, SecurityService securityService) {
        this.supplierService = supplierService;
        this.candyFabricService = candyFabricService;
        this.componentRequestQueue = componentRequestQueue;
        this.securityService = securityService;

        userDetails = securityService.getAuthenticatedUser();

        setSizeFull();

        add(configureSupplierInfo(), configureGrid());
    }

    private VerticalLayout configureSupplierInfo() {
        if (userDetails.getUsername().equals("admin")) {
            return new VerticalLayout(
                    new HorizontalLayout(new H1(userDetails.getUsername().toUpperCase() + " Supplier Panel"))
            );
        } else {
            supplier = supplierService.getSupplierByName(userDetails.getUsername());

            return new VerticalLayout(
                    new HorizontalLayout(new H1(supplier.getName()))
            );
        }
    }

    private VerticalLayout configureGrid() {
        grid.addColumn(e -> e.getCandyFabric().getName()).setHeader("Candy Fabric");
        grid.addColumn(ComponentRequest::getNumberComponents).setHeader("Number Components");
        grid.addColumn(ComponentRequest::isProcessed).setHeader("Processed");
        grid.addColumn(ButtonRendererBuilder.create("Send Components", this::componentRequestProcess));

        if (userDetails.getUsername().equals("admin")) {
            grid.setItems(componentRequestQueue.getComponentRequests());
        } else {
            grid.setItems(componentRequestQueue.getComponentRequests().stream()
                    .filter(e -> !e.isProcessed() && e.getSupplier().getName().equals(supplier.getName()))
                    .collect(Collectors.toList())
            );
        }

        return new VerticalLayout(grid);
    }

    private void componentRequestProcess(ComponentRequest componentRequest) {
        CandyFabric candyFabric = candyFabricService.getCandyFabricById(1L);

        candyFabric.setComponents(candyFabric.getComponents() + componentRequest.getNumberComponents());
        candyFabricService.save(candyFabric);

        componentRequest.setProcessed(true);

        componentRequestQueue.getComponentRequests().remove(componentRequest);
        componentRequestQueue.getComponentRequests().add(componentRequest);

        DefaultDialogs.generateSimpleDialog("Components has been sent").open();
        updateGrid();
    }

    private void updateGrid() {
        grid.setItems(componentRequestQueue.getComponentRequests().stream()
                .filter(e -> !e.isProcessed())
                .collect(Collectors.toList())
        );
    }
}
