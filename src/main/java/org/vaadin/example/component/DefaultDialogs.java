package org.vaadin.example.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultDialogs {
    public static Dialog generateSimpleDialog(String message) {
        Dialog dialog = new Dialog();
        Button closeButton = new Button("Close", VaadinIcon.CLOSE.create());

        dialog.add(new VerticalLayout(closeButton, new H1(message)));
        closeButton.getElement().getStyle().set("margin-left", "auto");

        closeButton.addClickListener(e -> dialog.close());

        return dialog;
    }
}
