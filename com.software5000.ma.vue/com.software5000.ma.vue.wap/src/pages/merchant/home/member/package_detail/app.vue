<template>
    <div class="workorder-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">套餐订单详情</ma-head>
        <div class="workorder-second-page" :class="[{'pb10' : state == 2}]">
            <!--会员信息-->
            <div class="member-info-box">
                <ul class="member-info">
                    <li class="order-state-label">
                        <zs-icon
                                :icon="['','nopay','finish'][state]"
                                size="40"
                                icon-dis="15"
                                :text="['','待支付','已收款'][state]"></zs-icon>
                    </li>
                    <li>
                        <span class="title">手机号</span>
                        <p class="info"><span>{{ finalData.user.mobile }}</span></p>
                    </li>
                    <li>
                        <span class="title">姓名</span>
                        <p class="info"><span>{{ finalData.user.realName }}</span></p>
                    </li>
                    <li>
                        <span class="title">绑定车辆</span>
                        <p class="info text-hide">
                            {{ userCarNum }}
                        </p>
                    </li>
                </ul>
            </div>
            <!--套餐详情-->
            <div class="package-detail">
                <p class="package-detail-title">
                    <zs-icon icon="pack" :size="25" text="套餐信息"></zs-icon>
                </p>
                <div class="package-info">
                    <ul class="open-package-list">
                        <li class="item" :class="['','use-up','time-out'][currTab-1]">
                            <div class="item-head">
                                <p class="head-info">
                                    {{finalData.businessPackage.packageName}}
                                </p>
                                <p class="head-info">
                                    <span class="item-name">金额<i>￥{{ finalData.totalAmount || '' }}</i></span>
                                    <span class="item-validate">
                                        <zs-icon icon="time" icon-dis="6" class="item-merchant"
                                                 :text="finalData.businessPackage.validTerm!=0 ? '有效期：'+finalData.businessPackage.validTerm+'年' : '有效期：永久' "></zs-icon>
                                        </span>
                                </p>
                            </div>
                            <div class="service-info">
                                <p v-for="(item,index) in finalData.businessPackage.packageAndItems" :key="item.id"
                                   v-show="index < 3 || showAll">
                                    {{ item.serviceItem.itemName }}
                                    <span class="r">×{{ item.useTimes }}</span>
                                </p>
                            </div>
                            <p class="show-more-item"
                               v-show="finalData.businessPackage.packageAndItems.length>3 && !showAll"
                               @click="showAll = true">
                                查看更多</p>
                        </li>
                    </ul>
                </div>
            </div>
            <!--订单信息-->
            <div class="order-info">
                <div class="order-info-item">
                    <div class="order-info-item-left">
                        订单编号
                    </div>
                    <div class="order-info-item-right">
                        {{finalData.orderNumber}}
                    </div>
                </div>
                <div class="order-info-item">
                    <div class="order-info-item-left">
                        新建时间
                    </div>
                    <div class="order-info-item-right">
                        {{finalData.createTime}}
                    </div>
                </div>
                <div class="order-info-item" v-if="state == 2">
                    <div class="order-info-item-left">
                        收款时间
                    </div>
                    <div class="order-info-item-right">
                        {{finalData.updateTime}}
                    </div>
                </div>
            </div>
            <!--按钮-->
            <ul class="workorder-page-footer" v-if="state == 1">
                <li
                        class="left bg-white"
                        @click="handleLeftClick">取消订单
                </li>
                <li
                        class="left"
                        @click="handleMiddleClick">现金收款
                </li>
                <li
                        class="right"
                        @click="handleRightClick">微信收款
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
                    <zs-button @click="handlePayCashConfirm">确定收款</zs-button>
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
    </div>
</template>

<script>
    import {
        turnToNextPage,
        getDataFromParam,
        isEmpty,
        imgPreLoad
    } from '../../../../../assets/js/utils';
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
                id: '',
                state: '',

                isLoading: false,
                canSubmit: true,
                orderTimer: null,
                currTab: 0,
                showAll: false,

                //收款
                payCashPopVisible: false,
                payWechatPopVisible: false,
                //更新工单
                finalData: {
                    createTime: '',
                    businessPackage: {
                        packageName: '',
                        packageAndItems: []
                    },
                    user: {
                        mobile: '',
                        realName: '',
                        cars: []
                    }
                }
            };
        },
        computed: {
            //车牌号
            userCarNum(){
                const temp = [];

                for (let i = 0; i < this.finalData.user.cars.length; i++) {
                    temp.push(this.finalData.user.cars[i].carNumber);
                }
                return temp.join(',') || '';
            },
            //实际应收款
            realTotalPrice(){
                const price = this.finalData.totalAmount;

                return (price > 0 ? parseFloat(price) : 0).toFixed(2);
            }
        },
        watch: {},
        methods: {
            //获取该套餐订单详情
            getData(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageOrderById,
                    {
                        id: this.id
                    },
                    true,
                    (data, model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            if (data.data && data.data.state != 5) {
                                this.finalData = data.data;
                                this.state = data.data.state;
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: '该订单不存在',
                                    duration: 1200
                                });
                                setTimeout(() => {history.go(-1);}, 1200);
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

            //返回首页
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },

            //点击左按钮
            handleLeftClick(){
                //取消订单
                this.$confirm({
                    type: 'order',
                    message: '是否取消该订单?'
                }).then(() => {
                    this.deleteOrder();
                }).catch(() => {
                });
            },
            //点击中按钮
            handleMiddleClick(){
                //现金收款弹窗
                this.payCashPopVisible = true;
            },
            //点击右按钮
            handleRightClick(){
                this.payWechatPopVisible = true;
                this.showBarCode();
            },
            //生成微信支付二维码
            showBarCode(){
                imgPreLoad(
                    `${this.$joggle.merchant.businessPackage.selectPackageOrderDeskPay}?packageOrderId=${this.id}`,
                    (img) => {
                        this.$refs.barCodeWrap.innerHTML = '';
                        this.$refs.barCodeWrap.appendChild(img);

                        clearInterval(this.orderTimer);

                        this.orderTimer = setInterval(() => {
                            this.isPaySuccess(() => {
                                this.payWechatPopVisible = false;
                                clearInterval(this.orderTimer);
                                this.getData();
                                this.$message({
                                    type: 'success',
                                    message: '支付成功',
                                    duration: 1200
                                });
                            });
                        }, 2000);
                    });
            },
            //验证是否支付完成
            isPaySuccess(fn, fnError){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageOrderById,
                    {
                        id: this.id
                    },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (data.data.state == 2) {
                                this.finalData = data.data;
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

            //取消订单
            deleteOrder(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackageOrderCancle,
                    {
                        businessPackageOrderId: this.id
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                            setTimeout(() => {
                                turnToNextPage('/wap/merchant/home/member/manage.html');
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
            //完成工单
            finishOrder(){
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
            //确认收款
            handlePayCashConfirm(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackageOrderState,
                    {
                        id: this.id
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.getData();
                            this.payCashPopVisible = false;
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            }

        },
        created(){
            this.id = getDataFromParam('id') || '';
            if (isEmpty(this.id)) {
                this.$message({
                    type: 'error',
                    message: '该套餐订单不存在'
                });
                this.canSubmit = false;
                return;
            }
            this.getData();
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
