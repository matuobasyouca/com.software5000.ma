<template>
    <div class="workorder-page" v-show="!isLoading">
        <ma-head
                @home-click="handleGoHome"
                :home="!isOperator"
                rightIcon="search1"
                :rightIconSize="18"
                @right-icon-click="searchPage=true"
        >{{ isEdit ? '修改工单' : '开单' }}
        </ma-head>
        <div class="car-number-show" v-if="isEdit">
            <zs-icon icon="car" size="24" text="服务车辆"></zs-icon>
            {{ this.finalData.carNumber }}
        </div>
        <car-number
                v-else
                :car-province="currCarArea"
                :car-letter="currCarLetter"
                :car-num="currCarNum"
                photo-icon
                @get-car-number="handleGetCarNumber"></car-number>
        <div class="item-select">
            <ul class="menu">
                <li
                        :class="[{'curr' : itemType === t[0]}]"
                        v-for="t in itemTypes"
                        v-if="!isOperator || operatorItemType.indexOf(t[0]) > -1"
                        :key="t[0]"
                        @click="handleSelectItemType(t[0])">{{ t[1] }}
                </li>
            </ul>
            <ul class="list">
                <li v-for="item in itemDatas" :key="item.id">
                    <p class="item-name">{{ item.itemName}}</p>
                    <div class="item-detail">
                        <span class="item-price">￥{{ item.salePrice }}</span>
                        <zs-input-number
                                :class="[{'is-zero' : item.itemsTimes == 0}]"
                                :min="0"
                                :max="totalSelectItemId.indexOf(item.id) > -1 ? 0 : 1000"
                                :disabled="totalSelectItemId.indexOf(item.id) > -1"
                                v-model="item.itemsTimes"
                                @click="handleItemTimesNotice(item)"
                                @change="handleItemTimesChange(item,$event)"></zs-input-number>
                    </div>
                </li>
            </ul>
        </div>
        <div class="workorder-page-footer">
            <span class="total-price" v-if="itemSelectDatas.length > 0">合计<i>￥{{ itemTotalPrice }}</i></span>
            <span class="notice" v-else>未选择服务项目</span>
            <p
                    class="control"
                    :class="[{'is-disabled' : itemSelectDatas.length === 0}]"
                    @click="handleNext">
                {{isOperator ? '提交工单' :'下一步'}}</p>
        </div>

        <!--客户搜索框-->
        <zs-slide-page class="search-page" v-model="searchPage" :homeIcon="false">
            <div class="search-page-input" slot="slide-page-title">
                <zs-input preIcon="search" v-model="memberPara.keyWord" placeholder="请输入姓名、车牌、关键词"
                          @enter="selectMemberData(false)"></zs-input>
                <span class="search-page-input-text" @click="selectMemberData(false)">搜索</span>
            </div>
            <ul class="item-list">
                <template v-if="memberData.length > 0">
                    <li class="list" v-for="member in memberData" :key="member.recordId"
                        @click="handSelectMemberData(member)">
                        <div class="list-top">
                            <zs-icon size="16" icon="user" :text="member.realName || '&nbsp;'"></zs-icon>
                            <zs-icon size="16" :icon="member.recordId ? 'member' : 'no-member'"
                                     :text="!member.recordId ? '非会员' : member.lvlName || '普通会员'"></zs-icon>
                            <span class="list-div-mobile r" v-if="member.mobile">{{member.mobile}}</span>
                        </div>
                        <div class="list-bottom">
                            <zs-icon size="16" icon="car2" :text="member.carNumber"></zs-icon>
                        </div>
                    </li>
                    <li
                            class="show-more"
                            :class="[{'no-more' : !hasNextPage}]"
                            @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                    </li>
                </template>
                <li v-else class="list-empty">暂无客户</li>
            </ul>
        </zs-slide-page>
        <!--下一页-->
        <zs-slide-page v-model="nextPageVisible" title="开单" @go-home="handleGoHome">
            <div class="workorder-second-page">
                <ul class="member-info">
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
                    <li v-if="!isOperator && memberRecordId" class="arrow-right check-member"
                        @click="handleToMemberDetail">查看消费记录
                    </li>
                    <li class="user-realname">
                        <span class="title">客户姓名</span>
                        <p class="info">
                            <zs-input
                                    v-model="finalData.user.realName"
                                    icon="circle-cross"
                                    placeholder="请输入客户姓名"></zs-input>
                        </p>
                        <span v-if="remarks" class="user-remarks">（ {{ remarks }}）</span>
                    </li>
                    <li>
                        <span class="title">手机号</span>
                        <p class="info">
                            <zs-input
                                    v-model="finalData.mobile"
                                    icon="circle-cross"
                                    :maxlength="11"
                                    placeholder="请输入手机号"></zs-input>
                        </p>
                    </li>
                </ul>
                <div class="item-info">
                    <p class="title">服务项目</p>
                    <ul class="item-list">
                        <li
                                v-for="(item,index) in itemSelectDatas"
                                :key="item.id">
                            <div class="item-p-1">
                                <span class="item-name">{{ item.serviceItemName }}<i>X{{ item.itemsTimes }}</i></span>
                                <span class="item-operator">￥{{ parseFloat(item.salePrice).toFixed(2) }}</span>
                                <zs-switch v-if="item._hasCard" v-model="item._useCard" class="item-switch"
                                           @change="handleToggleUseCard(index)"></zs-switch>
                            </div>
                            <div class="item-p-2" v-if="item.workerName || item._useCard">
                                <span class="item-price">{{ item.workerName }}</span>
                                <div class="use-card-info" v-if="item._hasCard">
                                    <p
                                            class="use-card-label"
                                            :class="[{'use-card' : item._useCard}]">
                                        {{ `${item._useCard ? '用卡' : '不用卡'}(剩余${packageRemain[item.serviceItemId]}次)` }}
                                    </p>
                                </div>
                            </div>
                            <div
                                    v-if="couponsData.length > 0"
                                    class="item-p-3 arrow-right"
                                    @click="handlePopCoupon(item.itemType,index)">
                                <span class="sub-title">卡券抵扣<i v-if="item.couponName">({{ item.couponName }})</i></span>
                                <span class="discount-price" :class="[{'gray' : !item.couponName}]">{{ handleCouponShow(item,index) }}</span>
                            </div>
                            <div class="item-p-4"
                                 v-if="item.discountType == 2 || (item.discountType == 1  && item.discountNumber < 10)">
                                <span class="sub-title">会员扣减</span>
                                <span class="discount-price">-￥{{ handleCalcDiscountPrice(item,index) }}</span>
                            </div>
                            <div class="item-p-5">
                                <span class="sub-title">总金额</span>
                                <span class="discount-price">￥{{ handleCalcItemTotalPrice(item,index) }}</span>
                            </div>
                        </li>
                    </ul>
                    <p class="item-total-price">项目费用<i>￥{{ itemCalcTotalPrice }}</i></p>
                </div>
                <ul class="deduct-info">
                    <li>
                        <zs-icon
                                class="label-icon"
                                icon="附"
                                size="18"
                                icon-dis="8"
                                icon-background="#e75845"
                                text="附加费用"></zs-icon>
                        <zs-input
                                icon="circle-cross"
                                type="number"
                                v-model="finalData.materialCost"
                                placeholder="请输入附加费用"></zs-input>
                    </li>
                    <li>
                        <zs-icon
                                class="label-icon"
                                icon="减"
                                size="18"
                                icon-dis="8"
                                icon-background="#2cc068"
                                text="商家扣减"></zs-icon>
                        <zs-input
                                icon="circle-cross"
                                type="number"
                                v-model="finalData.businessDeduct"
                                placeholder="请输入商家扣减"></zs-input>
                    </li>
                    <li class="member-balance" v-if="memberBalanceDeduct > 0">
                        <div class="p-1">
                            <zs-icon
                                    class="label-icon"
                                    icon="会"
                                    size="18"
                                    icon-dis="8"
                                    icon-background="#4b8dde"
                                    text="会员余额支付"></zs-icon>
                            - {{ useBalance ? Math.min(parseFloat(memberBalanceDeduct),
                            parseFloat(totalPriceBeforeBalance ||
                            0)).toFixed(2) : 0 }}
                            <zs-switch v-model="useBalance"></zs-switch>
                        </div>
                        <p class="p-2">(余额：{{ parseFloat(memberBalanceDeduct).toFixed(2) }})</p>
                    </li>
                    <li>
                        <span class="label-icon">备注</span>
                        <zs-input
                                icon="circle-cross"
                                v-model="finalData.comment"
                                placeholder="请输入工单备注"></zs-input>
                    </li>
                </ul>
                <div class="workorder-page-footer">
                    <span class="total-price">合计<i>￥{{ totalPrice }}</i></span>
                    <p class="control return-control" @click="nextPageVisible=false">上一步</p>
                    <p class="control" @click="updateWorkOrder">保存工单</p>
                </div>
            </div>
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
        </zs-slide-page>
    </div>
