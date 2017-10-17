package com.palmwifi.testcal.calendar;

import android.content.Context;


public class CalendarViewBuilder {


	public static CalendarView[] createMonthCalendarViews(Context context,
			int count, OnCalenderListener onCalenderListener) {
		return createMonthCalendarViews(context,count,true,onCalenderListener);
	}

	public static CalendarView[] createMonthCalendarViews(Context context,
														  int count,boolean isAutoClickFirstDay, OnCalenderListener onCalenderListener) {
		CalendarView[] calendarViews = new CalendarView[count];
		for (int i = 0; i < count; i++) {

			calendarViews[i] = new CalendarMonthView(context,onCalenderListener);
			calendarViews[i].setAutoClickFirstDay(isAutoClickFirstDay);
		}
		return calendarViews;
	}



}
