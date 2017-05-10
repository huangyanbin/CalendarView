package com.palmwifi.testcal;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmwifi.testcal.calendar.CalendarView;
import com.palmwifi.testcal.calendar.CalendarViewBuilder;
import com.palmwifi.testcal.calendar.CalendarViewPagerAdapter;
import com.palmwifi.testcal.calendar.CalendarViewPagerListener;
import com.palmwifi.testcal.calendar.CustomDate;
import com.palmwifi.testcal.calendar.OnCalenderListener;
import com.palmwifi.testcal.calendar.RecordState;

public class CalendarActivity extends AppCompatActivity implements OnCalenderListener,View.OnClickListener {

    private ViewPager monthViewPager;
    private TextView mDateTv;
    private CalendarView[] monthViews;
    private CalendarViewPagerListener monthPagerListener;
    private int cellHeight;
    private ImageView leftImg;
    private ImageView rightImg;
    private int rows;
    private Handler mHandler = new Handler();
    private CustomDate mClickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthViewPager = (ViewPager) findViewById(R.id.viewpager);
        mDateTv = (TextView) findViewById(R.id.tv_show_date);
        monthViews = CalendarViewBuilder.createMonthCalendarViews(this, 5, this);
        leftImg = (ImageView) findViewById(R.id.img_cal_left);
        rightImg = (ImageView) findViewById(R.id.img_cal_right);

        setViewPager();
        leftImg.setOnClickListener(this);
        rightImg.setOnClickListener(this);
    }

    private void setViewPager() {

        CalendarViewPagerAdapter<CalendarView> viewPagerAdapter = new CalendarViewPagerAdapter<>(monthViews);
        monthViewPager.setAdapter(viewPagerAdapter);
        monthViewPager.setCurrentItem(498);
        monthPagerListener = new CalendarViewPagerListener(monthViewPager, viewPagerAdapter);
        monthViewPager.addOnPageChangeListener(monthPagerListener);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                monthViewPager.requestLayout();
            }
        }, 150);


    }

    @Override
    public void clickDate(CustomDate date) {
        if (mClickDate == null || !date.isSameDay(mClickDate)) {
            mClickDate = date;
            mDateTv.setText(date.year + "年" + date.month + "月");
        }
    }


    @Override
    public void onMeasureCellHeight(int cellSpace) {
        cellHeight = cellSpace;
        if (rows != 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) monthViewPager.getLayoutParams();
            lp.height = rows * cellHeight;
            monthViewPager.setLayoutParams(lp);
        }
    }


    @Override
    public void changeRowCount(int rows) {
        this.rows = rows;
        if (cellHeight != 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) monthViewPager.getLayoutParams();
            lp.height = rows * cellHeight;
            monthViewPager.setLayoutParams(lp);
        }
    }

    @Override
    public RecordState setSignDateStatus(CustomDate date) {
        if (date.getYear() == 2017 && date.getMonth() <= 4) {
            if (date.getDay() % 10 <= 3) {
                return RecordState.Sign;
            } else if (date.getDay() % 10 <= 6) {
                return RecordState.Buckle;
            } else {
                return RecordState.UnSign;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_cal_left:
                monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()-1);
                break;
            case R.id.img_cal_right:
                monthViewPager.setCurrentItem(monthViewPager.getCurrentItem()+1);
                break;
        }
    }
}
