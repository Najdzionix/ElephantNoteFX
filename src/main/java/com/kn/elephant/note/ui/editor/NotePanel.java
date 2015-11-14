package com.kn.elephant.note.ui.editor;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.web.HTMLEditor;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NotePanel extends BasePanel {

    private NoteDto currentNoteDto;
    private DetailsNotePanel detailsNotePanel;
    private HTMLEditor editor;

    public NotePanel(NoteDto noteDto) {
        ActionMap.register(this);
        this.currentNoteDto = noteDto;
        detailsNotePanel = new DetailsNotePanel();
        setTop(detailsNotePanel);
        //editor
        editor = new HTMLEditor();
        setCenter(editor);
        setPadding(new Insets(15));

    }

    @ActionProxy(text = "loadnote")
    private void loadNote(ActionEvent event) {
        log.debug("LOAD NOTE");

        currentNoteDto = (NoteDto) event.getSource();
        detailsNotePanel.loadNote(currentNoteDto);
        editor.setHtmlText(currentNoteDto.getContent());
    }

    @ActionProxy(text = "")
    private void saveNote(ActionEvent event) {
        log.debug("Save note");
    }

    @ActionProxy(text = "")
    private void removeNote(ActionEvent event) {
        log.debug("Remove note");
    }


}
