package com.dylangao.networktrafficmonitor.ui;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dylangao.networktrafficmonitor.R;
import com.dylangao.networktrafficmonitor.database.ConfigDataUtils;
import com.dylangao.networktrafficmonitor.database.TrafficDataUtils;

import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class TrafficDetailFragment extends Fragment {

    private int mResourceId;
    private  int typeID;
    private TrafficDataUtils dailyTrafficBytesUpload;
    private TrafficDataUtils dailyTrafficBytesDownload;
    private TrafficDataUtils monthlyTrafficBytesUpload;
    private TrafficDataUtils monthlyTrafficBytesDownload;
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
        TextView bytesLimitDetail = (TextView)tab.findViewById(R.id.bytes_limit_detail);
        ProgressBar bytesLimitProgressBar = (ProgressBar)tab.findViewById(R.id.bytes_limit_progressBar);
        bytesLimitProgressBar.setMax(100);

        TextView moblieDetailTitle = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_title);
        TextView moblieDetailTotalBytes = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_bytes);
        TextView moblieDetailUpBytes = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_up_bytes);
        TextView moblieDetailDownBytes = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_down_bytes);

        TextView wifiDetailTitle = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_title);
        TextView wifiDetailTotalBytes = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_bytes);
        TextView wifiDetailUpBytes = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_up_bytes);
        TextView wifiDetailDownBytes = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_down_bytes);

        TextView totalDetailTitle = (TextView)tab.findViewById(R.id.total_detail_card).findViewById(R.id.detail_title);
        TextView totalDetailTotalBytes = (TextView)tab.findViewById(R.id.total_detail_card).findViewById(R.id.detail_bytes);
        TextView totalDetailUpBytes = (TextView)tab.findViewById(R.id.total_detail_card).findViewById(R.id.detail_up_bytes);
        TextView totalDetailDownBytes = (TextView)tab.findViewById(R.id.total_detail_card).findViewById(R.id.detail_down_bytes);

        moblieDetailTitle.setText("移动数据");
        wifiDetailTitle.setText("无线连接");
        totalDetailTitle.setText("流量总计");
        ContentResolver cr = getActivity().getContentResolver();

        dailyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        dailyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        monthlyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
        monthlyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);

        switch (getArguments().getInt(UIConstants.TAB_TYPE)) {
            case  UIConstants.MONTH_TYPE:
                bytesLimitTitleView.setText("月流量");
                if(ConfigDataUtils.getMonthlyPlanBytes(cr).equals("0")) {
                    bytesLimitDetail.setText("0/0(MB)");
                    bytesLimitProgressBar.setProgress(0);
                }
                break;
            case UIConstants.DAY_TYPE:
                bytesLimitTitleView.setText("日流量");
                if(ConfigDataUtils.getLimitBytesForDay(cr).equals("0")) {
                    bytesLimitDetail.setText("0/0(MB)");
                    bytesLimitProgressBar.setProgress(0);
                }
                break;
            default:
                break;
        }

        return tab;
    }
}
