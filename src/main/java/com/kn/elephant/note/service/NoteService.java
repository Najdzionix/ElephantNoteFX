package com.kn.elephant.note.service;

import com.kn.elephant.note.model.NoteDto;

import java.util.List;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
public interface NoteService {

    List<NoteDto> getAllNotes();
}
