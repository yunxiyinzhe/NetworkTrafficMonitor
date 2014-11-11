package com.dylangao.networktrafficmonitor.database;

import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class ConfigDataUtils {
    public static void setNetworkTrafficConfig(String key, String value,
                                               int id, ContentResolver cr) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS_ID, id);
        cv.put(COLUMNS_NAME, key);
        cv.put(COLUMNS_VALUE, value);
        String[] whereArgs = new String[]{key, String.valueOf(id)};

        cr.delete(NETWORK_TRAFFIC_CONFIG_URI, COLUMNS_NAME + "=? AND "
                + COLUMNS_ID + "=?", whereArgs);
        cr.insert(NETWORK_TRAFFIC_CONFIG_URI, cv);
    }

    public static String getNetworkTrafficConfig(String key, int id,
                                                 ContentResolver cr) {
        Cursor cursor = null;
        String result = new String("");

        String[] whereArgs = new String[]{String.valueOf(id), key};
        String[] columns = new String[]{COLUMNS_VALUE};

        cursor = cr.query(NETWORK_TRAFFIC_CONFIG_URI, columns, COLUMNS_ID
                + "=? AND " + COLUMNS_NAME + "=?", whereArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public static void setLastRecordTime(ContentResolver cr) {
        String date = ConfigDataUtils.getDateOfToday();
        setNetworkTrafficConfig(NETWORK_TRAFFIC_LAST_RECORD_TIME, date,
                NETWORK_TRAFFIC_LAST_RECORD_TIME_ID, cr);
    }

    public static String getLastRecordTime(ContentResolver cr) {
        String result =  getNetworkTrafficConfig(NETWORK_TRAFFIC_LAST_RECORD_TIME,
                NETWORK_TRAFFIC_LAST_RECORD_TIME_ID, cr);
        return result.equals("") ? "0" : result;
    }

    public static void setLimitBytesForDay(int limit, ContentResolver cr) {
        String value = String.valueOf(limit);
        setNetworkTrafficConfig(
                NETWORK_TRAFFIC_LIMIT_BYTES_FOR_DAY, value,
                NETWORK_TRAFFIC_LIMIT_BYTES_FOR_DAY_ID, cr);
    }

    public static String getLimitBytesForDay(ContentResolver cr) {
        String result = getNetworkTrafficConfig(
                NETWORK_TRAFFIC_LIMIT_BYTES_FOR_DAY,
                NETWORK_TRAFFIC_LIMIT_BYTES_FOR_DAY_ID, cr);
        return result.equals("") ? "0" : result;
    }

    public static void setMonthlyPlanMBytes(int mbytes, ContentResolver cr) {
        String value = String.valueOf(mbytes);
        setNetworkTrafficConfig(
                NETWORK_TRAFFIC_MONTHLY_PLAN_MBYTES, value,
                NETWORK_TRAFFIC_MONTHLY_PLAN_MBYTES_ID, cr);
    }

    public static String getMonthlyPlanBytes(ContentResolver cr) {
        String result = getNetworkTrafficConfig(
                NETWORK_TRAFFIC_MONTHLY_PLAN_MBYTES,
                NETWORK_TRAFFIC_MONTHLY_PLAN_MBYTES_ID, cr);
        return result.equals("") ? "0" : result;
    }

    public static String getDateOfToday() {
        long currTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currTime);
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        String date = String.valueOf(nowYear)
                + ((nowMonth < 10) ? String.valueOf(0) : "")
                + String.valueOf(nowMonth)
                + ((nowDay < 10) ? String.valueOf(0) : "")
                + String.valueOf(nowDay);
        return date;
    }
}
