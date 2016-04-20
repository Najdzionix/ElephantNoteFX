package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.Utils;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Kamil Nadłonek on 14-04-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class SettingsPanel extends TitlePanel {
    private Properties properties;
    private Label location;

    public SettingsPanel() {
        loadPropertiesFile();
        setTitle("Settings");
        setContent(createContent());
    }

    private Node createSaveButton() {
        HBox content = new HBox();
        content.getStyleClass().addAll("custom-pane");
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().addAll("button-blue");
        content.setAlignment(Pos.TOP_CENTER);
        saveButton.setOnAction(event -> {
            String pathDB = properties.getProperty(NoteConstants.DB_KEY_PROPERTY);
            if (!pathDB.equals(location.getText())) {
                //TODO restart application ??
                try {
                    moveDBFile(pathDB, location.getText());
                } catch (IOException e) {
                    log.error("Error during move DB(notes) file.", e);
                }
                properties.setProperty(NoteConstants.DB_KEY_PROPERTY, location.getText());
            }
        });

        content.getChildren().addAll(saveButton);
        return content;
    }

    private Node createContent() {
        VBox content = new VBox();
        content.getStyleClass().addAll("settings-panel");
        Label dbLabel = createLabel("Chose notes location:");
        content.getChildren().addAll(dbLabel, crateDBrow(), createSaveButton());
        return content;
    }

    private Node crateDBrow() {
        HBox content = new HBox();
        content.setSpacing(5.0);
        content.getStyleClass().addAll("db-row");
        location = new Label(properties.getProperty(NoteConstants.DB_KEY_PROPERTY));
        location.getStyleClass().add("db-text");
        Button dicButton = new Button();
        Icons.addIcon(MaterialDesignIcon.FOLDER, dicButton, "1.1em");
        dicButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Chose new location for notes.");
            File defaultDirectory = new File(StringUtils.substringBeforeLast(location.getText(), File.separator));
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
        File property = Utils.createFile(NoteConstants.APP_DIC + NoteConstants.PROPERTY_FILE_NAME);
        properties = new Properties();
        try {
            properties.load(new FileInputStream(property));
        } catch (IOException e) {
            log.error("Can not find property file", e);
        }
    }
}
