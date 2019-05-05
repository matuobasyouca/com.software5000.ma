package com.software5000.ma.controller;

/**
 * Created by jiye on 2017/7/19.
 */

import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultUtil;
import com.software5000.ma.entity.*;
import com.software5000.ma.service.*;
import com.software5000.util.DataHandlerUtil;
import com.software5000.util.ExcelUtil;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.MathUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ExcelDataController {
    private Log log = LogFactory.getLog(ExcelDataController.class);

    @Resource
    private ExcelDataService excelDataService;

    @Resource
    private UserService userService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private BusinessPackageService businessPackageService;

    @Resource
    private ServiceItemService serviceItemService;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 导入会员信息数据
     *
     * @param file
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "home/excelData/insertUserInfoByUpload", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertUserInfoByUpload(@RequestParam("file") MultipartFile file) throws SQLException {
        Map<Integer, List<String>> contentMap = null;
        try {
            //验证表头
            checkUserInfoImportTitle(ExcelUtil.readExcelTitle(file));
            contentMap = ExcelUtil.readExcelContent(file, 1);
        } catch (Exception e) {
            return new ReturnResult(Constant.StateCode.ERROR.codeName, "导入失败，请使用正确的导入模板!" + e.getMessage(), null);
        }
        List<UserExcelData> userExcelDataList = new ArrayList<>();

        Set<Map.Entry<Integer, List<String>>> entrySet = contentMap.entrySet();
        for (Map.Entry<Integer, List<String>> integerListEntry : entrySet) {
            List<String> contentLists = integerListEntry.getValue();
            if (ExcelUtil.isEmptyList(contentLists)) continue;
            userExcelDataList.add(pottingUserExcelData(contentLists));
        }

        //插入excel数据到数据库中，并将上次导入的数据删除（只留最新的导入数据结果）
        excelDataService.deleteUserExcelDataByBusinessId(UserDefaultUtil.getBusinessId());
        List<UserExcelData> userExcelDatas = excelDataService.insertUserExcelData(userExcelDataList);
        Integer successNum = 0;
        Integer failNum = 0;
        if (userExcelDatas != null && userExcelDatas.size() > 0) {
            //导入数据到各表中
            importUserExcelData(userExcelDatas);
            for (UserExcelData userExcelData : userExcelDatas) {
                if (userExcelData.getSuccessFlag().equals(Constant.ExportSuccessFlag.SUCCESS_FLAG.codeName)) {
                    successNum++;
                } else {
                    failNum++;
                }
            }
        }
        Map map = new HashMap();
        map.put("successNum", successNum);
        map.put("failNum", failNum);
        return ReturnResult.buildSuccessMsg().setData(map);
    }

    /**
     * 导入服务项数据
     *
     * @param file
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "home/excelData/insertServiceItemByUpload", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertServiceItemByUpload(@RequestParam("file") MultipartFile file) throws SQLException {
        Map<Integer, List<String>> contentMap = null;
        try {
            //验证表头
            checkServiceItemImportTitle(ExcelUtil.readExcelTitle(file));
            contentMap = ExcelUtil.readExcelContent(file, 1);
        } catch (Exception e) {
            return new ReturnResult(Constant.StateCode.ERROR.codeName, "导入失败，请使用正确的导入模板!" + e.getMessage(), null);
        }
        List<ServiceItemExcelData> itemExcelDatas = new ArrayList<>();
        Set<Map.Entry<Integer, List<String>>> entries = contentMap.entrySet();
        for (Map.Entry<Integer, List<String>> entry : entries) {
            List<String> contentLists = entry.getValue();
            if (ExcelUtil.isEmptyList(contentLists)) continue;//所有数据都为空则忽略该条数据
            itemExcelDatas.add(pottingServiceItemExcelData(contentLists));
        }


        Integer successNum = 0;
        Integer failNum = 0;
        if (itemExcelDatas != null && itemExcelDatas.size() > 0) {
            //插入excel数据到数据库中，并将上次导入的数据删除（只留最新的导入数据结果）
            excelDataService.deleteServiceExcelDataByBusinessId(UserDefaultUtil.getBusinessId());
            List<ServiceItemExcelData> serviceItemExcelDatas = excelDataService.insertServiceItemExcelData(itemExcelDatas);
            //导入数据到各表中
            importServiceItemExcelData(serviceItemExcelDatas);
            for (ServiceItemExcelData serviceItemExcelData : serviceItemExcelDatas) {
                if (serviceItemExcelData.getSuccessFlag().equals(Constant.ExportSuccessFlag.SUCCESS_FLAG.codeName)) {
                    successNum++;
                } else {
                    failNum++;
                }
            }
        }
        Map map = new HashMap();
        map.put("successNum", successNum);
        map.put("failNum", failNum);
        return ReturnResult.buildSuccessMsg().setData(map);
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 下载服务项导入模版
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServiceException
     */
    @RequestMapping(value = "home/excelData/selectServiceItemTemplate", method = RequestMethod.GET)
    public void selectServiceItemTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServiceException {
        String[] caption = {"项目名称", "售价", "所属分类", "排序"};
        ExcelUtil.exportTemplate("服务项导入模版", "服务项导入模版", caption, null, null, response, request);
    }

    /**
     * 下载会员信息导入模版
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServiceException
     */
    @RequestMapping(value = "home/excelData/selectUserInfoTemplate", method = RequestMethod.GET)
    public void selectUserInfoTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServiceException {
        String[] caption = {"会员姓名", "车牌号码", "联系方式", "会员余额", "套餐名称", "有效期", "套餐项目1", "项目1剩余次数", "套餐项目2", "项目2剩余次数", "套餐项目3", "项目3剩余次数", "套餐项目4", "项目4剩余次数", "套餐项目5", "项目5剩余次数"};
        ExcelUtil.exportTemplate("会员信息导入模版", "会员信息导入模版", caption, null, null, response, request);
    }

    /**
     * 下载导入的服务项的结果
     */
    @RequestMapping(value = "home/excelData/selectServiceItemImportResult", method = RequestMethod.GET)
    public void selectServiceItemImportResult(HttpServletRequest request, HttpServletResponse response) throws IOException, ServiceException, SQLException {
        String[] caption = {"项目名称", "售价", "所属分类", "排序", "导入结果"};
        String[] detail = {"serviceItemName", "salePrice", "itemType", "sort", "remark"};
        List<Map<String, Object>> dataLists = pottingServiceItemImportResult();
        ExcelUtil.exportTemplate("服务项导入结果", "服务项导入结果", caption, dataLists, detail, response, request);
    }

    /**
     * 下载导入的会员信息的结果
     */
    @RequestMapping(value = "home/excelData/selectUserInfoImportResult", method = RequestMethod.GET)
    public void selectUserInfoImportResult(HttpServletRequest request, HttpServletResponse response) throws IOException, ServiceException, SQLException {
        String[] caption = {"会员姓名", "车牌号码", "联系方式", "会员余额", "套餐名称", "有效期", "套餐项目1", "项目1剩余次数", "套餐项目2", "项目2剩余次数", "套餐项目3", "项目3剩余次数", "套餐项目4", "项目4剩余次数", "套餐项目5", "项目5剩余次数", "导入结果"};
        String[] detail = {"realName", "carNumber", "mobile", "memberBalance", "packageName", "validDate", "serviceItem1", "restTime1", "serviceItem2", "restTime2", "serviceItem3", "restTime3", "serviceItem4", "restTime4", "serviceItem5", "restTime5", "remark"};
        List<Map<String, Object>> dataLists = pottingUserInfoImportResult();
        ExcelUtil.exportTemplate("会员信息导入结果", "会员信息导入结果", caption, dataLists, detail, response, request);
    }


    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== home (对内) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="api (对外) ">
    /*================================== api (对外) start ==================================*/

    //<editor-fold desc="api (对外)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== api (对外) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="home&api (内&外) ">
    /*================================== home&api (内&外) start ==================================*/

    //<editor-fold desc="home&api (内&外)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== home&api (内&外) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="open (开放) ">
    /*================================== open (开放) start ==================================*/

    //<editor-fold desc="open (开放)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== open (开放) end ==================================*/
    //</editor-fold>


    /**
     * 封装用户会员信息excel数据成实体
     *
     * @param contentLists
     * @return
     */
    private UserExcelData pottingUserExcelData(List<String> contentLists) {
        Integer successFlag = Constant.ExportSuccessFlag.SUCCESS_FLAG.codeName; //1为成功数据,2为失败数据
        String remark = "";//失败的原因备注
        String realName = ValidUtil.isEmpty(contentLists.get(0)) ? "" : contentLists.get(0).trim();
        String carNumbers = ValidUtil.isEmpty(contentLists.get(1)) ? "" : contentLists.get(1).trim();
        String mobile = ValidUtil.isEmpty(contentLists.get(2)) ? "" : contentLists.get(2).trim();
        String memberBalance = ValidUtil.isEmpty(contentLists.get(3)) ? "" : contentLists.get(3).trim();
        String packageName = ValidUtil.isEmpty(contentLists.get(4)) ? "" : contentLists.get(4).trim();
        String validTime = ValidUtil.isEmpty(contentLists.get(5)) ? "" : contentLists.get(5).trim();
        String serviceItem1 = ValidUtil.isEmpty(contentLists.get(6)) ? "" : contentLists.get(6).trim();
        String restTime1 = ValidUtil.isEmpty(contentLists.get(7)) ? "" : contentLists.get(7).trim();
        String serviceItem2 = ValidUtil.isEmpty(contentLists.get(8)) ? "" : contentLists.get(8).trim();
        String restTime2 = ValidUtil.isEmpty(contentLists.get(9)) ? "" : contentLists.get(9).trim();
        String serviceItem3 = ValidUtil.isEmpty(contentLists.get(10)) ? "" : contentLists.get(10).trim();
        String restTime3 = ValidUtil.isEmpty(contentLists.get(11)) ? "" : contentLists.get(11).trim();
        String serviceItem4 = ValidUtil.isEmpty(contentLists.get(12)) ? "" : contentLists.get(12).trim();
        String restTime4 = ValidUtil.isEmpty(contentLists.get(13)) ? "" : contentLists.get(13).trim();
        String serviceItem5 = ValidUtil.isEmpty(contentLists.get(14)) ? "" : contentLists.get(14).trim();
        String restTime5 = ValidUtil.isEmpty(contentLists.get(15)) ? "" : contentLists.get(15).trim();
        //校验数据合理性(车牌必填，手机号必填，套餐名有填，套餐服务项1必填，套餐服务项1剩余次数必填)
        if (DataHandlerUtil.isDataEmpty(carNumbers)) {
            successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
            remark = "未填写车牌号;";
        }
        if (DataHandlerUtil.isDataEmpty(mobile)) {
            successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
            remark += "未填写手机号;";
        } else if (mobile.length() != 11) {
            successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
            remark += "手机号格式有误;";
        }
        if(ValidUtil.isNotEmpty(memberBalance)){
            try {
                Double.valueOf(memberBalance);
            }catch (Exception e){
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "会员余额格式有误;";
            }

        }

        if (!DataHandlerUtil.isDataEmpty(packageName)) {

            if (!(DataHandlerUtil.isDataEmpty(validTime) || validTime.equals("永久"))) {
                Date date = DateUtils.parseDate(validTime + " 23:59:59", DateUtils.DATE_FULL_STR);
                if (date == null) {
                    successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                    remark += "套餐有效期格式有误;";
                }
            }
            if (DataHandlerUtil.isDataEmpty(serviceItem1)) {
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写套餐项目1;";
            }
            if (DataHandlerUtil.isDataEmpty(restTime1)) {
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写项目1剩余次数;";
            }
            if (!DataHandlerUtil.isDataEmpty(serviceItem2) && DataHandlerUtil.isDataEmpty(restTime2)) {//如果填写了套餐项目2但是没有填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写项目2剩余次数;";
            }
            if (DataHandlerUtil.isDataEmpty(serviceItem2) && !DataHandlerUtil.isDataEmpty(restTime2)) {//如果没有填写套餐项目2但是填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写套餐项目2;";
            }
            if (!DataHandlerUtil.isDataEmpty(serviceItem3) && DataHandlerUtil.isDataEmpty(restTime3)) {//如果填写了套餐项目3但是没有填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写项目3剩余次数;";
            }
            if (DataHandlerUtil.isDataEmpty(serviceItem3) && !DataHandlerUtil.isDataEmpty(restTime3)) {//如果没有填写套餐项目3但是填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写套餐项目3;";
            }
            if (!DataHandlerUtil.isDataEmpty(serviceItem4) && DataHandlerUtil.isDataEmpty(restTime4)) {//如果填写了套餐项目3但是没有填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写项目4剩余次数;";
            }
            if (DataHandlerUtil.isDataEmpty(serviceItem4) && !DataHandlerUtil.isDataEmpty(restTime4)) {//如果没有填写套餐项目3但是填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写套餐项目4;";
            }
            if (!DataHandlerUtil.isDataEmpty(serviceItem5) && DataHandlerUtil.isDataEmpty(restTime5)) {//如果填写了套餐项目3但是没有填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写项目5剩余次数;";
            }
            if (DataHandlerUtil.isDataEmpty(serviceItem5) && !DataHandlerUtil.isDataEmpty(restTime5)) {//如果没有填写套餐项目3但是填写剩余次数
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写套餐项目5;";
            }
        } else {
            if (!DataHandlerUtil.isDataEmpty(serviceItem1) || !DataHandlerUtil.isDataEmpty(restTime1) || !DataHandlerUtil.isDataEmpty(serviceItem2)
                    || !DataHandlerUtil.isDataEmpty(restTime2) || !DataHandlerUtil.isDataEmpty(restTime3)
                    || !DataHandlerUtil.isDataEmpty(serviceItem3) || !DataHandlerUtil.isDataEmpty(restTime4)
                    || !DataHandlerUtil.isDataEmpty(serviceItem4) || !DataHandlerUtil.isDataEmpty(restTime5)
                    || !DataHandlerUtil.isDataEmpty(serviceItem5) || !DataHandlerUtil.isDataEmpty(validTime)) {
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "未填写套餐名称,不能填写有效期，项目名称和项目剩余次数;";
            }
        }

        UserExcelData userExcelData = new UserExcelData();
        userExcelData.setBusinessId(UserDefaultUtil.getBusinessId());
        userExcelData.setSuccessFlag(successFlag);
        userExcelData.setRealName(realName);
        userExcelData.setCarNumber(carNumbers.toUpperCase());
        userExcelData.setMobile(mobile);
        userExcelData.setMemberBalance(memberBalance);
        userExcelData.setPackageName(packageName);
        userExcelData.setValidDate(validTime);
        userExcelData.setServiceItem1(serviceItem1);
        userExcelData.setServiceItem2(serviceItem2);
        userExcelData.setServiceItem3(serviceItem3);
        userExcelData.setServiceItem4(serviceItem4);
        userExcelData.setServiceItem5(serviceItem5);
        userExcelData.setRestTime1(restTime1);
        userExcelData.setRestTime2(restTime2);
        userExcelData.setRestTime3(restTime3);
        userExcelData.setRestTime4(restTime4);
        userExcelData.setRestTime5(restTime5);
        userExcelData.setRemark(Constant.ExportSuccessFlag.SUCCESS_FLAG.codeName.equals(successFlag) ? "导入成功！" : ("导入失败！" + remark));
        return userExcelData;
    }

    /**
     * 封装用户会员信息excel数据成实体
     *
     * @param contentLists
     * @return
     */
    private ServiceItemExcelData pottingServiceItemExcelData(List<String> contentLists) {
        Integer successFlag = Constant.ExportSuccessFlag.SUCCESS_FLAG.codeName; //1为成功数据,2为失败数据
        String remark = "";//失败的原因备注
        String serviceItemName = contentLists.get(0);
        String salePrice = contentLists.get(1);
        Double salePriceDouble = 0D;
        String itemType = contentLists.get(2);
        String sort = contentLists.get(3);
        Integer sortInteger = 1;
        //校验数据合理性(项目名称必填，售价必填，所属分类必填)
        if (DataHandlerUtil.isDataEmpty(serviceItemName)) {
            successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
            remark = "未填写项目名称;";
        }
        if (DataHandlerUtil.isDataEmpty(salePrice)) {
            successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
            remark += "未填写售价;";
        } else {
            try {
                salePriceDouble = Double.valueOf(salePrice);
            } catch (Exception e) {
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "售价格式有误;";
            }
        }
        if (DataHandlerUtil.isDataEmpty(itemType)) {
            successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
            remark += "未填写所属分类;";
        } else {
            if (!(itemType.equals(Constant.ServiceItemType.CAR_WASH.desc) || itemType.equals(Constant.ServiceItemType.CAR_BEAUTY.desc) || itemType.equals(Constant.ServiceItemType.CAR_MAINTENANCE.desc) ||
                    itemType.equals(Constant.ServiceItemType.CAR_REPAIR.desc) || itemType.equals(Constant.ServiceItemType.CAR_SPRAY.desc))) {
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "所属分类填写错误请填写(洗车、美容、保养、机修、钣喷);";
            }
        }


        if (!DataHandlerUtil.isDataEmpty(sort)) {
            try {
                sortInteger = Integer.valueOf(sort);
            } catch (Exception e) {
                successFlag = Constant.ExportSuccessFlag.FAIL_FLAG.codeName;
                remark += "排序格式有误;";
            }
        }
        ServiceItemExcelData itemExcelData = new ServiceItemExcelData();
        itemExcelData.setBusinessId(UserDefaultUtil.getBusinessId());
        itemExcelData.setItemType(itemType);
        itemExcelData.setSalePrice(salePriceDouble);
        itemExcelData.setServiceItemName(serviceItemName);
        itemExcelData.setSort(sortInteger);
        itemExcelData.setSuccessFlag(successFlag);
        itemExcelData.setRemark(Constant.ExportSuccessFlag.SUCCESS_FLAG.codeName.equals(successFlag) ? "导入成功！" : ("导入失败！" + remark));
        return itemExcelData;
    }

    /**
     * 将数据插入到数据中
     *
     * @param userExcelDatas
     */
    public void importUserExcelData(List<UserExcelData> userExcelDatas) throws SQLException {
        List list = new ArrayList();//需要更新的数据
        for (int i = 0; i < userExcelDatas.size(); i++) {
            UserExcelData userExcelData = userExcelDatas.get(i);
            try {
                if (Constant.ExportSuccessFlag.FAIL_FLAG.codeName.equals(userExcelData.getSuccessFlag())) {
                    continue;
                }
                //判断车牌是否已经存在
                String carNumber = userExcelData.getCarNumber();
                String[] carSplits = carNumber.split("/");
                if (carSplits.length > 10) {
                    userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                    userExcelData.setRemark("导入失败，车牌最多只能为10个");
                    list.add(userExcelData);
                    continue;
                }
                List<String> carNumbers = new ArrayList<>();
                Boolean carSuccess = true;
                for (int i1 = 0; i1 < carSplits.length; i1++) {
                    String s = DataHandlerUtil.covertCarNumber(carSplits[i1]);
                    Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$|^[a-zA-Z]{2}\\d{7}$");
                    Matcher matcher = pattern.matcher(s.trim());
                    if (!matcher.matches()) {
                        userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                        userExcelData.setRemark("导入失败，车牌号" + carSplits[i1] + "格式有误");
                        list.add(userExcelData);
                        carSuccess = false;
                        break;
                    }
                    carNumbers.add(s);
                }
                if (!carSuccess) continue;

                //判断套餐是否存在，即套餐名是否正确
                String packageName = userExcelData.getPackageName();
                if (ValidUtil.isNotEmpty(packageName)) {
                    BusinessPackage businessPackage = businessPackageService.selectBusinessPackageByBusinessIdAndPackageName(UserDefaultUtil.getBusinessId(), packageName);
                    if (businessPackage == null) {
                        userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                        userExcelData.setRemark("导入失败， 套餐名称[" + packageName + "]不存在");
                        list.add(userExcelData);
                        continue;
                    }
                    //判断服务项是否存在，即各个服务项是否填写正确
                    ServiceItem serviceItem1 = null;
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem1())) {
                        serviceItem1 = serviceItemService.selectServiceItemByBusinessIdAndItemName(UserDefaultUtil.getBusinessId(), userExcelData.getServiceItem1());
                        if (serviceItem1 == null) {
                            userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                            userExcelData.setRemark("导入失败， 套餐项目1[" + userExcelData.getServiceItem1() + "]不存在");
                            list.add(userExcelData);
                            continue;
                        }
                    }
                    //判断服务项是否存在，即各个服务项是否填写正确
                    ServiceItem serviceItem2 = null;
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem2())) {
                        serviceItem2 = serviceItemService.selectServiceItemByBusinessIdAndItemName(UserDefaultUtil.getBusinessId(), userExcelData.getServiceItem2());
                        if (serviceItem2 == null) {
                            userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                            userExcelData.setRemark("导入失败， 套餐项目2[" + userExcelData.getServiceItem2() + "]不存在");
                            list.add(userExcelData);
                            continue;
                        }
                    }
                    //判断服务项是否存在，即各个服务项是否填写正确
                    ServiceItem serviceItem3 = null;
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem3())) {
                        serviceItem3 = serviceItemService.selectServiceItemByBusinessIdAndItemName(UserDefaultUtil.getBusinessId(), userExcelData.getServiceItem3());
                        if (serviceItem3 == null) {
                            userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                            userExcelData.setRemark("导入失败， 套餐项目3[" + userExcelData.getServiceItem3() + "]不存在");
                            list.add(userExcelData);
                            continue;
                        }
                    }
                    //判断服务项是否存在，即各个服务项是否填写正确
                    ServiceItem serviceItem4 = null;
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem4())) {
                        serviceItem4 = serviceItemService.selectServiceItemByBusinessIdAndItemName(UserDefaultUtil.getBusinessId(), userExcelData.getServiceItem4());
                        if (serviceItem4 == null) {
                            userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                            userExcelData.setRemark("导入失败， 套餐项目4[" + userExcelData.getServiceItem4() + "]不存在");
                            list.add(userExcelData);
                            continue;
                        }
                    }
                    //判断服务项目是否存在，即各个服务项是否填写正确
                    ServiceItem serviceItem5 = null;
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getRestTime5())) {
                        serviceItem5 = serviceItemService.selectServiceItemByBusinessIdAndItemName(UserDefaultUtil.getBusinessId(), userExcelData.getServiceItem5());
                        if (serviceItem5 == null) {
                            userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                            userExcelData.setRemark("导入失败， 套餐项目5[" + userExcelData.getServiceItem5() + "]不存在");
                            list.add(userExcelData);
                            continue;
                        }
                    }

                    //新增用户车辆
                    User insertUser = null;
                    int exceptionTemp = 0;
                    for (int j = 0; j < carNumbers.size(); j++) {
                        try {
                            User user = userService.insertUser(null, userExcelData.getMobile(), carNumbers.get(j));
                            if (insertUser == null) {
                                insertUser = user;
                            }
                        } catch (Exception e) {
                            exceptionTemp++;
                        }
                    }
                    if (insertUser == null) {
                        userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                        userExcelData.setRemark("导入失败， 新增用户失败");
                        list.add(userExcelData);
                        continue;
                    }
                    if (carNumbers.size() == exceptionTemp) {
                        userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                        userExcelData.setRemark("导入失败， 新增用户失败");
                        list.add(userExcelData);
                        continue;
                    }

                    insertUser.setRealName(userExcelData.getRealName());
                    userService.updateUser(insertUser);
                    //新增会员记录 如果已经存在就不增加
                    MemberLvlRecord memberLvlRecord = memberLvlRecordService.selectMemberLvlRecordWithMemberLvl(insertUser.getId(), UserDefaultUtil.getBusinessId());
                    Double excelBalance =  ValidUtil.isEmpty(userExcelData.getMemberBalance()) ? 0d : Double.valueOf(userExcelData.getMemberBalance());
                    if (memberLvlRecord == null) {
                        memberLvlRecord = new MemberLvlRecord();
                        memberLvlRecord.setUserId(insertUser.getId());
                        memberLvlRecord.setBusinessId(UserDefaultUtil.getBusinessId());
                        memberLvlRecord.setTotalConsume(0D);
                        memberLvlRecord.setMemberBalance(excelBalance);
                        memberLvlRecordService.insertMemberLvlRecord(memberLvlRecord);
                    } else {
                        Double memberBalance = ValidUtil.isEmpty(memberLvlRecord.getMemberBalance())?0d:memberLvlRecord.getMemberBalance();
                        int id = memberLvlRecord.getId();
                        memberLvlRecord = new MemberLvlRecord();
                        memberLvlRecord.setId(id);
                        memberLvlRecord.setMemberBalance(MathUtil.add(excelBalance,memberBalance));
                        memberLvlRecordService.updateMemberBalance(memberLvlRecord);
                    }

                    //新增套餐记录
                    MemberPackageRecord memberPackageRecord = new MemberPackageRecord();
                    memberPackageRecord.setUserId(insertUser.getId());
                    memberPackageRecord.setBusinessId(UserDefaultUtil.getBusinessId());
                    memberPackageRecord.setBusinessPackageId(businessPackage.getId());
                    if (DataHandlerUtil.isDataEmpty(userExcelData.getValidDate()) || userExcelData.getValidDate().equals("永久")) {
                        memberPackageRecord.setValidTime(null);
                    } else {
                        Date date = DateUtils.parseDate(userExcelData.getValidDate() + " 23:59:59", DateUtils.DATE_FULL_STR);
                        memberPackageRecord.setValidTime(new Timestamp(date.getTime()));
                    }
                    memberPackageRecord = memberPackageRecordService.insertMemberPackageRecord(memberPackageRecord);
                    //新增使用记录信息
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem1())) {
                        MemberItemUseRecord memberItemUseRecord = new MemberItemUseRecord();
                        memberItemUseRecord.setMemberPackageRecordId(memberPackageRecord.getId());
                        memberItemUseRecord.setRemainTimes(Integer.valueOf(userExcelData.getRestTime1()));
                        memberItemUseRecord.setUseTimes(Integer.valueOf(userExcelData.getRestTime1()));
                        memberItemUseRecord.setServiceItemId(serviceItem1.getId());
                        serviceItemService.insertMemberItemUseRecord(memberItemUseRecord);
                    }
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem2())) {
                        MemberItemUseRecord memberItemUseRecord = new MemberItemUseRecord();
                        memberItemUseRecord.setMemberPackageRecordId(memberPackageRecord.getId());
                        memberItemUseRecord.setRemainTimes(Integer.valueOf(userExcelData.getRestTime2()));
                        memberItemUseRecord.setUseTimes(Integer.valueOf(userExcelData.getRestTime2()));
                        memberItemUseRecord.setServiceItemId(serviceItem2.getId());
                        serviceItemService.insertMemberItemUseRecord(memberItemUseRecord);
                    }
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem3())) {
                        MemberItemUseRecord memberItemUseRecord = new MemberItemUseRecord();
                        memberItemUseRecord.setMemberPackageRecordId(memberPackageRecord.getId());
                        memberItemUseRecord.setRemainTimes(Integer.valueOf(userExcelData.getRestTime3()));
                        memberItemUseRecord.setUseTimes(Integer.valueOf(userExcelData.getRestTime3()));
                        memberItemUseRecord.setServiceItemId(serviceItem3.getId());
                        serviceItemService.insertMemberItemUseRecord(memberItemUseRecord);
                    }
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem4())) {
                        MemberItemUseRecord memberItemUseRecord = new MemberItemUseRecord();
                        memberItemUseRecord.setMemberPackageRecordId(memberPackageRecord.getId());
                        memberItemUseRecord.setRemainTimes(Integer.valueOf(userExcelData.getRestTime4()));
                        memberItemUseRecord.setUseTimes(Integer.valueOf(userExcelData.getRestTime4()));
                        memberItemUseRecord.setServiceItemId(serviceItem4.getId());
                        serviceItemService.insertMemberItemUseRecord(memberItemUseRecord);
                    }
                    if (!DataHandlerUtil.isDataEmpty(userExcelData.getServiceItem5())) {
                        MemberItemUseRecord memberItemUseRecord = new MemberItemUseRecord();
                        memberItemUseRecord.setMemberPackageRecordId(memberPackageRecord.getId());
                        memberItemUseRecord.setRemainTimes(Integer.valueOf(userExcelData.getRestTime5()));
                        memberItemUseRecord.setUseTimes(Integer.valueOf(userExcelData.getRestTime5()));
                        memberItemUseRecord.setServiceItemId(serviceItem5.getId());
                        serviceItemService.insertMemberItemUseRecord(memberItemUseRecord);
                    }
                } else {
                    //新增用户车辆
                    User insertUser = null;
                    int exceptionTemp = 0;
                    for (int j = 0; j < carNumbers.size(); j++) {
                        try {
                            User user = userService.insertUser(null, userExcelData.getMobile(), carNumbers.get(j));
                            if (insertUser == null) {
                                insertUser = user;
                            }
                        } catch (Exception e) {
                            exceptionTemp++;
                        }
                    }
                    if (insertUser == null) {
                        userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                        userExcelData.setRemark("导入失败， 新增用户失败");
                        list.add(userExcelData);
                        continue;
                    }
                    if (carNumbers.size() == exceptionTemp) {
                        userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                        userExcelData.setRemark("导入失败， 新增用户失败");
                        list.add(userExcelData);
                        continue;
                    }

                    insertUser.setRealName(userExcelData.getRealName());
                    userService.updateUser(insertUser);
                    MemberLvlRecord memberLvlRecord = memberLvlRecordService.selectMemberLvlRecordWithMemberLvl(insertUser.getId(), UserDefaultUtil.getBusinessId());
                    Double excelBalance =  ValidUtil.isEmpty(userExcelData.getMemberBalance()) ? 0d : Double.valueOf(userExcelData.getMemberBalance());
                    if (memberLvlRecord == null) {
                        memberLvlRecord = new MemberLvlRecord();
                        memberLvlRecord.setUserId(insertUser.getId());
                        memberLvlRecord.setBusinessId(UserDefaultUtil.getBusinessId());
                        memberLvlRecord.setTotalConsume(0D);
                        memberLvlRecord.setMemberBalance(excelBalance);
                        memberLvlRecordService.insertMemberLvlRecord(memberLvlRecord);
                    } else {
                        Double memberBalance = memberLvlRecord.getMemberBalance() == null?0d:memberLvlRecord.getMemberBalance();
                        int id = memberLvlRecord.getId();
                        memberLvlRecord = new MemberLvlRecord();
                        memberLvlRecord.setId(id);
                        memberLvlRecord.setMemberBalance(MathUtil.add(memberBalance,excelBalance));
                        memberLvlRecordService.updateMemberBalance(memberLvlRecord);
                    }
                }
            } catch (Exception e) {
                log.error("数据导入失败", e);
                userExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                userExcelData.setRemark("导入失败，数据格式有误，请检查");
                list.add(userExcelData);
                continue;
            }
        }
        //批量更新有新增异常的数据
        excelDataService.updateUserExcelData(list);
    }

    /**
     * 将数据插入到数据中
     *
     * @param itemExcelDatas
     */
    public void importServiceItemExcelData(List<ServiceItemExcelData> itemExcelDatas) throws SQLException {
        List list = new ArrayList();//需要更新的数据
        for (ServiceItemExcelData itemExcelData : itemExcelDatas) {
            if (Constant.ExportSuccessFlag.FAIL_FLAG.codeName.equals(itemExcelData.getSuccessFlag())) {
                continue;
            }
            String serviceItemName = itemExcelData.getServiceItemName();
            ServiceItem item = serviceItemService.selectServiceItemByBusinessIdAndItemName(UserDefaultUtil.getBusinessId(), serviceItemName);
            if (item != null) {
                itemExcelData.setSuccessFlag(Constant.ExportSuccessFlag.FAIL_FLAG.codeName);
                itemExcelData.setRemark("导入失败，服务项名称已经存在");
                list.add(itemExcelData);
                continue;
            }
            ServiceItem serviceItem = new ServiceItem();
            serviceItem.setSort(itemExcelData.getSort());
            serviceItem.setItemName(serviceItemName);
            serviceItem.setBusinessId(UserDefaultUtil.getBusinessId());
            serviceItem.setSalePrice(itemExcelData.getSalePrice());
            serviceItem.setOriginalPrice(itemExcelData.getSalePrice());
            serviceItem.setTopState(Constant.ServiceItemTopState.NO_TOP.codeName);
            String itemType = itemExcelData.getItemType();
            serviceItem.setItemType(Constant.ServiceItemType.CAR_WASH.desc.equals(itemType) ? Constant.ServiceItemType.CAR_WASH.codeName :
                    Constant.ServiceItemType.CAR_BEAUTY.desc.equals(itemType) ? Constant.ServiceItemType.CAR_BEAUTY.codeName :
                            Constant.ServiceItemType.CAR_MAINTENANCE.desc.equals(itemType) ? Constant.ServiceItemType.CAR_MAINTENANCE.codeName :
                                    Constant.ServiceItemType.CAR_REPAIR.desc.equals(itemType) ? Constant.ServiceItemType.CAR_REPAIR.codeName : Constant.ServiceItemType.CAR_SPRAY.codeName);
            serviceItemService.insertServiceItem(serviceItem);
        }
        //批量更新有新增异常的数据
        excelDataService.updateServiceItemExcelData(list);
    }

    /**
     * 封装用户信息导入结果excel表数据
     *
     * @return
     * @throws SQLException
     */
    private List<Map<String, Object>> pottingUserInfoImportResult() throws SQLException {
        List<UserExcelData> userExcelDatas = excelDataService.selectUserInfoImportResult(UserDefaultUtil.getBusinessId());
        List listData = new ArrayList();
        for (UserExcelData userExcelData : userExcelDatas) {
            Map mapValue = new HashMap();
            mapValue.put("realName", userExcelData.getRealName());
            mapValue.put("carNumber", userExcelData.getCarNumber());
            mapValue.put("mobile", userExcelData.getMobile());
            mapValue.put("memberBalance", userExcelData.getMemberBalance());
            mapValue.put("packageName", userExcelData.getPackageName());
            mapValue.put("validDate", userExcelData.getValidDate());
            mapValue.put("serviceItem1", userExcelData.getServiceItem1());
            mapValue.put("restTime1", userExcelData.getRestTime1());
            mapValue.put("serviceItem2", userExcelData.getServiceItem2());
            mapValue.put("restTime2", userExcelData.getRestTime2());
            mapValue.put("serviceItem3", userExcelData.getServiceItem3());
            mapValue.put("restTime3", userExcelData.getRestTime3());
            mapValue.put("serviceItem4", userExcelData.getServiceItem4());
            mapValue.put("restTime4", userExcelData.getRestTime4());
            mapValue.put("serviceItem5", userExcelData.getServiceItem5());
            mapValue.put("restTime5", userExcelData.getRestTime5());
            mapValue.put("remark", userExcelData.getRemark());
            listData.add(mapValue);
        }
        return listData;
    }

    /**
     * 封装服务项导入结果excel表数据
     *
     * @return
     * @throws SQLException
     */
    private List<Map<String, Object>> pottingServiceItemImportResult() throws SQLException {
        List<ServiceItemExcelData> serviceItemExcelDatas = excelDataService.selectItemImportResult(UserDefaultUtil.getBusinessId());
        List listData = new ArrayList();
        for (ServiceItemExcelData serviceItemExcelData : serviceItemExcelDatas) {
            Map mapValue = new HashMap();
            mapValue.put("serviceItemName", serviceItemExcelData.getServiceItemName());
            mapValue.put("salePrice", serviceItemExcelData.getSalePrice());
            mapValue.put("itemType", serviceItemExcelData.getItemType());
            mapValue.put("sort", serviceItemExcelData.getSort());
            mapValue.put("remark", serviceItemExcelData.getRemark());
            listData.add(mapValue);
        }
        return listData;
    }

    /**
     * 验证服务项导入表头格式是否正确
     *
     * @param titles
     */
    private void checkServiceItemImportTitle(String[] titles) throws Exception {
        if (titles.length < 4) {
            throw new Exception("表头长度不对");
        } else {
            if (ValidUtil.isEmpty(titles[0]) || !titles[0].trim().equals("项目名称"))
                throw new Exception("模版不对-项目名称");

            if (ValidUtil.isEmpty(titles[1]) || !titles[1].trim().equals("售价"))
                throw new Exception("模版不对-售价");

            if (ValidUtil.isEmpty(titles[2]) || !titles[2].trim().equals("所属分类"))
                throw new Exception("模版不对-所属分类");

            if (ValidUtil.isEmpty(titles[3]) || !titles[3].trim().equals("排序"))
                throw new Exception("模版不对-排序");
        }
    }

    /**
     * 验证会员信息导入表头格式是否正确
     *
     * @param titles
     */
    private void checkUserInfoImportTitle(String[] titles) throws Exception {
        if (titles.length < 16) {
            throw new Exception("表头长度不对");
        } else {
            if (ValidUtil.isEmpty(titles[0]) || !titles[0].trim().equals("会员姓名"))
                throw new Exception("模版不对-会员姓名");

            if (ValidUtil.isEmpty(titles[1]) || !titles[1].trim().equals("车牌号码"))
                throw new Exception("模版不对-车牌号码");

            if (ValidUtil.isEmpty(titles[2]) || !titles[2].trim().equals("联系方式"))
                throw new Exception("模版不对-联系方式");

            if (ValidUtil.isEmpty(titles[3]) || !titles[3].trim().equals("会员余额"))
                throw new Exception("模版不对-会员余额");

            if (ValidUtil.isEmpty(titles[4]) || !titles[4].trim().equals("套餐名称"))
                throw new Exception("模版不对-套餐名称");

            if (ValidUtil.isEmpty(titles[5]) || !titles[5].trim().equals("有效期"))
                throw new Exception("模版不对-有效期");

            if (ValidUtil.isEmpty(titles[6]) || !titles[6].trim().equals("套餐项目1"))
                throw new Exception("模版不对-套餐项目1");

            if (ValidUtil.isEmpty(titles[7]) || !titles[7].trim().equals("项目1剩余次数"))
                throw new Exception("模版不对-项目1剩余次数");

            if (ValidUtil.isEmpty(titles[8]) || !titles[8].trim().equals("套餐项目2"))
                throw new Exception("模版不对-套餐项目2");

            if (ValidUtil.isEmpty(titles[9]) || !titles[9].trim().equals("项目2剩余次数"))
                throw new Exception("模版不对-项目2剩余次数");

            if (ValidUtil.isEmpty(titles[10]) || !titles[10].trim().equals("套餐项目3"))
                throw new Exception("模版不对-套餐项目3");

            if (ValidUtil.isEmpty(titles[11]) || !titles[11].trim().equals("项目3剩余次数"))
                throw new Exception("模版不对-项目3剩余次数");

            if (ValidUtil.isEmpty(titles[12]) || !titles[12].trim().equals("套餐项目4"))
                throw new Exception("模版不对-套餐项目4");

            if (ValidUtil.isEmpty(titles[13]) || !titles[13].trim().equals("项目4剩余次数"))
                throw new Exception("模版不对-项目4剩余次数");

            if (ValidUtil.isEmpty(titles[14]) || !titles[14].trim().equals("套餐项目5"))
                throw new Exception("模版不对-套餐项目5");

            if (ValidUtil.isEmpty(titles[15]) || !titles[15].trim().equals("项目5剩余次数"))
                throw new Exception("模版不对-项目5剩余次数");
        }
    }
}