package com.kn.elephant.note.ui.editor;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.service.TagService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.EditableLabel;
import com.kn.elephant.note.ui.TagNode;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.validator.ValidatorHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.GridView;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class DetailsNotePanel extends BasePanel {
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");
    private static final Logger LOGGER = LogManager.getLogger(DetailsNotePanel.class);

    private NoteDto noteDto;
    private GridPane gridPane;
    private ObservableList<TagDto> tagsDto;

    @Inject
    private TagService tagService;
    private ValidatorHelper validatorHelper = new ValidatorHelper();

    public DetailsNotePanel() {
        ActionMap.register(this);
        setMaxHeight(150);
    }

    public void loadNote(NoteDto noteDto) {
        this.noteDto = noteDto;
        Node node = createLeftPanel();
        getStyleClass().add("detailPanel");
        setLeft(node);
        setCenter(createTagPanel());
    }

    private Node createLeftPanel() {
        BorderPane box = new BorderPane();
        box.getStyleClass().add("custom-pane");
        gridPane = new GridPane();
        createDates("Created:", 0);
        createDates("Updated:", 2);
        box.setTop(gridPane);
        box.setCenter(new EditableLabel(noteDto.getTitle()));

        return box;
    }

    private void createDates(String labelText, int colIndex) {
        Label label = new Label(labelText);
        label.getStyleClass().addAll("noteLabelTime", "control-label");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setConstraints(label, colIndex, 0);

        Label timeLabel = new Label(noteDto.getCreateAt().format(FORMATTER));
        timeLabel.getStyleClass().addAll("noteDateTime", "control-label-two");
        GridPane.setHalignment(timeLabel, HPos.LEFT);
        GridPane.setConstraints(timeLabel, colIndex + 1, 0);
        gridPane.getChildren().addAll(label, timeLabel);
    }

    private Node createTagPanel() {
        BorderPane content = new BorderPane();
        content.getStyleClass().add("custom-pane");
        List<TagDto> noteTags = tagService.getTagByNoteId(noteDto.getId());
        tagsDto = FXCollections.observableList(noteTags);
        GridView<TagDto> gridView = new GridView<>(tagsDto);
        gridView.setCellFactory(arg0 -> new TagNode("removeTag"));
        gridView.setCellWidth(120);
        gridView.setCellHeight(25);
        content.setCenter(gridView);


        content.setBottom(new AddTagPanel(noteDto, tagsDto));
        return content;
    }


    @ActionProxy(text = "")
    protected void removeTag(ActionEvent event) {
        LOGGER.info("remove tag action");
//        todo find better way get item.
        TagDto item = ((TagNode) ((Button) event.getSource()).getParent().getParent()).getItem();
        LOGGER.debug(item);
        boolean isDeleted = tagService.removeTagFromNote(item.getId(), noteDto.getId());
        if (isDeleted) {
            tagsDto.remove(item);
        } else {
            ActionFactory.callAction("showNotificationPanel", NoticeData.createErrorNotice("Operation remove tag failed"));
        }
    }
}
