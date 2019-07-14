package com.trance.empire.modules.match.model;

import io.protostuff.Tag;

public class FightDto {
	
	/**
	 * 玩家id
	 */
	@Tag(1)
	private long id;

	/**
	 * 主公名称
	 */
	@Tag(2)
	private String playerName;
	
	/**
	 * 等级
	 */
	@Tag(3)
	private int level = 1;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
