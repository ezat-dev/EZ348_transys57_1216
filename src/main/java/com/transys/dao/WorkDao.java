package com.transys.dao;

import java.util.List;

import com.transys.domain.InOut;
import com.transys.domain.Product;
import com.transys.domain.Work;

public interface WorkDao {

   List<Work> workDetailList(Work work);

   Work workDetailDescData(Work work);

   List<Product> workDetailProductList(Product product);

   Work workDetailEditData(Work work);

   void setWorkDetailEditDataSave(Work work);

   void setWorkDetailAddDataSave(Work work);

   void setWorkDetailDelete(Work work);
   
   void setWorkDetailInlineDelete(Work work);

   void setWorkDetailEndSalt(Work work);

   void setWorkDetailEndTime(Work work);

   void setWorkDetailForcingStart(Work work);

   void setWorkDetailForcingEnd(Work work);

   Work getWorkDetailEndTime(Work work);
   
   Work getWorkDetail(Work work);
   
    
   List<Work> getAllProducts();
       
   List<Work> workDayList(Work work);
       
   List<Work> workMonthList(Work work);
       
   List<Work> workYearList(Work work);
   
   List<Work> workDayPrint(Work work);
   
   List<Work> workMonthPrint(Work work);
   
   List<Work> workYearPrint(Work work);

void workDayPrintListCheckCntSet(Work work);
Work workDetailDescDataOverView(Work work);

List<InOut> getInOutList();
}