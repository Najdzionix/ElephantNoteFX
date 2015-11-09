package com.kn.elephant.note.service;

import com.kn.elephant.note.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NoteServiceImp implements NoteService {
    @Override
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            notes.add(createTestValues());
        }

        return notes;
    }

    private Note createTestValues() {
        Note parent = new Note("Note title parent", "Short desc ....", "");
        parent.getSubNotes().add(new Note("Note title child one", "Short desc ....", ""));
        parent.getSubNotes().add(new Note("Note title child two", "Short desc ....", ""));
        return parent;
    }
}
