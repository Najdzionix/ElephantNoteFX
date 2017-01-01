package com.kn.elephant.note;

import java.io.File;

import org.apache.commons.io.FileUtils;

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

    public static final int MAX_NUMBER_OF_COLUMNS_AND_ROWS = 100;



//    private static final String TABLE_CSS = "<style> .pure-table{border-collapse:collapse;border-spacing:0;empty-cells:show;border:1px solid #cbcbcb}.pure-table caption{color:#000;font:italic 85%/1 arial,sans-serif;padding:1em 0;text-align:center}.pure-table td,.pure-table th{border-left:1px solid #cbcbcb;border-width:0 0 0 1px;font-size:inherit;margin:0;overflow:visible;padding:.5em 1em}.pure-table td:first-child,.pure-table th:first-child{border-left-width:0}.pure-table thead{background-color:#e0e0e0;color:#000;text-align:left;vertical-align:bottom}.pure-table td{background-color:transparent}.pure-table-odd td{background-color:#f2f2f2}.pure-table-striped tr:nth-child(2n-1) td{background-color:#f2f2f2}.pure-table-bordered td{border-bottom:1px solid #cbcbcb}.pure-table-bordered tbody>tr:last-child>td{border-bottom-width:0}.pure-table-horizontal td,.pure-table-horizontal th{border-width:0 0 1px;border-bottom:1px solid #cbcbcb}.pure-table-horizontal tbody>tr:last-child>td{border-bottom-width:0}</style>";
    private static final String TABLE_CSS = "<style>.pure-table {border-collapse: collapse;border-spacing: 0;empty-cells: show;border: 1px solid #cbcbcb}.pure-table td,.pure-table th {border-left: 1px solid #cbcbcb;border-width: 0 0 0 1px;font-size: inherit;margin: 0;overflow: visible;padding: .5em 1em}.pure-table td:first-child,.pure-table th:first-child {border-left-width: 0}.pure-table thead {background-color: #e0e0e0;color: #000;text-align: left;vertical-align: bottompadding: 1em 0;font-weight: bold;}.pure-table td {background-color: transparent}.pure-table-striped tbody tr:nth-child(2n-1) td {background-color:#f2f4d4}.pure-table-horizontal td,.pure-table-horizontal th {border-width: 0 0 1px;border: 1px solid #cbcbcb}.pure-table-horizontal tbody>tr:last-child>td {border-bottom-width: 0}</style>";

    public static String INIT_NOTE_CONTENT = "<!DOCTYPE html> <html> <head>"  + TABLE_CSS + "</head> <body>  </body> </html>";

}