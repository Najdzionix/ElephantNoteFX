package com.kn.elephant.note;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.service.Test;
import com.kn.elephant.note.ui.ListNotePanel;
import com.kn.elephant.note.ui.MenuPanel;
import com.kn.elephant.note.ui.editor.DetailsNotePanel;
import com.kn.elephant.note.ui.editor.NotePanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

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
        Font font = Font.loadFont(Main.class.getResource("../../../../fonts/Lato-Regular.ttf").toExternalForm(), 20);

        LOGGER.info(font.toString());


        Scene scene = new Scene(mainPane);

        scene.getStylesheets().add(mainCss);
//        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Lato:700italic&subset=latin,latin-ext");

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        testService.hello();
    }

    private void buildUI() {
        mainPane = new BorderPane();
        mainPane.setLeft(new ListNotePanel());

        mainPane.setTop(new MenuPanel());
        mainPane.setCenter(new NotePanel(null));

        FontAwesome.Glyph glyph = FontAwesome.Glyph.LAPTOP;
        Color randomColor = new Color( Math.random(), Math.random(), Math.random(), 1);
        Glyph graphic = Glyph.create( "FontAwesome|" + glyph.name()).size(1.7).color(randomColor).useGradientEffect();
        Button button = new Button(glyph.name(), graphic);


        Button testButton =   new Button("", new Glyph("FontAwesome", "TRASH_ALT"));


//        testButton.getStyleClass().setAll("exit-button");
//        GlyphsDude.setIcon(testButton, FontAwesomeIcon.CLOSE, "6em");
        mainPane.setBottom(button);

//        mainPane.setStyle("-fx-border-color: red; -fx-border-width: 3;");
    }


}
