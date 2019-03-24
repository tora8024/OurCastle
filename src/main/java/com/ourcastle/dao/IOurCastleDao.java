package com.ourcastle.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.ourcastle.orm.OcAddAttr;
import com.ourcastle.orm.OcOrderInfo;
import com.ourcastle.orm.OcOrderInfoPK;
import com.ourcastle.orm.OcPeriodOfTimeInfo;

public interface IOurCastleDao {

	List<OcPeriodOfTimeInfo> findOcPeriodOfTimeInfo();

	OcOrderInfo findOcOrderInfo(OcOrderInfoPK pk) throws Exception;

	EntityManager getEntityManager();

	void setEntityManager(EntityManager entityManager);

	void persist(OcOrderInfo ocOrderInfo) throws Exception;

	OcPeriodOfTimeInfo findOcPeriodOfTimeInfoByCond(String periodId) throws Exception;

	OcAddAttr findOcAddAttrByCond(String category, String paramName)
			throws Exception;

	List<OcOrderInfo> findOrderByParam(String phoneNum, String periodId,
			String startDate, String endDate, String parentName) throws Exception;

	OcOrderInfo findOrderById(String phoneNum, String periodId,
			String bookingDate, String orderId) throws Exception;

	String sumChildrenCnt(String periodId, String bookingDate) throws Exception;

	List<OcPeriodOfTimeInfo> findOcPeriodOfTimeInfoByDayType(String dayType)
			throws Exception;

	void persistAddAttr(OcAddAttr OcAddAttr) throws Exception;
	
	

}