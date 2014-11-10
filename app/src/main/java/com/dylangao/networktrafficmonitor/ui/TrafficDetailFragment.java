package com.dylangao.networktrafficmonitor.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrafficDetailFragment extends Fragment {

    private int mResourceId;

    public static TrafficDetailFragment newInstance(int resourceId) {
        TrafficDetailFragment detailFragment = new TrafficDetailFragment();
        detailFragment.mResourceId = resourceId;
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(mResourceId, container, false);
    }
}
