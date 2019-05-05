<template>
    <div class="workorder-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome" :home="!isOperator">工单详情</ma-head>
        <div class="workorder-second-page" :class="[{'pb10' : state == 4}]">
            <!--会员信息-->
            <ul class="member-info">
                <li class="order-state-label">
                    <zs-icon
                            :icon="['','serving','','nopay','finish'][state]"
                            size="40"
                            icon-dis="15"
                            :text="['','未完工','','待付款','已完成'][state]"></zs-icon>
                </li>
                <li class="member-info-car">
                    <span class="title">服务车辆</span>
                    <div
                            class="info info-car-number"
                            :class="[{'is-member' : memberRecordId}]">
                        <span class="number">{{ finalData.carNumber }}</span>
                        <div class="lvlname-wrap"><p v-if="finalData.carNumber" class="lvlname">{{ memberLvl}}</p>
                        </div>
                    </div>
                </li>
                <li v-if="!isOperator && memberRecordId" class="arrow-right check-member" @click="handleToMemberDetail">
                    查看消费记录
                </li>
                <li>
                    <span class="title">客户姓名</span>
                    <p class="info"><span>{{ finalData.user.realName || '----' }}</span><span class="user-remarks"
                                                                                              v-if="remarks">({{ remarks }})</span>
                    </p>
                </li>
                <li>
                    <span class="title">手机号</span>
                    <p class="info">{{ finalData.mobile || '----' }}</p>
                </li>
            </ul>
            <!--项目信息-->
            <div class="item-info">
                <p class="title">服务项目</p>
                <ul class="item-list">
                    <li v-for="(item,index) in itemSelectDatas" :key="item.id">
                        <div class="item-p-1">
                            <span class="item-name">{{ item.serviceItemName }}<i>X{{ item.itemsTimes }}</i></span>
                            <span class="item-operator">￥{{ parseFloat(item.salePrice).toFixed(2) }}</span>
                            <zs-switch
                                    v-if="!isOperator && state == 3 && item._hasCard"
                                    v-model="item._useCard"
                                    @change="handleToggleUseCard(index)"
                                    class="item-switch"></zs-switch>
                            <p
                                    v-if="!isOperator && ((state == 1 && item._useCard) || (state == 4 && item.usePackageTimes > 0))"
                                    class="use-card-label1">用卡</p>
                        </div>
                        <div class="item-p-2" v-if="item.workerName || (!isOperator && item._hasCard)">
                            <span class="item-price">{{ item.workerName }}</span>
                            <div class="use-card-info" v-if="!isOperator && item._hasCard">
                                <p
                                        v-if="state == 3"
                                        class="use-card-label"
                                        :class="[{'use-card' : item._useCard}]">
                                    {{ `${item._useCard ? '用卡' : '不用卡'}(剩余${packageRemain[item.serviceItemId]}次)` }}</p>
                                <p v-if="state != 3 && state != 4" class="card-remain">剩余{{
                                    packageRemain[item.serviceItemId] }}次</p>
                            </div>
                        </div>
                        <div
                                v-if="!isOperator && (couponsData.length > 0 || item.couponDeduct > 0)"
                                class="item-p-3"
                                :class="[{'arrow-right' : state == 3}]"
                                @click="handlePopCoupon(item.itemType,index)">
                            <span class="sub-title">卡券抵扣<i v-if="item.couponName">({{ item.couponName }})</i></span>
                            <span class="discount-price" :class="[{'gray' : !item.couponName}]">{{ handleCouponShow(item,index) }}</span>
                        </div>
                        <div class="item-p-4"
                             v-if="handleDiscountShow(item)">
                            <span class="sub-title">会员扣减</span>
                            <span class="discount-price">-￥{{ state != 3 ? item.discountPrice : handleCalcDiscountPrice(item,index) }}</span>
                        </div>
                        <div class="item-p-5" v-if="!isOperator">
                            <span class="sub-title">总金额</span>
                            <span class="discount-price">￥{{ state != 3 ? item.totalPrice : handleCalcItemTotalPrice(item,index) }}</span>
                        </div>
                    </li>
                </ul>
                <p class="item-total-price">
                    <template v-if="!isOperator && parseFloat(packageDeduct) > 0">
                        <span>套餐抵扣</span><i>￥{{ packageDeduct }}</i>
                    </template>
                    <span>项目费用</span><i>￥{{ itemCalcTotalPrice }}</i>
                    <!--<template v-if="isOperator && state == 4">-->
                    <!--<span>提成金额</span><i>￥ 5.00</i>-->
                    <!--</template>-->
                </p>
            </div>
            <!--扣减信息-->
            <ul class="deduct-info" v-if="!isOperator">
                <li v-if="state == 3 || finalData.materialCost > 0">
                    <zs-icon
                            class="label-icon"
                            icon="附"
                            size="18"
                            icon-dis="8"
                            icon-background="#e75845"
                            text="附加费用"></zs-icon>
                    <zs-input
                            v-if="state == 3"
                            type="number"
                            v-model="finalData.materialCost"></zs-input>
                    <span class="deduct-text" v-else>￥ {{ finalData.materialCost }}</span>
                </li>
                <li v-if="state == 3 || finalData.businessDeduct > 0">
                    <zs-icon
                            class="label-icon"
                            icon="减"
                            size="18"
                            icon-dis="8"
                            icon-background="#2cc068"
                            text="商家扣减"></zs-icon>
                    <template v-if="state == 3">
                        -
                        <zs-input
                                type="number"
                                v-model="finalData.businessDeduct"></zs-input>
                    </template>
                    <span class="deduct-text" v-else>- ￥ {{ finalData.businessDeduct }}</span>
                </li>
                <li class="member-balance" v-if="memberBalanceDeduct > 0">
                    <div class="p-1" :class="[{'state-other':state != 3}]">
                        <zs-icon
                                class="label-icon"
                                icon="会"
                                size="18"
                                icon-dis="8"
                                icon-background="#4b8dde"
                                text="会员余额支付"></zs-icon>
                        -
                        <template v-if="state == 3">
                            {{ useBalance ? Math.min(parseFloat(memberBalanceDeduct), parseFloat(totalPriceBeforeBalance
                            || 0)).toFixed(2) : 0 }}
                            <zs-switch v-model="useBalance"></zs-switch>
                        </template>
                        <template v-else>
                            ￥{{ parseFloat(memberBalanceDeduct).toFixed(2) }}
                        </template>
                    </div>
                    <p v-if="state!=4" class="p-2">(余额：{{ parseFloat(memberBalanceDeduct).toFixed(2) }})</p>
                </li>
                <!--会员折扣和中晟卡券用来兼容旧数据，以后可以删除-->
                <li v-if="this.state == 4 && _discountDeduct > 0">
                    <zs-icon
                            class="label-icon"
                            icon="折"
                            size="18"
                            icon-dis="8"
                            icon-background="#4b8dde"
                            :text="`会员折扣(${_discountNum}折)`"></zs-icon>
                    <span class="deduct-text">- ￥ {{ _discountDeduct }}</span>
                </li>
                <template v-if="state == 4 && _couponDeduct > 0">
                    <li
                            v-for="(coupon,index) in tempCouponShow"
                            class="coupon-wrap"
                            v-if="coupon.couponDeduct > 0"
                            :key="index">
                        <zs-icon
                                class="label-icon"
                                icon="券"
                                size="18"
                                icon-dis="8"
                                icon-background="#ff8023"
                                :text="coupon.couponName || '优惠券'"></zs-icon>
                        <span class="deduct-text">- ￥ {{ coupon.couponDeduct }}</span>
                    </li>
                </template>
                <li class="final-total-price" v-if="!isOperator">
                    {{ state == 4 ? ['','现金收款','微信收款'][finalData.payType] : '应收款' }}
                    <i>￥ {{ state == 4 || state == 1 ? _totalPrice : realTotalPrice }}</i>
                </li>
            </ul>
            <!--备注-->
            <div class="order-comment" v-if="!isOperator && (state == 3 || finalData.comment)"
                 :class="[{'is-disabled' : state != 3}]">
                <span class="label-icon">备注</span>
                <zs-input
                        v-if="state == 3"
                        icon="circle-cross"
                        v-model="finalData.comment"
                        placeholder="请输入工单备注"></zs-input>
                <span v-else>{{ finalData.comment }}</span>
            </div>
            <!--图片-->
            <div class="order-images" v-if="!isOperator">
                <p class="title">施工图片<i v-if="state != 4">(最多可上传6张照片)</i></p>
                <div class="up-load-box">
                    <upload
                            v-if="state != 4"
                            :images="images"
                            @change="selectImageLink"
                            @delete="deleteWorkOrderImg"
                            @view="handleViewPic"
                    ></upload>
                    <template v-else>
                        <ul v-if="images.length>0" class="image-show-wrap clr">
                            <li class="image-show-item" v-for="(image,index) in images" :key="image.id"
                                @click="handleViewPic(index)">
                                <div class="image-show" :style="{'backgroundImage':`url(${image.showImg})`}"></div>
                            </li>
                        </ul>
                        <p v-else class="list-empty">未上传图片！</p>
                    </template>
                </div>
            </div>
            <!--时间-->
            <ul class="order-times">
                <li><span>工单编号</span> {{ orderNo }}</li>
                <li><span>新建时间</span> {{ createTime }}</li>
                <li v-if="state == 3 || (state == 4 && isOperator)"><span>完工时间</span> {{ commitTime }}</li>
                <li v-if="state == 4 && !isOperator"><span>结算时间</span> {{ payTime }}</li>
            </ul>
            <!--按钮-->
            <ul class="workorder-page-footer" v-if="!isOperator && state == 1">
                <li
                        class="left bg-white"
                        @click="handleDeleteOrderClick">取消工单
                </li>
                <li
                        class="right"
                        @click="updateWorkOrderForFinish">施工完成
                </li>
            </ul>
            <ul class="workorder-page-footer no-pay-control" v-if="!isOperator && state == 3">
                <li
                        class="left bg-white"
                        @click="handleDeleteOrderClick">取消工单
                </li>
                <li
                        class="left"
                        :class="[{'is-disabled' : !canSubmit }]"
                        @click="handleCashPay">现金收款
                </li>
                <li
                        class="right"
                        :class="[{'is-disabled' : !canSubmit || parseFloat(realTotalPrice) === 0}]"
                        @click="handleWechatPay">微信收款
                </li>
            </ul>
        </div>
        <!--现金收款-->
        <zs-dialog class="settle-pop" v-model="payCashPopVisible" show-close>
            <p slot="title">
                <zs-icon icon="nopay" size="24" icon-dis="8" text="现金收款"></zs-icon>
            </p>
            <p class="pay-pop-p1">￥ {{ realTotalPrice }}</p>
            <p class="pay-pop-p2">是否收到现金或刷卡支付?</p>
            <ul class="pay-pop-control" slot="footer">
                <li>
                    <zs-button @click="payCashPopVisible = false">取消</zs-button>
                </li>
                <li>
                    <zs-button @click="updateWorkOrderForSettle">确定收款</zs-button>
                </li>
            </ul>
        </zs-dialog>
        <!--微信收款-->
        <zs-dialog class="settle-pop wechat-pay-pop" v-model="payWechatPopVisible">
            <p slot="title">
                <zs-icon icon="wechat-pay" size="24" icon-dis="8" text="微信扫码付款"></zs-icon>
            </p>
            <p class="pay-pop-barcode" ref="barCodeWrap">
                <img class="pay-pop-loading" src="../../../../../assets/img/three-dots.svg">
            </p>
            <p class="pay-pop-p1">￥ {{ realTotalPrice }}</p>
        </zs-dialog>
        <!--优惠券弹窗-->
        <div class="coupon-modal" v-show="couponSelectVisible" @click="couponSelectVisible = false"></div>
        <div
                class="coupon-select-wrap"
                :class="[{'coupon-show' : couponSelectVisible}]">
            <ul class="coupon-list">
                <li
                        class="coupons-item"
                        v-for="(coupon,index) in couponsItemData"
                        :key="coupon.id"
                        @click="handleSelectCoupon(coupon.takeUuid)">
                    <div class="coupons-price">
                        <span class="coupons-price-label">￥</span>{{ coupon.coupon.cpMoney }}
                        <p class="coupons-price-type">现金券</p>
                    </div>
                    <div class="coupons-detail">
                        <p class="coupons-name">{{ coupon.coupon.cpName }}</p>
                        <p class="coupons-date">
                            {{`${coupon.useBeginDay.slice(0,10)}到${coupon.useEndDay.slice(0,10)}`}}</p>
                    </div>
                    <zs-icon
                            class="coupons-checked"
                            :icon="coupon.couponKey === itemSelectDatas[currItemIndex].couponKey ? 'check' : 'uncheck'"
                            size="20"></zs-icon>
                </li>
            </ul>
        </div>
        <!--查看大图-->
        <swiper v-model="imageViewVisible" :images="images" :index="imageIndex"></swiper>
    </div>
