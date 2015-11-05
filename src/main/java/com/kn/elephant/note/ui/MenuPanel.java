package com.kn.elephant.note.ui;

import com.kn.elephant.note.Main;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class MenuPanel extends BorderPane {

    public MenuPanel() {
        getStyleClass().add("menuBar");
        setCenter(getSearchPanel());
    }

    private Node getSearchPanel() {
         //TODO create utils to load css file
        String searchBoxCss = Main.class.getResource("../../../../css/searchBox.css").toExternalForm();
        VBox vbox = new VBox();
        vbox.getStylesheets().add(searchBoxCss);
        vbox.setPrefWidth(200);
        vbox.setMaxWidth(Control.USE_PREF_SIZE);
        vbox.getStyleClass().add("search-box");
        vbox.getChildren().add(new SearchBox());

        ToolBar toolBar = new ToolBar();

        toolBar.getItems().add(new Button("Options1"));
        toolBar.getItems().add(new Button("Options2"));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);
        toolBar.getItems().add(vbox);
        return toolBar;
    }
}
