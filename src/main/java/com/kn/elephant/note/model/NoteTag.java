package com.kn.elephant.note.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.kn.elephant.note.DBConnection;
import lombok.Data;

/**
 * Created by Kamil Nadłonek on 12.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Data
@DatabaseTable(tableName = "notetag")
public class NoteTag {
    public final static String NOTE_ID_FIELD_NAME = "noteId";
    public final static String TAG_ID_FIELD_NAME = "tagId";
    public final static String TAG_NOTE_TABLE_NAME = "notetag";

    @DatabaseField(generatedId = true)
    private int id;

    // This is a foreign object which just stores the id from the User object in this table.
    @DatabaseField(foreign = true, columnName = NOTE_ID_FIELD_NAME)
    private Note note;

    // This is a foreign object which just stores the id from the Post object in this table.
    @DatabaseField(foreign = true, columnName = TAG_ID_FIELD_NAME)
    private Tag tag;

    public NoteTag(){}

    public NoteTag(Note note, Tag tag){
        this.note = note;
        this.tag = tag;
    }

}
