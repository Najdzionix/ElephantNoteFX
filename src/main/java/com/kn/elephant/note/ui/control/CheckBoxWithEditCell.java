package com.kn.elephant.note.ui.control;

import java.util.function.Consumer;

import com.kn.elephant.note.ui.ChangeValue;
import com.kn.elephant.note.utils.Icons;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 22-03-2017 email:kamilnadlonek@gmail.com
 */
@Log4j2
@Getter
public class CheckBoxWithEditCell<T extends CheckBoxCell> extends ListCell<T> {

    private static final int PADDING = 130;
    private final CheckBox checkBox;
    private ChangeValue<String> saveAction;
    private Consumer<T> deleteAction;

    public CheckBoxWithEditCell(Consumer<T> deleteAction) {
        this.deleteAction = deleteAction;
        checkBox = new CheckBox();
        getStyleClass().add("check-box-cell");
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            saveAction = (oldValue, newValue) -> item.setContent(newValue);
            checkBox.setSelected(item.isCheck());
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                item.setCheck(newValue);
            });
            setGraphic(buildUI(item));
            setText(null);
        } else {
            setGraphic(null);
            setText(null);
        }
    }

    private Node buildUI(T item) {
        HBox pane = new HBox();
        pane.setSpacing(2);
        EditableLabel label = new EditableLabel(item.getContent(), getSaveAction());
        label.getLabelText().setWrapText(true);
        Parent parent = getParent().getParent().getParent();
        setWithLabel(label, parent.getBoundsInParent().getWidth() - PADDING);
        parent.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            double newWidth = newValue.getWidth() - PADDING;
            setWithLabel(label, newWidth);
        });
        Button deleteButton = new Button();
        Icons.addIcon(MaterialDesignIcon.CLOSE, deleteButton, "1.2em");
        deleteButton.setOnAction(event -> {
            deleteAction.accept(item);
        });
        pane.getChildren().addAll(checkBox, label, deleteButton);
        return pane;
    }

    private static void setWithLabel(EditableLabel label, double width) {
        label.getLabelText().prefWidthProperty().bind(new SimpleDoubleProperty(width));
        label.getEditTextField().prefWidthProperty().bind(new SimpleDoubleProperty(width));
    }
}