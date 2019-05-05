<template>
    <div class="page-main">
        <div class="page-main-body">
            <ul class="user-detail">
                <li><span>工单编号：</span>{{ orderNo }}</li>
                <li><span>车牌：</span>{{ carNumber }}</li>
                <li><span>姓名：</span>{{ realName }}</li>
                <li><span>手机号：</span>{{ mobile }}</li>
                <li><span>会员等级：</span>{{ memberLvl }}</li>
                <li><span>备注：</span>{{ comment }}</li>
            </ul>
            <zs-table
                    class="item-table"
                    :data="itemDatas"
                    :columns="itemColumns"
                    :context="context"
                    :no-data-colspan="9"
                    border></zs-table>
            <ul class="account-detail">
                <li>
                    <div class="account-detail-item">
                        <span class="account-1">项目费用</span>
                        <span class="account-2">{{ itemTotalPrice }}</span>
                    </div>
                </li>
                <li>
                    <div class="account-detail-item">
                        <span class="account-1">附加费用</span>
                        <div class="account-2">
                            <zs-input
                                    type="number"
                                    :auto-select="finalData.materialCost == 0"
                                    v-model="finalData.materialCost"></zs-input>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="account-detail-item">
                        <span class="account-1">商家扣减</span>
                        <div class="account-2">
                            -
                            <zs-input
                                    type="number"
                                    :auto-select="finalData.businessDeduct == 0"
                                    v-model="finalData.businessDeduct"></zs-input>
                        </div>
                    </div>
                </li>
                <li v-if="memberBalance > 0" class="member-balance">
                    <div class="account-detail-item">
                        <span class="account-1"></span>
                        <zs-checkbox
                                v-model="memberBalanceDeduct"
                                :true-label="memberBalance"
                                :false-label="0">会员余额支付 <i>(余额:{{ parseFloat(memberBalance).toFixed(2) }})</i>
                        </zs-checkbox>
                        <span class="account-2">- {{ Math.min(parseFloat(memberBalanceDeduct), parseFloat(totalPriceBeforeBalance)).toFixed(2) }}</span>
                    </div>
                </li>
            </ul>
            <ul class="account-detail">
                <li class="account-sum">
                    <div class="account-detail-item">
                        <span class="account-2">实际应收：<i>￥{{ totalPrice }}</i></span>
                    </div>
                </li>
            </ul>
        </div>
        <div class="page-main-footer">
            <zs-button :class="[{'is-disabled' : !canSubmit}]" type="primary" @click="handlePayCashPop">
                <zs-icon icon="cash" size="24" text="现金收款"></zs-icon>
            </zs-button>
            <zs-button :class="[{'is-disabled' : !canSubmit || parseFloat(totalPrice) === 0}]" type="success"
                       @click="handlePayWechatPop">
                <zs-icon icon="wechat" size="24" text="微信收款"></zs-icon>
            </zs-button>
        </div>
        <!--现金收款-->
        <zs-dialog class="settle-pop" v-model="payCashPopVisible">
            <p slot="title">现金收款</p>
            <p class="pay-pop-p1">￥{{ totalPrice }}</p>
            <p class="pay-pop-p2">请收到现金后点击"确认收款"!</p>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="payCashPopVisible = false">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="updateWorkOrderForSettle">确定收款
                </zs-button>
            </template>
        </zs-dialog>
        <!--微信收款-->
        <zs-dialog class="settle-pop wechat-pay-pop" v-model="payWechatPopVisible">
            <p slot="title">微信收款</p>
            <p class="pay-pop-p1">￥{{ totalPrice }}</p>
            <p class="pay-pop-barcode" ref="barCodeWrap">
                <img class="pay-pop-loading" src="../../../../../../../assets/img/three-dots.svg">
            </p>
        </zs-dialog>
        <!--选择优惠券-->
        <zs-dialog class="select-pop coupon-select-pop" v-model="couponSelectVisible">
            <p slot="title">选择优惠券</p>
            <ul class="employee-select-list">
                <li class="employee-select-hd"><span>选择</span><span>优惠券名称</span><span>优惠券金额</span></li>
                <template v-if="couponsItemData.length > 0">
                    <li
                            v-for="(c,index) in couponsItemData"
                            :key="c.id"
                            v-if="handleCouponListShow(c)"
                            @click="handleSelectCoupon(c.takeUuid)">
                        <span><i class="table-radio"
                                 :class="[{'curr' : c.couponKey === itemDatas[currItemIndex].couponKey}]"></i></span>
                        <span>{{ c.coupon.cpName }}</span>
                        <span>{{ c.coupon.cpMoney }}</span>
                    </li>
                </template>
                <li v-else class="employee-empty">
                    <p>暂无可用优惠券</p>
                </li>
            </ul>
        </zs-dialog>
    </div>
