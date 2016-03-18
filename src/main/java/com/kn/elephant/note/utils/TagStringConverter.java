package com.kn.elephant.note.utils;

import com.kn.elephant.note.dto.TagDto;
import javafx.util.StringConverter;

/**
 * Created by Kamil Nadłonek on 18.03.16.
 * email:kamilnadlonek@gmail.com
 */
public class TagStringConverter extends StringConverter<TagDto> {
    @Override
    public String toString(TagDto tagDto) {
        return tagDto.getName();
    }

    @Override
    public TagDto fromString(String tagName) {
        return new TagDto().setName(tagName);
    }
}
