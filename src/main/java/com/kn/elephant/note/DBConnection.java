package com.kn.elephant.note;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kn.elephant.note.model.Note;
import com.kn.elephant.note.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kamil Nadłonek on 11.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class DBConnection {

    private static Logger LOGGER = LogManager.getLogger(DBConnection.class);

    private static DBConnection instance;
    ConnectionSource connectionSource = null;
    Map<Class, Dao> daoMap;
    List<Class> listModelClass = Arrays.asList(Note.class, Tag.class);

    private DBConnection() throws Exception {
        LOGGER.info("Initialize connection to DB {}", NoteConstants.DATA_BASE_URL);
        // create our data-source for the database
        connectionSource = new JdbcConnectionSource(NoteConstants.DATA_BASE_URL);
        // setup our database and DAOs
        setupDatabase(NoteConstants.CREATE_DATA_BASE);
        loadDao();
    }

    public static DBConnection getInstance() throws Exception {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public void closeConnection() {
        LOGGER.info("Close connection for data base.");
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (SQLException e) {
                LOGGER.warn("Error during close conneciton to DB, {}", e);
            }
        }
    }

    /**
     * Setup our database
     */
    private void setupDatabase(boolean createNewDB) throws Exception {
        if (createNewDB) {
            dropTables(connectionSource);
            createTables(connectionSource);
        }
    }

    private void loadDao() {
        LOGGER.debug("Load Dao");
        daoMap = new HashMap<>();
        listModelClass.stream().forEach(clazz -> {
            Dao dao = null;
            try {
                dao = DaoManager.createDao(connectionSource, clazz);
            } catch (SQLException e) {
                LOGGER.warn("Failed create Dao class for {}", clazz);
            }
            if (dao != null) {
                daoMap.put(clazz, dao);
            }
        });
    }

    public <D extends Dao<T, ?>, T> Dao getDao(Class<T> clazz) {
        return daoMap.get(clazz);
    }


    private void dropTables(ConnectionSource source) throws Exception {
        LOGGER.debug("Drop tables");
        listModelClass.stream().forEach(cl -> {
            try {
                TableUtils.dropTable(source, cl, true);
            } catch (SQLException e) {
                LOGGER.warn("Failed drop table for class {}", cl);
            }
        });
    }

    private void createTables(ConnectionSource source) throws Exception {
        LOGGER.debug("Create tables");
        listModelClass.stream().forEach(cl -> {
            try {
                TableUtils.createTable(source, cl);
            } catch (SQLException e) {
                LOGGER.error("Can not create table for class {}", cl);
            }
        });
    }
}