package com.kn.elephant.note.ui.editor;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.model.NoteType;
import com.kn.elephant.note.notification.ActionNoticeData;
import com.kn.elephant.note.notification.NotificationAction;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.NoteException;
import com.kn.elephant.note.utils.cache.NoteCache;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
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
        log.debug("User want to remove note:" + cache.getCurrentNoteDto());
        NotificationAction buttonAction = () -> {
            log.info("Restore note action.");
            ActionFactory.callAction("refreshList");
            notificationPane.hide();
        };
        BiConsumer<NoteDto, Boolean> hide = (noteDto, isButtonActionWasFire) -> {
            if (!isButtonActionWasFire) {
                log.info("Remove note permanently." + noteDto);
                noteService.removeNote(noteDto.getId());
            }

        };
        ActionNoticeData noticeData = new ActionNoticeData("Note will be remove, Do you want cancel operation?");
        noticeData.setButtonAction(buttonAction).setButtonName("Restore note").setHideAfterAction(hide);
        noticeData.setNoteDto(cache.getCurrentNoteDto());
        ActionFactory.callAction("removeNoteFromList");
        ActionFactory.callAction("showNotificationPanel", noticeData);

    }

    @ActionProxy(text = "")
    private void showNotificationPanel(ActionEvent event) throws InterruptedException {
        NoticeData noticeData = (NoticeData) event.getSource();
        Text label = new Text(noticeData.getMessage());
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(noticeData.getIcon(), label);
        log.info("Notice type:" + noticeData.getType());
        if (ActionNoticeData.ACTION_NOTICE_TYPE.equals(noticeData.getType())) {
            ActionNoticeData actionNoticeData = (ActionNoticeData) noticeData;
            Button restoreButton = new Button(actionNoticeData.getButtonName());
            restoreButton.setOnAction(eventButton -> {
                actionNoticeData.callAction();
            });
            box.getChildren().add(restoreButton);
        }
        notificationPane.setGraphic(box);
        notificationPane.show();
        hideNotifications(noticeData);
    }

    private void hideNotifications(NoticeData noticeData) {
        Runnable task = () -> {
            try {
                Thread.sleep(noticeData.getDisplayTime());
                notificationPane.hide();
                if (ActionNoticeData.ACTION_NOTICE_TYPE.equals(noticeData.getType())) {
                    log.info("Normal hide notification ");
                    ((ActionNoticeData) noticeData).callHideAction();
                }

            } catch (InterruptedException e) {
                log.error("Hide notification thread error", e);
            }
        };
        new Thread(task).start();
    }
}