package com.yon.wallpaper.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.yon.wallpaper.app.BaseAppliction;
import com.yon.wallpaper.bean.Word;
import com.yon.wallpaper.util.FileUtil;

/**
 * 解析金山词霸导出的doc单词文件
 * 
 * @author lyjiang
 * @time 2013/01/13
 */
public class KingsoftPowerWordParser {

	private String filePath;

	private ParserThread parserThread;

	/**
	 * 解析金山词霸导出的doc文件
	 * 
	 * @param filePath
	 */
	public void parse(String filePath) {
		parserThread = new ParserThread(filePath);
		parserThread.start();
	}
}

class ParserThread extends Thread {
	private String filePath;

	public ParserThread(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void run() {
		File f = new File(filePath);
		if (!f.exists()) {
			// 通知ui
			System.out.println("file " + f.getAbsolutePath() + " not exists!");
		} else {
			parse(f);
		}

	}

	private void parse(File f) {
		BufferedReader br = null;
		InputStream is= null;
		String line = "";
		StringBuffer wordExplain = null;
		Word word ;
		boolean isWord = false;
		List<Word> words = new ArrayList<Word>();
		try {
			is = new FileInputStream(f);
			String charset = FileUtil.getFileEncode(f.getAbsolutePath());
			System.out.println("charset:" + charset);
			br = new BufferedReader(new InputStreamReader(is, charset));
			wordExplain = new StringBuffer();
			word = new Word();
			while ((line = br.readLine()) != null) {
				if ("".equals(line)) {//单词词义结束，重置状态
					word.setExplain(wordExplain.toString().trim());
					System.out.println(word);
					words.add(word);
					wordExplain = new StringBuffer();
					word = new Word();
					isWord = true;
				} else {
					if(isWord){//第一行是单词
						
						word.setWord(line);
						isWord = false;
					}else{
						wordExplain.append(line).append(" ");
					}
				}
			}
			WordService.getInstance().saveBatch(words);
			BaseAppliction.setWordsCache(words);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}