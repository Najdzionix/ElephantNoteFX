package com.kn.elephant.note.utils.cache;

import java.util.Comparator;
import java.util.Optional;

import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.dto.NoteDto;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 22-01-2017 email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NoteCache {

    private static final SimpleMapCache<Version, NoteDto> cache = new SimpleMapCache<>(6000L, NoteConstants.MAX_SIZE_CACHE);

    @Getter
    private Version version;
    @Getter
    private NoteDto currentNoteDto;

    private static NoteCache noteCache = new NoteCache();

    public static NoteCache getInstance() {
        return noteCache;
    }

    private NoteCache() {
    }

    public void loadNote(NoteDto newNote) {
        if (currentNoteDto != null) {
            // save old Note ....
            version.increaseVersion();
            cache.put(version, currentNoteDto);
        }

        Optional<Version> versionOnCache = findNewestVersionOnCache(newNote.getId());
        version = versionOnCache.orElseGet(() -> new Version(newNote.getId()));
        currentNoteDto = newNote;
    }

    public void noteChanged(NoteDto noteDto) {
        if (noteDto.getId().equals(currentNoteDto.getId())) {
            version.increaseVersion();
            currentNoteDto = noteDto;
            cache.put(version, currentNoteDto);
        } else {
            log.error("Something gone wrong!!!!");
        }
    }

    public void noteChanged(String contentNote) {
        if (!currentNoteDto.getContent().equals(contentNote)) {
            version.increaseVersion();
            currentNoteDto.setContent(contentNote);
            cache.put(version, currentNoteDto);
        }
    }

    private Optional<Version> findNewestVersionOnCache(long noteId) {
        return cache.getKeys().stream().filter(v -> v.getNoteId() == noteId).max(Comparator.comparing(Version::getVersion));
    }

}