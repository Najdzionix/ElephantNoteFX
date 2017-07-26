package com.kn.elephant.note.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.j256.ormlite.dao.Dao;
import com.kn.elephant.note.DBConnection;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.NoteType;

/**
 * Created by Kamil Nadłonek on 21-07-2017
 * email:kamilnadlonek@gmail.com
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DBConnection.class })
public class NoteServiceImpTest {

    private static final String CONTENT_NOTE = "Content...<div>Kamil</div>";
    private static final String SHORT_DESCRIPTION = "very short";
    private static final String TITLE = "Note testing expectedNote service";
    private NoteDto noteDto;

    private NoteServiceImp noteService;

    @Mock
    private DBConnection dbConnection;
    @Mock
    private Dao noteDao;

    @Before
    public void setup() throws Exception {
        PowerMockito.mockStatic(DBConnection.class);
        when(dbConnection.getDao(Note.class)).thenReturn(noteDao);

        when(DBConnection.getInstance()).thenReturn(dbConnection);

        noteDto = new NoteDto();
        noteDto.setContent(CONTENT_NOTE);
        noteDto.setCreateAt(LocalDateTime.now());
        noteDto.setType(NoteType.HTML);
        noteDto.setShortDescription(SHORT_DESCRIPTION);

        noteService = new NoteServiceImp();
        noteService.setDbConnection(dbConnection);
    }

    private Note createNote() {
        Note expectedNote = new Note();
        expectedNote.setContent(CONTENT_NOTE);
        expectedNote.setShortDescription(SHORT_DESCRIPTION);
        expectedNote.setTitle(TITLE);
        expectedNote.setType(NoteType.HTML);
        expectedNote.setCleanContent("Content...Kamil");
        return expectedNote;
    }

    @Test
    public void shouldSaveNote() throws SQLException {
        // Given
        Note expectedNote = createNote();
        // When
        Optional<NoteDto> savedNote = noteService.saveNote(this.noteDto);
        // Then
        NoteDto updatedNote = savedNote.get();
        assertEquals(expectedNote.getContent(), updatedNote.getContent());
        assertEquals(expectedNote.getShortDescription(), updatedNote.getShortDescription());
        assertEquals(expectedNote.getType(), updatedNote.getType());
        verify(noteDao, only()).createOrUpdate(any(Note.class));
    }

    @Test
    public void shouldOnlyTextForCleanContent() throws SQLException {
        // Given
        Note expectedNote = createNote();
        String jsonContent = "[{\"content\":\"First task\",\"id\":\"158f5924-454e-403b-950d-c05def435e97\",\"done\":false},{\"content\":\"Completed task\",\"id\":\"981d5fda-528c-40b5-8867-382330392684\",\"done\":true},{\"content\":\"Last task\",\"id\":\"889a43ea-3e6d-40b7-8730-3fb00674258e\",\"done\":false}]";
        String expectedCleanContent = "First task;Completed task;Last task";

        noteDto.setType(NoteType.TODO);
        noteDto.setContent(jsonContent);

        expectedNote.setType(NoteType.TODO);
        expectedNote.setContent(jsonContent);
        expectedNote.setCleanContent(expectedCleanContent);

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        // When
        NoteDto updatedNote = noteService.saveNote(noteDto).get();
        // Then
        assertEquals(jsonContent, updatedNote.getContent());
        assertEquals(SHORT_DESCRIPTION, updatedNote.getShortDescription());
        assertEquals(NoteType.TODO, updatedNote.getType());

        verify(noteDao, atLeastOnce()).createOrUpdate(captor.capture());
        assertEquals(expectedCleanContent, captor.getValue().getCleanContent());

    }

}