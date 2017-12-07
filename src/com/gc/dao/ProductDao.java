package com.gc.dao;

import java.util.ArrayList;
import java.util.List;
import com.gc.dto.ProductDto;


public interface ProductDao {
	
	public List<ProductDto> list(int startRow, int numOfRecords); 

	public void update(ProductDto dto);
	
	public ArrayList<ProductDto> searchByName(String gameName);
}
