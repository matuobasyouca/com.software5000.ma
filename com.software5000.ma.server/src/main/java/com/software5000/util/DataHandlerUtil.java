package com.software5000.util;

import com.software5000.ma.entity.UserExcelData;
import com.zscp.master.util.ValidUtil;

import java.util.regex.Pattern;

/**
 * Created by jiye on 2017/7/10.
 */
public class DataHandlerUtil {
    /**
     * 判断是否为空规则配置，如果数据的值等于"无"也当空处理，后续规则可以在这里边配置
     *
     * @param str
     * @return
     */
    public static boolean isDataEmpty(String str) {
        if (ValidUtil.isEmpty(str)) {
            return true;
        } else {
            return str.equals("无");
        }
    }

    /**
     * 转换车牌规则，如果车牌不包含中文在车牌前面加个"闽"，后续可以在这边配置车牌规则
     *
     * @param carNumber
     * @return
     */
    public static String covertCarNumber(String carNumber) {
        if (!Pattern.compile("[\u4e00-\u9fa5]").matcher(carNumber).find()) {
            carNumber = "闽" + carNumber;
        }
        return carNumber;
    }

    /**
     * 是否可以导入这条数据(手机号不能为空，车牌号不能为空，套餐名不能为空，项目必须<=3[这边通过判断serviceItem1是否有值来判断])
     *
     * @param userExcelData
     * @return
     */
    public static boolean isImport(UserExcelData userExcelData) {
        if (isDataEmpty(userExcelData.getMobile())) return false;
        if (isDataEmpty(userExcelData.getCarNumber())) return false;
        if (isDataEmpty(userExcelData.getPackageName())) return false;
        if (isDataEmpty(userExcelData.getServiceItem1())) return false;
        return true;
    }

    /**
     * 转换套餐，因为套餐数量不多可以根据套餐名之间对数据库的id
     *
     * @param packageName
     * @return 如果值为-1则需要看这条数据是否有问题。
     */
    public static Integer convertPackageName(String packageName) {
        Integer packageId = -1;
        if (isDataEmpty(packageName)) return packageId;
        if (packageName.equals("C")) packageId = 131;
        if (packageName.equals("C套餐")) packageId = 131;
        if (packageName.equals("D")) packageId = 132;
        if (packageName.equals("E")) packageId = 133;
        if (packageName.equals("F")) packageId = 134;
        if (packageName.equals("金卡")) packageId = 135;
        if (packageName.equals("白金卡")) packageId = 136;
        if (packageName.equals("钻石卡")) packageId = 137;
        return packageId;
    }

    /**
     * 转换服务项规则，直接返回id
     * @param serviceItem
     * @return
     */
    public static Integer convertServiceItem(String serviceItem){
        if(serviceItem.equals("普洗"))return 497;
        if(serviceItem.equals("精洗"))return 504;
        if(serviceItem.equals("室内清洗"))return 500;
        if(serviceItem.equals("封釉"))return 499;
        if(serviceItem.equals("手工打蜡"))return 509;
        if(serviceItem.equals("气动打蜡"))return 510;
        if(serviceItem.equals("全车抛光打蜡"))return 498;
        return -1;
    }
}
