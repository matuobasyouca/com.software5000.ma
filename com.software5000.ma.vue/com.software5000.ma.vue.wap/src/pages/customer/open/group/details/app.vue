<template>
    <div class="page">
        <div class="page-top group-state">
            <zs-icon class="icon-back" :size="12" icon="arrow-left" @click="handGoBack"></zs-icon>
            <div class="page-top-main" :class="['','grouping','success','fail','fail'][groupData.openState || '1']">
                <div class="state-top">
                    <zs-icon
                            :icon="['','group-grouping','group-success','group-fail','group-fail'][groupData.openState || '1']"
                            :size="60"></zs-icon>
                </div>
                <div class="state-text">{{['','拼团中','拼团成功','拼团失败','拼团失败'][groupData.openState || '1']}}</div>
                <div class="state-over" v-if="groupData.openState==2">
                    {{packClusterData.clusterNum}}人团
                </div>
                <div class="state-over" v-if="groupData.openState==3 || groupData.openState==4">
                    {{packClusterData.successNum}}人参与拼团，差{{packClusterData.clusterNum-packClusterData.successNum}}人
                </div>
            </div>
            <div class="state-time" v-if="groupData.openState==1 && countdown">
                <div class="countdown">
                    <i class="red-block"><span>{{handCountdownH.substring(0,1)}}</span></i>
                    <i class="red-block"><span>{{handCountdownH.substring(1)}}</span></i>
                    <span class="maohao">:</span>
                    <i class="red-block"><span>{{handCountdownM.substring(0,1)}}</span></i>
                    <i class="red-block"><span>{{handCountdownM.substring(1)}}</span></i>
                    <span class="maohao">:</span>
                    <i class="red-block"><span>{{handCountdownS.substring(0,1)}}</span></i>
                    <i class="red-block"><span>{{handCountdownS.substring(1)}}</span></i>
                </div>
            </div>
        </div>
        <div class="package-main">
            <div class="store-name">
                <zs-icon :size="15" icon="shang3" :text="packClusterData.businessName"></zs-icon>
                <div class="r" v-if="groupData.openState==3 || groupData.openState==4">
                    {{['已退款','退款中'][groupData.openState-3 || 0]}}
                </div>
            </div>
            <div class="package">
                <div class="package-left" v-if="groupData.openState"
                     :style="`background-image: url(${packClusterData.shareImgPath ? packClusterData.shareImgPathUrl : groupData.imgSrc})`"></div>
                <div class="package-right">
                    <!--<div class="package-title">{{packClusterData.clusterName}}</div>-->
                    <div class="package-date">
                        {{packClusterData.businessPackage.packageName}}(有效期{{packClusterData.businessPackage.validTerm!=0
                        ?
                        packClusterData.businessPackage.validTerm+'年' : '永久'}})
                    </div>
                    <div class="package-price">
                        <p class="original">
                            报团价<span class="red">￥{{packClusterData.salePrice}}</span>
                        </p>
                        <p class="del">
                            原价:￥{{packClusterData.businessPackage.salePrice}}
                        </p>
                    </div>
                </div>

            </div>
            <div class="package-item">
                <div class="package-item-table clr">
                    <div class="package-item-table-td"
                         v-for="(item,index) in packClusterData.businessPackage.packageAndItems" :key="index">
                        <div class="item-table-td-left">
                            {{item.serviceItem.itemName}}
                        </div>
                        <div class="item-table-td-right">
                            {{item.useTimes}}
                        </div>
                    </div>
                    <div class="package-item-table-td"
                         v-if="packClusterData.businessPackage.packageAndItems.length%2==1">
                        <div class="item-table-td-left">
                            &nbsp;
                        </div>
                        <div class="item-table-td-right">
                            &nbsp;
                        </div>
                    </div>
                </div>
            </div>
            <!--       <div class="package-item">
                       {{packageItemText}}
                   </div>-->
        </div>


        <div class="details-info-1">
            <div class="item">
                <div class="item-left">
                    手机号
                </div>
                <div class="item-right">
                    {{groupData.mobile}}
                </div>
            </div>
            <div class="item">
                <div class="item-left">
                    车牌号
                </div>
                <div class="item-right">
                    {{groupData.carNumber}}
                </div>
            </div>
        </div>
        <div class="details-info-2">
            <ul class="info-2-top">
                <li>
                    <div class="top-left">订单编号</div>
                    <div class="top-right">{{groupData.orderNo}}</div>
                </li>
                <li>
                    <div class="top-left">下单时间</div>
                    <div class="top-right">{{groupData.createTime}}</div>
                </li>
                <li>
                    <div class="top-left">支付时间</div>
                    <div class="top-right">{{groupData.payTime}}</div>
                </li>
                <li v-if="groupData.openState==3">
                    <div class="top-left">退款时间</div>
                    <div class="top-right">{{groupData.updateTime}}</div>
                </li>
            </ul>
            <div class="notice">
                拼团须知
                <div class="r">
                    <zs-icon :size="16" icon="user2" text="好友拼单"></zs-icon>
                    <zs-icon :size="16" icon="check3" text="人满成功"></zs-icon>
                    <zs-icon :size="16" icon="cash-pay2" text="人不满退款"></zs-icon>
                </div>
            </div>
        </div>
        <!--推荐蒙版-->
        <div class="share-model" v-if="shareModel" @click="shareModel=false">
            <div class="share-body">
                <zs-icon class="guide-arrow-icon" icon="guide-arrow" size="100"></zs-icon>
                <div class="body-main">
                    <p>还差{{packClusterData.clusterNum-packClusterData.successNum}}人</p>
                    <p>点击右上角发送给朋友</p>
                </div>
            </div>
        </div>
        <div class="footer" v-if="groupData.openState==1">
            <div class="footer-main" @click="shareModel=true">
                邀请好友拼单
            </div>
        </div>
    </div>
