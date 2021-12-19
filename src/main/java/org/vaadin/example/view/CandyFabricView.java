package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.component.ButtonRendererBuilder;
import org.vaadin.example.component.CandyEditor;
import org.vaadin.example.component.DefaultDialogs;
import org.vaadin.example.entity.Candy;
import org.vaadin.example.entity.CandyFabric;
import org.vaadin.example.entity.Order;
import org.vaadin.example.entity.Supplier;
import org.vaadin.example.model.ComponentRequest;
import org.vaadin.example.model.ComponentRequestQueue;
import org.vaadin.example.service.CandyFabricService;
import org.vaadin.example.service.OrderService;
import org.vaadin.example.service.SupplierService;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "fabric", layout = MainLayout.class)
@RolesAllowed({"FABRIC_USER", "ADMIN"})
public class CandyFabricView extends VerticalLayout {
    private final OrderService orderService;
    private final SupplierService supplierService;
    private final OrderView orderView;
    private final CandyFabricService candyFabricService;
    private ComponentRequestQueue componentRequestQueue;
    private CandyEditor candyEditor;

    private Grid<Order> grid = new Grid<>(Order.class, false);

    private CandyFabric candyFabric;

    private H1 header;

    private final Button previousOrders = new Button("Previous Orders");
    private final Button orderComponents = new Button("Order the components");
    private final Button addCandy = new Button("New Candy");

    private final HorizontalLayout toolbar = new HorizontalLayout(previousOrders, orderComponents, addCandy);

    @Autowired
    public CandyFabricView(OrderService orderService, CandyFabricService candyFabricService, SupplierService supplierService, OrderView orderView, ComponentRequestQueue componentRequestQueue, CandyEditor editor) {
        this.orderService = orderService;
        this.candyFabricService = candyFabricService;
        this.supplierService = supplierService;
        this.orderView = orderView;
        this.componentRequestQueue = componentRequestQueue;
        this.candyEditor = editor;

        setSizeFull();

        add(toolbar, configureCandyFabricInfo(), configureGrid());
        addButtonListeners();
    }

    private VerticalLayout configureCandyFabricInfo() {
        candyFabric = candyFabricService.getCandyFabricById(1L);
        header = new H1(candyFabric.getName() + " - Components left: " + candyFabric.getComponents());

        return new VerticalLayout(
                new HorizontalLayout(header)
        );
    }

    private VerticalLayout configureGrid() {
        grid.addColumn("id");
        grid.addColumn(order -> order.getCandy().getName()).setHeader("Candy Name");
        grid.addColumn("number");
        grid.addColumn("price");
        grid.addColumn(ButtonRendererBuilder.create("Ship Order", this::shipOrderProcess));
        updateItems();

        return new VerticalLayout(grid);
    }

    private void addButtonListeners() {
        previousOrders.addClickListener(e -> orderView.showCandyFabricShippedOrders(candyFabric));
        orderComponents.addClickListener(e -> {
            Dialog dialog = new Dialog();
            Button close = new Button("Close", clickEvent -> dialog.close());
            ComboBox<Integer> integerComboBox = new ComboBox<>("Number of Components", Arrays.asList(1, 2, 3, 4, 5));
            integerComboBox.setRequired(true);
            ComboBox<Supplier> supplierComboBox = new ComboBox<>("Supplier", supplierService.getAll());
            supplierComboBox.setItemLabelGenerator(Supplier::getName);
            supplierComboBox.setRequired(true);
            Button send = new Button("Send", sendEvent -> {
                componentRequestQueue.getComponentRequests().add(ComponentRequest.builder()
                        .candyFabric(candyFabric)
                        .numberComponents(integerComboBox.getValue())
                        .supplier(supplierComboBox.getValue())
                        .processed(false)
                        .build());
            dialog.close();
            });

            dialog.add(new VerticalLayout(close, integerComboBox, supplierComboBox, send));
            dialog.open();
        });
        addCandy.addClickListener(e -> candyEditor.editCandy(new Candy()));
    }

    private void shipOrderProcess(Order order) {
        if (candyFabric.getComponents() - order.getNumber() < 0) {
            DefaultDialogs.generateSimpleDialog("Order hasn't been shipped. Out of components. Please, order more components.").open();
            return;
        }

        order.setInProgress(false);
        order.setDelivered(true);

        orderService.save(order);

        candyFabric.setComponents(candyFabric.getComponents() - order.getNumber());
        candyFabricService.save(candyFabric);

        DefaultDialogs.generateSimpleDialog("Order has been shipped").open();
        updateItems();
    }

    public void updateItems() {
        candyFabric =  candyFabricService.getCandyFabricById(1L);
        header.setText(candyFabric.getName() + " - Components left: " + candyFabric.getComponents());

        List<Order> inProgressCandyFabricOrders = candyFabric.getOrders().stream()
                .filter(order -> order.isInProgress() && order.isFabricOrder())
                .collect(Collectors.toList());

        grid.setItems(inProgressCandyFabricOrders);
    }
}
