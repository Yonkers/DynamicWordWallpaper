package com.yon.wallpaper.service;

import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.yon.wallpaper.control.WordPainter;

public class WordsService extends WallpaperService {
	
	public static final String PREFERENCES_NAME_WORD_SIZE = "word_size";

	@Override
	public Engine onCreateEngine() {

		return new WordsEngine();
	}

	class WordsEngine extends WallpaperService.Engine {

		private WordPainter painter ;
		
		public WordsEngine() {
			SurfaceHolder holder = getSurfaceHolder();
			painter = new WordPainter(holder, getApplicationContext());
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			painter.onStop();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			Log.d("lyj","onVisibilityChanged..."+visible);
			if (visible) {
				// regist callback
				painter.onResume();
			} else {
				// remove callback
				painter.onPause();
			}
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			Log.d("lyj","onSurfaceChanged...");
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			Log.d("lyj","onSurfaceCreated...");
			painter.start();
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			Log.d("lyj","onSurfaceDestroyed...");
			boolean retry = true;
			painter.onStop();
			while (retry) {
				try {
					painter.join();
					retry = false;
				} catch (InterruptedException e) {}
			}
		}

	}
}