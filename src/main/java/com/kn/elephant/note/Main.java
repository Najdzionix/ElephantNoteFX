package com.kn.elephant.note;

import com.gluonhq.ignite.guice.GuiceContext;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.ui.ChangeValue;
import com.kn.elephant.note.ui.MenuPanel;
import com.kn.elephant.note.ui.View;
import com.kn.elephant.note.ui.editor.NotePanel;
import com.kn.elephant.note.ui.leftMenu.ListNotePanel;
import com.kn.elephant.note.ui.setting.*;
import com.kn.elephant.note.utils.Utils;
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

import java.io.File;
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

    private BorderPane mainPane;
    private NotePanel notePanel;
    private LeftMenuPanel leftMenuPanel;
    private List<ChangeValue<View>> observersView = new ArrayList<>();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Start NoteFX.");
        context.init();
        ActionMap.register(this);
        primaryStage.setTitle("Hello in ElephantNoteFX alpha version");

        buildUI();
        Font.loadFont(Main.class.getResource("../../../../fonts/Lato-Regular.ttf").toExternalForm(), 20);
        mainPane.setMinHeight(NoteConstants.MIN_HEIGHT);
        mainPane.setMinWidth(NoteConstants.MIN_WIDTH);
        primaryStage.setMinWidth(NoteConstants.MIN_WIDTH);
        primaryStage.setMinHeight(NoteConstants.MIN_HEIGHT);
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
        checkConfiguration();
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("root");
        notePanel = new NotePanel();
        leftMenuPanel = new LeftMenuPanel();
        changeMainView(new ActionEvent(View.MAIN, null));
        MenuPanel menuPanel = new MenuPanel();
        mainPane.setTop(menuPanel);

        observersView.add(leftMenuPanel);
        observersView.add(menuPanel);
    }

    private void checkConfiguration() {
        if (!Utils.existsFile(NoteConstants.APP_DIC + File.separator + NoteConstants.PROPERTY_FILE_NAME)
                || !Utils.existsFile(Utils.getProperty(NoteConstants.DB_KEY_PROPERTY))) {
            new DialogDB();
        }
        //todo what do when path to DB is incorrect ?
    }

    @ActionProxy(text = "Settings")
    private void changeMainView(ActionEvent event) {
        View currentView = (View) event.getSource();
        observersView.parallelStream().forEach(observer -> observer.changeValue(currentView, currentView));
        if (View.SETTINGS.equals(currentView)) {
            mainPane.setLeft(leftMenuPanel);
            mainPane.setCenter(new SettingsPanel());
        } else if (View.TAG.equals(currentView)) {
            mainPane.setLeft(leftMenuPanel);
            mainPane.setCenter(new TagPanel());
        } else if (View.MAIN.equals(currentView)) {
            mainPane.setCenter(notePanel);
            mainPane.setLeft(new ListNotePanel());
        } else if (View.ABOUT.equals(currentView)) {
            mainPane.setCenter(new AboutPanel());
            mainPane.setLeft(leftMenuPanel);
        } else {
            log.warn("Not recognize type of view:" + currentView);
        }
        mainPane.requestLayout();
    }


}
