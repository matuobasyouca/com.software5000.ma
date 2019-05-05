<template>
    <div class="cashier-page" v-show="!isLoading">
        <ma-head :left-icon="showGoBack" :home="false">{{ orderTitle }}</ma-head>
        <div class="cashier-info">
            <p class="cashier-item">应付款 <span>￥{{ totalPrice/100 }}</span></p>
            <p class="cashier-item">服务信息 <span>{{ orderService }}</span></p>
        </div>
        <div class="cashier-detail" v-if="orderDetails.length > 0">
            <h3 class="cashier-block-title">明细</h3>
            <ul class="cashier-detail-list">
                <li class="cashier-item" v-for="d in orderDetails">
                    {{ d.dk }} <span>{{ d.dv }}</span>
                </li>
            </ul>
        </div>
        <div class="cashier-instruction" v-if="orderContent">
            <h3 class="cashier-block-title">说明</h3>
            <p class="cashier-instruction-main" v-html="orderContent"></p>
        </div>
        <div class="cashier-button" @click="handlePay">微信支付</div>
    </div>
</template>

<script type="text/ecmascript-6">
    import maHead from '../../../../components_proj/ma_head/app.vue';
    import { getDataFromParam, isEmpty, turnToNextPage, parseJSON } from '../../../../assets/js/utils';
    import { turnToHostPage } from '../../../../assets/js/turnToHostPage';
    import { selectWxCode, selectWxCodeAgain } from '../../../../assets/js/wxUtils'
    export default{
        components: {
            maHead
        },
        data(){
            return {
                isLoading: true,
                payOrderId: '',
                code: '',
                //订单信息
                orderTitle: '',
                totalPrice: '',
                orderService: '',
                orderDetails: '',
                orderContent: '',
                orderNo: '',
                //微信支付参数
                appId: '',
                timeStamp: '',
                nonceStr: '',
                packagevar: '',
                paySign: '',
                subscribe: 0,
                nextUrl: '',//支付成功后的跳转页面
                canPay: false//控制能否点击支付按钮
            }
        },
        computed: {
            showGoBack(){
                return this.orderTitle === '诚品套餐支付' ? 'arrow-left' : '';
            }
        },
        methods: {
            //获取微信支付参数
            selectWxParams(fn, loading){
                this.$ajax(
                    this.$joggle.customer.cashier.unifiedOrder,
                    { payOrderId: parseInt(this.payOrderId), code: this.code },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.appId = data.data.appId;
                            this.timeStamp = `${data.data.timeStamp}`;
                            this.nonceStr = data.data.nonceStr;
                            this.packagevar = `prepay_id=${data.data.packagevar}`;
                            this.paySign = data.data.paySign;
                            this.canPay = true;
                            this.appId && fn && fn();
                            this.subscribe = data.data.subscribe;
                        } else if (data.code === 'ZS012042') {
                            selectWxCodeAgain()
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            //获取订单信息
            selectPayOrder(fn, loading){
                this.$ajax(
                    this.$joggle.customer.cashier.selectPayOrder,
                    { payOrderId: this.payOrderId },
                    loading,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            if (isEmpty(data.data)) {
                                setTimeout(() => {
                                    turnToHostPage('/open/user_center.html', 'emkt');
                                }, 1000)
                                return;
                            }
                            this.orderTitle = data.data.orderTitle;
                            this.totalPrice = data.data.totalPrice;
                            this.orderService = data.data.orderService;
                            this.orderDetails = data.data.orderDetail ? parseJSON(data.data.orderDetail) : '';
                            this.orderContent = data.data.orderContent;
                            this.orderNo = data.data.orderNo;
                            this.nextUrl = data.data.redirectUrl;
                            fn && fn();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            //检查订单是否有效
            checkPay(fn, loading){
                this.$ajax(
                    this.$joggle.customer.cashier.checkPay,
                    { orderNo: this.orderNo },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            fn && fn();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1000
                            })
                            setTimeout(() => {
                                turnToHostPage('/open/user_center.html', 'emkt');
                            }, 1000)
                        }
                    }
                )
            },
            //支付
            handlePay(){
                if (!this.canPay)return;
                let loading = this.$loading();
                let _this = this;

                this.checkPay(() => {
                    WeixinJSBridge.invoke(
                        'getBrandWCPayRequest', {
                            appId: this.appId,
                            timeStamp: this.timeStamp,
                            nonceStr: this.nonceStr,
                            package: this.packagevar,
                            signType: 'MD5',
                            paySign: this.paySign
                        },
                        function (res) {
                            loading.close();
                            if (res.err_msg === 'get_brand_wcpay_request:ok') {
                                if (!isEmpty(_this.nextUrl)) {
                                    if (_this.nextUrl.indexOf('?') > -1) {
                                        turnToNextPage(`${_this.nextUrl}&orderNo=${this.orderNo}&subscribe=${this.subscribe}`)
                                    } else {
                                        turnToNextPage(_this.nextUrl, {
                                            orderNo: _this.orderNo,
                                            subscribe: _this.subscribe
                                        })
                                    }

                                } else if (_this.subscribe == 0) {
                                    turnToNextPage('/wap/customer/open/scan.html')
                                } else {
                                    turnToHostPage('/open/user_center.html', 'emkt')
                                }
                            } else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
                                _this.$message({
                                    type: 'error',
                                    message: '取消付款'
                                })
                            } else {
                                _this.message({
                                    type: 'error',
                                    message: '付款失败'
                                })
                            }
                        }
                    )
                }, loading)
            }
        },
        created(){
            this.payOrderId = getDataFromParam('payOrderId') || '';
            if (isEmpty(this.payOrderId)) {
                this.$message({
                    type: 'error',
                    message: '订单不存在'
                })
                setTimeout(() => {
                    turnToHostPage('/open/user_center.html', 'emkt');
                }, 1000)
                return;
            }
            selectWxCode((code) => {
                this.code = code;
                let loading = this.$loading();
                let selectPayOrder = new Promise((resolve) => {
                    this.selectPayOrder(() => {
                        resolve();
                    }, loading);
                })
                let selectWxParams = new Promise((resolve) => {
                    this.selectWxParams(() => {
                        resolve();
                    }, loading)
                })

                Promise.all([selectPayOrder, selectWxParams]).then(() => {
                    loading.close();
                    this.isLoading = false;
                })
            })
        }
    }
</script>

<style lang="less">
    @import "./style.less";
</style>