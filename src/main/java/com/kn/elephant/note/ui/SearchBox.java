package com.kn.elephant.note.ui;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.control.NoteNode;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PopOver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class SearchBox  extends BasePanel {

    private TextField textBox;
    private Button clearButton;

     @Inject
     private NoteService noteService;
    private PopOver popOver = new PopOver();

    public SearchBox() {
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        textBox = new TextField();
//        textBox.setOnAction(event -> { loadNotesPopOverResults().show(textBox);});
        textBox.setPromptText("Search");
        clearButton = new Button("X");
        clearButton.setVisible(false);

       initPopOverResults();

        getChildren().addAll(textBox, clearButton);
        clearButton.setOnAction((ActionEvent actionEvent) -> {
            textBox.setText("");
//            loadNotesPopOverResults().show(textBox);
            textBox.requestFocus();
        });
        textBox.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            clearButton.setVisible(StringUtils.isNotEmpty(newValue));
            if(StringUtils.isNotEmpty(newValue)) {
                loadNotesPopOverResults(noteService.findNotes(newValue));
            }
        });
    }

    private void initPopOverResults(){
        popOver.setArrowIndent(25);
        popOver.setArrowSize(20);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popOver.setTitle("Results");
    }

    private void loadNotesPopOverResults(List<NoteDto> notes) {
        if(popOver.isShowing()) {
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
        scrollPane.getStyleClass().add("content-search");
        VBox list = new VBox();
        list.getStyleClass().add("list-search-notes");
        list.setSpacing(2.0);
        List<Node> nodes = notes.parallelStream().map(NoteNode::new).collect(Collectors.toList());
        list.getChildren().addAll(nodes);
        scrollPane.setContent(list);
        return scrollPane;
    }
}
