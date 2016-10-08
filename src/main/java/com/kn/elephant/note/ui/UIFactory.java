package com.kn.elephant.note.ui;

import javafx.scene.control.Label;

/**
 * Created by Kamil Nadłonek on 08-10-2016
 * email:kamilnadlonek@gmail.com
 */
public class UIFactory {


	public static Label createLabel(String text) {
		Label label = new Label(text);
		label.getStyleClass().add("control-labelText");
		return label;
	}
}
