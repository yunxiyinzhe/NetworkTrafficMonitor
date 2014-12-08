package com.dylangao.networktrafficmonitor.database;

import android.net.Uri;

public class DataBaseConstants {
    public static final String DB_FILE_NAME = "networktraffic.db";
    public static final String AUTOHORITY = "com.dylan.networktraffic";
    public static final String NETWORK_TRAFFIC_CONFIG_TABLE_NAME = "network_traffic_config";
    public static final String NETWORK_TRAFFIC_FOR_DAY_TABLE_NAME = "network_traffic_day";
    public static final String NETWORK_TRAFFIC_FOR_MONTH_TABLE_NAME = "network_traffic_month";
    public static final String COLUMNS_ID = "id";
    public static final String COLUMNS_NAME = "name";
    public static final String COLUMNS_VALUE = "value";
    public static final String COLUMNS_TYPE = "type";
    public static final String COLUMNS_MOBILE = "mobile";
    public static final String COLUMNS_TOTAL = "total";
    public static final String COLUMNS_MOBILE_BEFORE = "mobile_before";
    public static final String COLUMNS_TOTAL_BEFORE = "total_before";
    public static final String COLUMNS_MOBILE_INIT = "mobile_init";
    public static final String COLUMNS_TOTAL_INIT = "total_init";
    public static final String NETWORK_TRAFFIC_TYPE_UPLOAD = "upload";
    public static final String NETWORK_TRAFFIC_TYPE_DOWNLOAD = "download";
    public static final String NETWORK_TRAFFIC_CYCLE_TYPE_DAY = "day";
    public static final String NETWORK_TRAFFIC_CYCLE_TYPE_MONTH = "month";
    public static final String NETWORK_TRAFFIC_LAST_RECORD_TIME = "last_record_time";
    public static final String NETWORK_TRAFFIC_LIMIT_BYTES_FOR_DAY = "limit_bytes_for_day";
    public static final String NETWORK_TRAFFIC_MONTHLY_PLAN_MBYTES = "monthly_plan_Mbytes";
    public static final String NETWORK_TRAFFIC_MONTHLY_PLAN_LIMIT_PERCENT = "monthly_plan_limit_percent";
    public static final String NETWORK_TRAFFIC_MONTHLY_USED_CORRECT = "monthly_plan_used_correct";

    public static final String CREATE_NETWORK_TRAFFIC_FOR_DAY_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NETWORK_TRAFFIC_FOR_DAY_TABLE_NAME
            + " ("
            + COLUMNS_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMNS_TYPE
            + " TEXT,"
            + COLUMNS_MOBILE
            + " BIGINT,"
            + COLUMNS_TOTAL
            + " BIGINT,"
            + COLUMNS_MOBILE_BEFORE
            + " BIGINT,"
            + COLUMNS_TOTAL_BEFORE
            + " BIGINT,"
            + COLUMNS_MOBILE_INIT
            + " BIGINT,"
            + COLUMNS_TOTAL_INIT
            + " BIGINT" + ");";

    public static final String CREATE_NETWORK_TRAFFIC_FOR_MONTH_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NETWORK_TRAFFIC_FOR_MONTH_TABLE_NAME
            + " ("
            + COLUMNS_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMNS_TYPE
            + " TEXT,"
            + COLUMNS_MOBILE
            + " BIGINT,"
            + COLUMNS_TOTAL
            + " BIGINT,"
            + COLUMNS_MOBILE_BEFORE
            + " BIGINT,"
            + COLUMNS_TOTAL_BEFORE
            + " BIGINT,"
            + COLUMNS_MOBILE_INIT
            + " BIGINT,"
            + COLUMNS_TOTAL_INIT
            + " BIGINT" + ");";

    public static final String CREATE_NETWORK_TRAFFIC_CONFIG_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NETWORK_TRAFFIC_CONFIG_TABLE_NAME
            + " ("
            + COLUMNS_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMNS_NAME
            + " TEXT,"
            + COLUMNS_VALUE + " TEXT" + ");";

    public static final Uri NETWORK_TRAFFIC_FOR_DAY_URI = Uri.parse(String
            .format("content://%s/%s", AUTOHORITY,
                    NETWORK_TRAFFIC_FOR_DAY_TABLE_NAME));

    public static final Uri NETWORK_TRAFFIC_FOR_MONTH_URI = Uri.parse(String
            .format("content://%s/%s", AUTOHORITY,
                    NETWORK_TRAFFIC_FOR_MONTH_TABLE_NAME));

    public static final Uri NETWORK_TRAFFIC_CONFIG_URI = Uri.parse(String
            .format("content://%s/%s", AUTOHORITY,
                    NETWORK_TRAFFIC_CONFIG_TABLE_NAME));

    public static final int URI_TYPE_NETWORK_TRAFFIC_FOR_DAY = 1;
    public static final int URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH = 2;
    public static final int URI_TYPE_NETWORK_TRAFFIC_CONFIG = 3;

    public static final int NETWORK_TRAFFIC_TYPE_UPLOAD_ID = 1;
    public static final int NETWORK_TRAFFIC_TYPE_DOWNLOAD_ID = 2;

    public static final int NETWORK_TRAFFIC_LAST_RECORD_TIME_ID = 1;
    public static final int NETWORK_TRAFFIC_LIMIT_BYTES_FOR_DAY_ID = 2;
    public static final int NETWORK_TRAFFIC_MONTHLY_PLAN_MBYTES_ID = 3;
    public static final int NETWORK_TRAFFIC_MONTHLY_PLAN_LIMIT_PERCENT_ID = 4;
    public static final int NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_ID = 5;

}
