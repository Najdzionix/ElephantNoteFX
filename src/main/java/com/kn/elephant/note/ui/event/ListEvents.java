package com.kn.elephant.note.ui.event;

import com.kn.elephant.note.ui.BasePanel;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Created by Kamil Nadłonek on 20-08-2017
 * email:kamilnadlonek@gmail.com
 */
public class ListEvents extends BasePanel {

	private VBox menuBox;

	public ListEvents(){
		createContent();
		getStyleClass().addAll("menu-left");
	}

	private void createContent() {
		menuBox = new VBox();
		menuBox.getStyleClass().add("menu-panel");
		menuBox.getChildren().add(new Label("List events ..."));

		AnchorPane borderPane = new AnchorPane();
		borderPane.getChildren().add(menuBox);
		AnchorPane.setTopAnchor(menuBox, 0.0);
		AnchorPane.setBottomAnchor(menuBox, 0.0);

		setCenter(borderPane);
	}
}
