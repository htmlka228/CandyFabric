package org.vaadin.example.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.example.entity.Role;
import org.vaadin.example.entity.Supplier;
import org.vaadin.example.entity.User;
import org.vaadin.example.service.SupplierService;
import org.vaadin.example.service.UserService;

import java.util.Collections;

@SpringComponent
@UIScope
public class SupplierEditor extends VerticalLayout implements KeyNotifier {
    private final UserService userService;
    private final SupplierService supplierService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private Dialog editor = new Dialog();
    private Supplier supplier;

    private final TextField nameField = new TextField("Name","Supplier Name");

    private final Binder<Supplier> binder = new BeanValidationBinder<>(Supplier.class);

    @Autowired
    public SupplierEditor(SupplierService supplierService, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.supplierService = supplierService;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        setWidthToTextField();
        configureModalWindows();
        setBinderFields();
        setSpacing(true);

        addKeyPressListener(Key.ENTER, e -> save());
    }

    public Dialog getEditor() {
        return editor;
    }

    private void setWidthToTextField() {
        nameField.setWidthFull();
    }

    private void configureModalWindows() {
        editor.setWidth("500px");
        editor.add(new VerticalLayout(
                nameField,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));
    }

    private void setBinderFields() {
        String emptyFieldMessage = "This field cannot be empty";

        binder.forField(nameField).asRequired(emptyFieldMessage).bind(
                Supplier::getName,
                Supplier::setName
        );
    }

    public void editSupplier(Supplier supplier) {
        if (supplier == null) {
            return;
        } else if (supplier.getId() != null) {
            //no op
        } else {
            this.supplier = supplier;
        }

        binder.setBean(this.supplier);
        editor.open();
        nameField.focus();
    }

    private void save() {
        if (binder.isValid()) {
            supplierService.save(supplier);
            userService.save(User.builder()
                    .username(supplier.getName())
                    .password(bCryptPasswordEncoder.encode("supplier"))
                    .roles(Collections.singleton(Role.SUPPLIER_USER))
                    .active(true)
                    .build()
            );

            editor.close();
        } else {
            DefaultDialogs.generateSimpleDialog("You entered an invalid or empty value").open();
        }
    }

    private void cancel() {
        editor.close();
    }
}
