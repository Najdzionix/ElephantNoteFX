package com.kn.elephant.note.service;

import java.util.List;
import java.util.Optional;

import com.kn.elephant.note.dto.NoteDto;

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

    /**
     *
     * @return only notes which can be parents
     */
    List<NoteDto> getAllParentNotes();

    /**
     *  Check if provide title is unique, if not return false, otherwise return true
     * @param title
     * @return
     */
    boolean isTitleNoteUnique(String title);

	boolean isTitleNoteUnique(String title, Long expectNoteId);

	/**
     * Looking for notes which contains sequence given by user
     * @param pattern
     * @return List of notes which matched to pattern
     */
    List<NoteDto> findNotes(String pattern);

    /**
     * Return all notes assign to tagId
     * @param tagId
     * @return List of notes belongs to given tagId
     */
    List<NoteDto> getNotesByTagId(Long tagId);

    /**
     * If given childNote has parent than it is return
     * @param childNoteId
     * @return Note which is parent for given child note id
     */
    Optional<NoteDto> getParentNote(Long childNoteId);
}
