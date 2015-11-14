package com.kn.elephant.note.ui.editor;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.service.TagService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.EditableLabel;
import com.kn.elephant.note.ui.Icons;
import com.kn.elephant.note.ui.TagNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.controlsfx.control.ToggleSwitch;
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

    ObservableList<TagDto> tagDtos;

    @Inject
    private TagService tagService;
    private TextField tagTF;

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
        BorderPane box = new BorderPane();
        gridPane = new GridPane();
        createDates("Created:", 0);
        createDates("Updated:", 2);
        box.setTop(gridPane);
        box.setCenter(new EditableLabel(noteDto.getTitle()));

        HBox noteBoxButtons = new HBox();
        Action saveAction = ActionMap.action("saveNote");
        saveAction.setGraphic(Icons.SAVE_NOTE);
        Button saveButton = ActionUtils.createButton(saveAction);
        Action removeAction = ActionMap.action("removeNote");
        removeAction.setGraphic(Icons.REMOVE_NOTE);
        Button removeButton = ActionUtils.createButton(removeAction);
        ToggleSwitch modeButton = new ToggleSwitch("Edit");
        noteBoxButtons.setSpacing(10);
        noteBoxButtons.getChildren().addAll(saveButton, removeButton, modeButton);

        box.setBottom(noteBoxButtons);
        return box;
    }

    private void createDates(String labelText, int colIndex) {
        Label label = new Label(labelText);
        label.getStyleClass().add("noteLabelTime");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setConstraints(label, colIndex, 0);

        Label timeLabel = new Label(noteDto.getCreateAt().format(FORMATTER));
        timeLabel.getStyleClass().add("noteDateTime");
        GridPane.setHalignment(timeLabel, HPos.LEFT);
        GridPane.setConstraints(timeLabel, colIndex + 1, 0);

        gridPane.getChildren().addAll(label, timeLabel);
    }

    private Node createTagPanel() {
        BorderPane content = new BorderPane();
//        List<String> tags = Arrays.asList("car", "home", "tip", "important", "javaFx");
//        List<TagNode> tagNodes = tags.stream().map(tag -> new TagNode(tag, "removeTag")).collect(Collectors.toList());
        List<TagDto> tags = tagService.getTagByNoteId(noteDto.getId());
        tagDtos = FXCollections.observableList(tags);
        GridView<TagDto> gridView = new GridView(tagDtos);
        gridView.setCellFactory(arg0 ->  new TagNode("removeTag"));
        gridView.setCellWidth(100);
        gridView.setCellHeight(25);
        content.setCenter(gridView);




        HBox box = new HBox();
        box.setSpacing(5);
        Label addTagLabel = new Label("Add tag:");
        tagTF = new TextField();
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
    protected void removeTag(ActionEvent event) {
        LOGGER.info("remove tag action");
        TagDto item = ((TagNode) ((Button) event.getSource()).getParent().getParent()).getItem();
        LOGGER.debug(item);
        boolean isDeleted = tagService.removeTagFromNote(item.getId(), noteDto.getId());
        if(isDeleted) {
            tagDtos.remove(item);
        }
//        todo when failed show notification
    }


    @ActionProxy(text = "")
    private void addTag(ActionEvent event) {
        LOGGER.info(String.format("Add tag of name %s", tagTF.getText()));
        TagDto dto = new TagDto();
        dto.setName(tagTF.getText());
        dto.getNotes().add(noteDto);
        tagService.saveTag(dto);


    }

}
