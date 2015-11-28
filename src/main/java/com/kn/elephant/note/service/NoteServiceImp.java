package com.kn.elephant.note.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.NoteTag;
import com.kn.elephant.note.model.NoteType;
import com.kn.elephant.note.model.Tag;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Kamil Nadłonek on 09.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class NoteServiceImp extends BaseService implements NoteService {

    private Dao<Note, Long> noteDao;
    private Dao<Tag, Long> tagDao;
    private Dao<NoteTag, Long> noteTagDao;

    public NoteServiceImp() {
        noteDao = dbConnection.getDao(Note.class);
        noteTagDao = dbConnection.getDao(NoteTag.class);
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
//                    dto.setParentNote(parent.get());
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
                if (noteDto.getParentNote() != null) {
                    note.setParent(noteDao.queryForId(noteDto.getParentNote().getId()));
                }
            } else {
                note = noteDao.queryForId(noteDto.getId());
            }
            note.setTitle(noteDto.getTitle());
            note.setType(noteDto.getType());
            note.setUpdateAt(LocalDateTime.now());
            note.setContent(noteDto.getContent());
            note.setShortDescription(noteDto.getShortDescription());
            if (StringUtils.isNotEmpty(note.getContent())) {
                note.setCleanContent(Jsoup.parse(noteDto.getContent()).text());
            }

            noteDao.createOrUpdate(note);

            return Optional.of(convertToNoteDto(note));

        } catch (SQLException e) {
            log.error("Data base error: ", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean removeNote(Long noteId) {
        try {
            Note note = noteDao.queryForId(noteId);
            if (note != null) {
                note.setDeleted(true);
                removeTagRelations(noteId);
                removeChildNoteRelations(noteId);
                return noteDao.update(note) == 1;
            } else {
                log.warn(String.format("No found note (id %s)", noteId));
//                todo throw custom exception ...
                return false;
            }
        } catch (SQLException e) {
            log.error("Error database: ", e);
            return false;
        }
    }

    @Override
    public List<NoteDto> getAllParentNotes() {
        try {
            List<Note> noteList = noteDao.queryBuilder().orderBy("id", false).where().eq("deleted",
                    false).and().ne("parentId", null).query();
            return noteList.stream().map(this::convertToNoteDto).collect(Collectors.toList());
        } catch (SQLException e) {
            log.error("Data base error. ", e);
            return Collections.emptyList();
        }

    }

    private void removeTagRelations(Long noteId) throws SQLException {
        DeleteBuilder<NoteTag, Long> deleteBuilder = noteTagDao.deleteBuilder();
        deleteBuilder.where().eq(NoteTag.NOTE_ID_FIELD_NAME, noteId);
        noteTagDao.delete(deleteBuilder.prepare());
    }

    private void removeChildNoteRelations(Long parentNoteId) throws SQLException {
        UpdateBuilder<Note, Long> updateBuilder = noteDao.updateBuilder();
        updateBuilder.updateColumnValue(Note.COLUMN_NAME_PARENT_ID, null);
        updateBuilder.where().eq(Note.COLUMN_NAME_PARENT_ID, parentNoteId);

        noteDao.update(updateBuilder.prepare());
    }

    private Optional<NoteDto> getParentFromList(List<NoteDto> dtos, Long id) {
        return dtos.stream().filter(note -> note.getId().equals(id)).findFirst();
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