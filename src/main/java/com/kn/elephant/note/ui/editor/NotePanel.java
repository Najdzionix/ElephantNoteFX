package com.kn.elephant.note.ui.editor;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import java.util.Optional;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NotePanel extends BasePanel {

    private NoteDto currentNoteDto;
    private DetailsNotePanel detailsNotePanel;
    private HTMLEditor editor;
    private WebView webView;

    @Inject
    private NoteService noteService;

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
//        todo load title
        currentNoteDto.setContent(editor.getHtmlText());
        Optional<NoteDto> updatedNote = noteService.saveNote(currentNoteDto);
        if (updatedNote.isPresent()) {
            currentNoteDto = updatedNote.get();
            loadNote(new ActionEvent(currentNoteDto, null));
        }
//         todo else inform user save failed ..
    }

    @ActionProxy(text = "")
    private void removeNote(ActionEvent event) {
        log.debug("Remove note");
    }

    @ActionProxy(text = "Edit mode")
    private void switchDisplayMode(ActionEvent event) {
        log.info("Switch event !!!!!");
        if (((ToggleButton) event.getSource()).isSelected()) {
            webView = new WebView();
            webView.getEngine().loadContent(currentNoteDto.getContent());
            setCenter(webView);
        } else {
            editor.setHtmlText(currentNoteDto.getContent());
            setCenter(editor);
        }

    }


}
