package com.toursims.mobile.model.kml;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Question {
	
	@Attribute
	private String type;
	
	@Element
	private String title;
		
	@ElementList(entry="Answer",required=false)
	private List<Answer> Answers;

	@ElementList(entry="Clue",required=false)
	private List<Clue> Clues;
	
	public List<Answer> getAnswers() {
		return Answers;
	}
	
	public Question() {
		super();
	}

	public List<Clue> getClues() {
		return Clues;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getType() {
		return type;
	}
	
	public void setAnswers(List<Answer> answers) {
		Answers = answers;
	}
	
	public void setClues(List<Clue> clues) {
		Clues = clues;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
