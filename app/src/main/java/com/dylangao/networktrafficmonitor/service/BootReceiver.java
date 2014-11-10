package com.dylangao.networktrafficmonitor.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent it = new Intent(context, MonitorService.class);
		it.putExtra("FromBoot", true);
		context.startService(it);
	}

}
