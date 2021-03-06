package com.dylangao.networktrafficmonitor.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.util.Log;

import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class TrafficDataUtils {
    private String mType;
    private int mComputingCycle;
    private ContentResolver mContentResolver;
    private long initialMobileTrafficBytes = 0;
    private long initialTotalTrafficBytes = 0;

    public TrafficDataUtils(String type, int computingCycle, ContentResolver cr) {
        mType = type;
        mComputingCycle = computingCycle;
        mContentResolver = cr;
    }

    public String getType() {
        return mType;
    }

    public int getComputingCycle() {
        return mComputingCycle;
    }

    public void initialTrafficBytes() {
        initialMobileTrafficBytes = getTrafficData(COLUMNS_MOBILE_INIT, mContentResolver);
        initialTotalTrafficBytes = getTrafficData(COLUMNS_TOTAL_INIT, mContentResolver);

    }

    public void resetTrafficBytes() {
        long[] trafficBytes = new long[6];
        (trafficBytes)[0] = 0;
        (trafficBytes)[1] = 0;
        (trafficBytes)[2] = getMobileBytes();
        (trafficBytes)[3] = getTotalBytes();
        (trafficBytes)[4] = 0;
        (trafficBytes)[5] = 0;
        setTrafficData(trafficBytes, mContentResolver);
        initialMobileTrafficBytes = getTrafficData(COLUMNS_MOBILE_INIT, mContentResolver);
        initialTotalTrafficBytes = getTrafficData(COLUMNS_TOTAL_INIT, mContentResolver);

    }

    public void restoreTrafficBytes() {
        long[] trafficBytes = new long[6];

        long mobileBytesSincePowerOn = getMobileBytes();
        long mobileBytesBefore = getTrafficData(COLUMNS_MOBILE_BEFORE, mContentResolver);
        Log.v("MonitorService",
                "mobileBytesBefore is "
                        + Long.toString(mobileBytesSincePowerOn) + " "
                        + "totalTxBytesBeforeForDay is "
                        + Long.toString(mobileBytesBefore));
        if (mobileBytesSincePowerOn < mobileBytesBefore) {
            Log.v("MonitorService", "should not reaches here");
            mobileBytesBefore = 0;
        }

        (trafficBytes)[0] = mobileBytesSincePowerOn
                + initialMobileTrafficBytes - mobileBytesBefore;

        long totalBytesSincePowerOn = getTotalBytes();
        long totalBytesBefore = getTrafficData(COLUMNS_TOTAL_BEFORE, mContentResolver);
        Log.v("MonitorService",
                "totalBytesSincePowerOn is "
                        + Long.toString(totalBytesSincePowerOn) + " "
                        + "totalTxBytesBeforeForDay is "
                        + Long.toString(totalBytesBefore));
        if (totalBytesSincePowerOn < totalBytesBefore) {
            Log.v("MonitorService", "should not reaches here");
            totalBytesBefore = 0;
        }

        (trafficBytes)[1] = totalBytesSincePowerOn
                + initialTotalTrafficBytes - totalBytesBefore;
        (trafficBytes)[2] = mobileBytesBefore;
        (trafficBytes)[3] = totalBytesBefore;

        (trafficBytes)[4] = getTrafficData(COLUMNS_MOBILE_INIT, mContentResolver);
        (trafficBytes)[5] = getTrafficData(COLUMNS_TOTAL_INIT, mContentResolver);

        setTrafficData(trafficBytes, mContentResolver);


        Log.v("MonitorService",
                getTableUri().toString() +
                        " mobileBytes " + mType + " is:"
                        + Double.toString((trafficBytes)[0] / 1024.0 / 1024.0)
                        + " "
                        + "totalBytes " + mType + " is:"
                        + Double.toString((trafficBytes)[1] / 1024.0 / 1024.0));

    }

    private Uri getTableUri() {
        Uri tableUri = Uri.parse("");
        switch (mComputingCycle) {
            case URI_TYPE_NETWORK_TRAFFIC_FOR_DAY:
                tableUri = NETWORK_TRAFFIC_FOR_DAY_URI;
                break;
            case URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH:
                tableUri = NETWORK_TRAFFIC_FOR_MONTH_URI;
                break;
            default:
                break;
        }
        return tableUri;
    }

    private long getMobileBytes() {
        return mType.equals(NETWORK_TRAFFIC_TYPE_UPLOAD) ? TrafficStats
                .getMobileTxBytes() : TrafficStats.getMobileRxBytes();

    }

    private long getTotalBytes() {
        return mType.equals(NETWORK_TRAFFIC_TYPE_UPLOAD) ? TrafficStats
                .getTotalTxBytes() : TrafficStats.getTotalRxBytes();
    }

    public long getTrafficData(String column, ContentResolver cr) {
        Cursor cursor = null;
        long result = 0;

        int type_id;
        if (mType.equals(NETWORK_TRAFFIC_TYPE_UPLOAD)) {
            type_id = NETWORK_TRAFFIC_TYPE_UPLOAD_ID;
        } else {
            type_id = NETWORK_TRAFFIC_TYPE_DOWNLOAD_ID;
        }
        String[] whereArgs = new String[]{mType, String.valueOf(type_id)};
        String[] columns = new String[]{column};

        cursor = cr.query(getTableUri(), columns, COLUMNS_TYPE + "=? AND " + COLUMNS_ID
                + "=?", whereArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public void setTrafficData(long[] data, ContentResolver cr) {
        int type_id;
        if (mType.equals(NETWORK_TRAFFIC_TYPE_UPLOAD)) {
            type_id = NETWORK_TRAFFIC_TYPE_UPLOAD_ID;
        } else {
            type_id = NETWORK_TRAFFIC_TYPE_DOWNLOAD_ID;
        }
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS_ID, type_id);
        cv.put(COLUMNS_TYPE, mType);
        cv.put(COLUMNS_MOBILE, (data)[0]);
        cv.put(COLUMNS_TOTAL, (data)[1]);
        cv.put(COLUMNS_MOBILE_BEFORE, (data)[2]);
        cv.put(COLUMNS_TOTAL_BEFORE, (data)[3]);
        cv.put(COLUMNS_MOBILE_INIT, (data)[4]);
        cv.put(COLUMNS_TOTAL_INIT, (data)[5]);

        String[] whereArgs = new String[]{mType, String.valueOf(type_id)};

        cr.delete(getTableUri(), COLUMNS_TYPE + "=? AND " + COLUMNS_ID + "=?", whereArgs);
        cr.insert(getTableUri(), cv);
    }

    public void updateInitTrafficData(long[] data, ContentResolver cr) {
        int type_id;
        if (mType.equals(NETWORK_TRAFFIC_TYPE_UPLOAD)) {
            type_id = NETWORK_TRAFFIC_TYPE_UPLOAD_ID;
        } else {
            type_id = NETWORK_TRAFFIC_TYPE_DOWNLOAD_ID;
        }
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS_MOBILE_INIT, (data)[0]);
        cv.put(COLUMNS_TOTAL_INIT, (data)[1]);

        String[] whereArgs = new String[]{mType, String.valueOf(type_id)};

        cr.update(getTableUri(), cv, COLUMNS_TYPE + "=? AND " + COLUMNS_ID + "=?", whereArgs);
    }
}
