package com.trance.empire.modules.player.model;

import io.protostuff.Tag;

public class ReqLogin {
	
	@Tag(value = 1)
	private String userName;
	
	@Tag(value = 2)
	private int serverId;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getServerId() {
		return serverId;
	}
	
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}
