package com.trance.empire.modules.player.model;

import io.protostuff.Tag;

public class ReqRename {
	
	@Tag(1)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
