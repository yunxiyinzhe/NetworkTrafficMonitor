package com.dylangao.networktrafficmonitor.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dylangao.networktrafficmonitor.R;

public class TrafficAppDetailActivity extends Activity {
    private ListView lv;
    private List<AppInfo> data;
    private AppDetailListViewAdapter adapter;
    private Handler handler = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_app_detail_layout);
        lv = (ListView)findViewById(R.id.listView);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                showContent();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                data = getData();
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    public void showContent() {
        adapter = new AppDetailListViewAdapter(this);
        lv.setAdapter(adapter);
        findViewById(R.id.loading_indicator).setVisibility(View.GONE);
        findViewById(R.id.listView).setVisibility(View.VISIBLE);
    }

    private List<AppInfo> getData() {
        List<AppInfo> appList = new ArrayList<AppInfo>();
        List<ApplicationInfo> appsInfo = getPackageManager().getInstalledApplications(PackageManager.GET_ACTIVITIES);

        for(int i = 0; i < appsInfo.size(); i++) {
            ApplicationInfo app = appsInfo.get(i);
            AppInfo tmpInfo =new AppInfo();
            tmpInfo.setAppName(app.loadLabel(getPackageManager()).toString());
            tmpInfo.setAppIcon(app.loadIcon(getPackageManager()));
            tmpInfo.setUid(app.uid);
            int uploadBytes = (int)(TrafficStats.getUidTxBytes(app.uid)/1024L/1024L);
            int downloadBytes = (int)(TrafficStats.getUidRxBytes(app.uid)/1024L/1024L);
            int totalBytes = uploadBytes + downloadBytes;
            tmpInfo.setUploadBytes(uploadBytes);
            tmpInfo.setDownloadBytes(downloadBytes);
            tmpInfo.setTotalBytes(totalBytes);
            appList.add(tmpInfo);
        }
        Collections.sort(appList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo lhs, AppInfo rhs) {
                int bytes1 = lhs.getTotalBytes();
                int bytes2 = rhs.getTotalBytes();
                if(bytes1 < bytes2) {
                    return 1;
                }
                return -1;
            }
        });
        return appList;
        }

        static class ViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public TextView appDetail;
    }

    public class AppDetailListViewAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        private AppDetailListViewAdapter(Context context)
        {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.app_detail_item, null);
                holder.appIcon = (ImageView)convertView.findViewById(R.id.appIcon);
                holder.appName = (TextView)convertView.findViewById(R.id.app_name);
                holder.appDetail = (TextView)convertView.findViewById(R.id.app_detail);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.appIcon.setImageDrawable(data.get(position).getAppIcon());
            holder.appName.setText((String) data.get(position).getAppName());
            String detail = String.format("上传:%d MB 下载:%d MB 总计:%d MB",
                    data.get(position).getUploadBytes(),
                    data.get(position).getDownloadBytes(),
                    data.get(position).getTotalBytes());
            holder.appDetail.setText(detail);

            return convertView;
        }

    }

    public class AppInfo {
        private String appName="";
        private Drawable appIcon=null;
        private int uid;
        private int uploadBytes;
        private int downloadBytes;
        private int totalBytes;

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public void setAppIcon(Drawable appIcon) {
            this.appIcon = appIcon;
        }

        public String getAppName() {
            return appName;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getUid() {
            return uid;
        }

        public void setUploadBytes(int uploadBytes) {
            this.uploadBytes = uploadBytes;
        }

        public int getUploadBytes() {
            return uploadBytes;
        }

        public void setDownloadBytes(int downloadBytes) {
            this.downloadBytes = downloadBytes;
        }

        public int getDownloadBytes() {
            return downloadBytes;
        }

        public void setTotalBytes(int totalBytes) {
            this.totalBytes = totalBytes;
        }

        public int getTotalBytes() {
            return totalBytes;
        }
    }
}