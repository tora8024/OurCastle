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
import com.ourcastle.model.OcSettingVo;
import com.ourcastle.model.QueryOcOrderInfoVo;
import com.ourcastle.model.ResponseVo;
import com.ourcastle.orm.OcAddAttr;
import com.ourcastle.orm.OcPeriodOfTimeInfo;
import com.ourcastle.utils.OcConstants;


/**
 * Handles requests for the Employee service.
 */
@Controller
@RequestMapping("/rs/admin")
public class OurCastleAdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(OurCastleAdminController.class);

	private static Gson gson = new Gson();
	     
    @Autowired
    private IOurCastleLogic ourCastleLogic;
	
	public IOurCastleLogic getOurCastleLogic() {
		return ourCastleLogic;
	}


	public void setOurCastleLogic(IOurCastleLogic ourCastleLogic) {
		this.ourCastleLogic = ourCastleLogic;
	}


	
		
	@RequestMapping(value = "/findOcSetting", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody ResponseVo findOcSetting() {
		
		try {

			logger.info("findOcSetting==>in");
			String result = gson.toJson(ourCastleLogic.findOcSetting());
			logger.info("findOcSetting==>output:"+result);
			logger.info("findOcSetting==>out");		
			return new ResponseVo(OcConstants.OK,result);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseVo(OcConstants.FAIL,"查詢設定失敗,\n原因:"+e.getMessage());
		}
	}
	
	
	

	@RequestMapping(value = "/submitSetting", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ResponseVo submitSetting(@RequestBody OcSettingVo settingVo) {

		try{

			logger.error("submitSetting==>in");
			logger.error("submitSetting==>input"+gson.toJson(settingVo));
			

			if(settingVo.latestPromotionsUrl==null||settingVo.latestPromotionsUrl.length()==0){
				throw new Exception("未填最新優惠活動網址");
			}
			
			if(settingVo.latestPromotionsUrl.length()>2048){
				throw new Exception("最新優惠活動 網址超過2048個字元");
			}
	
			
			/*submit setting to db*/
			ourCastleLogic.updateAttrValueByParam(OcConstants.CATG_LASTEST_PROMOTIONS,OcConstants.PARAM_URL, settingVo.latestPromotionsUrl);
			
			
			
		}catch(Exception e){
			logger.error(e.getMessage());

			return new ResponseVo(OcConstants.FAIL,"設定失敗,\n原因:"+e.getMessage());
			
		}
		logger.info("submitSetting==>Setting success");
		logger.info("submitSetting==>out");
		 
		return new ResponseVo(OcConstants.OK,"設定成功.");
	}
	
	
	
		
}
