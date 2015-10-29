package com.kn.elephant.note;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class Main extends Application {

    public static final Logger LOGGER = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Start ElephantNoteFX.");

        primaryStage.setTitle("Hello in ElephantNoteFX alpha version");
        BorderPane pane = new BorderPane();
        primaryStage.setScene(new Scene(pane));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
