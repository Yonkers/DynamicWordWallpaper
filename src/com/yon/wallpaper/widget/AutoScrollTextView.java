package com.yon.wallpaper.widget;

//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.util.AttributeSet;
//import android.view.Display;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//public class AutoScrollTextView extends TextView implements OnClickListener {
//	public final static String TAG = AutoScrollTextView.class.getSimpleName();
//
//	private float textLength = 0f;// 文本长度
//	private float viewWidth = 0f;
//	private float step = 0f;// 文字的横坐标
//	private float y = 0f;// 文字的纵坐标
//	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
//	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
//	public boolean isStarting = false;// 是否开始滚动
//	private Paint paint = null;// 绘图样式
//	private String text = "";// 文本内容
//	private float speed = 0.5f ;
//
//	public AutoScrollTextView(Context context) {
//		super(context);
//		initView();
//	}
//
//	public AutoScrollTextView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		initView();
//	}
//
//	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		initView();
//	}
//
//	private void initView() {
//		setOnClickListener(this);
//	}
//
//	public void init(WindowManager windowManager) {
//		paint = getPaint();
//		text = getText().toString();
//		textLength = paint.measureText(text);
//		viewWidth = getWidth();
//		if (viewWidth == 0) {
//			if (windowManager != null) {
//				Display display = windowManager.getDefaultDisplay();
//				viewWidth = display.getWidth();
//			}
//		}
//		step = textLength;
//		temp_view_plus_text_length = viewWidth + textLength;
//		temp_view_plus_two_text_length = viewWidth + textLength * 2;
//		y = getTextSize() + getPaddingTop();
//	}
//	public void setSpeed(float speed) {  
//        this.speed = speed;  
//    }
//	@Override
//	public Parcelable onSaveInstanceState() {
//		Parcelable superState = super.onSaveInstanceState();
//		SavedState ss = new SavedState(superState);
//
//		ss.step = step;
//		ss.isStarting = isStarting;
//
//		return ss;
//
//	}
//
//	@Override
//	public void onRestoreInstanceState(Parcelable state) {
//		if (!(state instanceof SavedState)) {
//			super.onRestoreInstanceState(state);
//			return;
//		}
//		SavedState ss = (SavedState) state;
//		super.onRestoreInstanceState(ss.getSuperState());
//
//		step = ss.step;
//		isStarting = ss.isStarting;
//	}
//
//	public static class SavedState extends BaseSavedState {
//		public boolean isStarting = false;
//		public float step = 0.0f;
//
//		SavedState(Parcelable superState) {
//			super(superState);
//		}
//
//		@Override
//		public void writeToParcel(Parcel out, int flags) {
//			super.writeToParcel(out, flags);
//			out.writeBooleanArray(new boolean[] { isStarting });
//			out.writeFloat(step);
//		}
//
//		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
//
//			public SavedState[] newArray(int size) {
//				return new SavedState[size];
//			}
//
//			@Override
//			public SavedState createFromParcel(Parcel in) {
//				return new SavedState(in);
//			}
//		};
//
//		private SavedState(Parcel in) {
//			super(in);
//			boolean[] b = null;
//			in.readBooleanArray(b);
//			if (b != null && b.length > 0)
//				isStarting = b[0];
//			step = in.readFloat();
//		}
//	}
//
//	public void startScroll() {
//		isStarting = true;
//		invalidate();
//	}
//
//	public void stopScroll() {
//		isStarting = false;
//		invalidate();
//	}
//
//	@Override
//	public void onDraw(Canvas canvas) {
//		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
//		if (!isStarting) {
//			return;
//		}
//		step += speed;// 0.5为文字滚动速度。
//		if (step > temp_view_plus_two_text_length)
//			step = textLength;
//		invalidate();
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (isStarting)
//			stopScroll();
//		else
//			startScroll();
//
//	}
//}

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * 单行文本跑马灯控件
 * 
 * @author admin
 * 
 */
public class AutoScrollTextView extends TextView implements OnClickListener ,OnTouchListener{
	public final static String TAG = AutoScrollTextView.class.getSimpleName();

	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 0f;// 文字的横坐标
	private float y = 0f;// 文字的纵坐标
	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	public boolean isStarting = false;// 是否开始滚动
	private Paint paint = null;// 绘图样式
	private CharSequence text = "";// 文本内容
	private float speed = 0.5f;
	private int textColor = 0xFF000000;

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int color) {
		this.textColor = color;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	

	@Override
	public void setTextSize(float size) {
		super.setTextSize(size);
		stopScroll();
		init(viewWidth);
		startScroll();
	}

	public AutoScrollTextView(Context context) {
		super(context);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	/** */
	/**
	 * 初始化控件
	 */
	private void initView() {
//		setOnClickListener(this);
		setOnTouchListener(this);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		init(viewWidth);
	}

	/** */
	/**
	 * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下!
	 */
	public void init(float width/* WindowManager windowManager */) {
		text = super.getText();
		paint = super.getPaint();
		// Paint paint=new Paint();
		text = getText().toString();
		textLength = paint.measureText(text.toString());
		// viewWidth = getWidth();
		// if (viewWidth == 0) {
		// if (windowManager != null) {
		// Display display = windowManager.getDefaultDisplay();
		// viewWidth = display.getWidth();
		// }
		// }
		viewWidth = width;
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
		y = getTextSize() + getPaddingTop();
		this.setHeight((int)y+10+getPaddingBottom()*2);
		paint.setColor(textColor);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;

	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;

	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if (b != null && b.length > 0)
				isStarting = b[0];
			step = in.readFloat();
		}
	}

	/** */
	/**
	 * 开始滚动
	 */
	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	/** */
	/**
	 * 停止滚动
	 */
	public void stopScroll() {
		isStarting = false;
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		// super.onDraw(canvas);

		canvas.drawText(text, 0, text.length(), temp_view_plus_text_length
				- step, y, paint);
		if (!isStarting) {
			return;
		}
		step += speed;
		if (step > temp_view_plus_two_text_length)
			step = textLength;
		invalidate();
	}

	@Override
	public void onClick(View v) {
//		if (isStarting)
//			stopScroll();
//		else
//			startScroll();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			if (isStarting)
				stopScroll();
			else
				startScroll();
			return true;
		}
		return false;
	}
	

}
