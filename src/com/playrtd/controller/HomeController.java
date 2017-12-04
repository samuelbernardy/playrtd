package com.playrtd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/*
 * author: samuelbernardy
 * author: yasrodriguez
 * author: josephdgarza
 */

@Controller
public class HomeController {

	@RequestMapping("/")
	public String main() {
		
		return "";
	}

	@RequestMapping("/return")
	public String loggedIn() {
		return "return";
	}

	@RequestMapping("/seerecent")
	public String seeRecent() {
		return "seerecent";
	}

	@RequestMapping("/rolled")
	public String rolled() {
		return "gameon";
	}
}