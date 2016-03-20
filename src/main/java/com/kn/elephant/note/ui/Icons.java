package com.kn.elephant.note.ui;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.control.action.Action;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class Icons {

    static {
        try {
//            Font.loadFont(GlyphsDude.class.getResource(FontAwesomeIconView.TTF_PATH).openStream(), 10.0);
//            Font.loadFont(GlyphsDude.class.getResource(WeatherIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(GlyphsDude.class.getResource(MaterialDesignIconView.TTF_PATH).openStream(), 10.0);
//            Font.loadFont(GlyphsDude.class.getResource(MaterialIconView.TTF_PATH).openStream(), 10.0);
//            Font.loadFont(GlyphsDude.class.getResource(OctIconView.TTF_PATH).openStream(), 10.0);
        } catch (IOException ex) {
            Logger.getLogger(MaterialDesignIconView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Glyph REMOVE_TAG = Glyph.create("FontAwesome|" + FontAwesome.Glyph.CLOSE).size(20).color(Color.RED).useGradientEffect();
    public static final Glyph SAVE_TAG = Glyph.create("FontAwesome|" + FontAwesome.Glyph.CHECK).color(Color.GREEN).size(18);
    public static final Glyph SAVE_TITLE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.SAVE).color(Color.BLACK).size(18);
    public static final Glyph SAVE_NOTE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.SAVE).color(Color.BLACK).size
            (18);
    public static final Glyph REMOVE_NOTE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.TRASH).color(Color.BLACK)
            .size(18);

    public static final Glyph INFORM = Glyph.create("FontAwesome|" + FontAwesome.Glyph.INFO).color(Color.YELLOW)
            .size(30);

    public static final Glyph ERROR = Glyph.create("FontAwesome|" + FontAwesome.Glyph.EXCLAMATION_TRIANGLE).color(Color
            .RED).size(30);
    public static final Glyph EDIT_TITLE = Glyph.create("FontAwesome|" + FontAwesome.Glyph.PENCIL).color(Color.BLACK).size(16);


    public static void addIcon(GlyphIcons icon, Action action, String size) {
        action.setText(icon.characterToString());
        action.getStyleClass().add("glyph-icon");
        action.setStyle(String.format("-fx-font-family: %s; -fx-font-size: %s;", icon.getFontFamily(), size));
    }
}
