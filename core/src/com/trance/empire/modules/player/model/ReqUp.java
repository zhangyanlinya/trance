package com.trance.empire.modules.player.model;

import io.protostuff.Tag;

public class ReqUp {
	
	@Tag(1)
	private long targetId;

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}
}
