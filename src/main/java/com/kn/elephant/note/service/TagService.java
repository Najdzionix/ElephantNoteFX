package com.kn.elephant.note.service;

import com.kn.elephant.note.dto.TagDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil Nadłonek on 12.11.15.
 * email:kamilnadlonek@gmail.com
 */
public interface TagService {

    List<TagDto> getAll();
    List<TagDto> getTagByNoteId(Long noteId);
    boolean removeTag(Long id);
    boolean removeTagFromNote(Long tagId, Long noteId);
    Optional<TagDto> saveTag(TagDto tagDto);
    boolean isUnique(String tagName);
}
