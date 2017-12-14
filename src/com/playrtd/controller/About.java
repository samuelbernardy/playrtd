package com.playrtd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class About {
	
	@RequestMapping("/about")
	public String about() {
		return "about";
	}

}
