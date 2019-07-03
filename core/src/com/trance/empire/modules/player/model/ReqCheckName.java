package com.trance.empire.modules.player.model;

import io.protostuff.Tag;

public class ReqCheckName {
	
	@Tag(1)
	private String playerName;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
