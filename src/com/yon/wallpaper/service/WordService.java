package com.yon.wallpaper.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.yon.wallpaper.bean.Word;
import com.yon.wallpaper.db.DataHelper;

public class WordService {

	Context context;
	private static WordService wordService ;

	private Dao<Word, Integer> wordDao = null;

	private WordService(Dao<Word, Integer> wordDao) {
		this.wordDao = wordDao;
	}

	public static WordService getInstance(){
		if(null == wordService){
			try {
				wordService = new WordService(DataHelper.getInstance(null).getWordDao());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wordService;
	}
	
	public void save(Word word) {
		try {
			wordDao.create(word);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveBatch(final List<Word> words) {
		try {
			wordDao.callBatchTasks(new Callable<Word>() {

				@Override
				public Word call() throws Exception {
					int n = words.size();
					for (int i=0;i<n;i++) {
						wordDao.create(words.get(i));
						System.out.println("saved:" + words.get(i));
					}
					return null;
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Word word) {
		try {
			wordDao.update(word);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有的单词
	 * 
	 * @return
	 */
	public List<Word> getAll() {
		try {
			List<Word> curses = wordDao.queryForAll();
			return curses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 某个单词是否存在
	 * @param word
	 * @return
	 */
	public boolean isExists(Word word) {
		try {
			List<Word> words = wordDao.queryForEq("word", word.getWord());
			if (null != words && words.size() > 0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteBach(List<Word> words) {
		try {
			wordDao.delete(words);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Word> getByTime(List<String> params) {
		try {
			QueryBuilder<Word, Integer> builder = wordDao.queryBuilder();
			Where<Word, Integer> where = builder.where();
			where.in("date", params.toArray());
			builder.setWhere(where);
			PreparedQuery<Word> pq = builder.prepare();
			System.out.println(pq.getStatement());
			return wordDao.query(pq);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, ArrayList<Word>> getWeekCurseByDate(List<String> params) {
		try {
			Map<String, ArrayList<Word>> curses = new HashMap<String, ArrayList<Word>>();
			ArrayList<Word> dateList;
			for (String date : params) {
				dateList = (ArrayList<Word>) wordDao.queryForEq("date", date);
				if (null != dateList && dateList.size() > 0) {
					curses.put(date, dateList);
				}
			}
			return curses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
