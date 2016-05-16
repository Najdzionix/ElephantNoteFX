package com.kn.elephant.note;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by Kamil Nadłonek on 11.11.15.
 * email:kamilnadlonek@gmail.com
 */
public class NoteConstants {

    public static final String APP_VERSION = "1.0";
    public static final String DATA_BASE_URL = "jdbc:sqlite:";
    public static final String FOLDER_APP = "NoteFx";
    public static final String DB_FILE_NAME = "NoteFx.db";
    public static final String PROPERTY_FILE_NAME = "noteFx.properties";
    public static final String DB_KEY_PROPERTY = "db.location";
    public static final String VERSION_KEY_PROPERTY = "version";
    public static final String APP_DIC = FileUtils.getUserDirectoryPath() + File.separator + FOLDER_APP + File.separator;

    public static final String RED_COLOR = "#E32B2B";
    public static final String YELLOW_COLOR = "#E0E632";
    public static final String ORANGE_COLOR = " -color-secondary;";
    public static final String GRAY_DIVIDER = " -color-divider-gray;";
    public static final String BLUE_COLOR = " -color-blue;";
    public static final String WHITE = " white;";
    public static final double MIN_HEIGHT = 600.0;
    public static final double MIN_WIDTH = 1300.0;

    public static final String CSS_ACTIVE = "active";
}