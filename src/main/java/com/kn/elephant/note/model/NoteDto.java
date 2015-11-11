package com.kn.elephant.note.model;

import java.util.List;

/**
 * Created by Kamil Nadłonek on 06.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NoteDto {

    private Long id;
    private String title;
    private String shortDescription;
    private String content;
    private List<NoteDto> subNotes;


    public Long getId() {
        return id;
    }

    public NoteDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NoteDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public NoteDto setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NoteDto setContent(String content) {
        this.content = content;
        return this;
    }

    public List<NoteDto> getSubNotes() {
        return subNotes;
    }

    public NoteDto setSubNotes(List<NoteDto> subNotes) {
        this.subNotes = subNotes;
        return this;
    }
}