package com.trance.empire.modules.battle.model;

import io.protostuff.Tag;

public class AttackInfoDto implements Comparable<AttackInfoDto>{

	@Tag(1)
	private long playerId;
	
	/**
	 * 攻击者id
	 */
	@Tag(2)
	private String playerName;
	
	/**
	 * 时间
	 */
	@Tag(3)
	private long time;
	
	/**
	 * 失去的资源
	 */
	@Tag(4)
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


    @Override
    public int compareTo(AttackInfoDto other) {
        if(time > other.time){
            return -1;
        }
        if(time < other.time){
            return 1;
        }
        return 0;
    }
}
