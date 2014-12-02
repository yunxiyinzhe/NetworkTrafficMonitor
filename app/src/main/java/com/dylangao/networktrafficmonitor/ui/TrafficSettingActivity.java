package com.dylangao.networktrafficmonitor.ui;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dylangao.networktrafficmonitor.database.ConfigDataUtils;
import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.views.ButtonFlat;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dylangao.networktrafficmonitor.R;

public class TrafficSettingActivity extends FragmentActivity {
    private  ContentResolver cr;
    private TextView monthPlanView;
    private TextView dayPlanView;
    private Slider percentSlider;
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
        monthPlanView = (TextView)findViewById(R.id.month_plan_mb_view);
        dayPlanView = (TextView)findViewById(R.id.day_plan_mb_view);
        monthPlanView.setText(ConfigDataUtils.getMonthlyPlanBytes(cr) + "MB");
        dayPlanView.setText(ConfigDataUtils.getLimitBytesForDay(cr) + "MB");

        percentSlider = (Slider)findViewById(R.id.month_plan_slider);
        percentSlider.setOnValueChangedListener(new Slider.OnValueChangedListener(){
            @Override
            public void onValueChanged(int value) {
                ConfigDataUtils.setMonthlyPlanLimitPercent(value, cr);
                monthPlanLimitView.setText(Integer.valueOf(ConfigDataUtils.getMonthlyPlanBytes(cr)) *
                        value / 100 + "MB" + " " + value + "%");
            }
        });

        if(ConfigDataUtils.getMonthlyPlanBytes(cr).equals("0")) {
            percentSlider.setActivated(false);
        } else {
            percentSlider.setValue(Integer.valueOf(ConfigDataUtils.getMonthlyPlanLimitPercent(cr)));
        }

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

    }
    private void showPlanSetDlg(final int type) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title((type == UIConstants.MONTH_TYPE ? R.string.monthly_plan_setting_tips : R.string.day_plan_setting_tips))
                .customView(R.layout.plan_input_dialog)
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
}