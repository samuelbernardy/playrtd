package com.gc.dto;

import java.io.Serializable;

public class OwnedGamesDto implements Serializable{
	
	private int appID;
	
	public OwnedGamesDto () {
		
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

}
