package com.software5000.ma.aliyunSms;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 短信处理类
 * Created by wujin on 2017/1/8.
 */
@Service
public class SmsService {

    private Log log = LogFactory.getLog(SmsService.class);

    /**
     * 发送的请求类型
     */
    public enum SmsRequestType {
        BUSINESS_CHANGE_CAR("BUSINESS_CHANGE_CAR_"),//商家修改其会员用户车牌号
        BUSINESS_CHANGE_MOBILE("BUSINESS_CHANGE_MOBILE_"),//商家修改其会员用户手机号
        USER_USE_MOBILE_LOGIN("USER_USE_MOBILE_LOGIN_"),//用户手机号登陆
        USER_FORGET_PWD("USER_FORGET_PWD_"), //用户忘记密码
        USER_CHANGE_MOBILE("USER_CHANGE_MOBILE_") //修改手机号
        ;
        public String requestName;

        private SmsRequestType(String requestName) {
            this.requestName = requestName;
        }
    }

    /**
     * key:Constant.SmsRequest+手机号 value:内存中存在的发送短信记录
     */
    private static Map<String, SmsRecord> smsRecordMap = new HashMap<>();

    private static SmsService instance;


    /**
     * 验证短信是否符合发送规则
     *
     * @param expand         附加属性
     * @param mobile         手机号
     * @param smsRequestType 请求类型
     * @throws ServiceException
     */
    private void validSmsSend(String expand, String mobile, SmsRequestType smsRequestType) throws ServiceException {
        String key = smsRequestType.requestName + mobile;
        //判断该手机号在该请求上是否存在
        SmsRecord smsRecord = smsRecordMap.get(key);
        if (smsRecord == null) {
            //第一次发跳过验证
            smsRecordMap.put(key, initSmsRecord(expand, mobile));
        } else {
            Long thisTime = System.currentTimeMillis();
            //验证时间间隔是否符合
            if (validTimeIntervalIsTooShort(smsRecord.getLastSendTime(), thisTime)) {
                //发送短信间隔太短
                throw new ServiceException(Constant.StateCode.SEND_MSG_TIME_INTERVAL_TOO_SHORT.codeName);
            }
            LimitQueue<Long> limitTimeQueue = smsRecord.getLimitTimeQueue();
            limitTimeQueue.offer(System.currentTimeMillis());
            //验证单位时间内发送的频率
            if (validFrequencyIsTooHigh(limitTimeQueue)) {
                //发送短信频率太高
                throw new ServiceException(Constant.StateCode.SEND_MSG_FREQUENCY_TOO_HIGH.codeName);
            }
            if (expand != null) smsRecord.setExpand(expand);
            smsRecord.setExpirationTime(getExpirationMillisecondTime());
            smsRecord.setTimes((smsRecord.getTimes() + 1));
            smsRecord.setLimitTimeQueue(limitTimeQueue);
        }
    }

    /**
     * 获取发送记录
     *
     * @param mobile
     * @param smsRequestType
     * @return
     */
    public SmsRecord getSmsRecord(String mobile, SmsRequestType smsRequestType) {
        SmsRecord smsRecord = getSmsRecordMap().get(smsRequestType.requestName + mobile);
        log.debug("获取短信记录SmsRecord,验证码："+smsRecord.getExpand()+",过期时间："+smsRecord.getExpirationTime()+"，队列信息："+LimitQueue.getLimitQueueInfo(smsRecord.getLimitTimeQueue())+",smsRecord="+smsRecord.toString());
        return smsRecord;
    }

    /**
     * 获取内存中的所有记录
     *
     * @return
     */
    public Map<String, SmsRecord> getSmsRecordMap() {
        return smsRecordMap;
    }

    /**
     * 清楚内存数据
     */
    public void removeSmsRecord(String key) {
        smsRecordMap.remove(key);
    }

    /**
     * 清除内存数据
     *
     * @param mobile
     * @param smsRequestType
     */
    public void removeSmsRecord(String mobile, SmsRequestType smsRequestType) {
        removeSmsRecord(smsRequestType.requestName + mobile);
    }

    /**
     * 验证时间间隔是否太短
     *
     * @param lastTime
     * @param thisTime
     * @return
     */
    private boolean validTimeIntervalIsTooShort(Long lastTime, Long thisTime) {
        return (thisTime - lastTime) / 1000 < Integer.parseInt(Constant.getCodeByName(Constant.AliyunSms.BETWEEN_LONGER).getCodeValue());
    }

    /**
     * 验证时间频率是否太高
     *
     * @param limitTimeQueue
     * @return
     */
    private boolean validFrequencyIsTooHigh(LimitQueue<Long> limitTimeQueue) {
        boolean isTooHigh = true;
        if (limitTimeQueue.size() <= getLimitTime()) {
            isTooHigh = false;
        } else {
            isTooHigh = (limitTimeQueue.lastElement() - limitTimeQueue.peek()) < Integer.parseInt(Constant.getCodeByName(Constant.AliyunSms.UNIT_TIME).getCodeValue());
        }
        return isTooHigh;
    }

    /**
     * 验证验证码是否过期
     *
     * @param expiredTime 过期时间
     * @return true为过期
     */
    public boolean validExpirationTimeIsExpired(Long expiredTime) {
        log.debug("验证验证码过期判断,过期时间："+expiredTime+"，当前时间："+System.currentTimeMillis());
        return System.currentTimeMillis() > expiredTime;
    }

