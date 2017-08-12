package com.kn.elephant.note.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.kn.elephant.note.dto.NoteDto;
import com.kn.elephant.note.dto.TagDto;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.NoteTag;
import com.kn.elephant.note.model.Tag;

import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 12.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class TagServiceImp extends BaseService implements TagService {

    private static final String COMMA = ", ";
    private static final String OPEN_PARENTHESIS = " (";
    private static final String CLOSE_PARENTHESIS = ") ";
    private Dao<Note, Long> noteDao;
    private Dao<Tag, Long> tagDao;
    private Dao<NoteTag, Long> noteTagDao;

    private PreparedQuery<Tag> tagByNoteIdQuery = null;

    @Inject
    private NoteService noteService;

    public TagServiceImp() {
        noteDao = dbConnection.getDao(Note.class);
        noteTagDao = dbConnection.getDao(NoteTag.class);
        tagDao = dbConnection.getDao(Tag.class);
    }

    @Override
    public List<TagDto> getAll() {
        return convertTagToDto(getTags());
    }

    @Override
    public boolean removeTagFromNote(Long tagId, Long noteId) {
        try {
            DeleteBuilder<NoteTag, Long> deleteBuilder = noteTagDao.deleteBuilder();
            deleteBuilder.where().eq(NoteTag.NOTE_ID_FIELD_NAME, noteId)
                    .and()
                    .eq(NoteTag.TAG_ID_FIELD_NAME, tagId);

            return noteTagDao.delete(deleteBuilder.prepare()) == 1;
        } catch (SQLException e) {
            log.error("Data base error. ", e);
            return false;
        }
    }

    private List<TagDto> convertTagToDto(List<Tag> tags) {
        List<TagDto> results = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto dto = convertToTagDto(tag);
            results.add(dto);
        }
        return results;
    }

    private TagDto convertToTagDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setCreateAt(tag.getCreateAt()).setUpdateAt(tag.getCreateAt()).setId(tag.getId())
                .setName(tag.getName());
            dto.setNotes(noteService.getNotesByTagId(tag.getId())); //get notes from note service ....
        return dto;
    }

    private List<Tag> getTags() {
        try {
            return tagDao.queryBuilder().orderBy("id", false).where().eq("deleted", false)
                    .query();
        } catch (SQLException e) {
            log.error("Data base error. ", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<TagDto> getTagByNoteId(Long noteId) {
        try {
            if (tagByNoteIdQuery == null) {
                tagByNoteIdQuery = makeQueryForNoteTags();
            }
            tagByNoteIdQuery.setArgumentHolderValue(0, noteDao.queryForId(noteId));
            List<Tag> tags = tagDao.query(tagByNoteIdQuery);

            return convertTagToDto(tags);
        } catch (SQLException e) {
            log.error("Data base error:", e);
            return Collections.emptyList();
        }
    }

    private PreparedQuery<Tag> makeQueryForNoteTags() throws SQLException {
        QueryBuilder<NoteTag, Long> noteTagQB = noteTagDao.queryBuilder();
        noteTagQB.selectColumns(NoteTag.TAG_ID_FIELD_NAME);
        SelectArg noteSelectArg = new SelectArg();
        noteTagQB.where().eq(NoteTag.NOTE_ID_FIELD_NAME, noteSelectArg);

        return tagDao.queryBuilder().where().in("id", noteTagQB).prepare();
    }

    @Override
    public boolean removeTag(Long id) {
        try {
            Tag tag = tagDao.queryForId(id);
            deletedAllRelationshipsForTag(id);
            tag.setDeleted(true);
            return tagDao.createOrUpdate(tag).isUpdated();
        } catch (SQLException e) {
            log.error("Data base error:", e);
            return false;
        }
    }

    private void deletedAllRelationshipsForTag(Long tagId) {
        try {
            DeleteBuilder<NoteTag, Long> deleteBuilder = noteTagDao.deleteBuilder();
            PreparedQuery<NoteTag> prepare = deleteBuilder.where().in(NoteTag.TAG_ID_FIELD_NAME, tagId).prepare();
            int query = noteTagDao.delete((PreparedDelete<NoteTag>) prepare);
            log.debug("Deleted " + query  + " relations between Note and Tag, for tagId:" + tagId);
        } catch (SQLException e) {
            log.error("Can not deleted relations Note->Tag, data base error:", e);
        }
    }

    @Override
    public Optional<TagDto> saveTag(TagDto tagDto) {
        try {
            Tag tag;
            if (tagDto.getCreateAt() == null) {
                tag = new Tag();
                tag.setCreateAt(LocalDateTime.now());
            } else {
                tag = tagDao.queryForId(tagDto.getId());
            }
            tag.setName(tagDto.getName());
            tag.setUpdateAt(LocalDateTime.now());
            tag.setDeleted(false);
            Dao.CreateOrUpdateStatus status = tagDao.createOrUpdate(tag);
            saveTagNoteRelation(tag.getId(), tagDto.getNotes());
            if (status.isUpdated() || status.isCreated()) {
                return Optional.of(convertToTagDto(tag));
            }

        } catch (SQLException e) {
            log.error("Error data base: ", e);
        }

        return Optional.empty();
    }

    private void saveTagNoteRelation(Long tagId, List<NoteDto> notes) throws SQLException {
        String insertValues = notes.stream().map(note -> OPEN_PARENTHESIS + note.getId() + COMMA + tagId + CLOSE_PARENTHESIS).collect(Collectors.joining(","));
        log.debug("Insert values: " + insertValues);
        StringBuilder sql = new StringBuilder(" INSERT INTO ");
        sql.append(NoteTag.TAG_NOTE_TABLE_NAME)
                .append(OPEN_PARENTHESIS).append(NoteTag.NOTE_ID_FIELD_NAME).append(COMMA)
                .append(NoteTag.TAG_ID_FIELD_NAME).append(" ) VALUES ")
                .append(insertValues)
                .append(" ;");

        noteTagDao.updateRaw(sql.toString());
    }

    @Override
    public boolean isUnique(String tagName) {
        try {
            List<Tag> tags = tagDao.queryBuilder().where().eq("name", tagName).query();
            return tags.isEmpty();
        } catch (SQLException e) {
            log.error("Sql exception: " + e.getMessage(), e);
            return true;
        }
    }
}
