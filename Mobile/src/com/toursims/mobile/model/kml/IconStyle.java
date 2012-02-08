package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class IconStyle {

	@Element(required=false)
	private Icon Icon;

	@Element(required=false)
	private hotSpot hotSpot;

	@Element(required=false)
	private double scale;

	public IconStyle() {
		super();
	}

	public IconStyle(Icon icon, hotSpot hotSpot, double scale) {
		super();
		this.Icon = icon;
		this.hotSpot = hotSpot;
		this.scale = scale;
	}

	public Icon getIcon() {
		return Icon;
	}

	public void setIcon(Icon icon) {
		this.Icon = icon;
	}

	public hotSpot getHotSpot() {
		return hotSpot;
	}

	public void setHotSpot(hotSpot hotSpot) {
		this.hotSpot = hotSpot;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
}