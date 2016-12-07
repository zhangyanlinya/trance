package com.trance.view.constant;

public enum BulletType {

	COMMON("bullet/0.png"),

	ONE("bullet/1.png"),

	TWO("bullet/2.png"),

	THREE("bullet/3.png"),

	FOUR("bullet/4.png"),

	FIVE("bullet/5.png")
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
