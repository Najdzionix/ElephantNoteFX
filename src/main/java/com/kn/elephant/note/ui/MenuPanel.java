package com.kn.elephant.note.ui;

import com.google.inject.Inject;
import com.kn.elephant.note.Main;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.utils.ActionFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

import java.util.Optional;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class MenuPanel extends BasePanel {

    @Inject
    private NoteService noteService;

    public MenuPanel() {
        ActionMap.register(this);
        getStyleClass().add("menuBar");
        setCenter(getSearchPanel());
    }

    private Node getSearchPanel() {
        //TODO create utils to load css file
        String searchBoxCss = Main.class.getResource("../../../../css/searchBox.css").toExternalForm();
        VBox vbox = new VBox();
        vbox.getStylesheets().add(searchBoxCss);
        vbox.setPrefWidth(200);
        vbox.setMaxWidth(Control.USE_PREF_SIZE);
        vbox.getStyleClass().add("search-box");
        vbox.getChildren().add(new SearchBox());

        ToolBar toolBar = new ToolBar();

        Button leftMenuButton = ActionUtils.createButton(ActionMap.action("showLeftMenu"));

        toolBar.getItems().add(leftMenuButton);
        toolBar.getItems().add(ActionUtils.createButton(ActionMap.action("addNoteDialog")));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);
        toolBar.getItems().add(vbox);
        return toolBar;
    }

    @ActionProxy(text = "Add note")
    private void addNoteDialog() {
        DialogNote dialogNote = new DialogNote();
        Optional<NoteDto> noteDto = dialogNote.getDialog().showAndWait();
        if (noteDto.isPresent()) {
            Optional<NoteDto> dto = noteService.saveNote(noteDto.get());
            ActionFactory.callAction("addNoteToList", dto);
        }
    }
}
