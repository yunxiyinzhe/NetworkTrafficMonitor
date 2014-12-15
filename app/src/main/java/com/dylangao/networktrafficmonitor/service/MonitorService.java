package com.dylangao.networktrafficmonitor.service;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.dylangao.networktrafficmonitor.database.TrafficDataUtils;
import com.dylangao.networktrafficmonitor.database.ConfigDataUtils;
import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class MonitorService extends Service {
	private static final int delay = 5000;
	private static final int STARTMONITOR = 0;
	private boolean isFromBoot = false;
	private ContentResolver cr;

	private TrafficDataUtils dailyTrafficBytesUpload;
	private TrafficDataUtils dailyTrafficBytesDownload;
	private TrafficDataUtils monthlyTrafficBytesUpload;
	private TrafficDataUtils monthlyTrafficBytesDownload;

	private NetworkTrafficHandler handler = new NetworkTrafficHandler(this);

	static class NetworkTrafficHandler extends Handler {
		private final WeakReference<MonitorService> mService;

		NetworkTrafficHandler(MonitorService service) {
			mService = new WeakReference<MonitorService>(service);
		}

		@Override
		public void handleMessage(Message msg) {
			MonitorService service = mService.get();
			if (service != null) {
				switch (msg.what) {
				case STARTMONITOR:
					service.handler.postDelayed(service.inqueryRunnable, delay);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		}
	}

	private Runnable inqueryRunnable = new Runnable() {

		@Override
		public void run() {
			Log.v("MonitorService", "MonitorService run every 5s");
			if (isNewDay()) {
				handleNewDay();
			}
			if (isNewMonth()) {
				handleNewMonth();
			}
			restoreData();
			handler.postDelayed(inqueryRunnable, delay);
		}
	};

	@Override
	public void onCreate() {
		Log.v("MonitorService", "MonitorService onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		cr = getContentResolver();
		dailyTrafficBytesUpload = new TrafficDataUtils(
				NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        dailyTrafficBytesUpload.initialTrafficBytes();

		dailyTrafficBytesDownload = new TrafficDataUtils(
				NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        dailyTrafficBytesDownload.initialTrafficBytes();

		monthlyTrafficBytesUpload = new TrafficDataUtils(
				NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
        monthlyTrafficBytesUpload.initialTrafficBytes();

		monthlyTrafficBytesDownload = new TrafficDataUtils(
				NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
       monthlyTrafficBytesDownload.initialTrafficBytes();

		handler.sendEmptyMessage(STARTMONITOR);
		checkNetworkTrafficLimits();
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public void onDestroy() {
		Log.v("MonitorService", "MonitorService onDestroy");
	}

	private void handleNewDay() {
		Log.v("MonitorService", "handleNewDay");
		dailyTrafficBytesUpload.resetTrafficBytes();
		dailyTrafficBytesDownload.resetTrafficBytes();
	}

	private void handleNewMonth() {
		Log.v("MonitorService", "handleNewMonth");
		monthlyTrafficBytesUpload.resetTrafficBytes();
		monthlyTrafficBytesDownload.resetTrafficBytes();
        ConfigDataUtils.setMonthlyUsedCorrect(0, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_DOWNLOAD, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_DOWNLOAD_ID, getContentResolver());
        ConfigDataUtils.setMonthlyUsedCorrect(0, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_UPLOAD, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_UPLOAD_ID, getContentResolver());
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


	private void checkNetworkTrafficLimits() {
		long limitBytesForDay = Long.parseLong(ConfigDataUtils.getLimitBytesForDay(cr));
		long monthlyPlanBytes = Long.parseLong(ConfigDataUtils.getMonthlyPlanBytes(cr));

		long dayBytes = dailyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE, cr) +
				dailyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE, cr);
		long monthBytes = monthlyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE, cr) +
				monthlyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE, cr);
		Log.v("MonitorService","limitBytesForDay is " + limitBytesForDay + "dayBytes is " + dayBytes);
		if(limitBytesForDay > 0 && limitBytesForDay < dayBytes) {
			Log.v("MonitorService","send day alert");
		}
		Log.v("MonitorService","mMonthlyPlanBytes is " + monthlyPlanBytes + "monthBytes is " + monthBytes);
		if(monthlyPlanBytes > 0 && monthlyPlanBytes < monthBytes) {
			Log.v("MonitorService","send month alert");
		}
	}

	private void restoreData() {
		Log.v("MonitorService", "Begin to restore Data");
		dailyTrafficBytesUpload.restoreTrafficBytes();
		dailyTrafficBytesDownload.restoreTrafficBytes();
		monthlyTrafficBytesUpload.restoreTrafficBytes();
		monthlyTrafficBytesDownload.restoreTrafficBytes();

		ConfigDataUtils.setLastRecordTime(cr);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
