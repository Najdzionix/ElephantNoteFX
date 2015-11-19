package com.kn.elephant.note.ui;

import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class Icons {


    public static Glyph REMOVE_TAG = Glyph.create("FontAwesome|" + FontAwesome.Glyph.CLOSE).size(20).color(Color.RED).useGradientEffect();
    public static final Glyph SAVE_TAG = Glyph.create("FontAwesome|" + FontAwesome.Glyph.CHECK).color(Color.GREEN).size(18);
    public static final Glyph EDIT_TITLE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.PENCIL).color(Color.BLACK).size(18);
    public static final Glyph SAVE_TITLE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.SAVE).color(Color.BLACK).size(18);
    public static final Glyph SAVE_NOTE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.SAVE).color(Color.BLACK).size
            (18);
    public static final Glyph REMOVE_NOTE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.TRASH).color(Color.BLACK)
            .size(18);

    public static final Glyph INFORM = Glyph.create("FontAwesome|" + FontAwesome.Glyph.INFO).color(Color.YELLOW)
            .size(30);

    public static final Glyph ERROR = Glyph.create("FontAwesome|" + FontAwesome.Glyph.EXCLAMATION_TRIANGLE).color(Color
            .RED).size(30);


}
