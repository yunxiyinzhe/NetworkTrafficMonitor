package com.dylangao.networktrafficmonitor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import com.dylangao.networktrafficmonitor.R;
import com.dylangao.networktrafficmonitor.service.MonitorService;
import com.material.widget.TabIndicator;
import android.util.Log;

public class TrafficDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_detail_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.detail_pager);
        TrafficDetailPagerAdapter adapter = new TrafficDetailPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabIndicator indicator = (TabIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        ImageButton returnButton = (ImageButton)findViewById(R.id.app_detail_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TrafficDetailActivity.this, TrafficAppDetailActivity.class);
                startActivity(intent);
            }
        });
        ImageButton settingButton = (ImageButton)findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TrafficDetailActivity.this, TrafficSettingActivity.class);
                startActivity(intent);
            }
        });
        if(!isServiceRunning()) {
            Log.v("MainActivity", "MonitorService is not running");
            startService(new Intent(this, MonitorService.class));
        } else {
            Log.v("MainActivity", "MonitorService is already running");
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Log.v("MainActivity", "MonitorService class name is " + MonitorService.class.getName());
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.v("MainActivity", "Service name is " + service.service.getClassName());
            if ("com.dylangao.networktrafficmonitor.MonitorService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}