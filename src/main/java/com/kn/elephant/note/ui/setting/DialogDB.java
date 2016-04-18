package com.kn.elephant.note.ui.setting;

import com.kn.elephant.note.Main;
import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.Icons;
import com.kn.elephant.note.utils.NoteException;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static com.kn.elephant.note.utils.Utils.createFile;

/**
 * Created by Kamil Nadłonek on 17-04-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class DialogDB extends BasePanel {

    private Label location;

    public DialogDB() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Chose location for notes.");
        dialog.setHeaderText("Not found file with notes. \nIf it is first run, please provide directory where will be save notes,\n otherwise provide last location notes.");
        dialog.setResizable(false);
        dialog.getDialogPane().getStyleClass().add("card");
        dialog.getDialogPane().setContent(crateDBrow());

        createAppFolder();

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            String path = location.getText();
            if (StringUtils.isNotBlank(path)) {
                String pathDbFile = path + (path.endsWith(File.separator) ? NoteConstants.DB_FILE_NAME : File.separator + NoteConstants.DB_FILE_NAME);
                createPropertyFile(pathDbFile);
            } else {
                event.consume();
            }

        });
        dialog.getDialogPane().getStylesheets().addAll(Main.loadCssFiles());
        dialog.showAndWait();
    }

    private File createAppFolder() {
        log.info(NoteConstants.APP_DIC);
        return createFile(NoteConstants.APP_DIC);
    }

    private void createPropertyFile(String pathDbFile) {
        Properties properties = new Properties();
        properties.put(NoteConstants.DB_KEY_PROPERTY, pathDbFile);
        properties.put(NoteConstants.VERSION_KEY_PROPERTY, NoteConstants.APP_VERSION);
        File propertyFile = createFile(NoteConstants.APP_DIC + NoteConstants.PROPERTY_FILE_NAME);
        try {
            properties.store(new FileOutputStream(propertyFile), "Never mind:)");
        } catch (IOException e) {
            log.error("Can not properties file :" + propertyFile.getAbsolutePath(), e);
            throw new NoteException("Can not create properties file on location: " + propertyFile.getAbsolutePath());
        }
    }

    private Node crateDBrow() {
        HBox content = new HBox();
        content.setSpacing(5.0);
        content.getStyleClass().addAll("db-row");
        location = new Label(NoteConstants.APP_DIC);
        location.getStyleClass().add("db-text");
        Button dicButton = new Button();
        Icons.addIcon(MaterialDesignIcon.FOLDER, dicButton, "1.1em");
        dicButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Chose new location for notes.");
            File defaultDirectory = new File(location.getText());
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(null);
            location.setText(selectedDirectory.getAbsolutePath());
        });

        content.getChildren().addAll(location, dicButton);
        return content;
    }
}
