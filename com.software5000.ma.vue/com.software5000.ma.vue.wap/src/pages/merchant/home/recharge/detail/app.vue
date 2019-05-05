<template>
    <div class="page-recharge">
        <ma-head @home-click="handleGoHome">充值详情</ma-head>
        <div class="recharge-sec-1">
            <div class="recharge-card">
                <p class="recharge-card-p1">
                    姓名<span class="user-name">{{ realName || '----' }}</span>
                    <span class="user-mobile right">{{ mobile }}</span>
                </p>
                <p class="recharge-card-p2">
                    {{ parseFloat(reChargeMoney || 0).toFixed(2) }}
                    <span class="right">{{ parseFloat(grantMoney || 0).toFixed(2) }}</span>
                </p>
                <p class="recharge-card-p3">
                    充值金额
                    <span class="right">赠送金额</span>
                </p>
                <p class="recharge-card-p4">
                    绑定车牌
                    <span class="car-number">{{ cars.join(',')}}</span>
                </p>
            </div>
        </div>
        <ul class="recharge-sec-2">
            <li class="order-info-li"><span>订单编号</span>{{ orderNumber }}</li>
            <li class="order-info-li"><span>下单时间</span>{{ createTime }}</li>
            <li class="order-info-li" v-if="state == 2"><span>收款时间</span>{{ updateTime }}</li>
        </ul>
        <ul class="recharge-sec-3" v-if="state == 1">
            <li class="cash-pay" @click="handlePayPop(1)">现金收款</li>
            <li class="wechat-pay" @click="handlePayPop(2)">微信收款</li>
        </ul>
        <!--现金收款-->
        <zs-dialog class="settle-pop" v-model="payCashPopVisible" show-close>
            <p slot="title">
                <zs-icon icon="nopay" size="24" icon-dis="8" text="现金收款"></zs-icon>
            </p>
            <p class="pay-pop-p1">￥ {{ parseFloat(reChargeMoney).toFixed(2) }}</p>
            <p class="pay-pop-p2">是否收到现金或刷卡支付?</p>
            <ul class="pay-pop-control" slot="footer">
                <li>
                    <zs-button @click="payCashPopVisible = false">取消</zs-button>
                </li>
                <li>
                    <zs-button @click="updateRechargeOrder">确定收款</zs-button>
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
            <p class="pay-pop-p1">￥ {{ parseFloat(reChargeMoney).toFixed(2) }}</p>
        </zs-dialog>
    </div>
</template>

<script>
    import { getDataFromParam, turnToNextPage, isEmpty, imgPreLoad } from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import carNumber from '../../../../../components_proj/car_number/app.vue';
    export default {
        components: {
            maHead,
            carNumber
        },
        props: {},
        data(){
            return {
                isLoading: '',
                id: '',
                state: '',
                mobile: '',
                realName: '',
                cars: [],
                reChargeMoney: '',
                grantMoney: '',
                orderNumber: '',
                createTime: '',
                updateTime: '',
                payCashPopVisible: false,
                payWechatPopVisible: false,
                orderTimer: null
            };
        },
        computed: {},
        watch: {
            payWechatPopVisible(val){
                if (!val) {
                    clearInterval(this.orderTimer);
                }
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 收款
             */
            updateRechargeOrder(){
                this.$ajax(
                    this.$joggle.merchant.member.updateRechargeOrderState,
                    { id: this.id },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.payCashPopVisible = false;
                            this.selectRechargeOrder(() => {
                                loading.close();
                                this.$message({
                                    type: 'success',
                                    message: '支付成功',
                                    duration: 1200
                                });
                            }, loading);
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
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 通过id获取充值订单
             */
            selectRechargeOrder(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.member.selectRechargeOrderById,
                    { id: this.id },
                    loading,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.state = data.data.state;
                            this.realName = data.data.user.realName;
                            this.mobile = data.data.mobile;
                            this.cars = data.data.carNumbers.split(',');
                            this.reChargeMoney = data.data.reChargeMoney;
                            this.grantMoney = data.data.grantMoney;
                            this.orderNumber = data.data.orderNumber;
                            this.createTime = data.data.createTime;
                            this.updateTime = data.data.updateTime;
                            fn && fn(data);
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
             * 判断是否支付成功
             * @param fn
             * @param fnError
             */
            selectIsPaySuccess(fn, fnError){
                this.$ajax(
                    this.$joggle.merchant.member.selectRechargeOrderById,
                    { id: this.id },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (parseInt(data.data.state) === 2) {
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
             * 跳转到首页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 支付弹窗
             * @param payType 支付方式 ： 1 - 现金； 2 - 微信
             */
            handlePayPop(payType){
                this[['', 'payCashPopVisible', 'payWechatPopVisible'][payType]] = true;
                if (payType === 2) {
                    this.handleShowBarCode();
                }
            },
            /**
             * 生成微信支付二维码
             */
            handleShowBarCode(){
                imgPreLoad(
                    `${this.$joggle.merchant.member.weChatPayBarCode}?rechargeOrderId=${this.id}&t=${new Date().getTime()}`,
                    (img) => {
                        this.$refs.barCodeWrap.innerHTML = '';
                        this.$refs.barCodeWrap.appendChild(img);

                        clearInterval(this.orderTimer);
                        this.orderTimer = setInterval(() => {
                            this.selectIsPaySuccess(() => {
                                clearInterval(this.orderTimer);
                                const loading = this.$loading();

                                this.payWechatPopVisible = false;
                                this.selectRechargeOrder(() => {
                                    loading.close();
                                    this.$message({
                                        type: 'success',
                                        message: '支付成功'
                                    });
                                }, loading);
                            });
                        }, 2000);
                    });
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.id = getDataFromParam('id');
            if (isEmpty(this.id)) {
                this.$messgae({
                    type: 'error',
                    message: '该充值订单不存在',
                    duration: 1200
                });
                return;
            }
            const loading = this.$loading();

            this.selectRechargeOrder(() => {
                loading.close();
                this.isLoading = false;
            }, loading);
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
