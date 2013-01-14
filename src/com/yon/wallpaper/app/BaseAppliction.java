package com.yon.wallpaper.app;

import java.util.List;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.yon.wallpaper.R;
import com.yon.wallpaper.bean.Word;
import com.yon.wallpaper.db.DataHelper;

public class BaseAppliction extends Application implements OnClickListener {
	private static String TAG = "BaseAppliction";
	
	private static BaseAppliction app ;
	
	private static List<Word> wordsCache ;
	
	private boolean viewLocked = false;
	
	private WindowManager windowManager;
	private WindowManager.LayoutParams winParams;
	
	private DataHelper dataHelper ;
	
	/**
	 * 当前的布局文件，桌面单词或者桌面单词设置界面
	 */
	View view;
	private int width;
	private int height;

	public static BaseAppliction getApp(){
		if(null == app){
			app = new BaseAppliction();
		}
		return app ;
	}
	
	@Override
	public void onCreate() {
		// mWM
		// =(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		// final View win =
		// LayoutInflater.from(this).inflate(R.layout.ctrl_window, null);
		//
		// win.setOnTouchListener(new OnTouchListener() {
		// float lastX, lastY;
		// public boolean onTouch(View v, MotionEvent event) {
		// final int action = event.getAction();
		//
		// float x = event.getX();
		// float y = event.getY();
		// if(action == MotionEvent.ACTION_DOWN) {
		// lastX = x;
		// lastY = y;
		// } else if(action == MotionEvent.ACTION_MOVE) {
		// mWMParams.x += (int) (x - lastX);
		// mWMParams.y += (int) (y - lastY);
		// mWM.updateViewLayout(win, mWMParams);
		// }
		// return true;
		// }
		// });
		//
		// WindowManager wm = mWM;
		// WindowManager.LayoutParams wmParams = new
		// WindowManager.LayoutParams();
		// wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
		// WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// mWMParams = wmParams;
		// wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		// //type是关键，这里的2002表示系统级窗口，你也可以试试2003。
		// wmParams.format=1;
		//
		// wmParams.width = 300;
		// wmParams.height = 200;
		//
		// wm.addView(win, wmParams);

		windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		// 获取屏幕宽度和高度
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight();

//		setupPreviewSettingLayout();
		
		//init the databaseHelper
		DataHelper.getInstance(this);
	}
	
	

	public DataHelper getDataHelper() {
		return dataHelper;
	}


	private void setupPreviewSettingLayout() {
		view = LayoutInflater.from(this).inflate(
				R.layout.desk_word_preview_setting, null);

		winParams = new WindowManager.LayoutParams();
		winParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//		winParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		winParams.format = 1;

		winParams.width = WindowManager.LayoutParams.FILL_PARENT;
		winParams.height = WindowManager.LayoutParams.FILL_PARENT;

		view.findViewById(R.id.btn_small_down).setOnClickListener(this);
		view.findViewById(R.id.toggle_lock_screen).setOnClickListener(this);
		
		windowManager.addView(view, winParams);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_small_down:
			Log.d(TAG, "click-->btn_small_down");
			break;
		case R.id.toggle_lock_screen:
			Log.d(TAG, "click-->toggle_lock_screen");
			lockCurrentView();
			break;
		}

	}

	private void lockCurrentView() {
		if (null != winParams) {
			if(viewLocked){//屏幕锁住了，解锁
				winParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			}else{//屏幕没锁住，要锁住
				winParams.flags = 0;
			}
			windowManager.addView(view, winParams);
			viewLocked = !viewLocked;
		}
	}
	
	public static void setWordsCache(List<Word> cache){
		wordsCache = cache;
	}
	public static List<Word> getWordsCache(){
		return wordsCache ;
	}
}
