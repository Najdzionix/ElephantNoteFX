package com.kn.elephant.note.utils;

import com.kn.elephant.note.NoteConstants;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 30-3-16.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class ListenerFactory {

    public static ChangeListener<Boolean> getListenerHover(Node node) {
        return (observable, oldValue, newValue) -> {
            if (!node.getStyle().contains(NoteConstants.WHITE)) {
                if (newValue) {
                    node.setStyle("-fx-border-color: " + NoteConstants.ORANGE_COLOR + "  -fx-font-weight: bold;");
                } else {
                    node.setStyle("-fx-border-color: " + NoteConstants.GRAY_DIVIDER);
                }
            }
        };
    }

    public static ChangeListener<Boolean> getListenerFocused(Node node) {
        return (observable, oldValue, newValue) -> {
                if (newValue) {
                    node.setStyle("-fx-border-color: " + NoteConstants.WHITE + "  -fx-font-weight: bold;");
                } else {
                    node.setStyle("-fx-border-color: " + NoteConstants.GRAY_DIVIDER);
                }
        };
    }
}
