package com.yon.wallpaper.control;

import java.util.Random;
import java.util.Timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.yon.wallpaper.R;
import com.yon.wallpaper.service.WordsService;

public class WordPainter extends Thread implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	
	private static int[] COLORS = new int[] {Color.RED,Color.GREEN,Color.BLUE,Color.MAGENTA};
	
	private int width ;
	private int height ;
	
	private int wordSize = 20;
	
	private int sleepTime = 5000;
	
	private Context context ;
	
	private SurfaceHolder surfaceHolder ;
	
	private SharedPreferences prefs;
	
	private boolean live = true;
	private boolean wait = false;
	
	private Timer drawTimer = null;
	
	public WordPainter(SurfaceHolder surfaceHolder, Context context){
		Log.d("lyj", "WordPainter()");
		this.context = context;
		this.surfaceHolder = surfaceHolder;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight();
		
		//获取Sharepreference并注册配置变化事件
		prefs = context.getSharedPreferences(WordsService.PREFERENCES_NAME_WORD_SIZE, Context.MODE_PRIVATE);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		wordSize = Integer.parseInt(prefs.getString(WordsService.PREFERENCES_NAME_WORD_SIZE, "20"));
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		wordSize = Integer.parseInt(sharedPreferences.getString(WordsService.PREFERENCES_NAME_WORD_SIZE, "20"));
	}
	
	public void onPause(){
		wait = true ;
		synchronized(this) {
			this.notify();
		}
	}
	
	public void onResume(){
		wait = false ;
		synchronized(this) {
			this.notify();
		}
	}
	
//	public void onStart(){
//		live = true ;
//		synchronized(this) {
//			this.notify();
//		}
//	}
	
	public void onStop(){
		live = false ;
		synchronized(this) {
			this.notify();
		}
	}

	@Override
	public void run() {
		Canvas c = null ;
		while(live){
			try {
				Log.d("lyj", "before draw...");
				c = surfaceHolder.lockCanvas();
				//绘制单词墙纸
				drawWordPaper(c);
				if (c != null) {
					surfaceHolder.unlockCanvasAndPost(c);
				}
				Log.d("lyj", "after draw...");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				
			}
			// pause if no need to animate
			synchronized (this) {
				if (wait) {
					try {
						wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 绘制单词墙纸
	 * @param canvas
	 */
	private void drawWordPaper(Canvas canvas){
		
		Random random = new Random();
		int top = random.nextInt(height);
		Paint paint = new Paint();
		canvas.drawColor(0xff000000);
		Bitmap bkg = BitmapFactory.decodeResource(context.getResources(), R.drawable.wooden_black);
		canvas.drawBitmap(bkg, 0, 0, paint);
		
		int color = random.nextInt(COLORS.length-1);
		paint.setColor(COLORS[color]);
		paint.setTextSize(wordSize);
		
		
		String word = "Confidence 自信";
		float wordWidth = paint.measureText(word);
		if(wordWidth<=width){
			Log.d("lyj", "top:"+top);
			canvas.drawText(word, 0, top, paint);
		}else{//内容长度超过屏幕宽度
			String w = word;
			int count = paint.breakText(word, true, width, null);
			while(count<w.length()){
				w = w.substring(0, count);
				Log.d("lyj", "--top:"+top);
				drawWord(canvas,paint,w,0,top);
				count = paint.breakText(word, true, width, null);
				top += (wordSize+10);
			}
			
		}
	}

	private void drawWord(Canvas canvas,Paint paint,String text,int x,int y){
		canvas.drawText(text, 0, y, paint);
	}
	
}
