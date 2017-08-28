package com.kn.elephant.note.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.kn.elephant.note.NoteConstants;

import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 17-04-2016
 * email:kamilnadlonek@gmail.com
 */
@Log4j2
public class Utils {

    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");

    public static File createFile(String pathToFile) {
        try {
            File file = FileUtils.getFile(pathToFile);
            if (!file.exists()) {
                if (StringUtils.endsWith(pathToFile, File.separator)) {
                    file.mkdir();
                } else {
                    file.createNewFile();
                }
            }
            return file;
        } catch (IOException e) {
            log.error("Can not create file:" + pathToFile, e);
            throw new IllegalStateException("Can not create file:" + pathToFile);
        }
    }

    public static boolean existsFile(String path) {
        return FileUtils.getFile(path).exists();
    }

    public static String getProperty(String propertyName) {
        File property = createFile(NoteConstants.APP_DIC + NoteConstants.PROPERTY_FILE_NAME);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(property));
        } catch (IOException e) {
            log.error("Can not find property file", e);
        }

        return properties.getProperty(propertyName);
    }
}
