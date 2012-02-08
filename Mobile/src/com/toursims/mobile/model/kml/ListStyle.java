package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class ListStyle {

	@Element(required=false)
	private ItemIcon ItemIcon;

	public ListStyle() {
		super();
	}

	public ListStyle(ItemIcon itemIcon) {
		super();
		ItemIcon = itemIcon;
	}

	public ItemIcon getItemIcon() {
		return ItemIcon;
	}

	public void setItemIcon(ItemIcon itemIcon) {
		ItemIcon = itemIcon;
	}

}