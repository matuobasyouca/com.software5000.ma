package com.software5000.ma.service;

/**
 * Created by jiye on 2017/7/19.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.ma.dto.BusinessCarDto;
import com.software5000.ma.entity.Car;
import com.software5000.ma.entity.MemberLvlRecord;
import com.software5000.ma.entity.User;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.security.UserDefaultUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

@Service
public class UserService {
    private Log log = LogFactory.getLog(UserService.class);

    @Resource
    private BaseDao baseDao;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private CarService carService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/


    /**
     * 新增用户及商家的客户
     * @param wxopenId
     * @param mobile
     * @param carNumber
     * @return
     * @throws SQLException
     */
    public User insertUserAndMemberLvl(String wxopenId, String mobile, String carNumber, Integer businessId) throws SQLException {
        User user = insertUser(wxopenId,mobile,carNumber);
        if(businessId !=null && user!=null) {
            memberLvlRecordService.insertMemberLvlRecord(user.getId(), businessId);
        }
        return user;
    }

    /**
     *工单调用方法，当车牌和手机号存在不同用户时，传回车牌用户
     */
    public User inserUserAndMemberLvlByException(String wxopenId,String mobile,String carNumber,Integer businessId) throws SQLException {
        User user=insertUserAndMemberLvl(wxopenId,mobile,carNumber,businessId);
        if(user == null){
            user=selectUserByCarNo(carNumber);
        }
        return user;
    }

    /**
     * 新增用户
     *
     *规则①：车牌必须传入，如果车牌为空则直接返回null
     *规则②：如果结果集为1，则将手机，车牌，微信都绑定到这个用户上
     *规则③：如果结果集大于1，判断车牌号查询出的用户，如果此用户不存在或者此用户对应的手机号跟微信号都为空，则将该车牌按(优先手机号再微信号)进行合并
     *补充说明规则③：优先手机号再微信号是指：如果手机号带出的用户已经存在就把车牌号与该手机号绑定，否则绑定到微信号带出的用户上
     * @param wxopenId
     * @param mobile
     * @param carNumber
     * @return
     * @throws SQLException
     */
    public User insertUser(String wxopenId,String mobile,String carNumber) throws SQLException {
        //1.判断车牌号
        if(ValidUtil.isEmpty(carNumber)){
            return null;
        }
        //2.获取用户
        User wxUser = selectUserByOpenId(wxopenId);
        User mobileUser = selectUserByMobile(mobile);
        User carUser = selectUserByCarNo(carNumber);
        //3.计算用户的结果集
        Integer countSum = countUserSum(wxUser,mobileUser,carUser);
        //4.根据结果集处理
        //4.1 结果集为0,表示都没有查出用户，则新增
        if(countSum == 0){
          return countSumZero(wxopenId,mobile,carNumber);
        //4.2 结果集为1,表示查询用户都归属一个用户，则将所有的信息更新到此用户
        }else if(countSum == 1){
            Integer userId = wxUser != null?wxUser.getId():mobileUser!=null?mobileUser.getId():carUser.getId();
            return countSumOne(userId, carUser, wxopenId, mobile, carNumber);
        //4.3 结果集为2或者3,如果车牌带不出手机号，或者车牌带出的手机号/微信号为空，则将该车牌按(优先手机号再微信号)进行合并
        } else if(countSum == 2 || countSum == 3){
            return countSumTwoOrThree(carNumber,wxopenId,mobile, wxUser, mobileUser, carUser);
        }
        return null;
    }

    /**
     * 车牌，手机号更新用户逻辑
     * @param mobile
     * @param carNumber
     */
    public boolean checkUser(String mobile,String carNumber) throws SQLException {
        if(ValidUtil.isEmpty(mobile)||ValidUtil.isEmpty(carNumber)){
            return false;
        }
        User carUser = selectUserByCarNo(carNumber);
        User mobileUser = selectUserByMobile(mobile);
        if(carUser == null || mobileUser==null ||(carUser.getId().equals(mobileUser.getId()))||
                (ValidUtil.isEmpty(carUser.getWxOpenId())&&ValidUtil.isEmpty(carUser.getMobile()))){
            return true;
        }
        return false;
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
     * 修改用户信息
     *
     * @param user
     * @throws SQLException
     */
    public void updateUser(User user) throws SQLException {
        baseDao.updateEntityNotEmpty(user);
    }

    /**
     * 根据用户id更新微信openid
     *
     * @param userId
     * @param wxopenId
     * @throws ServiceException
     */
    public void updateWxopenIdByUserId(Integer userId, String wxopenId) throws SQLException {
        //查询openid是否存在,不存在才考虑更新
        if (selectUserByOpenId(wxopenId) == null) {
            //用户的openId为空才更新
            User user = selectUserById(userId);
            if (ValidUtil.isEmpty(user.getWxOpenId())) {
                user.setWxOpenId(wxopenId);
                updateUser(user);
            }
        }
    }

    /**
     * 用户合并操作
     */
    public void updateCarChangeUserId(Integer userId,String userIds)throws SQLException {
        if(null!=userIds&&userIds.length()>0) {
            Map param=new HashMap();
            param.put("ids",userIds);
            param.put("userId",userId);
            baseDao.update(User.Daos.updateCarChangeUserId.sqlMapname,param);
            String businessIds=(String)baseDao.selectObject(MemberLvlRecord.Daos.selectMemberLvlRecordBusiness.sqlMapname,param);
            param.put("businessIds",null==businessIds||"".equals(businessIds)?"0":businessIds);
            baseDao.update(MemberLvlRecord.Daos.updateMemberLvlRecordChangeUserId.sqlMapname,param);
            List<MemberLvlRecord> memberLvlRecords= (List<MemberLvlRecord>) baseDao.selectList(MemberLvlRecord.Daos.selectMemberLvlRecord.sqlMapname,null);
            baseDao.deleteEntitys(memberLvlRecords);
        }
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据ID获取用户信息
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public User selectUserById(Integer id) throws SQLException {
        return baseDao.selectEntityById(id, User.class);
    }

    /**
     * 根据用户手机号返回用户信息
     *
     * @param mobile
     * @return
     * @throws SQLException
     */
    public User selectUserByMobile(String mobile) throws SQLException {
        if(ValidUtil.isEmpty(mobile)) return null;
        User user = new User();
        user.setMobile(mobile);
        List<User> users = selectUser(user);
        if (users.size() <= 0) return null;
        user = users.get(0);
        return user;
    }

    /**
     * 查询用户
     *
     * @param user
     * @return
     */
    public List<User> selectUser(User user) throws SQLException {
        return baseDao.selectEntity(user);
    }

    /**
     * 根据车牌号查询用户
     *
     * @param carNumber 车牌号
     * @return
     */
    public User selectUserByCarNo(String carNumber) throws SQLException {
        if(ValidUtil.isEmpty(carNumber)) return null;
        List<User> users = new ArrayList<>();
        users = (List<User>) baseDao.selectList(User.Daos.selectUserByCarNo.sqlMapname, carNumber);
        if (users.size() < 1) return null;
        return users.get(0);
    }

    /**
     * 判定是否
     */

    /**
     * 查询商家会员信息中的所有车牌号
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo selectCarForMemberLv(Map<String, Object> param) throws SQLException {
        return baseDao.selectListByPage(User.Daos.selectBusinessCarByParam.sqlMapname
                , param
                , Integer.valueOf(param.getOrDefault("startPage", 1).toString())
                , Integer.valueOf(param.getOrDefault("pageSize", 10).toString())
                , null);
    }

    /**
     * 查询会员信息
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo selectBusinessUserByParam(Map<String, Object> param) throws SQLException {
        return baseDao.selectListByPage(User.Daos.selectBusinessUserByParam.sqlMapname
                , param
                , Integer.valueOf(param.getOrDefault("startPage", 1).toString())
                , Integer.valueOf(param.getOrDefault("pageSize", 10).toString())
                , null);
    }

    /**
     * 根据车牌号获取会员信息（包含到店信息）
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public BusinessCarDto selectUserByCarNumberForBusiness(Map<String, Object> param) throws SQLException {
        //获取会员信息
        param.put("businessId", UserDefaultUtil.getBusinessId());
        BusinessCarDto dto = (BusinessCarDto) baseDao.selectObject(User.Daos.selectUserByCarNumberForBusiness.sqlMapname, param);
        if (dto != null && dto.getUserId() != null) {
            //获取会员商家套餐信息
            param.put("userId", dto.getUserId());
            param.put("isNoValid", true);
            dto.setPackageList(memberPackageRecordService.selectMemberPackageRecord(param));
        }
        return dto;
    }

    /**
     * 根据openId获取用户信息
     *
     * @param openId
     * @return
     * @throws SQLException
     */
    public User selectUserByOpenId(String openId) throws SQLException {
        if(ValidUtil.isEmpty(openId)) return null;
        Object o = baseDao.selectObject(User.Daos.selectUserByOpenId.sqlMapname, openId);
        return o==null?null:(User)o ;
    }

    /**
     * 查询出open 或者 openId 或者 carNum的用户 这3个必填
     *
     * @param wxOpenId
     * @param mobile
     * @param carNumber
     * @return
     */
    public User selectUserByOpenIdOrMobileOrCar(String wxOpenId, String mobile, String carNumber) throws SQLException {
        Map param = new HashMap();
        param.put("wxOpenId", wxOpenId);
        param.put("mobile", mobile);
        param.put("carNumber", carNumber);
        Object o = baseDao.selectObject(User.Daos.selectUserByOpenIdOrMobileOrCar.sqlMapname, param);
        return o == null ? null : (User) o;
    }

    /**
     * 查询车辆信息
     *
     * @param userId
     * @return
     */
    public List<Car> selectCarInfo(Integer userId) throws SQLException {
        Car car = new Car();
        car.setUserId(userId);
        car.setCarState(Constant.CarState.NORMAL.codeName);
        return baseDao.selectEntity(car);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>

    /**
     * 计算用户的结果集，即用微信，手机号，车牌号，分别查询出的用户对应的id是否一致，一致归为一个结果集
     * @param wxUser
     * @param mobileUser
     * @param carUser
     * @return
     */
    private Integer countUserSum(User wxUser, User mobileUser, User carUser) {
        //都不存在,结果集为0
        if(wxUser ==null && mobileUser == null && carUser == null){
            return 0;
        }
        Set<Integer> idSet = new HashSet<>();
        if(wxUser != null) idSet.add(wxUser.getId());
        if(mobileUser != null) idSet.add(mobileUser.getId());
        if(carUser != null) idSet.add(carUser.getId());
        return idSet.size();
    }

    private boolean valEquea(Integer arg1, Integer arg2, Integer arg3) {
        if(arg1!=null && arg1.equals(arg2) && (arg1==null || arg1.equals(arg1))) {
            return true;
        }
        return false;
    }


    /**
     * 根据车牌及用户id新增车牌
     * @param carNumber
     * @param userId
     * @return
     * @throws SQLException
     */
    private Car createCar(String carNumber,Integer userId) throws SQLException {
        Car car = new Car();
        car.setCarNumber(carNumber);
        car.setUserId(userId);
        car.setCarState(Constant.CarState.NORMAL.codeName);
        baseDao.insertEntity(car);
        return car;
    }

    /**
     * 结果集为2或者3,如果车牌带不出手机号，或者车牌带出的手机号/微信号为空，则将该车牌按(优先手机号再微信号)进行合并
     * @param carNumber
     * @param wxUser
     * @param mobileUser
     * @param carUser
     * @return
     * @throws SQLException
     */
    private User countSumTwoOrThree(String carNumber,String wxopenId,String mobile, User wxUser, User mobileUser, User carUser) throws SQLException {
        if(carUser == null || (ValidUtil.isEmpty(carUser.getMobile()) && ValidUtil.isEmpty(carUser.getWxOpenId()))){
            if(mobileUser != null){
                if(carUser == null){
                    createCar(carNumber,mobileUser.getId());
                }else{
                    updateCarChangeUserId(mobileUser.getId(),carUser.getId().toString());
                }
                if(wxUser == null){
                    mobileUser.setWxOpenId(wxopenId);
                    updateUser(mobileUser);
                }
                return mobileUser;
            }else if(wxUser != null){
                if(carUser == null){
                    createCar(carNumber,wxUser.getId());
                }else{
                    updateCarChangeUserId(wxUser.getId(),carUser.getId().toString());
                }
                if(mobileUser == null){
                    wxUser.setMobile(mobile);
                    updateUser(wxUser);
                }
                return wxUser;
            }
            //合并后删除掉carUser
            if(carUser!=null) {
                baseDao.deleteEntityById(carUser.getId(),User.class);
            }
        }
        return null;
    }

    /**
     * 结果集为1,表示查询用户都归属一个用户，则将所有的信息更新到此用户
     * @param userId
     * @param carUser
     * @param wxopenId
     * @param mobile
     * @param carNumber
     * @return
     */
    private User countSumOne(Integer userId,User carUser,String wxopenId,String mobile,String carNumber) throws SQLException {
        if(carUser == null){
            createCar(carNumber, userId);
        }
        User user = new User();
        user.setId(userId);
        user.setMobile(mobile);
        user.setWxOpenId(wxopenId);
        updateUser(user);
        return user;
    }

    /**
     * 结果集为0,表示都没有查出用户，则新增
     * @param wxopenId
     * @param mobile
     * @param carNumber
     * @return
     */
    private User countSumZero(String wxopenId, String mobile, String carNumber) throws SQLException {
        User user = new User();
        user.setWxOpenId(wxopenId);
        user.setMobile(mobile);
        user = baseDao.insertEntity(user);
        createCar(carNumber, user.getId());
        return user;
    }


}