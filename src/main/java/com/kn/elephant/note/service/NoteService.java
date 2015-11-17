package com.kn.elephant.note.service;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.model.Note;

import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public interface NoteService {

    List<NoteDto> getAllNotes();

    Optional<NoteDto> saveNote(NoteDto noteDto);

    /**
     * Mark note as deleted (set deleted flag on true )
     * @param noteId
     * @return Return true when note marked as deleted, otherwise false
     */
    boolean removeNote(Long noteId);
}
