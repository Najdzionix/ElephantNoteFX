package com.kn.elephant.note.ui.leftMenu;

import org.apache.commons.lang3.StringUtils;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.ListenerFactory;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by Kamil Nadłonek on 06.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NoteTreeCell extends TreeCell<NoteDto> {

    private static final String ICON_SIZE = "1.7em";

    public NoteTreeCell() {
    }

    @Override
    public void updateItem(NoteDto item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || isEditing()) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(createNoteCell(getTreeItem()));
        }
    }

    private Node createNoteCell(TreeItem<NoteDto> item) {
        NoteDto currentNoteDto = item.getValue();
        HBox box = new HBox();

        box.hoverProperty().addListener(ListenerFactory.getListenerHover(box));
        this.focusedProperty().addListener(ListenerFactory.getListenerFocused(box));

        Text title = new Text(currentNoteDto.getTitle());
        Text desc = new Text(StringUtils.abbreviate(currentNoteDto.getShortDescription(), 30));
        desc.getStyleClass().add("smallText");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(title, desc);

        String iconName = item.getValue().getIcon();
        if(iconName != null ) {
            Label iconLabel = Icons.builderIcon(MaterialIcon.valueOf(iconName), ICON_SIZE);
            iconLabel.setStyle(" -fx-fill: #"+item.getValue().getColorIcon() +" ;");
            box.getChildren().add(iconLabel);
        }
        box.getChildren().add(vBox);
        box.getStyleClass().add("noteItem");
        return box;
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
