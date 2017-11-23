package com.palmwifi.testcal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by David on 2017/5/10.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home);
        findViewById(R.id.btn_new_month_cal).setOnClickListener(this);
        findViewById(R.id.btn_new_week_cal).setOnClickListener(this);
        findViewById(R.id.btn_cal).setOnClickListener(this);
        findViewById(R.id.btn_round_board).setOnClickListener(this);
        findViewById(R.id.btn_dash_board).setOnClickListener(this);
        findViewById(R.id.btn_dash_board).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()){
            case R.id.btn_new_month_cal:
                i = new Intent(this,CalendarMonthActivity.class); //日历
                break;
            case R.id.btn_new_week_cal:
                i = new Intent(this,CalendarWeekActivity.class); //日历
                break;
            case R.id.btn_cal:
                i = new Intent(this,CalendarActivity.class); //日历
                break;
            case R.id.btn_dash_board:
                i = new Intent(this,DashBoardActivity.class); //仪表盘
                break;
            case R.id.btn_round_board:
                i = new Intent(this,RingActivity.class); //圆盘
                break;

        }
        if(i  != null){
            startActivity(i);
        }
    }
}
