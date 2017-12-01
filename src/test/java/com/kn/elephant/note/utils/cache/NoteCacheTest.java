package com.kn.elephant.note.utils.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.kn.elephant.note.dto.NoteDto;

/**
 * Created by Kamil Nadłonek on 28-11-2017
 * email:kamilnadlonek@gmail.com
 */
public class NoteCacheTest {

    private NoteCache noteCache;

    private NoteDto noteDto;

    @Before
    public void setup() {
        noteCache = NoteCache.getInstance();
        noteCache.clearCache();
        noteDto = new NoteDto().setContent("initContent").setId(1L);
    }

    @Test
    public void shouldReturnFromCacheWithUpdatedContent() {
        // Given
        String expectedContent = "testContent";
        // When
        noteCache.changeNote(noteDto);
        noteCache.contentNoteChanged(expectedContent);
        String content = noteCache.getCurrentNoteDto().getContent();
        // Then
        assertEquals(expectedContent, content);
    }

    @Test
    public void shouldGetNoteFromCacheWhenLoadSecondTime() {
        // Given
        String expectedContent = "TestContent";
        // When
        noteCache.changeNote(noteDto);
        noteCache.contentNoteChanged(expectedContent);
        noteCache.changeNote(noteDto);
        String content = noteCache.getCurrentNoteDto().getContent();
        // Then
        assertEquals(expectedContent, content);
    }

    @Test
    public void shouldNotDuplicateVersions() {
    	// Given
        NoteDto secondNote = new NoteDto().setId(2L).setContent("Content 2");
        String expectedContent = "Content One changed...";

        // When
        noteCache.changeNote(noteDto);
        noteCache.contentNoteChanged(expectedContent);
        noteCache.changeNote(secondNote);
        noteCache.changeNote(noteDto);

    	// Then
        NoteDto currentNoteDto = noteCache.getCurrentNoteDto();
        assertEquals(expectedContent, currentNoteDto.getContent());
        assertEquals(2, noteCache.getVersion().getVersion());

    }

}