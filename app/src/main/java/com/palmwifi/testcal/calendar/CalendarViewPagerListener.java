package com.palmwifi.testcal.calendar;


import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class CalendarViewPagerListener implements OnPageChangeListener {

	private SlideDirection mDirection = SlideDirection.NO_SILDE;
	int mCurrIndex = 498;
	private CalendarView[] mShowViews;
	private ViewPager mPager;
	private boolean isAutoClickFristDay = true;

	public CalendarViewPagerListener(ViewPager viewPager, CalendarViewPagerAdapter<CalendarView> viewAdapter) {
		this.mShowViews = viewAdapter.getAllItems();
		this.mPager = viewPager;
	}

	@Override
	public void onPageSelected(int arg0) {
		measureDirection(arg0);
		updateCalendarView(arg0);
	}

	private void updateCalendarView(int arg0) {
		CalendarView calendar = mShowViews[arg0 % mShowViews.length];
		if(mDirection == SlideDirection.RIGHT){
			calendar.rightSlide();
		}else if(mDirection == SlideDirection.LEFT){
			calendar.leftSlide();
		}
		mDirection = SlideDirection.NO_SILDE;
	}

	

	private void measureDirection(int arg0) {

		if (arg0 > mCurrIndex) {
			mDirection = SlideDirection.RIGHT;

		} else if (arg0 < mCurrIndex) {
			mDirection = SlideDirection.LEFT;
		}
		mCurrIndex = arg0;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	

	
	public CalendarView getCurrentCalendar(){
		return mShowViews[mPager.getCurrentItem() % mShowViews.length];
		
	}

	
	public void updateCalendar(){
		CalendarView calendar = mShowViews[mPager.getCurrentItem() % mShowViews.length];
		calendar.update();
	}
	
	public void backToday(){
		CalendarView calendar = mShowViews[mPager.getCurrentItem() % mShowViews.length];
		calendar.backToday();
	}
	


	enum SlideDirection {
		RIGHT, LEFT, NO_SILDE;
	}
}