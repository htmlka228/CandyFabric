package org.vaadin.example.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DefaultLayouts {
    public static HorizontalLayout generateEditorButtons(ComponentEventListener<ClickEvent<Button>> saveListener, ComponentEventListener<ClickEvent<Button>> cancelListener) {
        Button save = new Button("Ok", VaadinIcon.CHECK.create());
        Button cancel = new Button("Cancel");

        save.getElement().getThemeList().add("primary");

        save.addClickListener(saveListener);
        cancel.addClickListener(cancelListener);

        return new HorizontalLayout(save, cancel);
    }
}
