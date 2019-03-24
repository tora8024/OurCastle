package com.ourcastle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ourcastle.logic.IOurCastleLogic;
import com.ourcastle.model.OcOrderInfoVo;
import com.ourcastle.model.QueryOcOrderInfoVo;
import com.ourcastle.model.ResponseVo;
import com.ourcastle.orm.OcPeriodOfTimeInfo;
import com.ourcastle.utils.OcConstants;


/**
 * Handles requests for the Employee service.
 */
@Controller
@RequestMapping("/rs")
public class OurCastleController {
	
	private static final Logger logger = LoggerFactory.getLogger(OurCastleController.class);

	private static Gson gson = new Gson();
	     
    @Autowired
    private IOurCastleLogic ourCastleLogic;
	
	public IOurCastleLogic getOurCastleLogic() {
		return ourCastleLogic;
	}


	public void setOurCastleLogic(IOurCastleLogic ourCastleLogic) {
		this.ourCastleLogic = ourCastleLogic;
	}


	
		
	@RequestMapping(value = "/findPeriodOfTimeInfo", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody ResponseVo findOcPeriodOfTimeInfo() {
		
		try {

			logger.info("findOcPeriodOfTimeInfo==>in");
			String result = gson.toJson(ourCastleLogic.findOcPeriodOfTimeInfo());
			logger.info("findOcPeriodOfTimeInfo==>output:"+result);
			logger.info("findOcPeriodOfTimeInfo==>out");		
			return new ResponseVo(OcConstants.OK,result);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseVo(OcConstants.FAIL,"查詢時段失敗,\n原因:"+e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/submitOrder", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ResponseVo submitOrder(@RequestBody OcOrderInfoVo orderVo) {

		try{

			logger.error("submitOrder==>in");
			logger.error("submitOrder==>input"+gson.toJson(orderVo));
			if(orderVo.bookingDate==null||orderVo.bookingDate.length()==0){
				throw new Exception("未填預約日期");
			}
			
			if(orderVo.childrenCnt==null||orderVo.childrenCnt.length()==0){
				throw new Exception("未填小孩人數");
			}

			if(orderVo.parentName==null||orderVo.parentName.length()==0){
				throw new Exception("未填家長姓名");
			}

			if(orderVo.phoneNum==null||orderVo.phoneNum.length()==0){
				throw new Exception("未填聯絡電話");
			}

			if(orderVo.periodId==null||orderVo.periodId.length()==0){
				throw new Exception("未選預約時段");
			}
			

			if(orderVo.mail==null||orderVo.mail.length()==0){
				throw new Exception("未填E-Mail信箱");
			}
						
			
			/*submit order to db*/
			ourCastleLogic.submitOrder(orderVo);
			
			

	    	/*Send Mail*/
	    	if(orderVo.mail!=null && orderVo.mail.length()>0){
	    		logger.info("send confirm mail to "+orderVo.mail+"===========");
	    		try{
	    			String to=orderVo.mail;
	    			String subject=String.format("<預約成功> %s",ourCastleLogic.findAttrValueByParam(OcConstants.CATG_STORE_INFO,OcConstants.PARAM_STORE_NAME));

	    			String periodDesc= "";
	    			OcPeriodOfTimeInfo ocPeriodOfTimeInfo =null;
	    			try{
	    				ocPeriodOfTimeInfo=ourCastleLogic.findOcPeriodOfTimeInfoByCond(orderVo.periodId);
	    			}catch(Exception e){
	    				logger.error("get perod title fail, exception"+e.getMessage());
	    			}
	    			
	    			periodDesc=periodDesc+ocPeriodOfTimeInfo.getTitle(); 
	    			
					String body = String.format(
							"Dear %s,\n"
							+ "日期:%s %s \n"
							+ "時段:%s \n"
							+ "小孩人數:%s \n"
							+ "聯絡電話:%s \n"
							+ "您已預約成功,謝謝\n\n"
							+ "===祕密基地 親子館 - Our Castle 聯絡資訊===\n"
							+ "地址:%s\n"
							+ "電話:%s / %s\n"
							+ "網址:%s",
							orderVo.parentName, 
							orderVo.bookingDate,ourCastleLogic.getDayOfWeek(orderVo.bookingDate),
							periodDesc,
							orderVo.childrenCnt,
							orderVo.phoneNum,
							ourCastleLogic.findAttrValueByParam(OcConstants.CATG_STORE_INFO,OcConstants.PARAM_ADDRESS),
							ourCastleLogic.findAttrValueByParam(OcConstants.CATG_STORE_INFO,OcConstants.PARAM_TELEPHONE),ourCastleLogic.findAttrValueByParam(OcConstants.CATG_STORE_INFO,OcConstants.PARAM_CELLPHONE),
							ourCastleLogic.findAttrValueByParam(OcConstants.CATG_STORE_INFO,OcConstants.PARAM_WEB_ADDRESS));
											
							
						ourCastleLogic.sendMail(to,subject,body);
		    	}catch(Exception e){
		    		logger.error("send confirm mail fail exception:"+e.getMessage());
		    		return new ResponseVo(OcConstants.OK,"預約成功,\n但寄送確認信異常,請於臨櫃告知已預約即可");
		    	}
	    	}else{
	    		logger.info("not input mail,so do not send confirm mail===========");
	    	}
			
			
		}catch(Exception e){
			logger.error(e.getMessage());

			return new ResponseVo(OcConstants.FAIL,"預約失敗,\n原因:"+e.getMessage());
			
		}
		logger.info("submitOrder==>booking success");
		logger.info("submitOrder==>out");
		 
		return new ResponseVo(OcConstants.OK,"預約成功.");
	}
	
	
	
	
	
	@RequestMapping(value = "/findOrderByParam", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ResponseVo findOrderByParam(@RequestBody QueryOcOrderInfoVo queryOcOrderInfoVo) {
		
		try {

			logger.info("findOrderByParam==>in");
			logger.error("findOrderByParam==>input"+gson.toJson(queryOcOrderInfoVo));
			String result = gson.toJson(ourCastleLogic.findOrderByParam(queryOcOrderInfoVo.phoneNum, queryOcOrderInfoVo.periodId, queryOcOrderInfoVo.startDate, queryOcOrderInfoVo.endDate, queryOcOrderInfoVo.parentName));
			logger.info("findOrderByParam==>output:"+result);
			logger.info("findOrderByParam==>out");		
			return new ResponseVo(OcConstants.OK,result);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseVo(OcConstants.FAIL,"查詢預約紀錄失敗,\n原因:"+e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ResponseVo cancelOrder(@RequestBody OcOrderInfoVo orderVo) {

		try{

			logger.error("cancelOrder==>in");
			logger.error("cancelOrder==>input"+gson.toJson(orderVo));
			
			if(orderVo.orderId==null||orderVo.orderId.length()==0){
				throw new Exception("未填預約單號");
			}
			
			if(orderVo.phoneNum==null||orderVo.phoneNum.length()==0){
				throw new Exception("未填聯絡電話");
			}

			if(orderVo.periodId==null||orderVo.periodId.length()==0){
				throw new Exception("未填預約時段");
			}
			

			if(orderVo.bookingDate==null||orderVo.bookingDate.length()==0){
				throw new Exception("未填預約日期");
			}
						
			ourCastleLogic.cancelOrder(orderVo);
			
			
		}catch(Exception e){
			logger.error(e.getMessage());

			return new ResponseVo(OcConstants.FAIL,"取消預約失敗,\n原因:"+e.getMessage());
			
		}
		logger.info("cancelOrder==>cancel order success");
		logger.info("cancelOrder==>out");
		 
		return new ResponseVo(OcConstants.OK,"已成功取消預約");
	}
	
	
	@RequestMapping("/findAvailablePeriod/{bookingDate}")
	public @ResponseBody ResponseVo findAvailablePeriod(@PathVariable String bookingDate) {

		try {

			logger.info("findAvailablePeriod==>in");

			if(bookingDate==null||bookingDate.length()==0){
				logger.error("findAvailablePeriod==>bookingDate is emppty.");	
				throw new Exception("未選取預約日期");
			}
			
			if(ourCastleLogic.isClosedDay(bookingDate)){
				logger.error("findAvailablePeriod==>"+bookingDate+" is closed day.");
				return new ResponseVo(OcConstants.FAIL,bookingDate+"為公休日");
			}
			
			logger.info("findAvailablePeriod==>in");
			String result = gson.toJson(ourCastleLogic.findAvailablePeriod(bookingDate));
			logger.info("findAvailablePeriod==>output:"+result);
			logger.info("findAvailablePeriod==>out");		
			return new ResponseVo(OcConstants.OK,result);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseVo(OcConstants.FAIL,"查詢時段失敗,\n原因:"+e.getMessage());
		}
	
	}
	
	
	@RequestMapping("/findLatestPromotionsUrl")
	public @ResponseBody ResponseVo findLatestPromotionsUrl() {

		try {

			logger.info("findLatestPromotionsUrl==>in");
			
			
			logger.info("findLatestPromotionsUrl==>in");
			String result = gson.toJson(ourCastleLogic.findLatestPromotionsUrl());
			logger.info("findLatestPromotionsUrl==>output:"+result);
			logger.info("findLatestPromotionsUrl==>out");		
			return new ResponseVo(OcConstants.OK,result);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseVo(OcConstants.FAIL,"查詢時段失敗,\n原因:"+e.getMessage());
		}
	
	}
	
		
}
