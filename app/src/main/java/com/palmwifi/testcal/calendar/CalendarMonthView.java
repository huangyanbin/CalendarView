package com.palmwifi.testcal.calendar;


import android.content.Context;
import android.util.AttributeSet;


public class CalendarMonthView extends CalendarView {

    private static final int TOTAL_COL = 7;
    private static final int TOTAL_ROW = 6;

    public static CustomDate mShowDate;
    public static CustomDate mClickDate;
    public static CustomDate mClickHideDate;
    private OnCalenderListener mCallBack;


    public CalendarMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CalendarMonthView(Context context, OnCalenderListener callBack) {
        super(context);
        this.mCallBack = callBack;
        initDate();

    }

    public CalendarMonthView(Context context) {
        super(context);
        initDate();

    }

    @Override
    protected void initDate() {
        mShowDate = new CustomDate();
        fillDate();
    }


    @Override
    public void measureClickCell(int col, int row) {
        if (col >= TOTAL_COL || row >= TOTAL_ROW)
            return;
        Cell cell = rows[row].cells[col];
        if (cell == null || cell.state == State.PAST_MONTH_DAY || cell.state == State.NEXT_MONTH_DAY) {
            return;
        }
        CustomDate date = cell.date;
        mClickDate = date;
        mClickHideDate = date;
        fillMonthDate(false);
        invalidate();
    }


    private void fillDate() {
        fillMonthDate(true);
    }


    private void fillMonthDate(boolean isChangeClick) {

        int monthDay = DateUtil.getCurrentMonthDay();
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
            if (isChangeClick) {
                mClickHideDate = CustomDate.modifiDayForObject(mShowDate, monthDay, 0);
                mClickDate = CustomDate.modifiDayForObject(mShowDate, monthDay, 0);
            }

        } else {
            if (isChangeClick) {
                if(isAutoClickFirstDay())
                    mClickDate = CustomDate.modifiDayForObject(mShowDate, 1, 0);
                    mClickHideDate = CustomDate.modifiDayForObject(mShowDate, monthDay, 0);
            }
        }
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            rows[j] = new Row(j);
            for (int i = 0; i < TOTAL_COL; i++) {
                int position = i + j * TOTAL_COL;
                CustomDate date;
                if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
                    day++;
                    if (day == currentMonthDays) {
                        int maxRow = j + 1;
                        if (mCallBack != null)
                            mCallBack.changeRowCount(maxRow);
                    }
                    date = CustomDate.modifiDayForObject(mShowDate, day, i);
                    RecordState state = getRecordDateState(date);
                    if (date.isSameDay(mClickDate)) {
                        mClickDate.setWeek(i);
                        if (mCallBack != null)
                            mCallBack.clickDate(date);
                        updateCellData(j, i, date, State.CLICK_DAY, state);
                        continue;
                    }
                    if(date.isSameDay(mClickHideDate)){
                        mClickHideDate.setWeek(i);
                        if (mCallBack != null)
                            mCallBack.showDate(date);
                    }
                    if (isCurrentMonth && day == monthDay) {
                        updateCellData(j, i, date, State.TODAY, state);
                        continue;
                    }
                    updateCellData(j, i, date, State.CURRENT_MONTH_DAY, state);
                } else if (position < firstDayWeek) {
                    date = new CustomDate(mShowDate.year, mShowDate.month - 1, lastMonthDays - (firstDayWeek - position - 1), i);
                    updateCellData(j, i, date, State.PAST_MONTH_DAY, RecordState.Unkown);
                } else if (position >= firstDayWeek + currentMonthDays) {
                    date = new CustomDate(mShowDate.year, mShowDate.month + 1, position - firstDayWeek - currentMonthDays + 1, i);
                    updateCellData(j, i, date, State.NEXT_MONTH_DAY, RecordState.Unkown);
                }
            }
        }
    }


    @Override
    public void update() {
        fillMonthDate(false);

        invalidate();
    }

    @Override
    public void backToday() {
        initDate();
        invalidate();
    }

    @Override
    public void update(CustomDate showDate, CustomDate clickDate) {

        mShowDate = showDate;
        mClickDate = clickDate;
        mClickHideDate = clickDate;
        fillMonthDate(false);
        invalidate();
    }

    @Override
    public CustomDate getClickDate() {
        return mClickDate;
    }


    @Override
    public RecordState getRecordDateState(CustomDate date) {
        RecordState state = null;
        if (mCallBack != null) {
            state = mCallBack.setSignDateStatus(date);
        }
        return state != null ? state : RecordState.UnSign;
    }



    public void rightSlide() {
        if (mShowDate.month == 12) {
            mShowDate.month = 1;
            mShowDate.year += 1;
        } else {
            mShowDate.month += 1;
        }

        fillDate();
        invalidate();
    }

    public void leftSlide() {
        if (mShowDate.month == 1) {
            mShowDate.month = 12;
            mShowDate.year -= 1;
        } else {
            mShowDate.month -= 1;
        }

        fillDate();
        invalidate();
    }


    public void setOnCalenderListener(OnCalenderListener callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public OnCalenderListener getOnCalenderListener() {
        return mCallBack;
    }


}
