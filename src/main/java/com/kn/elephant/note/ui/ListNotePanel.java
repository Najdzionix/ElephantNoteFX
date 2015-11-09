package com.kn.elephant.note.ui;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.service.ElephantModule;
import com.kn.elephant.note.service.NoteService;
import javafx.collections.FXCollections;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
public class ListNotePanel extends BorderPane {
    private static final Logger LOGGER = LogManager.getLogger(ListNotePanel.class);
    public HiddenSidesPane contentPane;
    private GuiceContext context = new GuiceContext(this, () -> Collections.singletonList(new ElephantModule()));

    @Inject
    private NoteService noteService;

    public ListNotePanel() {
        context.init();
        ActionMap.register(this);
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-padding: 30");
        contentPane = new HiddenSidesPane();

        contentPane.setContent(getContent());
        contentPane.setTriggerDistance(10.0);
        contentPane.setLeft(new LeftMenuPanel());
        setCenter(contentPane);
    }

    private Node getContent() {
        ScrollPane pane = new ScrollPane();
        //todo remove
        pane.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pane.setContent(getListNotes());
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        return pane;
    }


    private Node getListNotes() {
        List<Note> notes = noteService.getAllNotes();
        TreeItem<Note> root = new TreeItem<>();
        for(Note note : notes) {
            TreeItem<Note> item = new TreeItem<>(note);
            if(!note.getSubNotes().isEmpty()){
               item.getChildren().addAll(note.getSubNotes().stream().map((Function<Note, TreeItem<Note>>) TreeItem::new
               ).collect(Collectors.toList()));
            }

            root.getChildren().add(item);
        }
        TreeView<Note> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);

        treeView.setCellFactory(param -> new NoteTreeCell());

        return treeView;
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
