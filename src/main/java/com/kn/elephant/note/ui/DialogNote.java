package com.kn.elephant.note.ui;

import static com.kn.elephant.note.utils.Utils.toRGBCode;

import java.util.ListIterator;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

import com.google.inject.Inject;
import com.kn.elephant.note.Main;
import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.model.NoteType;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.control.GlyphsListCell;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.validator.ValidatorHelper;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 27.11.15. email:kamilnadlonek@gmail.com
 */
@Log4j2
public class DialogNote extends BasePanel {

    private static final int MAX_WIDTH_PARENT_NAME = 50;
    private static final int VALUE = 6;
    private Dialog<NoteDto> dialog;
    private TextField titleText;
    private TextField shortDescText;
    private ComboBox<NoteDto> parentsBox;
    private ComboBox<NoteType> typeBox;
    @Inject
    private NoteService noteService;
    private ValidatorHelper validatorHelper;
    private ColorPicker colorPicker;
    private ComboBox<GlyphIcons> iconBox;

    DialogNote() {
        ActionMap.register(this);
        createContent();
    }

    public DialogNote(NoteDto noteDto) {
        this();
        dialog.setTitle("Editing note");
        dialog.setHeaderText("Now you can change note.");

        titleText.setText(noteDto.getTitle());
        shortDescText.setText(noteDto.getShortDescription());
        typeBox.getSelectionModel().select(noteDto.getType());
        typeBox.setDisable(true);
        selectParentNote(noteDto);
        iconBox.getSelectionModel().select(MaterialIcon.valueOf(noteDto.getIcon()));
        colorPicker.setValue(Color.valueOf(noteDto.getColorIcon()));
        setIconColor(noteDto.getColorIcon()); // iconLabel is null can not change color ...
    }

    private void selectParentNote(NoteDto noteDto) {
        Optional<NoteDto> parentNote = noteService.getParentNote(noteDto.getId());
        if (parentNote.isPresent()) {
            ListIterator<NoteDto> noteDtoListIterator = parentsBox.getItems().listIterator();
            int index = 0;
            while (noteDtoListIterator.hasNext()) {
                if (noteDtoListIterator.next().getId().equals(parentNote.get().getId())) {
                    break;
                }
                index++;
            }
            parentsBox.getSelectionModel().select(index);
        }
    }

    public TextField getTitleText() {
        return titleText;
    }

    private void createContent() {
        dialog = new Dialog<>();
        validatorHelper = new ValidatorHelper();
        dialog.setTitle("New note");
        dialog.setHeaderText("Welcome in wizard notes.");
        dialog.setResizable(false);

        Label titleLabel = UIFactory.createLabel("Title: ");
        Label shortDescriptionL = UIFactory.createLabel("Short description: ");
        Label parentLabel = UIFactory.createLabel("Choose parent");
        Label noteTypeLabel = UIFactory.createLabel("Choose note type");
        Label iconLabel = UIFactory.createLabel("Choose icon for note");
        titleText = new TextField();
        titleText.setId("dialogNoteTitleText");
        Platform.runLater(() -> titleText.requestFocus());

        validatorHelper.registerEmptyValidator(titleText, "Title can not empty.");
        uniqueTagTitleValidator();
        shortDescText = new TextField();
        shortDescText.setId("dialogNoteDescText");
        validatorHelper.registerEmptyValidator(shortDescText, "Short description can not be empty.");
        titleText.requestFocus();
        VBox box = new VBox();
        box.getChildren().addAll(titleLabel, titleText, shortDescriptionL, shortDescText, noteTypeLabel, createListNoteTypes(), parentLabel,
                createSelectionPaneParent(), iconLabel, createIconPicker());
        dialog.getDialogPane().getStyleClass().add("card");

        dialog.getDialogPane().setContent(box);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        btOk.getStyleClass().add("button-action");
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validatorHelper.isValid()) {
                event.consume();
            }
        });
        dialog.setResultConverter(buttonType -> {
            if (buttonType == buttonTypeOk) {
                String hexColor = toRGBCode(colorPicker.getValue());
                String iconName = iconBox.getValue().name();
                NoteDto noteDto = new NoteDto().setTitle(titleText.getText()).setShortDescription(shortDescText.getText()).setParentNote(parentsBox.getValue())
                        .setType(typeBox.getValue()).setIcon(iconName).setColorIcon(hexColor);
                if (noteDto.getType() == NoteType.HTML) {
                    noteDto.setContent(NoteConstants.INIT_NOTE_CONTENT);
                }
                return noteDto;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());
    }

    private void uniqueTagTitleValidator() {
        validatorHelper.registerCustomValidator(titleText, "Provide unique name of note.", node -> noteService.isTitleNoteUnique(((TextField) node).getText()));
    }

    private Node createSelectionPaneParent() {
        HBox box = new HBox();
        box.setSpacing(VALUE);
        ObservableList<NoteDto> notesDto = FXCollections.observableArrayList(noteService.getAllNotes());
        parentsBox = new ComboBox<>(notesDto);
        parentsBox.setButtonCell(new NoteListCell());
        parentsBox.setCellFactory(p -> new NoteListCell());
        parentsBox.setMinWidth(280);
        parentsBox.setMaxWidth(400);
        Button clearButton = ActionUtils.createButton(ActionFactory.getAction("clearSelection"));
        clearButton.getStyleClass().add("button-flat");
        box.getChildren().addAll(parentsBox, clearButton);

        return box;
    }

    private Node createListNoteTypes() {
        HBox box = new HBox();
        box.setSpacing(VALUE);
        ObservableList<NoteType> types = FXCollections.observableArrayList(NoteType.HTML, NoteType.TODO);
        typeBox = new ComboBox<>(types);
        typeBox.setMinWidth(280);
        typeBox.setMaxWidth(400);
        typeBox.getSelectionModel().select(0);
        box.getChildren().addAll(typeBox);
        return box;
    }

    private Node createIconPicker() {
        HBox box = new HBox();
        box.setPadding(new Insets(5));
        box.setSpacing(VALUE);
        colorPicker = new ColorPicker(Color.BLACK);
        iconBox = new ComboBox<>(Icons.getListNoteIcons());
        iconBox.setCellFactory(param -> new GlyphsListCell());
        iconBox.setButtonCell( new GlyphsListCell());
        iconBox.getStyleClass().add("iconComboBox");
        colorPicker.setOnAction(event -> {
            String color = toRGBCode(colorPicker.getValue());
            setIconColor(color);
            }
        );
        HBox.setHgrow(iconBox, Priority.ALWAYS);
        HBox.setHgrow(colorPicker, Priority.ALWAYS);
        box.getChildren().addAll(iconBox, colorPicker);
        return box;
        
    }

    private void setIconColor(String color) {
        GlyphsListCell buttonCell = (GlyphsListCell) iconBox.getButtonCell();
        buttonCell.setColor(color);
    }

    @ActionProxy(text = "Clear")
    private void clearSelection() {
        parentsBox.getSelectionModel().clearSelection();
    }

    private static String getDisplayName(NoteDto dto) {
        return StringUtils.abbreviate(dto.getTitle(), MAX_WIDTH_PARENT_NAME);
    }

    public Dialog<NoteDto> getDialog() {
        return dialog;
    }

    private class NoteListCell extends ListCell<NoteDto> {
        @Override
        protected void updateItem(NoteDto item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(getDisplayName(item));
            } else {
                setText(null);
            }
        }
    }
}
