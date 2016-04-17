package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.Main;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.Icons;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;

/**
 * Created by Kamil Nadłonek on 14-04-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class SettingsPanel extends BasePanel {
    private static final String DB_KEY_PROPERTY="db.location";
    private static final String FIRST_RUN_KEY_PROPERTY= "first.run";
    private Properties prop;
    private Label location;


    public SettingsPanel() {
        loadPropertiesFile();
        getStyleClass().addAll("content-panel");
        setTop(createTitle("Settings"));
        setCenter(createContent());
        setPadding(new Insets(15)); //WTF ???
        setBottom(createSaveButton());
    }

    private Node createSaveButton() {
        HBox content = new HBox();
        content.getStyleClass().addAll("custom-pane");
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().addAll("button-blue");
        content.setAlignment(Pos.TOP_CENTER);
        saveButton.setOnAction(event -> {
            String pathDB = prop.getProperty(DB_KEY_PROPERTY);
            if(!pathDB.equals(location.getText())) {
                //TODO restart application ??
                try {
                    moveDBFile(pathDB, location.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                prop.setProperty(DB_KEY_PROPERTY, location.getText());
            }


        });

        content.getChildren().addAll(saveButton);
        return content;
    }

    private Node createTitle(String text) {
        BorderPane content = new BorderPane();
        content.getStyleClass().addAll("custom-pane");
        Text title = new Text(text);
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("title");
        content.setCenter(title);
        return content;
    }

    private Node createContent() {
        VBox content = new VBox();
        content.getStyleClass().addAll("custom-pane", "settings-panel");
        Label dbLabel = createLabel("Chose notes location:");

        content.getChildren().addAll(dbLabel, crateDBrow());
        return content;
    }

    private Node crateDBrow() {
        HBox content = new HBox();
        content.setSpacing(5.0);
        content.getStyleClass().addAll("db-row");
        location = new Label(prop.getProperty(DB_KEY_PROPERTY));
        location.getStyleClass().add("db-text");
        Button dicButton = new Button();
        Icons.addIcon(MaterialDesignIcon.FOLDER, dicButton, "1.1em");
        dicButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Chose new location for notes.");
            File defaultDirectory = new File(location.getText());
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(null);
            if (!location.getText().equals(selectedDirectory.getAbsolutePath())) {
                log.info("It has to move DB file.");
                //todo
            }
            location.setText(selectedDirectory.getAbsolutePath());
        });

        content.getChildren().addAll(location, dicButton);
        return content;
    }

    private void moveDBFile(String oldPath, String newPath) throws IOException {
        FileUtils.moveFile(FileUtils.getFile(oldPath), new File(newPath));
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("control-labelText");
        return label;
    }

    private void loadPropertiesFile() {
        try {
            prop = new Properties();
            prop.load(Main.class.getResourceAsStream("../../../../" + "settings.properties"));
        } catch (IOException e) {
            log.error("Not found properties file.", e);
        }
    }
}