</template>
<script>
    import {
        turnToNextPage,
        getDataFromParam,
        isEmpty,
        isCarNum,
        isMobile
    } from '../../../../../assets/js/utils';
    import { defaultInfo } from '../../../../../assets/js/defaultInfo';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import carNumber from '../../../../../components_proj/car_number/app.vue';

    export default {
        components: {
            maHead,
            carNumber
        },
        data() {
            return {
                isLoading: true,
                isEdit: false,
                nextPageVisible: false,
                isOperator: '',
                operatorId: '',
                operatorName: '',
                operatorItemType: '',
                //默认车牌
                currCarArea: '',
                currCarLetter: '',
                currCarNum: '',
                //会员相关
                memberRecordId: '',
                memberLvl: '',
                packageRemain: {},
                remarks: '',
                useBalance: true,
                memberBalanceDeduct: 0,
                //商家项目相关
                itemTypes: [],
                itemType: '',
                itemDatas: [],
                //已选项目列表
                itemSelectDatas: [],
                //记录当前工单中所有包括管理员和操作员已创建的项目的serviceItemId
                totalSelectItemId: [],
                //创建或编辑工单
                finalData: {
                    id: '',
                    totalPrice: '',
                    carNumber: '',
                    workOrderDetails: [],
                    userId: '',
                    user: {
                        realName: ''
                    },
                    state: 1,
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
                //客户搜索
                searchPage: false,
                memberData: [],
                hasNextPage: false,
                memberPara: {
                    keyWord: '',
                    startPage: 1,
                    pageSize: 10
                }
            };
        },
        computed: {
            //项目总价
            itemTotalPrice() {
                let price = 0;

                this.itemSelectDatas.forEach((item) => {
                    price += item.itemsTimes * item.salePrice;
                });
                return parseFloat(isEmpty(price) ? 0 : price).toFixed(2);
            },
            //计算后的项目总价
            itemCalcTotalPrice() {
                let price = 0;

                this.itemSelectDatas.forEach((item) => {
                    price += parseFloat(item.totalPrice);
                });
                return parseFloat(price).toFixed(2);
            },
            //商家扣减前的价格
            priceBeforeBusinessDeduct() {
                return parseFloat(this.itemCalcTotalPrice) + parseFloat(this.finalData.materialCost || 0);
            },
            //使用余额前的总价格
            totalPriceBeforeBalance() {
                let price = this.priceBeforeBusinessDeduct - (this.finalData.businessDeduct || 0);

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            },
            //总价
            totalPrice() {
                let price = this.totalPriceBeforeBalance - (this.useBalance ? this.memberBalanceDeduct : 0);

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            }

        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 提交工单
             */
            updateWorkOrder() {
                this.itemSelectDatas.forEach((item) => {
                    !item._useCard && (item.usePackageTimes = 0);
                });
                this.finalData.workOrderDetails = this.itemSelectDatas;
                this.finalData.totalPrice = parseFloat(this.totalPrice);
                this.finalData.businessDeduct = Math.min(this.priceBeforeBusinessDeduct, parseFloat(this.finalData.businessDeduct || 0)).toFixed(2);
                this.finalData.materialCost = parseFloat(this.finalData.materialCost || 0).toFixed(2);
                this.finalData.balanceDeduct = this.useBalance ? Math.min(parseFloat(this.memberBalanceDeduct), parseFloat(this.totalPriceBeforeBalance)).toFixed(2) : 0;
                if (!isCarNum(this.finalData.carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '车牌号码有误!'
                    });
                } else if (!isEmpty(this.finalData.mobile) && !isMobile(this.finalData.mobile)) {
                    this.$message({
                        type: 'error',
                        message: '请输入正确的手机号码!'
                    });
                } else {
                    this.$ajax(
                        this.$joggle.merchant.workorder.updateWorkOrder,
                        this.finalData,
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: data.msg
                                });
                                setTimeout(() => {
                                    turnToNextPage(`/wap/merchant/home/workorder/list.html${this.isOperator ? '#eyJ0YWIiOjF9' : '#eyJ0YWIiOjJ9'}`);
                                }, 1200);
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        }
                    );
                }
            },
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

            /**
             * 获取车牌默认所属地区
             * @param fn
             * @param loading
             */
            selectDefaultCarArea(fn, loading) {
                this.$ajax(
                    this.$joggle.business.selectBusinessInfo,
                    {},
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (!isEmpty(data.data)) {
                                const defaultCar = data.data.defaultCar;

                                this.currCarArea = defaultCar ? defaultCar[0] : '闽';
                                this.currCarLetter = defaultCar ? defaultCar[1] : 'D';
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                        fn && fn();
                    },
                    () => {
                        fn && fn();
                    }
                );
            },
            /**
             * 根据工单id/车牌获取工单信息
             * @param fn
             * @param loading
             * @param id
             */
            selectWorkOrder(fn, loading, id) {
                this.$ajax(
                    this.$joggle.merchant.workorder[isEmpty(id) ? 'selectWorkOrderByCarNumber' : 'selectWorkOrderById'],
                    isEmpty(id) ? { carNumber: this.finalData.carNumber } : { orderId: id },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            const workOrder = data.data.workOrder;
                            const user = data.data.user;
                            const userMobile = isEmpty(user) ? '' : user.mobile;

                            this.totalSelectItemId = this.isOperator ? data.data.serviceIdList || [] : [];
                            //套餐信息
                            this.packageRemain = this.handlePackageMerge(isEmpty(user) ? [] : user.packageList);
                            //工单信息
                            if (!isEmpty(id)) {
                                if (isEmpty(workOrder)) {
                                    this.$message({
                                        type: 'error',
                                        message: '工单不存在！'
                                    });
                                    return;
                                }
                                this.finalData.carNumber = isEmpty(workOrder) ? '' : workOrder.carNumber;
                            }
                            this.finalData.id = isEmpty(workOrder) ? '' : workOrder.id;
                            this.finalData.mobile = isEmpty(workOrder) ? userMobile : workOrder.mobile || '';
                            this.finalData.businessDeduct = isEmpty(workOrder) ? '' : workOrder.businessDeduct || '';
                            this.finalData.materialCost = isEmpty(workOrder) ? '' : workOrder.materialCost || '';
                            this.finalData.comment = isEmpty(workOrder) ? '' : workOrder.comment || '';

                            //会员信息
                            this.finalData.user.realName = isEmpty(user) ? '' : user.realName;
                            this.finalData.userId = isEmpty(user) ? '' : user.userId || '';
                            this.memberRecordId = isEmpty(user) ? '' : user.recordId || '';
                            this.remarks = isEmpty(user) ? '' : user.remarks || '';
                            this.memberLvl = isEmpty(this.memberRecordId) ? '非会员' : user.lvlName || '普通会员';
                            //会员余额
                            this.memberBalanceDeduct = isEmpty(user) ? 0 : user.memberBalance || 0;

                            const f0 = new Promise((resolve) => {
                                this.selectCouponsByCarNumber(() => {
                                    //工单项目

                                    const itemSelectTemps = isEmpty(workOrder) ? [] : workOrder.workOrderDetails || [];

                                    //将已选项目记录到列表中
                                    this.itemDatas.forEach((item) => {
                                        let setZero = true;

                                        for (let i = 0; i < itemSelectTemps.length; i++) {
                                            if (itemSelectTemps[i].serviceItemId == item.id) {
                                                this.$set(item, 'itemsTimes', itemSelectTemps[i].itemsTimes);
                                                setZero = false;
                                                break;
                                            }
                                        }
                                        if (setZero) {
                                            this.$set(item, 'itemsTimes', 0);
                                        }
                                    });
                                    //设置已选项目的套餐使用情况，和优惠券使用情况
                                    itemSelectTemps.forEach((it) => {
                                        it._hasCard = it._useCard = false;
                                        if (isEmpty(it.itemType) || isEmpty(it.serviceItemId)) {
                                            it.itemType = -1;
                                        }
                                        //需要编辑的项目暂时不自动带出优惠券
                                        it._autoCoupon = false;
                                        //设置套餐使用情况
                                        for (let id in this.packageRemain) {
                                            if (this.packageRemain[id] > 0 && id == it.serviceItemId) {
                                                it._hasCard = true;
                                                it._useCard = parseInt(it.usePackageTimes || 0) > 0;
                                                it.usePackageTimes = Math.min(it.itemsTimes, this.packageRemain[id]);
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
                                    this.itemSelectDatas = itemSelectTemps;
                                    resolve();
                                }, loading);
                            });

                            //获取包含用户优惠数据的项目信息
                            const f1 = new Promise((resolve) => {
                                this.selectServiceItem(() => {
                                    resolve();
                                }, loading);
                            });

                            Promise.all([f0, f1]).then(() => {
                                fn && fn();
                            });

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
                        this.itemDatas.forEach((item) => {
                            this.$set(item, 'itemsTimes', 0);
                        });
                        this.memberLvl = '';
                        this.packageRemain = {};
                    }
                );
            },
            /**
             * 获取服务项类别
             * @param fn
             * @param loading
             */
            selectItemType(fn, loading) {
                this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    { codeTypes: 'ServiceItemType' },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const temp = data.data.ServiceItemType;

                            temp.unshift(['', '全部']);
                            this.itemTypes = temp;
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
            selectServiceItem(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.workorder.selectServiceItemByPage,
                    {
                        itemType: this.itemType,
                        startPage: 1,
                        pageSize: 999,
                        userId: this.finalData.userId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const temp = data.data.list;

                            temp.forEach((item) => {
                                let setZero = true;

                                for (let i = 0; i < this.itemSelectDatas.length; i++) {
                                    if (this.itemSelectDatas[i].serviceItemId == item.id) {
                                        this.$set(item, 'itemsTimes', this.itemSelectDatas[i].itemsTimes);
                                        setZero = false;
                                        break;
                                    }
                                }
                                if (setZero) {
                                    this.$set(item, 'itemsTimes', 0);
                                }
                            });
                            this.itemDatas = temp;
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
             * 通过车牌获取可用优惠券
             * @param fn
             * @param loading
             */
            selectCouponsByCarNumber(fn, loading) {
                if (this.isOperator) {
                    fn && fn();
                    return;
                }
                this.$ajax(
                    this.$joggle.merchant.workorder.selectCouponsByCarNumber,
                    { carNumber: this.finalData.carNumber },
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
             * 根据输入的内容搜索对应客户信息
             * @param isShowMore
             */
            selectMemberData(isShowMore) {
                this.memberPara.carSelect = `${this.currCarArea}${this.currCarLetter}`;
                if (isShowMore) {
                    this.memberPara.startPage++;
                } else {
                    this.memberPara.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.workorder.selectMemberByCarNumber,
                    this.memberPara,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.hasNextPage = data.data.hasNextPage;
                            this.memberData = isShowMore ? this.memberData.concat(data.data.list) : data.data.list;
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
             * 跳转至会员详情
             */
            handleToMemberDetail() {
                if (!isEmpty(this.memberRecordId) && !this.isOperator) {
                    window.localStorage.wo = JSON.stringify(this.$data);
                    setTimeout(() => {
                        turnToNextPage('/wap/merchant/home/member/detail.html', { id: this.memberRecordId });
                    }, 100);
                }
            },
            /**
             * 返回首页
             */
            handleGoHome() {
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 输入或获取车牌号码
             * @param carNumber 传递出来的车牌号码
             */
            handleGetCarNumber(carNumber) {
                this.finalData.carNumber = carNumber;
                if (carNumber.length < 7) return;
                if (!isCarNum(carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '车牌号码有误!'
                    });
                } else if (isEmpty(window.localStorage.wo)) {
                    const loading = this.$loading();

                    this.selectWorkOrder(() => {
                        loading.close();
                    }, loading);
                } else {
                    window.localStorage.wo = '';
                }
            },
            /**
             * 合并套餐
             * @param arr 从后台获取的套餐数据
             * @returns {{1: number}}
             */
            handlePackageMerge(arr) {
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
             * 通过服务项目类别先择项目列表操作
             * @param type 点击的项目类别
             */
            handleSelectItemType(type) {
                this.itemType = type;
                const loading = this.$loading();

                this.selectServiceItem(() => {
                    loading.close();
                }, loading);
            },
            /**
             * 操作项目次数变更
             * @param item 当前项目
             * @param times 变更后的次数
             */
            handleItemTimesChange(item, times) {
                let canInsert = true;
                const usePackageTimes = Math.min(times, this.packageRemain[item.id] || 0);

                for (let i = 0; i < this.itemSelectDatas.length; i++) {
                    let it = this.itemSelectDatas[i];

                    if (it.serviceItemId === item.id) {
                        if (times === 0) {
                            for (let j = 0; j < this.couponsData.length; j++) {
                                if (this.couponsData[j].couponKey === this.itemSelectDatas[i].couponKey) {
                                    this.$set(this.couponsData[j], 'couponKey', '');
                                    break;
                                }
                            }
                            this.$delete(this.itemSelectDatas, i);
                        } else {
                            it.itemsTimes = times;
                            it.usePackageTimes = usePackageTimes;
                            const coupon = this.handleChooseMaxCoupon(it, it.couponKey);

                            if (!isEmpty(coupon.takeUuid) && it._autoCoupon) {
                                this.$set(it, 'couponUuid', coupon.takeUuid);
                                this.$set(it, 'couponName', coupon.coupon.cpName);
                                this.$set(it, 'couponDeduct', coupon.coupon.cpMoney);
                                this.$set(it, '_useCard', false);
                            } else {
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
                        }
                        canInsert = false;
                        break;
                    }
                }
                if (canInsert) {
                    const hasCard = this.packageRemain[item.id] && this.packageRemain[item.id] > 0;
                    const couponKey = this.couponKey;

                    item._autoCoupon = true;
                    const coupon = this.handleChooseMaxCoupon(item, couponKey);
                    const insertItem = {
                        id: '',
                        serviceItemId: item.id,
                        serviceItemName: item.itemName,
                        itemsTimes: times,
                        usePackageTimes,
                        salePrice: item.salePrice,
                        workerId: this.isOperator ? this.operatorId : '',
                        salerId: this.isOperator ? this.operatorId : '',
                        workerName: this.isOperator ? this.operatorName : '',
                        salerName: this.isOperator ? this.operatorName : '',
                        itemType: item.itemType,
                        discountType: item.discountType || '',
                        discountPrice: 0,
                        discountNumber: item.discountNumber || (item.discountType == 1 ? 10 : 0),
                        couponKey,
                        couponUuid: coupon.takeUuid || '',
                        couponName: coupon.coupon ? coupon.coupon.cpName || '' : '',
                        couponDeduct: coupon.coupon ? coupon.coupon.cpMoney || 0 : 0,
                        totalPrice: 0,
                        _autoCoupon: !this.isOperator,
                        _hasCard: hasCard,
                        _useCard: hasCard && isEmpty(coupon.takeUuid)

                    };

                    this.itemSelectDatas.push(insertItem);
                    this.couponKey++;
                }
            },
            /**
             * 已有员工创建该项目的提醒
             * @param item 当前点击的项目数据
             */
            handleItemTimesNotice(item) {
                if (this.totalSelectItemId.indexOf(item.id) > -1) {
                    this.$message({
                        type: 'error',
                        message: '已有员工创建该项目'
                    });
                }
            },
            /**
             * 点击下一步
             */
            handleNext() {
                if (this.itemSelectDatas.length === 0) return;
                if (this.isOperator) {
                    this.updateWorkOrder();
                } else if (!isCarNum(this.finalData.carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '车牌号码有误！'
                    });
                } else {
                    this.nextPageVisible = true;
                }
            },
            /**
             * 切换是否使用套餐
             * @param index 当前项目的索引
             */
            handleToggleUseCard(index) {
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
             * 计算项目会员折扣（包含打折和会员价）
             * @param item 当前项目
             * @param index 当前项目索引
             * @returns {string}
             */
            handleCalcDiscountPrice(item, index) {
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
            handleCalcItemTotalPrice(item, index) {
                const totalPrice = parseFloat((item.itemsTimes - (item._useCard ? item.usePackageTimes : 0)) * item.salePrice - (item.couponDeduct || 0) - item.discountPrice).toFixed(2);

                this.$set(this.itemSelectDatas[index], 'totalPrice', parseFloat(totalPrice));
                return totalPrice;
            },
            /**
             * 筛选出能用的最大面额的优惠券
             * @param item 当前项目
             * @param couponKey 当前项目的卡券标识
             * @returns {*}
             */
            handleChooseMaxCoupon(item, couponKey) {
                let itemType = item.itemType;
                let itemPrice = item.salePrice * (item.itemsTimes || 1);
                let maxCoupon = {
                    coupon: {
                        cpMoney: 0
                    }
                };
                let maxCouponIndex = -1;

                this.couponsData.forEach((coupon, index) => {
                    if (
                        item._autoCoupon &&
                        (isEmpty(coupon.couponKey) || coupon.couponKey === item.couponKey) &&
                        (!coupon.coupon.itemType || itemType == coupon.coupon.itemType) &&
                        coupon.coupon.cpMoney - itemPrice <= 0 &&
                        coupon.coupon.cpMoney - maxCoupon.coupon.cpMoney >= 0) {
                        maxCoupon = coupon;
                        maxCouponIndex = index;
                    }
                });
                if (maxCouponIndex > -1) {
                    this.$set(this.couponsData[maxCouponIndex], 'couponKey', couponKey);
                    return this.couponsData[maxCouponIndex];
                }
                return {};
            },
            /**
             * 打开卡券选择弹窗
             * @param itemType 当前项目类型
             * @param index 当前项目索引
             */
            handlePopCoupon(itemType, index) {

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
             * @param c 当前优惠券数据
             * @param index 当前项目索引
             * @returns {boolean}
             */
            handleCouponListShow(c, index) {
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
             * @param uuid 优惠券uuid
             */
            handleSelectCoupon(uuid) {
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
             * @param item
             * @param index
             * @returns {string}
             */
            handleCouponShow(item, index) {
                const temp = [];

                this.couponsData.forEach((coupon) => {
                    if (this.handleCouponListShow(coupon, index)) {
                        temp.push(coupon);
                    }
                });
                return temp.length === 0 ? '暂无可用卡券' : item.couponName ? `-￥${item.couponDeduct}` : '请选择卡券';
            },
            /**
             * 卡券栏位显示文字
             * @param flag
             * @returns {string}
             */
            handSetBackgroundClass(flag){
                if (flag) {
                    return 'list-p-wx-img';
                }
                return '';
            },
            //显示更多
            handleShowMore() {
                if (this.hasNextPage) {
                    this.selectMemberData(true);
                }
            },
            /**
             *   选择搜素中的数据
             * @param data
             */
            handSelectMemberData(data){
                let carNumber = data.carNumber;

                this.currCarArea = carNumber[0];
                this.currCarLetter = carNumber[1];
                this.currCarNum = carNumber.slice(2);
                this.finalData.carNumber = carNumber;
                this.searchPage = false;

            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        mounted() {
            if (window.localStorage.wo) {
                for (let key in this.$data) {
                    this[key] = JSON.parse(window.localStorage.wo)[key];
                }
                this.currCarArea = this.finalData.carNumber[0];
                this.currCarLetter = this.finalData.carNumber[1];
                this.currCarNum = this.finalData.carNumber.slice(2);

                this.isLoading = false;
                return;
            }
            defaultInfo((info) => {
                this.isOperator = info.mercharType ? info.mercharType.indexOf('operator') > -1 : false;
                this.operatorId = info.id || '';
                this.operatorItemType = info.itemTypes || '1,2,3,4,5';
                this.operatorName = info.userName || '';
            });
            this.finalData.id = getDataFromParam('id') || '';
            this.isEdit = !isEmpty(this.finalData.id);
            const loading = this.$loading();
            const f0 = new Promise((resolve) => {
                this.selectItemType(() => {
                    resolve();
                }, loading);
            });
            const f1 = new Promise((resolve) => {
                this.selectServiceItem(() => {
                    if (isEmpty(this.finalData.id)) {
                        resolve();
                    } else {
                        this.selectWorkOrder(() => {
                            resolve();
                        }, loading, this.finalData.id);
                    }
                }, loading);
            });
            const f2 = new Promise((resolve) => {
                if (this.isEdit) {
                    resolve();
                } else {
                    this.selectDefaultCarArea(() => {
                        resolve();
                    }, loading);
                }
            });

            Promise.all([f0, f1, f2]).then(() => {
                loading.close();
                this.isLoading = false;
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
