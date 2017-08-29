package com.kn.elephant.note.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.j256.ormlite.dao.Dao;
import com.kn.elephant.note.DBConnection;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.NoteTag;
import com.kn.elephant.note.model.NoteType;
import com.kn.elephant.note.model.Tag;
import com.kn.elephant.note.model.Event;

import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 29.10.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class InsertDataService {
    private static final String NOTE_TODO_CONTENT = "[{\"content\":\"First task\",\"id\":\"158f5924-454e-403b-950d-c05def435e97\",\"done\":false},{\"content\":\"Completed task\",\"id\":\"981d5fda-528c-40b5-8867-382330392684\",\"done\":true},{\"content\":\"Last task\",\"id\":\"889a43ea-3e6d-40b7-8730-3fb00674258e\",\"done\":false}]";
    private Dao<Note, Long> noteDao;
    private Dao<Tag, Long> tagDao;
    private Dao<Event, Long> eventDao;
    private Dao<NoteTag, Long> noteTagDao;

    public InsertDataService(DBConnection dbConnection) {
        noteDao = dbConnection.getDao(Note.class);
        tagDao = dbConnection.getDao(Tag.class);
        noteTagDao = dbConnection.getDao(NoteTag.class);
        eventDao = dbConnection.getDao(Event.class);
    }


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
        Note todoNote = createTestNote(noteNumber, "Todo note");
        todoNote.setType(NoteType.TODO);
        todoNote.setContent(NOTE_TODO_CONTENT);
        saveNote(todoNote);
        Event testEvent = createTestEvent();
        try {
            eventDao.createOrUpdate(testEvent);
        } catch (SQLException e) {
            log.error(e);
        }

    }

    private List<Tag> createTagsList() {
        List<String> tagNames = Arrays.asList("car", "home", "tip", "important", "javaFx");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
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
        for (Tag tag : tags) {
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
        note.setTitle(title + noteNumber);
        note.setShortDescription("Short descr ... note " + noteNumber);
        note.setContent("Content ..... note" + noteNumber);
        note.setType(NoteType.HTML);
        note.setDeleted(false);

        return note;
    }

    private Event createTestEvent() {
        Event event = new Event();
        event.setName("Example Event");
        event.setStartDate(LocalDateTime.now().plusMinutes(1));
        event.setDeleted(false);
        return event;
    }

}
