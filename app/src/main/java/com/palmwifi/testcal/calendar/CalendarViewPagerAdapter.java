package com.palmwifi.testcal.calendar;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class CalendarViewPagerAdapter<V extends View> extends PagerAdapter {
	
	private V[] views;

	
	public CalendarViewPagerAdapter(V[] views) {
		super();
		this.views = views;
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		View childView = views[arg1 % views.length];
		if(childView.getParent() != null){
			((ViewGroup)childView.getParent()).removeView(childView);
		}
		((ViewPager) arg0).addView(childView, 0);

		return childView;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startUpdate(View arg0) {
	}

	
	public V[] getAllItems() {
		return views;
	}
}
