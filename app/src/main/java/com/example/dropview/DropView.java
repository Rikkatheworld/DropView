package com.example.dropview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DropView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "DropImageView";

    private List<Circle> circles = new ArrayList<>();
    private Context mContext;
    private int radius;
    //波纹画笔
    private Paint mPaint1;
    //圆圈画笔
    private Paint mPaint2;
    //字体画笔
    private Paint mPaint3;
    //描边画笔
    private Paint mPaint4;
    private boolean mIsRunning = false;
    //波纹默认的间隔时间 600
    private long mSpeed;
    private long mLastCreateTime;
    //波纹的最大半径
    private int mMaxRadius;
    //动效持续时间 2000
    private long mDuration;
    //中间字体的间隔 0%
    private String text;
    //起始坐标
    private int x;
    private int y;

    public DropView(Context context) {
        this(context, null);
        this.mContext = context;
//          init();
    }

    public DropView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public DropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        initTypedArray(mContext, attrs);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropImageView);
        if (typedArray != null) {
            int duration = typedArray.getInt(R.styleable.DropImageView_duration, 2000);
            mDuration = duration;

            int speed = typedArray.getInt(R.styleable.DropImageView_speed, 600);
            mSpeed = speed;

            String mtext = typedArray.getString(R.styleable.DropImageView_text);
            text = mtext;

            int stroke_color = typedArray.getColor(R.styleable.DropImageView_stroke_color, Color.WHITE);
            mPaint4.setColor(stroke_color);

            int wave_color = typedArray.getColor(R.styleable.DropImageView_wave_color, 0xFF008577);
            mPaint1.setColor(wave_color);

            int circle_color = typedArray.getColor(R.styleable.DropImageView_circle_color, Color.WHITE);
            mPaint2.setColor(circle_color);

            int wave_radius = typedArray.getInt(R.styleable.DropImageView_wave_radius, 500);
            mMaxRadius = wave_radius;

            int mradius = typedArray.getInt(R.styleable.DropImageView_radius, 250);
            radius = mradius;

            typedArray.recycle();
        }
    }

    private void init() {
        mPaint1 = new Paint();
        mPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint1.setStrokeWidth(3);
        mPaint1.setColor(0xFF0288D1);
        mPaint1.setAntiAlias(true);

        mPaint2 = new Paint();
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        mPaint2.setStrokeWidth(0);
        mPaint2.setColor(Color.WHITE);
        mPaint2.setAntiAlias(true);

        mPaint3 = new Paint();
        mPaint3.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint3.setTextAlign(Paint.Align.CENTER);

        mPaint4 = new Paint();
        mPaint4.setStyle(Paint.Style.STROKE);
        mPaint4.setColor(Color.RED);
        mPaint4.setStrokeWidth(10);
        mPaint4.setAntiAlias(true);
        mPaint4.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));

        mSpeed = 600;
        mDuration = 2000;
        text = "0%";
    }

    //支持wrap_content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        int mWidth = 500;
        int mHeight = 500;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    mWidth,
                    getContext().getResources()
                            .getDisplayMetrics());
            widthSize += getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    mHeight,
                    getContext().getResources()
                            .getDisplayMetrics());
            heightSize += getPaddingLeft() + getPaddingRight();
        }
        if (widthSize < heightSize) {
            heightSize = widthSize;
        } else {
            widthSize = heightSize;
        }
        setMeasuredDimension(widthSize, heightSize);

        // 当布局参数设置为wrap_content时，设置默认值
//        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
//
//            setMeasuredDimension(mWidth, mHeight);
//            // 宽 / 高任意一个布局参数为 wrap_content时，都设置默认值
//        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            setMeasuredDimension(mWidth, heightSize);
//        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            setMeasuredDimension(widthSize, mHeight);
//        }
        x = widthSize / 2;
        y = heightSize / 2;
        Log.e("xxxx", x + " " + y);
        Log.e("radius", radius + "");
        Log.e("textsize", "" + mPaint3.getTextSize());
        Log.e("MaxRadius", mMaxRadius + "");
        if (radius > x) {
            radius = x;
        }
//        radius = getMeasuredWidth() / 2 / 3;
        mPaint3.setTextSize(radius / 2);
//        mMaxRadius = 2 * radius;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("xxxx1", x + " " + y);
        Log.e("radius1", radius + "");
        Log.e("textsize1", "" + mPaint3.getTextSize());
        Log.e("MaxRadius1", mMaxRadius + "");

        int resId = canvas.saveLayer(0, 0, x * 2, y * 2, mPaint3, Canvas.ALL_SAVE_FLAG);
        canvas.drawText(text, x, y + (mPaint3.getTextSize() / 2 - 10), mPaint3);

        canvas.drawCircle(x, y, radius, mPaint2);

        canvas.drawCircle(x, y, radius, mPaint4);

        Iterator<Circle> iterator = circles.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint1.setAlpha(circle.getAlpha());
                mPaint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                canvas.drawCircle(x, y, circle.getCurrentRadius(), mPaint1);
            } else {
                iterator.remove();
            }
        }
        if (circles.size() > 0) {
            postInvalidateDelayed(10);
        }
    }


    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Paint getmPaint1() {
        return mPaint1;
    }

    public void setmPaint1(Paint mPaint1) {
        this.mPaint1 = mPaint1;
    }

    public Paint getmPaint2() {
        return mPaint2;
    }

    public void setmPaint2(Paint mPaint2) {
        this.mPaint2 = mPaint2;
    }

    public Paint getmPaint3() {
        return mPaint3;
    }

    public void setmPaint3(Paint mPaint3) {
        this.mPaint3 = mPaint3;
    }

    public Paint getmPaint4() {
        return mPaint4;
    }

    public void setmPaint4(Paint mPaint4) {
        this.mPaint4 = mPaint4;
    }

    public long getmSpeed() {
        return mSpeed;
    }

    public void setmSpeed(long mSpeed) {
        this.mSpeed = mSpeed;
    }

    public int getmMaxRadius() {
        return mMaxRadius;
    }

    public void setmMaxRadius(int mMaxRadius) {
        this.mMaxRadius = mMaxRadius;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getx() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int gety() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mCreateCircle.run();
        }
    }

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        circles.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }


    private class Circle {
        private long mCreateTime;

        public Circle() {
            this.mCreateTime = System.currentTimeMillis();
        }

        public int getAlpha() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return (int) ((1.0f - percent) * 255);
        }

        public float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return radius + percent * (mMaxRadius - radius);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsRunning = false;
    }
}