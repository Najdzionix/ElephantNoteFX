package com.kn.elephant.note.utils;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.ui.Icons;
import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.glyphfont.Glyph;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamil Nadłonek on 17.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class ActionFactory {

    public static final String UPDATE_TITLE_ACTION_NAME = "updateTitle";
    private static Map<String, Action> actions = new HashMap<>();

    public static Action getUpdateNoteTitle() {
        return getAction(UPDATE_TITLE_ACTION_NAME);
    }

    public static Action getAddTag() {
        return getAction("addTag", Icons.SAVE_TAG);
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
        if (actions.containsKey(actionName)) {
            return actions.get(actionName);
        } else {
            Action action = ActionMap.action(actionName);
            if (icon != null) {
                action.setGraphic(icon);
            }
            actions.put(actionName, action);
            return action;
        }
    }


}
