package com.palmwifi.testcal.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.palmwifi.testcal.R;


public abstract class CalendarView extends View {


    public static final int TOTAL_COL = 7;
    public static final int TOTAL_ROW = 6;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mRectPaint;
    private int mCellSpace;
    private int mCellSpaceY;
    public Row rows[] = new Row[TOTAL_ROW];
    private int touchSlop;
    private boolean callBackCellSpace;
    public static CustomDate NowDate = new CustomDate();
    private boolean isNeedShowClickDay = true;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarView(Context context) {
        super(context);
        init(context);

    }

    public abstract void update();

    public abstract void backToday();

    public abstract void update(CustomDate showDate, CustomDate clickDate);

    public abstract CustomDate getClickDate();

    public abstract CustomDate getShowDate();

    public abstract RecordState getRecordDateState(CustomDate date);

    public abstract int getClickRow();

    public abstract void rightSlide();

    public abstract void leftSlide();

    public abstract void setOnCalenderListener(OnCalenderListener callBack);

    protected abstract void measureClickCell(int col, int row);

    public abstract OnCalenderListener getOnCalenderListener();

    protected abstract void initDate();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < TOTAL_ROW; i++) {
            if (rows[i] != null) {
                rows[i].drawCells(canvas);
            }
        }
    }

    private void init(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mCellSpaceY = getResources().getDimensionPixelOffset(R.dimen.calendar_view_height);
        mCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.cal_sign_color));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int mViewWidth = w;
        mCellSpace = mViewWidth / TOTAL_COL;
        if (!callBackCellSpace) {
            if (getOnCalenderListener() != null)
                getOnCalenderListener().onMeasureCellHeight(mCellSpaceY);
            callBackCellSpace = true;
        }
        mTextPaint.setTextSize(mCellSpace / 3);
    }

    private float mDownX;
    private float mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mCellSpace);
                    int row = (int) (mDownY / mCellSpaceY);
                    measureClickCell(col, row);
                }
                break;
        }
        return true;
    }

    class Row {
        public int j;

        Row(int j) {
            this.j = j;
        }

        public Cell[] cells = new Cell[TOTAL_COL];

        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null) {
                    Cell nextCell = null;
                    Cell perCell = null;
                    if(i+1 < cells.length){
                        nextCell = cells[i+1];
                    }
                    if(i -1 >= 0){
                        perCell = cells[i-1];
                    }
                    cells[i].drawSelf(canvas,perCell,nextCell);
                }
            }

        }
    }


    class Cell {
        public CustomDate date;
        public State state;
        public RecordState recordState;
        public int i;
        public int j;

        public Cell(CustomDate date, State state, RecordState recordState, int i, int j) {
            this.date = date;
            this.state = state;
            this.i = i;
            this.j = j;
            this.recordState = recordState;
        }

        public void update(CustomDate date, State state, RecordState recordState) {
            this.date = date;
            this.state = state;
            this.recordState = recordState;
        }

        public void drawSelf(Canvas canvas,Cell perCell,Cell nextCell) {
            if (state == State.NEXT_MONTH_DAY || state == State.PAST_MONTH_DAY) {
                return;
            }
            boolean isSign = false;
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.drak_calendar_text_color));
            switch (recordState) {
                case Sign:
                    mRectPaint.setColor(ContextCompat.getColor(getContext(), R.color.cal_sign_color));
                    mTextPaint.setColor(Color.WHITE);
                    isSign = true;
                    mCirclePaint.setColor(mRectPaint.getColor());
                    break;
                case Buckle:
                    mRectPaint.setColor(ContextCompat.getColor(getContext(), R.color.cal_buckle_color));
                    mCirclePaint.setColor(mRectPaint.getColor());
                    mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.cal_buckle_text_color));
                    break;
                case UnSign:
                    mCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.cal_safe_color));
                    mRectPaint.setColor(Color.TRANSPARENT);
                    break;
                case Unkown:
                    mCirclePaint.setColor(Color.TRANSPARENT);
                    mRectPaint.setColor(Color.TRANSPARENT);
                    break;
            }
            float x = (float) (mCellSpace * (i + 0.5));
            float y = (float) ((j + 0.5) * mCellSpaceY);
            String content = date.day + "";
            int radius = mCellSpace / 2 - 20;
            boolean isNeedRect = (isSign && i != 6 && nextCell != null && (nextCell.state == State.CURRENT_MONTH_DAY
                    ||nextCell.state == State.CLICK_DAY ||nextCell.state == State.TODAY)
                    && nextCell.recordState == RecordState.Sign);
            if(isNeedRect){
                canvas.drawRect(x,y-radius,x+mCellSpace,y+radius,mRectPaint);
            }
            switch (state) {
                case CURRENT_MONTH_DAY:
                    canvas.drawCircle(x, y, radius, mRectPaint);
                    break;
                case TODAY:
                    canvas.drawCircle(x, y, radius, mRectPaint);
                    if(isNeedShowClickDay) {
                        content = "今";
                    }
                    break;
                case CLICK_DAY:
                    if(isNeedShowClickDay){
                        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.base_black_title_color));
                        mCirclePaint.setStyle(Paint.Style.STROKE);
                        mCirclePaint.setStrokeWidth(4);
                        if (date.isSameDay(NowDate)) {
                            content = "今";
                        }
                        if(isSign){
                            mRectPaint.setColor(Color.WHITE);
                            canvas.drawCircle(x, y, radius, mRectPaint);
                        }
                        canvas.drawCircle(x, y, radius, mCirclePaint);
                    }else{
                        canvas.drawCircle(x, y, radius, mRectPaint);
                    }
                    break;
                default:
                    canvas.drawCircle(x, y, radius, mRectPaint);
                    break;
            }
            canvas.drawText(content, x - mTextPaint.measureText(content) / 2, y + mTextPaint.measureText("1", 0, 1) * 2 / 3, mTextPaint);
        }
    }

    protected void updateCellData(int row, int col, CustomDate date, State state, RecordState mState) {
        if (rows[row].cells[col] != null) {
            rows[row].cells[col].update(date, state, mState);
        } else {
            rows[row].cells[col] = new Cell(date, state, mState, col, row);
        }

    }


    enum State {
        CURRENT_MONTH_DAY, TODAY, CLICK_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY;
    }

}
