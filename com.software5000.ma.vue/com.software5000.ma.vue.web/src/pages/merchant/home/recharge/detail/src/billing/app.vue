<template>
    <div class="page-main billing">
        <div class="list">
            <div class="list-item">
                <div class="list-item-text">手机号</div>
                <div class="list-item-con">
                    {{orderData.mobile || '&nbsp;'}}
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">姓名</div>
                <div class="list-item-con">
                    {{orderData.user.realName || '&nbsp;'}}
                </div>
            </div>
            <div class="list-item list-item-cars">
                <div class="list-item-text">绑定车牌</div>
                <div class="member-cars">
                    <div class="car-num" v-for="(item,index) in orderData.user.cars" :key="index">
                        <p>{{item.carNumber.slice(0,2)}}<span class="spot">·</span>{{item.carNumber.slice(2)}}</p>
                    </div>
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">充值金额</div>
                <div class="list-item-con">
                    {{orderData.reChargeMoney || '&nbsp;'}}
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">赠送金额</div>
                <div class="list-item-con">
                    {{orderData.grantMoney || 0 }}
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">下单时间</div>
                <div class="list-item-con">
                    {{orderData.createTime}}
                </div>
            </div>
            <div class="list-item" v-if="orderData.state==2">
                <div class="list-item-text">收款时间</div>
                <div class="list-item-con">
                    {{orderData.updateTime}}
                </div>
            </div>
            <div class="list-item list-item-last">
                <div class="list-item-text">订单编号</div>
                <div class="list-item-con">
                    {{orderData.orderNumber}}
                </div>
            </div>
            <div class="page-main-button" v-if="orderData.state==1">
                <zs-button type="primary" @click="cashPayDialog=true">
                    <zs-icon icon="cash" text="现金收款" :size="22"></zs-icon>
                </zs-button>
                <zs-button type="success" @click="handleWeChatPayDialog">
                    <zs-icon icon="wechat" text="微信收款" :size="22"></zs-icon>
                </zs-button>
            </div>
            <div class="page-main-pay" v-if="orderData.state==2">
                <zs-icon :icon="orderData.payType==1 ? 'money' : 'wechat2'" :size="20"
                         :text="orderData.payType==1 ? '现金收款' : '微信收款'"></zs-icon>
                <span class="bold">￥{{orderData.reChargeMoney}}</span>
            </div>

        </div>

        <!--现金收款弹窗-->
        <zs-dialog class="cash-pay-dialog" v-model="cashPayDialog">
            <span slot="title">现金收款</span>
            <div>
                <p class="money-pay">￥{{orderData.reChargeMoney}}</p>
                <p>请收到现金后点击“确认收款”</p>
            </div>
            <span slot="footer" class="dialog-footer">
                <zs-button type="cancel" @click="cashPayDialog=false">取消</zs-button>
                <zs-button type="primary" @click="updateCashPay">确认收款</zs-button>
            </span>
        </zs-dialog>
        <!--微信收款弹窗-->
        <zs-dialog class="weChat-Pay-dialog" v-model="weChatPayDialog">
            <span slot="title">微信收款</span>
            <div>
                <p class="money-pay">￥{{orderData.reChargeMoney}}</p>
                <div class="pay-code" ref="barCodeWrap">
                    <img class="pay-pop-loading" src="../../../../../../../assets/img/three-dots.svg">
                </div>
            </div>
        </zs-dialog>
    </div>
</template>

<script>
    import { getDataFromParam, imgPreLoad } from '../../../../../../../assets/js/utils';

    export default {
        name: 'billing',
        data() {
            return {
                id: '',
                orderData: {
                    businessPackage: {
                        packageName: '',
                        packageAndItems: []
                    },
                    user: {
                        mobile: '',
                        realName: '',
                        cars: []
                    }
                },
                orderTimer: '',
                cashPayDialog: false,
                weChatPayDialog: false
            };
        },
        watch : {
            weChatPayDialog(val){
                if(!val){
                    clearInterval(this.orderTimer);
                }
            }
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/

            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/
            //现金支付
            updateCashPay() {
                this.$ajax(
                    this.$joggle.merchant.member.updateRechargeOrderState,
                    {
                        id: this.id
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.selectData();
                            this.cashPayDialog = false;
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
            },
            
            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/
            /**
             *  根据id查找订单详情
             */
            selectData() {
                this.$ajax(
                    this.$joggle.merchant.member.selectRechargeOrderById,
                    {
                        id: this.id
                    },
                    true,
                    (data, model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            if (data.data) {
                                this.orderData = data.data;
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
            /**
             *  生成微信支付二维码
             */
            selectBarCode() {
                imgPreLoad(
                    `${this.$joggle.merchant.member.selectRechargeOrderDeskPay}?rechargeOrderId=${this.id}&t=${new Date().getTime()}`,
                    (img) => {
                        this.$refs.barCodeWrap.innerHTML = '';
                        this.$refs.barCodeWrap.appendChild(img);

                        clearInterval(this.orderTimer);

                        this.orderTimer = setInterval(() => {
                            this.selectIsPaySuccess(() => {
                                this.weChatPayDialog = false;
                                clearInterval(this.orderTimer);
                                this.$message({
                                    type: 'success',
                                    message: '支付成功',
                                    duration: 1200
                                });
                            });
                        }, 2000);
                    });
            },
            /**
             *  验证是否支付完成
             * @param fn  后续操作
             * @param fnError  发生错误时的后续操作
             */
            selectIsPaySuccess(fn, fnError) {
                this.$ajax(
                    this.$joggle.merchant.member.selectRechargeOrderById,
                    {
                        id: this.id
                    },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (data.data.state == 2) {
                                this.orderData = data.data;
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
            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             *  打开微信支付
             */
            handleWeChatPayDialog() {
                this.weChatPayDialog = true;
                this.selectBarCode();
            }

            /* ------------------- handle (操) end -------------------*/


        },
        created() {
            this.id = getDataFromParam('id');
            this.selectData();
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
