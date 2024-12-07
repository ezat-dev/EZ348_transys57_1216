package com.transys.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transys.dao.ArrivedTabDao;
import com.transys.domain.ArrivedTab;
import com.transys.domain.Product;

@Service
public class ArrivedTabServiceImpl implements ArrivedTabService{

	@Autowired
	private ArrivedTabDao arrivedTabDao;
	
	@Override
	public void arrivedTabTimer() throws InterruptedException, ExecutionException {
		
		
		//ARRIVED_TAB 데이터 조회
		List<ArrivedTab> arrivedTabList = arrivedTabDao.getArrivedTabDataSelect();
		
		for(ArrivedTab avt : arrivedTabList) {
			if(avt.getWorkdate() != null &&
					avt.getWorkdate().length() > 0) {
				//T_PRODUCT 데이터 조회
				Product product = arrivedTabDao.getArrivedTabProductSelect(avt);
				
				Thread.sleep(200);
				
				if(product.getDobun() == null) {
					//arrived_tab 데이터 있음.
					//arrived_tab 추가
					
					//ARRIVED_TAB 데이터 저장
					arrivedTabDao.setArrivedTabDataInsert(avt);
					
					//arrived_tab 삭제
					arrivedTabDao.setArrivedTabDataDelete(avt);
				}

			}			
		}

	}

}