</template>

<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';

    import carNumber from '../../../../../components_proj/car_number/app.vue';
    import {
        getDataFromParam,
        turnToNextPage,
        isEmpty,
        DateUtils
    } from '../../../../../assets/js/utils';
    import Base64 from '../../../../../assets/js/base64.min';

    import { turnToHostPage } from '../../../../../assets/js/turnToHostPage.js';

    export default {
        components: {
            carNumber, maHead
        },
        data() {
            return {
                openId: '',
                id: '',
                shareModel: false,
                payState: '',
                countdown: 0,
                activityState: 0, //该状态个人为进入开团活动页时的状态
                groupState: 0, //该状态个人为进入已有的开团活动页时的状态
                groupData: {},
                packClusterData: {
                    businessPackage: {
                        packageAndItems: []
                    }
                },
                shareDescription: ''
            };
        },
        computed: {
            //处理卡包包含的各项服务
            packageItemText() {
                let ret = '';
                if (!isEmpty(this.packClusterData.businessPackage)) {
                    let list = this.packClusterData.businessPackage.packageAndItems;
                    for (let i = 0; i < list.length; i++) {
                        ret += list[i].serviceItem.itemName + ' *' + list[i].useTimes + '、';
                    }
                    ret = ret.substring(0, ret.length - 1);
                }
                return ret;
            },
            //处理倒计时时间
            handCountdownH() {
                let ret = parseInt(this.countdown / 60 / 60);
                ret = ret > 9 ? ret + '' : '0' + ret;
                return ret || '00';
            },
            handCountdownM() {
                let ret = parseInt(this.countdown / 60 % 60);
                ret = ret > 9 ? ret + '' : '0' + ret;
                return ret || '00';
            },
            handCountdownS() {
                let ret = this.countdown % 60;
                ret = ret > 9 ? ret + '' : '0' + ret;
                return ret || '00';
            }
        },
        methods: {
            /*
             返回列表页面
             */
            handGoBack() {
                turnToNextPage('/wap/customer/open/group/list.html', { i: Base64.encode(this.openId) });
            },
            //获取参团详情数据
            getData(fn) {
                let sendData = {
                    openId: this.openId,
                    packClusterBuyRecordId: this.id
                };
                if (this.payState == 6) {
                    sendData.payState = 6;
                }
                this.$ajax(
                    this.$joggle.customer.packcluster.selectPackClusterBuyRecordDetail,
                    sendData,
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.groupData = data.data;
                            this.packClusterData = data.data.packCluster;
                            this.shareDescription = data.data.packCluster.shareDescription;
                            let now = DateUtils.dateToRegular(data.data.serviceTime).getTime();
                            let endTime = DateUtils.dateToRegular(data.data.endDay).getTime();
                            this.handRemainingTime(now, endTime);
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
            //已开团的剩余时间
            handRemainingTime(now, end) {
                let passTime = end;
                if (passTime > now) {
                    this.countdown = parseInt((passTime - now) / 1000);
                    let t = setInterval(() => {
                        if (this.countdown > 0) {
                            this.countdown--;
                        } else {
                            clearInterval(t);
                            this.groupData.openState = 4;
                        }
                    }, 1000);
                }
            },
            //设置wxsdk的配置信息
            setWxConfig(fn) {
                this.$ajax(
                    this.$joggle.open.selectWxJsAPI,
                    { url: window.location.href },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            let wxTitle = `【仅剩${this.packClusterData.clusterNum - this.packClusterData.successNum}个名额】 ${this.packClusterData.clusterName}`;
                            let itemList = this.packClusterData.businessPackage.packageAndItems;
                            let serviceText = '';

                            for (let i = 0; i < itemList.length; i++) {
                                serviceText += `${itemList[0].serviceItem.itemName} *${itemList[0].useTimes}`;
                            }
                            let wxDesc = this.shareDescription;
                            let host = window.location.host; //获取地址栏
                            let wxLink = `http://${host}/wap/customer/open/group/activity.html?id=${this.packClusterData.id}&openRecordId=${this.id}`;
                            let wxImgUrl = this.packClusterData.shareImgPath ? this.packClusterData.shareImgPathUrl : this.groupData.imgSrc;
                            wx.config({
                                debug: false,
                                appId: data.data.appId,
                                timestamp: data.data.timestamp,
                                nonceStr: data.data.nonce,
                                signature: data.data.signature,
                                jsApiList: ['checkJsApi', 'onMenuShareTimeline', 'onMenuShareAppMessage']
                            });
                            wx.ready(_ => {
                                wx.checkJsApi({
                                    jsApiList: [
                                        'onMenuShareTimeline',
                                        'onMenuShareAppMessage'
                                    ],
                                    success: function (res) {
                                    },
                                    error: function (res) {
                                    }
                                });
                                wx.onMenuShareTimeline({
                                    title: wxTitle, // 分享标题
                                    desc: wxDesc, // 分享描述
                                    link: wxLink, //分享的链接
                                    imgUrl: wxImgUrl, // 分享图标
                                    type: 'link', // 分享类型,music、video或link，不填默认为link
                                    success: function () {
                                        // 用户确认分享后执行的回调函数
                                    },
                                    cancel: function (res) {
                                        // 用户取消分享后执行的回调函数
                                    },
                                    error: function (res) {
                                        // 用户取消分享后执行的回调函数
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: wxTitle, // 分享标题
                                    desc: wxDesc, // 分享描述
                                    link: wxLink, //分享的链接
                                    imgUrl: wxImgUrl, // 分享图标
                                    type: 'link' // 分享类型,music、video或link，不填默认为link
                                });
                            });
                            wx.error(_ => {
                                // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                            });
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
            }
        },
        created() {
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            this.id = getDataFromParam('id') || '';
            this.payState = getDataFromParam('payState') || '';
            this.shareModel = getDataFromParam('showShare') ? true : false;
            if (!this.openId || !this.id) {
                this.$message({
                    type: 'error',
                    message: '请从正常路径进入页面',
                    duration: '1000'
                });
                setTimeout(() => {
                    turnToHostPage('/open/user_center.html', 'emkt');
                }, 1000);
            }
            let loading = this.$loading();
            this.getData(() => {
                if (this.groupData.openState == 1 && this.countdown == 0) {
                    this.groupData.openState = 4;
                }
                if (this.groupData.openState == 1) {
                    this.setWxConfig(() => {
                        loading.close();
                    });
                } else {
                    loading.close();
                }
            });
            //获取参团活动的数据后需做的事件

        }
    };
</script>
<style lang="less">
    @import 'style.less';
</style>