package com.kn.elephant.note.ui.control;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.action.ActionMap;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.utils.ActionFactory;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.octicons.OctIcon;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 06-05-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class ListNotesControl {
	private PopOver popOver = new PopOver();
	@Setter
	@Getter
	private Node owner = null;
	@Getter
	private String title = null;

	public ListNotesControl(String title) {
		this.title = title;
		ActionMap.register(this);
		initPopOver();
	}

	private void initPopOver() {
		popOver.setArrowIndent(25);
		popOver.setArrowSize(20);
		popOver.getStyleClass().add("popoverM");
		popOver.getRoot().getStyleClass().add("popoverM");
		popOver.setDetachable(false);
		popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
		if (StringUtils.isNotBlank(title)) {
			popOver.setHeaderAlwaysVisible(true);
			popOver.setTitle(title);
		}
	}

	public void setArrowLocation(PopOver.ArrowLocation location) {
		popOver.setArrowLocation(location);
	}

	public void showNotes(List<NoteDto> notes) {
		if (popOver.isShowing()) {
			log.debug("Is showing result of search.");
			popOver.setContentNode(createListOfNotes(notes));
		} else {
			popOver.setContentNode(createListOfNotes(notes));
			popOver.show(owner);
		}
	}

	private Node createListOfNotes(List<NoteDto> notes) {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.getStyleClass().add("content-search");
		if (notes.isEmpty()) {
			scrollPane.setContent(createEmptyResultPane());
		} else {
			VBox list = new VBox();
			list.getStyleClass().add("list-search-notes");
			list.setSpacing(2.0);
			List<Node> nodes = notes.parallelStream().map(NoteNode::new).collect(Collectors.toList());
			nodes.parallelStream().forEach(noteNode -> noteNode.setOnMouseClicked(event -> {
				ActionFactory.callAction("loadNote", ((NoteNode) noteNode).getNoteDto());
				ActionFactory.callAction("setSelectNote", ((NoteNode) noteNode).getNoteDto());

				clearSelectedNoteNodes();
				noteNode.getStyleClass().add("selected-node");
			}));

			list.getChildren().addAll(nodes);
			scrollPane.setContent(list);
		}
		return scrollPane;
	}

	private Node createEmptyResultPane() {
		HBox box = new HBox();
		box.setSpacing(5);
		box.setPrefHeight(110);
		box.getStyleClass().add("no-results");
		Label text = new Label("Not found notes.");
		text.setTextAlignment(TextAlignment.CENTER);
		text.getStyleClass().add("not-found-text");
		Node icon = GlyphsDude.createIcon(OctIcon.ALERT);
		icon.getStyleClass().addAll("glyph-icon", "alert-icon");
		String currentStyle = icon.getStyle();
		icon.setStyle(currentStyle + String.format("-fx-font-size: %s; ","4em"));

		box.getChildren().addAll(icon, text);
		return box;
	}

	private void clearSelectedNoteNodes() {
		ScrollPane scroll = (ScrollPane) popOver.getContentNode();
		ObservableList<Node> children = ((VBox) scroll.getContent()).getChildren();
		children.forEach(node -> node.getStyleClass().remove("selected-node"));
	}
}
