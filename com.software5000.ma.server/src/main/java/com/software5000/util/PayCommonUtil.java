package com.software5000.util;

import com.riversoft.weixin.pay.payment.bean.UnifiedOrderRequest;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.ValidUtil;
import com.zscp.master.util.encrypt.MD5Builder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayCommonUtil {
	public static Log log = LogFactory.getLog(PayCommonUtil.class);

	public static String TYPE_OF_WORKORDER = "w";
	public static String TYPE_OF_PACKAGEORDER = "p";
	public static String TYPE_OF_COOPERCOMBOORDER = "c";
	public static String TYPE_OF_COUPONSPACK = "b";
	public static String TYPE_OF_EMKT_COUPONSPACK = "cp";//卡包
	public static String TYPE_OF_PACK_CLUSTER = "pt";//拼团
	public static String TYPE_OF_RECHARGEORDER = "r";//充值
	public static SortedMap<Object, Object> xmlConvertToMap(String rxml) {
		// 解析xml成map
		Map<String, String> m = new HashMap<String, String>();
		try {
			m = XMLUtil.doXMLParse(rxml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		// 过滤空 设置 SortedMap
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String parameter = (String) it.next();
			String parameterValue = m.get(parameter);

			String v = "";
			if (null != parameterValue) {
				v = parameterValue.trim();
			}
			packageParams.put(parameter, v);
		}
		return packageParams;
	}

	/**
	 * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * 
	 * @return boolean
	 */
	public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams,
			String API_KEY) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}

		sb.append("key=" + API_KEY);

		// 算出摘要
		String mysign = MD5Builder.encodeToString(sb.toString(), characterEncoding).toLowerCase();
		String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();

		return tenpaySign.equals(mysign);
	}

	/**
	 * @author
	 * @date 2016-4-22
	 * @Description：sign签名
	 * @param characterEncoding
	 *            编码格式
	 * @return
	 */
	public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + API_KEY);
		String sign = MD5Builder.encodeToString(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	/**
	 * @author
	 * @date 2016-4-22
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	public static String getNonce_str() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String bg = outFormat.format(now);
		bg.substring(8, bg.length());
		String end = String.valueOf(buildRandom(4));
		return bg+end;
	}

	/**
	 * 封装微信h5统一下单信息
	 */
	public static UnifiedOrderRequest getUnifiedOrderRequest(String attach,String billCreatedIp, String bodyName, String notifyUrl, String openid,
															 String outOrder, String productId, String tradeType, int tradeFee) {
		UnifiedOrderRequest unifiedOrderRequest = new UnifiedOrderRequest();
		unifiedOrderRequest.setBillCreatedIp(billCreatedIp);
		unifiedOrderRequest.setBody(bodyName);
		unifiedOrderRequest.setTradeNumber(outOrder);
		unifiedOrderRequest.setTradeType(tradeType);
		unifiedOrderRequest.setTotalFee(tradeFee);
		if (null != notifyUrl) {
			unifiedOrderRequest.setNotifyUrl(notifyUrl);
		}
		if (null != openid) {
			unifiedOrderRequest.setOpenId(openid);
		}
		if (null != productId) {
			unifiedOrderRequest.setProductId(productId);
		}
        if(null != attach){
            unifiedOrderRequest.setAttach(attach);
        }
		return unifiedOrderRequest;
	}

	/**
	 * 根据id,价格，类型，时间(yyyyMMddHH)来生成订单号，只要这3个值确定则订单号也是确定的,增加时间是因为微信的订单有效期为2小
	 * 时需重新生成，所以设定每小时的订单变动一次
	 * @param id
	 * @param price
	 * @param type
	 * @param originalOutTradeNo 原来的订单号 必须按照格式来
	 * @return
	 */
	public static String getOutTradeNo(Integer id,Integer price,String type,String originalOutTradeNo){
		if(ValidUtil.isNotEmpty(originalOutTradeNo)){
			//解析订单号
			String[] strings = originalOutTradeNo.split("_");
			//1.数据必须长度是4; 2.价格不变; 3.未过期 则无需重新生成订单号
			if((strings.length==4)&&(price.equals(Integer.valueOf(strings[1])))&&(DateUtils.diffDate(DateUtils.MINUTE,DateUtils.parse(strings[3],"yyyyMMddHHmm"),new Date())<0)){
				return originalOutTradeNo;
			}
		}
		//二维码过期时间
		String overTime = DateUtils.formatDate(DateUtils.add(new Date(), DateUtils.HOUR, 1), "yyyyMMddHHmm");
		String outTradeNo = id + "_" + price + "_" + type +"_"+ overTime;
		return outTradeNo;
	}

	/**
	 * 根据订单号，解析ID
	 * @param outTradeNo
	 * @return
	 */
	public static String getTradeId(String outTradeNo){
		if(ValidUtil.isNotEmpty(outTradeNo)){
			//解析订单号
			String[] strings = outTradeNo.split("_");
			if(strings.length == 4) {
				return strings[0];
			}
		}
		return null;
	}

	/**
	 * 获取公共跳转地址
	 * @param businessId
	 * @return
	 */
	public static String getCommonRedirectUrl(Integer businessId, String orderNo) throws IOException {

		//获取是否有红包卡券
		String uuid = Constant.getStringCodeValueByName(Constant.CashierDesk.SHARE_RED_PACKET_UUID);
		if(!ValidUtil.isEmpty(uuid)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("uuid", uuid);
			param.put("businessId", businessId);

			//获取可用商家
			ReturnResult returnResult = PostUtil.postEMKT(Constant.Emkt.SELECT_BUSINESS_CANUSE_FOR_UUID.codeName ,param);
			if(returnResult.getCode().equals(Constant.StateCode.SUCCESS.codeName) && returnResult.getData() != null){
				if((boolean)returnResult.getData()) {
					return Constant.maUrl + Constant.getStringCodeValueByName(Constant.CashierDesk.SHARE_RED_PACKET_URL) + "?cpuuid=" + uuid + "&businessId=" + businessId + "&domain=" + Constant.emktUrl;
				}
			}
		}
		return null;
	}
}
