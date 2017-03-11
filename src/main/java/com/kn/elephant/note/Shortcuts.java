package com.kn.elephant.note;

import org.controlsfx.control.action.ActionMap;

import com.kn.elephant.note.utils.ActionFactory;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 11-03-2017 email:kamilnadlonek@gmail.com
 */
@Log4j2
public class Shortcuts {

    private final Scene scene;

    public Shortcuts(Scene scene) {
        this.scene = scene;
        ActionMap.register(this);
        saveNote();
        newNoteDialog();
        search();
    }

    private void saveNote() {
        final KeyCombination saveKeyComb = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        addEventHandler(saveKeyComb, "saveNote");
    }

    private void newNoteDialog() {
        final KeyCombination newNoteKeyComb = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        addEventHandler(newNoteKeyComb, "addNoteDialog");
    }

    private void search() {
        final KeyCombination searchKeyComb = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        addEventHandler(searchKeyComb, "setSearchFocus");
    }

    private void addEventHandler(KeyCombination newNoteKeyComb, String actionName) {
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (newNoteKeyComb.match(event)) {
                ActionFactory.callAction(actionName);
            }
        });
    }
}
