/**
 * Created by JANZ on 2017/3/21.
 */
const ROOT = '';
const joggle = {
    open: {
        selectWxJsAPI: ROOT + '/open/mzscp/selectWxJsAPI'//获取使用wxsdk需要的参数
    },
    mzscp: {
        selectConstantTypes: ROOT + '/open/mzscp/selectConstantTypes', //下拉筛选数据
        uploadImgForBase64: ROOT + '/home/mzscp/insertImgForBase64', //上传base64图片
        selectProvince: ROOT + '/mzscp/selectProvinces', //获取省份
        selectAreaCode: ROOT + '/mzscp/selectAreaCode', //获取城市或地区
        selectWxCode: ROOT + '/open/wx/weChatRedirect', //获取微信code
        selectWxOpenId: ROOT + '/open/wx/selectWxopenId', //获取微信openId
        selectUserByCarNumber: ROOT + '/open/wx/selectUserByCarNumber', //根据车牌号查用户
        updateUser: ROOT + '/open/wx/updateUser' //更新用户信息
    },
    business: {
        selectBusinessUser: ROOT + '/home/business/selectBusinessUser', //获取登陆用户信息
        selectBusinessListPage: ROOT + '/home/business/selectBusinessListPage', //查看所有联盟商家分页信息
        selectBusinessInfo: ROOT + '/home/business/selectBusinessInfo', //根据商家ID获取商家信息
        selectMemberLvlList: ROOT + '/home/business/selectMemberLvlList', //获取商家会员等级列表
        updateBusiness: ROOT + '/home/business/updateBusiness', //更新商家信息
        insertBusiness: ROOT + '/home/business/insertBusiness', //插入商家信息
        insertBusinessUser: ROOT + '/home/business/insertBusinessUser', //插入商家用户
        updateBusinessUser: ROOT + '/home/business/updateBusinessUser', //修改商家用户
        updateMemberLvl: ROOT + '/home/business/updateMemberLvl', //	更新商家会员等级
        insertMemberLvl: ROOT + '/home/business/insertMemberLvl', //	新增商家会员等级
        updateUserOpenId: ROOT + '/home/business/updateUserOpenId' //更新商家用户的openid,如果是boss则将微信号更新到商家上

    },
    merchant: {
        open: {
            login: ROOT + '/merchant/login', //登录接口
            logout: ROOT + '/logout' //退出接口
        },
        index: {
            // workbench: ROOT + "/home/finance/selectWorkbench", //商家登陆首页查询
        },
        workorder: {
            imgRecUrl: ROOT + '/open/workOrder/selectScanPlateNumber', //识别车牌图片
            selectWorkOrderStateCount: ROOT + '/home/workOrder/selectWorkOrderStateCount', //查询各个工单状态的数量
            selectWorkOrderByCarNumber: ROOT + '/home/workOrder/selectWorkOrderByCarNumber', //根据车牌获取工单信息
            selectWorkOrderById: ROOT + '/home/workOrder/selectWorkOrderById', //根据工单id获取工单信息
            selectWorkOrderByPage: ROOT + '/home/workOrder/selectWorkOrderPageForWap', //分页查询工单列表 {state : 1}
            selectServiceItemByPage: ROOT + '/home/serviceItem/selectPageServiceItemForWorkOrder', //分页获取商家服务项
            selectServiceItem: ROOT + '/home/serviceItem/selectServiceItemList', //不分页查询商家服务项
            updateWorkOrder: ROOT + '/home/workOrder/updateWorkOrder', //保存工单
            updateWorkOrderForSettle: ROOT + '/home/workOrder/updateWorkOrderForSettle', //结算工单
            updateWorkOrderForFinish: ROOT + '/home/workOrder/updateWorkOrderForFinish', //施工完成
            deleteWorkOrderById: ROOT + '/home/workOrder/deleteWorkOrderById', //根据id删除工单
            // deleteItemById: ROOT + "/home/workOrder/deleteWorkOrderDetailById", //根据id删除项目
            // antiSettle: ROOT + "/home/workOrder/updateWorkOrderForAntiSettle", //反结算
            selectCouponsByCarNumber: ROOT + '/home/workOrder/selectCouponByCarNumber', //通过车牌获取优惠券
            selectCouponsById: ROOT + '/home/workOrder/selectCouponUsedByUuid', //查询优惠券是否有效
            weChatPayBarCode: ROOT + '/home/workOrder/selectWorkOrderDeskPay', //微信支付二维码
            // downLoadWorkOrder : ROOT + "/home/workOrder/selectWorkOrderForExcel", //导出工单列表
            upLoadWorkOrderImg: ROOT + '/home/workOrder/insertWorkOrderImg', //上传工单图片
            insertWorkOrderImg: ROOT + '/home/workOrder/insertWorkOrderImg', //添加工单图片
            deleteWorkOrderImg: ROOT + '/home/workOrder/deleteWorkOrderImgById', //删除工单图片
            selectMemberByCarNumber: ROOT + '/home/workOrder/selectMerchantMemberPage' //根据车牌模糊搜索会员(分页)
        },
        employee: {
            // insert: ROOT + "/home/business/insertBusinessUser", //新增员工
            // selectByPage: ROOT + "/home/business/selectPageOperatorBusinessUser", //分页查询员工信息
            // update: ROOT + "/home/business/updateBusinessUser", //跟新员工
        },
        member: {
            selectMemberInfo: ROOT + '/home/member/selectMemberInfo', //分页查询商家会员信息
            selectMemberDetailInfo: ROOT + '/home/member/selectMemberDetailInfo', //根据会员ID查询会员信息
            selectMemberInfoByMobile: ROOT + '/home/member/selectMemberInfoByMobile', //会员详情页面—查询手机号是否已存在
            selectMemberBusinessPackage: ROOT + '/home/member/selectMemberBusinessPackage', //查询用户购买套餐卡
            updateMemberInfo: ROOT + '/home/member/updateMemberInfo', //会员详情页面—修改会员信息
            insertCarNumberByParam: ROOT + '/home/member/insertCarNumberByParam', //会员详情页面—根据用户id添加用户车辆信息
            updateCarStateByParam: ROOT + '/home/member/updateCarStateByParam', //会员详情页面—车辆假删除，修改用户车辆状态

            selectCarNumber: ROOT + '/home/member/selectCarNumber', //查询车辆是否绑定
            selectBusinessUserByParam: ROOT + '/home/member/selectBusinessUserByParam', //根据条件模糊搜索会员(分页)

            insertRechargeOrder: ROOT + '/home/member/insertRechargeOrder', // 充值
            updateRechargeOrderState: ROOT + '/home/member/updateRechargeOrderState', //充值现金支付
            weChatPayBarCode: ROOT + '/home/member/selectRechargeOrderDeskPay',//充值微信支付二维码
            selectRechargeOrderPage: ROOT + '/home/member/selectRechargeOrderPage', //列表查询
            selectRechargeOrderById: ROOT + '/home/member/selectRechargeOrderById' //详情查询
        },
        businessPackage: {
            insertBusinessPackage: ROOT + '/home/businessPackage/insertBusinessPackage', //新增套餐卡
            updateBusinessPackage: ROOT + '/home/businessPackage/updateBusinessPackage', //修改套餐卡
            deleteBusinessPackage: ROOT + '/home/businessPackage/deleteBusinessPackage', //删除套餐卡
            selectBusinessPackageByPage: ROOT + '/home/businessPackage/selectBusinessPackageByPage', //查询套餐卡分页
            updateBusinessPackageState: ROOT + '/home/businessPackage/updateBusinessPackageState', //修改套餐卡装填
            selectBusinessPackageById: ROOT + '/home/businessPackage/selectBusinessPackageById', //	查询套餐卡详情

            // selectCooperComboPage: ROOT + "/home/businessPackage/selectCooperComboPage", //诚品合作套餐列表
            //
            //
            selectBusinessPackageOrderById: ROOT + '/home/businessPackage/selectBusinessPackageOrderById', //套餐卡购买详情
            // selectPaidBusinessPackageOrder: ROOT + "/home/businessPackage/selectPaidBusinessPackageOrder", //待支付套餐卡列表
            // selectBusinessPackageList: ROOT + "/home/businessPackage/selectBusinessPackageList", //商家名下上架状态的套餐卡
            //
            insertBusinessPackageOrder: ROOT + '/home/businessPackage/insertBusinessPackageOrder', //购买套餐卡
            //
            // selectNoPaidBusinessPackageOrder: ROOT + "/home/businessPackage/selectNoPaidBusinessPackageOrder", //待支付套餐卡列表
            updateBusinessPackageOrderCancle: ROOT + '/home/businessPackage/updateBusinessPackageOrderCancle', //取消订单
            updateBusinessPackageOrderState: ROOT + '/home/businessPackage/updateBusinessPackageOrderState', //现金收款
            selectPackageOrderDeskPay: ROOT + '/home/businessPackage/selectPackageOrderDeskPay', //微信收款

            insertMemberLvl: ROOT + '/home/business/insertMemberLvl', //新增商家会员等
            updateMemberLvl: ROOT + '/home/business/updateMemberLvl', //更新商家会员等级

            selectNoPaidBusinessPackageOrder: ROOT + '/home/businessPackage/selectNoPaidBusinessPackageOrder', //待支付套餐卡列表
            selectPaidBusinessPackageOrder: ROOT + '/home/businessPackage/selectPaidBusinessPackageOrder', //套餐卡购买记录
            selectCountByState: ROOT + '/home/businessPackage/selectCountByState' //获取列表数量
        },
        finance: {
            selectFinanceInOrOutComeDto: ROOT + '/home/finance/selectFinanceInOrOutComeDto', //查询总收入与总支出
            selectPageFinance: ROOT + '/home/finance/selectPageFinance', //分页查询财务明细
            selectBusinessCheckMoney: ROOT + '/home/finance/selectBusinessCheckMoney', //查询可提现金额及已提现金额
            selectPageWechatPayOrder: ROOT + '/home/finance/selectPageWechatPayOrder', //分页查询微信提现明细
            selectCheckMoneyDto: ROOT + '/home/finance/selectCheckMoneyDto', //查询可提现的基本信息
            insertDrawMoney: ROOT + '/home/finance/insertDrawMoney', //商家提现
            InsertFinaceOtherPay: ROOT + '/home/finace/InsertFinaceOtherPay' //记支出
        },
        setting: {
            selectPageServiceItem: ROOT + '/home/serviceItem/selectPageServiceItem', //分页查询商家的服务项
            selectWhetherServiceItemEdit: ROOT + '/home/serviceItem/selectWhetherServiceItemEdit', //验证当前服务项是否可以删除or修改，如果有被购买，正在使用的无法删除，true可以删除，false不可以删除
            deleteServiceItem: ROOT + '/home/serviceItem/deleteServiceItem', //删除服务项
            insertServiceItem: ROOT + '/home/serviceItem/insertServiceItem', //	新增服务项
            updateServiceItem: ROOT + '/home/serviceItem/updateServiceItem', //更新服务项
            getMobileAndFrameNumByCarNum: ROOT + '/merchant/workOrder/opt/getMobileAndFrameImg' //根据车牌查车架号和用户手机
            // userInfoTemplate: ROOT + "/home/excelData/selectUserInfoTemplate", //下载会员信息导入模板
            // serviceItemTemplate: ROOT + "/home/excelData/selectServiceItemTemplate", //下载服务项导入模板
            // importUserInfo: ROOT + "/home/excelData/insertUserInfoByUpload", //导入会员信息
            // importServiceItem: ROOT + "/home/excelData/insertServiceItemByUpload", //导入服务项
            // userInfoResult: ROOT + "/home/excelData/selectUserInfoImportResult", //下载导入的会员信息的结果
            // serviceItemResult: ROOT + "/home/excelData/selectServiceItemImportResult", //下载导入的服务项的结果
        }
    },
    customer: {
        business: {
            selectBusinessByPage: ROOT + '/open/business/selectBusinessPageForOpen', //获取商家列表
            selectBusiness: ROOT + '/open/business/selectBusinessForOpen', //获取商家信息
            selectBusinessItem: ROOT + '/open/serviceItem/selectServiceItemForOpen', //获取商家项目
            selectBusinessPackage: ROOT + '/open/businessPackage/selectBusinessPackageForOpen' //获取商家套餐
        },
        package: {
            selectUserPackageRecord: ROOT + '/open/businessPackage/selectUserMemberPackageRecord', //获取用户套餐卡
            selectUserPackageCount: ROOT + '/open/businessPackage/selectMemberPackageRecordCount' //获取用户套餐卡统计
        },
        binding: {
            insertUser: ROOT + '/open/user/insertUserAndCar' //商家端 新增用户并绑定车牌
        },
        cashier: {
            unifiedOrder: ROOT + '/open/bank/unifiedOrder', //生成预支付订单
            selectPayOrder: ROOT + '/open/bank/selectPayOrder', //根据id查询收银台信息
            checkPay: ROOT + '/open/bank/checkPay' //检查订单是否有效
        },
        consume: {
            selectConsumeListByOpenId: ROOT + '/open/workOrder/selectPageByState', //根据openId获取消费列表
            selectConsumeDetailById: ROOT + '/open/workOrder/selectUserWorkOrderByWorkOrderId' //根据id获取消费详情
        },
        lottery: {
            selectLotteryTimes: ROOT + '/open/lottery/selectTimes', //获取抽奖次数
            selectPrize: ROOT + '/open/lottery/selectPrize', //抽奖
            CouponinsertUsed: ROOT + '/open/lottery/insertMaCouponUsed', //领取抽中的卡券
            selectPayOrderByOrderNo: ROOT + '/open/bank/selectSinglePayOrderByOrderNo' //根据orderNo获取payOrder
        },
        car: {
            insertUserCar: '/open/user/insertUserCar', //新增用户车辆
            deleteUserCar: '/open/user/deleteUserCar', //删除用户车辆
            selectCarListByOpenId: ROOT + '/open/user/selectCarListByOpenId' //通过openId查询车辆信息

        },
        packcluster: {
            selectHaveBuyPackCluster: ROOT + '/open/packClusterBuyRecord/selectHaveBuyPackCluster', //查询是否有参团记录信息
            selectPackClusterInfo: ROOT + '/open/packCluster/selectPackClusterInfo', //查询团活动信息
            selectPackClusterPeron: ROOT + '/open/packClusterBuyRecord/selectPackClusterPeron', //根据购买吃参团id查询参团的活动信息
            insertPackClusterBuyRecordByPay: ROOT + '/open/packClusterBuyRecord/insertPackClusterBuyRecordByPay', //支付购买拼团增加参团记录信息
            selectPagePackClusterBuyRecordByOpenId: ROOT + '/open/packCluster/selectPagePackClusterBuyRecordByOpenId', //分页查询用户所有参与的拼团
            selectPackClusterBuyRecordDetail: ROOT + '/open/packCluster/selectPackClusterBuyRecordDetail', //查询用户参与的拼团详情
            selectPackClusterBuyRecordCount: ROOT + '/open/packClusterBuyRecord/selectPackClusterBuyRecordCount' //分页查询用户各拼团状态数量

        },
        paySuccess: {
            getWxConfig: ROOT + '/open/mzscp/selectWxJsAPI', //获取使用wxsdk需要的参数
            selectCouponsByCpUuid: ROOT + '/open/coupons/selectCouponsByCpUuid' //
        }
    },
    operator: {
        workorder: {
            selectPagePaymentRateDto: ROOT + '/home/workOrder/selectPagePaymentRateDto', // 分页查询商家的开单数跟支付数
            selectWorkOrderPaymentRate: ROOT + '/home/workOrder/selectWorkOrderPaymentRate' // 查询商家总的开单数和支付数
        },
        finance: {
            selectPaymentPage: ROOT + '/home/finance/selectPaymentPage', //分页收支数据
            selectTotalSum: ROOT + '/home/finance/selectTotalSum', //统计收支数据
            selecPaymentBusiness: ROOT + '/home/finance/selecPaymentBusiness' //查询有消费的商家
        },
        open: {
            login: ROOT + '/operator/login', //登录接口
            logout: ROOT + '/logout' //退出接口
        }
    }
};

export default joggle;
