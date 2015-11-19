package com.kn.elephant.note.ui.editor;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
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
        webView = new WebView();
        setPadding(new Insets(15));
    }

    @ActionProxy(text = "loadnote")
    private void loadNote(ActionEvent event) {
        setCenter(null);
        setCenter(editor);
        currentNoteDto = (NoteDto) event.getSource();
        log.debug("Load note: " + currentNoteDto);
        detailsNotePanel.loadNote(currentNoteDto);
        editor.setHtmlText(currentNoteDto.getContent());
    }


    @ActionProxy(text = "")
    private void updateTitle(ActionEvent event) {
        log.debug("Update note title" + event.getSource());
        currentNoteDto.setTitle((String) event.getSource());
        log.info(currentNoteDto);
    }

    @ActionProxy(text = "")
    private void saveNote(ActionEvent event) {
//        todo load title
        currentNoteDto.setContent(editor.getHtmlText());
        log.debug("Save note:" + currentNoteDto);
        Optional<NoteDto> updatedNote = noteService.saveNote(currentNoteDto);
        if (updatedNote.isPresent()) {
            currentNoteDto = updatedNote.get();
            loadNote(new ActionEvent(currentNoteDto, null));
        }
//         todo else inform user save failed ..
    }

    @ActionProxy(text = "")
    private void removeNote(ActionEvent event) {
        log.debug("Remove note:" + currentNoteDto);
        if (noteService.removeNote(currentNoteDto.getId())) {
            log.info("note was removed");
            ActionMap.action("removeNoteFromList").handle(null);
//            todo show notification
        }
    }

    @ActionProxy(text = "Edit mode")
    private void switchDisplayMode(ActionEvent event) {
        log.debug("Switch mode display note");
        if (((ToggleButton) event.getSource()).isSelected()) {
            editor.setHtmlText(currentNoteDto.getContent());
            setCenter(editor);
        } else {
            webView.getEngine().loadContent(currentNoteDto.getContent());
            webView.setContextMenuEnabled(false);
            webView.setDisable(true);
            webView.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
            setCenter(webView);
        }
    }


}
