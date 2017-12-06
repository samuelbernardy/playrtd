package com.playrtd.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import com.gc.dto.ProductDto;

@Controller
public class RandomGameGen {
	
		
		@RequestMapping("/randgame")
		public String randgame(Model model) {
		// TODO Auto-generated method stub
		 
		Configuration cfg =new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		Criteria crit = s.createCriteria(ProductDto.class);
		
		String id="";
		String name = "";
		int tag = 0;
		String img = "";
		String desc = "";
		Object[] obj = new Object[5];
//		ArrayList<obj> arr = new ArrayList<obj>();
		String[] arrl = new String[4];
		Query q2 = s.createQuery("select g.appID,g.gameName,g.tag,g.image,g.description from ProductDto g WHERE g.tag = 19 ORDER BY RAND()");
		
		q2.setFirstResult(1);
		q2.setMaxResults(1);
		List results = q2.list();
		Iterator i = results.iterator();
		List<ProductDto> list = new ArrayList<ProductDto>();
		while(i.hasNext()) 
		{
			
			//Objects position is being correlated by the createQuery above. IE. g.appID is the first, so that would be obj[0]
		obj = (Object[])i.next(); 
		id = (String)obj[0];
		name = (String)obj[1];
		tag = (int)obj[2];
		img = (String)obj[3];
		desc = (String)obj[4];
		list.add(new ProductDto(id, name, img, desc));
//		arrl[0] = id;
//		arrl[1]= name;
//		arrl[2] = img;
//		arrl[3] = desc;
		System.out.println(id);
		System.out.println(name);
		System.out.println(tag);
		System.out.println(img);
		System.out.println(desc);
		
		
		}
		
		s.flush();
		s.close();
		model.addAttribute("gameID", id);
		model.addAttribute("gameImg", img);
		model.addAttribute("description", desc);
		model.addAttribute("gameName", name);
		return "randgame";
		}
}
