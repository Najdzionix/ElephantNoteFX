package com.kn.elephant.note.utils.cache;

import java.util.Comparator;
import java.util.Optional;

import org.apache.commons.collections4.map.LRUMap;

import com.kn.elephant.note.NoteConstants;
import com.kn.elephant.note.dto.NoteDto;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 22-01-2017
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NoteCache {

	private LRUMap<Version, NoteDto> cache;

    @Getter
    private Version version;
    @Getter
    private NoteDto currentNoteDto;

    private static NoteCache noteCache = new NoteCache();

    public static NoteCache getInstance() {
        return noteCache;
    }

    private NoteCache() {
    	cache = new LRUMap<>(NoteConstants.MAX_SIZE_CACHE);
    }
    
    public void setActiveNote(NoteDto newNote) {
        if (currentNoteDto != null) {
            // save old Note ....
            version.increaseVersion();
            cache.put(version, currentNoteDto);
        }

        Optional<Version> versionOnCache = findNewestVersionOnCache(newNote.getId());
        if(versionOnCache.isPresent()) {
            version = versionOnCache.get();
            currentNoteDto = cache.get(version);
        } else {
            version = new Version(newNote.getId());
            currentNoteDto = newNote;
        }
    }

    public synchronized void contentNoteChanged(String contentNote) {
        if (contentNote != null && !contentNote.equals(currentNoteDto.getContent())) {
            version.increaseVersion();
            version.setSave(false);
            currentNoteDto.setContent(contentNote);
            cache.put(version, currentNoteDto);
        }
    }
    public void savedCurrentNote() {
        version.setSave(true);
        cache.put(version, currentNoteDto);
    }

    private Optional<Version> findNewestVersionOnCache(long noteId) {
    	return cache.keySet().stream().filter(v -> v.getNoteId() == noteId).max(Comparator.comparing(Version::getNoteId));
    }
    
    void clearCache() {
        cache.clear();
    }

}
