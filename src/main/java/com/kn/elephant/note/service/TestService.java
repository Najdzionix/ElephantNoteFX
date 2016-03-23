package com.kn.elephant.note.service;

import com.j256.ormlite.dao.Dao;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.NoteTag;
import com.kn.elephant.note.model.NoteType;
import com.kn.elephant.note.model.Tag;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class TestService extends BaseService implements Test {
    private Dao<Note, Long> noteDao;
    private Dao<Tag, Long> tagDao;
    private Dao<NoteTag, Long> noteTagDao;

    public TestService() {
        super();
        try {
            noteDao = dbConnection.getDao(Note.class);
            tagDao = dbConnection.getDao(Tag.class);
            noteTagDao = dbConnection.getDao(NoteTag.class);
        } catch (Exception e) {
            log.error("DBConnection exception: {}", e.getMessage());
        }
    }

    @Override
    public void hello() {
        try {
            log.info("Hello test service.:)" + getNote(1L));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Note getNote(Long noteId) throws SQLException{
        if (noteDao.idExists(noteId)) {
            return noteDao.queryForId(noteId);
        } else {
            log.warn("Day does exist, by id {}", noteId);
            return null;
        }
    }

    @Override
    public void insertExampleData() {
        log.info("Load example data to DB");
        int noteNumber = 1;
        //create parent notes
        List<Tag> tags = createTagsList();
        for (int i = 10; i > 0; i--) {
            Note note = createTestNote(noteNumber, "Parent note:");
            saveNote(note);
            setTagForNote(note, tags);
            createChildrenNotes(note);
            noteNumber++;
        }
    }

    private List<Tag> createTagsList(){
        List<String> tagNames = Arrays.asList("car", "home", "tip", "important", "javaFx");
        List<Tag> tags = new ArrayList<>();
        for(String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setUpdateAt(LocalDateTime.now());
            tag.setCreateAt(LocalDateTime.now());
            tag.setName(tagName);
            try {
                tagDao.create(tag);
                tags.add(tag);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return tags;
    }

    private void setTagForNote(Note note, List<Tag> tags) {
        for(Tag tag : tags) {
            NoteTag noteTag = new NoteTag(note, tag);
            try {
                noteTagDao.create(noteTag);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createChildrenNotes(Note parent) {
        int i = new Random().nextInt(5);
        log.info("Child i " + i);
        for (; i > 0; i--) {
            Note child = createTestNote(i, "Child note:");
            child.setParent(parent);
            saveNote(child);
        }

    }

    private void saveNote(Note note) {
        try {
            noteDao.create(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Note createTestNote(int noteNumber, String title) {
        Note note = new Note();
        LocalDateTime nowDate = LocalDateTime.now();
        note.setCreateAt(nowDate);
        note.setUpdateAt(nowDate);
        note.setTitle( title + noteNumber);
        note.setShortDescription("Short descr ... note " + noteNumber);
        note.setContent("Content ..... note" + noteNumber);
        note.setType(NoteType.HTML);
        note.setDeleted(false);

        return note;
    }

}
