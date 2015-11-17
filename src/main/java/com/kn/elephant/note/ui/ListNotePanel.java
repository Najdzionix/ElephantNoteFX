package com.kn.elephant.note.ui;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class ListNotePanel extends BasePanel {
    public HiddenSidesPane contentPane;

    @Inject
    private NoteService noteService;
    private  TreeView<NoteDto> treeView;
    public ListNotePanel() {
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
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pane.setContent(getListNotes());
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        return pane;
    }


    private Node getListNotes() {
        List<NoteDto> noteDtos = noteService.getAllNotes();
        Collections.reverse(noteDtos);
        TreeItem<NoteDto> root = new TreeItem<>();
        for (NoteDto noteDto : noteDtos) {
            TreeItem<NoteDto> item = new TreeItem<>(noteDto);
            if (!noteDto.getSubNotes().isEmpty()) {
                item.getChildren().addAll(noteDto.getSubNotes().stream().map(TreeItem::new
                ).collect(Collectors.toList()));
            }

            root.getChildren().add(item);
        }
        treeView = new TreeView<>(root);
        treeView.setShowRoot(false);

        treeView.setCellFactory(param -> new NoteTreeCell());
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                log.debug("Change note");
                ActionMap.action("loadNote").handle(new ActionEvent(newValue.getValue(), null));
            }
        });
        treeView.getSelectionModel().select(0);
        return treeView;
    }

    @ActionProxy(text = "Toggle left menu")
    private void showLeftMenu() {
        log.info("Execute left Menu action ");
        if (contentPane.getPinnedSide() == Side.LEFT) {
            contentPane.setPinnedSide(null);
        } else {
            contentPane.setPinnedSide(Side.LEFT);
        }
    }

    @ActionProxy(text = "")
    private void removeNote(ActionEvent event) {
       log.info("Remove note from list ...");
        TreeItem<NoteDto> selectedItem = treeView.getSelectionModel().getSelectedItem();
        selectedItem.getParent().getChildren().remove(selectedItem);
    }
}
