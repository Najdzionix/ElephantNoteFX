package com.kn.elephant.note.ui.control;

import org.controlsfx.control.PopOver;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.service.TagService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 06-05-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class TagCell extends BasePanel {
    private TagDto tagDto;

    @Inject
    private TagService tagService;

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
        BorderPane testContent = new BorderPane();
        testContent.setCenter(box);
        testContent.setRight(createDeleteButton());
        return testContent;
    }

    private Button createDeleteButton() {
        Button deleteB = new Button();
        Icons.addIcon(MaterialDesignIcon.CLOSE_OCTAGON, deleteB, "1.7em");
        deleteB.setAlignment(Pos.CENTER_RIGHT);
        deleteB.setOnAction(event -> {
            log.info("Delete tag:" + tagDto);
            tagService.removeTag(tagDto.getId());  //TODO notification about result delete operation
            ActionFactory.callAction("refreshListTags");
        });
        return deleteB;
    }
}
