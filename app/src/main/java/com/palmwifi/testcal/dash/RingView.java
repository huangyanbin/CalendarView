package com.palmwifi.testcal.dash;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;


import com.palmwifi.testcal.R;


/**
 * Created by David on 2016/11/10.
 */

public class RingView extends View {

    public static final int BORDER = 42;
    public static final int TOTAL_ANGLE = 360;

    private Paint mArcPaint;
    private int mTextHeight;
    private Paint mIntervalsPaint;
    private Paint mCenterCirclePaint;
    private int totalAngle = TOTAL_ANGLE;
    private int totalSection = 3;
    private String[] TextStrArray;
    private int[] colorRes;
    private int defaultColor;
    private int bgColor;
    private int selectTextColor;
    private int unSelectTextColor;
    private int circleBorder = BORDER;
    private Point centerPoint;
    private int centerRadius;
    private float centerTextSize;
    private int selectPosition;
    private RectF oval;
    private RectF oval2;
    private int touchSlop;
    private Path path;
    private int tempTotalAngle;
    private int animTime = 20;
    private Paint mShadowPaint;
    private boolean isAnim;
    private OnDashItemClickListener itemClickListener;


    public RingView(Context context) {
        super(context);
        initView();
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
        initView();
    }

    public RingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(attrs);
        initView();
    }

    private void initAttr(AttributeSet attrs) {

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RingView);
        defaultColor = a.getColor(R.styleable.RingView_arc_select_color, ContextCompat.getColor(getContext(), R.color.selectColor));
        selectTextColor = a.getColor(R.styleable.RingView_arcSelectTextColor, Color.WHITE);
        bgColor = a.getColor(R.styleable.RingView_arc_bg_color, ContextCompat.getColor(getContext(), R.color.arc_bg));
        unSelectTextColor = a.getColor(R.styleable.RingView_arcTextColor, ContextCompat.getColor(getContext(), R.color.arc_text));
        circleBorder = a.getDimensionPixelSize(R.styleable.RingView_arcBorder, BORDER);
        centerTextSize = a.getDimension(R.styleable.RingView_arcTextSize, 25);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        a.recycle();
    }


    protected void initView() {


        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(circleBorder);
        mArcPaint.setStyle(Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.BUTT);
        mIntervalsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIntervalsPaint.setColor(Color.BLACK);
        mIntervalsPaint.setTextSize(centerTextSize);
        mIntervalsPaint.setTypeface(Typeface.DEFAULT);
        mCenterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterCirclePaint.setAntiAlias(true);
        mCenterCirclePaint.setColor(bgColor);
        mCenterCirclePaint.setStyle(Style.STROKE);
        mTextHeight = (int) mIntervalsPaint.measureText("NN");
        mShadowPaint= new Paint(Paint.ANTI_ALIAS_FLAG); //初始化画笔，为后面阴影效果使用。
        //mShadowPaint.setStyle(Style.STROKE);
       // mShadowPaint.setStrokeWidth(5);
        //mShadowPaint.setShadowLayer(10f, 5.0f, 5.0f, Color.parseColor("#f2f2f2")); //设置阴影层，这是关键。

        path = new Path();
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
            case MotionEvent.ACTION_MOVE:
              /*  int  disX = (int) (event.getX() - mDownX);
                int  disY = (int) (event.getY() - mDownY);
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {*/
                int clickX = (int) event.getX();
                int clickY = (int) event.getY();
                int x = clickX - centerPoint.x;
                int y = clickY - centerPoint.y;
                int z = (int) Math.sqrt(Math.pow(Math.abs(x), 2) + Math.pow(Math.abs(y), 2));
                if (z >= centerRadius - circleBorder / 2 && z <= centerRadius + circleBorder + 20) {
                    double angle = Math.abs(Math.toDegrees(Math.atan((event.getY() - centerPoint.y) / (event.getX() - centerPoint.x))));
                    if (x >= 0 && y < 0) {
                        angle = 90 - angle;
                    } else if (x >= 0 && y >= 0) {
                        angle = 90 + angle;
                    } else if (x < 0 && y >= 0) {
                        angle = 270 - angle;
                    } else {
                        angle = 270 + angle;
                    }
                    int selectPosition = (int) (angle / (totalAngle / totalSection));
                    setSelectPosition(selectPosition);
                }

                /*}*/
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float perAngle = totalAngle / totalSection;
        float startAngle = -90;
        if (centerPoint == null) {
            int px = getMeasuredWidth() / 2;
            int py = getMeasuredHeight() / 2;
            int maxRadius = Math.min(px, py);
            py = maxRadius;
            int margin = maxRadius / 5;

            if (px > py) {
                px = (px - py) + maxRadius;
            }
            centerPoint = new Point(px, py);
            oval = new RectF(margin + (px - py), margin, maxRadius * 2 - margin + (px - py), maxRadius * 2 - margin);
            oval2 = new RectF(oval.left-circleBorder, oval.top -circleBorder,oval.right+circleBorder, oval.bottom+circleBorder);
            centerRadius = maxRadius - margin;
            float innerRadius = (centerRadius-circleBorder/2)/((float)(centerRadius + circleBorder));
            float outRadius = (centerRadius+circleBorder/2)/((float)(centerRadius + circleBorder));
            RadialGradient gradient = new RadialGradient(px,py,centerRadius+circleBorder,new int[]{0x00ffffff,0xffd4d4d4,0xffd4d4d4,0x00ffffff},new float[]{innerRadius- (1-outRadius)/3,innerRadius,outRadius,outRadius+(1-outRadius)/3}, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(gradient);
        }

        mCenterCirclePaint.setStrokeWidth(circleBorder);
        if(!isAnim) {
            canvas.drawArc(oval2, startAngle + perAngle * selectPosition, totalAngle, false, mShadowPaint);
        }
        canvas.drawArc(oval, startAngle +perAngle*selectPosition, totalAngle, false, mCenterCirclePaint);

        for (int i = 0; i < totalSection; i++) {
            if (i == selectPosition) {
                int selectColor = (colorRes == null ? defaultColor : ContextCompat.getColor(getContext(), colorRes[i]));
                mArcPaint.setColor(selectColor);
                canvas.drawArc(oval, startAngle, perAngle, false, mArcPaint);

            }else {
                mArcPaint.setColor(ContextCompat.getColor(getContext(),R.color.arc_inteval));
                canvas.drawArc(oval, startAngle, 0.5f, false, mArcPaint);
            }
            path.reset();
            path.addArc(oval, startAngle+perAngle/2, perAngle);
            mIntervalsPaint.setColor(i == selectPosition ? selectTextColor : unSelectTextColor);

            if (TextStrArray != null) {
                boolean isReduce = false;
                if (TextStrArray[i].length() > 5) {
                    mIntervalsPaint.setTextSize(mIntervalsPaint.getTextSize() - 5);
                    isReduce = true;
                }
                if (isReduce) {
                    mIntervalsPaint.setTextSize(mIntervalsPaint.getTextSize() + 5);
                }
                canvas.drawTextOnPath(TextStrArray[i], path, 0, circleBorder / 2 - mTextHeight / 2 + 6, mIntervalsPaint);
            } else {
                canvas.drawTextOnPath((i + 1) + "", path, 0, circleBorder / 2 - mTextHeight / 2, mIntervalsPaint);
            }
            startAngle += perAngle;
        }
    }



    public void initDash(String[] intervalStrs, int[] colors) {
        this.TextStrArray = intervalStrs;
        this.colorRes = colors;

    }


    public void startAnim(final int anim) {
        tempTotalAngle = totalAngle;
        totalAngle =1;
        isAnim = true;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,tempTotalAngle);
        valueAnimator.setDuration(anim);
        valueAnimator.setInterpolator(new DecelerateInterpolator());//减速差值器
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                totalAngle = (int) animation.getAnimatedValue();
                if(totalAngle <1){
                    totalAngle =1;

                }
             if(tempTotalAngle == totalAngle){
                 isAnim = false;
             }
                invalidate();
            }
        });
        valueAnimator.start();

    }

    public void start(final int anim) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,totalSection);
        valueAnimator.setDuration(anim);
        valueAnimator.setRepeatCount(100);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //valueAnimator.setInterpolator(new DecelerateInterpolator());//减速差值器
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                selectPosition = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();

    }


    public void setOnDashItemClickListener(OnDashItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public void setSelectPosition(int selectPosition) {

        if (this.selectPosition != selectPosition) {
            this.selectPosition = selectPosition;
            if (itemClickListener != null) {
                itemClickListener.onItemClick(this, selectPosition);
            }
            invalidate();
        }


    }

    public void setTotalSection(int totalSection) {
        this.totalSection = totalSection;
    }


    public interface OnDashItemClickListener {

        void onItemClick(RingView dashView, int position);
    }
}
