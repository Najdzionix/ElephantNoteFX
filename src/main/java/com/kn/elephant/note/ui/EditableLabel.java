package com.kn.elephant.note.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class EditableLabel extends Region {

    private static final Logger LOGGER = LogManager.getLogger(EditableLabel.class);

    private Label label;
    private TextField textField;
    private Button editButton;
    private ValidationSupport validationSupport = new ValidationSupport();

    public EditableLabel(String text) {
        ActionMap.register(this);
        HBox box = new HBox();
        label = new Label(text);
        label.getStyleClass().add("noteTitle");
        textField = new TextField(text);
        validationSupport.registerValidator(textField, Validator.createEmptyValidator("No empty title"));
        editButton = ActionUtils.createButton(ActionMap.action("editNoteTitle"));

        box.getChildren().addAll(label, editButton);
        getChildren().add(box);
    }

    private void editMode() {
        getChildren().clear();
        HBox box = new HBox();
        textField.setText(label.getText());

        editButton = ActionUtils.createButton(ActionMap.action("saveNoteTitle"));
        box.getChildren().addAll(textField, editButton);
        getChildren().add(box);
    }

    private void normalMode() {
        getChildren().clear();
        HBox box = new HBox();
        editButton = ActionUtils.createButton(ActionMap.action("editNoteTitle"));
        box.getChildren().addAll(label, editButton);
        getChildren().add(box);
    }

    @ActionProxy(text = "Edit title ...")
    private void editNoteTitle() {
        LOGGER.info("Execute edit title note action ");
        editMode();
    }

    @ActionProxy(text = "Save")
    private void saveNoteTitle() {
        LOGGER.info("Save note and send info to world ....");
        label.setText(textField.getText());
        normalMode();
    }
}
