package com.software5000.ma.service;

/**
 * Created by Administrator on 2017/7/24.
 */

import com.software5000.ma.entity.Car;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class CarService {
    private Log log = LogFactory.getLog(CarService.class);

    @Resource
    private BaseDao baseDao;

    @Resource
    private UserService userService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 添加用户车辆信息
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public Car insertCarNumberByParam(Map paramMap) throws SQLException {
        Car car = new Car();
        car.setUserId(Integer.valueOf(paramMap.get("userId").toString()));
        car.setCarNumber(paramMap.get("carNumber").toString());
        car.setCarState(Constant.CarState.NORMAL.codeName);
        return baseDao.insertEntity(car);
    }

    /**
     * 添加用户车辆信息
     *
     * @param car
     * @return
     * @throws SQLException
     */
    public Car insertCarNumberByCar(Car car) throws SQLException {
        return baseDao.insertEntity(car);
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 删除车辆
     *
     * @param car
     * @throws SQLException
     */
    public void deleteCar(Car car) throws SQLException {
        baseDao.deleteEntity(car);
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 修改车辆的归属
     *
     * @param car
     * @throws SQLException
     */
    public void updateCarChangeUserId(Car car) throws SQLException {
        baseDao.updateEntityNotEmpty(car);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据用户id查询用户的所有车辆
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public List<Car> selectUserCarById(Integer userId) throws SQLException {
        Car car = new Car();
        car.setUserId(userId);
        car.setCarState(Constant.CarState.NORMAL.codeName);
        return baseDao.selectEntity(car);
    }

    /**
     * 根据车牌号查询车辆是否已被绑定
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public List<Car> selectCarByCarNumber(Map paramMap) throws SQLException {
        Car car = new Car();
        car.setCarNumber(paramMap.get("carNumber").toString());
        car.setCarState(Constant.CarState.NORMAL.codeName);
        return baseDao.selectEntity(car);
    }

    /**
     * 根据用户id和车牌号，查询车辆是否曾经被用户绑定过
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public List<Car> selectCarByParam(Map paramMap) throws SQLException {
        Car car = new Car();
        car.setUserId(null != paramMap.get("userId") ? Integer.valueOf(paramMap.get("userId").toString()) : null);
        car.setCarNumber(paramMap.get("carNumber").toString());
        return baseDao.selectEntity(car);
    }

    /**
     * 通过车辆的id查询车辆信息
     *
     * @param carId
     * @return
     * @throws SQLException
     */
    public Car selectCarById(Integer carId) throws SQLException {
        return baseDao.selectEntityById(carId, Car.class);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}