package com.kn.elephant.note.ui.editor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.GridView;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.model.NoteType;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.service.TagService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.ui.control.EditableLabel;
import com.kn.elephant.note.ui.control.TagNode;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class DetailsNotePanel extends BasePanel {
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");
    private static final Logger LOGGER = LogManager.getLogger(DetailsNotePanel.class);
    private NoteDto noteDto;
    private GridPane datesPane;
    private ObservableList<TagDto> tagsDto;

    @Inject
    private TagService tagService;

    @Inject
    private NoteService noteService;

    public DetailsNotePanel() {
        ActionMap.register(this);
    }

    public void loadNote(NoteDto noteDto) {
        this.noteDto = noteDto;
        Node node = createNotePanel();
        getStyleClass().add("detailPanel");
        setLeft(node);
        setCenter(createTagPanel());
    }

    private Node createNotePanel() {
        BorderPane box = new BorderPane();
        box.getStyleClass().add("custom-pane");
        datesPane = new GridPane();
        createDates("Created:", 0);
        createDates("Updated:", 2);
        box.setTop(datesPane);
        box.setCenter(createNoteTitlePanel());
        if(noteDto.getType() == NoteType.HTML) {
            box.setBottom(createButtonsPanel());
        }
        return box;
    }

    private Node createButtonsPanel(){
        HBox content = new HBox();
        content.getStyleClass().add("button-pane");
        ToggleSwitch modeButton = new ToggleSwitch("Edit mode");
        modeButton.setSelected(true);
        modeButton.selectedProperty().addListener((observable, oldValue, newValue) -> ActionFactory.callAction("switchDisplayMode", newValue));
        content.getChildren().addAll(modeButton);
        return content;
    }

    private Node createNoteTitlePanel() {
        BorderPane borderPane = new BorderPane();
        Node titleLabel = createTitleLabel();
        EditableLabel descLabel = new EditableLabel(noteDto.getShortDescription(), 60, (oldText, newText) -> {
            if (!oldText.equals(newText)) {
                ActionFactory.callAction("updateDesc", newText);
            }
        });

        borderPane.setBottom(descLabel);
        borderPane.setCenter(titleLabel);
        if(StringUtils.isNoneEmpty(noteDto.getIcon())) {
            Label iconLabel = Icons.builderIcon(MaterialIcon.valueOf(noteDto.getIcon()), "2.5em", noteDto.getColorIcon());
            iconLabel.getStyleClass().add("iconNote");
            borderPane.setLeft(iconLabel);
        }
        return borderPane;
    }

    private Node createTitleLabel() {
        EditableLabel title = new EditableLabel(noteDto.getTitle(), 50, (oldText, newText) -> {
            if (!oldText.equals(newText)) {
                ActionFactory.callAction("updateTitle", newText);
            }
        });
        title.addCssClass("noteTitle");
        title.registerCustomValidator("Provide unique title of note.", node -> noteService.isTitleNoteUnique(((TextField) node).getText()));

        return title;
    }

    private void createDates(String labelText, int colIndex) {
        Label label = new Label(labelText);
        label.getStyleClass().addAll("noteLabelTime", "control-labelText");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setConstraints(label, colIndex, 0);

        Label timeLabel = new Label(noteDto.getCreateAt().format(FORMATTER));
        timeLabel.getStyleClass().addAll("noteDateTime", "control-labelText-two");
        GridPane.setHalignment(timeLabel, HPos.LEFT);
        GridPane.setConstraints(timeLabel, colIndex + 1, 0);
        datesPane.getChildren().addAll(label, timeLabel);
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
        TagDto item = (TagDto) event.getSource();
        LOGGER.info("Remove tag (" + item + ") action from note:" + noteDto);
        boolean isDeleted = tagService.removeTagFromNote(item.getId(), noteDto.getId());
        if (isDeleted) {
            tagsDto.remove(item);
        } else {
            ActionFactory.callAction("showNotificationPanel", NoticeData.createErrorNotice("Operation remove tag failed"));
        }
    }
}
