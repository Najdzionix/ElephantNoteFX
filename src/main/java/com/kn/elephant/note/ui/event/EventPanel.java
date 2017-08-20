package com.kn.elephant.note.ui.event;

import com.kn.elephant.note.ui.setting.TitlePanel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
public class EventPanel extends TitlePanel {

	public EventPanel() {
		setTitle("Event name");
		setContent(createContent());
	}

	private Node createContent() {
		HBox box = new HBox();
		Text text = new Text("Content Event ....");
		box.getChildren().addAll(text);
		box.setAlignment(Pos.TOP_CENTER);
		return box;
	}
}
