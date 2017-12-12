package com.playrtd.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gc.dto.RecentLikesDto;
@Controller
public class LikeButton {

	
	
	@RequestMapping(value = "/like", method = RequestMethod.GET)
		public String likeButton	(@RequestParam(value = "gameImg") String img, @RequestParam(value = "gameName") String gameName, 
				@RequestParam(value ="persona") String persona, Model model) {
		
		
		
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		System.out.println(img);
		RecentLikesDto likes = new RecentLikesDto();
		likes.setRecentLikeIMG(img);
		likes.setRecentLikeName(gameName);
		likes.setUserID(persona);
		System.out.println(likes.getRecentLikeIMG());
		System.out.println(likes.getUserID());
		
		
		
		Session session = factory.openSession();
		Transaction t = (Transaction) session.beginTransaction();
		
		Query query = session.createQuery("FROM RecentLikesDto WHERE recentLikeName='" + gameName + "'");
		RecentLikesDto result=(RecentLikesDto) query.uniqueResult();
		
		if(result == null) {
			session.persist(likes);
		}
		
		
		t.commit();
		session.close();
		model.addAttribute("gameName", gameName);
		return "gameon";
		
	}
}