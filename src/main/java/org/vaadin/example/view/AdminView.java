package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.component.SupplierEditor;
import org.vaadin.example.entity.Supplier;

import javax.annotation.security.RolesAllowed;

@Route(value = "admin", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class AdminView extends VerticalLayout {
    private final SupplierEditor editor;

    private final Button newSupplierButton = new Button("New Supplier");
    private final HorizontalLayout toolbar = new HorizontalLayout(newSupplierButton);

    @Autowired
    public AdminView(SupplierEditor supplierEditor) {
        this.editor = supplierEditor;

        add(toolbar);
        newSupplierButton.addClickListener(e -> editor.editSupplier(new Supplier()));
    }
}
