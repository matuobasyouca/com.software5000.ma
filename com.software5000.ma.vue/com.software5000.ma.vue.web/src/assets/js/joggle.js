/**
 * Created by JANZ on 2017/3/21.
 */
const ROOT = '';
const joggle = {
    mzscp: {
        selectConstantTypes: ROOT + '/open/mzscp/selectConstantTypes', //下拉筛选数据
        createBarCode: ROOT + '/open/coupons/selectQr?type=&url=', //GET方式生成二维码，后面接跳转地址的字符串
        uploadImgForBase64: ROOT + '/home/mzscp/insertImgForBase64' //上传base64图片
    },
    merchant: {
        open: {
            login: ROOT + '/merchant/login', //登录接口
            logout: ROOT + '/logout' //退出接口
        },
        index: {
            workbench: ROOT + '/home/finance/selectWorkbench' //商家登陆首页查询
        },
        workorder: {
            selectBusinessInfo: ROOT + '/home/business/selectBusinessInfo', //查询商家信息
            updateDefaultCar: ROOT + '/home/business/updateBusinessDefaultCar', //更新默认车牌
            selectMemberByCarNumber: ROOT + '/home/workOrder/selectMerchantMemberPage', //根据车牌模糊搜索会员(分页)
            selectWorkOrderByCarNumber: ROOT + '/home/workOrder/selectWorkOrderByCarNumber', //根据车牌获取工单信息
            selectWorkOrderById: ROOT + '/home/workOrder/selectWorkOrderById', //根据工单id获取工单信息
            selectWorkOrderByPage: ROOT + '/home/workOrder/selectWorkOrderPage', //根据条件分页查询工单列表
            selectServiceItem: ROOT + '/home/serviceItem/selectPageServiceItem', //获取商家服务项
            updateWorkOrder: ROOT + '/home/workOrder/updateWorkOrder', //保存工单
            updateWorkOrderForSettle: ROOT + '/home/workOrder/updateWorkOrderForSettle', //结算工单
            updateWorkOrderForFinish: ROOT + '/home/workOrder/updateWorkOrderForFinish', //施工完成
            deleteWorkOrderById: ROOT + '/home/workOrder/deleteWorkOrderById', //根据id删除工单
            deleteItemById: ROOT + '/home/workOrder/deleteWorkOrderDetailById', //根据id删除项目
            antiSettle: ROOT + '/home/workOrder/updateWorkOrderForAntiSettle', //反结算
            selectCouponsByCarNumber: ROOT + '/home/workOrder/selectCouponByCarNumber', //通过车牌获取优惠券
            selectCouponsById: ROOT + '/home/workOrder/selectCouponUsedByUuid', //查询优惠券是否有效
            weChatPayBarCode: ROOT + '/home/workOrder/selectWorkOrderDeskPay', //微信支付二维码
            downLoadWorkOrder: ROOT + '/home/workOrder/selectWorkOrderForExcel'//导出工单列表
        },
        employee: {
            insert: ROOT + '/home/business/insertBusinessUser', //新增员工
            selectByPage: ROOT + '/home/business/selectPageOperatorBusinessUser', //分页查询员工信息
            update: ROOT + '/home/business/updateBusinessUser' //跟新员工
        },
        member: {
            selectByPage: ROOT + '/home/member/selectMemberInfo', //分页查询会员信息
            selectMemberDetailInfo: ROOT + '/home/member/selectMemberDetailInfo', //根据会员ID查询会员信息
            selectMemberInfoByMobile: ROOT + '/home/member/selectMemberInfoByMobile', //会员详情页面—查询手机号是否已存在
            selectCarNumber: ROOT + '/home/member/selectCarNumber', //查询车辆是否绑定

            updateMemberInfo: ROOT + '/home/member/updateMemberInfo', //会员详情页面—修改会员信息
            insertCarNumberByParam: ROOT + '/home/member/insertCarNumberByParam', //会员详情页面—根据用户id添加用户车辆信息
            updateCarStateByParam: ROOT + '/home/member/updateCarStateByParam', //会员详情页面—车辆假删除，修改用户车辆状态

            selectMemberBusinessPackage: ROOT + '/home/member/selectMemberBusinessPackage',//查询用户购买套餐卡
            selectBusinessUserByParam: ROOT + '/home/member/selectBusinessUserByParam'//查询用户信息
        },
        businessPackage: {
            insertBusinessPackage: ROOT + '/home/businessPackage/insertBusinessPackage', //新增套餐卡
            updateBusinessPackage: ROOT + '/home/businessPackage/updateBusinessPackage', //修改套餐卡
            deleteBusinessPackage: ROOT + '/home/businessPackage/deleteBusinessPackage', //删除套餐卡
            selectBusinessPackageById: ROOT + '/home/businessPackage/selectBusinessPackageById', //查询套餐卡详情

            selectBusinessPackageByPage: ROOT + '/home/businessPackage/selectBusinessPackageByPage', //查询套餐卡分页
            selectCooperComboPage: ROOT + '/home/businessPackage/selectCooperComboPage', //诚品合作套餐列表

            selectBusinessPackageOrderById: ROOT + '/home/businessPackage/selectBusinessPackageOrderById', //套餐卡购买详情
            selectPaidBusinessPackageOrder: ROOT + '/home/businessPackage/selectPaidBusinessPackageOrder', //待支付套餐卡列表

            updateBusinessPackageState: ROOT + '/home/businessPackage/updateBusinessPackageState', //修改套餐卡装填
            selectBusinessPackageList: ROOT + '/home/businessPackage/selectBusinessPackageList', //商家名下上架状态的套餐卡

            insertBusinessPackageOrder: ROOT + '/home/businessPackage/insertBusinessPackageOrder', //购买套餐卡

            selectNoPaidBusinessPackageOrder: ROOT + '/home/businessPackage/selectNoPaidBusinessPackageOrder', //待支付套餐卡列表
            updateBusinessPackageOrderCancle: ROOT + '/home/businessPackage/updateBusinessPackageOrderCancle', //取消订单
            updateBusinessPackageOrderState: ROOT + '/home/businessPackage/updateBusinessPackageOrderState', //现金收款
            selectPackageOrderDeskPay: ROOT + '/home/businessPackage/selectPackageOrderDeskPay', //微信收款

            insertMemberLvl: ROOT + '/home/business/insertMemberLvl', //新增商家会员等
            updateMemberLvl: ROOT + '/home/business/updateMemberLvl' //更新商家会员等级

        },
        finance: {
            selectFinanceInOrOutComeDto: ROOT + '/home/finance/selectFinanceInOrOutComeDto', //查询总收入与总支出
            selectPageFinance: ROOT + '/home/finance/selectPageFinance', //分页查询财务明细
            selectBusinessCheckMoney: ROOT + '/home/finance/selectBusinessCheckMoney', //查询可提现金额及已提现金额
            selectPageWechatPayOrder: ROOT + '/home/finance/selectPageWechatPayOrder', //分页查询微信提现明细
            selectCheckMoneyDto: ROOT + '/home/finance/selectCheckMoneyDto', //查询可提现的基本信息
            insertDrawMoney: ROOT + '/home/finance/insertDrawMoney'//商家提现
        },
        setting: {
            selectPageServiceItem: ROOT + '/home/serviceItem/selectPageServiceItem', //分页查询商家的服务项
            selectWhetherServiceItemEdit: ROOT + '/home/serviceItem/selectWhetherServiceItemEdit', //验证当前服务项是否可以删除or修改，如果有被购买，正在使用的无法删除，true可以删除，false不可以删除
            deleteServiceItem: ROOT + '/home/serviceItem/deleteServiceItem', //删除服务项
            insertServiceItem: ROOT + '/home/serviceItem/insertServiceItem', //新增服务项
            updateServiceItem: ROOT + '/home/serviceItem/updateServiceItem', //更新服务项
            updateBusiness: ROOT + '/home/business/updateBusiness', //设置商家关账日
            userInfoTemplate: ROOT + '/home/excelData/selectUserInfoTemplate', //下载会员信息导入模板
            serviceItemTemplate: ROOT + '/home/excelData/selectServiceItemTemplate', //下载服务项导入模板
            importUserInfo: ROOT + '/home/excelData/insertUserInfoByUpload', //导入会员信息
            importServiceItem: ROOT + '/home/excelData/insertServiceItemByUpload', //导入服务项
            userInfoResult: ROOT + '/home/excelData/selectUserInfoImportResult', //下载导入的会员信息的结果
            serviceItemResult: ROOT + '/home/excelData/selectServiceItemImportResult', //下载导入的服务项的结果

            selectMemberLvlList: ROOT + '/home/business/selectMemberLvlList'//获取商家会员等级列表

        }

    },
    customer: {},
    operator: {
        selectBusinessListPage: ROOT + '/home/business/selectBusinessListPage', //分页查询所有的商家
        open: {
            login: ROOT + '/operator/login', //登录接口
            logout: ROOT + '/logout'//退出接口
        },
        activity: {
            insertPackCluster: ROOT + '/home/packCluster/insertPackCluster', //新建拼团活动
            updatePackCluster: ROOT + '/home/packCluster/updatePackCluster', //修改拼团活动
            deletePackCluster: ROOT + '/home/packCluster/deletePackCluster', //删除拼团活动
            selectPackClusterInfoById: ROOT + '/home/packCluster/selectPackClusterInfoById', //查询活动详情
            updatePackClusterState: ROOT + '/home/packCluster/updatePackClusterState', //拼团活动上下架
            selectPackClusterByPage: ROOT + '/home/packCluster/selectPackClusterByPage', //拼团活动列表
            selectPackClusterOpenNum: ROOT + '/home/packCluster/selectPackClusterOpenNum', //获取拼团数量情况
            selectPagePackClusterBuyRecordByPage: ROOT + '/open/packCluster/selectPagePackClusterBuyRecordByPage' //获取购买记录分页
        }
    }

};

export default joggle;
