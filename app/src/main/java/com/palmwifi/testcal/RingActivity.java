package com.palmwifi.testcal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.palmwifi.testcal.dash.RingView;

/**
 * Created by David on 2017/4/13.
 */

public class RingActivity extends AppCompatActivity {

    private RingView tempDashView;

    private RingView tempDashView2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ring);
        tempDashView = (RingView) findViewById(R.id.humi);
        String[] str = {"差", "中", "好"};
        int[] colors = {R.color.arc1, R.color.arc2, R.color.arc3};
        tempDashView.setTotalSection(3);
        tempDashView.setSelectPosition(1);
        tempDashView.initDash(str,colors);
        tempDashView.startAnim(800);
        tempDashView2 = (RingView) findViewById(R.id.humi2);
        tempDashView2.setTotalSection(5);



    }
}
