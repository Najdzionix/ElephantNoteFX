package com.kn.elephant.note.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.action.Action;
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
        label = new Label(text);
        label.getStyleClass().add("noteTitle");
        textField = new TextField(text);
        validationSupport.registerValidator(textField, Validator.createEmptyValidator("No empty title"));
        Action editAction = ActionMap.action("editNoteTitle");
        editAction.setGraphic(Icons.EDIT_TITLE);
        editButton = ActionUtils.createButton(editAction);

        createBox(label, editButton);

    }

    private void createBox(Node... nodes) {
        getChildren().clear();
        HBox box = new HBox();
        box.setSpacing(5);
        box.getChildren().addAll(nodes);
        getChildren().add(box);
    }

    private void editMode() {
        textField.setText(label.getText());
        Action saveAction = ActionMap.action("saveNoteTitle");
        saveAction.setGraphic(Icons.SAVE_TAG);
        editButton = ActionUtils.createButton(saveAction);
        createBox(textField, editButton);
    }

    private void normalMode() {
        editButton = ActionUtils.createButton(ActionMap.action("editNoteTitle"));
        createBox(label, editButton);
    }

    @ActionProxy(text = "")
    private void editNoteTitle() {
        LOGGER.info("Execute edit title note action ");
        editMode();
    }

    @ActionProxy(text = "")
    private void saveNoteTitle() {
        LOGGER.info("Save note and send info to world ....");
        label.setText(textField.getText());
        normalMode();
    }
}
