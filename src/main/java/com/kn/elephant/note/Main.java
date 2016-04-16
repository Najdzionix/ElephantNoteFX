package com.kn.elephant.note;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.service.Test;
import com.kn.elephant.note.ui.MenuPanel;
import com.kn.elephant.note.ui.View;
import com.kn.elephant.note.ui.editor.NotePanel;
import com.kn.elephant.note.ui.leftMenu.ListNotePanel;
import com.kn.elephant.note.ui.setting.LeftMenuPanel;
import com.kn.elephant.note.ui.setting.SettingsPanel;
import com.kn.elephant.note.ui.setting.TagPanel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class Main extends Application {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private GuiceContext context = new GuiceContext(this, () -> Collections.singletonList(new ElephantModule()));

    @Inject
    private Test testService;

    private BorderPane mainPane;
    private NotePanel notePanel;
    private LeftMenuPanel leftMenuPanel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Start ElephantNoteFX.");
        context.init();
        ActionMap.register(this);
        primaryStage.setTitle("Hello in ElephantNoteFX alpha version");
        if (NoteConstants.CREATE_DATA_BASE) {
            testService.insertExampleData();
        }
        buildUI();
        Font.loadFont(Main.class.getResource("../../../../fonts/Lato-Regular.ttf").toExternalForm(), 20);

        Scene scene = new Scene(mainPane);
        scene.getStylesheets().addAll(loadCssFiles());
//        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Lato:700italic&subset=latin,latin-ext");

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static List<String> loadCssFiles() {
//        "materialfx-toggleswitch.css"
        String[] cssNames = {"material-fx-v0_3.css", "main.css"};
        List<String> cssFiles = new ArrayList<>();
        for (String cssName : cssNames) {
            cssFiles.add(Main.class.getResource("../../../../css/" + cssName).toExternalForm());
        }

        return cssFiles;
    }

    private void buildUI() {
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("root");
        notePanel = new NotePanel();
        leftMenuPanel = new LeftMenuPanel();
        changeMainView(new ActionEvent(View.MAIN, null));
        mainPane.setTop(new MenuPanel());
    }

    @ActionProxy(text = "Settings")
    private void changeMainView(ActionEvent event) {
        View view = (View) event.getSource();
        if (View.SETTINGS.equals(view)) {
            mainPane.setLeft(leftMenuPanel);
            mainPane.setCenter(new SettingsPanel());
        } else if (View.TAG.equals(view)) {
            mainPane.setLeft(leftMenuPanel);
            mainPane.setCenter(new TagPanel());
        } else if (View.MAIN.equals(view)) {
            mainPane.setCenter(notePanel);
            mainPane.setLeft(new ListNotePanel());
        } else if (View.ABOUT.equals(view)) {
            mainPane.setCenter(new TagPanel());
            mainPane.setLeft(leftMenuPanel);
        } else {
            log.warn("Not recognize type of view:" + view);
        }
        mainPane.requestLayout();
    }
}
