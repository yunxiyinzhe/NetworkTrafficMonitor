package com.dylangao.networktrafficmonitor.ui;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dylangao.networktrafficmonitor.R;
import com.dylangao.networktrafficmonitor.database.ConfigDataUtils;
import com.dylangao.networktrafficmonitor.service.MonitorService;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class TrafficDetailActivity extends ActionBarActivity implements MaterialTabListener {

	private ViewPager pager;
	private ViewPagerAdapter pagerAdapter;
	MaterialTabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traffic_detail_layout);

		tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
		pager = (ViewPager) this.findViewById(R.id.viewpager);

		// init view pager
		pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	// when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

		// insert all tabs from pagerAdapter data
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
                            );
        }

        ImageButton appdetailButton = (ImageButton)findViewById(R.id.app_detail_button);
        appdetailButton.setOnClickListener(new View.OnClickListener() {
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

        hasSetMonthlyPlan();
	}

    private void hasSetMonthlyPlan() {
        ContentResolver cr = getContentResolver();
        if(ConfigDataUtils.getMonthlyPlanBytes(cr).equals("0")) {
            new MaterialDialog.Builder(this)
                    .content(R.string.monthly_plan_setting_tips)
                    .positiveText(android.R.string.ok)  // the default is 'Accept', this line could be left out
                    .negativeText(android.R.string.cancel)  // leaving this line out will remove the negative button
                    .callback(new MaterialDialog.Callback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            Intent intent = new Intent();
                            intent.setClass(TrafficDetailActivity.this, TrafficSettingActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                        }
                    }).build().show();
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Log.v("MainActivity", "MonitorService class name is " + MonitorService.class.getName());
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.v("MainActivity", "Service name is " + service.service.getClassName());
            if ("com.dylangao.networktrafficmonitor.service.MonitorService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

	@Override
	public void onTabSelected(MaterialTab tab) {
		// when the tab is clicked the pager swipe content to the tab position
		pager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabReselected(MaterialTab tab) {
	}

	@Override
	public void onTabUnselected(MaterialTab tab) {
	}

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        protected final String[] TAB_TITLES = new String[]{"月流量统计", "日流量统计"};

        private int mCount = TAB_TITLES.length;

        public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int num) {
            Fragment tab = TrafficDetailFragment.newInstance(R.layout.traffic_detail_tab_layout);
            Bundle args = new Bundle();
            if (num == 0) {
                args.putInt(UIConstants.TAB_TYPE,UIConstants.MONTH_TYPE);
            }
            if (num == 1) {
                args.putInt(UIConstants.TAB_TYPE,UIConstants.DAY_TYPE);
            }
            tab.setArguments(args);
            return tab;
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position % TAB_TITLES.length];

        }
    }

}
