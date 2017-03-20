package com.kn.elephant.note.ui.editor;

import java.util.Optional;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.model.NoteType;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.NoteException;
import com.kn.elephant.note.utils.cache.NoteCache;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 10.11.15. email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NotePanel extends BasePanel {

    private DetailsNotePanel detailsNotePanel;

    private NoteCache cache = NoteCache.getInstance();

    @Inject
    private NoteService noteService;
    private NotificationPane notificationPane;

    @Setter
    private Application app;

    private Editor currentEditor;

    public NotePanel() {
        super();
        ActionMap.register(this);

        detailsNotePanel = new DetailsNotePanel();
        setTop(detailsNotePanel);

        getStyleClass().add("content-pane");
        notificationPanel();

    }

    private void notificationPanel() {

        notificationPane = new NotificationPane();
        notificationPane.showFromTopProperty().setValue(false);
        notificationPane.getStyleClass().add("notification");
        setCenter(notificationPane);
    }

    @ActionProxy(text = "loadnote")
    private void loadNote(ActionEvent event) {
        NoteDto newNote = (NoteDto) event.getSource();
        cache.loadNote(newNote);

        if (newNote.getType() == NoteType.TODO) {
            currentEditor = new TodoEditor();
        } else {
            currentEditor = new HtmlEditor();
        }
        currentEditor.setNoteCache(cache);
        cachingNoteContentChanges();
        currentEditor.loadNote(newNote);
        notificationPane.setContent((Node) currentEditor);
        log.debug("Load note: " + newNote);
        detailsNotePanel.loadNote(cache.getCurrentNoteDto());

    }

    private void cachingNoteContentChanges() {
        addEventFilter(MouseEvent.MOUSE_EXITED, event -> cache.contentNoteChanged(currentEditor.getContent()));
    }

    @ActionProxy(text = "")
    private void updateTitle(ActionEvent event) {
        log.debug("Update note title" + event.getSource());
        cache.getCurrentNoteDto().setTitle((String) event.getSource());
        saveNote();
    }

    @ActionProxy(text = "")
    private void updateDesc(ActionEvent event) {
        log.debug("Update note desc" + event.getSource());
        cache.getCurrentNoteDto().setShortDescription((String) event.getSource());
        saveNote();
    }

    @ActionProxy(text = "")
    private void saveNote() {
        try {
            cache.contentNoteChanged(currentEditor.getContent());
            // cache.noteChanged(cache.getCurrentNoteDto());
            log.debug("Save note:" + cache.getCurrentNoteDto());
            Optional<NoteDto> updatedNote = noteService.saveNote(cache.getCurrentNoteDto());
            if (updatedNote.isPresent()) {
                // cache.loadNote(updatedNote.get());
                loadNote(new ActionEvent(updatedNote.get(), null));
                ActionFactory.callAction("showNotificationPanel", new NoticeData("Note saved."));
                ActionFactory.callAction("refreshNote", cache.getCurrentNoteDto());
            } else {
                throw new NoteException("Save note operation failed.");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ActionFactory.callAction("showNotificationPanel", NoticeData.createErrorNotice("Operation saving failed"));
        }

        // refresh list panel
    }

    @ActionProxy(text = "")
    private void removeNote(ActionEvent event) {
        log.debug("Remove note:" + cache.getCurrentNoteDto());
        if (noteService.removeNote(cache.getCurrentNoteDto().getId())) {
            log.info("note was removed");
            ActionFactory.callAction("removeNoteFromList");
            ActionFactory.callAction("showNotificationPanel", new NoticeData("Note has been removed."));
        }
    }

    @ActionProxy(text = "")
    private void showNotificationPanel(ActionEvent event) throws InterruptedException {
        NoticeData noticeData = (NoticeData) event.getSource();
        notificationPane.show(noticeData.getMessage());
        notificationPane.setGraphic(noticeData.getIcon());
        hideNotifications(4000);
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