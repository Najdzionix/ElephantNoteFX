package com.kn.elephant.note.ui.control;

import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.ui.BasePanel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;

/**
 * Created by Kamil Nadłonek on 21-04-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
//@EqualsAndHashCode(callSuper = false)
public class TagCell extends BasePanel {

    private TagDto tagDto;
    private boolean isShow = false;

    public TagCell(TagDto tagDto, double maxWidth, double maxHeight) {
        this.tagDto = tagDto;
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
        setCenter(createContent());
    }

    public void showNotes() {
        if (!isShow) {
            ListView<String> listNotes = new ListView<>();
            listNotes.getItems().addAll(tagDto.getNotes().parallelStream().map(noteDto -> noteDto.getTitle()).collect(Collectors.toList()));
            AnchorPane box = new AnchorPane();
            box.getChildren().add(listNotes);
            setBottom(box);
            isShow = true;
        } else {
            hideNotes();
        }
    }

    public void hideNotes() {
        setBottom(null);
        isShow = false;
    }

    private Node createContent() {
        VBox box = new VBox();
        Label tagNameLabel = new Label(tagDto.getName());
        TextFlow noteInfoFlow = new TextFlow();
        Text text = new Text("Used in ");
        Text noteAmountText = new Text(tagDto.getNotes().size() + " ");
        noteAmountText.setStyle("-fx-text-fill: red");
        Text notesText = new Text(" notes");
        noteInfoFlow.getChildren().addAll(text, noteAmountText, notesText);
        box.getChildren().addAll(tagNameLabel, noteInfoFlow);
        box.getStyleClass().addAll("testBorder");
        return box;
    }

    public boolean isShow() {
        return isShow;
    }

    @Override
    public String toString() {
        return tagDto.getName();
    }

}
