package com.gc.dto;

public class UserInfoDto {
	
	private int steam_ID;
	private String personaName;
	private String avatar;

	public UserInfoDto () {
		
	}

	public int getSteam_ID() {
		return steam_ID;
	}

	public void setSteam_ID(int steam_ID) {
		this.steam_ID = steam_ID;
	}

	public String getPersonaName() {
		return personaName;
	}

	public void setPersonaName(String personaName) {
		this.personaName = personaName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
