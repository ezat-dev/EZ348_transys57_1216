package com.transys.service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transys.dao.TrackingDao;
import com.transys.domain.Tracking;
import com.transys.util.OpcDataMap;
import com.transys.util.UtilClass;

@Service
public class TrackingServiceImpl3 implements TrackingService3{
	
	@Autowired
	private TrackingDao trackingDao;
	
	private static final Logger logger = LoggerFactory.getLogger(TrackingServiceImpl3.class);
	
	//11
	public void trackingDataSet(String devicecode, int curLocation, String setDataDir) 
			throws InterruptedException, ExecutionException {
		OpcDataMap opcDataMap = new OpcDataMap();
		
		Map<String, JSONArray> dataMap = opcDataMap.getOpcDataListMap2(setDataDir);
				
		JSONArray rowsArray = dataMap.get("dataList");			
		
		Tracking tracking = new Tracking();
		String pumBun = "0000";
		int prdChk = 0;
		StringBuffer desc = new StringBuffer();
		short resetValue = 0;
		for(int i=0; i<rowsArray.size(); i++) {
			JSONObject rowObj = (JSONObject) rowsArray.get(i);
			
			String tagName = rowObj.get("tagName").toString();
			String value = rowObj.get("value").toString();
			
			if("PUMBUN".equals(tagName)) {
				pumBun = String.format("%04d",Integer.parseInt(value));
				tracking.setPumbun(pumBun);				
			}
			
			if("PRD_CHK".equals(tagName)) {
				tracking.setDevicecode(devicecode);				
				tracking.setCurLocation(curLocation);
				prdChk = 1;
			}
		}
		
		//DB저장
		if(!"0000".equals(tracking.getPumbun()) && tracking.getPumbun() != null && prdChk != 0) {
			desc.append("DEVICECODE : "+tracking.getDevicecode()+"// ");
			desc.append("PUMBUN : "+tracking.getPumbun()+"// ");
			desc.append("CURLOCATION : "+tracking.getCurLocation()+"// ");
			desc.append("setDataDir : "+setDataDir);
			
			logger.info("TRACKING(57호기) : {}",desc.toString());						
			//트래킹 실행
			trackingDao.ccf1Tracking01(tracking);
			//지연시간 0.3초
			Thread.sleep(300);
			
			//트래킹처리 후 PRD_CHK_01 0으로 변경
			opcDataMap.setOpcData(setDataDir+".PRD_CHK", resetValue);
		}
		
	}
	
	//[1]투입완료: 탈지로입구 리프트에 처리품이 위치할때
	public void ccf3Tracking01() throws InterruptedException, ExecutionException {
		//Transys.TRACKING.CCF07.C01의 하위태그 조회		
		String setDataDir = "Transys.TRACKING.CCF07.C01";
		
		//호기, 위치(순서), 태그경로
		trackingDataSet("7",1,setDataDir);
	}
	
	//[2]예열장입 : 처리품이 예열실에 도착할 때
	public void ccf3Tracking02() throws InterruptedException, ExecutionException {
		//Transys.TRACKING.CCF07.C02의 하위태그 조회		
		String setDataDir = "Transys.TRACKING.CCF07.C02";
	
		//호기, 위치(순서), 태그경로
		trackingDataSet("7",2,setDataDir);
	}
	
	//[3]침탄실(1) : 침탄 처리 시작위치
	public void ccf3Tracking03() throws InterruptedException, ExecutionException {
		//Transys.TRACKING.CCF07.C02의 하위태그 조회		
		String setDataDir = "Transys.TRACKING.CCF07.C03";
	
		//호기, 위치(순서), 태그경로
		trackingDataSet("7",3,setDataDir);
	}
	
	//[4]확산실(1) : 침탄 처리 종료 및 확산 처리 시작위치
	public void ccf3Tracking04() throws InterruptedException, ExecutionException {
		//Transys.TRACKING.CCF07.C02의 하위태그 조회		
		String setDataDir = "Transys.TRACKING.CCF07.C04";
		
		//호기, 위치(순서), 태그경로
		trackingDataSet("7",4,setDataDir);
	}
	
	//[5]냉각실(1) : 확산 처리 종료 위치
	public void ccf3Tracking05() throws InterruptedException, ExecutionException{
		
		String setDataDir = "Transys.TRACKING.CCF07.C05";
		
		trackingDataSet("7",5,setDataDir);
		
	}
	
	//[6]소입1실(1) : 소입 처리 시작 위치
	public void ccf3Tracking06() throws InterruptedException, ExecutionException{
			
		String setDataDir = "Transys.TRACKING.CCF07.C06";
			
		trackingDataSet("7",6,setDataDir);
			
	}
	
	//[7]소입2 추출 : 소입 처리 종료
	public void ccf3Tracking07() throws InterruptedException, ExecutionException{
				
		String setDataDir = "Transys.TRACKING.CCF07.C07";
				
		trackingDataSet("7",7,setDataDir);
				
	}
	
	//[8]SALT장입 : SALT 처리 시작위치
	public void ccf3Tracking08() throws InterruptedException, ExecutionException{
					
		String setDataDir = "Transys.TRACKING.CCF07.C08";
					
		trackingDataSet("7",8,setDataDir);
					
	}
	
	//[9]SALT추출 : 출구 리트리버 후퇴정지
	public void ccf3Tracking09() throws InterruptedException, ExecutionException{
						
		String setDataDir = "Transys.TRACKING.CCF07.C09";
						
		trackingDataSet("7",9,setDataDir);
						
	}	
}
