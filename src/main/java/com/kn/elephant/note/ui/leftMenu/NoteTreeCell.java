package com.kn.elephant.note.ui.leftMenu;

import org.apache.commons.lang3.StringUtils;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.utils.ListenerFactory;

import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by Kamil Nadłonek on 06.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NoteTreeCell extends TreeCell<NoteDto> {
    public NoteTreeCell() {
    }

    @Override
    public void updateItem(NoteDto item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || isEditing()) {
            setText(null);
            setGraphic(null);
        } else {
            ;
            setGraphic(createNoteCell(getTreeItem()));
        }
    }

    private Node createNoteCell(TreeItem<NoteDto> item) {
        NoteDto currentNoteDto = item.getValue();
        VBox vBox = new VBox();

        vBox.hoverProperty().addListener(ListenerFactory.getListenerHover(vBox));
        this.focusedProperty().addListener(ListenerFactory.getListenerFocused(vBox));

        Text title = new Text(currentNoteDto.getTitle());
        Text desc = new Text(StringUtils.abbreviate(currentNoteDto.getShortDescription(), 30));
        vBox.getChildren().addAll(title, desc);
        vBox.getStyleClass().add("noteItem");
        return vBox;
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
