package com.dylangao.networktrafficmonitor.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
		Intent it = new Intent(context, MonitorService.class);
		it.putExtra("FromBoot", true);
        cr = context.getContentResolver();
        dailyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        dailyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        dailyTrafficBytesUpload.initialTrafficBytes(!isNewDay());
        dailyTrafficBytesDownload.initialTrafficBytes(!isNewDay());

        monthlyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
        monthlyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
        monthlyTrafficBytesUpload.initialTrafficBytes(!isNewMonth());
        monthlyTrafficBytesDownload.initialTrafficBytes(!isNewMonth());

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
