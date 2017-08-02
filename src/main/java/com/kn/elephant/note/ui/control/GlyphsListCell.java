package com.kn.elephant.note.ui.control;

import de.jensd.fx.glyphs.GlyphIcon;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;

/**
 * Created by Kamil Nadłonek on 02-08-2017
 * email:kamilnadlonek@gmail.com
 */
public class GlyphsListCell extends ListCell<GlyphIcon> {

	public GlyphsListCell() {
		getStyleClass().add("glyph-grid-cell");
		setAlignment(Pos.CENTER);
	}

	@Override
	protected void updateItem(GlyphIcon item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setGraphic(null);
		} else {
			setGraphic(item);
		}
	}
}
