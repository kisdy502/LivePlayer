package com.fm.lvplay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.List;


/**
 * JumpingView.class
 *
 * @author vio_wang
 * @desc
 * @date 2018/2/26
 */
public class JumpingView extends View {
	private static final String TAG = JumpingView.class.getSimpleName();

	private static final int NUM_NOTES = 4;
	private static final int INCREMENT = 2;
	private static final int DIF = 12;

	private static final int[] DIFS = new int[]{0, DIF * 3, DIF, DIF * 5};

	private boolean isRunning;
	private boolean hasInitSize;

	private float mHeight;

	private int mNoteWidth;
	private int baseX = 0;
	private int mSmallWidth;
	private int mSmallHeight;

	private Paint mPaint;

	private List<FPoint> mStartPoints;

	public JumpingView(Context context) {
		this(context, null);
	}

	public JumpingView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public JumpingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPaint();
		initAttrs(context, attrs);
		initValues(mSmallWidth, mSmallHeight);
		initPoints();
		hasInitSize = true;
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JumpingView);
		mSmallWidth = ta.getDimensionPixelOffset(R.styleable.JumpingView_small_width, 0);
		mSmallHeight = ta.getDimensionPixelOffset(R.styleable.JumpingView_small_height, 0);
		ta.recycle();
	}

	private void initPoints() {
		mStartPoints = new ArrayList<FPoint>();
		for (int i = 0; i < NUM_NOTES; i++) {
			FPoint startPoint = new FPoint();
			float x = (float) (((2 * i) + 0.5) * mNoteWidth);
			startPoint.set(x, 0);
			mStartPoints.add(startPoint);
		}
	}

	private void initValues(int width, int height) {
		mNoteWidth = width / (NUM_NOTES * 2 - 1);
		mPaint.setStrokeWidth(mNoteWidth * 0.9f);
		mHeight = (float) height;
	}

	class RenderTask extends Thread {
		@Override public void run() {
			super.run();
			try {
				if (!hasInitSize) {
					Thread.sleep(150);
					initPoints();
				}
				if (mStartPoints == null) {
					initPoints();
				}
				drawUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void drawUI() throws Exception {
		synchronized (this) {
			while (isRunning) {
				postInvalidate();
				Thread.sleep(50);
			}
		}
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		try {
			if (ArrayUtils.isEmpty(mStartPoints) || mStartPoints.size() != NUM_NOTES) {
				return;
			}
			for (int i = 0; i < NUM_NOTES; i++) {
				baseX += INCREMENT;
				canvas.drawLine(mStartPoints.get(i).x,
						mHeight,
						mStartPoints.get(i).x,
						mHeight * 3 / 4 - getHeight(DIFS[i] + baseX) * mHeight * 3 / 4,
						mPaint);
			}
		} catch (Exception e) {
			Log.e(TAG, "Exception : " + e.getMessage());
		}

	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mSmallWidth, mSmallHeight);
	}

	private float getHeight(int x) {
		return (float) (Math.sin(Math.PI * (double) x / 100) + 1) / 2;
	}

	public void setColor(int color) {
		int c = getResources().getColor(color);
		if (mPaint.getColor() != c) {
			mPaint.setColor(c);
		}
	}

	@Override public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == VISIBLE) {
			if (!isRunning) {
				isRunning = true;
				new RenderTask().start();
			}
		} else {
			mStartPoints = null;
			isRunning = false;
		}
	}
}