</template>

<script>
    import { isEmpty, getDataFromParam, turnToNextPage, imgPreLoad } from '../../../../../../../assets/js/utils';
    export default {
        name: 'maining',
        data(){
            return {
                context: this,
                canSubmit: true,
                //微信支付轮询计时器
                orderTimer: null,
                //基础信息
                orderNo: '',
                carNumber: '',
                realName: '',
                mobile: '',
                userId: '',
                memberRecordId: '',
                memberLvl: '',
                comment: '',
                //会员余额
                memberBalance: 0,
                memberBalanceDeduct: 0,
                //套餐项目剩余
                packageRemain: {},
                //工单项目列表
                itemDatas: [],
                itemColumns: [
                    {
                        title: '已选项目',
                        key: 'serviceItemName',
                        width: 152
                    },
                    {
                        title: '售价',
                        width: 76,
                        render(row){
                            return parseFloat(row.salePrice).toFixed(2);
                        }
                    },
                    {
                        title: '数量',
                        width: 64,
                        className: 'item-times',
                        render(row){
                            return row.itemsTimes;
                        }
                    },
                    {
                        title: '卡券抵扣',
                        width: 196,
                        render(row, col, index, _this){
                            const itemCoupons = [];

                            _this.couponsData.forEach((coupon) => {
                                if (_this.handleCouponListShow(coupon, index)) {
                                    itemCoupons.push(coupon);
                                }
                            });
                            return !isEmpty(row.couponUuid) ? `<span class="coupon-deduct use-coupon zs-coupon" @click="handlePopCoupon(${row.itemType},${index})">${row.couponName} (-${row.couponDeduct})</span>` : itemCoupons.length > 0 ? `<p class="ten-cross" @click="handlePopCoupon(${row.itemType},${index})"><i></i></p>` : `<span>无可用卡券</span>`;
                        }
                    },
                    {
                        title: '会员扣减',
                        width: 82,
                        render(row, col, index, _this){
                            let discountPrice = 0;
                            const priceBeforeDiscount = (row.itemsTimes - (row._useCard ? _this.handlePackageDeductTimes(row, index) : 0)) * row.salePrice - (row.couponDeduct || 0);

                            //row.discountType : 1 -> 折扣; 2 -> 会员价
                            if (row.discountType == 1) {
                                discountPrice = parseFloat((1 - (row.discountNumber || 10) / 10) * priceBeforeDiscount).toFixed(2);
                            } else if (row.discountType == 2) {
                                const temp = (row.itemsTimes - (row._useCard ? _this.handlePackageDeductTimes(row, index) : 0)) * (row.salePrice - (row.discountNumber || 0));

                                discountPrice = parseFloat(temp - priceBeforeDiscount > 0 ? priceBeforeDiscount : temp).toFixed(2);
                            }
                            _this.$set(_this.itemDatas[index], 'discountPrice', parseFloat(discountPrice));
                            return `-${discountPrice}`;
                        }
                    },
                    {
                        title: '总金额',
                        width: 84,
                        render(row, col, index, _this){
                            const totalPrice = parseFloat((row.itemsTimes - (row._useCard ? _this.handlePackageDeductTimes(row, index) : 0)) * row.salePrice - (row.couponDeduct || 0) - row.discountPrice).toFixed(2);

                            _this.$set(_this.itemDatas[index], 'totalPrice', parseFloat(totalPrice));
                            return totalPrice;
                        }
                    },
                    {
                        title: '派工人员',
                        width: 84,
                        render(row){
                            return row.workerName || '/';
                        }
                    },
                    {
                        title: '销售人员',
                        width: 84,
                        render(row){
                            return row.salerName || '/';
                        }
                    },
                    {
                        title: '使用套餐',
                        width: 126,
                        className: 'item-control',
                        render(row, col, index, _this){
                            return row._hasCard ? `
                                <p class="table-switch-wrap ${row._useCard ? 'is-usecard' : ''}">
                                    <span class="table-switch" @click="handleToggleUseCard(${index})"></span>
                                    ${row._useCard ? `x ${_this.handlePackageDeductTimes(row, index)}` : '不用卡'}
                                </p>` : '';
                        }
                    }
                ],
                currItemIndex: '',
                currItemType: '',
                //优惠券情况
                couponSelectVisible: false,
                couponsData: [],
                couponsItemData: [],
                couponKey: 1,
                //结算工单
                finalData: {
                    id: '',
                    totalPrice: '',
                    workOrderDetails: [],
                    businessDeduct: '',
                    materialCost: '',
                    payType: ''
                },
                //收款
                payCashPopVisible: false,
                payWechatPopVisible: false,
                //商家所有项目，用来重新计算会员折扣
                items: []
            };
        },
        computed: {
            //项目费用
            itemTotalPrice(){
                let price = 0;

                this.itemDatas.forEach((item) => {
                    price += parseFloat(item.totalPrice);
                });
                return parseFloat(price).toFixed(2);
            },
            //商家扣减前的应收款
            totalPriceBeforeDeduct(){
                return (parseFloat(this.itemTotalPrice) + parseFloat(this.finalData.materialCost || 0)).toFixed(2);
            },
            //使用余额前的总价格
            totalPriceBeforeBalance(){
                let price = this.totalPriceBeforeDeduct - (this.finalData.businessDeduct || 0);

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            },
            //实际应收
            totalPrice(){
                let price = this.totalPriceBeforeBalance - this.memberBalanceDeduct;

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            }
        },
        watch: {
            payWechatPopVisible(val){
                !val && clearInterval(this.orderTimer);
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /**
             * 更新工单信息
             * @param fn
             * @param loading
             */
            updateWorkOrder(fn, loading){

                this.finalData.businessDeduct = Math.min(parseFloat(this.finalData.businessDeduct || 0), this.totalPriceBeforeDeduct).toFixed(2) * 1;
                this.finalData.materialCost = parseFloat(this.finalData.materialCost || 0).toFixed(2) * 1;
                this.finalData.balanceDeduct = Math.min(parseFloat(this.memberBalanceDeduct), parseFloat(this.totalPriceBeforeBalance)).toFixed(2);
                this.finalData.totalPrice = parseFloat(this.totalPrice);

                this.itemDatas.forEach((item) => {
                    item.usePackageTimes = item._useCard ? item.usePackageTimes : 0;
                });
                this.finalData.workOrderDetails = this.itemDatas;
                this.$ajax(
                    this.$joggle.merchant.workorder.updateWorkOrder,
                    this.finalData,
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /**
             * 确认现金收款
             */
            updateWorkOrderForSettle(){
                this.$ajax(
                    this.$joggle.merchant.workorder.updateWorkOrderForSettle,
                    { orderId: this.finalData.id },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: '收款成功',
                                duration: 1200
                            });
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/workorder/detail_balance.html', { id: this.finalData.id });
                            }, 1200);
                        } else {
                            this.$message({
                                type: 'error',
                                message: '收款失败',
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

            /**
             * 根据工单id获取工单详情
             * @param fn
             * @param loading
             */
            selectWorkOrder(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderById,
                    { orderId: this.finalData.id },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            const workOrder = data.data.workOrder;
                            const user = data.data.user;

                            if (isEmpty(workOrder)) {
                                this.$message({
                                    type: 'error',
                                    message: '工单不存在!',
                                    duration: 1200
                                });
                                this.canSubmit = false;
                            } else if (workOrder.state == 4) {
                                this.$message({
                                    type: 'error',
                                    message: '该工单已结算!',
                                    duration: 1200
                                });
                                this.canSubmit = false;
                            } else {
                                //基础信息
                                this.carNumber = workOrder.carNumber;
                                this.orderNo = workOrder.orderNo;
                                this.comment = workOrder.comment || '';
                                this.mobile = workOrder.mobile || '----';
                                this.memberRecordId = isEmpty(user) ? '' : user.recordId || '';
                                this.userId = isEmpty(user) ? '' : user.userId || '';
                                this.memberLvl = isEmpty(this.memberRecordId) ? '非会员' : user.lvlName || '普通会员';
                                this.realName = isEmpty(user) ? '--' : user.realName || '--';
                                this.finalData.materialCost = workOrder.materialCost || '';
                                this.finalData.businessDeduct = workOrder.businessDeduct || '';
                                //套餐信息
                                this.packageRemain = this.handlePackageMerge(isEmpty(user) ? [] : user.packageList);
                                //会员余额
                                this.memberBalance = isEmpty(user) ? 0 : user.memberBalance || 0;
                                this.memberBalanceDeduct = workOrder.balanceDeduct ? user.memberBalance : 0;

                                //工单项目
                                const itemArr = isEmpty(workOrder) ? [] : workOrder.workOrderDetails || [];
                                const f0 = new Promise((resolve) => {
                                    this.selectCouponsByCarNumber(() => {
                                        resolve();
                                    }, loading);
                                });
                                const f1 = new Promise((resolve) => {
                                    this.selectServiceItem(() => {
                                        resolve();
                                    }, loading);
                                });

                                Promise.all([f0, f1]).then(() => {
                                    itemArr.forEach((it) => {

                                        //重新设置会员折扣
                                        this.items.forEach((item) => {
                                            if (item.id === it.serviceItemId) {
                                                this.$set(it, 'discountNumber', item.discountNumber);
                                                this.$set(it, 'discountType', item.discountType);
                                            }
                                        });

                                        //优惠券信息
                                        it._readonly = true;
                                        it._hasCard = it._useCard = false;
                                        if (isEmpty(it.itemType) || isEmpty(it.serviceItemId)) {
                                            it.itemType = -1;
                                        }
                                        //设置套餐使用情况
                                        for (let id in this.packageRemain) {
                                            if (this.packageRemain[id] > 0 && id == it.serviceItemId) {
                                                it._hasCard = true;
                                                it._useCard = parseInt(it.usePackageTimes || 0) > 0;
                                                break;
                                            }
                                        }
                                        //标记项目中已经选中的优惠券
                                        this.$set(it, 'couponKey', this.couponKey);
                                        //该变量用来控制项目中的卡券是否已失效
                                        let itCouponRemain = false;

                                        if (!isEmpty(it.couponUuid)) {
                                            this.couponsData.forEach((coupon) => {
                                                if (it.couponUuid === coupon.takeUuid) {
                                                    this.$set(coupon, 'couponKey', this.couponKey);
                                                    itCouponRemain = true;
                                                }
                                            });
                                            if (!itCouponRemain) {
                                                this.$set(it, 'couponUuid', '');
                                                this.$set(it, 'couponName', '');
                                                this.$set(it, 'couponDeduct', '');
                                            }
                                        }
                                        this.couponKey++;
                                    });
                                    this.itemDatas = itemArr;
                                    fn && fn();
                                });
                            }
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duraton: 1200
                            });
                        }
                    }
                );
            },
            /**
             * 通过车牌获取优惠券
             * @param fn
             * @param loading
             */
            selectCouponsByCarNumber(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectCouponsByCarNumber,
                    { carNumber: this.carNumber },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.couponsData = data.data;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                        fn && fn();
                    }
                );
            },
            /**
             * 获取商家服务项
             * @param fn
             * @param loading
             */
            selectServiceItem(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectServiceItem,
                    {
                        startPage: 1,
                        pageSize: 999,
                        itemType: '',
                        userId: this.userId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.items = data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        fn && fn();
                    }
                );
            },
            /**
             * 用来判断优惠券是否有效
             * @param fn
             * @param loading
             */
            selectCouponsById(fn, loading){
                const cpUuidArr = [];

                this.couponsData.forEach((c) => {
                    if (!isEmpty(c.couponKey)) {
                        cpUuidArr.push(c.takeUuid);
                    }
                });

                if (cpUuidArr.length === 0) {
                    fn && fn();
                    return;
                }
                this.$ajax(
                    this.$joggle.merchant.workorder.selectCouponsById,
                    { uuid: cpUuidArr.join(',') },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: '有卡券不可使用，请刷新页面重试！',
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /**
             * 判断是否支付成功
             * @param fn
             * @param fnError
             */
            selectWorkorderState(fn, fnError){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderById,
                    { orderId: this.finalData.id },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (parseInt(data.data.workOrder.state) === 4) {
                                fn && fn();
                            } else {
                                fnError && fnError();
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/

            /**
             * 切换是否使用项目套餐
             * @param index 当前项目索引
             */
            handleToggleUseCard(index){
                const currSelectItem = this.itemDatas[index];

                this.$set(currSelectItem, '_useCard', !currSelectItem._useCard);
                if (currSelectItem._useCard) {
                    this.$set(currSelectItem, 'couponUuid', '');
                    this.$set(currSelectItem, 'couponName', '');
                    this.$set(currSelectItem, 'couponDeduct', 0);
                    for (let i = 0; i < this.couponsData.length; i++) {
                        if (this.couponsData[i].couponKey === currSelectItem.couponKey) {
                            this.$set(this.couponsData[i], 'couponKey', '');
                            break;
                        }
                    }
                }
            },
            /**
             * 设置合并套餐
             * @param arr 从后台获取的套餐数组
             * @returns {{1: number}}
             */
            handlePackageMerge(arr){
                const remainTimes = { 1: 0 };

                if (arr.length > 0) {
                    for (let i = 0; i < arr.length; i++) {
                        if (arr[i].memberItemUseRecords.length > 0) {
                            let memberItemRC = arr[i].memberItemUseRecords;

                            for (let j = 0; j < memberItemRC.length; j++) {
                                let canCreate = true;

                                for (let k in remainTimes) {
                                    if (parseInt(k) === parseInt(memberItemRC[j].serviceItem.id)) {
                                        remainTimes[k] = parseInt(remainTimes[k]) + parseInt(memberItemRC[j].remainTimes);
                                        canCreate = false;
                                        break;
                                    }
                                }
                                if (canCreate) {
                                    remainTimes[memberItemRC[j].serviceItem.id] = memberItemRC[j].remainTimes;
                                }
                            }
                        }
                    }
                }
                return remainTimes;
            },
            /**
             * 计算套餐抵扣数
             * @param row 当前项目对象
             * @param index 当前项目索引
             * @returns {Number}
             */
            handlePackageDeductTimes(row, index){
                if (row._useCard) {
                    const arr = [row.itemsTimes, this.packageRemain[row.serviceItemId] || 0];

                    this.$set(this.itemDatas[index], 'usePackageTimes', Math.min.apply(Math, arr));
                }
                return parseInt(row.usePackageTimes);

            },
            /**
             * 打开卡券选择弹窗
             * @param itemType 当前项目类型
             * @param index 当前项目索引
             */
            handlePopCoupon(itemType, index){

                if (this.itemDatas[index]._useCard) {
                    this.$message({
                        type: 'warning',
                        message: '已选择使用套餐，不能使用优惠券！'
                    });
                    return;
                }

                this.currItemIndex = index;
                this.currItemType = itemType || '-1';

                const temp = [];

                this.couponsData.forEach((coupon, index) => {
                    if (this.handleCouponListShow(coupon)) {
                        coupon._index = index;
                        temp.push(coupon);
                    }
                });
                this.couponsItemData = temp;
                this.couponSelectVisible = true;
            },
            /**
             * 控制优惠券选择列表的展示
             * @param c 当前优惠券
             * @param index 当前项目索引
             * @returns {boolean}
             */
            handleCouponListShow(c, index){
                index = isEmpty(index) ? this.currItemIndex : index;
                const currItem = this.itemDatas[index];

                if (isEmpty(currItem)) return false;

                const itemType = currItem.itemType || -1;

                return (!c.couponKey || c.couponKey == currItem.couponKey) &&
                    (!c.coupon.itemType || c.coupon.itemType == itemType) &&
                    c.coupon.cpMoney - currItem.itemsTimes * currItem.salePrice <= 0;
            },
            /**
             * 选中和取消选中优惠券
             * @param uuid 优惠券uuid
             */
            handleSelectCoupon(uuid){
                const currItem = this.itemDatas[this.currItemIndex];

                for (let i = 0; i < this.couponsData.length; i++) {
                    if (this.couponsData[i].takeUuid === uuid) {
                        if (this.couponsData[i].couponKey === currItem.couponKey) {
                            this.$set(this.couponsData[i], 'couponKey', '');
                            this.$set(currItem, 'couponUuid', '');
                            this.$set(currItem, 'couponName', '');
                            this.$set(currItem, 'couponDeduct', 0);
                        } else {
                            this.couponsItemData.forEach((itemCoupon) => {
                                this.$set(itemCoupon, 'couponKey', '');
                                this.$set(this.couponsData[itemCoupon._index], 'couponKey', '');
                            });
                            this.$set(this.couponsData[i], 'couponKey', currItem.couponKey);
                            this.$set(currItem, 'couponUuid', this.couponsData[i].takeUuid);
                            this.$set(currItem, 'couponName', this.couponsData[i].coupon.cpName);
                            this.$set(currItem, 'couponDeduct', this.couponsData[i].coupon.cpMoney);
                        }
                    }
                }
                this.couponSelectVisible = false;
            },
            /**
             * 现金收款弹窗
             */
            handlePayCashPop(){
                if (!this.canSubmit) return;
                this.finalData.payType = 1;
                const loading = this.$loading();

                //优惠券有效
                this.selectCouponsById(() => {
                    //更新工单
                    this.updateWorkOrder(() => {
                        this.payCashPopVisible = true;
                        loading.close();
                    }, loading);
                }, loading);
            },
            /**
             * 微信收款弹窗
             */
            handlePayWechatPop(){
                if (!this.canSubmit || parseFloat(this.totalPrice) === 0) return;
                this.finalData.payType = 2;
                const loading = this.$loading();

                //优惠券有效
                this.selectCouponsById(() => {
                    //更新工单
                    this.updateWorkOrder(() => {
                        this.payWechatPopVisible = true;
                        this.handleShowBarCode();
                        loading.close();
                    }, loading);
                }, loading);
            },
            /**
             * 生成微信支付二维码
             */
            handleShowBarCode(){
                imgPreLoad(
                    `${this.$joggle.merchant.workorder.weChatPayBarCode}?orderId=${this.finalData.id}&totalPrice=${this.finalData.totalPrice}&t=${new Date().getTime()}`,
                    (img) => {
                        this.$refs.barCodeWrap.innerHTML = '';
                        this.$refs.barCodeWrap.appendChild(img);

                        clearInterval(this.orderTimer);

                        this.orderTimer = setInterval(() => {
                            this.selectWorkorderState(() => {
                                this.payWechatPopVisible = false;
                                this.$message({
                                    type: 'success',
                                    message: '支付成功',
                                    duration: 1200
                                });
                                setTimeout(() => {
                                    turnToNextPage('/web/merchant/home/workorder/detail_balance.html', { id: this.finalData.id });
                                }, 1200);
                            });
                        }, 2000);
                    });
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.finalData.id = getDataFromParam('id') || '';
            if (isEmpty(this.finalData.id)) {
                this.$message({
                    type: 'error',
                    message: '工单不存在!',
                    duration: 1200
                });
                this.canSubmit = false;
                return;
            }
            const loading = this.$loading();

            this.selectWorkOrder(() => {
                loading.close();
                this.$emit('input', false);
            }, loading);

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
