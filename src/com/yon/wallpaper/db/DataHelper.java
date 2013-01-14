/**
 * 
 */
package com.yon.wallpaper.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yon.wallpaper.bean.Word;

/**
 * @author lyjiang
 * 
 */
public class DataHelper extends OrmLiteSqliteOpenHelper {

	public static final String DATABASE_NAME = "DESK_WORDS.db";

	public static final int DATABASE_VERSION = 1;
	
	private static DataHelper dataHelper = null;

	private Dao<Word, Integer> wordDao = null;
	

	public DataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * 第一次初始化需要传递一个context
	 * @param context
	 * @return
	 */
	public static DataHelper getInstance(Context context){
		if(null == dataHelper && context!= null)
			dataHelper = new DataHelper(context);
		return dataHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(connectionSource, Word.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		super.close();
		wordDao = null;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			TableUtils.dropTable(connectionSource, Word.class, true);
			onCreate(db, connectionSource);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Dao<Word, Integer> getWordDao() throws SQLException {
		if (wordDao == null) {
			wordDao = getDao(Word.class);
		}
		return wordDao;
	}
	
}
