package com.toursims.mobile.model.kml;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Question {
	public static final String TYPE_MULTIPLE_CHOICE = "multiple-choice";
	public static final String TYPE_EXACT = "exact";
	
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
	
	public boolean hasMultipleAnswers() {
		int i = 0;
		
		for(Answer a : Answers){
			if(a.isTrue()){
				i++;
			}
		}
		return (i>1);
	}
	
	private int getNumberAnswers() {
		int i = 0;
		
		for(Answer a : Answers){
			if(a.isTrue()){
				i++;
			}
		}
		return i;
	}
	
	public boolean checkAnswer(String s) {
		if(s.equals(Answers.get(0).getValue())){
			return true;
		}
		return false;
	}
	
	public boolean checkAnswer(int i) {
		return Answers.get(i).isTrue();
	}
	
	public boolean checkAnswers(List<Integer> ints) {
		if(ints.size()==getNumberAnswers()) {
			for(Integer i : ints){
				if(!Answers.get(i).isTrue()){
					return false;
				}
			}
			return true;
		}		
		return false;
	}
}
