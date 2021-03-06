package com.kn.elephant.note.ui.leftMenu;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

import com.google.inject.Inject;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.service.NoteService;
import com.kn.elephant.note.ui.BasePanel;
import com.kn.elephant.note.utils.ActionFactory;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class ListNotePanel extends BasePanel {
	public HiddenSidesPane contentPane;

	@Inject
	private NoteService noteService;
	private TreeView<NoteDto> treeView;

	public ListNotePanel() {
		ActionMap.register(this);
		getStyleClass().addAll("menu-left");
		contentPane = new HiddenSidesPane();
		contentPane.setContent(getContent());
		contentPane.setTriggerDistance(10.0);
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
				ActionFactory.callAction("loadNote", newValue.getValue());
//                ActionFactory.callAction("setEditButton", true);
				newValue.setExpanded(true);
			}
		});
		treeView.getSelectionModel().select(0);
		treeView.setStyle(" -fx-background-color: -color-white-gray;");
		return treeView;
	}

	@ActionProxy(text = "")
	private void removeNoteFromList(ActionEvent event) {
		TreeItem<NoteDto> selectedItem = treeView.getSelectionModel().getSelectedItem();
		log.debug("Remove note from list:" + selectedItem.getValue());
		ObservableList<TreeItem<NoteDto>> children = selectedItem.getChildren();
		if (!children.isEmpty()) {
			selectedItem.getParent().getChildren().addAll(children);
		}
		selectedItem.getParent().getChildren().remove(selectedItem);
	}

	@ActionProxy(text = "")
	private void addNoteToList(ActionEvent event) {
		log.debug("Add item to tree");
		NoteDto noteDto = (NoteDto) event.getSource();
		TreeItem<NoteDto> item = new TreeItem<>(noteDto);
		if (noteDto.getParentNote() == null) {
			treeView.getRoot().getChildren().add(item);
		} else {
			Optional<TreeItem<NoteDto>> parent = findParent(noteDto.getParentNote());
			if (parent.isPresent()) {
				TreeItem<NoteDto> parentTreeItem = parent.get();
				log.debug("Found parent :" + parentTreeItem.getValue());
				parentTreeItem.getChildren().add(item);
			} else {
				log.warn("No found parent! It should be:" + noteDto.getParentNote());
			}
		}
		treeView.getSelectionModel().select(item);
		treeView.refresh();
	}
	@ActionProxy(text = "")
	private void refreshList() {
		log.info("Refresh list ALL");
		contentPane.setContent(getContent());
	}

	@ActionProxy(text = "")
	private void refreshNote(ActionEvent event) {
		log.debug("Refresh item to tree");
		NoteDto noteDto = (NoteDto) event.getSource();
		if (noteDto.getParentNote() == null) {
			Optional<TreeItem<NoteDto>> noteItem = findParent(noteDto);
			if (noteItem.isPresent()) {
				log.debug("Refresh note:" + noteItem.get().getValue());
				noteItem.get().setValue(noteDto);
			}

		}

		treeView.refresh();
	}

	@ActionProxy(text = "")
	private void setSelectNote(ActionEvent event) {
		NoteDto noteDto = (NoteDto) event.getSource();
		Optional<TreeItem<NoteDto>> noteItem = findNoteItem(noteDto);
		if (noteItem.isPresent()) {
			treeView.getSelectionModel().select(noteItem.get());
		}
	}

	private Optional<TreeItem<NoteDto>> findNoteItem(NoteDto noteDto) {
		Optional<TreeItem<NoteDto>> parent = findParent(noteDto);
		if (parent.isPresent()) {
			return parent;
		} else {
			return treeView.getRoot().getChildren().parallelStream()
				.flatMap(noteDtoTreeItem -> noteDtoTreeItem.getChildren().stream())
				.filter(tr -> tr.getValue().getId() == noteDto.getId()).findFirst();
		}
	}

	private Optional<TreeItem<NoteDto>> findParent(NoteDto noteDto) {
		return treeView.getRoot().getChildren().stream().filter(tr -> tr.getValue().getId() == noteDto.getId()).findFirst();
	}
}
