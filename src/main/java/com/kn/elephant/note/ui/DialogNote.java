package com.kn.elephant.note.ui;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.utils.ActionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

/**
 * Created by Kamil Nadłonek on 27.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class DialogNote extends BasePanel {

    public static final int MAX_WIDTH_PARENT_NAME = 100;
    private Dialog<NoteDto> dialog;
    private TextField titleText;
    private TextField shortDescText;
    private ComboBox<NoteDto> choiceBox;

    @Inject
    private NoteService noteService;

    public DialogNote() {
        ActionMap.register(this);
        createContent();
    }

    public void createContent() {
        dialog = new Dialog<>();
        dialog.setTitle("New note");
        dialog.setHeaderText("Create new note \n" +
                "press Okay (or click title bar 'X' for cancel).");
        dialog.setResizable(false);

        Label titleLabel = new Label("Title: ");
        Label shortDescriptionL = new Label("Short description: ");
        Label parent = new Label("Choose parent");
        titleText = new TextField();
        shortDescText = new TextField();
        ObservableList<NoteDto> noteDtos = FXCollections.observableArrayList(noteService.getAllNotes());
        choiceBox = new ComboBox<>(noteDtos);
        choiceBox.setButtonCell(new NoteListCell());
        choiceBox.setCellFactory(p -> new NoteListCell());

        Button clearButton = ActionUtils.createButton(ActionFactory.getAction("clearSelection", Icons.REMOVE_TAG));

        GridPane grid = new GridPane();
        grid.add(titleLabel, 1, 1);
        grid.add(titleText, 2, 1);
        grid.add(parent, 1, 3);
        grid.add(shortDescriptionL, 1, 2);
        grid.add(shortDescText, 2, 2);
        grid.add(choiceBox, 2, 3);
        grid.add(clearButton, 3, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == buttonTypeOk) {
                return new NoteDto().setTitle(titleText.getText()).setShortDescription(shortDescText.getText())
                        .setParentNote(choiceBox.getValue());
            }
            return null;
        });
    }

    @ActionProxy(text = "")
    private void clearSelection() {
        choiceBox.getSelectionModel().clearSelection();
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
