package com.palmwifi.testcal.dash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


import com.palmwifi.testcal.R;

import java.text.DecimalFormat;

/**
 * Created by David on 2016/11/10.
 */

public class DashboardView extends View {

    public static final int MAX_AMIN_TIME = 400;
    public static final int BORDER = 42;
    public static final int CENTER_CIRCLE_RADIUS = 35;
    public static final int TOTAL_ANGLE = 330;
    public static final int GRADUATED_LINE_PADDING = 0; //刻度线
    public static final int SCALE_VALUE_PADDING = 7; //刻度线
    public static final int SCALE_INV = 10; //每隔间距10画一个值 默认
    public static final int GRADUATED_INV = 2;//每隔间距2画一个刻度线 默认
    public static final float LINE_PERCENT = 0.6f;
    private Paint mArcPaint;
    private Paint mInvPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private int mTextHeight;
    private Paint mIntervalsPaint;
    private Paint mNumberPaint;
    private Paint mCenterCirclePaint;
    private int totalAngle = TOTAL_ANGLE;

    private int[] intervals = {35, 10, 35};
    private String[] intervalStr;
    private int angle = 0;
    private int startNum = 0;
    private int[] colorRes = {R.color.arc1, R.color.arc2, R.color.arc3};
    private int centerCircleRadius = CENTER_CIRCLE_RADIUS;
    private int circleBorder = BORDER;
    private int graduatedLineinv = GRADUATED_LINE_PADDING; //指针和线之间的间距
    private int scaleValueinv = SCALE_VALUE_PADDING; //数字和指针的间距
    private int graduatedLineLength = SCALE_VALUE_PADDING;//线的长度
    private int tempAngle = 0;
    private String unit = "℃";
    private int animAngle = 5;
    private int scaleValInv = SCALE_INV;
    private int graduatedInv = GRADUATED_INV;
    private float linePercent = LINE_PERCENT;
    private float angleTextSize;
    private float numberTextSize;
    private float graduatedTextSize;
    private int[] filterNumbers;
    private String drawNumText;
    public static DecimalFormat df = new DecimalFormat("#.0");
    private Path path;
    private RectF oval;


    public DashboardView(Context context) {
        super(context);
        initView();
    }

