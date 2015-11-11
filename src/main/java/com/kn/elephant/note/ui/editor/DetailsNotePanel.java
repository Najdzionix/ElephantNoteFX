package com.kn.elephant.note.ui.editor;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.EditableLabel;
import com.kn.elephant.note.ui.Icons;
import com.kn.elephant.note.ui.TagNode;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.GridView;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class DetailsNotePanel extends BasePanel {
    private NoteDto noteDto;
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");
    private static final Logger LOGGER = LogManager.getLogger(DetailsNotePanel.class);

    private GridPane gridPane;

    public DetailsNotePanel() {
        ActionMap.register(this);
//        todo
        setMaxHeight(150);
    }

    public void loadNote(NoteDto noteDto) {
        this.noteDto = noteDto;
        setLeft(createLeftPanel());
        setCenter(createTagPanel());
    }

    private Node createLeftPanel() {
        gridPane = new GridPane();
        createDates("Created:", 0);
        createDates("Updated:", 1);
        setBottom(new EditableLabel(noteDto.getTitle()));
        return gridPane;
    }

    private void createDates(String labelText, int rowIndex) {
        Label label = new Label(labelText);
        label.getStyleClass().add("noteLabelTime");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setConstraints(label, 0, rowIndex);

        Label timeLabel = new Label(noteDto.getCreateAt().format(FORMATTER));
        timeLabel.getStyleClass().add("noteDateTime");
        GridPane.setHalignment(timeLabel, HPos.LEFT);
        GridPane.setConstraints(timeLabel, 1, rowIndex);

        gridPane.getChildren().addAll(label, timeLabel);
    }

    private Node createTagPanel() {
        BorderPane content = new BorderPane();
        List<String> tags = Arrays.asList("car", "home", "tip", "important", "javaFx");
//        List<TagNode> tagNodes = tags.stream().map(tag -> new TagNode(tag, "removeTag")).collect(Collectors.toList());

        GridView<String> gridView = new GridView(FXCollections.observableList(tags));
        gridView.setCellFactory(arg0 -> new TagNode("removeTag"));
        gridView.setCellWidth(100);
        gridView.setCellHeight(25);
        content.setCenter(gridView);

        HBox box = new HBox();
        box.setSpacing(5);
        Label addTagLabel = new Label("Add tag:");
        TextField tagTF = new TextField();
        box.getStyleClass().add("textFieldTag");
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(tagTF, Validator.createEmptyValidator("Name tag can not be empty!"));
        TextFields.bindAutoCompletion(tagTF, tags);
        Action addTagAction = ActionMap.action("addTag");
        addTagAction.setGraphic(Icons.SAVE_TAG);
        Button testButton = ActionUtils.createButton(addTagAction);
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.getChildren().addAll(spacer, addTagLabel, tagTF, testButton);
        content.setBottom(box);
        return content;
    }

    @ActionProxy(text = "")
    protected void removeTag() {
        LOGGER.info("remove tag action");
    }


    @ActionProxy(text = "")
    private void addTag(ActionEvent event) {
        LOGGER.info("Add tag ...");

    }

}
