package com.trance.empire.modules.player.model;

import io.protostuff.Tag;

public class ReqReconnect {
	
	@Tag(1)
	private String userName;
	
	@Tag(2)
	private int server;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}
}
