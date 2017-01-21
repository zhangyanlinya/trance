package com.trance.view.constant;

public enum UiType {
	
	LEVEL("ui/level.png"),
	FOODS("ui/foods.png"),
	GOLD("ui/gold.png"),
	SILVER("ui/silver.png"),
	ITEMBOX("ui/itembox.png"),
	CHANGE("ui/change.png"),
	TRAIN("ui/train.png"),
	BLANK("ui/blank.png"),
	CLOSE("ui/close.png"),
	LEVELUP("ui/levelup.png"),
	UPBUILDING("ui/upbuilding.png"),
	FIXED("ui/fixed.png"),
	ONE("ui/1.png"),
	TWO("ui/2.png"),
	THREE("ui/3.png"),
	;
	
	private final String value;
	
	UiType(String value){
		this.value = value;
	}
	
	public String getVlaue(){
		return value;
	}
}
