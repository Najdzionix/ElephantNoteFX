package com.kn.elephant.note.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.glyphfont.Glyph;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 * Created by Kamil Nadłonek on 17.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class ActionFactory {
    private static final Logger LOGGER = LogManager.getLogger(ActionFactory.class);

    public static final String UPDATE_TITLE_ACTION_NAME = "updateTitle";
    private static Map<String, Action> actions = new HashMap<>();

    public static Action getUpdateNoteTitle() {
        return getAction(UPDATE_TITLE_ACTION_NAME);
    }

    public static void callAction(String actionName) {
        callAction(actionName, null);
    }

    public static void callAction(String actionName, Object value) {
        getAction(actionName).handle(new ActionEvent(value, null));
    }

    public static Action getAction(String actionName) {
        return getAction(actionName, null);
    }

    public static Action getAction(String actionName, Glyph icon) {
        LOGGER.debug("Get action:" + actionName);
        if (actions.containsKey(actionName)) {
            return actions.get(actionName);
        } else {
            Action action = ActionMap.action(actionName);
            if (action == null) {
                LOGGER.warn("Not found action:" + actionName);
                throw new RuntimeException("Not found action:" + actionName);
            }
            if (icon != null) {
                action.setGraphic(icon);
            }
            actions.put(actionName, action);
            return action;
        }

    }

    public static Button createButtonWithAction(String actionName) {
        Button buttonBase = new Button();
        buttonBase.setOnAction((event) -> {
            ActionFactory.callAction(actionName);
        });
        return buttonBase;
    }
}
