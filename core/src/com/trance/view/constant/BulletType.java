package com.trance.view.constant;

public enum BulletType {

	ONE("bullet/1.png"),

	TWO("bullet/2.png"),

	THREE("bullet/3.png"),

	FOUR("bullet/4.png"),

	FIVE("bullet/5.png"),

	SIX("bullet/6.png"),

	SEVEN("bullet/7.png"),

	EIGHT("bullet/5.png"),

	NIE("bullet/5.png")
	;

	private  String value;

	BulletType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static BulletType valueOf(int index){
		return BulletType.values()[index];
	}
}
