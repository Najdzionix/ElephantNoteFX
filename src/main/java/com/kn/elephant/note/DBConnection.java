package com.kn.elephant.note;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.NoteTag;
import com.kn.elephant.note.model.Tag;
import com.kn.elephant.note.model.Event;
import com.kn.elephant.note.service.InsertDataService;
import com.kn.elephant.note.utils.Utils;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kamil Nadłonek on 11.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class DBConnection {

    private static final String SQL_VERIFY_STRUCTURE_DB = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name IN ('note', 'tag', 'note_tag')";
    private static final long EXPECTED_AMOUNT_TABLE = 3L;
    private static DBConnection instance;
    private ConnectionSource connectionSource = null;
    private Map<Class, Dao> daoMap;
    private List<Class> listModelClass = Arrays.asList(Note.class, Tag.class, NoteTag.class, Event.class);

    private DBConnection() throws Exception {
        String urlDB = NoteConstants.DATA_BASE_URL + Utils.getProperty(NoteConstants.DB_KEY_PROPERTY);
        log.info("Initialize connection to DB {}", urlDB);
        // create our data-source for the database
        connectionSource = new JdbcConnectionSource(urlDB);
        // setup our database and DAOs
        verifyDatabase();

    }

    public static DBConnection getInstance() throws Exception {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public void closeConnection() {
        log.info("Close connection for data base.");
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (IOException e) {
                log.warn("Error during close connection to DB, {}", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * If table not exists than create table.
     */
    private void verifyDatabase() throws Exception {
        long amountTable = connectionSource.getReadOnlyConnection("sqlite_master").queryForLong(SQL_VERIFY_STRUCTURE_DB);
        if (EXPECTED_AMOUNT_TABLE != amountTable) {
            createTableIfNotExists(connectionSource);
            loadDao();
            InsertDataService insertDataService = new InsertDataService(this);
            insertDataService.insertExampleData();
        } else {
            loadDao();
        }
    }

    private void loadDao() {
        log.debug("Load Dao");
        daoMap = new HashMap<>();
        listModelClass.stream().forEach((Class clazz) -> {
            Dao dao = null;
            try {
                dao = DaoManager.createDao(connectionSource, clazz);
            } catch (SQLException e) {
                log.warn("Failed create Dao class for {}", clazz);
            }
            if (dao != null) {
                daoMap.put(clazz, dao);
            }
        });
    }

    public <D extends Dao<T, ?>, T> Dao getDao(Class<T> clazz) {
        return daoMap.get(clazz);
    }

    private void createTableIfNotExists(ConnectionSource source) throws Exception {
        log.debug("Create tables");
        listModelClass.stream().forEach(cl -> {
            try {
                TableUtils.createTableIfNotExists(source, cl);
            } catch (SQLException e) {
                log.error("Can not create table for class {}", cl);
            }
        });
    }
}