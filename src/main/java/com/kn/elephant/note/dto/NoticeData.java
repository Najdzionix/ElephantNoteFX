package com.kn.elephant.note.dto;

import com.kn.elephant.note.NoteConstants;
import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.octicons.OctIcon;
import javafx.scene.Node;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Kamil Nadłonek on 19.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Accessors(chain = true)
@Data
public class NoticeData {

    private String message;
    private Node icon;

    public NoticeData(String message) {
        this(message, OctIcon.INFO, NoteConstants.YELLOW_COLOR);
    }

    public static NoticeData createErrorNotice(String message) {
        return new NoticeData(message, OctIcon.ALERT, NoteConstants.RED_COLOR);
    }

    public NoticeData(String message, GlyphIcons icon, String color) {
        this.message = message;
        this.icon = GlyphsDude.createIcon(icon);;
        this.icon.getStyleClass().addAll("glyph-icon","icon-notification");
        String currentStyle = this.icon.getStyle();
        this.icon.setStyle(currentStyle + String.format("-fx-fill: %s; -fx-font-size: %s;", color, "2em"));
    }
}
