package com.kn.elephant.note.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil Nadłonek on 06.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Accessors(chain = true)
@Data
public class NoteDto {

    private Long id;
    private String title;
    private String shortDescription;
    private String content;
    private List<NoteDto> subNotes;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public NoteDto() {
        subNotes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

}