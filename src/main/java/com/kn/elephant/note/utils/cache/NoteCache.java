package com.kn.elephant.note.utils.cache;

import java.util.Comparator;
import java.util.Map;
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
    // TODO: 01/12/17 improve cache because lose information about changes in note (for example content
    // - memory references)
    public void changeNote(NoteDto newNote) {
        if (currentNoteDto != null) {
            // save previous one
            addMapCache();
        }
        Optional<Map.Entry<Version, NoteDto>> noteCache = findNewestVersionOnCache(newNote.getId());
        if (noteCache.isPresent()) {
            currentNoteDto = noteCache.get().getValue();
            version = noteCache.get().getKey();
            version = version.increaseVersion();
        } else {
            currentNoteDto = newNote;
            version = new Version(newNote.getId());
        }
    }

    public synchronized void contentNoteChanged(String contentNote) {
        if (contentNote != null && !contentNote.equals(currentNoteDto.getContent())) {
            version.setSave(false);
            currentNoteDto.setContent(contentNote);
            addMapCache();
        }
    }
    public void savedCurrentNote() {
        version.setSave(true);
        addMapCache();
    }

    private void addMapCache() {
        if (cache.containsKey(version) || cache.containsValue(currentNoteDto)) {
            return;
        }

        cache.put(version, currentNoteDto);
        version = version.increaseVersion();
    }

    private Optional<Map.Entry<Version, NoteDto>> findNewestVersionOnCache(long noteId) {
        return cache.entrySet().stream()
            .filter(entry -> entry.getKey().getNoteId() == noteId)
            .max(Comparator.comparing(entry -> entry.getKey().getVersion()));
    }
    
    void clearCache() {
        cache.clear();
        version = null;
        currentNoteDto = null;
    }

}
