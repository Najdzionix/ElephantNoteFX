package com.kn.elephant.note.ui.editor;

import com.kn.elephant.note.model.NoteDto;
import com.kn.elephant.note.ui.BasePanel;
import javafx.geometry.Insets;
import javafx.scene.web.HTMLEditor;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NotePanel extends BasePanel {

    private NoteDto noteDto;

    public NotePanel(NoteDto noteDto) {
        this.noteDto = noteDto;
        setTop(new DetailsNotePanel(noteDto));
        //editor
        setCenter(new HTMLEditor());
        setPadding(new Insets(5));

    }
}
