package com.trance.empire.modules.battle.model;

public class AttackInfoDto {

    private long playerId;

	/**
	 * 攻击者id
	 */
	private String playerName;
	
	/**
	 * 时间
	 */
	private long time;
	
	/**
	 * 失去的资源
	 */
	private String rewards;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getRewards() {
		return rewards;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}
	
	
}
