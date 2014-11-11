package com.dylangao.networktrafficmonitor.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dylangao.networktrafficmonitor.R;

public class TrafficSettingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_setting_layout);
        Switch switchButton = (Switch)findViewById(R.id.traffic_monitor_switch);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        Button monthPlanButton = (Button)findViewById(R.id.month_plan_button);
        monthPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonthlyPlanSetDlg();
            }
        });

    }
    private void showMonthlyPlanSetDlg() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.monthly_plan_setting_title)
                .customView(R.layout.monthly_plan_dialog)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();
        dialog.show();
    }
}