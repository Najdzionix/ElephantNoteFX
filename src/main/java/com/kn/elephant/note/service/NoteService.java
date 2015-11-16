package com.kn.elephant.note.service;

import com.kn.elephant.note.dto.NoteDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public interface NoteService {

    List<NoteDto> getAllNotes();
    Optional<NoteDto> saveNote(NoteDto noteDto);
}
