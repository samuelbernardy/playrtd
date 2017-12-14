package com.playrtd.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gc.dto.ProductDto;
import com.gc.dto.RecentLikesDto;

@Controller
public class Favorites {


	// TODO ADD @requestparam for persona to be passed rather than hardcoding
	// through.
	@RequestMapping(value = "/favorites", method = RequestMethod.GET)
	public String favorites(Model model, @CookieValue("steamID") Long steam_ID, @CookieValue("avatar") String avatar,
			@CookieValue("persona") String persona) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();

		Object[] obj = new Object[2];
		String query = QueryLikes(steam_ID);
		String recentLikeIMG = "";
		String recentLikeName = "";
		String userID = "";
		String uPersona = persona;
		String storeURL = "";
		
		Query q2 = s.createQuery(query);

		q2.setFirstResult(0);
		q2.setMaxResults(999);
		List results = q2.list();
		Iterator i = results.iterator();
		List<RecentLikesDto> list = new ArrayList<RecentLikesDto>();
		while (i.hasNext()) {

			obj = (Object[]) i.next();
			userID= (String) obj[0];
//			uPersona = (String) obj[1];
			recentLikeIMG = (String) obj[2];
			recentLikeName = (String) obj[3];
			storeURL = (String) obj[4];

			list.add(new RecentLikesDto(userID, uPersona, recentLikeIMG, recentLikeName, storeURL));
		}

		s.flush();
		s.close();

		model.addAttribute("avatar", avatar);
		model.addAttribute("persona", persona);

		model.addAttribute("list", list);
		//model.addAttribute("persona", persona);

		return "favorites";
	}

	public static String QuesryLikes() {
		String temp = "select userID,persona,recentLikeIMG,recentLikeName,storeURL from RecentLikesDto ORDER BY ID DESC";
		return temp;
	}

	public static String QueryLikes(Long persona) {
		String temp = "select userID,persona,recentLikeIMG,recentLikeName,storeURL from RecentLikesDto WHERE userID = '"
				+ persona + "'" + " ORDER BY ID DESC";
		return temp;

	}
}
