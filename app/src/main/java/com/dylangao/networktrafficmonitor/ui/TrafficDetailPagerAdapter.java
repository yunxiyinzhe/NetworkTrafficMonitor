package com.dylangao.networktrafficmonitor.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.dylangao.networktrafficmonitor.R;
import com.material.widget.TabIndicator;

public class TrafficDetailPagerAdapter extends FragmentPagerAdapter implements TabIndicator.TabResourceProvider {

    protected static final String[] TAB_TITLES = new String[]{"月流量统计", "日流量统计"};

    private int mCount = TAB_TITLES.length;

    public TrafficDetailPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment tab = TrafficDetailFragment.newInstance(R.layout.traffic_detail_tab_layout);
        Bundle args = new Bundle();
        if (position == 0) {
            args.putInt(UIConstants.TAB_TYPE,UIConstants.MONTH_TYPE);
        }
        if (position == 1) {
            args.putInt(UIConstants.TAB_TYPE,UIConstants.DAY_TYPE);
        }
        tab.setArguments(args);
        return tab;
    }

    @Override
    public String getText(int position) {
        return TrafficDetailPagerAdapter.TAB_TITLES[position % TAB_TITLES.length];
    }

    @Override
    public int getCount() {
        return mCount;
    }
}
