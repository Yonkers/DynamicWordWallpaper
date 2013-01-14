package com.yon.wallpaper.activity;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.yon.wallpaper.R;
import com.yon.wallpaper.app.BaseAppliction;
import com.yon.wallpaper.service.KingsoftPowerWordParser;
import com.yon.wallpaper.widget.AutoScrollTextView;

public class DeskWordPreviewActivity extends Activity implements
		OnClickListener, OnTouchListener {
	private static String TAG = "DeskWordPreviewActivity";

	private static final int BIG_MOVE_DISTANCE = 30;
	private static final int SMALL_MOVE_DISTANCE = 5;

	private WindowManager windowManager;
	private View deskWordSettingLayout;

	private RelativeLayout wordPreviewLayout;
	
	private AutoScrollTextView previewText ;

	private int width;
	private int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.desk_word_preview_setting);
		windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		// 获取屏幕宽度和高度
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight();

		findViewById(R.id.btn_small_down).setOnClickListener(this);
		findViewById(R.id.btn_small_up).setOnClickListener(this);
		findViewById(R.id.btn_big_down).setOnClickListener(this);
		findViewById(R.id.btn_big_up).setOnClickListener(this);
		
		previewText = (AutoScrollTextView) findViewById(R.id.preview_text);
		previewText.init(width);
		previewText.setSpeed(1f);
		previewText.startScroll();
		findViewById(R.id.toggle_lock_screen).setOnClickListener(this);
		
		wordPreviewLayout = (RelativeLayout) findViewById(R.id.previe_word_layout);
		wordPreviewLayout.setOnTouchListener(this);

		
	}
float ts = 20;
float sd = 0.5f;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_small_down:
//			moveDown(SMALL_MOVE_DISTANCE);
			if(sd>0.6) 
				sd -= 0.1;
			previewText.setSpeed(sd);
			break;
		case R.id.btn_small_up:
			if(sd<1.8){
				sd += 0.2;
			}
			previewText.setSpeed(sd);
//			moveUp(SMALL_MOVE_DISTANCE);
			break;
		case R.id.btn_big_down:
//			moveDown(BIG_MOVE_DISTANCE);
			previewText.setTextSize(ts--);
			break;
		case R.id.btn_big_up:
//			moveUp(BIG_MOVE_DISTANCE);
			previewText.setTextSize(ts++);
			break;
		case R.id.toggle_lock_screen:
			Intent it = new Intent();
			it.setAction(Intent.ACTION_GET_CONTENT);
			it.setType("image/*");
			startActivityForResult(it, 0);
			break ;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(0 == requestCode && resultCode == RESULT_OK){
			Uri uri = data.getData();
			String path = uri.getPath();
			Log.d(TAG, "path:"+path);
			new KingsoftPowerWordParser().parse(path);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	// http://android-drag-and-drop-listview.googlecode.com/svn/trunk/
	// android-drag-and-drop-listview-read-only
	float lastX, lastY;
	boolean flag = false;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		flag = true;
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (flag) {
			final int action = event.getAction();
			float x = event.getX();
			float y = event.getY();
			if (action == MotionEvent.ACTION_DOWN) {
				lastX = x;
				lastY = y;
			} else if (action == MotionEvent.ACTION_MOVE) {
				if (y + wordPreviewLayout.getHeight() >= height) {
					y = height - wordPreviewLayout.getHeight();
				}
				wordPreviewLayout.setY(y);
				wordPreviewLayout.postInvalidate();
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 手指离开屏幕时，把flag设为false
			flag = false;
			if(null != BaseAppliction.getWordsCache()){
				int size =  BaseAppliction.getWordsCache().size();
				previewText.setText(BaseAppliction.getWordsCache().get(new Random().nextInt(size-1)).toString());
			}
		}
		return super.onTouchEvent(event);
	}

	private void moveDown(int distance) {
		float y = wordPreviewLayout.getY();
		y += distance;
		if (y + wordPreviewLayout.getHeight() >= height) {
			y = height - wordPreviewLayout.getHeight();
		}
		wordPreviewLayout.setY(y);
		wordPreviewLayout.postInvalidate();
	}

	private void moveUp(int distance) {
		float y = wordPreviewLayout.getY();
		y -= distance;
		if (y < 0) {
			y = 0;
		}
		wordPreviewLayout.setY(y);
		wordPreviewLayout.postInvalidate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
