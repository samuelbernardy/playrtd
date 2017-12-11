package com.gc.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.gc.dto.ProductDto;
import com.playrtd.util.HibernateUtility;

public class ProductDaoHibernate implements ProductDao {
	
private static SessionFactory sessionFactory;
	
	public ProductDaoHibernate() {
		sessionFactory = HibernateUtility.getSessionFactory();
	}

	@Override
	public List<ProductDto> list(int startRow, int numOfRecords) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(ProductDto.class).setFirstResult(startRow).setMaxResults(numOfRecords);
		ArrayList<ProductDto> list = (ArrayList<ProductDto>) crit.list();
		tx.commit();
		session.close();
		return list;
	}

	@Override
	public void update(ProductDto dto) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(dto);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public ArrayList<ProductDto> searchByName(String gameName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(ProductDto.class);
		crit.add(Restrictions.eq("gameName", gameName));
		ArrayList<ProductDto> game = (ArrayList<ProductDto>) crit.list();
		tx.commit();
		session.close();
		return game;
	}

}
