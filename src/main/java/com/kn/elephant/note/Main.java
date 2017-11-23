package com.kn.elephant.note;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import com.gluonhq.ignite.guice.GuiceContext;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.ui.ChangeValue;
import com.kn.elephant.note.ui.MenuPanel;
import com.kn.elephant.note.ui.View;
import com.kn.elephant.note.ui.editor.NotePanel;
import com.kn.elephant.note.ui.event.EventPanel;
import com.kn.elephant.note.ui.event.ListEvents;
import com.kn.elephant.note.ui.leftMenu.ListNotePanel;
import com.kn.elephant.note.ui.setting.AboutPanel;
import com.kn.elephant.note.ui.setting.DialogDB;
import com.kn.elephant.note.ui.setting.LeftMenuPanel;
import com.kn.elephant.note.ui.setting.SettingsPanel;
import com.kn.elephant.note.ui.setting.TagPanel;
import com.kn.elephant.note.utils.Utils;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class Main extends Application {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static HostServices hostServices;
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
        Font.loadFont(Main.class.getResource("/fonts/Lato-Regular.ttf").toExternalForm(), 20);
        mainPane.setMinHeight(NoteConstants.MIN_HEIGHT);
        mainPane.setMinWidth(NoteConstants.MIN_WIDTH);
        primaryStage.setMinWidth(NoteConstants.MIN_WIDTH);
        primaryStage.setMinHeight(NoteConstants.MIN_HEIGHT);
        Scene scene = new Scene(mainPane);
        scene.getStylesheets().addAll(loadCssFiles());
        new Shortcuts(scene);
        SchedulerEvents schedulerEvents = new SchedulerEvents();
        schedulerEvents.start();
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        hostServices = getHostServices();

        primaryStage.setOnCloseRequest(we -> {
            schedulerEvents.getTasks().forEach(timerTask -> {
                log.debug("Close timer task: {}", timerTask.cancel());
            });
            primaryStage.close();
            System.exit(1);
        });

    }

    public static List<String> loadCssFiles() {
        String[] cssNames = {"material-fx-v0_3.css", "main.css"};
        List<String> cssFiles = new ArrayList<>();
        for (String cssName : cssNames) {
            cssFiles.add(Main.class.getResource("/css/" + cssName).toExternalForm());
        }

        return cssFiles;
    }

    private void buildUI() {
        checkConfiguration();
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("root");
        notePanel = new NotePanel();
        notePanel.setApp(this);
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

        switch (currentView) {
        case MAIN:
            mainPane.setCenter(notePanel);
            mainPane.setLeft(new ListNotePanel());
            break;
        case TAG:
            mainPane.setCenter(new TagPanel());
            mainPane.setLeft(leftMenuPanel);
            break;
        case ABOUT:
            mainPane.setCenter(new AboutPanel());
            mainPane.setLeft(leftMenuPanel);
            break;
        case SETTINGS:
            mainPane.setCenter(new SettingsPanel());
            mainPane.setLeft(leftMenuPanel);
            break;
        case EVENTS:
            EventPanel eventPanel = new EventPanel();
            mainPane.setCenter(eventPanel);
            mainPane.setLeft(new ListEvents(eventPanel));
            break;
        default:
            log.warn("Not recognize type of view:" + currentView);
            break;
        }
        mainPane.requestLayout();
    }

    public static HostServices getHostService() {
        return hostServices ;
    }
}
