package com.kn.elephant.note.ui;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import org.controlsfx.control.PopOver;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class SearchBox  extends Region {

    private TextField textBox;
    private Button clearButton;

    public SearchBox() {
        setId("SearchBox");
        getStyleClass().add("search-box");
        setMinHeight(24);
        setPrefSize(200, 24);
        setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        textBox = new TextField();
        textBox.setOnAction(event -> { createPopOverResults().show(textBox);});
        textBox.setPromptText("Search");
        clearButton = new Button("X");
        clearButton.setVisible(false);
        getChildren().addAll(textBox, clearButton);
        clearButton.setOnAction((ActionEvent actionEvent) -> {
            textBox.setText("");
            createPopOverResults().show(textBox);
            textBox.requestFocus();
        });
        textBox.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            clearButton.setVisible(textBox.getText().length() != 0);
        });
    }

    private PopOver createPopOverResults() {
        PopOver popOver = new PopOver();
        popOver.setDetachable(false);
        popOver.setContentNode(new TestNode("Search results ..."));
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popOver.setArrowIndent(25);
        popOver.setArrowSize(20);
        popOver.setHeaderAlwaysVisible(true);
        return popOver;
    }

    @Override
    protected void layoutChildren() {
        textBox.resize(getWidth(), getHeight());
        clearButton.resizeRelocate(getWidth() - 18, 6, 12, 13);
    }
}
