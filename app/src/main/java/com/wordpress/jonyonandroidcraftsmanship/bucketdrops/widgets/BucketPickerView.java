package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BucketPickerView extends LinearLayout implements View.OnTouchListener {
    private TextView tvMonth = null;
    private TextView tvDate = null;
    private TextView tvYear = null;
    private Calendar mCalendar = null;
    private SimpleDateFormat mFormatter = null;
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    private boolean mIncrement = false;
    private boolean mDecrement = false;
    public static final int DELAY = 250;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mIncrement) {
                increment(mActiveId);
            }
            if (mDecrement) {
                decrement(mActiveId);
            }
            if (mIncrement||mDecrement) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
            }
            return true;
        }
    });
    private int MESSAGE_WHAT = 123;
    private int mActiveId = 0;

    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view, this);
        mCalendar = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvYear = (TextView) findViewById(R.id.tvYear);
        tvDate.setOnTouchListener(this);
        tvMonth.setOnTouchListener(this);
        tvYear.setOnTouchListener(this);
        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        update(date, month, year, 0, 0, 0);
    }

    private void update(int date, int month, int year, int hour, int minute, int second) {
        mCalendar.set(Calendar.DATE, date);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);
        tvDate.setText(date + "");
        tvMonth.setText(mFormatter.format(mCalendar.getTime()));
        tvYear.setText(year + "");
    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.tvDate:
                processEventsFor(tvDate, event);
                break;
            case R.id.tvMonth:
                processEventsFor(tvMonth, event);
                break;
            case R.id.tvYear:
                processEventsFor(tvYear, event);
                break;
        }
        return true;
    }

    private void processEventsFor(TextView textView, MotionEvent event) {
        Drawable[] drawables = textView.getCompoundDrawables();
        if (hasDrawableTop(drawables) && hasDrawableBottom(drawables)) {
            Rect topBounds = drawables[TOP].getBounds();
            Rect bottomBounds = drawables[BOTTOM].getBounds();
            float x = event.getX();
            float y = event.getY();
            mActiveId = textView.getId();
            if (topDrawableHit(textView, topBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    mIncrement = true;
                    increment(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                }
                if (isActionUpOrCancel(event)) {
                    mIncrement = false;
                }
            } else if (bottomDrawableHit(textView, bottomBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    mDecrement = true;
                    decrement(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                }
                if (isActionUpOrCancel(event)) {
                    mDecrement = false;
                }
            } else {
                mIncrement = false;
                mDecrement = false;
            }
        }
    }

    private void increment(int id) {
        switch (id) {
            case R.id.tvDate:
                mCalendar.add(Calendar.DATE, 1);
                break;
            case R.id.tvMonth:
                mCalendar.add(Calendar.MONTH, 1);
                break;
            case R.id.tvYear:
                mCalendar.add(Calendar.YEAR, 1);
                break;
        }
        set(mCalendar);
    }

    private void decrement(int id) {
        switch (id) {
            case R.id.tvDate:
                mCalendar.add(Calendar.DATE, -1);
                break;
            case R.id.tvMonth:
                mCalendar.add(Calendar.MONTH, -1);
                break;
            case R.id.tvYear:
                mCalendar.add(Calendar.YEAR, -1);
                break;
        }
        set(mCalendar);
    }

    private void set(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        tvDate.setText(date + "");
        tvYear.setText(year + "");
        tvMonth.setText(mFormatter.format(calendar.getTime()));
    }

    private boolean isActionDown(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_DOWN;
    }

    private boolean isActionUpOrCancel(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL;
    }

    private boolean topDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymin = textView.getPaddingTop();
        int ymax = textView.getPaddingTop() + drawableHeight;
        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean bottomDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymax = textView.getHeight() - textView.getPaddingBottom();
        int ymin = ymax - drawableHeight;
        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean hasDrawableTop(Drawable[] drawables) {
        return drawables[TOP] != null;
    }

    private boolean hasDrawableBottom(Drawable[] drawables) {
        return drawables[BOTTOM] != null;
    }
}
