package com.kn.elephant.note.ui.control;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.ListenerFactory;
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
            ActionFactory.callAction("loadNote", noteDto);
            ActionFactory.callAction("clearSelectedNoteNodes");
            getStyleClass().add("selected-node");
        });

        this.hoverProperty().addListener(ListenerFactory.getListenerHover(this));
    }
}
