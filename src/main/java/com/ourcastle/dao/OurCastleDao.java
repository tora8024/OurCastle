package com.ourcastle.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.ourcastle.orm.OcAddAttr;
import com.ourcastle.orm.OcOrderInfo;
import com.ourcastle.orm.OcOrderInfoPK;
import com.ourcastle.orm.OcPeriodOfTimeInfo;
import com.ourcastle.utils.OcConstants;
 
@Repository
public class OurCastleDao implements IOurCastleDao
{
      
	static Logger logger = LoggerFactory.getLogger(OurCastleDao.class);
	
	@PersistenceContext(unitName = "testPU")
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	

	@Override
	public List<OcPeriodOfTimeInfo> findOcPeriodOfTimeInfo() {
		Query query = entityManager.createNativeQuery("select * from oc_period_of_time_info a",OcPeriodOfTimeInfo.class);
		return (List<OcPeriodOfTimeInfo>)query.getResultList();
	}
	
	@Override
	public OcPeriodOfTimeInfo findOcPeriodOfTimeInfoByCond(String periodId) throws Exception {
		try{
			logger.info("findOcPeriodOfTimeInfoByCond==>start");
			String sql ="select * from oc_period_of_time_info a where a.period_id = '"+periodId+"'";
			logger.info("sql==>"+sql);
			Query query = entityManager.createNativeQuery(sql,OcPeriodOfTimeInfo.class);
			List<OcPeriodOfTimeInfo> result =(List<OcPeriodOfTimeInfo>)query.getResultList();
			if(result!=null){
				logger.info("findOcPeriodOfTimeInfoByCond==>result size="+result.size());

				logger.info("findOcPeriodOfTimeInfoByCond==>end");
				return result.get(0);
			}else{
				String msg="findOcPeriodOfTimeInfoByCond==>not found by periodId:"+periodId;
				logger.error(msg);
				throw new Exception(msg);
			}
		}catch(Exception e){
			logger.error("findOcPeriodOfTimeInfoByCond fail exception:"+e.getMessage());
			throw e;
		}
	}
	
	
	@Override
	public List<OcPeriodOfTimeInfo> findOcPeriodOfTimeInfoByDayType(String dayType) throws Exception {
		try{
			logger.info("findOcPeriodOfTimeInfoByDayType==>start");
			
			
			String sql ="select * from oc_period_of_time_info a ";
			
			
			if (dayType.equalsIgnoreCase(OcConstants.DAY_TYPE_NORMAL_DAY)){
				sql+="where a.category = '"+OcConstants.DAY_TYPE_NORMAL_DAY+"'";
			}else{
				sql+="where a.category in ('"+OcConstants.DAY_TYPE_NORMAL_DAY+"','"+OcConstants.DAY_TYPE_HOLIDAY+"')";
			}
			logger.info("sql==>"+sql);
			Query query = entityManager.createNativeQuery(sql,OcPeriodOfTimeInfo.class);
			List<OcPeriodOfTimeInfo> result =(List<OcPeriodOfTimeInfo>)query.getResultList();
			logger.info("findOcPeriodOfTimeInfoByDayType==>result size="+result.size());
			logger.info("findOcPeriodOfTimeInfoByDayType==>end");
			return result;
		}catch(Exception e){
			logger.error("findOcPeriodOfTimeInfoByDayType fail exception:"+e.getMessage());
			throw e;
		}
	}
	
	
	
	@Override
	public OcAddAttr findOcAddAttrByCond(String category,String paramName) throws Exception {
		try{
			logger.info("findOcAddAttrByCond==>start");
			String sql ="SELECT * FROM oc_add_attr where category='"+category+"' and param_name ='"+paramName+"'";
			logger.info("sql==>"+sql);
			Query query = entityManager.createNativeQuery(sql,OcAddAttr.class);
			List<OcAddAttr> result =(List<OcAddAttr>)query.getResultList();
			if(result!=null){
				logger.info("findOcAddAttrByCond==>result size="+result.size());

				logger.info("findOcAddAttrByCond==>end");
				return result.get(0);
			}else{
				return null;
			}
		}catch(Exception e){
			logger.error("findOcAddAttrByCond fail exception:"+e.getMessage());
			throw e;
		}
	}
	
	
	
