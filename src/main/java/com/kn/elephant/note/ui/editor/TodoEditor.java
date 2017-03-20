package com.kn.elephant.note.ui.editor;

import static com.kn.elephant.note.utils.Icons.createButtonWithIcon;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.CheckListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.cache.NoteCache;
import com.kn.elephant.note.utils.validator.ValidatorHelper;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Created by Kamil Nadłonek on 19-03-2017 email:kamilnadlonek@gmail.com
 */
public class TodoEditor extends BasePanel implements Editor {
    private static final String SEPARATOR = "#;#";
    private static final String DEFAULT_TASK = "Default task";
    private CheckListView<String> listTasks;
    private NoteCache cache;
    private ValidatorHelper validatorHelper = new ValidatorHelper();

    public TodoEditor() {
        getStyleClass().add("custom-pane");
    }

    @Override
    public void loadNote(NoteDto noteDto) {
        ObservableList<String> strings;
        if (StringUtils.isEmpty(noteDto.getContent())) {
            strings = FXCollections.observableArrayList(DEFAULT_TASK);
        } else {
            String[] split = noteDto.getContent().split(SEPARATOR);
            strings = FXCollections.observableArrayList(split);
        }

        listTasks = new CheckListView<>(strings);
        createContent();
    }

    @Override
    public String getContent() {
        // TODO: 19/03/17 lose information about selected tasks
        return listTasks.getItems().stream().collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public void setNoteCache(NoteCache cache) {
        this.cache = cache;
    }

    private void createContent() {
        setCenter(listTasks);
        BorderPane content = new BorderPane();
        HBox addPanel = new HBox();
        addPanel.setSpacing(10);
        String iconSize = "1.2em";
        Button saveButton = createButtonWithIcon(iconSize, "saveNote", MaterialDesignIcon.CONTENT_SAVE);
        Button deleteButton = createButtonWithIcon(iconSize, "removeNote", MaterialIcon.DELETE);
        addPanel.getChildren().addAll(saveButton, deleteButton);
        content.setLeft(addPanel);
        content.setRight(createAddTaskUI());
        setTop(content);
    }

    private Node createAddTaskUI() {
        // TODO: 19/03/17 set empty validator
        HBox pane = new HBox();
        validatorHelper.removeAllNodes();
        TextField textField = new TextField();
        validatorHelper.registerEmptyValidator(textField, "Task can not be empty.");
        pane.setSpacing(5);
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            if (validatorHelper.isValid()) {
                String task = textField.getText();
                listTasks.getItems().add(task);
                cache.contentNoteChanged(getContent());
            }
        });
        pane.getChildren().addAll(textField, addButton);
        return pane;
    }

    public String convertToJson(List<NoteTask> tasks) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }

}
