package com.toursims.mobile.model.kml;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Placemark {

	@Element(required=false)
	private String name;

	@Element(data=true,required=false)
	private String description;

	@Element(required=false)
	private String address;

	// Ici nous n'avons pas besoin de l'attribut inline car les �l�ments ne sont pas m�l�s � d'autres
	@ElementList(required=false)
	private List<Style> StyleMap;

	@Element(required=false)
	private Point Point;

	@Element(required=false)
	private LookAt LookAt;

	@Element(required=false)
	private GeometryCollection GeometryCollection;

	@Element(required=false)
	private String styleUrl;
	
	@Element(required=false)
	private ExtendedData ExtendedData;
	
	public ExtendedData getExtendedData() {
		return ExtendedData;
	}
	
	public void setExtendedData(ExtendedData extendedData) {
		ExtendedData = extendedData;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Style> getStyleMap() {
		return StyleMap;
	}

	public void setStyleMap(List<Style> styleMap) {
		this.StyleMap = styleMap;
	}

	public Point getPoint() {
		return Point;
	}

	public void setPoint(Point point) {
		this.Point = point;
	}

	public LookAt getLookAt() {
		return LookAt;
	}

	public void setLookAt(LookAt lookAt) {
		this.LookAt = lookAt;
	}

	public GeometryCollection getGeometryCollection() {
		return GeometryCollection;
	}

	public void setGeometryCollection(GeometryCollection geometryCollection) {
		GeometryCollection = geometryCollection;
	}

	public String getStyleUrl() {
		return styleUrl;
	}

	public void setStyleUrl(String styleUrl) {
		this.styleUrl = styleUrl;
	}
}