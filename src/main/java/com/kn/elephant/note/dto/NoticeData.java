package com.kn.elephant.note.dto;

import com.kn.elephant.note.NoteConstants;

import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
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

    public static final String NORMAL = "NORMAL";
    private String message;
    private Node icon;
    private int displayTime = 4000;
    private String type;
    public NoticeData(String message) {
        this(message, OctIcon.INFO, NoteConstants.YELLOW_COLOR);
    }

    public static NoticeData createErrorNotice(String message) {
        return new NoticeData(message, OctIcon.ALERT, NoteConstants.RED_COLOR);
    }

    public NoticeData(String message, OctIcon icon, String color) {
        this.message = message;
        this.icon = new OctIconView(icon);
        this.icon.getStyleClass().addAll("glyph-icon","icon-notification");
        String currentStyle = this.icon.getStyle();
        this.icon.setStyle(currentStyle + String.format("-fx-fill: %s; -fx-font-size: %s;", color, "2em"));
    }

    public String getType() {
        return NORMAL;
    }
}
