package com.gc.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.gc.dto.GameIdMappingDto;
import com.playrtd.util.HibernateUtility;

public class GameIdMappingDaoTwitch implements GameIdMappingDao {
	//TODO Use utility class / session factory
	private static SessionFactory sessionFactory;
	
	public GameIdMappingDaoTwitch() {
		sessionFactory = HibernateUtility.getSessionFactory();
	}

	@Override
	public void insert(ArrayList<GameIdMappingDto> dtoArr) {
		Session session = sessionFactory.openSession();

		//TODO Utilize a Batch insert instead of loop
		for (GameIdMappingDto dto : dtoArr) {
			Transaction tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		}
		//TODO How to close session - to avoid Entity Manager is Closed
		session.close();
	}

	@Override
	public List<GameIdMappingDto> list(int startRow, int numOfRecords) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(GameIdMappingDto.class).setFirstResult(startRow).setMaxResults(numOfRecords);
		ArrayList<GameIdMappingDto> list = (ArrayList<GameIdMappingDto>) crit.list();
		tx.commit();
		session.close();
		return list;
	}

	@Override
	public void update(GameIdMappingDto dto) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(dto);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void delete(GameIdMappingDto dto) {

	}

	@Override
	public void deleteAll() {

	}

	@Override
	public GameIdMappingDto searchByName(String gameName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(GameIdMappingDto.class);
		crit.add(Restrictions.eq("gameName", gameName));
		GameIdMappingDto game = (GameIdMappingDto) crit.uniqueResult();
		tx.commit();
		session.close();
		return game;
	}

}
