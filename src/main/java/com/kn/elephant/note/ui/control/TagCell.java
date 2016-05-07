package com.kn.elephant.note.ui.control;

import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.ui.BasePanel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

/**
 * Created by Kamil Nadłonek on 06-05-2016
 * email:kamilnadlonek@gmail.com
 */
public class TagCell extends BasePanel {
    private TagDto tagDto;

    public TagCell(TagDto tagDto) {
        this.tagDto = tagDto;
        getStyleClass().addAll("tag-cell");
        setCenter(createContent());
        ListNotesControl notesControl = new ListNotesControl(null);
        notesControl.setOwner(this);
        notesControl.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        setOnMouseClicked(event -> {
            notesControl.showNotes(tagDto.getNotes());
        });
    }

    private Node createContent() {
        VBox box = new VBox();
        Label tagNameLabel = new Label(tagDto.getName());
        tagNameLabel.getStyleClass().add("tagName");
        Text text = new Text(String.format("Used in %d notes", tagDto.getNotes().size()));
        text.getStyleClass().add("notesInfo");
        box.getChildren().addAll(tagNameLabel, text);
        return box;
    }
}
