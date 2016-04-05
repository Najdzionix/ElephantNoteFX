package com.kn.elephant.note.ui.leftMenu;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.utils.ListenerFactory;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Kamil Nadłonek on 06.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NoteTreeCell extends TreeCell<NoteDto> {
    private TextFlow textFlow;
    private TextField textField;
    private VBox vBox;

    public NoteTreeCell() {}

    @Override
    public void updateItem(NoteDto item, boolean empty) {
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
                setGraphic(createNoteCell(getTreeItem()));
            }
        }
    }

    private Node createNoteCell(TreeItem<NoteDto> item) {
        NoteDto currentNoteDto = item.getValue();
        vBox = new VBox();

        vBox.hoverProperty().addListener(ListenerFactory.getListenerHover(vBox));
        this.focusedProperty().addListener(ListenerFactory.getListenerFocused(vBox));

        Text title = new Text(currentNoteDto.getTitle());
        Text desc = new Text( StringUtils.abbreviate(currentNoteDto.getShortDescription(),30));
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(title, desc);

        vBox.getChildren().addAll(title, desc);
        vBox.getStyleClass().add("noteItem");
        return vBox;
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
