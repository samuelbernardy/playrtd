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

public class GameIdMappingDaoTwitch implements GameIdMappingDao {
	private static Configuration config = new Configuration().configure("hibernate.cfg.xml");
	private static SessionFactory sessionFactory = config.buildSessionFactory();
	private static Session session = sessionFactory.openSession();

	@Override
	public void insert(ArrayList<GameIdMappingDto> dtoArr) {

		for (GameIdMappingDto dto : dtoArr) {
			Transaction tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		}
		//TODO How to close session - to avoid Entity Manager is Closed
		//session.close();
	}

	@Override
	public List<GameIdMappingDto> list(int startRow, int numOfRecords) {

		return null;
	}

	@Override
	public void update(GameIdMappingDto dto) {
		session.beginTransaction();
		session.update(dto);
		session.getTransaction().commit();
	}

	@Override
	public void delete(GameIdMappingDto dto) {

	}

	@Override
	public void deleteAll() {

	}

	@Override
	public GameIdMappingDto searchByName(String gameName) {
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(GameIdMappingDto.class);
		crit.add(Restrictions.eq("gameName", gameName));
		GameIdMappingDto game = (GameIdMappingDto) crit.uniqueResult();
		tx.commit();
		//session.close();
		return game;
	}

}
