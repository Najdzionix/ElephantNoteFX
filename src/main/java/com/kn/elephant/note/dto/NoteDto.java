package com.kn.elephant.note.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kn.elephant.note.model.NoteType;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by Kamil Nadłonek on 06.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Accessors(chain = true)
@Data
@ToString(exclude = {"content", "shortDescription", "subNotes"})
public class NoteDto {

    private Long id;
    private String title;
    private String shortDescription;
    private String content;
    private NoteType type;
    private List<NoteDto> subNotes;
    private NoteDto parentNote;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public NoteDto() {
        subNotes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

}