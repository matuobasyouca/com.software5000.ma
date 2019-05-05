package com.software5000.util;

import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.template.Data;
import com.riversoft.weixin.mp.template.Templates;
import com.riversoft.weixin.mp.user.Users;
import com.software5000.base.Constant;
import com.software5000.ma.dto.WxMsgDto;
import com.software5000.ma.entity.PackClusterBuyRecord;
import com.software5000.ma.entity.WorkOrder;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiang on 2017/07/26.
 */
public class WxMsgUtil {

    private static Log log = LogFactory.getLog(WxMsgUtil.class);

    //默认字体颜色
    private static String COLOR = "#030303";

    /**
     * 工单结算，发送消息提醒
     * @param openId
     * @param order
     */
    public static void sendMsgForSettle(String openId, WorkOrder order) {
        if(!ValidUtil.isEmpty(openId)) {
            try {
                Map<String, Object> eParam = new HashMap<String, Object>();
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //跳转地址
                String url = Constant.maUrl + "/wap/customer/open/consume/detail.html?id=" + order.getId();
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data("您的爱车服务已结账", COLOR));
                msgData.put("keyword1", new Data(order.getCarNumber(), COLOR));
                msgData.put("keyword2", new Data(order.getBusinessName(), COLOR));
                StringBuffer serviceItemStr = new StringBuffer();
                order.getWorkOrderDetails().forEach(s -> serviceItemStr.append(s.getServiceItemName()).append("/"));
                msgData.put("keyword3", new Data(serviceItemStr.length() >= 16 ? serviceItemStr.substring(0, 15) + "..."  : serviceItemStr.substring(0, serviceItemStr.length()-1).toString(), COLOR));
                msgData.put("keyword4", new Data(order.getTotalPrice() + "元", COLOR));
                msgData.put("keyword5", new Data(DateUtils.formatDate(order.getCommitTime(), DateUtils.DATE_FULL_STR), COLOR));
                //TODO 后续判断有没有赠送抽奖次数或卡券，有的话显示备注，并设置跳转链接
                templates.send(openId, Constant.settleOrderTmpl, url, msgData);
            }catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForSettle", e);
            }
        }
    }

    /**
     * 使用套餐卡发送消息
     * @param openId
     * @param dto
     */
    public static void sendMsgForConsumePackage(String openId, WxMsgDto dto) {
        if(!ValidUtil.isEmpty(openId)) {
            try {
                Map<String, Object> eParam = new HashMap<String, Object>();
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //跳转地址
                String url = Constant.maUrl + "/wap/customer/open/package.html?i=" + java.util.Base64.getEncoder().encodeToString(openId.getBytes());
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data(dto.getFirst(), COLOR));
                msgData.put("keyword1", new Data(dto.getKeyword1(), COLOR));
                msgData.put("keyword2", new Data(dto.getKeyword2(), COLOR));
                msgData.put("keyword3", new Data(dto.getKeyword3(), COLOR));
                templates.send(openId,Constant.consumePackageTmpl, url, msgData);
            }catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForConsumePackage", e);
            }
        }
    }

    /**
     * 发送套餐到期提醒
     * @param openId
     * @param dto
     */
    public static void sendMsgForExpirePackage(String openId, WxMsgDto dto) {
        if(!ValidUtil.isEmpty(openId)) {
            try {
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //跳转地址
                String url = Constant.maUrl+ "/wap/customer/open/package.html?i=" + java.util.Base64.getEncoder().encodeToString(openId.getBytes());
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data(dto.getFirst(), COLOR));
                msgData.put("keyword1", new Data(dto.getKeyword1(), COLOR));
                msgData.put("keyword2", new Data(dto.getKeyword2(), COLOR));
                templates.send(openId, Constant.expirePackageTmpl, url, msgData);
            }catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForConsumePackage", e);
            }
        }
    }

    /**
     * 开团成功，发送消息提醒
     *
     * @param openId
     * @param packClusterBuyRecord
     */
    public static void sendMsgForOpenGroup(String openId, PackClusterBuyRecord packClusterBuyRecord) {
        if (!ValidUtil.isEmpty(openId)) {
            try {
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //跳转地址
                String url = Constant.maUrl + "/wap/customer/open/group/details.html?id="
                        + packClusterBuyRecord.getId() + "&i=" + Base64.getEncoder().encodeToString(packClusterBuyRecord.getWxOpenId().getBytes());
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data("你已成功开团，还差"
                        + (packClusterBuyRecord.getPackCluster().getClusterNum() -1)
                        + "人，赶紧邀请好友拼单吧", COLOR));
                msgData.put("keyword1", new Data(packClusterBuyRecord.getPackCluster().getBusinessPackage().getPackageName(), COLOR));
                msgData.put("keyword2", new Data(String.valueOf(packClusterBuyRecord.getPackCluster().getSalePrice())+"元", COLOR));
                msgData.put("keyword3", new Data(Users.defaultUsers().get(openId).getNickName(), COLOR));
                msgData.put("keyword4", new Data(String.valueOf(packClusterBuyRecord.getPackCluster().getClusterNum()), COLOR));
                msgData.put("keyword5", new Data(DateUtils.formatDate(packClusterBuyRecord.getEndDay(),DateUtils.DATE_FULL_STR), COLOR));
                templates.send(openId, Constant.openGroupTmpl, url, msgData);
            } catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForOpenGroup", e);
            }
        }
    }

    /**
     * 参团成功，发送消息提醒
     *
     * @param openId
     * @param packClusterBuyRecord
     */
    public static void sendMsgForJoinGroup(String openId, PackClusterBuyRecord packClusterBuyRecord) {
        if (!ValidUtil.isEmpty(openId)) {
            try {
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //跳转地址
                String url = Constant.maUrl + "/wap/customer/open/group/details.html?id="
                        + packClusterBuyRecord.getId() + "&i=" + Base64.getEncoder().encodeToString(packClusterBuyRecord.getWxOpenId().getBytes());
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data("你已成功参团，还差"
                        + (packClusterBuyRecord.getPackCluster().getClusterNum() - packClusterBuyRecord.getPackCluster().getSuccessNum())
                        + "人，赶紧邀请好友拼单吧", COLOR));
                msgData.put("keyword1", new Data(packClusterBuyRecord.getPackCluster().getBusinessPackage().getPackageName(), COLOR));
                msgData.put("keyword2", new Data(String.valueOf(packClusterBuyRecord.getPackCluster().getSalePrice())+"元", COLOR));
                msgData.put("keyword3", new Data(Users.defaultUsers().get(packClusterBuyRecord.getColonelOpenId()).getNickName(), COLOR));
                msgData.put("keyword4", new Data(String.valueOf(packClusterBuyRecord.getPackCluster().getClusterNum()), COLOR));
                msgData.put("keyword5", new Data(DateUtils.formatDate(packClusterBuyRecord.getEndDay(),DateUtils.DATE_FULL_STR), COLOR));
                templates.send(openId, Constant.joinGroupTmpl, url, msgData);
            } catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForJoinGroup", e);
            }
        }
    }

    /**
     * 拼团成功，发送消息提醒
     *
     * @param openId
     * @param packClusterBuyRecord
     */
    public static void sendMsgForSpellGroupSuccess(String openId, PackClusterBuyRecord packClusterBuyRecord) {
        if (!ValidUtil.isEmpty(openId)) {
            try {
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //跳转地址
                String url = Constant.maUrl + "/wap/customer/open/group/details.html?id="
                        + packClusterBuyRecord.getId() + "&i=" + Base64.getEncoder().encodeToString(packClusterBuyRecord.getWxOpenId().getBytes());
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data("你参与的拼团已成功，套餐已可使用。", COLOR));
                msgData.put("keyword1", new Data(packClusterBuyRecord.getPackCluster().getBusinessPackage().getPackageName(), COLOR));
                msgData.put("keyword2", new Data(String.valueOf(packClusterBuyRecord.getPackCluster().getSalePrice())+"元", COLOR));
                templates.send(openId, Constant.spellGroupSuccessTmpl, url, msgData);
            } catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForSpellGroupSuccess", e);
            }
        }
    }

    /**
     * 拼团失败，发送消息提醒
     *
     * @param openId
     * @param packClusterBuyRecord
     */
    public static void sendMsgForSpellGroupFail(String openId, PackClusterBuyRecord packClusterBuyRecord) {
        if (!ValidUtil.isEmpty(openId)) {
            try {
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //跳转地址
                String url = Constant.maUrl + "/wap/customer/open/group/details.html?id="
                        + packClusterBuyRecord.getId() + "&i=" + Base64.getEncoder().encodeToString(packClusterBuyRecord.getWxOpenId().getBytes())+"&payState="+Constant.OrderState.HAVE_REFUND.codeName;
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data("你参与的拼团未达到拼团人数，拼团失败，金额已退回至微信零钱。", COLOR));
                msgData.put("keyword1", new Data(packClusterBuyRecord.getPackCluster().getBusinessPackage().getPackageName(), COLOR));
                msgData.put("keyword2", new Data(String.valueOf(packClusterBuyRecord.getPackCluster().getSalePrice())+"元", COLOR));
                msgData.put("keyword3", new Data(String.valueOf(packClusterBuyRecord.getPayMoney()/100.0)+"元", COLOR));
                templates.send(openId, Constant.spellGroupFailTmpl, url, msgData);
            } catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForSpellGroupFail", e);
            }
        }
    }

    /**
     * 参团失败，发送消息提醒
     *
     * @param openId
     * @param packClusterBuyRecord
     */
    public static void sendMsgForJoinGroupFail(String openId, PackClusterBuyRecord packClusterBuyRecord) {
        if (!ValidUtil.isEmpty(openId)) {
            try {
                Templates templates = Templates.with(AppSetting.defaultSettings());
                //内容参数
                Map<String, Data> msgData = new HashMap<String, Data>();
                msgData.put("first", new Data("您好，您参与的拼团失败了！", COLOR));
                msgData.put("keyword1", new Data(packClusterBuyRecord.getPackCluster().getBusinessPackage().getPackageName(), COLOR));
                msgData.put("keyword2", new Data("参团人数已满", COLOR));
                msgData.put("remark", new Data("付款金额已退回至您的微信零钱。", COLOR));
                templates.send(openId, Constant.joinGroupFailTmpl,null, msgData);
            } catch (Exception e) {
                log.error("WxMsgUtil.sendMsgForJoinGroupFail", e);
            }
        }
    }
}
