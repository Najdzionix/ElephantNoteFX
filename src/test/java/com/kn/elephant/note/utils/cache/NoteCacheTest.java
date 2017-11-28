package com.kn.elephant.note.utils.cache;

import org.junit.Assert;
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
        noteCache.setActiveNote(noteDto);
        noteCache.contentNoteChanged(expectedContent);
        String content = noteCache.getCurrentNoteDto().getContent();
        // Then
        Assert.assertEquals(expectedContent, content);
    }

    @Test
    public void shouldGetNoteFromCacheWhenLoadSecondTime() {
        // Given
        String expectedContent = "TestContent";
        // When
        noteCache.setActiveNote(noteDto);
        noteCache.contentNoteChanged(expectedContent);
        noteCache.setActiveNote(noteDto);
        String content = noteCache.getCurrentNoteDto().getContent();
        // Then
        Assert.assertEquals(expectedContent, content);
    }

}