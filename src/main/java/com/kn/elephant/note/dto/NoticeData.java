package com.kn.elephant.note.dto;

import com.kn.elephant.note.ui.Icons;
import lombok.Data;
import lombok.experimental.Accessors;
import org.controlsfx.glyphfont.Glyph;

/**
 * Created by Kamil Nadłonek on 19.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Accessors(chain = true)
@Data
public class NoticeData {
    private String message;
    private Glyph icon;


    public NoticeData(String message) {
        this(message, Icons.INFORM);
    }

    public NoticeData(String message, Glyph icon) {
        this.message = message;
        this.icon = icon;
    }
}
