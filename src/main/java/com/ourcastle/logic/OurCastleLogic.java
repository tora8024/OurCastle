package com.ourcastle.logic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ourcastle.dao.IOurCastleDao;
import com.ourcastle.model.AvailablePeriodVo;
import com.ourcastle.model.OcOrderInfoVo;
import com.ourcastle.model.OcSettingVo;
import com.ourcastle.orm.OcAddAttr;
import com.ourcastle.orm.OcOrderInfo;
import com.ourcastle.orm.OcOrderInfoPK;
import com.ourcastle.orm.OcPeriodOfTimeInfo;
import com.ourcastle.utils.OcConstants;
import com.ourcastle.utils.OcEnum;
import com.ourcastle.utils.ReadConfigFile;
import com.ourcastle.utils.OcEnum.*;

@Service
public class OurCastleLogic implements IOurCastleLogic
{
	static Logger logger = LoggerFactory.getLogger(OurCastleLogic.class);
    
	ReadConfigFile config=new ReadConfigFile();
	
	@Autowired
    private MailSender mailSender;
    
	@Override
    public MailSender getMailSender() {
		return mailSender;
	}
	
	@Override
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Autowired
    private IOurCastleDao ourCastleDao;
    
    @Override
	public IOurCastleDao getOurCastleDao() {
		return ourCastleDao;
	}
    
    @Override
	public void setOurCastleDao(IOurCastleDao ourCastleDao) {
		this.ourCastleDao = ourCastleDao;
	}

