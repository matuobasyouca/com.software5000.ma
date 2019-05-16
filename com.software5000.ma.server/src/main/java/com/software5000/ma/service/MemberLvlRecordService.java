package com.software5000.ma.service;

import com.github.pagehelper.PageInfo;
import com.software5000.base.MyBaseDao;
import com.software5000.ma.entity.Car;
import com.software5000.ma.entity.MemberLvl;
import com.software5000.ma.entity.MemberLvlRecord;
import com.software5000.ma.entity.RechargeOrder;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.mybatis.plugins.PermissionHelper;
import com.zscp.master.util.MathUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MemberLvlRecordService {
    private Log log = LogFactory.getLog(MemberLvlRecordService.class);

    @Resource
    private MyBaseDao baseDao;

    @Resource
    private CarService carService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    public MemberLvlRecord insertMemberLvlRecord(MemberLvlRecord memberLvlRecord) throws SQLException {
        return (MemberLvlRecord) baseDao.insertEntity(memberLvlRecord);
    }

    /**
     * 给商家新增用户，如果已经存在用户不新增
     *
     * @param userId
     * @param businessId
     */
    public void insertMemberLvlRecord(Integer userId, Integer businessId) throws SQLException {
        PermissionHelper.ignorePermissionThisTime();
        MemberLvlRecord param = new MemberLvlRecord();
        param.setUserId(userId);
        param.setBusinessId(businessId);
        MemberLvlRecord memberLvlRecord = baseDao.selectSingleEntity(param);
        if (memberLvlRecord == null) {
            memberLvlRecord = new MemberLvlRecord();
            memberLvlRecord.setBusinessId(businessId);
            memberLvlRecord.setUserId(userId);
            memberLvlRecord.setTotalConsume(0D);
            baseDao.insertEntity(memberLvlRecord);
        }

    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 根据会员等级记录的userId删除会员
     *
     * @param userId
     * @throws SQLException
     */
    public void deleteMemberLvlRecord(Integer userId) throws SQLException {
        MemberLvlRecord memberLvlRecord = new MemberLvlRecord();
        memberLvlRecord.setUserId(userId);
        baseDao.deleteEntity(memberLvlRecord);
    }

    /**
     * 根据会员编号删除会员记录
     */
    public void deleteMemberLvlRecords(String userIds) throws SQLException {
        if (null != userIds && userIds.length() > 0) {
            Map param = new HashMap();
            param.put("ids", userIds);
            PermissionHelper.ignorePermissionThisTime();
            baseDao.delete(MemberLvlRecord.Daos.deleteMemberLvlRecords.sqlMapname, param);
        }
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 根据消费金额升级会员权限 userId，businessId通过权限控制可以不填
     *
     * @param userId
     * @param businessId
     * @param monetary   消费金额
     * @throws ServiceException
     */
    public MemberLvlRecord upgradeMemberLvlRecord(Integer userId, Integer businessId, Double monetary) throws SQLException {
        MemberLvlRecord memberLvlRecord = selectMemberLvlRecordWithMemberLvl(userId, businessId);
        if (memberLvlRecord != null) {
            Double lessConsume = memberLvlRecord.getTotalConsume();
            lessConsume = null == lessConsume ? 0D : lessConsume;
            Double greaterConsume = MathUtil.add(lessConsume, monetary);
            memberLvlRecord.setTotalConsume(greaterConsume);
            if (memberLvlRecord.getTotalTimes() == null) memberLvlRecord.setTotalTimes(0);
            memberLvlRecord.setTotalTimes(monetary >= 0 ? memberLvlRecord.getTotalTimes() + 1 : memberLvlRecord.getTotalTimes() - 1);
            if (memberLvlRecord.getTotalTimes() < 0) memberLvlRecord.setTotalTimes(0);
            baseDao.updateEntity(memberLvlRecord, "id", true);
        } else {
            if (carService.selectUserCarById(userId).size() != 0) {
                memberLvlRecord = new MemberLvlRecord();
                memberLvlRecord.setTotalConsume(monetary);
                memberLvlRecord.setUserId(userId);
                memberLvlRecord.setBusinessId(businessId);
                memberLvlRecord.setTotalTimes(1);
                baseDao.insertEntity(memberLvlRecord);
            }
        }
        return memberLvlRecord;
    }

    /**
     * 修改会员的会员备注
     *
     * @param memberLvlRecord
     * @throws SQLException
     */
    public void updateMemberLvlRecord(MemberLvlRecord memberLvlRecord) throws SQLException {
        baseDao.updateEntity(memberLvlRecord, "remarks", true);
    }

    /**
     * @param memberLvlRecord 修改会员余额
     */

    public void updateMemberBalance(MemberLvlRecord memberLvlRecord) throws SQLException {
        baseDao.updateEntity(memberLvlRecord, "memberBalance", true);

    }

    /**
     * 会员充值
     */
    public MemberLvlRecord updateMemberLvl(Integer userId, Integer businessId, Double rechargeMoney, Double grantMoney, boolean flag) throws SQLException {
        MemberLvlRecord memberLvlRecord = selectMemberLvlRecordWithMemberLvl(userId, businessId);
        if (null == memberLvlRecord) {
            memberLvlRecord = new MemberLvlRecord();
            memberLvlRecord.setUserId(userId);
            memberLvlRecord.setBusinessId(businessId);
            memberLvlRecord = insertMemberLvlRecord(memberLvlRecord);
        }
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("businessId", businessId);
        param.put("state", Constant.OrderState.PAID.codeName);
        if (flag) {
            memberLvlRecord = upgradeMemberLvlRecord(userId, businessId, rechargeMoney);
            Double maxRechargeMoney = (Double) baseDao.selectObject(RechargeOrder.Daos.selectMaxReChargeMoneyById.sqlMapname, param);
            maxRechargeMoney = maxRechargeMoney == null ? 0D : maxRechargeMoney;
            if (rechargeMoney >= maxRechargeMoney) {
                param.put("rechargeMoney", rechargeMoney);
                Integer lvlId = selectMemberLvlId(param);
                if (null != lvlId) {
                    memberLvlRecord.setMemberLvlId(lvlId);
                }
            }
        }
        memberLvlRecord.setMemberBalance(null == memberLvlRecord.getMemberBalance() ? MathUtil.add(rechargeMoney, grantMoney) : MathUtil.add(MathUtil.add(memberLvlRecord.getMemberBalance(), rechargeMoney), grantMoney));
        updateMemberLvlRecord(memberLvlRecord);
        return memberLvlRecord;
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    //分页查询商家的会员信息
    public PageInfo selectByPage(Map<String, Object> paramMap) throws SQLException {

        //因为车辆导致分页异常，先查询出会员记录ID
        PageInfo pageInfo = baseDao.selectEntitiesByPage(MemberLvlRecord.Daos.selectPageMemberLvlRecordIdByParam.sqlMapname, paramMap,
                Integer.valueOf(paramMap.getOrDefault("startPage", 1).toString()),
                Integer.valueOf(paramMap.getOrDefault("pageSize", 10).toString()), "id desc");
        //如果有数据的时候，就去获取详细信息
        if (pageInfo.getList() != null && pageInfo.getList().size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ids", pageInfo.getList().stream().map(s -> s.toString()).collect(Collectors.joining(",")));
            pageInfo.setList(baseDao.selectList(MemberLvlRecord.Daos.selectPageMemberLvlRecordByParam.sqlMapname, map));
        }
        return pageInfo;
    }


    /**
     * 根据会员等级记录id查看该用户信息、车辆及会员等级
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public MemberLvlRecord selectMemberLvlRecordById(Map<String, Object> paramMap) throws SQLException {
        MemberLvlRecord memberLvlRecord = (MemberLvlRecord) baseDao.selectObject(MemberLvlRecord.Daos.selectMemberLvlRecordByParam.sqlMapname, paramMap);
        Car car = new Car();
        car.setUserId(memberLvlRecord.getUserId());
        car.setCarState(Constant.CarState.NORMAL.codeName);
        memberLvlRecord.getUser().setCars(baseDao.selectEntity(car));
        return memberLvlRecord;
    }

    /**
     * 根据用户id查询出用户会员记录MemberLvlRecord及关联的MemberLvl
     *
     * @param userId
     * @return
     * @throws ServiceException
     */
    public MemberLvlRecord selectMemberLvlRecordWithMemberLvl(Integer userId, Integer businessId) throws SQLException {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("businessId", businessId);
        Object o = baseDao.selectObject(MemberLvlRecord.Daos.selectMemberLvlRecordAndMemberLvlByUserId.sqlMapname, map);
        return o == null ? null : (MemberLvlRecord) o;
    }

    /**
     * 根据累计消费金额区间查询出商家级别最高的会员(lessConsume<结果<=greaterConsume)
     *
     * @param greaterConsume
     * @return
     * @throws ServiceException
     */
    public MemberLvl selectByConsumeTopOne(Double greaterConsume, Integer businessId) throws SQLException {
        Map paramMap = new HashMap();
        paramMap.put("greaterConsume", greaterConsume);
        paramMap.put("businessId", businessId);
        return (MemberLvl) baseDao.selectObject(MemberLvl.Daos.selectByConsumeTopOne.sqlMapname, paramMap);
    }

    /**
     * 通过会员等级记录id查询会员等级记录
     *
     * @param memberLvlRecordId
     * @return
     * @throws SQLException
     */
    public MemberLvlRecord selectMemberLvlRecordById(Integer memberLvlRecordId) throws SQLException {
        return baseDao.selectEntityById(memberLvlRecordId, MemberLvlRecord.class);
    }

    /**
     * 查询会员等级ID
     */
    public Integer selectMemberLvlId(Map param) throws SQLException {
        return (Integer) baseDao.selectObject(MemberLvl.Daos.selectByRechargeTopOne.sqlMapname, param);
    }

    /**
     * 查询会员数量
     */
    public Integer selectUserCount(Map param) throws SQLException {
        return (Integer) baseDao.selectObject(MemberLvlRecord.Daos.selectUserCount.sqlMapname, param);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}