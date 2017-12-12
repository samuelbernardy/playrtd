package com.gc.dto;

import java.io.Serializable;

public class DiscordDto implements Serializable {

	private String title;
	private String link;
	private String description;

	public DiscordDto() {
		this.title = "";
		this.link = "";
		this.description = "";
	}

	public DiscordDto(String title, String link, String description) {
		this.title = title;
		this.link = link;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "DiscordDto [title=" + title + ", link=" + link + ", description=" + description + "]\n";
	}

}
