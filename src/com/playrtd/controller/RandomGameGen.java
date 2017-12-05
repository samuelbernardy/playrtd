package com.playrtd.controller;
import java.util.Iterator;
import java.util.List;
 
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.gc.dto.ProductDto;
public class RandomGameGen {
	public static void main(String[] args) {
		 
		// TODO Auto-generated method stub
		 
		Configuration cfg =new Configuration();
		cfg.configure("Hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
//		Query q = s.createQuery("select g from ProductDto g");
		Query q = s.createQuery("from ProductDto");
		List<ProductDto> g1 = q.list();
		System.out.println("data loaded here");
		
		
//		for(ProductDto g2:g1)
//		{
//			if(g2.getTag()==19) { // grabs all in tag 
//		System.out.println(g2.getId());
//		System.out.println(g2.getGameName());
//		System.out.println(g2.getAppID());
//		System.out.println(g2.getImage());
//		System.out.println(g2.getTag());
//		}
		 
		System.out.println("this is the second query");
		Query q2 = s.createQuery("select g.appID,g.gameName,g.tag from ProductDto g");
		List l1 = q2.list();
		Iterator i = l1.iterator();
		while(i.hasNext()) 
		{
		
		Object[] obj = (Object[])i.next(); // may be able to remove from iterate and use random class to choose randomly
		String id = (String)obj[0];
		String name = (String)obj[1];
		int tag = (int)obj[2];
		if (tag == 19) { // can set here to choose what prints out
		System.out.println(id);
		System.out.println(name);
		System.out.println(tag);
		}
		 
		}
		
		
		
		 
//		System.out.println("this is the third query");
//		Query q3 = s.createQuery("select g.name from Games g");
//		List l2 = q3.list();
//		Iterator i2 = l2.iterator();
//		while(i2.hasNext())
//		{
//		Object obj = (Object)i2.next();
//		String name = (String)obj;
//		System.out.println(name);
//		 
//		}
		s.flush();
		s.close();
		}
}
