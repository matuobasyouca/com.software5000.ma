<template>
    <div class="pay-success-page" v-if="!isLoading">
        <ma-head :go-back="false" :home="false">支付成功</ma-head>
        <div class="pay-success-detail">
            <div class="detail-middle">
                <div class="box-shadow"></div>
                <div class="redPack-img"><img src="./asset/redPack.png"></div>
                <p class="p1">恭喜你，获得现金红包</p>
                <p class="p2">分享给朋友，一起抢</p>
                <zs-button class="share-btn" type="primary" @click="shareDialog">立即分享</zs-button>
                <div class="row-make">
                    <div class="row-circle"></div>
                </div>
                <div class="ewm-img">
                    <img :src="barCodeImg">
                </div>
                <p class="p3">长按关注公众号</p>
                <p class="p4">查看消费账单</p>
            </div>
        </div>
        <div class="shade" ref="share" @click="cancelShare">
            <div class="redPack-p">
                <p class="p-top">恭喜你，获得现金红包</p>
                <p class="p-bottom">分享给朋友，一起抢</p>
            </div>
            <div class="jian-img"></div>
        </div>

        <!--分享成功dialog-->
        <zs-dialog
                class="share-dialog"
                v-model="showShareDialog">
            <div class="dialog-body">
                <div class="dialog-img"></div>
                <p class="p1">分享成功</p>
                <p class="p2">你可继续分享，或去领红包</p>
            </div>
            <template slot="footer">
                <span class="continue" @click="continueShare">继续分享</span>
                <span class="goGet" @click="goGetRedpack">去领取</span>
            </template>
        </zs-dialog>
    </div>
</template>
<script>
    import maHead from '../../../../components_proj/ma_head/app.vue';
    import { getDataFromParam } from '../../../../assets/js/utils';
    import { turnToHostPage } from '../../../../assets/js/turnToHostPage';
    export default {
        components: {
            maHead
        },
        data() {
            return {
                isLoading: false,
                hrefLink: '',
                orderNo: '',
                businessId: '',
                cpuuid: '',
                showShareDialog: false,
                cpImgSrc: '',
                id: ''
            };
        },
        methods: {
            getImgMessage(fn, loading) {
                console.log(`${this.domain}${this.$joggle.customer.paySuccess.selectCouponsByCpUuid}`);
                this.$ajax(
                    `${this.domain}${this.$joggle.customer.paySuccess.selectCouponsByCpUuid}`,
                    {
                        cpUuid: this.cpuuid
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.cpImgSrc = data.data.cpImgSrc;
                            this.id = data.data.id;
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            });
                        }
                    }
                );
            },
            updateCouponsForForwardCount() {
                let url = this.domain + '/open/coupons/updateCouponsForForwardCount';

                this.$ajax(
                    url,
                    {
                        id: this.id
                    },
                    false,
                    () => {

                    }
                );
            },
            //去领取
            goGetRedpack() {
                turnToHostPage('/open/coupons/take.html', 'emkt', {
                    orderNo: this.orderNo,
                    businessId: this.businessId,
                    cpuuid: this.cpuuid
                });
            },
            //继续分享
            continueShare() {
                this.showShareDialog = false;
                this.shareDialog();
            },
            shareDialog() {
                this.$refs.share.style.display = 'block';
            },
            cancelShare() {
                this.$refs.share.style.display = 'none';
            },
            //设置wxsdk的配置信息
            setWxConfig(fn, loading) {
                this.$ajax(
                    this.$joggle.customer.paySuccess.getWxConfig,
                    { url: window.location.href },
                    loading,
                    (data) => {
                        const _this = this;

                        if (data.code === 'ZS011000') {
                            fn && fn();
                            wx.config({
                                debug: false,
                                appId: data.data.appId,
                                timestamp: data.data.timestamp,
                                nonceStr: data.data.nonce,
                                signature: data.data.signature,
                                jsApiList: ['checkJsApi', 'onMenuShareTimeline', 'onMenuShareAppMessage']
                            });
                            wx.ready(() => {
                                const url = encodeURIComponent(`http://emkt.${_this.hrefLink}.com/open/coupons/take.html?orderNo=${_this.orderNo}&businessId=${_this.businessId}&cpuuid=${_this.cpuuid}`);

                                wx.checkJsApi({
                                    jsApiList: [
                                        'onMenuShareTimeline',
                                        'onMenuShareAppMessage'
                                    ]
                                });
                                wx.onMenuShareTimeline({
                                    title: '您的好友赠送您现金红包', // 分享标题
                                    desc: '大家都在抢，手快有，手慢无', // 分享描述
                                    link: `http://ma.${_this.hrefLink}.com/open/mzscp/redirctUrl?url=${url}`,
                                    imgUrl: this.cpImgSrc, // 分享图标
                                    type: 'link', // 分享类型,music、video或link，不填默认为link
                                    success() {
                                        // 用户确认分享后执行的回调函数
                                        _this.updateCouponsForForwardCount();
                                        _this.cancelShare();
                                        _this.showShareDialog = true;
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: '您的好友赠送您现金红包', // 分享标题
                                    desc: '大家都在抢，手快有，手慢无', // 分享描述
                                    link: `http://ma.${_this.hrefLink}.com/open/mzscp/redirctUrl?url=${url}`,
                                    imgUrl: _this.cpImgSrc, // 分享图标
                                    type: 'link', // 分享类型,music、video或link，不填默认为link
                                    success() {
                                        // 用户确认分享后执行的回调函数
                                        _this.updateCouponsForForwardCount();
                                        _this.cancelShare();
                                        _this.showShareDialog = true;
                                    }
                                });
                            });
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
            }
        },
        computed: {
            barCodeImg() {
                let ret = '/wap/static/img/bar_code.552f56c.png';
                let hostArr = window.location.host.split('.'); //获取地址栏

                if (this.recodeType == 1) {
                    if (hostArr[1] === 'zhongshengchengpin') {
                        ret = '/wap/static/img/bar_code_test.00ff770.png';
                    }
                }
                return ret;
            }
        },
        created() {
            this.hrefLink = window.location.href.split('.')[1];

            this.orderNo = getDataFromParam('orderNo');
            this.businessId = getDataFromParam('businessId');
            this.cpuuid = getDataFromParam('cpuuid');
            this.domain = getDataFromParam('domain');

            const loading = this.$loading();

            this.getImgMessage(() => {
                this.setWxConfig(() => {
                    loading.close();
                }, loading);
            }, loading);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>