package com.kn.elephant.note.ui;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TimerTask;

import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

import com.google.inject.Inject;
import com.kn.elephant.note.Main;
import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.service.Reminder;
import com.kn.elephant.note.ui.control.SearchBox;
import com.kn.elephant.note.utils.ActionFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class MenuPanel extends BasePanel implements ChangeValue<View> {

    @Inject
    private NoteService noteService;
    private Map<View, Button> menuButtons = new LinkedHashMap<>();
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


        addNoteButton = ActionUtils.createButton(ActionMap.action("addNoteDialog"));
        addNoteButton.setVisible(true);
        createMenuButton("Main", View.MAIN);
        createMenuButton("Settings", View.SETTINGS);
        createMenuButton("Events", View.EVENTS);

        toolBar.getItems().addAll(menuButtons.values());
        toolBar.getItems().addAll(addNoteButton);
        Button reminder = new Button("Reminder");
        reminder.setOnAction( v -> {
            LocalDateTime time = LocalDateTime.now().plusSeconds(2);
            Reminder reminder1 = new Reminder(time);

            TimerTask timerTask = new TimerTask() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            log.info("Start Task: Time's up!%n");
                            Dialog<String> dialog = new Dialog<>();
                            dialog.setTitle("Hello2");
                            dialog.setContentText("Hello2");
                            dialog.getDialogPane().getStyleClass().add("card");
                            dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());
                            ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
                            final Button btOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
                            btOk.addEventFilter(ActionEvent.ACTION, event -> {
                                reminder1.getTimer().cancel();
                            });
                            dialog.show();
                     
                        }
                    });
                }
            };

            reminder1.setTask(timerTask);
            reminder1.schedule();

        });
        toolBar.getItems().add(reminder);
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
        menuButtons.put(view, button);
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

    @Override
    public void changeValue(View oldValue, View newValue) {
        addNoteButton.setVisible(View.MAIN.equals(newValue));
        menuButtons.entrySet().parallelStream().forEach(entry -> {
            entry.getValue().getStyleClass().remove(NoteConstants.CSS_ACTIVE);
            if (entry.getKey().equals(newValue)) {
                entry.getValue().getStyleClass().add(NoteConstants.CSS_ACTIVE);
            }
        });
    }
}