    @Override
	public void sendMail(String to, String subject, String body) throws Exception
    {
    	try{
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        mailSender.send(message);
	    }catch(Exception e){
			logger.error("sendMail fail exception:"+e.getMessage());
			throw e;
		}
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public List<OcPeriodOfTimeInfo> findOcPeriodOfTimeInfo() throws Exception {

    	try{
	    	List<OcPeriodOfTimeInfo> list=ourCastleDao.findOcPeriodOfTimeInfo();
	    	logger.info("findOcPeriodOfTimeInfo==>list size="+list.size());
	    	
			return ourCastleDao.findOcPeriodOfTimeInfo();
    	}catch(Exception e){
    		logger.error("findOcPeriodOfTimeInfo fail exception:"+e.getMessage());
    		throw e;
    	}
	}
    

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public OcPeriodOfTimeInfo findOcPeriodOfTimeInfoByCond(String periodId) throws Exception {

    	try{
	    	OcPeriodOfTimeInfo obj=ourCastleDao.findOcPeriodOfTimeInfoByCond(periodId);
	    	
			return obj;
    	}catch(Exception e){
    		logger.error("findOcPeriodOfTimeInfoByCond fail exception:"+e.getMessage());
    		throw e;
    	}
	}
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public OcAddAttr findOcAddAttrByCond(String category, String paramName) throws Exception {

    	try{
    		OcAddAttr obj=ourCastleDao.findOcAddAttrByCond(category,paramName);
	    	
			return obj;
    	}catch(Exception e){
    		logger.error("findOcAddAttrInfoByCond fail exception:"+e.getMessage());
    		throw e;
    	}
	}
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public String findAttrValueByParam(String category, String paramName) throws Exception {

    	try{
    		OcAddAttr obj=ourCastleDao.findOcAddAttrByCond(category,paramName);
    		
			return obj.getParamValue();
    	}catch(Exception e){
    		logger.error("findOcAddAttrInfoByCond fail exception:"+e.getMessage());
    		throw e;
    	}
	}
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public List<OcOrderInfoVo> findOrderByParam(String phoneNum, String periodId, String startDate ,String endDate,String parentName) throws Exception {

    	try{
    		logger.info("findOrderByParam===>start");
    		    		
    		List<OcOrderInfo> list=ourCastleDao.findOrderByParam(phoneNum, periodId, startDate, endDate ,parentName);
    		List<OcOrderInfoVo> listVo=new ArrayList<OcOrderInfoVo>();
    		if(list!=null){
    			logger.info("findOrderByParam===>result size:"+list.size());
    			
    			for (OcOrderInfo orm:list){
    				listVo.add(parseOrderInfoOrmToVo(orm));
    				
    			}
    		}
    		
    		logger.info("findOrderByParam===>end");
			return listVo;
    	}catch(Exception e){
    		logger.error("findOrderByParam fail exception:"+e.getMessage());
    		throw e;
    	}
	}
    
  
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
    public void submitOrder(OcOrderInfoVo orderVo) throws Exception{
    	
    	try{
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    	logger.info(String.format("Process booking record==> bookingDate:%s, periodId:%s, childrenCnt:%s ",orderVo.bookingDate,orderVo.periodId,orderVo.childrenCnt));
			
	    	//check is over time of period id
	    	if(isOverTimeOfPeriod( orderVo.bookingDate,  orderVo.periodId)==true){
	    		throw new Exception("該場次時間已經開始了,無法預約");
	    	}
	    	
	    	
	    	/*Check Record in DB*/
	    	logger.info("Check booking record is Existed=========");
	    	isExistedOrder(orderVo.phoneNum, orderVo.periodId, orderVo.bookingDate);
			
			//check Available count For PeriodId 
	    	logger.info("Check booking count is available=========");
			findAvailableCntForPeriodId(orderVo.bookingDate,orderVo.periodId,orderVo.childrenCnt);

	    	/*Inset Record to DB*/
    		logger.info("Insert booking record=========");
	    	OcOrderInfo ocOrderInfo=new OcOrderInfo();
	    	OcOrderInfoPK ocOrderInfoPK =new OcOrderInfoPK();
	    	ocOrderInfoPK.setBookingDate(sdf.parse(orderVo.bookingDate));
	    	ocOrderInfoPK.setOrderId(genOrderId(orderVo.periodId));
	    	ocOrderInfoPK.setPeriodId(orderVo.periodId);
	    	ocOrderInfoPK.setPhoneNum(orderVo.phoneNum);
	    	ocOrderInfo.setId(ocOrderInfoPK);
	    	
	    	ocOrderInfo.setChildrenCnt(Integer.valueOf(orderVo.childrenCnt));
	    	ocOrderInfo.setParentName(orderVo.parentName);
	    	ocOrderInfo.setMail(orderVo.mail);
	    	ocOrderInfo.setRemark(orderVo.remark);

	    	ocOrderInfo.setBookingStatus(OcConstants.STATUS_Open);
	    	ocOrderInfo.setCreateDate(new Date());
	    	ocOrderInfo.setUpdateTime(new Date());

	    	ourCastleDao.persist(ocOrderInfo);
	    	
    	}catch(Exception e){
    		logger.error("submitOrder fail exception:"+e.getMessage());
    		throw e;
    	}
    }
    
    private String genOrderId(String periodId){
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
    	String orderId=sdf.format(new Date())+"-"+periodId;
    	return orderId;
    }
    
    
    
    
    @Override
	public String getDayOfWeek(String bookingDate) {
		String dayOfWeekStr="";
		Calendar cal = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = (Date) formatter.parse(bookingDate);
			cal = Calendar.getInstance();
			cal.setTime(date);
		} catch (ParseException e) {
			logger.error("ParseException :" + e);
			return "";
		}

		// 取得星期幾的整數值
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		// 判斷取得的數值等於星期幾
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			dayOfWeekStr="(日)";
			break;
		case Calendar.MONDAY:
			dayOfWeekStr="(一)";
			break;
		case Calendar.TUESDAY:
			dayOfWeekStr="(二)";
			break;
		case Calendar.WEDNESDAY:
			dayOfWeekStr="(三)";
			break;
		case Calendar.THURSDAY:
			dayOfWeekStr="(四)";
			break;
		case Calendar.FRIDAY:
			dayOfWeekStr="(五)";
			break;
		case Calendar.SATURDAY:
			dayOfWeekStr="(六)";
		}
		return dayOfWeekStr;
	}
    
    
	private String checkDayType(String bookingDate) {
		String dayType="";

		
		Calendar cal = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = (Date) formatter.parse(bookingDate);
			cal = Calendar.getInstance();
			cal.setTime(date);
		} catch (ParseException e) {
			logger.error("ParseException :" + e);
			return "";
		}

		// 取得星期幾的整數值
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		// 判斷取得的數值等於星期幾
		switch (dayOfWeek) {
		case Calendar.SATURDAY:
		case Calendar.SUNDAY:
			dayType=OcConstants.DAY_TYPE_HOLIDAY;
			break;
		case Calendar.MONDAY:
		case Calendar.TUESDAY:
		case Calendar.WEDNESDAY:
		case Calendar.THURSDAY:
		case Calendar.FRIDAY:
			dayType=OcConstants.DAY_TYPE_NORMAL_DAY;
			break;
		}
		

		if(isHoliday(bookingDate)){
			dayType=OcConstants.DAY_TYPE_HOLIDAY;
		}
		
		return dayType;
	}
    
	private boolean isHoliday(String bookingDate){
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = (Date) formatter.parse(bookingDate);
			DateFormat formatter2 = new SimpleDateFormat("MM-dd");
			String bookingDate2="";
			bookingDate2=formatter2.format(date);
			/**
			 * @todo input holiday list 
			 * */
			Date nowDate=new Date();
			String year=String.valueOf(nowDate.getYear()+1900);
			String holidayList =this.findAttrValueByParam(year+OcConstants.CATG_Year,OcConstants.PARAM_NATIONAL_HOLIDAY);
			String[] holidayArr=holidayList.split(",");
			for (String holiday:holidayArr){
				if (bookingDate2.equalsIgnoreCase(holiday)){
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("check is Holiday  ,Exception :" + e);
		}
		return false;
	}

	@Override
	public boolean isClosedDay(String bookingDate){
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = (Date) formatter.parse(bookingDate);
			DateFormat formatter2 = new SimpleDateFormat("MM-dd");
			String bookingDate2="";
			bookingDate2=formatter2.format(date);
			/**
			 * @todo input holiday list 
			 * */
			Date nowDate=new Date();
			String year=String.valueOf(nowDate.getYear()+1900);
			String closedDayList =this.findAttrValueByParam(year+OcConstants.CATG_Year,OcConstants.PARAM_CLOSED_DAY);
			String[] closedDayArr=closedDayList.split(",");
			for (String closedDay:closedDayArr){
				if (bookingDate2.equalsIgnoreCase(closedDay)){
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("check is Holiday  ,Exception :" + e);
		}
		return false;
	}
    
    private OcOrderInfoVo parseOrderInfoOrmToVo(OcOrderInfo orm){
    	Map<String,String> periodMap=new HashMap<String,String>();
    	try{
    		List<OcPeriodOfTimeInfo> list=this.findOcPeriodOfTimeInfo();    		
    		for(OcPeriodOfTimeInfo obj: list){
    			periodMap.put(obj.getPeriodId(), obj.getTitle());
    		}
    	}catch (Exception e){
    		logger.error("findOcPeriodOfTimeInfo for get title, exception:"+e.getMessage());
    		
    	}
    	
    	OcOrderInfoVo vo=new OcOrderInfoVo();
    	SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    	vo.bookingDate=dateSdf.format(orm.getId().getBookingDate());
    	vo.orderId=orm.getId().getOrderId();
    	vo.periodId=orm.getId().getPeriodId();
    	if(periodMap!=null&&periodMap.containsKey(vo.periodId)){
    		vo.periodTitle=periodMap.get(vo.periodId);
    	}
    	vo.phoneNum=orm.getId().getPhoneNum();
    	
    	vo.childrenCnt=String.valueOf(orm.getChildrenCnt());
    	vo.parentName=orm.getParentName();
    	vo.mail=orm.getMail();
    	vo.remark=orm.getRemark();

    	vo.bookingStatus=orm.getBookingStatus();
    	vo.createDate=timeSdf.format(orm.getCreateDate());
    	vo.updateTime=timeSdf.format(orm.getUpdateTime());
    	
		return vo;
    	
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
    public void cancelOrder(OcOrderInfoVo orderVo) throws Exception{
    	
    	try{
	    	new SimpleDateFormat("yyyy-MM-dd");
	    	
	    	/*Check Record in DB*/
	    	logger.info("check booking record=========");
	    	OcOrderInfo order=ourCastleDao.findOrderById(orderVo.phoneNum, orderVo.periodId, orderVo.bookingDate ,orderVo.orderId);
	    	
	    	/*Inset Record to DB*/
    		logger.info("update booking status to cancel=========");
    		order.setBookingStatus(OcConstants.STATUS_Cancel);
	    	
    	}catch(Exception e){
    		logger.error("submitOrder fail exception:"+e.getMessage());
    		throw e;
    	}
    }

	@Override
	public List<AvailablePeriodVo> findAvailablePeriod(String bookingDate) throws Exception {

		
		try{			
			List<AvailablePeriodVo> availablePeriodVoList=new ArrayList<AvailablePeriodVo>();
			int maxLimit=Integer.valueOf(this.findAttrValueByParam(OcConstants.CATG_CHILD_COUNT,OcConstants.PARAM_MAX_LIMIT));
			new SimpleDateFormat("yyyy-MM-dd");
			String dayType=checkDayType(bookingDate);
			List<OcPeriodOfTimeInfo> periodList=ourCastleDao.findOcPeriodOfTimeInfoByDayType(dayType);
			
			for(OcPeriodOfTimeInfo obj:periodList){

				String periodId =obj.getPeriodId();

				if(isOverTimeOfPeriod( bookingDate,  periodId)==true){
					continue;
				}
				
				String nowCnt=ourCastleDao.sumChildrenCnt(periodId,bookingDate);
				AvailablePeriodVo vo=new AvailablePeriodVo();
				if(maxLimit>=Integer.valueOf(nowCnt)){
					vo.availableCnt =String.valueOf(maxLimit-Integer.valueOf(nowCnt));
				}
				vo.periodId=periodId;
				vo.bookingDate=bookingDate;
				vo.title=obj.getTitle();

				if (periodId!=null&& periodId.equalsIgnoreCase("P14") && noGoodNightPeriod(bookingDate)){
					vo.availableCnt="0";
				}
				
				availablePeriodVoList.add(vo);
				
			}
			
//			if(availablePeriodVoList.size()==0){
//				throw new Exception("已無場次可供預約");
//			}
			
			return availablePeriodVoList;
	    	
    	}catch(Exception e){
    		logger.error("submitOrder fail exception:"+e.getMessage());
    		throw e;
    	}
	}
	
	
	
	private void findAvailableCntForPeriodId(String bookingDate,String periodId,String bookingCnt) throws Exception {

		try{			
			int maxLimit=Integer.valueOf(this.findAttrValueByParam(OcConstants.CATG_CHILD_COUNT,OcConstants.PARAM_MAX_LIMIT));
			
			OcPeriodOfTimeInfo ocPeriodOfTimeInfo =null;
			
			String nowCnt=ourCastleDao.sumChildrenCnt(periodId,bookingDate);
			
			if(nowCnt!=null){
				int availableCnt=maxLimit-Integer.valueOf(nowCnt);
				if(availableCnt>=0){
					try{
						ocPeriodOfTimeInfo=this.findOcPeriodOfTimeInfoByCond(periodId);
					}catch(Exception e){
						logger.error("get perod title fail, exception"+e.getMessage());
			    		throw e;
					}
					
					if(availableCnt==0){
						throw new Exception(ocPeriodOfTimeInfo.getTitle()+"場次已額滿");
					}
					
					if(Integer.valueOf(bookingCnt)>availableCnt){
						throw new Exception(ocPeriodOfTimeInfo.getTitle()+"超過可預約人數: "+availableCnt);
					}
					
				}
			}
			
	    	
    	}catch(Exception e){
    		logger.error("findAvailableCntForPeriodId fail exception:"+e.getMessage());
    		throw e;
    	}
	}
	    
	
	private void isExistedOrder(String phoneNum, String periodId, String bookingDate) throws Exception {

		try{		
			List<OcOrderInfoVo> list=findOrderByParam(phoneNum, periodId, bookingDate ,bookingDate,"");
			boolean isBooking=false;
			for(OcOrderInfoVo vo:list){
				if(vo.bookingStatus.equalsIgnoreCase(OcConstants.STATUS_Open)){
					isBooking=true;
					break;
				}
			}
			if(isBooking){
				try{
					OcPeriodOfTimeInfo ocPeriodOfTimeInfo = this.findOcPeriodOfTimeInfoByCond(periodId);

					throw new Exception(ocPeriodOfTimeInfo.getTitle() +" 您已經預約過了.");
				}catch(Exception e){
					logger.error("get perod title fail, exception"+e.getMessage());
					throw e;
				}
				
			}
			
			
	    	
    	}catch(Exception e){
    		logger.error("findAvailableCntForPeriodId fail exception:"+e.getMessage());
    		throw e;
    	}
	}
	
	
	public static void main(String[] aa){
		
		
		String sBookingDate="2015-04-06";
		String periodId="P14";
		
		isOverTimeOfPeriod( sBookingDate,  periodId);
		
	}
	
	private static boolean isOverTimeOfPeriod(String sBookingDate, String periodId){
		

		Date dNowDateTime=new Date();
		SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
		String sNowDate=dateSdf.format(dNowDateTime);
		boolean isOverTimeOfPeriod =true;
		
		try {
			logger.info("compare date=================");
			logger.info("now         :"+dateSdf.format(dateSdf.parse(sNowDate))+"==>"+dateSdf.parse(sNowDate).getTime());
			logger.info("dBookingDate:"+dateSdf.format(dateSdf.parse(sBookingDate))+"==>"+dateSdf.parse(sBookingDate).getTime());
			
			if(dateSdf.parse(sNowDate).getTime()==dateSdf.parse(sBookingDate).getTime()){
				logger.info("compare time=================");
				String bookingDateTime=sBookingDate+" "+getStartTimeByPeriodId(periodId);		
				SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dBookingDateTime = null;
				try {
					dBookingDateTime=timeSdf.parse(bookingDateTime);
				} catch (ParseException e) {
					logger.error("parse time error");
				}
				logger.info("nowDateTime     :"+timeSdf.format(dNowDateTime)+"==>"+dNowDateTime.getTime());
				logger.info("dBookingDatetime:"+timeSdf.format(dBookingDateTime)+"==>"+dBookingDateTime.getTime());
				
				if(dNowDateTime.getTime()<dBookingDateTime.getTime()){
					isOverTimeOfPeriod= false;
				}
			}else if (dateSdf.parse(sNowDate).getTime()<dateSdf.parse(sBookingDate).getTime()){
				isOverTimeOfPeriod= false;
			}
			
		} catch (ParseException e1) {
			logger.error("parse date error");
		}
		
		logger.info("isOverTimeOfPeriod==>"+isOverTimeOfPeriod);
		return isOverTimeOfPeriod;
	}
	
	private static String getStartTimeByPeriodId(String periodId){
		String startTime="";
		switch(OcEnum.valueOf(periodId)){
			case P11 :
				startTime=OcConstants.P11_Start_Time;
				break;
			case P12 :
				startTime=OcConstants.P12_Start_Time;
				break;
			case P13 :
				startTime=OcConstants.P13_Start_Time;
				break;
			case P14 :
				startTime=OcConstants.P14_Start_Time;
				break;
				
		}
				
		return startTime;
		
		
	} 
	

	@Override
	public String findLatestPromotionsUrl() throws Exception {
		
		try{
			return this.findAttrValueByParam(OcConstants.CATG_LASTEST_PROMOTIONS,OcConstants.PARAM_URL);
	    	
    	}catch(Exception e){
    		logger.error("findLatestPromotionsUrl fail exception:"+e.getMessage());
    		throw e;
    	}
	}
	
	

	@Override
	public OcSettingVo findOcSetting() throws Exception {
		
		OcSettingVo ocSettingVo =new OcSettingVo();
		try{
			Date nowDate=new Date();
			String year=String.valueOf(nowDate.getYear()+1900);
			String closedDay =this.findAttrValueByParam(year+OcConstants.CATG_Year,OcConstants.PARAM_CLOSED_DAY);
			String childrenCount =this.findAttrValueByParam(year+OcConstants.CATG_CHILD_COUNT,OcConstants.PARAM_MAX_LIMIT);
			String nationalHoliday =this.findAttrValueByParam(year+OcConstants.CATG_Year,OcConstants.PARAM_CLOSED_DAY);
			String latestPromotionsUrl=this.findAttrValueByParam(OcConstants.CATG_LASTEST_PROMOTIONS,OcConstants.PARAM_URL);
			
			ocSettingVo.latestPromotionsUrl =latestPromotionsUrl;
			ocSettingVo.closedDay =closedDay;
			ocSettingVo.childrenCount =childrenCount;
			ocSettingVo.nationalHoliday =nationalHoliday;			
			
			return ocSettingVo;
	    	
    	}catch(Exception e){
    		logger.error("findOcSetting fail exception:"+e.getMessage());
    		throw e;
    	}
	}
	
	

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public void updateAttrValueByParam(String category, String paramName, String paramValue) throws Exception {

    	try{
    		OcAddAttr obj=ourCastleDao.findOcAddAttrByCond(category,paramName);
    		if (obj!=null){
    			//merge
    			obj.setParamValue(paramValue);
    		}else{

    			//insert
    			for (int i =1;i<=9;i++){

					SimpleDateFormat sdf = new SimpleDateFormat("MMddHH");
					String seq=sdf.format(new Date())+"-"+i;
    				try {
    					logger.info("insertAttrValueByParam seq="+seq);
    					OcAddAttr newObj= new OcAddAttr();
    					newObj.setSeq(seq);
    					newObj.setCategory(category);
    					newObj.setParamName(paramName);
    					newObj.setParamValue(paramValue);
    					newObj.setParamDesc("");    					
    					ourCastleDao.persistAddAttr(newObj);
    				}catch(Exception e){
    					logger.error("insertAttrValueByParam("+seq+") fail exception:"+e.getMessage()+", try again.");
    					
    				}
    			}
    		}
    	}catch(Exception e){
    		logger.error("updateAttrValueByParam fail exception:"+e.getMessage());
    		throw e;
    	}
	}
    
    
    private static boolean noGoodNightPeriod(String sBookingDate){
		

		boolean isNoGoodNightPeriod =false;
			
			SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date startDate =dateSdf.parse("2015-07-01");
				Date bookingDate=dateSdf.parse(sBookingDate);
				if (bookingDate.getTime()>=startDate.getTime()){
					isNoGoodNightPeriod=true;
				}
			} catch (Exception e) {
	    		logger.error("check oGoodNightPeriod fail exception:"+e.getMessage());
				isNoGoodNightPeriod=true;
			}
			return isNoGoodNightPeriod;
	}

}