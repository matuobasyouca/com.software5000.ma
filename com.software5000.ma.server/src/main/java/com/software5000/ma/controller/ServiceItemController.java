package com.software5000.ma.controller;

/**
 * Created by jiye on 2017/7/21.
 */

import com.software5000.ma.entity.ItemAndMemberLvl;
import com.software5000.ma.entity.MemberLvlRecord;
import com.software5000.ma.entity.ServiceItem;
import com.software5000.ma.service.ItemAndMemberLvlService;
import com.software5000.ma.service.MemberLvlRecordService;
import com.software5000.ma.service.ServiceItemService;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
public class ServiceItemController {
    private Log log = LogFactory.getLog(ServiceItemController.class);

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private MemberLvlRecordService memberLvlRecordService;

    @Autowired
    private ItemAndMemberLvlService itemAndMemberLvlService;
    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 插入服务项
     *
     * @param serviceItem 要增加的服务项
     * @return
     */
    @RequestMapping(value = "/home/serviceItem/insertServiceItem", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertServiceItem(@RequestBody ServiceItem serviceItem) throws SQLException {
        if(serviceItemService.selectServiceItemByItemName(serviceItem.getItemName())!=null){
            return ReturnResult.buildEnumResult(Constant.StateCode.SERVICE_ITEM_NAME_EXIST);
        }
        if (serviceItem.getBusinessId() == null)
            serviceItem.setBusinessId(UserDefaultUtil.getBusinessId());
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.insertServiceItem(serviceItem));
    }

