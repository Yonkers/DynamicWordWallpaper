package com.yon.wallpaper.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.yon.wallpaper.R;
import com.yon.wallpaper.service.WordsService;

public class WallpaperSetting extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(
				WordsService.PREFERENCES_NAME_WORD_SIZE);
		addPreferencesFromResource(R.xml.wallpaper_settings);
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		String value = sharedPreferences.getString(WordsService.PREFERENCES_NAME_WORD_SIZE, "");
		Log.d("lyj", "WordsService.PREFERENCES_NAME-->"+value);
	}
}
