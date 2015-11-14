package com.kn.elephant.note.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil Nadłonek on 11.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Accessors(chain = true)
@Data
public class TagDto {

    private Long id;
    private String name;
    private List<NoteDto> notes;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public TagDto() {
        notes = new ArrayList<>();
    }
}
