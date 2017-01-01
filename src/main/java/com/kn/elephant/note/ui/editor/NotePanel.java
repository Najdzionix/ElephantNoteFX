package com.kn.elephant.note.ui.editor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
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
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSException;

/**
 * Created by Kamil Nadłonek on 10.11.15. email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NotePanel extends BasePanel {

    private NoteDto currentNoteDto;
    private DetailsNotePanel detailsNotePanel;
    private HTMLEditor editor;
    private WebView webView;
    private Button insertLinkButton;

	private TableGenerator tableGenerator;

    @Inject
    private NoteService noteService;
    private NotificationPane notificationPane;
    private Button tableButton;

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
    }

    private void notificationPanel() {
        notificationPane = new NotificationPane();
        notificationPane.showFromTopProperty().setValue(false);
        notificationPane.getStyleClass().add("notification");
        setCenter(notificationPane);
    }

    @ActionProxy(text = "loadnote")
    private void loadNote(ActionEvent event) {
        notificationPane.setContent(editor);
        currentNoteDto = (NoteDto) event.getSource();
        log.debug("Load note: " + currentNoteDto);
        detailsNotePanel.loadNote(currentNoteDto);
        editor.setHtmlText("");
        editor.setHtmlText(currentNoteDto.getContent());
    }

    private void addButtonsToToolbar() {
        Node node = editor.lookup(".top-toolbar");
        if (node instanceof ToolBar) {
            createButtons((ToolBar) node);
        }
    }

    private void createButtons(ToolBar toolBar) {
        Action saveAction = ActionMap.action("saveNote");
        final String sizeIcon = "1.4em";
        Icons.addIcon(MaterialDesignIcon.CONTENT_SAVE, saveAction, sizeIcon);
        Button saveButton = ActionUtils.createButton(saveAction);

        Action removeAction = ActionMap.action("removeNote");
        Icons.addIcon(MaterialDesignIcon.DELETE, removeAction, sizeIcon);
        Button removeButton = ActionUtils.createButton(removeAction);

        Action insertAction = ActionMap.action("insertLink");
        Icons.addIcon(MaterialDesignIcon.LINK_OFF, insertAction, sizeIcon);
        insertLinkButton = new Button();
        ActionUtils.configureButton(insertAction, insertLinkButton);

        Action insertDivAction = ActionMap.action("insertTable");
        Icons.addIcon(MaterialDesignIcon.TABLE, insertDivAction, sizeIcon);
        tableButton = new Button();
        ActionUtils.configureButton(insertDivAction, tableButton);

        editor.setOnMouseClicked((MouseEvent event) -> {
            final int clickCount = event.getClickCount();
            if (clickCount == 2) {
                insertLinkButton.getStyleClass().remove("disableButton");
            }
        });

        toolBar.getItems().addAll(saveButton, new Separator(), removeButton, new Separator(), insertLinkButton, tableButton);
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
        String currentText = editor.getHtmlText();
        String hyperlinkHtml = "<a href=\"" + selected.trim() + "\" title=\"" + selected + "\" target=\"_blank\">" + selected + "</a>";

        if (selected != null & !selected.isEmpty()) {
            editor.setHtmlText(currentText.replace(selected, hyperlinkHtml));
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
        currentNoteDto.setTitle((String) event.getSource());
        saveNote();
    }

    @ActionProxy(text = "")
    private void updateDesc(ActionEvent event) {
        log.debug("Update note desc" + event.getSource());
        currentNoteDto.setShortDescription((String) event.getSource());
        saveNote();
    }

    @ActionProxy(text = "")
    private void saveNote() {
        currentNoteDto.setContent(editor.getHtmlText());
        log.debug("Save note:" + currentNoteDto);
        Optional<NoteDto> updatedNote = noteService.saveNote(currentNoteDto);
        if (updatedNote.isPresent()) {
            currentNoteDto = updatedNote.get();
            loadNote(new ActionEvent(currentNoteDto, null));
            ActionFactory.callAction("showNotificationPanel", new NoticeData("Note saved."));
            ActionFactory.callAction("refreshNote", currentNoteDto);
        } else {
            ActionFactory.callAction("showNotificationPanel", NoticeData.createErrorNotice("Operation saving failed"));
        }

        // refresh list panel
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
        if ((boolean) event.getSource()) {
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