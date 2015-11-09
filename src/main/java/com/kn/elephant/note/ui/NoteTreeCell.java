package com.kn.elephant.note.ui;

import com.kn.elephant.note.model.Note;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created by Kamil Nadłonek on 06.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NoteTreeCell extends TreeCell<Note> {
    private TextFlow textFlow;
    private TextField textField;

    public NoteTreeCell() {}

    @Override
    public void updateItem(Note item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
//                setText(getString());
                setGraphic(test(getTreeItem()));
            }
        }
    }

    private Node test(TreeItem<Note> item) {
        Note currentNote = item.getValue();
        VBox vBox = new VBox();
        Text textBold = new Text(currentNote.getTitle());
        String family = "Helvetica";
        textBold.setFont(Font.font(family, FontWeight.BOLD, 14));
        Text normal = new Text(currentNote.getShortDescription());
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(textBold, normal);

        vBox.getChildren().addAll(textBold, normal);
        vBox.getStyleClass().add("noteItem");
        return vBox;

    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
