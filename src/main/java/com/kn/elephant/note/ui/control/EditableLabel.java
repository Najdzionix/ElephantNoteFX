package com.kn.elephant.note.ui.control;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kn.elephant.note.ui.ChangeValue;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.validator.Validator;
import com.kn.elephant.note.utils.validator.ValidatorHelper;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
@Getter
@Setter
public class EditableLabel extends Region {

    private static final Logger LOGGER = LogManager.getLogger(EditableLabel.class);
    public static final String SIZE_BUTTON = "1.1em";

    private String fullText;
    private int maxWidthText = Integer.MAX_VALUE;
    private Label labelText;
    private TextField editTextField;
    private Button editButton;
    private Button saveChangeButton;
    private ValidatorHelper validatorHelper = new ValidatorHelper();
    public  EditableLabel(String text, ChangeValue saveAction) {
        this(text, Integer.MAX_VALUE, saveAction);
    }

    public EditableLabel(String text, int maxWidthText, ChangeValue<String> saveActionFun) {
        fullText = text;
        this.maxWidthText = maxWidthText;
        labelText = new Label(StringUtils.abbreviate(fullText, maxWidthText));
        editTextField = new TextField(fullText);
        addCssClass("editable-label");
        createEditButton();
        createSaveButton(saveActionFun);
        activeNormalMode();

        validatorHelper.registerEmptyValidator(editTextField, "Field can not be empty.");
        editTextField.setOnAction(event -> saveAction(saveActionFun));
        editTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            // check if focus gained or lost
            if (!newValue && validatorHelper.isValid()) {
                activeNormalMode();
            }
        });
    }

    private void activeEditMode() {
        createContent(editTextField, saveChangeButton);
        editTextField.requestFocus();
    }

    private void activeNormalMode() {
        createContent(labelText, editButton);
    }

    private void createContent(Node... nodes) {
        getChildren().clear();
        HBox box = new HBox();
        box.setSpacing(5);
        box.getChildren().addAll(nodes);
        getChildren().add(box);
    }

    public void addCssClass(String cssClass) {
        this.getStyleClass().add(cssClass);
    }

    private void createEditButton() {
        editButton = new Button();
        Icons.addIcon(MaterialDesignIcon.PENCIL, editButton, SIZE_BUTTON);
        editButton.getStyleClass().add("button");
        editButton.setOnAction(event -> activeEditMode());
    }

    private void createSaveButton(ChangeValue<String> saveAction) {
        saveChangeButton = new Button();
        Icons.addIcon(MaterialDesignIcon.CHECK, saveChangeButton, SIZE_BUTTON);
        saveChangeButton.setOnAction(event -> saveAction(saveAction));
    }

    private void saveAction(ChangeValue<String> saveActionFun) {
        if (validatorHelper.isValid()) {
            saveActionFun.changeValue(fullText, editTextField.getText());
            labelText.setText(StringUtils.abbreviate(editTextField.getText(), maxWidthText));
            activeNormalMode();
        }
    }

    public void registerCustomValidator(String errorMessage, Validator validator) {
        validatorHelper.registerCustomValidator(editTextField, errorMessage, validator);
    }
}