package com.gc.dao;

import java.util.ArrayList;
import java.util.List;

import com.gc.dto.GameIdMappingDto;

public interface GameIdMappingDao {
	
		public void insert(ArrayList<GameIdMappingDto> dto);
		
		public List<GameIdMappingDto> list(int startRow, int numOfRecords); 

		public void update(GameIdMappingDto dto);
		
		public void delete(GameIdMappingDto dto);
		
		public void deleteAll();

		public ArrayList<GameIdMappingDto> searchByName(String gameName);
}
