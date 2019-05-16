package com.software5000.ma.service;

/**
 * Created by jiye on 2017/7/19.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.MyBaseDao;
import com.software5000.base.ServiceException;
import com.software5000.ma.dto.MemberPackageRecordDto;
import com.software5000.ma.dto.PackClusterBuyRecordDto;
import com.software5000.ma.dto.WxMsgDto;
import com.software5000.ma.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberPackageRecordService {
    private Log log = LogFactory.getLog(MemberPackageRecordService.class);

    @Resource
    private MyBaseDao baseDao;

    @Resource
    private BusinessPackageService businessPackageService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增会员套餐记录
     * @param memberPackageRecord
     * @return
     * @throws SQLException
     */
    public MemberPackageRecord insertMemberPackageRecord(MemberPackageRecord memberPackageRecord) throws SQLException {
        return (MemberPackageRecord) baseDao.insertEntity(memberPackageRecord);
    }

    /**
     * 会员套餐生成
     */
    public void insertMemberPackageRecord(BusinessPackageOrder businessPackageOrder) throws SQLException{
        Map param = new HashMap();
        param.put("businessPackageId",businessPackageOrder.getBusinessPackageId());
        BusinessPackage businessPackage= businessPackageService.selectBusinessPackageById(param);
        List<PackageAndItem> itemList = businessPackage.getPackageAndItems();
        MemberItemUseRecord memberItemUseRecord;
        MemberPackageRecord memberPackageRecord;
        for(int i=0;i<businessPackageOrder.getQuantity();i++) {
            memberPackageRecord = new MemberPackageRecord();
            memberPackageRecord.setBusinessId(businessPackageOrder.getBusinessId());
            memberPackageRecord.setBusinessPackageId(businessPackageOrder.getBusinessPackageId());
            memberPackageRecord.setUserId(businessPackageOrder.getUserId());
            memberPackageRecord.setPackageOrderId(businessPackageOrder.getId());
            memberPackageRecord.setValidTime(getValidTime(businessPackage.getValidTerm()));
            memberPackageRecord = insertMemberPackageRecord(memberPackageRecord);
            for (PackageAndItem packageAndItem : itemList) {
                memberItemUseRecord = new MemberItemUseRecord();
                memberItemUseRecord.setMemberPackageRecordId(memberPackageRecord.getId());
                memberItemUseRecord.setRemainTimes(packageAndItem.getUseTimes());
                memberItemUseRecord.setServiceItemId(packageAndItem.getServiceItemId());
                memberItemUseRecord.setUseTimes(packageAndItem.getUseTimes());
                baseDao.insertEntity(memberItemUseRecord);
            }
        }
    }

    /**
     * 拼团用户套餐生成
     * @param packClusterBuyRecords
     * @param businessPackId
     * @throws SQLException
     */
    public void insertMemberPackageRecordByPackCluster(List<PackClusterBuyRecordDto> packClusterBuyRecords, Integer businessPackId) throws SQLException {
        Map param = new HashMap();
        param.put("businessPackageId",businessPackId);
        BusinessPackage businessPackage= businessPackageService.selectBusinessPackageById(param);
        List<PackageAndItem> itemList = businessPackage.getPackageAndItems();
        List<MemberItemUseRecord> memberItemUseRecords = new ArrayList<>();
        for (PackClusterBuyRecordDto packClusterBuyRecord : packClusterBuyRecords) {
            MemberPackageRecord memberPackageRecord = new MemberPackageRecord();
            memberPackageRecord.setBusinessId(businessPackage.getBusinessId());
            memberPackageRecord.setBusinessPackageId(businessPackId);
            memberPackageRecord.setUserId(packClusterBuyRecord.getUserId());
            memberPackageRecord.setPackageOrderId(null);
            memberPackageRecord.setValidTime(getValidTime(businessPackage.getValidTerm()));
            memberPackageRecord = insertMemberPackageRecord(memberPackageRecord);
            for (PackageAndItem packageAndItem : itemList) {
                MemberItemUseRecord memberItemUseRecord = new MemberItemUseRecord();
                memberItemUseRecord.setMemberPackageRecordId(memberPackageRecord.getId());
                memberItemUseRecord.setRemainTimes(packageAndItem.getUseTimes());
                memberItemUseRecord.setServiceItemId(packageAndItem.getServiceItemId());
                memberItemUseRecord.setUseTimes(packageAndItem.getUseTimes());
                memberItemUseRecords.add(memberItemUseRecord);
            }
        }
        baseDao.insertEntities(memberItemUseRecords);
    }


    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 套餐卡使用
     */
    public Map<Integer,Integer> updateMemberPackageRecordByUse(Integer userId, Map<Integer,Integer> serviceItems,Integer wokerOrderId) throws SQLException{
        Map param=new HashMap();
        param.put("userId",userId);
        List<MemberPackageRecord> memberPackageRecords = selectMemberPackageRecord(param);
        //遍历套餐进行次数扣减
        WorkItemUserRecord workItemUserRecord;
        for (MemberPackageRecord memberPackageRecord : memberPackageRecords) {
            List<MemberItemUseRecord> memberItemUseRecords = memberPackageRecord.getMemberItemUseRecords();
            for (MemberItemUseRecord memberItemUseRecord : memberItemUseRecords) {
                if (serviceItems.keySet().contains(memberItemUseRecord.getServiceItemId())) {
                    //剩余次数
                    Integer remainTimes = memberItemUseRecord.getRemainTimes();
                    //使用了服务项次数
                    Integer itemTimes = serviceItems.get(memberItemUseRecord.getServiceItemId());
                    //还有剩余次数则扣减
                    if (remainTimes > 0) {
                        workItemUserRecord=new WorkItemUserRecord();
                        workItemUserRecord.setWorkOrderId(wokerOrderId);
                        workItemUserRecord.setMemberItemUseRecordId(memberItemUseRecord.getId());
                        //单个套餐就能满足扣减
                        if(remainTimes-itemTimes>=0){
                            serviceItems.remove(memberItemUseRecord.getServiceItemId());
                            memberItemUseRecord.setRemainTimes(remainTimes - itemTimes);
                            baseDao.updateEntity(memberItemUseRecord);
                            workItemUserRecord.setTimes(itemTimes);
                            baseDao.insertEntity(workItemUserRecord);
                        }else{//需要多个套餐和起来进行扣减
                            memberItemUseRecord.setRemainTimes(0);
                            serviceItems.put(memberItemUseRecord.getServiceItemId(),itemTimes-remainTimes);
                            baseDao.updateEntity(memberItemUseRecord);
                            workItemUserRecord.setTimes(remainTimes);
                            baseDao.insertEntity(workItemUserRecord);
                        }
                    }
                }
            }
        }
        return serviceItems;
    }

    /**
     * 套餐卡反结算
     */
    public void updateMemberPackageRecordByCounter(Integer wokerOrderId) throws SQLException{
        WorkItemUserRecord workItemUserRecord=new WorkItemUserRecord();
        workItemUserRecord.setWorkOrderId(wokerOrderId);
        List<WorkItemUserRecord> workItemUserRecords=baseDao.selectEntity(workItemUserRecord);
        MemberItemUseRecord memberItemUseRecord;
        for(WorkItemUserRecord workItemUseRecord:workItemUserRecords){
            memberItemUseRecord=new MemberItemUseRecord();
            memberItemUseRecord.setId(workItemUseRecord.getMemberItemUseRecordId());
            memberItemUseRecord=baseDao.selectSingleEntity(memberItemUseRecord);
            memberItemUseRecord.setRemainTimes(memberItemUseRecord.getRemainTimes()+workItemUseRecord.getTimes());
            baseDao.updateEntity(memberItemUseRecord);
            baseDao.deleteEntity(workItemUseRecord);
        }
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据用户id及商家id查看该用户在商家下的套餐
     *
     * @param param
     * @return
     * @throws ServiceException
     */
    public List<MemberPackageRecord> selectMemberPackageRecord(Map param) throws SQLException {
        return (List<MemberPackageRecord>) baseDao.selectList(MemberPackageRecord.Daos.selectByParam.sqlMapname, param);
    }

    /**
     * 获取工单使用的套餐信息
     * @param orderId
     * @return
     * @throws SQLException
     */
    public List<WxMsgDto> selectWorkOrderPackageMsg(Integer orderId) throws SQLException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orderId", orderId);
        return (List<WxMsgDto>)baseDao.selectList(MemberPackageRecord.Daos.selectWorkOrderPackageMsg.sqlMapname, param);
    }

    /**
     * 获取即将到期的套餐为微信消息信息
     * @return
     * @throws SQLException
     */
    public List<WxMsgDto> selectExpirePackageMsg() throws SQLException {
        return (List<WxMsgDto>)baseDao.selectList(MemberPackageRecord.Daos.selectExpirePackageMsg.sqlMapname);
    }

    /**
     * 根据用户openid查看已购买套餐
     */
    public PageInfo selectMemberPackageRecordByOpenId(Map param) throws ServiceException{
        try {
            PageInfo pageInfo=baseDao.selectEntitiesByPage(MemberPackageRecord.Daos.selectMemberPackageRecordIds.sqlMapname,param,Integer.parseInt(param.getOrDefault("startPage",1).toString()),Integer.parseInt(param.getOrDefault("pageSize",10).toString()),null);
            if(pageInfo.getList().size()>0) {
                param.put("ids", pageInfo.getList());
                pageInfo.setList((List<MemberPackageRecordDto>) baseDao.selectList(MemberPackageRecord.Daos.selectMemberPackageRecordByOpenId.sqlMapname, param));
            }
            return pageInfo;
        } catch (Exception e) {
            log.error("查询失败，param="+param,e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }

    /**
     * 查询用户各状态下套餐卡数量
     */
    public List<Map> selectMemberPackageRecordCount(Map param) throws ServiceException{
        try{
            return (List<Map>)baseDao.selectList(MemberPackageRecord.Daos.selectMemberPackageRecordCount.sqlMapname,param);
        }catch (Exception e) {
            log.error("查询失败，param="+param,e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>

    private Timestamp getValidTime(Integer validTerm){
        if (validTerm == null || 0 == validTerm) {
           return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, validTerm);
            return (Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime())));
        }
    }

}