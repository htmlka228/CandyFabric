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
import org.vaadin.example.component.DefaultDialogs;
import org.vaadin.example.entity.Candy;
import org.vaadin.example.entity.CandyFabric;
import org.vaadin.example.entity.Order;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.model.ComponentRequest;
import org.vaadin.example.service.CandyFabricService;
import org.vaadin.example.service.CandyService;
import org.vaadin.example.service.OrderService;
import org.vaadin.example.service.ShopService;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;

@Route(value = "shop", layout = MainLayout.class)
@RolesAllowed({"SHOP_USER", "ADMIN"})
public class ShopView extends VerticalLayout {
    private final ShopService shopService;
    private final CandyService candyService;
    private final OrderService orderService;
    private final OrderView orderView;
    private final CandyFabricService candyFabricService;

    private Grid<Candy> grid = new Grid<>(Candy.class, true);

    private Shop shop;
    private CandyFabric candyFabric;
    private H1 balance = new H1();

    private final Button inProgressOrders = new Button("In-Progress Orders");
    private final Button previousOrders = new Button("Previous Orders");
    private final Button addMoreCoins = new Button("Add More Candy Coins");

    private final HorizontalLayout toolbar = new HorizontalLayout(inProgressOrders, previousOrders, addMoreCoins);

    @Autowired
    public ShopView(ShopService shopService, CandyService candyService, OrderService orderService, CandyFabricService candyFabricService, OrderView orderView) {
        this.shopService = shopService;
        this.candyService = candyService;
        this.orderService = orderService;
        this.candyFabricService = candyFabricService;
        this.orderView = orderView;

        shop = shopService.getShopById(1L);
        candyFabric = candyFabricService.getCandyFabricById(1L);

        setSizeFull();

        add(toolbar, configureShopInfo(), configureGrid());
        addButtonListeners();
    }

    private VerticalLayout configureShopInfo() {
        balance.setText("Candy Coins: " + shop.getBalance());

        return new VerticalLayout(
                new HorizontalLayout(new H1(shop.getName()), balance)
        );
    }

    private VerticalLayout configureGrid() {
        grid.addColumn(ButtonRendererBuilder.create("Create Order", this::showCandyNumberInOrderDialog));
        grid.setItems(candyService.getAllCandies());

        return new VerticalLayout(grid);
    }

    private void addButtonListeners() {
        inProgressOrders.addClickListener(e -> orderView.showShopInProgressOrders(shop));
        previousOrders.addClickListener(e -> orderView.showShopPreviousOrders(shop));
        addMoreCoins.addClickListener(e -> {
            Shop shop = shopService.getShopById(1L);

            shop.setBalance(shop.getBalance() + 1000.00);
            balance.setText("Candy Coins: " + shop.getBalance());
            this.shop = shop;

            shopService.save(shop);
        });
    }

    private void createOrderProcess(Candy candy, int number) {
        Order order = Order.builder()
                .candy(candy)
                .number(number)
                .price(candy.getPrice() * number)
                .inProgress(true)
                .delivered(false)
                .shopOrder(true)
                .fabricOrder(true)
                .build();

        Shop shop = shopService.getShopById(1L);
        CandyFabric candyFabric = candyFabricService.getCandyFabricById(1L);

        shop.getOrders().add(order);
        candyFabric.getOrders().add(order);
        shop.setBalance(shop.getBalance() - order.getPrice());

        orderService.save(order);
        shopService.save(shop);
        candyFabricService.save(candyFabric);

        balance.setText("Candy Coins: " + shop.getBalance());

        this.shop = shop;

        DefaultDialogs.generateSimpleDialog("Order has been created").open();
    }

    private void showCandyNumberInOrderDialog(Candy candy) {
        Dialog dialog = new Dialog();
        Button close = new Button("Close", clickEvent -> dialog.close());
        ComboBox<Integer> integerComboBox = new ComboBox<>("Number of Components", Arrays.asList(1, 2, 3, 4, 5));
        integerComboBox.setRequired(true);
        Button send = new Button("Send", sendEvent -> {
            dialog.close();
            createOrderProcess(candy, integerComboBox.getValue());
        });

        dialog.add(new VerticalLayout(close, integerComboBox, send));
        dialog.open();
    }

    private void updateGrid() {
        grid.setItems(candyService.getAllCandies());
    }
}
