package com.kn.elephant.note.ui.editor;

import static com.kn.elephant.note.utils.Icons.createButtonWithIcon;

import org.controlsfx.control.action.ActionProxy;

import com.kn.elephant.note.Main;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.LinkUtils;
import com.kn.elephant.note.utils.cache.NoteCache;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
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
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSException;

/**
 * Created by Kamil Nadłonek on 19-03-2017 email:kamilnadlonek@gmail.com
 */
@Log4j2
public class HtmlEditor extends BasePanel implements Editor {

    private HTMLEditor editor;
    private WebView webView;
    private Button insertLinkButton;
    private Button tableButton;
    private TableGenerator tableGenerator;
    private NoteCache cache;

    public HtmlEditor() {
        editor = new HTMLEditor();
        webView = new WebView();
        tableGenerator = new TableGenerator(tableButton);

        addButtonsToToolbar();
        handleOpenLinksAction();
        httpWatcher();
    }

    @Override
    public void loadNote(NoteDto noteDto) {
        editor.setHtmlText("");
        editor.setHtmlText(noteDto.getContent());
        setCenter(editor);
    }

    @Override
    public String getContent() {
        return editor.getHtmlText();
    }

    @Override
    public void setNoteCache(NoteCache cache) {
        this.cache = cache;
    }

    private void createButtons(ToolBar toolBar) {
        final String sizeIcon = "1.3em";
        Button saveButton = createButtonWithIcon(sizeIcon, "saveNote", MaterialDesignIcon.CONTENT_SAVE);
        Button removeButton = createButtonWithIcon(sizeIcon, "removeNote", MaterialIcon.DELETE);
        Button testButton = createButtonWithIcon(sizeIcon, "insertDiv", MaterialIcon.ADD);
        insertLinkButton = createButtonWithIcon(sizeIcon, "insertLink", MaterialDesignIcon.LINK_VARIANT);
        tableButton = createButtonWithIcon(sizeIcon, "insertTable", MaterialDesignIcon.GRID);

        editor.setOnMouseClicked((MouseEvent event) -> {
            final int clickCount = event.getClickCount();
            if (clickCount == 2) {
                insertLinkButton.getStyleClass().remove("disableButton");
            }
        });
        toolBar.getItems().addAll(saveButton, new Separator(), removeButton, new Separator(), insertLinkButton, tableButton, new Separator(), testButton);
    }

    private void addButtonsToToolbar() {
        Node node = editor.lookup(".top-toolbar");
        if (node instanceof ToolBar) {
            createButtons((ToolBar) node);
        }
    }

    private void handleOpenLinksAction() {
        WebView webView = (WebView) editor.lookup("WebView");
        WebEngine engine = webView.getEngine();
        engine.locationProperty().addListener((ov, oldLoc, loc) -> {
            if (LinkUtils.isUrl(loc)) {
                log.info("Open link {} browser ", loc);
                Main.getHostService().showDocument(loc);
                loadNote(cache.getCurrentNoteDto());
            }
        });
    }

    private void httpWatcher() {
        editor.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String content = LinkUtils.replaceLinkOnHtmlLinkTag(editor.getHtmlText());
                if (!editor.getHtmlText().equals(content)) {
                    cache.contentNoteChanged(content);
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

    @ActionProxy(text = "")
    private void insertDiv(ActionEvent event) {
        editor.setHtmlText(HtmlGenerator.test(editor.getHtmlText()));
    }

    @ActionProxy(text = "Edit mode")
    private void switchDisplayMode(ActionEvent event) {
        log.debug("Switch mode display note");
        if ((boolean) event.getSource()) {
            editor.setHtmlText(cache.getCurrentNoteDto().getContent());
            setCenter(editor);
        } else {
            webView.getEngine().loadContent(cache.getCurrentNoteDto().getContent());
            webView.setContextMenuEnabled(false);
            webView.setDisable(true);
            webView.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
            setCenter(webView);
        }
    }
}
