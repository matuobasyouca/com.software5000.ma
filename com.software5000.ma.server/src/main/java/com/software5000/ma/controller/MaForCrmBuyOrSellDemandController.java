package com.software5000.ma.controller;

import com.software5000.ma.entity.Business;
import com.software5000.ma.service.BusinessService;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.util.PostUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Controller
public class MaForCrmBuyOrSellDemandController {
    @Resource
    private BusinessService businessService;

    /**
     * 跨MA接口查询商家列表
     * @param param
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @RequestMapping(value = "/home/buyDemand/selectPageBuyDemand", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageBuyDemand(@RequestBody Map param)throws SQLException,IOException {
        return PostUtil.postCRM(Constant.Crm.SELECT_PAGE_BUY_DEMAND.codeName, param);
    }
    /**
     * 跨MA接口查询商家列表
     * @param param
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @RequestMapping(value = "/home/sellDemand/selectPageSellDemand", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageSellDemand(@RequestBody Map param)throws SQLException,IOException {
       //查询商家顾问id
        Business business=businessService.selectBusinessByIdForConsultantId(Integer.valueOf(param.get("businessId").toString()));
        if(business.getConsultantId()==null){
            return ReturnResult.buildSuccessMsg();
        }else {
            param.put("consultantId", business.getConsultantId());
            return PostUtil.postCRM(Constant.Crm.SELECT_PAGE_SELL_DEMAND.codeName, param);
        }
    }



}
