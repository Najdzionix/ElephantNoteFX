package com.kn.elephant.note.ui;

import com.google.inject.Inject;
import com.kn.elephant.note.Main;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.utils.ActionFactory;
import com.kn.elephant.note.utils.validator.Validator;
import com.kn.elephant.note.utils.validator.ValidatorHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.validation.ValidationSupport;

/**
 * Created by Kamil Nadłonek on 27.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class DialogNote extends BasePanel {

    public static final int MAX_WIDTH_PARENT_NAME = 100;
    private Dialog<NoteDto> dialog;
    private TextField titleText;
    private TextField shortDescText;
    private ComboBox<NoteDto> parentsBox;

    @Inject
    private NoteService noteService;
    private ValidatorHelper validatorHelper;

    public DialogNote() {
        ActionMap.register(this);
        createContent();
    }

    public void createContent() {
        dialog = new Dialog<>();
        validatorHelper = new ValidatorHelper();
        dialog.setTitle("New note");
        dialog.setHeaderText("Create new note \n" +
                "press Okay (or click title bar 'X' for cancel).");
        dialog.setResizable(false);

        Label titleLabel = createLabel("Title: ");
        Label shortDescriptionL = createLabel("Short description: ");
        Label parentLabel = createLabel("Choose parent");
        titleText = new TextField();
        validatorHelper.registerEmptyValidator(titleText, "Title can not empty.");
        uniqueTagTitleValidator();
        shortDescText = new TextField();
        validatorHelper.registerEmptyValidator(shortDescText, " Short description can not be empty.");

        VBox box = new VBox();
        box.getChildren().addAll(titleLabel, titleText, shortDescriptionL, shortDescText, parentLabel, createSelectionPaneParent());
        dialog.getDialogPane().getStyleClass().add("card");
        dialog.getDialogPane().setContent(box);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validatorHelper.isValid()) {
                event.consume();
            }
        });
        dialog.setResultConverter(buttonType -> {
            if (buttonType == buttonTypeOk) {
                return new NoteDto().setTitle(titleText.getText()).setShortDescription(shortDescText.getText())
                        .setParentNote(parentsBox.getValue());
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());
    }

    private void uniqueTagTitleValidator() {
        validatorHelper.registerCustomValidator(titleText, "Provide unique name of note.",
                node -> noteService.isTitleNoteUnique(((TextField)node).getText()));
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("control-label");
        return label;
    }

    private Node createSelectionPaneParent() {
        HBox box = new HBox();
        box.setSpacing(6);
        ObservableList<NoteDto> notesDto = FXCollections.observableArrayList(noteService.getAllNotes());
        parentsBox = new ComboBox<>(notesDto);
        parentsBox.setButtonCell(new NoteListCell());
        parentsBox.setCellFactory(p -> new NoteListCell());
        parentsBox.setMinWidth(280);
        Button clearButton = ActionUtils.createButton(ActionFactory.getAction("clearSelection"));
        clearButton.getStyleClass().add("button-flat");
        box.getChildren().addAll(parentsBox, clearButton);

        return box;
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
