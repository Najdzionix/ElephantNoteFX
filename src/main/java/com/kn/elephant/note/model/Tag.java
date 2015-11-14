package com.kn.elephant.note.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Kamil Nadłonek on 11.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Data
@DatabaseTable(tableName = "tag")
public class Tag {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "createAt", persisterClass = DateTimePersister.class)
    private LocalDateTime createAt;

    @DatabaseField(columnName = "updateAt", persisterClass = DateTimePersister.class)
    private LocalDateTime updateAt;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "deleted")
    private boolean deleted;

    public Tag(){};
}
