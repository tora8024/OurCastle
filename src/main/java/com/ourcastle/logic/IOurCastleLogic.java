package com.ourcastle.logic;

import java.util.List;

import org.springframework.mail.MailSender;

import com.ourcastle.dao.IOurCastleDao;
import com.ourcastle.model.AvailablePeriodVo;
import com.ourcastle.model.OcOrderInfoVo;
import com.ourcastle.model.OcSettingVo;
import com.ourcastle.orm.OcAddAttr;
import com.ourcastle.orm.OcPeriodOfTimeInfo;

public interface IOurCastleLogic {

	List<OcPeriodOfTimeInfo> findOcPeriodOfTimeInfo() throws Exception;

	void submitOrder(OcOrderInfoVo orderVo) throws Exception;

	IOurCastleDao getOurCastleDao();

	void setOurCastleDao(IOurCastleDao ourCastleDao);

	MailSender getMailSender();

	void setMailSender(MailSender mailSender);

	OcPeriodOfTimeInfo findOcPeriodOfTimeInfoByCond(String periodId) throws Exception;

	String getDayOfWeek(String bookingDate);

	void sendMail(String to, String subject, String body) throws Exception;

	OcAddAttr findOcAddAttrByCond(String category, String paramName)
			throws Exception;

	String findAttrValueByParam(String category, String paramName) throws Exception;


	List<OcOrderInfoVo> findOrderByParam(String phoneNum, String periodId,
			String startDate, String endDate, String parentName)
			throws Exception;

	void cancelOrder(OcOrderInfoVo orderVo) throws Exception;

	List<AvailablePeriodVo> findAvailablePeriod(String bookingDate) throws Exception;

	boolean isClosedDay(String bookingDate);

	String findLatestPromotionsUrl() throws Exception;

	OcSettingVo findOcSetting() throws Exception;

	void updateAttrValueByParam(String category, String paramName,
			String paramValue) throws Exception;


}