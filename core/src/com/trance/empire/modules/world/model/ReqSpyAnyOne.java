package com.trance.empire.modules.world.model;

import io.protostuff.Tag;

public class ReqSpyAnyOne {
	
	@Tag(1)
	private long targetId;

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}
}
