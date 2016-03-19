package com.kn.elephant.note;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.service.Test;
import com.kn.elephant.note.ui.MenuPanel;
import com.kn.elephant.note.ui.editor.NotePanel;
import com.kn.elephant.note.ui.leftMenu.ListNotePanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class Main extends Application {

    public static final Logger LOGGER = LogManager.getLogger(Main.class);
    private GuiceContext context = new GuiceContext(this, () -> Collections.singletonList(new ElephantModule()));

    @Inject
    private Test testService;

    private BorderPane mainPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Start ElephantNoteFX.");
        context.init();
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

        testService.hello();
    }

    public static List<String> loadCssFiles() {
        String[] cssNames = {"materialfx-toggleswitch.css", "material-fx-v0_3.css", "main.css"};
        List<String> cssFiles = new ArrayList<>();
        for (String cssName : cssNames) {
            cssFiles.add(Main.class.getResource("../../../../css/" + cssName).toExternalForm());
        }

        return cssFiles;
    }

    private void buildUI() {
        mainPane = new BorderPane();
        mainPane.setCenter(new NotePanel(null));
        mainPane.setLeft(new ListNotePanel());
        mainPane.setTop(new MenuPanel());
//        mainPane.setStyle("-fx-border-color: red; -fx-border-width: 3;");
    }


}
