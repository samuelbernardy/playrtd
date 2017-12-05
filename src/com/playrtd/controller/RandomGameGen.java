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


		

		
		Query q2 = s.createQuery("select g.appID,g.gameName,g.tag,g.image,g.description from ProductDto g WHERE g.tag = 3859 ORDER BY RAND()");
		
		q2.setFirstResult(1);
		q2.setMaxResults(1);
		List results = q2.list();
		Iterator i = results.iterator();
		while(i.hasNext()) 
		{
			
			//Objects position is being correlated by the createQuery above. IE. g.appID is the first, so that would be obj[0]
		Object[] obj = (Object[])i.next(); 
		String id = (String)obj[0];
		String name = (String)obj[1];
		int tag = (int)obj[2];
		String img = (String)obj[3];
		String desc = (String)obj[4];
		System.out.println(id);
		System.out.println(name);
		System.out.println(tag);
		System.out.println(img);
		System.out.println(desc);

		}
		s.flush();
		s.close();
		}
}
