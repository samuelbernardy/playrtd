package com.gc.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.gc.dto.GameIdMappingDto;

	public class GameIdMappingDaoTwitch implements GameIdMappingDao {
	private Configuration config = new Configuration().configure("hibernate.cfg.xml");
	private SessionFactory sessionFactory = config.buildSessionFactory();
	private Session session = sessionFactory.openSession();
	
	@Override
	public void insert(ArrayList<GameIdMappingDto> dtoArr) {
		
		for (GameIdMappingDto dto : dtoArr) {
			Transaction tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		}
		session.close();
	}

	@Override
	public List<GameIdMappingDto> list(int startRow, int numOfRecords) {
		
		return null;
	}

	@Override
	public void update(GameIdMappingDto dto) {

	}

	@Override
	public void delete(GameIdMappingDto dto) {

	}
	
	@Override
	public void deleteAll() {

	}

	@Override
	public GameIdMappingDto searchByName(String gameName) {
		return null;
		
	}

}
