package com.kn.elephant.note.ui;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.control.NoteNode;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;
import de.jensd.fx.glyphs.octicons.OctIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class SearchBox extends BasePanel {

    private TextField textBox;
    private Button clearButton;

    @Inject
    private NoteService noteService;
    private PopOver popOver = new PopOver();

    public SearchBox() {
        ActionMap.register(this);
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        textBox = new TextField();
        textBox.setPromptText("Search");
        Action clearAction = ActionFactory.getAction("clearAction");
        clearButton = ActionUtils.createButton(clearAction);
        Icons.addIcon(OctIcon.X, clearAction, "1.0em");
        clearButton.setVisible(false);

        initPopOverResults();

        getChildren().addAll(textBox, clearButton);

        textBox.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            clearButton.setVisible(StringUtils.isNotEmpty(newValue));
            if (StringUtils.isNotEmpty(newValue)) {
                loadNotesPopOverResults(noteService.findNotes(newValue));
            }
        });
    }

    private void initPopOverResults() {
        popOver.setArrowIndent(25);
        popOver.setArrowSize(20);
        popOver.setHeaderAlwaysVisible(true);
        popOver.getStyleClass().add("popoverM");
        popOver.getRoot().getStyleClass().add("popoverM");
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popOver.setTitle("Results");
    }

    private void loadNotesPopOverResults(List<NoteDto> notes) {
        if (popOver.isShowing()) {
            log.debug("Is showing result of search.");
            popOver.setContentNode(createListOfNotes(notes));
        } else {
            popOver.setContentNode(createListOfNotes(notes));
            popOver.show(textBox);
        }
    }

    @Override
    protected void layoutChildren() {
        textBox.resize(getWidth(), getHeight());
        clearButton.resizeRelocate(getWidth() - 18, 6, 12, 13);
    }

    private Node createListOfNotes(List<NoteDto> notes) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("content-search");
        if (notes.isEmpty()) {
            Text text = new Text("Not found notes.");
            text.setTextAlignment(TextAlignment.CENTER);
            text.getStyleClass().add("not-found-text");
            scrollPane.setContent(text);
        } else {
            VBox list = new VBox();
            list.getStyleClass().add("list-search-notes");
            list.setSpacing(2.0);
            List<Node> nodes = notes.parallelStream().map(NoteNode::new).collect(Collectors.toList());
            list.getChildren().addAll(nodes);
            scrollPane.setContent(list);
        }
        return scrollPane;
    }

    @ActionProxy(text = "")
    private void clearAction() {
        textBox.setText("");
        textBox.requestFocus();
    }

    @ActionProxy(text = "")
    private void clearSelectedNoteNodes() {
        ScrollPane scroll = (ScrollPane) popOver.getContentNode();
        ObservableList<Node> children = ((VBox) scroll.getContent()).getChildren();
        children.stream().forEach(node ->   node.getStyleClass().remove("selected-node"));
    }
}
