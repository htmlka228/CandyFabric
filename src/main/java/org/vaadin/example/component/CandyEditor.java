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
import org.vaadin.example.entity.Candy;
import org.vaadin.example.service.CandyService;

@SpringComponent
@UIScope
public class CandyEditor extends VerticalLayout implements KeyNotifier {
    private final CandyService candyService;
    private Dialog editor = new Dialog();
    private Candy candy;

    private final TextField nameField = new TextField("Name","Candy Name");
    private final TextField priceField = new TextField("Price","Candy Price");

    private final Binder<Candy> binder = new BeanValidationBinder<>(Candy.class);

    @Autowired
    public CandyEditor(CandyService candyService) {
        this.candyService = candyService;

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
        priceField.setWidthFull();
    }

    private void configureModalWindows() {
        editor.setWidth("500px");
        editor.add(new VerticalLayout(
                nameField,
                priceField,
                DefaultLayouts.generateEditorButtons(e -> save(), e -> cancel())
        ));
    }

    private void setBinderFields() {
        String emptyFieldMessage = "This field cannot be empty";

        binder.forField(nameField).asRequired(emptyFieldMessage).bind(
                Candy::getName,
                Candy::setName
        );
        binder.forField(priceField).asRequired(emptyFieldMessage).bind(
                e -> String.valueOf(e.getPrice()),
                (candy, s) -> candy.setPrice(Double.parseDouble(s))
        );
    }

    public void editCandy(Candy candy) {
        if (candy == null) {
            return;
        } else if (candy.getId() != null) {
            //no op
        } else {
            this.candy = candy;
        }

        binder.setBean(this.candy);
        editor.open();
        nameField.focus();
    }

    private void save() {
        if (binder.isValid()) {
            candyService.save(candy);
            editor.close();
        } else {
            DefaultDialogs.generateSimpleDialog("You entered an invalid or empty value").open();
        }
    }

    private void cancel() {
        editor.close();
    }
}
