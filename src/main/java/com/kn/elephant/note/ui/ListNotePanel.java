package com.kn.elephant.note.ui;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.HiddenSidesPane;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class ListNotePanel extends BorderPane {

    HiddenSidesPane contentPane;

    public ListNotePanel() {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-padding: 30");
        contentPane = new HiddenSidesPane();

        contentPane.setContent(getContent());
        contentPane.setTriggerDistance(100.0);
        contentPane.setLeft(new LeftMenuPanel());
//        contentPane.setPinnedSide(Side.LEFT);
        setCenter(contentPane);
    }

    private Node getContent() {
        ScrollPane pane = new ScrollPane();
        ListView<String> listNodes = new ListView<>();
        listNodes.setItems(FXCollections.observableArrayList(
                "Note 1", "Note 2", "Long Note 3", "Note 4", "Note 5", "Note 6"
        ));

        TestNode testNode = new TestNode("List notes");
        pane.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pane.setContent(testNode);
        return pane;
    }
}
