package com.kn.elephant.note.ui.editor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import com.kn.elephant.note.utils.cache.NoteCache;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSException;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NotePanel extends BasePanel {

    private static final String URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
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
        log.debug("Load note: " + newNote); // TODO: 05/03/17 I need to this?
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
        final String sizeIcon = "1.3em";

        Button saveButton = ActionFactory.createButtonWithAction("saveNote");
        Icons.addIcon(MaterialDesignIcon.CONTENT_SAVE, saveButton, sizeIcon);

        Button removeButton = ActionFactory.createButtonWithAction("removeNote");
        Icons.addIcon(MaterialIcon.DELETE, removeButton, sizeIcon);

        insertLinkButton = ActionFactory.createButtonWithAction("insertLink");
        Icons.addIcon(MaterialDesignIcon.LINK_VARIANT, insertLinkButton, sizeIcon);

        tableButton = ActionFactory.createButtonWithAction("insertTable");
        Icons.addIcon(MaterialDesignIcon.GRID, tableButton, sizeIcon);

        editor.setOnMouseClicked((MouseEvent event) -> {
            final int clickCount = event.getClickCount();
            if (clickCount == 2) {
                insertLinkButton.getStyleClass().remove("disableButton");
            }
        });

        toolBar.getItems().addAll(saveButton, new Separator(), removeButton, new Separator(), insertLinkButton, tableButton);
    }

    private void cachingNoteContentChanges() {
        editor.addEventFilter(MouseEvent.MOUSE_EXITED, event -> cache.noteChanged(editor.getHtmlText()));
    }

    private void handleOpenLinksAction() {
        WebView webView = (WebView) editor.lookup("WebView");
        WebEngine engine = webView.getEngine();
        engine.locationProperty().addListener((ov, oldLoc, loc) -> {
			if (isUrl(loc)) {
				log.info("Open link {} browser ", loc);
				app.getHostServices().showDocument(loc);
				loadNote(new ActionEvent(cache.getCurrentNoteDto(), null));
			}

		});
    }

    private static boolean isUrl(String url) {
        log.info("Validate url: {}", url);
        if(StringUtils.isNotEmpty(url)) {
            Pattern pattern = Pattern.compile(URL_REGEX);
            Matcher matcher = pattern.matcher(url);
            return matcher.find();
        }
        return false;
    }

    private void httpLisener() {
        editor.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<InputEvent>() {
            Pattern urlPattern = Pattern.compile("http://[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]");

            @Override
            public void handle(InputEvent event) {
                log.info("event ");
                log.info(editor.getHtmlText());
                String text = editor.getHtmlText();
                Matcher matcher = urlPattern.matcher(text);
                // check all occurance
                boolean foundUrl = false;
                while (matcher.find()) {
                    System.out.print("Start index: " + matcher.start());
                    System.out.print(" End index: " + matcher.end() + " ");
                    System.out.println(matcher.group());
                    text.replace(matcher.group(), "<url>" + matcher.group() + "</url>");
                    foundUrl = true;
                }
                // if(foundUrl) {
                // editor.setHtmlText(text);
                // }
            }
        });

    }

    @ActionProxy(text = "")
    private void insertLink(ActionEvent event) {
        log.debug("Insert link");
        WebView webView = (WebView) editor.lookup("WebView");
        String selected = (String) webView.getEngine().executeScript("window.getSelection().toString();");

        if (selected != null && !selected.isEmpty()) {
            String url = "\"http://" + selected +"\"; \n";
            log.debug(url);
            String script = " var range = window.getSelection().getRangeAt(0);\n" +
                "var selectionContents = range.extractContents();\n" +
                "var div = document.createElement(\"a\");\n" +
                "var url = "  + url +
                "div.href=url;\n" +
                "div.appendChild(selectionContents);\n" +
                "range.insertNode(div);";
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
        log.info("TESTT DIVVVVVVV");
        WebView webView = (WebView) editor.lookup("WebView");
        String selected = (String) webView.getEngine().executeScript("window.getSelection().toString();");

        String currentText = editor.getHtmlText();
        String cssStyle = "\t<style type=\"text/css\">\n" + "\t\t\thtml, body {\n" + "\t\t\t\theight: 100%;\n" + "\t\t\t\tmin-height: 100%;\n"
                + "\t\t\t\tmargin: 0;\n" + "\t\t\t\tpadding: 0;\n" + "\t\t\t}\n" + "\t\t\t.split-pane-divider {\n" + "\t\t\t\tbackground: #aaa;\n" + "\t\t\t}\n"
                + "\t\t\t#left-component {\n" + "\t\t\t\twidth: 20em;\n" + "\t\t\t}\n" + "\t\t\t#divider {\n"
                + "\t\t\t\tleft: 20em; /* same as left component width */\n" + "\t\t\t\twidth: 5px;\n" + "\t\t\t}\n" + "\t\t\t#right-component {\n"
                + "\t\t\t\tleft: 20em;\n" + "\t\t\t\tmargin-left: 5px; /* same as divider width */\n" + "\t\t\t}\n" + "\t\t</style> \t<script>\n"
                + "\t\t\t$(function() {\n" + "\t\t\t\t$('div.split-pane').splitPane();\n" + "\t\t\t});\n" + "\t\t</script>";
        String hyperlinkHtml = "<div style=\"width: 100%;\">\n" + "   <div style=\"float:left; width: 49%; border-style: solid;\">LEFT</div>\n"
                + "   <div style=\"float:right; width: 49%; border-style: solid;\">RIGHT</div>\n" + "</div><div style=\"clear:both\"></div>";

        hyperlinkHtml = "\t<div id=\"split-pane-1\" class=\"split-pane fixed-left\">\n" + "\t\t\t<div class=\"split-pane-component\" id=\"left-component\">\n"
                + "\t\t\t\tThis is the left component\n" + "\t\t\t</div>\n" + "\t\t\t<div class=\"split-pane-divider\" id=\"divider\"></div>\n"
                + "\t\t\t<div class=\"split-pane-component\" id=\"right-component\">\n" + "\t\t\t\tThis is the right component\n"
                + "\t\t\t\t<button onclick=\"$('div.split-pane').splitPane('firstComponentSize', 0);\">Collapse first component</button>\n" + "\t\t\t</div>\n"
                + "\t\t</div>";

        // editor.setHtmlText(currentText.replace(selected, test));
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