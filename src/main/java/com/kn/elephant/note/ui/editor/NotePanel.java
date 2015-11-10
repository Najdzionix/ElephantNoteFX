package com.kn.elephant.note.ui.editor;

import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.ui.BasePanel;

/**
 * Created by Kamil Nadłonek on 10.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NotePanel extends BasePanel {

    private Note note;

    public NotePanel(Note note) {
        this.note = note;
        setTop(new DetailsNotePanel(note));
        //editor

    }
}
