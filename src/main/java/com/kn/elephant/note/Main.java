package com.kn.elephant.note;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.service.Test;
import com.kn.elephant.note.ui.ListNotePanel;
import com.kn.elephant.note.ui.MenuPanel;
import com.kn.elephant.note.ui.TestNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

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
        buildUI();
        String mainCss = Main.class.getResource("../../../../css/main.css").toExternalForm();
        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(mainCss);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        testService.hello();
    }

    private void buildUI() {
        mainPane = new BorderPane();
        mainPane.setLeft(new ListNotePanel());

        mainPane.setTop(new MenuPanel());
        mainPane.setCenter(new TestNode("Content"));
//        mainPane.setStyle("-fx-border-color: red; -fx-border-width: 3;");
    }



}
