package com.dylangao.networktrafficmonitor.ui;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gc.materialdesign.views.ProgressBarDetermininate;
import com.dylangao.networktrafficmonitor.R;
import com.dylangao.networktrafficmonitor.database.ConfigDataUtils;
import com.dylangao.networktrafficmonitor.database.TrafficDataUtils;
import com.github.lzyzsd.circleprogress.CircleProgress;

import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class TrafficDetailFragment extends Fragment {

    private int mResourceId;
    private  int typeID;
    private TrafficDataUtils dailyTrafficBytesUpload;
    private TrafficDataUtils dailyTrafficBytesDownload;
    private TrafficDataUtils monthlyTrafficBytesUpload;
    private TrafficDataUtils monthlyTrafficBytesDownload;

    private TextView bytesLimitTitleView;
    private TextView bytesLimitDetail;
    private CircleProgress bytesLimitProgressBar;

    private TextView moblieDetailTitle;
    private TextView moblieDetailTotalBytes;
    private TextView moblieDetailUpBytes;
    private TextView moblieDetailDownBytes;

    private TextView wifiDetailTitle;
    private TextView wifiDetailTotalBytes;
    private TextView wifiDetailUpBytes;
    private TextView wifiDetailDownBytes;

    private ContentResolver cr;


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
        bytesLimitTitleView = (TextView)tab.findViewById(R.id.bytes_limit_title);
        cr = getActivity().getContentResolver();

        bytesLimitDetail = (TextView)tab.findViewById(R.id.bytes_limit_detail);
        bytesLimitProgressBar = (CircleProgress)tab.findViewById(R.id.bytes_limit_progressBar);
        bytesLimitProgressBar.setMax(100);

        moblieDetailTitle = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_title);
        moblieDetailTotalBytes = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_bytes);
        moblieDetailUpBytes = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_up_bytes);
        moblieDetailDownBytes = (TextView)tab.findViewById(R.id.moblie_detail_card).findViewById(R.id.detail_down_bytes);

        wifiDetailTitle = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_title);
        wifiDetailTotalBytes = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_bytes);
        wifiDetailUpBytes = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_up_bytes);
        wifiDetailDownBytes = (TextView)tab.findViewById(R.id.wifi_detail_card).findViewById(R.id.detail_down_bytes);
        updateViews();
        return tab;
    }

    private void updateViews( ) {
        moblieDetailTitle.setText("移动数据");
        wifiDetailTitle.setText("无线连接");

        dailyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        dailyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_DAY, cr);
        monthlyTrafficBytesUpload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
        monthlyTrafficBytesDownload = new TrafficDataUtils(
                NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
        int used, planed, correctedUp, correctedDown;
        switch (getArguments().getInt(UIConstants.TAB_TYPE)) {
            case  UIConstants.MONTH_TYPE:
                bytesLimitTitleView.setText("月流量");
                used = (int)(monthlyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) +
                        (int)(monthlyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024);
                planed = Integer.valueOf(ConfigDataUtils.getMonthlyPlanBytes(cr));
                correctedUp = Integer.valueOf(ConfigDataUtils.getMonthlyUsedCorrect(NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_UPLOAD_ID,cr));
                correctedDown = Integer.valueOf(ConfigDataUtils.getMonthlyUsedCorrect(NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_DOWNLOAD_ID,cr));
                int correctedUsed = used + correctedUp + correctedDown;
                bytesLimitDetail.setText(correctedUsed + "/" + planed +"(MB)");
                if(ConfigDataUtils.getMonthlyPlanBytes(cr).equals("0")) {
                    bytesLimitProgressBar.setProgress(0);
                } else {
                    int progress = correctedUsed * 100 / planed;
                    if(progress > 100) {
                        progress = 100;
                        bytesLimitProgressBar.setTextColor(getResources().getColor(R.color.material_pink_500));
                    } else {
                        bytesLimitProgressBar.setTextColor(getResources().getColor(R.color.primary_color));
                    }
                    bytesLimitProgressBar.setProgress(progress);
                }

                moblieDetailTotalBytes.setText(correctedUsed + "MB");
                int fakeUpBytes = (int)(monthlyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + correctedUp;
                int fakeDownBytes = (int)(monthlyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + correctedDown;

                moblieDetailUpBytes.setText(fakeUpBytes + "MB");
                moblieDetailDownBytes.setText(fakeDownBytes + "MB");

                wifiDetailTotalBytes.setText((int)(monthlyTrafficBytesUpload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024) +
                        (int)monthlyTrafficBytesDownload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024 - used + "MB");
                wifiDetailUpBytes.setText((int)monthlyTrafficBytesUpload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024 -
                        (int)(monthlyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + "MB");
                wifiDetailDownBytes.setText((int)monthlyTrafficBytesDownload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024 -
                        (int)(monthlyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + "MB");
                break;
            case UIConstants.DAY_TYPE:
                bytesLimitTitleView.setText("日流量");
                used = (int)(dailyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) +
                        (int)(dailyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024);
                planed = (Integer.valueOf(ConfigDataUtils.getLimitBytesForDay(cr)));
                bytesLimitDetail.setText(used + "/" + planed +"(MB)");
                if(ConfigDataUtils.getLimitBytesForDay(cr).equals("0")) {
                    bytesLimitProgressBar.setProgress(0);
                } else {
                    int progress = used * 100 / planed;
                    if(progress > 100) {
                        progress = 100;
                        bytesLimitProgressBar.setTextColor(getResources().getColor(R.color.material_pink_500));
                    } else {
                        bytesLimitProgressBar.setTextColor(getResources().getColor(R.color.primary_color));
                    }
                    bytesLimitProgressBar.setProgress(progress);
                }

                moblieDetailTotalBytes.setText(used + "MB");
                moblieDetailUpBytes.setText((int)(dailyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + "MB");
                moblieDetailDownBytes.setText((int)(dailyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + "MB");

                wifiDetailTotalBytes.setText((int)(dailyTrafficBytesUpload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024) +
                        (int)dailyTrafficBytesDownload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024 - used + "MB");
                wifiDetailUpBytes.setText((int)dailyTrafficBytesUpload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024 -
                        (int)(dailyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + "MB");
                wifiDetailDownBytes.setText((int)dailyTrafficBytesDownload.getTrafficData(COLUMNS_TOTAL,cr)/1024/1024 -
                        (int)(dailyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024) + "MB");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViews();
    }
}
