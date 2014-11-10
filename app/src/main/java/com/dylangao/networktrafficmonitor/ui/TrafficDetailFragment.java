package com.dylangao.networktrafficmonitor.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dylangao.networktrafficmonitor.R;

public class TrafficDetailFragment extends Fragment {

    private int mResourceId;
    private  int typeID;

    public static TrafficDetailFragment newInstance(int resourceId) {
        TrafficDetailFragment detailFragment = new TrafficDetailFragment();
        detailFragment.mResourceId = resourceId;
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View tab = inflater.inflate(mResourceId, container, false);
        TextView bytesLimitTitleView = (TextView)tab.findViewById(R.id.bytes_limit_title);
        switch (getArguments().getInt(UIConstants.TAB_TYPE)) {
            case  UIConstants.MONTH_TYPE:
                bytesLimitTitleView.setText("月流量");
                break;
            case UIConstants.DAY_TYPE:
                bytesLimitTitleView.setText("日流量");
                break;
            default:
                break;
        }

        return tab;
    }
}
