package com.transys.dao;

import java.util.List;

import com.transys.domain.ArrivedTab;
import com.transys.domain.Product;

public interface ArrivedTabDao {

	List<ArrivedTab> getArrivedTabDataSelect();
	
	Product getArrivedTabProductSelect(ArrivedTab arrivedTab);

	void setArrivedTabDataInsert(Product product);


}
