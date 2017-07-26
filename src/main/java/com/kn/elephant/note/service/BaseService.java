package com.kn.elephant.note.service;

import com.kn.elephant.note.DBConnection;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 11.11.15.
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
abstract class BaseService {

    protected DBConnection dbConnection;

    public BaseService() {
        try {
            dbConnection = DBConnection.getInstance();
        } catch (Exception e) {
            log.error("DBConnection exception: {}", e.getMessage());
        }
    }

    protected void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
