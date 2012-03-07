package com.toursims.mobile.model.kml;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Document {
	// Le tag ElementList permet de d�finir une liste d'�l�ments
	// Ensuite on a des attributs: entry permet de d�finir � quelle �l�ment fait r�f�rence la liste,
    // inline permet cr�er une liste avec des �l�ments qui ne sont pas exclusivement entre balises.
    // C'est � dire que ces �l�ments sont m�l�s avec d'autre �l�ments entre les m�mes balises.
    // l'attribut required quant � lui permet de d�finir si l'�l�ment est obligatoire ou pas
	
	@ElementList(entry="Placemark",inline=true,required=false)
	private List<Placemark> Placemark;

	@Element(required=false)
	private String name;

	@Element(required=false)
	private Style Style;
	
	@Element(required=false)
	private String end;

	@Element(required=false)
	private String presentation;

	// L'attribut data permet de signaler que l'�l�ment contient une section ( <!--[CDATA[ )
	@Element(data=true,required=false)
	private String Snippet;
	
	@ElementList(entry="Data",required=false)
	private List<Data> ExtendedData;
		
	public List<Data> getExtendedData() {
		return ExtendedData;
	}
	
	public void setExtendedData(List<Data> extendedData) {
		ExtendedData = extendedData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Style getStyle() {
		return Style;
	}

	public void setStyle(Style style) {
		this.Style = style;
	}

	public String getSnippet() {
		return Snippet;
	}

	public void setSnippet(String snippet) {
		this.Snippet = snippet;
	}

	public List<Placemark> getPlacemarks() {
		return Placemark;
	}

	public void setPlacemarks(List<Placemark> placemarks) {
		this.Placemark = placemarks;
	}
	
	public String getEnd() {
		if(end==null){
			return name;
		}
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
	
	public String getPresentation() {
		if(presentation==null){
			return presentation;
		}
		return presentation;
	}
}