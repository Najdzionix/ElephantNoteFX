package com.kn.elephant.note.ui.editor;

import static com.kn.elephant.note.utils.Icons.createButtonWithIcon;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.CheckListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.JsonParser;
import com.kn.elephant.note.utils.cache.NoteCache;
import com.kn.elephant.note.utils.validator.ValidatorHelper;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 19-03-2017 email:kamilnadlonek@gmail.com
 */
@Log4j2
public class TodoEditor extends BasePanel implements Editor {
    private static final String DEFAULT_TASK = "Default task";
    private CheckListView<NoteTask> listTasks;
    private NoteCache cache;
    private ValidatorHelper validatorHelper = new ValidatorHelper();

    public TodoEditor() {
        getStyleClass().add("custom-todo");
    }

    @Override
    public void loadNote(NoteDto noteDto) {
        ObservableList<NoteTask> strings = FXCollections.observableArrayList();
        if (!StringUtils.isEmpty(noteDto.getContent())) {
            List<NoteTask> tasks = getTasks(noteDto);
            strings.addAll(tasks);
        }

        listTasks = new CheckListView<>(strings);

        for (int i = 0; i < listTasks.getItems().size(); i++) {
            if (listTasks.getItems().get(i).isDone()) {
                listTasks.getCheckModel().check(i);
            }
        }

        listTasks.setCellFactory(lv -> new CheckBoxListCell<NoteTask>(listTasks::getItemBooleanProperty) {
            @Override
            public void updateItem(NoteTask employee, boolean empty) {
                super.updateItem(employee, empty);
                setText(employee == null ? "" : employee.getTask());
            }
        });

        listTasks.getCheckModel().getCheckedItems().addListener((ListChangeListener<NoteTask>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (NoteTask task : c.getAddedSubList()) {
                        task.setDone(true);
                    }
                }
                if (c.wasRemoved()) {
                    for (NoteTask task : c.getRemoved()) {
                        task.setDone(false);
                    }
                }
            }
        });

        createContent();
    }

    @Override
    public String getContent() {
        return JsonParser.serializeToJsonString(listTasks.getItems());
    }

    @Override
    public void setNoteCache(NoteCache cache) {
        this.cache = cache;
    }

    private List<NoteTask> getTasks(NoteDto noteDto) {
        return JsonParser.unmarshallJSON(new TypeReference<List<NoteTask>>() {
        }, noteDto.getContent());
    }

    private void createContent() {
        setCenter(listTasks);
        BorderPane content = new BorderPane();
        HBox addPanel = new HBox();
        addPanel.setSpacing(10);
        String iconSize = "1.8em";
        Button saveButton = createButtonWithIcon(iconSize, "saveNote", MaterialDesignIcon.CONTENT_SAVE);
        Button deleteButton = createButtonWithIcon(iconSize, "removeNote", MaterialDesignIcon.CALENDAR_REMOVE);
        addPanel.getChildren().addAll(saveButton, deleteButton);
        content.setLeft(addPanel);
        content.setRight(createAddTaskUI());
        content.getStyleClass().addAll("custom-pane", "kamil");
        setTop(content);
    }

    private Node createAddTaskUI() {
        HBox pane = new HBox();
        pane.setSpacing(5);
        pane.getStyleClass().add("textFieldTag");
        TextField textField = new TextField();
        Label addTaskLabel = new Label("Add task:");
        validatorHelper.removeAllNodes();
        validatorHelper.registerEmptyValidator(textField, "Task can not be empty.");
        Button addButton = new Button("");
        Icons.addIcon(MaterialDesignIcon.CHECK, addButton, "1.5em");
        addButton.setOnAction(event -> {
            if (validatorHelper.isValid()) {
                NoteTask task = new NoteTask();
                task.setTask(textField.getText());
                listTasks.getItems().add(task);
                cache.contentNoteChanged(getContent());
                textField.setText("");
            }
        });
        pane.getChildren().addAll(addTaskLabel, textField, addButton);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return pane;
    }
}