</template>

<script>
    import {
        turnToNextPage,
        getDataFromParam,
        isEmpty,
        imgPreLoad
    } from '../../../../../assets/js/utils';
    import { defaultInfo } from '../../../../../assets/js/defaultInfo';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import carNumber from '../../../../../components_proj/car_number/app.vue';
    import upload from '../../../../../components_proj/upload/app.vue';
    import swiper from '../../../../../components_proj/swiper/app.vue';
    export default {
        components: {
            maHead,
            carNumber,
            upload,
            swiper
        },
        data(){
            return {
                isLoading: true,
                isOperator: false,
                state: '',
                canSubmit: true,
                orderTimer: null,
                //会员相关
                memberRecordId: '',
                memberLvl: '',
                packageRemain: {},
                remarks: '',
                useBalance: true,
                memberBalanceDeduct: 0,
                //已选项目列表
                itemSelectDatas: [],
                //工单图片
                images: [],
                imageViewVisible: false,
                imageIndex: 0,
                //工单相关时间
                orderNo: '',
                createTime: '',
                commitTime: '',
                payTime: '',
                //已结算的总金额
                _totalPrice: 0,
                //收款
                payCashPopVisible: false,
                payWechatPopVisible: false,
                //更新工单
                finalData: {
                    id: '',
                    totalPrice: '',
                    carNumber: '',
                    workOrderDetails: [],
                    userId: '',
                    user: {
                        realName: ''
                    },
                    mobile: '',
                    businessDeduct: '',
                    materialCost: '',
                    payType: '',
                    comment: ''
                },
                //优惠券相关
                couponSelectVisible: false,
                couponsData: [],
                couponsItemData: [],
                couponKey: 1,
                currItemIndex: '',
                currItemType: '',
                //商家所有项目,用来重新计算会员折扣
                items: [],
                //以下数据用来兼容旧数据，以后可以删除
                _discountNum: 10,
                _discountDeduct: 0,
                _couponDeduct: 0,
                tempCouponShow: []
            };
        },
        computed: {
            //套餐抵扣 (使用套餐次数)*项目价格，求和
            packageDeduct(){
                let price = 0;

                this.itemSelectDatas.forEach((item) => {
                    if (this.state == 4) {
                        price += (item.usePackageTimes || 0) * item.salePrice;
                    } else {
                        price += (item._useCard ? item.usePackageTimes : 0) * item.salePrice;
                    }
                });
                return parseFloat(price).toFixed(2);
            },
            //计算完扣减后的项目费用（操作不做扣减）
            itemCalcTotalPrice(){
                let price = 0;

                this.itemSelectDatas.forEach((item) => {
                    if (!this.isOperator) {
                        price += parseFloat(item.totalPrice);
                    } else {
                        price += parseFloat(item.itemsTimes * item.salePrice);
                    }

                });
                return parseFloat(price).toFixed(2);
            },
            //商家扣减前的应收款
            priceBeforeBusinessDeduct(){
                return parseFloat(this.itemCalcTotalPrice) + parseFloat(this.finalData.materialCost || 0);
            },
            //使用余额前的总价格
            totalPriceBeforeBalance(){
                let price = this.priceBeforeBusinessDeduct - (this.finalData.businessDeduct || 0);

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            },
            //总价
            realTotalPrice(){
                let price = this.totalPriceBeforeBalance - (this.useBalance ? this.memberBalanceDeduct : 0);

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            }
        },
        methods: {

            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 新增工单图片
             * @param orderImg 图片标识
             * @param fn
             * @param loading
             */
            insertWorkOrderImg(orderImg, fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.insertWorkOrderImg,
                    {
                        workOrderId: this.finalData.id,
                        orderImg
                    },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            fn && fn(data.data);
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
            /**
             * 删除图片
             * @param index 当前图片索引
             * @param id 图片id
             */
            deleteWorkOrderImg(index, id){
                this.$ajax(
                    this.$joggle.merchant.workorder.deleteWorkOrderImg,
                    { imgId: id },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$delete(this.images, index);
                            this.$message({
                                type: 'success',
                                message: '删除成功',
                                duration: 1000
                            });
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1000
                            });
                        }
                    }
                );
            },
            /**
             * 删除工单
             */
            deleteOrder(){
                this.$ajax(
                    this.$joggle.merchant.workorder.deleteWorkOrderById,
                    { orderId: this.finalData.id },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: '取消成功'
                            });
                            setTimeout(() => {
//                                turnToNextPage('/wap/merchant/home/workorder/list.html#eyJ0YWIiOjJ9')
                                window.history.go(-1);
                            }, 1200);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 完成工单
             */
            updateWorkOrderForFinish(){
                this.$ajax(
                    this.$joggle.merchant.workorder.updateWorkOrderForFinish,
                    { orderId: this.finalData.id },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.selectWorkOrder(() => {
                                this.$message({
                                    type: 'success',
                                    message: data.msg
                                });
                                loading.close();
                            }, loading);
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /**
             * 确认收款
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
                                message: '收款成功'
                            });
                            this.payCashPopVisible = false;
                            setTimeout(() => {
                                turnToNextPage('/wap/merchant/home/workorder/detail.html', { id: this.finalData.id });
                            }, 1200);
                        } else {
                            this.$message({
                                type: 'error',
                                message: '收款失败'
                            });
                        }
                    }
                );
            },
            /**
             * 更新工单信息
             * @param fn
             * @param loading
             */
            updateWorkOrder(fn, loading){

                this.finalData.businessDeduct = Math.min(parseFloat(this.finalData.businessDeduct || 0), this.priceBeforeBusinessDeduct).toFixed(2) * 1;
                this.finalData.couponDeduct = parseFloat(this.realCouponDeduct || 0);
                this.finalData.discountDeduct = parseFloat(this.discountDeduct || 0);
                this.finalData.materialCost = parseFloat(this.finalData.materialCost || 0).toFixed(2) * 1;
                this.finalData.balanceDeduct = this.useBalance ? Math.min(parseFloat(this.memberBalanceDeduct), parseFloat(this.totalPriceBeforeBalance)).toFixed(2) : 0;
                this.finalData.totalPrice = parseFloat(this.realTotalPrice);
                this.itemSelectDatas.forEach((item) => {
                    item.usePackageTimes = item._useCard ? item.usePackageTimes : 0;
                });
                this.finalData.workOrderDetails = this.itemSelectDatas;
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
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 根据工单id/车牌获取工单信息
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
                                loading.close();
                                this.canSubmit = false;
                                return;
                            }
                            //套餐信息
                            this.packageRemain = this.handlePackageMerge(isEmpty(user) ? [] : user.packageList);
                            //工单信息
                            this.state = workOrder.state;
                            this.finalData.carNumber = workOrder.carNumber;
                            this.finalData.id = workOrder.id;
                            this.finalData.mobile = workOrder.mobile;
                            this.finalData.businessDeduct = workOrder.businessDeduct || '';
                            this.finalData.materialCost = workOrder.materialCost || '';
                            this.finalData.comment = workOrder.comment || '';
                            this.finalData.payType = workOrder.payType || '';
                            this.orderNo = workOrder.orderNo || '';
                            this.createTime = workOrder.createTime || '';
                            this.commitTime = workOrder.commitTime || '';
                            this.payTime = workOrder.payTime || '';
                            this._totalPrice = workOrder.totalPrice || 0;
                            //会员信息
                            this.finalData.user.realName = isEmpty(user) ? '' : user.realName;
                            this.finalData.userId = isEmpty(user) ? '' : user.userId || '';
                            this.memberRecordId = isEmpty(user) ? '' : user.recordId || '';
                            this.memberLvl = isEmpty(this.memberRecordId) ? '非会员' : user.lvlName || '普通会员';
                            this.remarks = isEmpty(user) ? '' : user.remarks || '';
                            //会员余额
                            this.useBalance = workOrder.balanceDeduct > 0;
                            this.memberBalanceDeduct = this.state == 3 ? isEmpty(user) ? 0 : user.memberBalance || 0 : workOrder.balanceDeduct || 0;
                            //工单图片
                            const imgTemp = workOrder.workOrderImgs || [];
                            const imgArr = [];

                            imgTemp.forEach((img) => {
                                imgArr.push(
                                    {
                                        id: img.id,
                                        showImg: img.orderImgSrc
                                    }
                                );
                            });
                            this.images = imgArr;
                            //设置项目列表
                            const itemSelectTemps = workOrder.workOrderDetails || [];

                            if (this.state == 3) {

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

                                    itemSelectTemps.forEach((it) => {
                                        //重新设置会员折扣
                                        this.items.forEach((item) => {
                                            if (it.serviceItemId === item.id) {
                                                this.$set(it, 'discountNumber', item.discountNumber);
                                                this.$set(it, 'discountType', item.discountType);
                                            }
                                        });

                                        //设置已选项目的套餐使用情况，和优惠券使用情况
                                        this.$set(it, '_hasCard', false);
                                        this.$set(it, '_useCard', false);
                                        this.$set(it, '_autoCoupon', true);
                                        if (isEmpty(it.itemType) || isEmpty(it.serviceItemId)) {
                                            it.itemType = -1;
                                        }
                                        //设置套餐使用情况
                                        for (let id in this.packageRemain) {
                                            if (this.packageRemain[id] > 0 && id == it.serviceItemId) {
                                                this.$set(it, '_hasCard', true);
                                                this.$set(it, '_useCard', parseInt(it.usePackageTimes || 0) > 0);
                                                this.$set(it, '_autoCoupon', parseInt(it.usePackageTimes || 0) === 0);
                                                this.$set(it, 'usePackageTimes', Math.min(it.itemsTimes, this.packageRemain[id]));
                                                break;
                                            }
                                        }
                                        //标记项目中已经选中的优惠券
                                        this.$set(it, 'couponKey', this.couponKey);
                                        //该变量用来控制项目中的优惠券是否已失效
                                        let itCouponRemain = false;

                                        if (!isEmpty(it.couponUuid)) {
                                            this.couponsData.forEach((coupon) => {
                                                if (it.couponUuid === coupon.takeUuid) {
                                                    this.$set(coupon, 'couponKey', this.couponKey);
                                                    itCouponRemain = true;
                                                }
                                            });
                                            //已失效，清空项目中的优惠券
                                            if (!itCouponRemain) {
                                                this.$set(it, 'couponUuid', '');
                                                this.$set(it, 'couponName', '');
                                                this.$set(it, 'couponDeduct', '');
                                            }
                                        }
                                        this.couponKey++;
                                    });
                                    fn && fn();
                                });
                            } else {
                                if (this.state == 1) {
                                    itemSelectTemps.forEach((it) => {
                                        this.$set(it, '_hasCard', this.packageRemain[it.serviceItemId] > 0);
                                        this.$set(it, '_useCard', parseInt(it.usePackageTimes || 0) > 0);
                                    });
                                }
                                fn && fn();
                            }
                            this.itemSelectDatas = itemSelectTemps;

                            //--------- 以下兼容旧数据------------，以后可以删除
                            this._discountNum = workOrder.discountNum || 10;
                            this._discountDeduct = workOrder.discountDeduct || 0;
                            this._couponDeduct = workOrder.couponDeduct || 0;
                            //设置优惠券展示信息
                            const couponName = workOrder.couponName ? workOrder.couponName.split(',') : [];
                            const couponDeduct = workOrder.couponEveryDeduct ? workOrder.couponEveryDeduct.split(',') : [];

                            for (let i = 0, len = couponName.length; i < len; i++) {
                                this.tempCouponShow.push(
                                    {
                                        couponName: couponName[i],
                                        couponDeduct: couponDeduct[i]
                                    }
                                );
                            }
                            //--------- 以上兼容旧数据------------
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duraton: 1200
                            });
                        }
                    },
                    () => {
                        //如果获取工单失败,清空所有相关数据
                        this.finalData.id = '';
                        this.finalData.totalPrice = 0;
                        this.finalData.workOrderDetails = [];
                        this.finalData.userId = '';
                        this.finalData.user.realName = '';
                        this.finalData.mobile = '';
                        this.finalData.businessDeduct = '';
                        this.finalData.materialCost = '';
                        this.finalData.comment = '';
                        this.itemSelectDatas = [];
                        this.memberLvl = '';
                        this.packageRemain = {};
                        this.images = [];
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
                    this.$joggle.merchant.workorder.selectServiceItemByPage,
                    {
                        itemType: '',
                        startPage: 1,
                        pageSize: 999,
                        userId: this.finalData.userId
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
             * 通过车牌获取优惠券
             * @param fn
             */
            selectCouponsByCarNumber(fn){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectCouponsByCarNumber,
                    { carNumber: this.finalData.carNumber },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            const temp = data.data || [];

                            temp.forEach((coupon) => {
                                this.$set(coupon, '_selected', false);
                            });
                            this.couponsData = temp;
                            fn && fn();
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
            /**
             * 上传base64，获取图片路径
             * @param image base64数据流
             */
            selectImageLink(image){
                this.$ajax(
                    this.$joggle.mzscp.uploadImgForBase64,
                    {
                        linkType: 'workOrderImg',
                        img: image
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.insertWorkOrderImg(data.data.imgLink, (data) => {
                                this.images.push({
                                    id: data.id,
                                    showImg: data.orderImgSrc
                                });
                                this.$message({
                                    type: 'success',
                                    message: '上传成功'
                                });
                                loading.close();
                            }, loading);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /**
             * 判断优惠券是否有效
             * @param fn
             * @param loading
             */
            selectCouponsById(fn, loading){
                if (isEmpty(this.finalData.couponUuid)) {
                    fn && fn();
                    return;
                }
                this.$ajax(
                    this.$joggle.merchant.workorder.selectCouponsById,
                    { uuid: this.finalData.couponUuid },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
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
            selectIsPaySuccess(fn, fnError){
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
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 返回首页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 跳转到会员详情
             */
            handleToMemberDetail(){
                if (!isEmpty(this.memberRecordId) && !this.isOperator) {
                    turnToNextPage('/wap/merchant/home/member/detail.html', { id: this.memberRecordId });
                }
            },
            /**
             * 点击查看大图
             * @param index 点击的图片索引
             */
            handleViewPic(index){
                this.imageViewVisible = true;
                this.imageIndex = index;
            },
            /**
             * 合并套餐
             * @param arr 从后台获取的套餐数据
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
             * 切换是否使用套餐 当前操作的项目索引
             * @param index
             */
            handleToggleUseCard(index){
                const it = this.itemSelectDatas[index];

                this.$set(it, '_autoCoupon', false);
                if (it._useCard) {
                    this.$set(it, 'couponUuid', '');
                    this.$set(it, 'couponName', '');
                    this.$set(it, 'couponDeduct', 0);
                    for (let i = 0; i < this.couponsData.length; i++) {
                        if (this.couponsData[i].couponKey === it.couponKey) {
                            this.$set(this.couponsData[i], 'couponKey', '');
                            break;
                        }
                    }
                }
            },
            /**
             * 会员折扣显示条件
             */
            handleDiscountShow(item){
                return this.state == 3 && item.discountType && (item.discountNumber || item.discountNumber == '0') || item.discountPrice > 0;
            },
            /**
             * 计算项目会员折扣
             * @param item 当前项目
             * @param index 当前项目索引
             * @returns {string}
             */
            handleCalcDiscountPrice(item, index){
                let discountPrice = 0;
                const priceBeforeDiscount = (item.itemsTimes - (item._useCard ? item.usePackageTimes : 0)) * item.salePrice - (item.couponDeduct || 0);

                //row.discountType : 1 -> 折扣; 2 -> 会员价
                if (item.discountType == 1) {
                    discountPrice = parseFloat((1 - item.discountNumber / 10) * priceBeforeDiscount).toFixed(2);
                } else if (item.discountType == 2) {
                    const temp = (item.itemsTimes - (item._useCard ? item.usePackageTimes : 0)) * (item.salePrice - item.discountNumber);

                    discountPrice = parseFloat(temp - priceBeforeDiscount > 0 ? priceBeforeDiscount : temp).toFixed(2);
                }

                this.$set(this.itemSelectDatas[index], 'discountPrice', parseFloat(discountPrice));
                return discountPrice;
            },
            /**
             * 计算项目总价
             * @param item 当前项目
             * @param index 当前项目索引
             * @returns {string}
             */
            handleCalcItemTotalPrice(item, index){
                const totalPrice = parseFloat((item.itemsTimes - (item._useCard ? item.usePackageTimes : 0)) * item.salePrice - (item.couponDeduct || 0) - item.discountPrice).toFixed(2);

                this.$set(this.itemSelectDatas[index], 'totalPrice', parseFloat(totalPrice));
                return totalPrice;
            },
            /**
             * 打开卡券选择弹窗
             * @param itemType 当前点击的项目类型
             * @param index 当前点击的项目索引
             */
            handlePopCoupon(itemType, index){
                if (this.state != 3) return;
                if (this.itemSelectDatas[index]._useCard) {
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
                if (this.couponsItemData.length > 0) {
                    this.couponSelectVisible = true;
                }
            },
            /**
             * 控制优惠券选择列表的展示
             * @param c 当前优惠券
             * @param index 当前项目索引
             * @returns {boolean}
             */
            handleCouponListShow(c, index){
                index = isEmpty(index) ? this.currItemIndex : index;
                const currItem = this.itemSelectDatas[index];
                const itemType = currItem.itemType;

                if (isEmpty(currItem)) return false;
                return (!c.couponKey || c.couponKey == currItem.couponKey) &&
                    (!c.coupon.itemType || c.coupon.itemType == itemType) &&
                    c.coupon.cpMoney - currItem.itemsTimes * currItem.salePrice <= 0;
            },
            /**
             * 选中和取消选中优惠券
             * @param uuid 当前优惠券uuid
             */
            handleSelectCoupon(uuid){
                const currItem = this.itemSelectDatas[this.currItemIndex];

                this.$set(currItem, '_autoCoupon', false);
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
                        break;
                    }
                }
                this.couponSelectVisible = false;
            },
            /**
             * 卡券栏位显示文字
             * @param item 当前项目数据
             * @param index 当前项目索引
             * @returns {string}
             */
            handleCouponShow(item, index){
                const temp = [];

                this.couponsData.forEach((coupon) => {
                    if (this.handleCouponListShow(coupon, index)) {
                        temp.push(coupon);
                    }
                });
                return temp.length === 0 && this.state == 3 ? '暂无可用卡券' : item.couponName ? `-￥${item.couponDeduct}` : '请选择卡券';
            },
            /**
             * 点击现金收款
             */
            handleCashPay(){
                //现金收款弹窗
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
             * 点击微信收款
             */
            handleWechatPay(){
                //微信收款弹窗
                if (!this.canSubmit || parseFloat(this.realTotalPrice) === 0) return;
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
             * 点击取消工单
             */
            handleDeleteOrderClick(){
                this.$confirm({
                    type: 'order',
                    message: '是否取消该工单?'
                }).then(() => {
                    this.deleteOrder();
                }).catch(() => {
                });
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
                            this.selectIsPaySuccess(() => {
                                this.payWechatPopVisible = false;
                                this.$message({
                                    type: 'success',
                                    message: '支付成功'
                                });
                                setTimeout(() => {
                                    turnToNextPage('/wap/merchant/home/workorder/detail.html', { id: this.finalData.id });
                                }, 1200);
                            });
                        }, 2000);
                    });
            }

            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/


            //计算套餐抵扣数
//            packageDeductTimes(row){
//                const arr = [row.itemsTimes, this.packageRemain[row.serviceItemId] || 0];
//
//                if (this.state != 4) {
//                    this.$set(row, 'usePackageTimes', Math.min.apply(Math, arr));
//                }
//                return row.usePackageTimes;
//            },

            //筛选出能用的最大面额的优惠券
//            selectMaxCoupon(item, couponKey){
//                let itemType = item.itemType;
//                let itemPrice = item.salePrice * (item.itemsTimes || 1);
//                let maxCoupon = {
//                    coupon: {
//                        cpMoney: 0
//                    }
//                };
//                let maxCouponIndex = -1;
//
//                this.couponsData.forEach((coupon, index) => {
//                    if (
//                        item._autoCoupon &&
//                        (isEmpty(coupon.couponKey) || coupon.couponKey === item.couponKey) &&
//                        (!coupon.coupon.itemType || itemType == coupon.coupon.itemType) &&
//                        coupon.coupon.cpMoney - itemPrice <= 0 &&
//                        coupon.coupon.cpMoney - maxCoupon.coupon.cpMoney >= 0) {
//                        maxCoupon = coupon;
//                        maxCouponIndex = index;
//                    }
//                });
//                if (maxCouponIndex > -1) {
//                    this.$set(this.couponsData[maxCouponIndex], 'couponKey', couponKey);
//                    return this.couponsData[maxCouponIndex];
//                }
//                return {};
//            },

            //----------------------------------------------------------------------

        },
        created(){
            this.finalData.id = getDataFromParam('id') || '';
            if (isEmpty(this.finalData.id)) {
                this.$message({
                    type: 'error',
                    message: '该工单不存在'
                });
                this.canSubmit = false;
                return;
            }
            const loading = this.$loading();

            defaultInfo((info) => {
                this.isOperator = info.mercharType ? info.mercharType.indexOf('operator') > -1 : false;
                this.selectWorkOrder(() => {
                    loading.close();
                    this.isLoading = false;
                }, loading);
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
