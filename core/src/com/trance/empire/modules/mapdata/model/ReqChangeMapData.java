package com.trance.empire.modules.mapdata.model;

import io.protostuff.Tag;

public class ReqChangeMapData {
	
	@Tag(1)
	private String from;
	
	@Tag(2)
	private String to;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}