	@Override
	public OcOrderInfo findOcOrderInfo(OcOrderInfoPK pk) throws Exception {
		try{
			OcOrderInfo result = entityManager.find(OcOrderInfo.class, pk);
			return result;
		}catch(Exception e){
			logger.error("findOcOrderInfo fail exception:"+e.getMessage());
			throw e;
		}
	}

	
	@Override
	public void persist(OcOrderInfo ocOrderInfo) throws Exception {
		try{
			logger.info("persist==>start");
			entityManager.persist(ocOrderInfo);
			entityManager.flush();
			logger.info("persist==>end");
		}catch(Exception e){
			logger.error("persist==>error");
			logger.error("insertOrder fail exception:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void persistAddAttr(OcAddAttr OcAddAttr) throws Exception {
		try{
			logger.info("persistAddAttr==>start");
			entityManager.persist(OcAddAttr);
			entityManager.flush();
			logger.info("persistAddAttr==>end");
		}catch(Exception e){
			logger.error("persistAddAttr==>error");
			logger.error("persistAddAttr fail exception:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<OcOrderInfo> findOrderByParam(String phoneNum,
			String periodId, String startDate, String endDate, String parentName) throws Exception {

		try{
			logger.info("findOrderByParam==>start");
			String sql ="SELECT * FROM oc_order_info ";
			
			List<Map<String,String>> paramValueList=new ArrayList<Map<String,String>>();
			if(phoneNum!=null&&phoneNum.length()>0){
				Map<String,String> map=new HashMap<String,String>();
				map.put("ColName", "phone_num");
				map.put("ColValue", phoneNum);
				paramValueList.add(map);
			}
			
//			if(periodId!=null&&periodId.length()>0){
//				Map<String,String> map=new HashMap<String,String>();
//				map.put("ColName", "period_id");
//				map.put("ColValue", periodId);
//				paramValueList.add(map);
//			}

			if(parentName!=null&&parentName.length()>0){
				Map<String,String> map=new HashMap<String,String>();
				map.put("ColName", "parent_name");
				map.put("ColValue", parentName);
				paramValueList.add(map);
			}
			
			if(paramValueList.size()>0){
				
				int i=1;
				for (Map<String,String> map:paramValueList){
					boolean likeScript=(map.get("ColValue").indexOf("%")>-1?true:false);
					if(i==1){						
						sql +="where "+map.get("ColName") + (likeScript? " like " : " = ") + "'"+map.get("ColValue")+"' ";
					}else{
						sql +="and "+map.get("ColName") + (likeScript? " like " : " = ") + "'"+map.get("ColValue")+"' ";
					}
					i++;
				}
				
				if(startDate!=null&&startDate.length()>0&&
					endDate!=null&&endDate.length()>0){
					sql +="and booking_date >= '" +startDate+ "' and booking_date <= '" +endDate +"' ";
				}
				
				if(periodId!=null&&periodId.length()>0){
					String[] periodIdArr=periodId.split(",");
					String temp="";
					for(int x=0;x<periodIdArr.length;x++){
						temp = temp+"'"+ periodIdArr[x]+"'";
						if(x<=periodIdArr.length-2){
							temp = temp+",";
						}
					}
					sql +=" and period_id in ("+temp+") ";
				}
				
				
			}else{
				
				if(startDate!=null&&startDate.length()>0&&
						endDate!=null&&endDate.length()>0){
						sql +="where booking_date >= '" +startDate+ "' and booking_date <= '" +endDate +"' ";
					}
				
				if(periodId!=null&&periodId.length()>0){
					String[] periodIdArr=periodId.split(",");
					String temp="";
					for(int x=0;x<periodIdArr.length;x++){
						temp = temp+"'"+ periodIdArr[x]+"'";
						if(x<=periodIdArr.length-2){
							temp = temp+",";
						}
					}
					sql +=" and period_id in ("+temp+") ";
				}
				
			}
			sql +=" order by booking_date ";
			
			logger.info("sql==>"+sql);
			Query query = entityManager.createNativeQuery(sql,OcOrderInfo.class);
			List<OcOrderInfo> result =(List<OcOrderInfo>)query.getResultList();
			logger.info("findOrderByParam==>end");
			return result;
		}catch(Exception e){
			logger.error("findOrderByParam fail exception:"+e.getMessage());
			throw e;
		}
	}
	
	
	
	@Override
	public OcOrderInfo findOrderById(String phoneNum, String periodId, String bookingDate ,String orderId) throws Exception {

		try{
			logger.info("findOrderById==>start");
			String sql ="SELECT * FROM oc_order_info ";
			sql +=" where order_id ='"+orderId+"' ";
			sql +=" and phone_num ='"+phoneNum+"' ";
			sql +=" and period_id ='"+periodId+"' ";
			sql +=" and booking_date ='"+bookingDate+"' ";
			
			logger.info("sql==>"+sql);
			Query query = entityManager.createNativeQuery(sql,OcOrderInfo.class);
			List<OcOrderInfo> result =(List<OcOrderInfo>)query.getResultList();

			logger.info("findOrderById==>end");
			return result.get(0);
		}catch(Exception e){
			logger.error("findOrderById fail exception:"+e.getMessage());
			throw e;
		}
	}
	
	@Override
	public String sumChildrenCnt(String periodId, String bookingDate) throws Exception {
			String sumCnt="0";
		try{
			logger.info("sumChildrenCnt==>start");
			String sql ="SELECT sum(children_cnt) FROM oc_order_info ";
			sql +=" where period_id ='"+periodId+"' ";
			sql +=" and booking_date ='"+bookingDate+"' ";
			sql +=" and booking_status ='"+OcConstants.STATUS_Open+"' ";
			
			logger.info("sql==>"+sql);
			Query query = entityManager.createNativeQuery(sql);
			List<String> result =(List<String>)query.getResultList();
			if(result!=null&& result.size()>0 &&result.get(0)!=null){
				sumCnt=String.valueOf(result.get(0));
			}
			logger.info("sumChildrenCnt==>end");
			return sumCnt;
		}catch(Exception e){
			logger.error("sumChildrenCnt fail exception:"+e.getMessage());
			throw e;
		}
	}
	
    
}