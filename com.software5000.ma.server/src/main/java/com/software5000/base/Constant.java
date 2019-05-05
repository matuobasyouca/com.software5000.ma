package com.software5000.base;

import com.software5000.base.entity.AreaCode;
import com.software5000.base.entity.SystemCode;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 全局变量名称定义类
 */
public class Constant {
    //图片域名
    public static String imgUrl;
    //ma域名
    public static String maUrl;
    //emkt域名
    public static String emktUrl;
    //crm域名
    public static String crmUrl;
    //跳转路径
    public static String redirectUrl;
    //工单结算提醒模板ID
    public static String settleOrderTmpl;
    //套餐消费提醒模板ID
    public static String consumePackageTmpl;
    //套餐到期提醒模板ID
    public static String expirePackageTmpl;
    //开团成功提醒模板ID
    public static String openGroupTmpl;
    //参团成功提醒模板ID
    public static String joinGroupTmpl;
    //拼团成功提醒模板ID
    public static String spellGroupSuccessTmpl;
    //拼团失败提醒模板ID
    public static String spellGroupFailTmpl;
    //参团失败提醒模板ID
    public static String joinGroupFailTmpl;

    public static String doMain;

    public Constant(String imgUrl,String maUrl,String emktUrl,String crmUrl,String redirectUrl,String settleOrderTmpl,String consumePackageTmpl,String expirePackageTmpl,String openGroupTmpl,String joinGroupTmpl,String spellGroupSuccessTmpl,String spellGroupFailTmpl,String joinGroupFailTmpl,String doMain) {
        this.imgUrl=imgUrl;
        this.maUrl=maUrl;
        this.emktUrl=emktUrl;
        this.crmUrl=crmUrl;
        this.redirectUrl=redirectUrl;
        this.settleOrderTmpl=settleOrderTmpl;
        this.consumePackageTmpl=consumePackageTmpl;
        this.expirePackageTmpl=expirePackageTmpl;
        this.openGroupTmpl=openGroupTmpl;
        this.joinGroupTmpl=joinGroupTmpl;
        this.spellGroupSuccessTmpl=spellGroupSuccessTmpl;
        this.spellGroupFailTmpl=spellGroupFailTmpl;
        this.joinGroupFailTmpl=joinGroupFailTmpl;
        this.doMain=doMain;
    }

    private static Log log = LogFactory.getLog(Constant.class);

    //***********************（start）需要从数据库获取，此处只需要记录对应的key**************************************************

    public static class SysConfig{
        /**
         * jwt 盐值
         */
        public static String JWT_SHARED_KEY = "jwtSharedKey";
    }

    /**
     * 云脉图片识别
     */
    public static class YunmaiOCR {
        /**
         * 接口链接
         */
        public static String ENGINE_URL = "engineUrl";
        /**
         * 平台账号
         */
        public static String USER_NAME = "username";
        /**
         * 密码
         */
        public static String PASS_WORD = "password";
    }

    /**
     * 实体名称：无
     * 字段：无
     * 说明：微信消息模板ID
     */
    public enum ImagePath {
        DEFAULT_PATH("defaultPath", "默认路径"),
        WORK_ORDER_IMG("workOrderImg","工单图片"),
        MERCHANT_LOGO_IMG("merchantLogoImg","商家LOGO图片"),
//        IMAGE_DOMIAN_URL("imageDomianUrl","图片域名"),
        IMAGE_SAVEROOT_PATH("imageSaveRootPath","写入根路径"),
        PACK_CLUSTER_IMG("packClusterImg","工单图片"),
        ;

        public String codeName; // 变量名称
        public String desc; // 描述

