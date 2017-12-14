package com.playrtd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Options {
	@RequestMapping(value = {"/choices", "/test"}, method = RequestMethod.GET)
	public String choices(Model model, @CookieValue(value = "steamID", required = false) Long steamid,
			@CookieValue(value = "avatar", required = false) String avatar,
			@CookieValue(value = "persona", required = false) String persona, @ModelAttribute("tag1") String tag1,
			@ModelAttribute("tag2") String tag2, @ModelAttribute("tag3") String tag3, @ModelAttribute("choicemsg") String choicemsg,
			@CookieValue(value = "nogames", required = false) String nogames) {

		model.addAttribute("avatar", avatar);
		model.addAttribute("persona", persona);
		model.addAttribute("opt1", tag1);
		model.addAttribute("opt2", tag2);
		model.addAttribute("opt3", tag3);
		model.addAttribute("choicemsg", choicemsg);
		model.addAttribute("nogames", nogames);

		return "choices";
	}

}