    /**
     * 验证验证码是否正确
     * @param code
     * @return
     */
    public boolean validCodeisRight(String code,SmsRecord smsRecord) throws ServiceException{
        if (smsRecord == null) return false;
        //验证码是否过期
        if (validExpirationTimeIsExpired(smsRecord.getExpirationTime())) {
            //验证码过期
            throw new ServiceException(Constant.StateCode.VERIFICATION_CODE_EXPIRES.codeName);
        }
        return smsRecord.getExpand().equals(code);
    }

    /**
     * 初始化一个新的记录
     *
     * @param expand
     * @param mobile
     * @return
     */
    private SmsRecord initSmsRecord(String expand, String mobile) {
        Long nowTime = System.currentTimeMillis();
        LimitQueue<Long> limitTimeQueue = new LimitQueue<>(getLimitTime() + 1);
        limitTimeQueue.offer(nowTime);
        log.debug("初始化短信记录SmsRecord,验证码："+expand+",过期时间："+getExpirationMillisecondTime()+"，队列信息："+LimitQueue.getLimitQueueInfo(limitTimeQueue));
        return new SmsRecord(1, nowTime, nowTime, limitTimeQueue, mobile, expand, getExpirationMillisecondTime());
    }



    /**
     * 单位时间内发送的次数
     *
     * @return
     */
    private Integer getLimitTime() {
        return Integer.parseInt(Constant.getCodeByName(Constant.AliyunSms.LIMIT_TIMES).getCodeValue());
    }

    /**
     * 验证码过期时间
     *
     * @return
     */
    private Long getExpirationMillisecondTime() {
        return getExpirationTime()*60*1000+System.currentTimeMillis();
    }

    /**
     * 验证码有效期单位分钟
     *
     * @return
     */
    private Integer getExpirationTime() {
        return Integer.parseInt(Constant.getCodeByName(Constant.AliyunSms.EXPIRATION_TIME).getCodeValue());
    }

    /**
     * 违规后限制发送的间隔单位为分钟
     */
    private Long getIllegalTimeInterval() {
        return Long.parseLong(Constant.getCodeByName(Constant.AliyunSms.RE_SEND_LONGER).getCodeValue());
    }


    /**
     * 发送验证码
     *
     * @param code
     * @param recNum
     * @param smsRequestType
     * @throws ServiceException
     */
    public void sendTemplateVerificationCodeMsg(String code, String recNum, SmsRequestType smsRequestType) throws ServiceException {
        try {
            validSmsSend(code, recNum, smsRequestType);
            sendTemplateVerificationCodeMsg(code, recNum);
        } catch (ServiceException e) {
            log.error("SmsService.sendTemplateVerificationCodeMsg", e);
        } catch (ClientException e) {
            log.error(e.getErrCode() + "  " + e.getErrMsg());
            throw new ServiceException(Constant.StateCode.SEND_MSG_ERROR.codeName);
        }
    }

    /**
     * 按模板要求发送验证码
     *
     * @param code   随机位数的验证码
     * @param recNum 手机号
     */
    public void sendTemplateVerificationCodeMsg(String code, String recNum) throws ClientException {
        String paramString = "{\"code\":\"" + code + "\"}";
        sendVerificationCodeMsg(paramString, recNum);
    }

    /**
     * 发送单个用户短信验证码
     *
     * @param paramString 模板中对应的参数
     * @param recNum      手机号
     * @throws ClientException
     */
    public void sendVerificationCodeMsg(String paramString, String recNum) throws ClientException {
        sendMsg(Constant.getCodeByName(Constant.AliyunSms.VERIFICATION_CODE_TEMPLATE).getCodeValue(), paramString, new ArrayList<String>(Arrays.asList(recNum)));
    }


    /**
     * 发送短信
     *
     * @param templateCode 控制台创建的模板CODE
     * @param paramString  模板中对应的参数
     * @param recNumLists  手机号列表
     */
    public void sendMsg(String templateCode, String paramString, List<String> recNumLists) throws ClientException {
        sendMsg(templateCode, paramString, recNumLists, Constant.getCodeByName(Constant.AliyunSms.SIGN_NAME).getCodeValue(),
                Constant.getCodeByName(Constant.AliyunSms.APP_KEY).getCodeValue(),
                Constant.getCodeByName(Constant.AliyunSms.APP_SECRET).getCodeValue());
    }

    /**
     * 发送短信
     *
     * @param templateCode 控制台创建的模板CODE
     * @param paramString  模板中对应的参数
     * @param recNumLists  手机号列表
     * @param signName     签名
     * @param appKey       授权key
     * @param appSecret    授权secret
     */
    public void sendMsg(String templateCode, String paramString, List<String> recNumLists, String signName, String appKey, String appSecret) throws ClientException {
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appKey, appSecret);
            IAcsClient client = new DefaultAcsClient(profile);
            for (String recNum : recNumLists) {
                SingleSendSmsRequest request = new SingleSendSmsRequest();
                request.setTemplateCode(templateCode);
                request.setRecNum(recNum);
                request.setParamString(paramString);
                request.setSignName(signName);
                SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            }
        } catch (ClientException e) {
            log.error("SmsService.sendMsg", e);
        }
    }

    public static void main(String[] args) {
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIcppSwOIYu0iz", "LzzwHlbj0h3ZnDZx5X1Lyuh0ptYgpp");
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendSmsRequest request = new SingleSendSmsRequest();
            request.setSignName("中晟诚品好车网");
            request.setTemplateCode("SMS_37660276");
            request.setParamString("{\"code\":\"6548\"}");
//            request.setRecNum("18030108354");
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            System.out.println(httpResponse);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
