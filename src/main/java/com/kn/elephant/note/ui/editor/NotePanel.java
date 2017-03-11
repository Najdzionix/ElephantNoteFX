package com.kn.elephant.note.ui.editor;

import java.util.Optional;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.LinkUtils;
import com.kn.elephant.note.utils.cache.NoteCache;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSException;

/**
 * Created by Kamil Nadłonek on 10.11.15. email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NotePanel extends BasePanel {

    private DetailsNotePanel detailsNotePanel;
    private HTMLEditor editor;
    private WebView webView;
    private Button insertLinkButton;

    private NoteCache cache = NoteCache.getInstance();
    private TableGenerator tableGenerator;
    @Inject
    private NoteService noteService;
    private NotificationPane notificationPane;
    private Button tableButton;
    @Setter
    private Application app;

    public NotePanel() {
        super();
        ActionMap.register(this);

        detailsNotePanel = new DetailsNotePanel();
        setTop(detailsNotePanel);
        editor = new HTMLEditor();
        webView = new WebView();
        getStyleClass().add("content-pane");
        notificationPanel();
        addButtonsToToolbar();

        tableGenerator = new TableGenerator(tableButton);
        cachingNoteContentChanges();
        handleOpenLinksAction();
        httpWatcher();
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

        notificationPane.setContent(editor);
        log.debug("Load note: " + newNote);
        detailsNotePanel.loadNote(cache.getCurrentNoteDto());
        editor.setHtmlText("");
        editor.setHtmlText(cache.getCurrentNoteDto().getContent());

    }

    private void addButtonsToToolbar() {
        Node node = editor.lookup(".top-toolbar");
        if (node instanceof ToolBar) {
            createButtons((ToolBar) node);
        }
    }

    private void createButtons(ToolBar toolBar) {
        Button saveButton = createToolBarButton("saveNote", MaterialDesignIcon.CONTENT_SAVE);
        Button removeButton = createToolBarButton("removeNote", MaterialIcon.DELETE);
        Button testButton = createToolBarButton("insertDiv", MaterialIcon.ADD);
        insertLinkButton = createToolBarButton("insertLink", MaterialDesignIcon.LINK_VARIANT);
        tableButton = createToolBarButton("insertTable", MaterialDesignIcon.GRID);

        editor.setOnMouseClicked((MouseEvent event) -> {
            final int clickCount = event.getClickCount();
            if (clickCount == 2) {
                insertLinkButton.getStyleClass().remove("disableButton");
            }
        });

        toolBar.getItems().addAll(saveButton, new Separator(), removeButton, new Separator(), insertLinkButton, tableButton, new Separator(),testButton);
    }

    private static Button createToolBarButton(String actionName, GlyphIcons icon) {
        final String sizeIcon = "1.3em";
        Button saveButton = ActionFactory.createButtonWithAction(actionName);
        Icons.addIcon(icon, saveButton, sizeIcon);
        return saveButton;
    }

    private void cachingNoteContentChanges() {
        editor.addEventFilter(MouseEvent.MOUSE_EXITED, event -> cache.noteChanged(editor.getHtmlText()));
    }

    private void handleOpenLinksAction() {
        WebView webView = (WebView) editor.lookup("WebView");
        WebEngine engine = webView.getEngine();
        engine.locationProperty().addListener((ov, oldLoc, loc) -> {
            if (LinkUtils.isUrl(loc)) {
                log.info("Open link {} browser ", loc);
                app.getHostServices().showDocument(loc);
                loadNote(new ActionEvent(cache.getCurrentNoteDto(), null));
            }

        });
    }

    private void httpWatcher() {
        editor.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String content = LinkUtils.replaceLinkOnHtmlLinkTag(editor.getHtmlText());
                if (!editor.getHtmlText().equals(content)) {
                    cache.noteChanged(content);
                    Platform.runLater(() -> {
                        editor.requestFocus();
                        WebView webView = (WebView) editor.lookup("WebView");
                        webView.getEngine().loadContent(content);
                        String script = "document.querySelector('div').focus();";
                        webView.getEngine().executeScript(script);
                    });
                }
            }
        });
    }

    @ActionProxy(text = "")
    private void insertLink() {
        log.debug("Insert link");
        WebView webView = (WebView) editor.lookup("WebView");
        String selected = (String) webView.getEngine().executeScript("window.getSelection().toString();");

        if (selected != null && !selected.isEmpty()) {
            String url = "\"http://" + selected + "\"; \n";
            log.debug(url);
            String script = " var range = window.getSelection().getRangeAt(0);\n" + "var selectionContents = range.extractContents();\n"
                    + "var div = document.createElement(\"a\");\n" + "var url = " + url + "div.href=url;\n" + "div.appendChild(selectionContents);\n"
                    + "range.insertNode(div);";
            webView.getEngine().executeScript(script);
        }
        insertLinkButton.getStyleClass().add("disableButton");
    }

    @ActionProxy(text = "")
    private void insertTable() {
        tableGenerator.insertTable(this::insertHtml);
    }

    private void insertHtml(String html) {
        WebView webView = (WebView) editor.lookup("WebView");
        WebEngine engine = webView.getEngine();
        try {
            String command = "document.execCommand(\"InsertHTML\", false, '" + html + "')";
            engine.executeScript(command);
        } catch (JSException e) {
            log.error("Javascript error:" + e.getMessage(), e);
        }
    }

    @ActionProxy(text = "")
    private void insertDiv(ActionEvent event) {
        editor.setHtmlText(HtmlGenerator.test(editor.getHtmlText()));
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
        cache.getCurrentNoteDto().setContent(editor.getHtmlText());
        cache.noteChanged(cache.getCurrentNoteDto());
        log.debug("Save note:" + cache.getCurrentNoteDto());
        Optional<NoteDto> updatedNote = noteService.saveNote(cache.getCurrentNoteDto());
        if (updatedNote.isPresent()) {
            cache.loadNote(updatedNote.get());
            loadNote(new ActionEvent(cache.getCurrentNoteDto(), null));
            ActionFactory.callAction("showNotificationPanel", new NoticeData("Note saved."));
            ActionFactory.callAction("refreshNote", cache.getCurrentNoteDto());
        } else {
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

    @ActionProxy(text = "Edit mode")
    private void switchDisplayMode(ActionEvent event) {
        log.debug("Switch mode display note");
        if ((boolean) event.getSource()) {
            editor.setHtmlText(cache.getCurrentNoteDto().getContent());
            notificationPane.setContent(editor);
        } else {
            webView.getEngine().loadContent(cache.getCurrentNoteDto().getContent());
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