package com.dylangao.networktrafficmonitor.ui;

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
        if (position == 0) {
            return TrafficDetailFragment.newInstance(R.layout.traffic_detail_tab_layout);
        }
        if (position == 1) {
            return TrafficDetailFragment.newInstance(R.layout.traffic_detail_tab_layout);
        }
        return null;
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
