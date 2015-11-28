package com.kn.elephant.note.ui.editor;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.Icons;
import com.kn.elephant.note.utils.ActionFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.NotificationPane;
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
    private NotificationPane notificationPane;

    public NotePanel(NoteDto noteDto) {
        ActionMap.register(this);
        this.currentNoteDto = noteDto;
        detailsNotePanel = new DetailsNotePanel();
        setTop(detailsNotePanel);
        //editor
        editor = new HTMLEditor();
        webView = new WebView();
        setPadding(new Insets(15));
        notificationPanel();
    }

    private void notificationPanel() {
        notificationPane = new NotificationPane();
        notificationPane.showFromTopProperty().setValue(false);
        notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
        setCenter(notificationPane);
    }


    @ActionProxy(text = "loadnote")
    private void loadNote(ActionEvent event) {
//        setCenter(null);
        notificationPane.setContent(editor);
        currentNoteDto = (NoteDto) event.getSource();
        log.debug("Load note: " + currentNoteDto);
        detailsNotePanel.loadNote(currentNoteDto);
        editor.setHtmlText("");
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
            ActionFactory.callAction("showNotificationPanel", new NoticeData("Note saved."));
        } else {
            ActionFactory.callAction("showNotificationPanel", new NoticeData("Operation saving failed", Icons.ERROR));
        }
    }

    @ActionProxy(text = "")
    private void removeNote(ActionEvent event) {
        log.debug("Remove note:" + currentNoteDto);
        if (noteService.removeNote(currentNoteDto.getId())) {
            log.info("note was removed");
            ActionFactory.callAction("removeNoteFromList");
            ActionFactory.callAction("showNotificationPanel", new NoticeData("Note has been removed."));
        }
    }

    @ActionProxy(text = "Edit mode")
    private void switchDisplayMode(ActionEvent event) {
        log.debug("Switch mode display note");
        if (((ToggleButton) event.getSource()).isSelected()) {
            editor.setHtmlText(currentNoteDto.getContent());
            notificationPane.setContent(editor);
        } else {
            webView.getEngine().loadContent(currentNoteDto.getContent());
            webView.setContextMenuEnabled(false);
            webView.setDisable(true);
            webView.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
            notificationPane.setContent(webView);
        }
    }

    @ActionProxy(text = "")
    private void showNotificationPanel(ActionEvent event) throws InterruptedException {
        NoticeData noticeData = (NoticeData) event.getSource();
        notificationPane.show(noticeData.getMessage());
        notificationPane.setGraphic(noticeData.getIcon());
        hideNotifications(3000);
    }

    private void hideNotifications(int time) {
        Runnable task = () -> {
            try {
                Thread.sleep(time);
                notificationPane.hide();
            } catch (InterruptedException e) {
                log.error("Hide notification thread error", e);
            }
        };
        new Thread(task).start();
    }

}
