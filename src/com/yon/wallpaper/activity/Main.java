package com.yon.wallpaper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;

import com.yon.wallpaper.R;

public class Main extends PreferenceActivity {

	private SharedPreferences prefs ;
	
	private String NAME_WALLPAPER_SETTING = "wallpaper_setting";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//add preference
		addPreferencesFromResource(R.xml.settings);
		getPreferenceManager().findPreference(NAME_WALLPAPER_SETTING).setOnPreferenceClickListener(preferenceClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	Preference.OnPreferenceClickListener preferenceClickListener = new Preference.OnPreferenceClickListener(){

		@Override
		public boolean onPreferenceClick(Preference preference) {
			if(NAME_WALLPAPER_SETTING.equals(preference.getKey())){
				Intent it = new Intent(Main.this,WallpaperSetting.class);
				Main.this.startActivity(it);
				return true;
			}
			return false;
		}
		
	};
}