    /**
     * 会员折扣设置
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/serviceItem/insertItemAndMemberLvl", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertItemAndMemberLvl(@RequestBody Map param) throws SQLException {
        ItemAndMemberLvl itemAndMemberLvl = new ItemAndMemberLvl();
        //全部批量设置
        if (ValidUtil.isNotEmpty(param.get("memberLvlId")) && ValidUtil.isEmpty(param.get("itemType")) && ValidUtil.isEmpty(param.get("serviceItemId"))) {
            //查询全部批量设置的折扣是否存在
            List<ItemAndMemberLvl> list = itemAndMemberLvlService.selectItemAndMemberLvlByParam(param);
            //全部批量设置的折扣存在，则只更新
            if (ValidUtil.isNotEmpty(list) && list.size() > 0) {
                ItemAndMemberLvl itemAndMember = list.get(0);
                itemAndMember.setDiscountNumber(Double.valueOf(param.get("discountNumber").toString()));
                itemAndMemberLvlService.updateItemAndMemberLvl(itemAndMember);
                itemAndMemberLvlService.deleteItemAndMemberLvlByParam(param);
                return ReturnResult.buildSuccessMsg();
            }
            //全部批量设置的折扣不存在，则新增
            itemAndMemberLvl.setMemberLvlId(Integer.valueOf(param.get("memberLvlId").toString()));
            itemAndMemberLvl.setDiscountType(Constant.ItemAndMemberLvlDiscountType.VIP_DISCOUNT.codeName);
            itemAndMemberLvl.setDiscountNumber(Double.valueOf(param.get("discountNumber").toString()));
            itemAndMemberLvlService.insertItemAndMemberLvl(itemAndMemberLvl);
            itemAndMemberLvlService.deleteItemAndMemberLvlByParam(param);
            return ReturnResult.buildSuccessMsg();
        }
        //项目项批量设置
        if (ValidUtil.isNotEmpty(param.get("memberLvlId")) && ValidUtil.isNotEmpty(param.get("itemType")) && ValidUtil.isEmpty(param.get("serviceItemId"))) {
            //查询项目项设置的折扣是否存在
            List<ItemAndMemberLvl> list = itemAndMemberLvlService.selectItemAndMemberLvlByParam(param);
            //项目项设置的折扣存在，则只更新
            if (ValidUtil.isNotEmpty(list) && list.size() > 0) {
                ItemAndMemberLvl itemAndMember = list.get(0);
                itemAndMember.setDiscountNumber(Double.valueOf(param.get("discountNumber").toString()));
                itemAndMemberLvlService.updateItemAndMemberLvl(itemAndMember);
                itemAndMemberLvlService.deleteItemAndMemberLvlByParam(param);
                return ReturnResult.buildSuccessMsg();
            }
            //项目项设置的折扣不存在，则新增
            itemAndMemberLvl.setMemberLvlId(Integer.valueOf(param.get("memberLvlId").toString()));
            itemAndMemberLvl.setItemType(Integer.valueOf(param.get("itemType").toString()));
            itemAndMemberLvl.setDiscountType(Constant.ItemAndMemberLvlDiscountType.VIP_DISCOUNT.codeName);
            itemAndMemberLvl.setDiscountNumber(Double.valueOf(param.get("discountNumber").toString()));
            itemAndMemberLvlService.insertItemAndMemberLvl(itemAndMemberLvl);
            itemAndMemberLvlService.deleteItemAndMemberLvlByParam(param);
            return ReturnResult.buildSuccessMsg();
        }
        //查询服务项设置的折扣是否存在
        List<ItemAndMemberLvl> list = itemAndMemberLvlService.selectItemAndMemberLvlByParam(param);
        //服务项设置的折扣存在，则只更新
        if (ValidUtil.isNotEmpty(list) && list.size() > 0) {
            ItemAndMemberLvl itemAndMember = list.get(0);
            itemAndMember.setDiscountType(Integer.valueOf(param.get("discountType").toString()));
            itemAndMember.setDiscountNumber(Double.valueOf(param.get("discountNumber").toString()));
            itemAndMemberLvlService.updateItemAndMemberLvl(itemAndMember);
            return ReturnResult.buildSuccessMsg();
        }
        //服务项设置的折扣不存在，则新增
        itemAndMemberLvl.setMemberLvlId(Integer.valueOf(param.get("memberLvlId").toString()));
        itemAndMemberLvl.setItemType(Integer.valueOf(param.get("itemType").toString()));
        itemAndMemberLvl.setServiceItemId(Integer.valueOf(param.get("serviceItemId").toString()));
        itemAndMemberLvl.setDiscountType(Integer.valueOf(param.get("discountType").toString()));
        itemAndMemberLvl.setDiscountNumber(Double.valueOf(param.get("discountNumber").toString()));
        return ReturnResult.buildSuccessMsg().setData(itemAndMemberLvlService.insertItemAndMemberLvl(itemAndMemberLvl));
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 删除服务项
     *
     * @param serviceItem 服务项
     * @return
     */
    @RequestMapping(value = "/home/serviceItem/deleteServiceItem", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteServiceItem(@RequestBody ServiceItem serviceItem) throws SQLException {
        serviceItemService.deleteServiceItem(serviceItem.getId());
        return ReturnResult.buildSuccessMsg();
    }


    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 更新服务项
     *
     * @param serviceItem
     * @return
     */
    @RequestMapping(value = "/home/serviceItem/updateServiceItem", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateServiceItem(@RequestBody ServiceItem serviceItem) throws SQLException {
        ServiceItem serviceItem1 = serviceItemService.selectServiceItemByItemName(serviceItem.getItemName());
        if(serviceItem1!=null && !serviceItem1.getId().equals(serviceItem.getId())){
            return ReturnResult.buildEnumResult(Constant.StateCode.SERVICE_ITEM_NAME_EXIST);
        }
        serviceItemService.updateServiceItem(serviceItem);
        return  ReturnResult.buildSuccessMsg();
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    /**
     * 分页查询服务项数据
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/serviceItem/selectPageServiceItem", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageOperatorBusinessUser(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectPageServiceItem(param));
    }

    /**
     * 验证服务项是否可以删除or修改，如果有被购买，正在使用的无法删除，true可以删除，false不可以删除
     *
     * @param idMap 服务项id
     * @return
     */
    @RequestMapping(value = "/home/serviceItem/selectWhetherServiceItemEdit", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult whetherServiceItemEdit(@RequestBody Map idMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectCountServiceItemHaveBuyNum(Integer.parseInt(idMap.get("id").toString())) <= 0);
    }

    /**
     * 查询服务项列表数据
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/serviceItem/selectServiceItemList", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectServiceItemList(@RequestBody(required = false) Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectServiceItemList(param));
    }

    /**
     * 根据项目分类展示服务项
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/serviceItem/selectServiceItemDiscountNumber",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectServiceItemDiscountNumber(@RequestBody Map param) throws SQLException {
        param.put("businessId",UserDefaultUtil.getBusinessId());
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectServiceItemDiscountNumber(param));
    }

    /**
     * 获取创建工单时，用到的服务项（包含会员折扣）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/serviceItem/selectPageServiceItemForWorkOrder", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageServiceItemForWorkOrder(@RequestBody Map param) throws SQLException {

        //判断参数是否有userId，如果有的话，就设置查询会员的会员等级
        if(!ValidUtil.isEmpty(param.get("userId"))) {
            MemberLvlRecord record = memberLvlRecordService.selectMemberLvlRecordWithMemberLvl(Integer.valueOf(param.get("userId").toString()), UserDefaultUtil.getBusinessId());
            if(record != null) {
                param.put("memberLvlId", record.getMemberLvlId());
            }
        }

        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectPageServiceItemForWorkOrder(param));
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

    /**
     * 获取商家服务项列表（用户端）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/serviceItem/selectServiceItemForOpen", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectServiceItemForOpen(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectServiceItemList(param));
    }

    /**
     * 分页查询商家服务项目信息
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/serviceItem/selectBusinessServiceItem", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessServiceItem(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectPageServiceItemForWorkOrder(param));
    }

    /**
     * 根据服务项目ID，获取信息
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/serviceItem/selectBusinessServiceItemById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessServiceItemById(@RequestBody Map param) throws SQLException {
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setId(Integer.valueOf(param.get("serviceId").toString()));
        return ReturnResult.buildSuccessMsg().setData(serviceItemService.selectServiceItem(serviceItem));
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}