        ImagePath(String codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：无
     * 字段：无
     * 说明：微信关注的消息
     */
    public enum SubscribeText {
        PACK_CLUSTER_CONTENT("packClusterContent", "拼团后扫码关注的回复内容"),
        SEARCH_CONTENT("searchContent", "微信搜索关注的回复内容"),
        ;

        public String codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "无"; // 实体名称
        public String colName = "无"; // 字段名称
        public String colDesc = "微信关注的消息"; // 字段说明

        SubscribeText(String codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    //***********************（end）需要从数据库获取，此处只需要记录对应的key**************************************************

    //***********************（start）不需要从数据库获取**************************************************

    /**
     * 实体名称：无
     * 字段：无
     * 说明：基础状态
     */
    public enum StateCode {
        MERCHANT_NOT_AUTHENTICATION("ZS004001","商家没有权限"),
        CUSTOMER_NOT_AUTHENTICATION("ZS004002","用户没有权限"),
        LOGIN_SUCCESS("ZS001000","登陆成功"),
        REGISTER_SUCCESS("ZS001001","注册成功"),
        LOGIN_FAIL("ZS002000","用户名或密码错误"),
        BUSINESS_ONLINE_FAIL("ZS003000","商家已禁用"),
        VAILD_CODE_ERROR("ZS002001","验证码错误"),
        SUCCESS("ZS011000","操作成功"),
        ERROR("ZS012000","操作失败"),
        SAVE_ERROR("ZS012001","保存失败"),
        SELECT_ERROR("ZS012002","查询失败"),
        DELETE_ERROR("ZS012003","删除失败"),
        JSON_CHANGE_ERROR("ZS012004","JSON转换失败"),
        UPDATE_ERROR("ZS012005","更新失败"),
        NO_USER("ZS012006","用户不存在"),
        FILE_WRITING_FAILED("ZS012007","文件写入失败"),
        SEND_MSG_ERROR("ZS012008","短信发送失败，请重试！"),
        SEND_MSG_TIME_INTERVAL_TOO_SHORT("ZS012009","短信发送时间间隔过短，请稍后再试！"),
        SEND_MSG_FREQUENCY_TOO_HIGH("ZS012010","短信发送频率太高，请稍后再试！"),
        SEND_MSG_ILLEGAL_OPERATION("ZS012011","因违规操作，暂时无法发送短信，请稍后再试！"),
        INSERT_MEMBER_FAILED("ZS012012","新增会员失败"),
        BUY_PACKAGE_FAILED("ZS012013","购买套餐异常"),
        BIND_CAR_FAILED("ZS012014","绑定车辆异常"),
        VERIFICATION_CODE_ERROR("ZS012015","验证码错误"),
        NO_MOBILE("ZS012016","手机号不存在"),
        CHANGE_PWD_ERROR("ZS012017","修改密码失败"),
        VERIFICATION_CODE_INVALID("ZS012018","验证码无效"),
        VERIFICATION_CODE_EXPIRES("ZS012019","验证码已过期"),
        MOBILE_HAVE_EXIST("ZS012020","手机号已存在"),
        CAR_HAVE_BIND("ZS012021","该车牌号已经被绑定"),
        CAR_DELETE_ERROR("ZS012022","该车牌号的车辆正在服务中，无法删除"),
        PACKAGE_DEDUCTION_ERROR("ZS012023","套餐扣减失败"),
        VALID_PASSWORD_ERROR("ZS012024","修改密码校验密码失败"),
        IMG_UPLOAD_ERROR("ZS012025","图片上传失败"),
        SETTLE_MONEY_TOO_LITTLE_ERROR("ZS012026","商家核销结算的金额太少"),
        SETTLE_MONEY_TOO_MUCH_ERROR("ZS012027","单笔核销结算的金额不得超过2万元"),
        SETTLE_ERROR("ZS012028","商家核销结算失败"),
        NO_BIND_WEIXIN_ERROR("ZS012029","商家未绑定微信，无法进行核销"),
        MONEY_NO_UNIFORMITY_ERROR("ZS012030","金额不一致"),
        NO_CAR_NUMBER("ZS012031","请输入车牌号"),
        ORDER_HAS_BEEN_CANCELLED("ZS012032","订单已被取消"),
        ORDER_HAS_BEEN_PAID("ZS012033","订单已被支付"),
        COUPON_INVALID("ZS012034","优惠券失效"),
        COUPON_HANDLE_ERROR("ZS012035","优惠券处理失败"),
        IMG_ADD_ERROR("ZS012036","图片添加失败"),
        IMG_DEL_ERROR("ZS012037","图片删除失败"),
        BUSINESS_ACCOUNT_EXISTS("ZS012038","商家账号已存在"),
        BUSINESS_MOBILE_EXISTS("ZS012039","商家手机号已存在"),
        COUPONS_PACK_WX_ERR("ZS012040","该微信已购买过，无法重复购买"),
        COUPONS_PACK_CAR_ERR("ZS012041","该车牌已购买过，无法重复购买"),
        CODE_HAVE_USE_ERR("ZS012042","code已经被使用"),
        ORDER_HAVE_FAIL_ERR("ZS012043","订单已失效"),
        SERVICE_ITEM_NAME_EXIST("ZS012044","服务项名称已存在"),
        CHECK_MONEY_NO_CORRECT("ZS012045","提现金额与实际不符！"),
        CODE_EMPTY_ERR("ZS012046","传入的code为空！"),
        LOTTERY_HAVE_RECEIVE("ZS012047","奖品已领取无法重新领取"),
        WX_HAVE_OPEN_CLUSTER("ZS012048","该微信已参与拼团"),
        MOBILE_HAVE_OPEN_CLUSTER("ZS012049","该手机号已参与拼团"),
        CAR_HAVE_OPEN_CLUSTER("ZS012050","该车牌已参与拼团"),
        MOBILE_NO_EQ("ZS012051","手机号与输入的不一致"),
        CLUSTER_HAVE_SUCCESS_EQ("ZS012052","该团已满，无法再参团"),
        INSERT_USER_ER("ZS012053","新增用户失败，用户信息已经被绑定无法新增"),
        /**套餐卡相关**/
        BUSINESSPACKAGENAME_HAVE_EXIST("ZS012120","套餐卡名称已存在"),
        BUSINESSPACKAGEORDER_NOT_BE_PAY("ZS012130","订单不是未支付状态!"),

        /**工单相关***/
        WORK_ORDER_STATE_ERROR("ZS012101","工单状态错误"),
        WORK_ORDER_BUSINESS_CLOSING("ZS012102","商家关账日，无法反结算"),
        WORK_ORDER_WEBCHAT_PAY("ZS012103","该工单为微信支付，无法反结算"),
        WORK_ORDER_USE_COUPON("ZS012104","该工单使用了中晟卡券，无法反结算"),
        WORK_ORDER_RECOGNITION_FAIL("ZS012105","图片识别失败"),


        /**会员等级相关***/
        MEMBERLVL_NAME_EXIST("ZS012110","该等级名称已存在"),
        MEMBERLVL_SET_MONEY_EXIST("ZS012111","设置的单次充值金额已存在"),
        ;

        public String codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "：无"; // 实体名称
        public String colName = ""; // 字段名称
        public String colDesc = ""; // 字段说明

        StateCode(String codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：无
     * 字段：无
     * 说明：访问系统的类型
     */
    public enum VisitWebType {
        API("api","接口调用"),
        HTML("html","页面调用")
        ;

        public String codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "无"; // 实体名称
        public String colName = "无"; // 字段名称
        public String colDesc = "访问系统的类型"; // 字段说明

        VisitWebType(String codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：无
     * 字段：无
     * 说明：iss类型
     */
    public enum VisitWebIss {
        CrmIss("com.zscp.saas.crm","crmIss"),
        MaIss("com.zscp.ma","maIss"),
        EmktIss("com.zscp.emkt","emktIss")
        ;

        public String codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "无"; // 实体名称
        public String colName = "无"; // 字段名称
        public String colDesc = "iss类型"; // 字段说明

        VisitWebIss(String codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：WorkOrder
     * 字段：state
     * 说明：工单状态
     */
    public enum WorkOrderState {
        SERVICE_ING(1, "未完工"),
        WAITING(2, "排队中"),
        NO_PAY(3, "未结算"),
        COMPLETE(4, "已结算"),
        BOOKING(5, "预约"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "WorkOrder"; // 实体名称
        public String colName = "state"; // 字段名称
        public String colDesc = "工单状态"; // 字段说明

        WorkOrderState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：WorkOrder
     * 字段：payType
     * 说明：付款方式
     */
    public enum WorkOrderPayType {
        CASH(1, "现金"),
        WEB_CHAT(2, "微信"),
        DEBTS(3, "挂账"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "WorkOrder"; // 实体名称
        public String colName = "payType"; // 字段名称
        public String colDesc = "付款方式"; // 字段说明

        WorkOrderPayType(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：Car
     * 字段：carState
     * 说明：车辆状态
     */
    public enum CarState{
        NORMAL(1,"正常"),
        DELETE(0,"删除"),
        ;

        public Integer codeName;// 变量名称
        public String desc;// 描述
        public String tableName="Car";// 实体名称
        public String colName="carState";// 字段名称
        public String colDesc="车辆状态";// 字段说明

        CarState(Integer codeName, String desc){
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：MemberLvl
     * 字段：state
     * 说明：会员等级状态
     */
    public enum MemberLvlState{
        ENABLE(1,"启用"),
        DISABLE(2,"停用"),
        ;

        public Integer codeName;// 变量名称
        public String desc;// 描述
        public String tableName="MemberLvl";// 实体名称
        public String colName="state";// 字段名称
        public String colDesc="会员等级状态";// 字段说明

        MemberLvlState(Integer codeName, String desc){
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：ItemAndMemberLvl
     * 字段：discountType
     * 说明：会员折扣类型
     */
    public enum ItemAndMemberLvlDiscountType{
        VIP_DISCOUNT(1,"会员折扣"),
        VIP_PRICE(2,"会员价"),
        ;
        public Integer codeName;// 变量名称
        public String desc;// 描述
        public String tableName="ItemAndMemberLvl";// 实体名称
        public String colName="discountType";// 字段名称
        public String colDesc="会员折扣类型";// 字段说明

        ItemAndMemberLvlDiscountType(Integer codeName, String desc){
            this.codeName = codeName;
            this.desc = desc;
        }
    }
    /**
     * 实体名称：Business
     * 字段：userType
     * 说明：商家类型
     */
    public enum BusinessUserType{
        MERCHANT("merchant","商家管理员"),
        OPERATOR("operator","操作员"),
        ;

        public String codeName;// 变量名称
        public String desc;// 描述
        public String tableName="Business";// 实体名称
        public String colName="userType";// 字段名称
        public String colDesc="商家类型";// 字段说明

        BusinessUserType(String codeName, String desc){
            this.codeName = codeName;
            this.desc = desc;
        }
    }


    //***********************（end）不需要从数据库获取**************************************************

    /**
     * 系统常用参数设置
     */
    public static class SettingValue {
        /**
         * 用户最多绑定车辆数
         */
        public static String CAR_BIND_MAX_NUM = "carBrindMaxNum";


    }

    /**
     * 联盟商家用户类型
     */
    public static class UserType {
        /**
         * 联盟商家-商家用户
         */
        public static String MERCHANT = "merchant";
        /**
         * 联盟商家-商家用户
         */
        public static String OPERATOR = "operator";
        /**
         * 联盟商家-客户用户
         */
        public static String CUSTOMER = "customer";
        /**
         * 运营用户
         */
        public static String ZSOPERATOR = "zsoperator";
    }

    /**
     * 登陆类型
     */
    public static class LoginType {
        /**
         * 手机号验证码登陆
         */
        public static String CUSTOMER_MOBILE_CODE_LOGIN = "customerCode";
        /**
         * 手机号密码登陆
         */
        public static String CUSTOMER_MOBILE_PASSWORD_LOGIN = "customerPassword";

        /**
         * 微信绑定后自动登陆
         */
        public static String CUSTOMER_WXOPENID_LOGIN = "customerWxopenid";
    }

    /**
     * 套餐类型
     */
    public static class PackageType {
        /**
         * 联盟商家直接出售的套餐
         */
        public static String SALE_PACKAGE = "sale";

        /**
         * 联盟商家自定义会员等级赠送的套餐
         */
        public static String MEMBER_GIVING_PACKAGE = "member";
    }

    /**
     * 套餐类型
     */
    public static  class  ComboState{

    }

    /**
     * 合作套餐套餐销售状态
     */
    public static  class  CouponSellState{
        /**
         * 可买
         */
        public static String COUPON_CAN_SELL = "couponCanSell";

        /**
         * 不可卖
         */
        public static String COUPON_NOT_SELL = "couponNotSell";
    }

    /**
    * 实体名称a
      字段：a
     * 说明：a
     */
    public enum  PackageState{
        SHELVE(1,"上架"),
        UNSHELVE(2,"下架"),
        HIDE(3,"隐藏"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述


        PackageState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 商家服务项状态
     */
    public static class ServiceItemState {
        /**
         * 冗余
         */
        public static String REDUNDANT = "itemredundant";

        /**
         * 正常
         */
        public static String NORMAL = "normal";
    }


    public static class SecuritySetting {
        /**
         * 商家过期时间
         */
        public static String MERCHANT_EXPIRED_TIME = "m.expiredTime";
        /**
         * 用户过期时间
         */
        public static String USER_EXPIRED_TIME = "u.expiredTime";
        /**
         * 商家权限不足跳转页面
         */
        public static String MERCHANT_REDIRECT_URL = "m.rdLoginUrl";

        /**
         * 操作员权限不足跳转页面
         */
        public static String OPERATOR_REDIRECT_URL = "o.rdLoginUrl";

        /**
         * 前端用户权限不足跳转页面
         */
        public static String CUSTOMER_REDIRECT_URL = "c.rdLoginUrl";

        /**
         * 运营端默认登陆页面
         */
        public static String ZSOPERATOR_DEAFULT_URL = "zs.dfUrl";

        /**
         * 运营端权限不足跳转页面
         */
        public static String ZSOPERATOR_REDIRECT_URL = "zs.dLoginUrl";

        /**
         * 商家默认首页
         */
        public static String MERCHANT_DEAFULT_URL = "m.dfUrl";

        /**
         * 操作员默认首页
         */
        public static String OPERATOR_DEAFULT_URL = "o.dfUrl";

        /**
         * 前端用户默认首页
         */
        public static String CUSTOMER_DEAFULT_URL = "c.dfUrl";

    }

    /**
     * 工单预约状态
     */
    public static class BookingState {
        /**
         * 已接收
         */
        public static String ACCEPT = "accept";
        /**
         * 已拒绝
         */
        public static String REFUSE = "refuse";
        /**
         * 待确认
         */
        public static String TO_BE_CONFIRM = "toBeConfirm";
        /**
         * 已撤销
         */
        public static String CANCEL = "cancel";
    }

    /**
     * 用户登陆状态
     */
    public static class UserLoginState {
        /**
         * 登陆
         */
        public static String LOG_IN = "login";
        /**
         * 未登录
         */
        public static String NOT_LOG_IN = "notLogin";
    }

    /**
     * 阿里大于短信平台
     */
    public static class AliyunSms {

        /**
         * 短信签名
         */
        public static String SIGN_NAME = "signName";

        /**
         * key
         */
        public static String APP_KEY = "appKey";
        /**
         * secret
         */
        public static String APP_SECRET = "appSecret";

        /**
         * 验证码短信模板模板
         */
        public static String VERIFICATION_CODE_TEMPLATE = "verificationCodeTemplate";

        //eg:1小时内 可以发送3次短信  每次间隔时长需超过60秒 否则将在1小时后才能重新发送

        /**
         * 单位时间，控制单位时间内发送的短信次数，单位为秒
         */
        public static String UNIT_TIME = "unitTime";

        /**
         * 单位时间内限制发送短信的次数
         */
        public static String LIMIT_TIMES = "limitTimes";

        /**
         * 2条短信发送的间隔时长，单位为秒
         */
        public static String BETWEEN_LONGER = "betweenLonger";

        /**
         * 超过限制后，可以重新发送短信的间隔时长 单位为分钟
         */
        public static String RE_SEND_LONGER = "reSendLonger";

        /**
         * 过期时间
         */
        public static String EXPIRATION_TIME="expirationTime";
    }

    public static class UserManagerState{
        public static String BIND_WECHAT = "bindWeChat";
        public static String UNBIND_WECHAT = "unbindWeChat";
    }



    /**
     * 域名
     */
    public static class DomainName {

        public static String DOMAIN_NAME = "domainName";
    }

    /**
     * 微信支付订单状态
     */
    public static class WeChatPayOrderState {
        /**
         * 订单未确认
         */
        public static String NO_CONFIRM = "noConfirm";

        /**
         * 订单已结算(核销)
         */
        public static String SETTLEMENT = "settlement";
    }


    public static class CheckMoneyOrder {
        /**
         * 每天对账单的最大的金额(单位元)--微信最大金额为2W，故不应该大于2W.
         */
        public static String MAX_MONEY = "maxMoney";

        /**
         * 每天对账单的最小的金额(单位元)--微信最小金额为1元，故最小金额不能低于1元
         */
        public static String MIN_MONEY = "minMoney";

        /**
         * 对账单还未确认
         */
        public static String STATE_NO_CONFIRM = "noConfirmState";

        /**
         *对账单商家已经确认
         */
        public static String STATE_HAVE_CONFIRM = "haveConfirmState";

        /**
         * 对账单商家已收到钱
         */
        public static String STATE_HAVE_PAY = "havePayState";
    }

    /**
     * 收银台配置
     */
    public static class CashierDesk{
        /**
         * 收银台页面
         */
        public static String  CASHIER_DESK_URL_JSAPI = "cashierDeskUrl";

        /**
         * 收银台回调地址
         */
        public static String PAY_ORDER_NOTIFY_URL = "payOrderNotifyUrl";

        /**
         * 抽奖页面
         */
        public static String LOTTERY_URL = "lotteryUrl";

        /**
         * 抽奖商家
         */
        public static String LOTTERY_BUSINESS_IDS = "lotteryBusinessIds";

        /**
         * 分享红包页面
         */
        public static String SHARE_RED_PACKET_URL = "shareRedPacketUrl";

        /**
         * 分享的卡券uuid
         */
        public static String SHARE_RED_PACKET_UUID = "shareRedPacketUuid";

    }

    /**
     * 常规设置
     */
    public static class NormalSetting{
        /**
         * 参团后的关注页
         */
        public static String PACK_CLUSTER_NOTE_URL = "packNoteUrl";

        /**
         * 参团后的详情页
         */
        public static String PACK_CLUSTER_DETAIL_URL = "packDetailUrl";

        /**
         * 商家微信标签，多个标签用,隔开
         */
        public static String BUSINESS_USER_TAGS = "businessUserTags";

        /**
         * 运营微信标签，多个标签用,隔开
         */
        public static String OPERATOR_USER_TAGS = "operatorUserTags";

        /**
         * 更高级的标签，即如果用户的标签中包含了这些，则不重新赋值标签，多个标签用,隔开
         */
        public static String SUPER_USER_TAGS = "superUserTags";
    }

    /**
     * 卡券礼包购买记录状态（无需到数据库获取）
     */
    public static class CouponsPackBuyRecordState{
        /**
         * 未支付
         */
        public static Integer  NOT_PAY = 1;

        /**
         * 已支付
         */
        public static Integer PAID = 2;

        /**
         * 已取消
         */
        public static Integer CANCEL = 3;
    }

    /**
     * 实体名称：无
     * 字段：无
     * 说明：导入数据是否成功标识
     */
    public enum ExportSuccessFlag {
        SUCCESS_FLAG(1, "导入成功"),
        FAIL_FLAG(2, "导入失败"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "无"; // 实体名称
        public String colName = "无"; // 字段名称
        public String colDesc = "导入数据是否成功标识"; // 字段说明

        ExportSuccessFlag(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：ServiceItem
     * 字段：itemType
     * 说明：服务项类型
     */
    public enum ServiceItemType {
        CAR_WASH(1,"洗车"),
        CAR_BEAUTY(2,"美容"),
        CAR_MAINTENANCE(3,"保养"),
        CAR_REPAIR(4,"机修"),
        CAR_SPRAY(5,"钣喷"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "ServiceItem"; // 实体名称
        public String colName = "itemType"; // 字段名称
        public String colDesc = "服务项类型"; // 字段说明

        ServiceItemType(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：BusinessUser
     * 字段：state
     * 说明：商家账号是否离职
     */
    public enum BusinessUserState {
        ON_WORK(1,"在职"),
        HAVE_QUIT(2,"离职"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "BusinessUser"; // 实体名称
        public String colName = "state"; // 字段名称
        public String colDesc = "商家账号是否离职"; // 字段说明

        BusinessUserState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：BusinessUser
     * 字段：bossType
     * 说明：是否老板，即是否是运营人员建的账号
     */
    public enum BusinessUserBossType {
        IS_BOSS(1,"运营人员建的账号"),
        NO_BOSS(2,"商家管理员自己建的账号"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "BusinessUser"; // 实体名称
        public String colName = "bossType"; // 字段名称
        public String colDesc = "是否老板，即是否是运营人员建的账号"; // 字段说明

        BusinessUserBossType(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：WechatPayOrder
     * 字段：orderType
     * 说明：微信支付订单类型
     */
    public enum WeChatPayOrderType {
        WORK_ORDER_TYPE(0,"工单"),
        PACKAGE_ORDER_TYPE(1,"套餐"),
        SETTLEMENT_ORDER_TYPE(2,"核销结算的订单"),
        COUPON_ORDER_TYPE(3,"核销卡券对账单"),
        COMBO_ORDER_TYPE(4,"诚品合作套餐"),
        RECHARGE_ORDER_TYPE(5,"充值"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "WechatPayOrder"; // 实体名称
        public String colName = "orderType"; // 字段名称
        public String colDesc = "订单类型"; // 字段说明

        WeChatPayOrderType(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：
     * 字段：
     * 说明：
     */
    public enum  OrderState{
        TO_BE_PAID(1,"订单未支付"),
        PAID(2,"订单已支付"),
        OVERDUE(3,"订单逾期"),
        NO_DATA(4,"无对应数据！"),
        CANCEL_ORDER(5,"已取消"),
        HAVE_REFUND(6,"已退款"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述

        OrderState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：Business
     * 字段：businessEnable
     * 说明：商家状态
     */
    public enum BusinessState {
        VALID_BUSINESS(1,"有效商家状态"),
        INVALID_BUSINESS(2,"无效商家状态"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "Business"; // 实体名称
        public String colName = "businessEnable"; // 字段名称
        public String colDesc = "商家状态"; // 字段说明

        BusinessState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：Business
     * 字段：businessLineState
     * 说明：商家上下架状态
     */
    public enum  BusinessLineState{
        BUSINESS_ONLINE(1,"上架"),
        BUSINESS_OFFLINE(2,"下架"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "Business"; // 实体名称
        public String colName = "businessLineState"; // 字段名称
        public String colDesc = ""; // 字段说明

        BusinessLineState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：Finance
     * 字段：financeType
     * 说明：财务记录来源
     */
    public enum FinanceType {
        WORK_ORDER_TYPE(1,"工单收入"),
        PACKAGE_ORDER_TYPE(2,"套餐收入"),
        COUPON_ORDER_TYPE(3,"卡券收入"),
        MMGOODCAR_ORDER_TYPE(4,"诚品补贴"),
        OPPOSITE_WORK_ORDER_TYPE(5,"反结算支出"),
        RECHARGE_TYPE(7,"充值收入"),
        OTHERPAY_TYPE(8,"其他支出"),
        BUY_DEMAND(9,"买车线索"),
        SELL_DEMAND(10,"卖车线索"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "Finance"; // 实体名称
        public String colName = "financeType"; // 字段名称
        public String colDesc = "财务记录来源"; // 字段说明

        FinanceType(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：Finance
     * 字段：inOrOutCome
     * 说明：收入 or 支出
     */
    public enum InOrOutCome {
        IN_COME(1,"收入类型"),
        OUT_COME(2,"支出类型"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "Finance"; // 实体名称
        public String colName = "inOrOutCome"; // 字段名称
        public String colDesc = "收入 or 支出"; // 字段说明

        InOrOutCome(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：
     * 字段：
     * 说明：
     */
    public enum Emkt {
        DOMAIN_NAME("emktDomainName", "域名"),
        MA_TOKEN("maToken", "联盟商家专用token"),
        INSERT_COUPON_USE("/api/coupons/insertMaCouponUsed", "营销平台插入优惠券使用记录"),
        SELECT_SINGLE_COUPON("/api/coupons/selectSingleCoupon", "营销平台查询单个可用优惠券"),
        SELECT_COUPONS_USED_BY_UUID("/api/coupons/selectCouponsUsedByUuid", "营销平台根据uuid查询优惠券。。。"),
        UPDATE_COUPONS_USED("/api/coupons/updateCouponsUsed", "营销平台更新优惠券"),
        UPDATE_MACHECKS_COUPON_USED("/api/coupons/updateMaChecksCouponUsed", "回调营销平台进行优惠券核销确认"),
        SELECT_MA_CHECKS("/api/coupons/selectMaChecks", "查询出符合对账单规则的优惠券核销数据"),
        SELECT_COUPONUSED_FOR_MA_CHECK("/api/coupons/selectCouponUsedForMaCheck", "获取卡券信息（核销用）"),
        UPDATE_WXFANS_FOR_VIP("/open/wxfans/updateWxFansForVip", "更新EMKT会员等级"),
        SELECT_BUSINESS_CANUSE_FOR_UUID("/api/coupons/selectBusinessCanUseForUuid", "查询商家是否可用该卡券"),
        SELECT_CHECK_CAN_PAY("/api/packClusterBuyRecord/selectCheckCanPay","验证是否可以支付"),
        UPDATE_PACKCLUSTER_BUY_RECORD_BY_PAY("/api/packClusterBuyRecord/updatePackClusterBuyRecordByPay","支付回调后调用更新拼团");
        ;

        public String codeName; // 变量名称
        public String desc; // 描述

        Emkt(String codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：无
     * 字段：无
     * 说明：crm接口
     */
    public enum Crm {
        INSERT_MABUSINESS("/api/maBusiness/insertMaBusiness","联盟商家设置商家顾问"),
        UPDATE_MABUSINESS("/api/maBusiness/updateMaBusiness","联盟商家更换门店销售顾问"),
        SELECT_MABUSINESS("/api/maBusiness/selectMaBusiness","联盟商家查看门店销售顾问"),
        SELECT_MATCH_DEPARTMENTS("/api/employee/selectMatchDepartments","查询所有销售门店"),
        SELECT_EMPLOYEES("/api/employee/selectEmployees","根据条件查询员工"),
        SELECT_PAGE_BUY_DEMAND("/api/buyDemand/selectPageBuyDemand","查询买车需求"),
        SELECT_PAGE_SELL_DEMAND("/api/sellDemand/selectPageSellDemand","查询卖车需求"),
        ;

        public String codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "无"; // 实体名称
        public String colName = "无"; // 字段名称
        public String colDesc = "crm接口"; // 字段说明

        Crm(String codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：ServiceItem
     * 字段：topState
     * 说明：是否置顶
     */
    public enum ServiceItemTopState {
        TOP(1,"置顶"),
        NO_TOP(0,"不置顶"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "ServiceItem"; // 实体名称
        public String colName = "topState"; // 字段名称
        public String colDesc = "是否置顶"; // 字段说明

        ServiceItemTopState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：LotteryAward
     * 字段：state
     * 说明：是否已经领奖
     */
    public enum LotteryAwardState {
        NO_Receive(1,"未领取"),
        HAVE_Receive(2,"已经领取"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "LotterAward"; // 实体名称
        public String colName = "state"; // 字段名称
        public String colDesc = "是否已经领奖"; // 字段说明

        LotteryAwardState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：PackClusterOpenRecord
     * 字段：state
     * 说明：开团状态
     */
    public enum PackClusterOpenRecordState {
        ING(1,"开团中"),
        SUCCESS(2,"开团成功"),
        FAIL(3,"开团失败"),
        ;

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "PackClusterOpenRecord"; // 实体名称
        public String colName = "state"; // 字段名称
        public String colDesc = "开团状态"; // 字段说明

        PackClusterOpenRecordState(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }

    /**
     * 实体名称：
     * 字段：
     * 说明：
     */
    public enum  canEdit{
        CANEDIT(1,"可以删除和编辑"),
        CANTEDIT(2,"不能修改和删除");

        public Integer codeName; // 变量名称
        public String desc; // 描述
        public String tableName = "PackCluster"; // 实体名称
        public String colName = "canEdit"; // 字段名称
        public String colDesc = "是否可以修改"; // 字段说明

        canEdit(Integer codeName, String desc) {
            this.codeName = codeName;
            this.desc = desc;
        }
    }
    //=============================基础变量开始======================================

    /**
     * key为枚举类名，value是一个字符串数组列表:[值，描述]
     */
    public static Map<String, List<String[]>> enums = initEnumMap();

    /**
     * key（为枚举类名，codeName），value是描述字符串desc
     */
    public static Map<String, String> enumValues;

    private static Map<String, SystemCode> codes;
    private static long lastFreshTimestamp = 0L;

    private static List<AreaCode> areaCodes;

    //=============================初始化开始======================================
    /**
     * 初始化枚举字段
     *
     * @return
     */
    private static Map<String, List<String[]>> initEnumMap() {
        Map<String, List<String[]>> enumsTmp = new HashMap();
        try {
            enumValues = new HashMap();
            Class<?> cls = Class.forName("com.software5000.base.Constant");
            for (Class sEnum : cls.getDeclaredClasses()) {
                if (sEnum.isEnum()) {
                    List<String[]> enumOptions = new ArrayList<>();
                    for (Object e : sEnum.getEnumConstants()) {
                        enumOptions.add(new String[]{e.getClass().getDeclaredField("codeName").get(e).toString(), e.getClass().getDeclaredField("desc").get(e).toString()});
                        enumValues.put(sEnum.getSimpleName() + "," + e.getClass().getDeclaredField("codeName").get(e).toString(), e.getClass().getDeclaredField("desc").get(e).toString());
                    }
                    enumsTmp.put(sEnum.getSimpleName(), enumOptions);
                }
            }
        } catch (Exception e) {
            log.error(" init Enum map error! ", e);
        }
        return enumsTmp;
    }

    /**
     * 初始化SystemCode和客户来源
     *
     * @param codes
     */
    public static void initCodes(Map codes) {
        Constant.codes = codes;
        lastFreshTimestamp = new Date().getTime();
    }

    /**
     * 初始化地区
     *
     * @param areCodes
     */
    public static void initAreaCode(List areCodes) {
        Constant.areaCodes = areCodes;
        lastFreshTimestamp = new Date().getTime();
    }

    //=============================初始化结束======================================

    public static List<AreaCode> getAreaCodesByParentId(Integer parentId) {
        return areaCodes.stream().filter(areaCode -> areaCode.getParentId() != null && areaCode.getParentId().equals(parentId)).collect(Collectors.toList());
    }

    public static AreaCode getAreaCodesById(Integer id) {
        if(id == null) return null;
        return areaCodes.get(areaCodes.indexOf(new AreaCode(id)));
    }

    public static AreaCode getAreaObjById(Object areaId) {
        if (ValidUtil.isEmpty(areaId) || (!ValidUtil.isNumber(areaId.toString()))) {
            return null;
        }
        for (AreaCode areaCode : areaCodes) {
            if (areaCode.getId().equals(Integer.valueOf(areaId.toString()))) {
                return areaCode;
            }
        }
        return null;
    }

    public static String getCodeName(String codeType, Object codeValue) {
        Collection codesByType = getCodesByType(codeType);
        String value = null;
        Iterator var4 = codesByType.iterator();

        while(var4.hasNext()) {
            SystemCode systemCode = (SystemCode)var4.next();
            if(systemCode.getCodeValue().equals(codeValue.toString())) {
                value = systemCode.getCodeName();
                break;
            }
        }

        return value;
    }

    public static String getCodeShowName(String codeType, Object codeValue) {
        if(ValidUtil.isEmpty(codeValue)) {
            return null;
        } else {
            Collection codesByType = getCodesByType(codeType);
            String value = null;
            Iterator var4 = codesByType.iterator();

            while(var4.hasNext()) {
                SystemCode systemCode = (SystemCode)var4.next();
                if(systemCode.getCodeValue().equals(codeValue.toString())) {
                    value = systemCode.getCodeShowName();
                    break;
                }
            }

            return value;
        }
    }
    public static Collection<SystemCode> getCodesByType(String codeType) {
        List codeByType = new ArrayList<>();
        for (SystemCode c :
                Constant.getAllCodes()) {
            if (codeType.equals(c.getCodeType())) {
                codeByType.add(c);
            }
        }
        return codeByType;
    }

    public static Collection<SystemCode> getAllCodes() {
        return codes.values();
    }

    public static long refreshTime() {
        return lastFreshTimestamp;
    }

    public static SystemCode getCodeByName(String codeName) {
        return codes.get(codeName);
    }

    public static String getStringCodeValueByName(String codeName){
        SystemCode codeByName = getCodeByName(codeName);
        if(codeByName==null || codeByName.getCodeValue()==null)
            return null;
        return codeByName.getCodeValue().toString();
    }

    public static Integer getIntegerCodeValueByName(String codeName){
        SystemCode codeByName = getCodeByName(codeName);
        if(codeByName==null || codeByName.getCodeValue()==null)
            return null;
        return Integer.parseInt(codeByName.getCodeValue().toString());
    }
}
