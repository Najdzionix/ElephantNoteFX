package com.kn.elephant.note.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;
import org.controlsfx.control.action.ActionUtils;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class ListNotePanel extends BorderPane {
    private static final Logger LOGGER = LogManager.getLogger(ListNotePanel.class);
    public HiddenSidesPane contentPane;

    public ListNotePanel() {
        ActionMap.register(this);
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-padding: 30");
        contentPane = new HiddenSidesPane();

        contentPane.setContent(getContent());
        contentPane.setTriggerDistance(100.0);
        contentPane.setLeft(new LeftMenuPanel());
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

    @ActionProxy(text = "Toggle left menu")
    private void showLeftMenu() {
        LOGGER.info("Execute left Menu action ");
        if (contentPane.getPinnedSide() == Side.LEFT) {
            contentPane.setPinnedSide(null);
        } else {
            contentPane.setPinnedSide(Side.LEFT);
        }

    }
}