    public DashboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
        initView();
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(attrs);
        initView();
    }

    private void initAttr(AttributeSet attrs) {

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DashboardView);
        totalAngle = a.getInteger(R.styleable.DashboardView_totalAngle, TOTAL_ANGLE);
        centerCircleRadius = a.getDimensionPixelSize(R.styleable.DashboardView_centerCircleRadius, CENTER_CIRCLE_RADIUS);
        circleBorder = a.getDimensionPixelSize(R.styleable.DashboardView_arcBorderDimen, BORDER);
        angleTextSize = a.getDimension(R.styleable.DashboardView_angleTextSize, 25);
        numberTextSize = a.getDimension(R.styleable.DashboardView_numberTextSize, 25);
        graduatedTextSize = a.getDimension(R.styleable.DashboardView_graduatedTextSize, 25);
        linePercent = a.getFloat(R.styleable.DashboardView_pointLinePercent, 0.6f);
        a.recycle();
    }


   /* @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measure(widthMeasureSpec);
        int height = measure(heightMeasureSpec);
        int d = Math.min(width, height);
        setMeasuredDimension(d, d);
    }

    protected int measure(int measureSpec) {
        int size;
        int measureMode = MeasureSpec.getMode(measureSpec);
        if (measureMode == MeasureSpec.UNSPECIFIED) {
            size = 250;
        } else {
            size = MeasureSpec.getSize(measureSpec);
        }
        return size;

    }*/

    protected void initView() {


        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(circleBorder);
        mArcPaint.setStyle(Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mInvPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInvPaint.setAntiAlias(true);
        mInvPaint.setStrokeWidth(circleBorder + 1);
        mInvPaint.setColor(Color.WHITE);
        mInvPaint.setStyle(Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(getResources().getColor(R.color.arc_text));
        mTextPaint.setTextSize(numberTextSize);
        mTextPaint.setTypeface(Typeface.DEFAULT);

        mIntervalsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIntervalsPaint.setColor(Color.WHITE);
        mIntervalsPaint.setTextSize(graduatedTextSize);
        mIntervalsPaint.setTypeface(Typeface.DEFAULT);
        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint.setColor(getResources().getColor(R.color.arc_temp));
        mNumberPaint.setTextSize(angleTextSize);
        mNumberPaint.setTypeface(Typeface.DEFAULT);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(3f);
        mLinePaint.setStyle(Style.FILL);
        mLinePaint.setColor(getResources().getColor(R.color.arc_line));

        mCenterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterCirclePaint.setAntiAlias(true);
        mCenterCirclePaint.setStrokeWidth(4f);
        mCenterCirclePaint.setColor(getResources().getColor(R.color.arc_line));
        mCenterCirclePaint.setStyle(Style.STROKE);
        path = new Path();
        mTextHeight = (int) mLinePaint.measureText("NN");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int px = getMeasuredWidth() / 2;
        int py = getMeasuredHeight() / 2;
        int maxRadius = Math.min(px, py);
        py = maxRadius;
        int x = maxRadius / 10;
        int realRadius = maxRadius - x;
        if(oval == null) {
            oval= new RectF(x + (px - py), maxRadius / 10, maxRadius * 2 - x + (px - py), maxRadius * 2 - x);
        }
        if (px > py) {
            px = (px - py) + maxRadius;
        }
        float totalScores = 0f;
        for (int s : intervals) {
            totalScores += s;
        }
        float perAngle = totalAngle / totalScores;
        int startAngle = (360 - totalAngle) / 2 + 90;
        for (int i = 0; i < intervals.length; i++) {
            mArcPaint.setColor(ContextCompat.getColor(getContext(),i < colorRes.length ? colorRes[i] : colorRes[0]));
            int sweepAngle = (int) (perAngle * intervals[i]);
            canvas.drawArc(oval, startAngle, sweepAngle, false, mArcPaint);
            startAngle += sweepAngle;
            mArcPaint.setColor(Color.WHITE);
            if (i != 0) {
                canvas.drawArc(oval, startAngle - sweepAngle - 5, 5, false, mInvPaint);
            }
        }
        canvas.drawCircle(px, py, centerCircleRadius, mCenterCirclePaint);
        String text = (drawNumText != null ? drawNumText : angle) + unit;
        canvas.drawText(text, px - (text.length() > 3 ?mTextHeight*3 :mTextHeight), 2 * maxRadius - mTextHeight + 6, mNumberPaint);
        canvas.save();
        canvas.rotate(-totalAngle / 2, px, py);
        int invPos = 0;
        int inv = startNum;
        int graduatedLineY = py - realRadius + circleBorder - graduatedLineLength;
        for (int i = startNum; i <= totalScores + startNum; i++) {
            canvas.save();
            if (i % scaleValInv == 0) {
                boolean isDraw = true;
                if (filterNumbers != null) {
                    ok:
                    for (int n : filterNumbers) {
                        if (n == i) {
                            isDraw = false;
                            break ok;
                        }
                    }
                }
                if (isDraw) {
                    canvas.drawText(i + "", px - mTextHeight + 6, graduatedLineY + scaleValueinv + graduatedLineLength + graduatedLineinv +
                            mTextHeight, mTextPaint);//方位
                }
            }
            if (i % graduatedInv == 0) {
                canvas.drawLine(px, graduatedLineY, px, graduatedLineY + graduatedLineLength, mLinePaint);
            }
            if (i == angle) {
                canvas.drawLine(px, py - centerCircleRadius, px, (int) (py - maxRadius * linePercent), mCenterCirclePaint);
            }
            if (intervalStr != null && invPos < intervals.length) {
                if (i - 1 == inv + intervals[invPos] / 2) {
                    path.reset();
                    path.addArc(oval,-90- (intervalStr[invPos].length() >5 ? 20:10),180);
                    boolean isReduce = false;
                    if(intervalStr[invPos].length() >5 ){
                        mIntervalsPaint.setTextSize(mIntervalsPaint.getTextSize() -5);
                        isReduce = true;
                    }
                    canvas.drawTextOnPath(intervalStr[invPos], path, 0, circleBorder / 2 - mTextHeight / 2 + 6, mIntervalsPaint);
                    if(isReduce){
                        mIntervalsPaint.setTextSize(mIntervalsPaint.getTextSize() +5);
                    }
                    inv += intervals[invPos];
                    invPos++;
                }
            }
            canvas.restore();
            canvas.rotate(perAngle, px, py);

        }
        canvas.restore();
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
        invalidate();
    }

    public void setAngleWithAnim(int angle) {
        if (angle == startNum) {
            this.angle = angle;
            invalidate();
            return;
        }
        tempAngle = angle;
        animAngle = 5;
        post(animAngleRunnable);
    }

    public void setAngleWithAnim(double angle) {
        drawNumText =  df.format(angle);
        setAngleWithAnim((int)angle);
    }



    private Runnable animAngleRunnable = new Runnable() {


        @Override
        public void run() {
            if (tempAngle > angle) {
                angle += animAngle;
                if (angle > tempAngle) {
                    angle = tempAngle;
                    tempAngle = 0;
                }
            }
            invalidate();
            postDelayed(this, 50);
        }
    };

    /**
     * 初始化
     *
     * @param startNum     开始的值
     * @param eachInterval 每个区 间隔
     */
    public void initDash(int startNum, int[] eachInterval, String[] intervalStrs) {

        initDash(startNum, eachInterval, intervalStrs, null, null);
    }


    public void initDash(int startNum, int[] eachInterval, String[] intervalStrs, String unit, int[] colors) {

        initDash(startNum, eachInterval, intervalStrs, unit, colors, null);
    }

    /**
     * 初始化
     *
     * @param startNum     开始的值
     * @param eachInterval 每个区 间隔
     * @param colors       区间 颜色值
     */
    public void initDash(int startNum, int[] eachInterval, String[] intervalStrs, String unit, int[] colors, int[] filterNumbers) {

        this.startNum = startNum;
        intervals = eachInterval;
        if (colorRes != null) {
            this.colorRes = colors;
        }
        this.intervalStr = intervalStrs;
        if (unit != null) {
            this.unit = unit;
        }
        if (filterNumbers != null) {
            this.filterNumbers = filterNumbers;
        }
        invalidate();
    }


}
