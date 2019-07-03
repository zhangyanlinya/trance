package com.trance.empire.modules.player.model;

import io.protostuff.Tag;

public class ReqCreatePlayer {
	
	@Tag(1)
	private String userName;
	
	@Tag(2)
	private String playerName;
	
	@Tag(3)
	private int server;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getServer() {
		return server;
	}
	public void setServer(int server) {
		this.server = server;
	}
}
