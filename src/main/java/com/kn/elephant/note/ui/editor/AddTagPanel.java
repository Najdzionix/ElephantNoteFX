package com.kn.elephant.note.ui.editor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.NoticeData;
import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.service.TagService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.TagStringConverter;
import com.kn.elephant.note.utils.validator.ValidatorHelper;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 23-3-16.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class AddTagPanel extends BasePanel {
    private final ValidatorHelper validatorHelper;
    private final TextField tagTF;
    private TagDto autoCompleteTag;
    private NoteDto noteDto;
    private ObservableList<TagDto> tagsDto;

    @Inject
    private TagService tagService;

    public AddTagPanel(NoteDto noteDto, ObservableList<TagDto> tagsDto) {
        ActionMap.register(this);
        this.tagsDto = tagsDto;
        this.noteDto = noteDto;
        List<TagDto> noteTags = tagService.getTagByNoteId(noteDto.getId());
        HBox box = new HBox();
        box.setSpacing(5);
        Label addTagLabel = new Label("Add tag:");
        tagTF = new TextField();
        tagTF.setId("addTagTextField");
        box.getStyleClass().add("textFieldTag");
        initAutoCompleteForTags(noteTags);

        Button addTagButton = new Button();
        addTagButton.setOnAction(event -> addTag());
        Icons.addIcon(MaterialDesignIcon.CHECK, addTagButton, "1.5em");
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.getChildren().addAll(spacer, addTagLabel, tagTF, addTagButton);
        setCenter(box);
        validatorHelper = new ValidatorHelper();
        validatorHelper.registerEmptyValidator(tagTF, "Name tag can not be empty!");
    }

    private void initAutoCompleteForTags(List<TagDto> tags) {
        List<TagDto> freeTags = tagService.getAll().stream().filter(tag -> !tags.contains(tag)).collect(Collectors.toList());

        AutoCompletionBinding<TagDto> bind = TextFields.bindAutoCompletion(tagTF,
                param -> freeTags.stream().filter(tagDto -> StringUtils.lowerCase(tagDto.getName()).contains(StringUtils.lowerCase(param.getUserText()))).collect(Collectors.toList()),
                new TagStringConverter());

        bind.setOnAutoCompleted(event -> {
            log.info("auto:" + event.getCompletion());    /** TODO */
            autoCompleteTag = event.getCompletion();
            tagTF.setText(autoCompleteTag.getName());
        });
    }

    private void addTag() {
        log.info(String.format("Add tag of name %s", tagTF.getText()));
        if (!validatorHelper.isValid() || StringUtils.isEmpty(tagTF.getText())) {
            log.debug("Tag is not valid.");
            return;
        }
        TagDto dto;
        if (autoCompleteTag != null && autoCompleteTag.getName().equalsIgnoreCase(tagTF.getText().trim())) {
            dto = autoCompleteTag;
        } else {
            dto = new TagDto();
            dto.setName(tagTF.getText());
        }
        dto.getNotes().add(noteDto);
        Optional<TagDto> tagDto = tagService.saveTag(dto);
        if (tagDto.isPresent()) {
            tagsDto.add(tagDto.get());
            tagTF.clear();
        } else {
            ActionFactory.callAction("showNotificationPanel", NoticeData.createErrorNotice("Operation add tag failed"));
        }
    }
}
