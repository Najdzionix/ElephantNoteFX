package com.kn.elephant.note.ui;

import com.google.inject.Inject;
import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.utils.ActionFactory;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class MenuPanel extends BasePanel implements ChangeValue<View> {

    @Inject
    private NoteService noteService;
    private ToggleButton modeButton;
    private List<Button> menuButtons = new ArrayList<>();
    private Button addNoteButton;
    private ToolBar toolBar;

    public MenuPanel() {
        ActionMap.register(this);
        getStyleClass().add("menuBar");
        setCenter(getSearchPanel());
    }

    private Node getSearchPanel() {
        VBox vbox = new VBox();
        vbox.setPrefWidth(200);
        vbox.setMaxWidth(Control.USE_PREF_SIZE);
        vbox.getStyleClass().add("search-box");
        vbox.getChildren().add(new SearchBox());

        toolBar = new ToolBar();
        toolBar.getStyleClass().add("tool-bar-menu");
//        modeButton = ActionUtils.createToggleButton(ActionMap.action("switchDisplayMode"));
//        modeButton.setSelected(true);

        addNoteButton = ActionUtils.createButton(ActionMap.action("addNoteDialog"));
        addNoteButton.setVisible(true);
        createMenuButton("Main", View.MAIN);
        createMenuButton("Settings", View.SETTINGS);

        toolBar.getItems().addAll(menuButtons);
        toolBar.getItems().addAll(addNoteButton);
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);
        toolBar.getItems().add(vbox);
        return toolBar;
    }

    private Button createMenuButton(String text, View view) {
        Button button = new Button(text);
        button.setOnAction(event -> {
            getStyleClass().add(NoteConstants.CSS_ACTIVE);
            ActionFactory.callAction("changeMainView", view);
        });
        menuButtons.add(button);
        return button;
    }

    @ActionProxy(text = "Add note")
    private void addNoteDialog() {
        DialogNote dialogNote = new DialogNote();
        Optional<NoteDto> noteDto = dialogNote.getDialog().showAndWait();
        if (noteDto.isPresent()) {
            Optional<NoteDto> dto = noteService.saveNote(noteDto.get());
            ActionFactory.callAction("addNoteToList", dto.get());
        }
    }

    @ActionProxy(text = "")
    private void setEditMode(ActionEvent event) {
        modeButton.setSelected((Boolean) event.getSource());
    }

    @Override
    public void changeValue(View oldValue, View newValue) {

        if (View.MAIN.equals(newValue)) {
            addNoteButton.setVisible(true);
        } else {
            addNoteButton.setVisible(false);
        }

        menuButtons.parallelStream().forEach(button -> {
            button.getStyleClass().remove(NoteConstants.CSS_ACTIVE);
//            if (newValue.equals(button.getView())) {
//                button.getStyleClass().add(NoteConstants.CSS_ACTIVE);
//            }
        });
    }
}
