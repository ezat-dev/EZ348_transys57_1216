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
		paramOutPut.setFireno(devicecode+"");
		//파라미터로 받은 설비로 status값 조회
		StringBuffer desc = new StringBuffer();
		OutPut outPut = outPutDao.getOutPutDeviceStatus(paramOutPut);
		
		if(outPut == null) {
			//status값 0이라면
			//OUTPUT_TAB에 INSERT
			
			switch(devicecode) {
				case 5: MainController.outPutChk5 = true;
						desc.append(devicecode+"호기 출고요청 완료");
				break;
				case 6: MainController.outPutChk6 = true;
						desc.append(devicecode+"호기 출고요청 완료");
				break;
				case 7: MainController.outPutChk7 = true;
						desc.append(devicecode+"호기 출고요청 완료");
				break;
			
			}
			
			outPutDao.setOutPutSend(paramOutPut);
			logger.info("OUTPUT(57호기) : {}",desc.toString());				
		}
	}

	//침탄 5~7호기
	 @Override
	public void outPutTimer() throws InterruptedException, ExecutionException {
		//설비별 출고요청 가능신호
		String hogi5 = "false";
		String hogi6 = "false";
		String hogi7 = "false";
	

		//설비별 출고제품 체크1  
		String hogi5Prd = "0";
		String hogi6Prd = "0";
		String hogi7Prd = "0";
		String outputCancel = "false";
	
		
		//창고 출고가능 요구신호
		int outContinue = 0;
		
		//각 설비별 출고요청가능 신호 받기
		OpcDataMap opcData = new OpcDataMap();
		//창고출고가능요구 1이면
		Map<String, Object> hogi1Map = opcData.getOpcData("Transys.OUTPUT.CM02.HOGI5");
		Map<String, Object> hogi2Map = opcData.getOpcData("Transys.OUTPUT.CM02.HOGI6");
		Map<String, Object> hogi3Map = opcData.getOpcData("Transys.OUTPUT.CM02.HOGI7");
	
		//창고 출고요청취소 신호
		Map<String, Object> outputCancelMap = opcData.getOpcData("Transys.OUTPUT.CM02.OUTPUT_CANCEL");		
		Thread.sleep(300);
		
		hogi5 = hogi1Map.get("value").toString();
		hogi6 = hogi2Map.get("value").toString();
		hogi7 = hogi3Map.get("value").toString();
		outputCancel = outputCancelMap.get("value").toString();

		//
		Map<String, Object> hogi1PrdMap = opcData.getOpcData("Transys.OUTPUT.CM02.HOGI5_PRD");
		Map<String, Object> hogi2PrdMap = opcData.getOpcData("Transys.OUTPUT.CM02.HOGI6_PRD");
		Map<String, Object> hogi3PrdMap = opcData.getOpcData("Transys.OUTPUT.CM02.HOGI7_PRD");
		
		Thread.sleep(300);
		
		hogi5Prd = hogi1PrdMap.get("value").toString();
		hogi6Prd = hogi2PrdMap.get("value").toString();
		hogi7Prd = hogi3PrdMap.get("value").toString();
	
		
		StringBuffer desc = new StringBuffer();
		desc.append("hogi5 : "+hogi5+"// hogi6 : "+hogi6+"// hogi7 : "+hogi7);
		desc.append("hogi5Prd : "+hogi5Prd+"// hogi6Prd : "+hogi6Prd+"// hogi7Prd : "+hogi7Prd);
		
		Map<String, Object> outContinueMap = opcData.getOpcData("Transys.PLCWRITE.CM02.DEVICECODE");
		
		outContinue = Integer.parseInt(outContinueMap.get("value").toString());
		desc.append("outContinue : "+outContinue+"// ");
//		logger.info("OUTPUT {}",desc.toString());
		//출고요청신호 확인시 1이면
		
		
		//출고요청취소 신호 들어올경우
		if("true".equals(outputCancel)) {
			outPutDao.outputCancel();
			opcData.setOpcData("Transys.OUTPUT.CM02.OUTPUT_CANCEL", false);
		}		
		
		//5호기
		if("true".equals(hogi5)) {
			desc = new StringBuffer();
			desc.append("hogi5 : "+hogi5+"// ");			
			//화물 위치체크
			if("0".equals(hogi5Prd)) {
				desc.append("hogi5Prd : "+hogi5Prd+"// ");				
				//PLCWRITE의 설비값이 0일때
				if(outContinue == 0) {
					desc.append("outContinue : "+outContinue+"// ");
					desc.append("MainController.outPutChk5 : "+MainController.outPutChk5+"// ");
					
					logger.info("OUTPUT : {} ",desc.toString());					
					if(!MainController.outPutChk5) {
						desc.append("MainController.outPutChk5 ** : "+MainController.outPutChk5+"// ");
						logger.info("OUTPUT : {} ",desc.toString());						
						outPut(5);
					}
				}
			}
		}
		
		//6호기
		if("true".equals(hogi6)) {

			//화물 위치체크
			if("0".equals(hogi6Prd)) {

				//PLCWRITE의 설비값이 0일때
				if(outContinue == 0) {

					if(!MainController.outPutChk6) {

						outPut(6);
					}
				}
			}
		}
		
		//7호기
		if("true".equals(hogi7)) {
			//화물 위치체크
			if("0".equals(hogi7Prd)){
				//PLCWRITE의 설비값이 0일때
				if(outContinue == 0) {
					if(!MainController.outPutChk7) {
						outPut(7);
					}
				}
			}
		}
		
		
		
		
	}
}
