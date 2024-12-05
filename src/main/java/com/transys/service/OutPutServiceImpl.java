package com.transys.service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transys.controller.MainController;
import com.transys.dao.OutPutDao;
import com.transys.domain.OutPut;
import com.transys.util.OpcDataMap;

@Service
public class OutPutServiceImpl implements OutPutService{

	@Autowired
	private OutPutDao outPutDao;
	
	private static final Logger logger = LoggerFactory.getLogger(OutPutServiceImpl.class);

	@Override
	public void outPut(int devicecode) {
		OutPut paramOutPut = new OutPut();
		paramOutPut.setFireno(devicecode);
		//파라미터로 받은 설비로 status값 조회
		StringBuffer desc = new StringBuffer();
		OutPut outPut = outPutDao.getOutPutDeviceStatus(paramOutPut);
			if(outPut.getWorkdate() != null) {
				//status값 0이라면
				if(outPut.isOutPutChk1()) {
					//OUTPUT_TAB에 INSERT					
					MainController.outPutChk1 = true;
//					outPutDao.setOutPutSend(outPut);
					desc.append("1호기 출고요청 완료");				
				}
				
				if(outPut.isOutPutChk2()) {
					//OUTPUT_TAB에 INSERT					
					MainController.outPutChk2 = true;
//					outPutDao.setOutPutSend(outPut);
					desc.append("2호기 출고요청 완료");				
				}
				
				if(outPut.isOutPutChk3()) {
					//OUTPUT_TAB에 INSERT					
					MainController.outPutChk3 = true;
//					outPutDao.setOutPutSend(outPut);
					desc.append("3호기 출고요청 완료");
				}
				
				if(outPut.isOutPutChk4()) {
					//OUTPUT_TAB에 INSERT					
					MainController.outPutChk4 = true;
//					outPutDao.setOutPutSend(outPut);
					desc.append("4호기 출고요청 완료");
				}
				
				logger.info("OUTPUT(14호기) : {}",desc.toString());					
			}
	}

	//침탄 1~4호기
	@Override
	public void outPutTimer() throws InterruptedException, ExecutionException {
		//설비별 출고요청 가능신호
		boolean hogi1 = false;
		boolean hogi2 = false;
		boolean hogi3 = false;
		boolean hogi4 = false;

		//설비별 출고제품 체크
		boolean hogi1Prd = false;
		boolean hogi2Prd = false;
		boolean hogi3Prd = false;
		boolean hogi4Prd = false;
		
		//창고 출고가능 요구신호
		boolean outContinue = false;
		
		//각 설비별 출고요청가능 신호 받기
		OpcDataMap opcData = new OpcDataMap();
		//창고출고가능요구 1이면
		Map<String, Object> hogi1Map = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI1");
		Map<String, Object> hogi2Map = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI2");
		Map<String, Object> hogi3Map = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI3");
		Map<String, Object> hogi4Map = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI4");
		Thread.sleep(300);
		
		hogi1 = Boolean.parseBoolean(hogi1Map.get("value").toString());
		hogi2 = Boolean.parseBoolean(hogi2Map.get("value").toString());
		hogi3 = Boolean.parseBoolean(hogi3Map.get("value").toString());
		hogi4 = Boolean.parseBoolean(hogi4Map.get("value").toString());

		//
		Map<String, Object> hogi1PrdMap = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI1_PRD");
		Map<String, Object> hogi2PrdMap = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI2_PRD");
		Map<String, Object> hogi3PrdMap = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI3_PRD");
		Map<String, Object> hogi4PrdMap = opcData.getOpcData("Transys.OUTPUT.CM01.HOGI4_PRD");
		Thread.sleep(300);
		
		hogi1Prd = Boolean.parseBoolean(hogi1PrdMap.get("value").toString());
		hogi2Prd = Boolean.parseBoolean(hogi2PrdMap.get("value").toString());
		hogi3Prd = Boolean.parseBoolean(hogi3PrdMap.get("value").toString());
		hogi4Prd = Boolean.parseBoolean(hogi4PrdMap.get("value").toString());	
		
		Map<String, Object> outContinueMap = opcData.getOpcData("Transys.OUTPUT.CM01.OUT_CONTINUE");
		
		outContinue = Boolean.parseBoolean(outContinueMap.get("value").toString());
		
		//출고요청신호 확인시 1이면
		
		//1호기
		if(hogi1) {
			//화물 위치체크
			if(!hogi1Prd) {
				//출고 진행중이 아니라면
				if(!outContinue) {
					if(!MainController.outPutChk1) {
						outPut(1);
					}
				}
			}
		}
		
		//2호기
		if(hogi2) {
			//화물 위치체크
			if(!hogi2Prd) {
				//출고 진행중이 아니라면
				if(!outContinue) {
					if(!MainController.outPutChk2) {
						outPut(2);
					}
				}
			}
		}
		
		//3호기
		if(hogi3) {
			//화물 위치체크
			if(!hogi3Prd) {
				//출고 진행중이 아니라면
				if(!outContinue) {
					if(!MainController.outPutChk3) {
						outPut(3);
					}
				}
			}
		}
		
		//4호기
		if(hogi4) {
			//화물 위치체크
			if(!hogi4Prd) {
				//출고 진행중이 아니라면
				if(!outContinue) {
					if(!MainController.outPutChk4) {
						outPut(4);
					}
				}
			}
		}
		
		
	}
}
