package com.kn.elephant.note.ui.control;

import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Kamil Nadłonek on 24-3-16.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NoteNode extends BasePanel {

    private NoteDto noteDto;

    public NoteNode(NoteDto noteDto) {
        this.noteDto = noteDto;
        getStyleClass().add("note-node");
        setTop(getTitleNode(noteDto.getTitle()));
        setBottom(getDescNode(noteDto.getShortDescription()));
        registerListeners();
    }

    private Node getTitleNode(String title) {
        TextFlow textFlow = new TextFlow();
        Text titleText = new Text(title);
        textFlow.getChildren().add(titleText);
        return textFlow;
    }

    private Node getDescNode(String desc) {
        TextFlow descFlow = new TextFlow();
        Text descText = new Text(StringUtils.abbreviate(desc, 100));
        descFlow.getChildren().add(descText);
        return descFlow;
    }

    private void registerListeners() {
        setOnMouseClicked(event -> {
//            todo hide popover results;
            ActionFactory.callAction("loadNote", noteDto);
        });

        this.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!getStyle().contains(NoteConstants.WHITE)) {
                if (newValue) {
                    setStyle("-fx-border-color: " + NoteConstants.ORANGE_COLOR + "  -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-border-color: " + NoteConstants.GRAY_DIVIDER);
                }
            }
        });

        this.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                setStyle("-fx-border-color: " + NoteConstants.ORANGE_COLOR + "  -fx-font-weight: bold;");
            } else {
                setStyle("-fx-border-color: " + NoteConstants.GRAY_DIVIDER);
            }
        });
    }


}
