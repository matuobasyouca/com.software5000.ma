<template>
    <div class="page-main billing">
        <div class="list">
            <div class="list-item">
                <div class="list-item-text">手机号</div>
                <div class="list-item-con">
                    {{orderData.user.mobile || '&nbsp;'}}
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
                <div class="list-item-text">购买套餐</div>
                <div class="list-item-con">
                    <div class="package-box curr" :class="{'more-item' :  isShowMore}">
                        <div class="package-box-top">
                            <p class="package-box-top-left">{{orderData.businessPackage.packageName}}<span>￥{{orderData.businessPackage.salePrice}}</span></p>
                            <p class="package-box-top-right">有效期：{{orderData.businessPackage.validTerm!=0 ? orderData.businessPackage.validTerm+'年' : '永久'}}</p>
                        </div>
                        <ul class="package-box-bottom">
                            <li v-for="(item2,index2) in orderData.businessPackage.packageAndItems" :key="index2">
                                <p class="package-box-bottom-item">{{item2.serviceItem.itemName}}<span class="r">{{item2.useTimes}}次</span></p>
                            </li>
                            <li class="package-box-more" @click.stop="packageShowMore" v-if="orderData.businessPackage.packageAndItems.length>3">
                                <zs-icon rightIcon="arrow-bottom" :text="isShowMore ? '收起' : '查看更多'" :size="8"></zs-icon>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="package-box-Mask" v-show="packageMask" @click="hideMask" ></div>
            <div class="list-item" v-if="orderData.businessPackageName">
                <div class="list-item-text">提成人员</div>
                <div class="list-item-con">
                    {{orderData.businessPackageName || '&nbsp;'}}
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">订单编号</div>
                <div class="list-item-con">
                    {{orderData.orderNumber}}
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">下单时间</div>
                <div class="list-item-con">
                    {{orderData.createTime}}
                </div>
            </div>
            <div class="list-item" v-if="orderData.state==2">
                <div class="list-item-text">结算时间</div>
                <div class="list-item-con">
                    {{orderData.updateTime}}
                </div>
            </div>
            <div class="page-main-button" v-if="orderData.state==1">
                <zs-button type="cancel" @click="cancelOrder"><zs-icon icon="draft2" text="取消订单" :size="22"></zs-icon></zs-button>
                <zs-button type="primary" @click="cashPayDialog=true"><zs-icon icon="cash" text="现金收款" :size="22"></zs-icon></zs-button>
                <zs-button type="success" @click="openWeChatPayDialog"><zs-icon icon="wechat" text="微信收款" :size="22"></zs-icon></zs-button>
            </div>
            <div class="page-main-pay" v-if="orderData.state==2">
                <zs-icon :icon="orderData.payType==1 ? 'money' : 'wechat2'" :size="20" :text="orderData.payType==1 ? '现金收款' : '微信收款'"></zs-icon><span class="bold">￥{{orderData.totalAmount}}</span>
            </div>

        </div>

        <!--现金收款弹窗-->
        <zs-dialog class="cash-pay-dialog" v-model="cashPayDialog">
            <span slot="title">现金收款</span>
            <div>
                <p class="money-pay">￥{{orderData.totalAmount}}</p>
                <p>请收到现金后点击“确认收款”</p>
            </div>
            <span slot="footer" class="dialog-footer">
                <zs-button type="cancel" @click="cashPayDialog=false">取消</zs-button>
                <zs-button type="primary" @click="cashPay">确认收款</zs-button>
            </span>
        </zs-dialog>
        <!--微信收款弹窗-->
        <zs-dialog class="weChat-Pay-dialog" v-model="weChatPayDialog">
            <span slot="title">微信收款</span>
            <div>
                <p class="money-pay">￥{{orderData.totalAmount}}</p>
                <div class="pay-code" ref="barCodeWrap">
                    <img class="pay-pop-loading"  src="../../../../../../../assets/img/three-dots.svg">
                </div>
            </div>
        </zs-dialog>
    </div>
</template>

<script>
    import {isEmpty,getDataFromParam,turnToNextPage,imgPreLoad } from '../../../../../../../assets/js/utils';
    export default {
        name: 'billing',
        data(){
            return {
                id : '',
                orderData : {
                    businessPackage : {
                        packageName : '',
                        packageAndItems : []
                    },
                    user : {
                        mobile : '',
                        realName : '',
                        cars : []
                    }
                },
                isShowMore : false,
                packageMask : false,
                orderTimer : '',
                cashPayDialog : false,
                weChatPayDialog : false
            }
        },
        computed: {

        },
        methods: {
            getData(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageOrderById,
                    {
                        id : this.id
                    },
                    true,
                    (data,model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            if(data.data){
                                this.orderData=data.data;
                            }else{
                                this.$message({
                                    type: 'error',
                                    message: '该订单不存在',
                                    duration: 1200
                                })
                                setTimeout(()=>{history.go(-1)},1200);
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                )
            },

            //购买套餐查看更多
            packageShowMore(){
                this.isShowMore=!this.isShowMore;
                this.packageMask=this.isShowMore;
            },
            hideMask(){
                this.isShowMore=false;
                this.packageMask=false;
            },

            //取消订单
            cancelOrder(){
                this.$confirm({
                    type:'draft3',
                    title: '取消订单',
                    showClose: true,
                    message:'确定取消该套餐订单？'
                }).then((action) => {
                    this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackageOrderCancle,
                    {
                        "businessPackageOrderId":this.id
                    },
                    true,
                    (data,loading) => {
                        if (data.code === 'ZS011000') {
                        this.$message({
                                type: 'success',
                                message: data.msg
                            });
                            setTimeout(()=>{
                                turnToNextPage('/web/merchant/home/member/buy_package.html');
                            },1200);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                    )
                })
            },
            //现金支付
            cashPay(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackageOrderState,
                    {
                        "id":this.id
                    },
                    true,
                    (data,loading) => {
                        if (data.code === 'ZS011000') {
                            this.getData();
                            this.cashPayDialog=false;
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
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
            //打开微信支付
            openWeChatPayDialog(){
                this.weChatPayDialog=true;
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
                                this.weChatPayDialog = false;
                                clearInterval(this.orderTimer);
                                this.$message({
                                    type: 'success',
                                    message: '支付成功',
                                    duration: 1200
                                })
                            })
                        }, 2000)
                    })
            },
            //验证是否支付完成
            isPaySuccess(fn, fnError){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageOrderById,
                    {
                        id : this.id
                    },
                    false,
                    (data,model) => {
//                        model.close();
                        if (data.code === 'ZS011000') {
                            if (data.data.state == 2) {
                                this.orderData=data.data;
                                fn && fn()
                            } else {
                                fnError && fnError()
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                )
            },
        },
        created(){
            this.id=getDataFromParam('id');
            this.getData();
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
