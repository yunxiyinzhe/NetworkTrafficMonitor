package com.dylangao.networktrafficmonitor.ui;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dylangao.networktrafficmonitor.database.ConfigDataUtils;
import com.dylangao.networktrafficmonitor.database.TrafficDataUtils;
import com.gc.materialdesign.views.ButtonFlat;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dylangao.networktrafficmonitor.R;

import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class TrafficSettingActivity extends FragmentActivity {
    private  ContentResolver cr;
    private TextView monthPlanView;
    private TextView dayPlanView;
    private SeekBar percentSlider;
    private  TextView monthPlanLimitView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_setting_layout);
        cr = getContentResolver();
        ButtonFlat monthPlanButton = (ButtonFlat)findViewById(R.id.month_plan_button);
        monthPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlanSetDlg(UIConstants.MONTH_TYPE);
            }
        });

        ButtonFlat dayPlanButton = (ButtonFlat)findViewById(R.id.day_plan_button);
        dayPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlanSetDlg(UIConstants.DAY_TYPE);
            }
        });

        ButtonFlat monthlyUsedCorrectButton = (ButtonFlat)findViewById(R.id.correct_monthly_used_button);
        monthlyUsedCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonthlyUsedCorrectDlg();
            }
        });

        monthPlanView = (TextView)findViewById(R.id.month_plan_mb_view);
        dayPlanView = (TextView)findViewById(R.id.day_plan_mb_view);
        monthPlanView.setText(ConfigDataUtils.getMonthlyPlanBytes(cr) + "MB");
        dayPlanView.setText(ConfigDataUtils.getLimitBytesForDay(cr) + "MB");


        monthPlanLimitView = (TextView)findViewById(R.id.month_plan_limit_mb);
        monthPlanLimitView.setText(Integer.valueOf(ConfigDataUtils.getMonthlyPlanBytes(cr)) *
                (Integer.valueOf(ConfigDataUtils.getMonthlyPlanLimitPercent(cr))) / 100 +
                "MB" + " " + ConfigDataUtils.getMonthlyPlanLimitPercent(cr) + "%");

        ImageButton backButton = (ImageButton)findViewById(R.id.return_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrafficSettingActivity.this.finish();
            }
        });

        percentSlider = (SeekBar)findViewById(R.id.month_plan_slider);
        if(ConfigDataUtils.getMonthlyPlanBytes(cr).equals("0")) {
            percentSlider.setActivated(false);
        } else {
            percentSlider.setProgress(Integer.valueOf(ConfigDataUtils.getMonthlyPlanLimitPercent(cr)));
        }

        percentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ConfigDataUtils.setMonthlyPlanLimitPercent(progress, cr);
                monthPlanLimitView.setText(Integer.valueOf(ConfigDataUtils.getMonthlyPlanBytes(cr)) *
                        progress / 100 + "MB" + " " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private void showPlanSetDlg(final int type) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title((type == UIConstants.MONTH_TYPE ? R.string.monthly_plan_setting_tips : R.string.day_plan_setting_tips))
                .customView(R.layout.input_dialog)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText et = (EditText) dialog.findViewById(R.id.bytes_num);
                        if (!et.getText().toString().equals("")) {
                            int num = Integer.valueOf(et.getText().toString());
                            if (type == UIConstants.MONTH_TYPE) {
                                ConfigDataUtils.setMonthlyPlanMBytes(num, cr);
                                monthPlanView.setText(num + "MB");
                                monthPlanLimitView.setText(num * (Integer.valueOf(ConfigDataUtils.getMonthlyPlanLimitPercent(cr))) / 100 +
                                        "MB" + " " + ConfigDataUtils.getMonthlyPlanLimitPercent(cr) + "%");
                            } else {
                                ConfigDataUtils.setLimitBytesForDay(num, cr);
                                dayPlanView.setText(num + "MB");
                            }
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();
        dialog.show();
    }

    private void showMonthlyUsedCorrectDlg() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title( R.string.monthly_used_correct_tips)
                .customView(R.layout.input_dialog)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText et = (EditText) dialog.findViewById(R.id.bytes_num);
                        if (!et.getText().toString().equals("")) {
                            int num = Integer.valueOf(et.getText().toString());
                            TrafficDataUtils monthlyTrafficBytesUpload = new TrafficDataUtils(
                                    NETWORK_TRAFFIC_TYPE_UPLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
                            TrafficDataUtils monthlyTrafficBytesDownload = new TrafficDataUtils(
                                    NETWORK_TRAFFIC_TYPE_DOWNLOAD, URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH, cr);
                            int usedUpload = (int)(monthlyTrafficBytesUpload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024);
                            int usedDownload = (int)(monthlyTrafficBytesDownload.getTrafficData(COLUMNS_MOBILE,cr)/1024/1024);
                            int correctUpload, correctDownload;
                            correctDownload = num -usedDownload -usedUpload;
                            if( (usedDownload + correctDownload) > 0 ) {
                                correctUpload = 0;
                            } else {
                                correctUpload = correctDownload + usedDownload;
                                correctDownload = -usedDownload;
                            }
                            ConfigDataUtils.setMonthlyUsedCorrect(correctDownload, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_DOWNLOAD, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_DOWNLOAD_ID, getContentResolver());
                            ConfigDataUtils.setMonthlyUsedCorrect(correctUpload, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_UPLOAD, NETWORK_TRAFFIC_MONTHLY_USED_CORRECT_UPLOAD_ID, getContentResolver());
                        }

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();
        dialog.show();
    }
}