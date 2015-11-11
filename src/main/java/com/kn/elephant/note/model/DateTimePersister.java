package com.kn.elephant.note.model;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by Kamil Nadłonek on 11.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class DateTimePersister extends DateType {

    private static final DateTimePersister SINGLETION = new DateTimePersister();

    private DateTimePersister() {
        super(SqlType.DATE, new Class<?>[]{LocalDate.class});
    }

    public static DateTimePersister getSingleton() {
        return SINGLETION;
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        Timestamp timestamp = results.getTimestamp(columnPos);
        if (timestamp == null) {
            return null;
        } else {
            return timestamp.getTime();
        }
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        if (sqlArg == null) {
            return null;
        } else {
            Long value = (Long) sqlArg;
            Instant instant = Instant.ofEpochMilli(value);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }


    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        LocalDateTime date = (LocalDateTime) javaObject;
        return Timestamp.valueOf(date);
    }


}
