package com.kn.elephant.note.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.model.NoteType;

/**
 * Created by Kamil Nadłonek on 21-07-2017 email:kamilnadlonek@gmail.com
 */
public class NoteServiceImpTest {

    public static final String CONTENT_NOTE = "Content...<div>Kamil</div>";
    private NoteDto noteDto;

    private NoteService noteService;

    @Before
    public void setup() {
        noteDto = new NoteDto();
        noteDto.setContent(CONTENT_NOTE);
        noteDto.setCreateAt(LocalDateTime.now());
        noteDto.setType(NoteType.HTML);
        noteDto.setShortDescription("very short");

        noteService = new NoteServiceImp();
    }

    @Test
    public void shouldSaveNote() {
        // Given

        // When
		Optional<NoteDto> savedNote = noteService.saveNote(this.noteDto);
		// Then

		NoteDto note = savedNote.get();
		Assert.assertEquals(CONTENT_NOTE, note.getContent());

    }

}