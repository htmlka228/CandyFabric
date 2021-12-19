package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import org.vaadin.example.entity.CandyFabric;
import org.vaadin.example.entity.Order;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.entity.Supplier;
import org.vaadin.example.model.ComponentRequestQueue;
import org.vaadin.example.service.CandyFabricService;
import org.vaadin.example.service.ShopService;
import org.vaadin.example.service.SupplierService;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@RequiredArgsConstructor
public class OrderView {
    private Dialog orderDialog;

    private Button close = new Button("Close", VaadinIcon.CLOSE.create());

    public void showShopInProgressOrders(Shop shop) {
        List<Order> inProgressOrders = shop.getOrders().stream()
                .filter(order -> order.isInProgress() && order.isShopOrder())
                .collect(Collectors.toList());

        configureDialog(createGrid(inProgressOrders));

        orderDialog.open();
    }

    public void showShopPreviousOrders(Shop shop) {
        List<Order> previousShopOrders = shop.getOrders().stream()
                .filter(order -> order.isDelivered() && order.isShopOrder())
                .collect(Collectors.toList());

        configureDialog(createGrid(previousShopOrders));

        orderDialog.open();
    }

    public void showCandyFabricShippedOrders(CandyFabric candyFabric) {
        List<Order> shippedOrders = candyFabric.getOrders().stream()
                .filter(order -> order.isDelivered() && order.isFabricOrder())
                .collect(Collectors.toList());

        configureDialog(createGrid(shippedOrders));

        orderDialog.open();
    }

    private Grid<Order> createGrid(List<Order> orders) {
        Grid<Order> grid = new Grid<>(Order.class, false);

        grid.addColumn("id");
        grid.addColumn(order -> order.getCandy().getName()).setHeader("Candy Name");
        grid.addColumn("number");
        grid.addColumn("price");

        grid.setItems(orders);

        return grid;
    }

    private void configureDialog(Grid<Order> grid) {
        orderDialog = new Dialog();
        close.getElement().getStyle().set("margin-left", "auto");
        close.addClickListener(e -> orderDialog.close());

        orderDialog.add(new VerticalLayout(close, grid));
        orderDialog.setWidth("calc(100vw - (48*var(--lumo-space-m)))");
    }
}
