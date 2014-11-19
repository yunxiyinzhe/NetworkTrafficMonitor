package com.dylangao.networktrafficmonitor.service;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dylangao.networktrafficmonitor.database.ConfigDataUtils;
import com.dylangao.networktrafficmonitor.database.TrafficDataUtils;
import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class BootReceiver extends BroadcastReceiver {
    private TrafficDataUtils dailyTrafficBytesUpload;
    private TrafficDataUtils dailyTrafficBytesDownload;
    private TrafficDataUtils monthlyTrafficBytesUpload;
    private TrafficDataUtils monthlyTrafficBytesDownload;
    private ContentResolver cr;
	@Override
	public void onReceive(Context context, Intent intent) {
        cr = context.getContentResolver();
        dailyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        dailyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);

        if(isNewDay()) {
            dailyTrafficBytesUpload.updateInitTrafficData(new long[]{0, 0}, cr);
            dailyTrafficBytesDownload.updateInitTrafficData(new long[]{0, 0}, cr);
        } else {
            dailyTrafficBytesUpload.updateInitTrafficData(new long[]{dailyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE, cr),
                    dailyTrafficBytesUpload.getTrafficData(COLUMNS_TOTAL, cr)}, cr);
            dailyTrafficBytesDownload.updateInitTrafficData(new long[]{dailyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE, cr),
                    dailyTrafficBytesDownload.getTrafficData(COLUMNS_TOTAL, cr)}, cr);
        }

        monthlyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
        monthlyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);

        if(isNewMonth()) {
            monthlyTrafficBytesUpload.updateInitTrafficData(new long[]{0, 0}, cr);
            monthlyTrafficBytesDownload.updateInitTrafficData(new long[]{0, 0}, cr);
        } else {
            monthlyTrafficBytesUpload.updateInitTrafficData(new long[]{monthlyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE, cr),
                    monthlyTrafficBytesUpload.getTrafficData(COLUMNS_TOTAL, cr)}, cr);
            monthlyTrafficBytesDownload.updateInitTrafficData(new long[]{monthlyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE, cr),
                    monthlyTrafficBytesDownload.getTrafficData(COLUMNS_TOTAL, cr)}, cr);
        }
        Intent it = new Intent(context, MonitorService.class);
		context.startService(it);
	}

    private boolean isNewDay() {
        int lastRecordTime = Integer.parseInt(ConfigDataUtils.getLastRecordTime(cr));
        int dateOfToday = Integer.parseInt(ConfigDataUtils.getDateOfToday());
        Log.v("MonitorService", "lastRecordTime is " + lastRecordTime
                + " dateOfToday is " + dateOfToday);
        if (lastRecordTime >= dateOfToday) {
            return false;
        }
        return true;
    }

    private boolean isNewMonth() {
        int lastRecordMonth = Integer.parseInt(ConfigDataUtils
                .getLastRecordTime(cr).equals("0") ? "0" : ConfigDataUtils
                .getLastRecordTime(cr).substring(0, 6));
        int monthOfToday = Integer.parseInt(ConfigDataUtils.getDateOfToday()
                .substring(0, 6));
        Log.v("MonitorService", "lastRecordMonth is " + lastRecordMonth
                + " monthOfToday is " + monthOfToday);
        if (lastRecordMonth >= monthOfToday) {
            return false;
        }
        return true;
    }
}
