package com.kn.elephant.note.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconFactory;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.octicons.utils.OctIconFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class Icons {

    static {
        try {
//            Font.loadFont(GlyphsDude.class.getResource(FontAwesomeIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(MaterialDesignIconView.class.getResource(MaterialDesignIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(OctIconView.class.getResource(OctIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(MaterialIconView.class.getResource(MaterialIconView.TTF_PATH).openStream(), 10.0);
        } catch (IOException ex) {
            Logger.getLogger(MaterialDesignIconView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static final Glyph SAVE_TAG = Glyph.create("FontAwesome|" + FontAwesome.Glyph.CHECK).color(Color.GREEN).size(18);

    private static Text createGraphic(GlyphIcons icon, String size) {
        Text graphic = null;

        if (icon instanceof MaterialDesignIcon) {
            graphic = MaterialDesignIconFactory.get().createIcon(icon, size);
        } else if (icon instanceof MaterialIcon) {
            graphic = MaterialIconFactory.get().createIcon(icon, size);
        } else if (icon instanceof OctIcon) {
            graphic = OctIconFactory.get().createIcon(icon, size);
        } else {
            log.error("Not recognize icon");
        }
        return graphic;
    }

    public static void addIcon(GlyphIcons icon, Labeled node, String size) {
        Text graphic = createGraphic(icon, size);
        node.getStyleClass().add("buttonIcon");
        node.setGraphic(graphic);
    }

    public static Button createButtonWithIcon(String sizeIcon, String actionName, GlyphIcons icon) {
        Button saveButton = ActionFactory.createButtonWithAction(actionName);
        addIcon(icon, saveButton, sizeIcon);
        return saveButton;
    }

    public static Label builderIcon(GlyphIcons icon, String iconSize) {
        Label iconLabel = new Label();
        Text graphic = createGraphic(icon, iconSize);
        iconLabel.setGraphic(graphic);
        iconLabel.getStyleClass().add("noteIcon");
        return iconLabel;
    }
    public static Label builderIcon(GlyphIcons icon, String iconSize, String color) {
        Label iconLabel = builderIcon(icon, iconSize);
        iconLabel.setStyle(" -fx-fill: " + color + " !important;");
        return iconLabel;
    }



    public static ObservableList<GlyphIcons> getListNoteIcons() {
        List<GlyphIcons> listIcons = Arrays.asList(MaterialIcon.ACCOUNT_CIRCLE,
            MaterialIcon.WATCH_LATER,
            MaterialIcon.ACCOUNT_BOX,
            MaterialIcon.ADD_BOX,
            MaterialIcon.ALL_INCLUSIVE,
            MaterialIcon.BUILD,
            MaterialIcon.EMAIL,
            MaterialIcon.WB_INCANDESCENT,
            MaterialIcon.REMOVE_CIRCLE,
            MaterialIcon.LANGUAGE,
            MaterialIcon.STAR,
            MaterialIcon.SHOPPING_CART,
            MaterialIcon.DIRECTIONS_SUBWAY,
            MaterialIcon.REPORT,
            MaterialIcon.HOME
        );
        return FXCollections.observableArrayList(listIcons);
    }
}
