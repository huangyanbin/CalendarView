package com.palmwifi.testcal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.palmwifi.testcal.dash.DashboardView;

/**
 * Created by David on 2017/4/13.
 */

public class DashBoardActivity extends AppCompatActivity {

    private DashboardView tempDashView;
    private DashboardView humDashView;
    private final static int invs[] = {35, 10, 35};
    private final static int[] colorRes = {R.color.arc1, R.color.arc2, R.color.arc3};
    private final static int invs1[] = {25, 50, 25};
    private final static int[] colorRes1 = {R.color.arc21, R.color.arc22, R.color.arc23};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dash_broad);
        tempDashView = (DashboardView) findViewById(R.id.temp);
        humDashView = (DashboardView) findViewById(R.id.humi);
        String[] str = getResources().getStringArray(R.array.mult_temp_dash);
        String[] str2 = getResources().getStringArray(R.array.mult_huim_dash);
        tempDashView.initDash(-20, invs, str, "℃", colorRes);
        humDashView.initDash(0, invs1, str2, "%", colorRes1);
        tempDashView.setAngleWithAnim(30);
        humDashView.setAngleWithAnim(70);



    }
}
