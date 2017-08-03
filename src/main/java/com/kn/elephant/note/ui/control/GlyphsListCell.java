package com.kn.elephant.note.ui.control;

import com.kn.elephant.note.utils.Icons;

import de.jensd.fx.glyphs.GlyphIcons;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

/**
 * Created by Kamil Nadłonek on 02-08-2017
 * email:kamilnadlonek@gmail.com
 */
public class GlyphsListCell extends ListCell<GlyphIcons> {

	private static final String ICON_SIZE = "2.0em";
	private static final String GLYPH_GRID_CELL_CSS = "glyph-grid-cell";

	public GlyphsListCell() {
        getStyleClass().add(GLYPH_GRID_CELL_CSS);
        setAlignment(Pos.CENTER);
    }

    @Override
    public void updateItem(GlyphIcons item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            Node shape = Icons.builderIcon(item, ICON_SIZE);
            setGraphic(shape);
        }
    }
}
