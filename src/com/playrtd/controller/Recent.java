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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gc.dto.ProductDto;
import com.gc.dto.RecentLikesDto;

@Controller
public class Recent {

	@RequestMapping({ "/", "index" })
	public String favorites(Model model) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();

		Object[] obj = new Object[2];

		String recentLikeIMG = "";
		String recentLikeName = "";
		String persona = "";
		Query q2 = s.createQuery("select recentLikeIMG, recentLikeName, persona from RecentLikesDto ORDER BY ID DESC");

		q2.setFirstResult(0);
		q2.setMaxResults(20);
		List results = q2.list();
		Iterator i = results.iterator();
		List<RecentLikesDto> list = new ArrayList<RecentLikesDto>();
		while (i.hasNext()) {

			obj = (Object[]) i.next();
			recentLikeIMG = (String) obj[0];
			recentLikeName = (String) obj[1];
			persona = (String) obj[2];

			list.add(new RecentLikesDto(recentLikeIMG, recentLikeName, persona));
		}

		s.flush();
		s.close();
		model.addAttribute("list", list);

		// model.addAttribute("persona", persona);

		return "index";
	}

}
