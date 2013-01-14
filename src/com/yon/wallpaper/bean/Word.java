package com.yon.wallpaper.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author lyjiang
 * 
 */
@DatabaseTable(tableName = "words")
public class Word {

	@DatabaseField(generatedId = true)
	private Integer id;

	@DatabaseField
	private String word;

	@DatabaseField
	private String explain;

	public Word() {

	}

	public Word(String word, String explain) {
		this.word = word;
		this.explain = explain;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	@Override
	public String toString() {
		return  word + " " + explain ;
	}

}
