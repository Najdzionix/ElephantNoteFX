package com.kn.elephant.note.service;

import com.j256.ormlite.dao.Dao;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.NoteType;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NoteServiceImp extends BaseService implements NoteService {

    private Dao<Note, Long> noteDao;

    public NoteServiceImp() {
        noteDao = dbConnection.getDao(Note.class);
    }

    @Override
    public List<NoteDto> getAllNotes() {
        List<NoteDto> notesDto = new ArrayList<>();
        List<Note> notes = new ArrayList<>(getNotes());
        log.debug(String.format("Found %s notes.", notes.size()));

        for (Note note : notes) {
            NoteDto dto = convertToNoteDto(note);
            if (note.getParent() != null) {
                Optional<NoteDto> parent = getParentFromList(notesDto, note.getParent().getId());
                if (parent.isPresent()) {
                    parent.get().getSubNotes().add(dto);
                }

            } else {
                notesDto.add(dto);
            }
        }

        return notesDto;
    }

    /**
     * No return child notes !!!
     *
     * @param note
     * @return Note without children
     */
    private NoteDto convertToNoteDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getId()).setTitle(note.getTitle())
                .setShortDescription(note.getShortDescription())
                .setContent(note.getContent())
                .setCreateAt(note.getCreateAt())
                .setUpdateAt(note.getUpdateAt())
                .setType(note.getType());
        return dto;
    }

    @Override
    public Optional<NoteDto> saveNote(NoteDto noteDto) {
        Note note;
        try {
            if (noteDto.getId() == null) {
                note = new Note();
                note.setCreateAt(LocalDateTime.now());
                note.setType(NoteType.HTML);
            } else {
                note = noteDao.queryForId(noteDto.getId());
            }
            note.setTitle(noteDto.getTitle());
            note.setType(noteDto.getType());
            note.setUpdateAt(LocalDateTime.now());
            note.setContent(noteDto.getContent());
            note.setCleanContent(Jsoup.parse(noteDto.getContent()).text());
            noteDao.createOrUpdate(note);

            return Optional.of(convertToNoteDto(note));

        } catch (SQLException e) {
            log.error("Data base error: ", e);
        }

        return Optional.empty();
    }

    private Optional<NoteDto> getParentFromList(List<NoteDto> dtos, Long id) {
        return dtos.stream().filter(note -> note.getId() == id).findFirst();
    }

    private List<Note> getNotes() {
        try {
            return noteDao.queryBuilder().orderBy("parentId", true).orderBy("id", false).where().eq("deleted", false).query();
        } catch (SQLException e) {
            log.error("Data base error. ", e);
            return Collections.emptyList();
        }
    }

}