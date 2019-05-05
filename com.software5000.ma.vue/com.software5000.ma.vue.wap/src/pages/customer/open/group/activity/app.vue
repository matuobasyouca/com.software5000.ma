<template>
    <div class="activity">
        <swiper-banner
                :images="images"
        ></swiper-banner>
        <div class="contain">
            <div class="price-position">
                <div class="price-left"><span class="title">拼团价</span><span class="red">￥{{packCulsterData.salePrice || '0'}}</span><i
                        class="del">原价：￥{{packCulsterData.businessPackage ?
                    packCulsterData.businessPackage.salePrice : '0'}}</i></div>
            </div>
            <div class="item group-card">
                <div class="price">
                    <div class="price-right">{{packCulsterData.clusterNum}}人团</div>
                </div>
                <div class="title">
                    {{packCulsterData.clusterName}}
                </div>
                <div class="card" v-if="packCulsterData.businessPackage">
                    <span>{{packCulsterData.businessPackage.packageName}}</span>
                    <div class="r">
                        <zs-icon :size="12" icon="time3"
                                 :text="packCulsterData.businessPackage.validTerm!=0 ? '有效期：'+packCulsterData.businessPackage.validTerm+'年' : '有效期：永久'"></zs-icon>
                    </div>
                </div>
                <div class="card-item">
                    <ul>
                        <li v-for="(item,index) in packCulsterData.businessPackage.packageAndItems">
                            <span class="h3">{{item.serviceItem.itemName}}</span>
                            <span class="time">{{item.useTimes}}次</span>
                        </li>
                        <li v-if="packCulsterData.businessPackage.packageAndItems && packCulsterData.businessPackage.packageAndItems.length%2 != 0">
                            <span class="h3"></span>
                            <span class="time"></span>
                        </li>
                    </ul>
                </div>

                <div class="notice">
                    拼团须知
                    <div class="ri">
                        <zs-icon :size="16" icon="user3" text="好友拼单"></zs-icon>
                        <zs-icon :size="16" icon="check3" text="人满成功"></zs-icon>
                        <zs-icon :size="16" icon="cash-pay2" text="人不满退款"></zs-icon>
                    </div>
                </div>
            </div>
            <div class="item member" v-if="openRecordId">
                <ul class="member-top" v-if="culsterList.length!=0">
                    <li :class="{ on: culsterList[index]}"
                        v-for="(item,index) in openCulsterData.packCluster.clusterNum"
                        :style="handWeChatImg(culsterList[index])"
                        :key="index"></li>
                </ul>
                <div class="member-bottom" v-if="countdown && openState==1">
                    仅剩下 <span class="red">{{openCulsterData.packCluster.clusterNum - openCulsterData.packClusterPerDtos.length}}  </span>
                    个名额，
                    <div class="countdown">
                        <i class="red-block">{{handCountdownH}}</i>
                        <span>:</span>
                        <i class="red-block">{{handCountdownM}}</i>
                        <span>:</span>
                        <i class="red-block">{{handCountdownS}}</i>
                    </div>
                    后结束
                </div>
                <div class="member-bottom" v-else>
                    已结束
                </div>
            </div>
            <div class="item item-bottom">
                <div class="group-info-top">拼团说明</div>
                <ul class="group-info-main" v-html="handDescriptionText" v-if="handDescriptionText">
                </ul>
                <p v-else class="group-none">暂无拼团说明</p>
            </div>
            <div class="item item-bottom">
                <div class="group-info-top">商品详情</div>
                <div class="group-detail" v-html="attachedText" v-if="attachedText"></div>
                <p v-else class="group-none">暂无商品详情</p>
            </div>
        </div>
        <!--推荐蒙版-->
        <div class="share-model" v-if="shareModel" @click="shareModel=false">
            <div class="share-body">
                <zs-icon class="guide-arrow-icon" icon="guide-arrow" size="100"></zs-icon>
                <div class="body-main">
                    <p>还差{{openCulsterData.packCluster.clusterNum - openCulsterData.packClusterPerDtos.length}}人</p>
                    <p>点击右上角发送给朋友</p>
                </div>
            </div>
        </div>
        <!--底部按钮-->
        <div class="page-footer">
            <div class="footer-body self" :class="{ gray : activityState!=0 && activityState!=3 && activityState!=4 }"
                 @click="handOpenGroup" v-if="isMySelf">
                {{['一键开团','活动未开始','活动已结束','您已参与该拼团,邀请好友拼单','您的拼团已成功','拼团失败，正在退款'][activityState]}}
            </div>
            <div class="footer-body clr" v-else>
                <div class="footer-body-left" @click="handOpenGroup2(1)" v-if="activityState==0 || activityState==3">
                    我也来开团
                </div>
                <div class="footer-body-right"
                     :class="{ gray : openState !=1 || groupState==1 , w100 : activityState!=0 && activityState!=3}"
                     @click="handOpenGroup2(2)">
                    {{['一键参团','拼团已结束','拼团失败，正在退款'][groupState]}}
                </div>
            </div>
        </div>
        <img src="./assets/user-default.jpg" alt="" style="display: none">
    </div>
</template>

<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import swiperBanner from '../../../../../components_proj/swiper-banner/app.vue';
    import { getDataFromParam, turnToNextPage, isEmpty, DateUtils } from '../../../../../assets/js/utils';
    import { turnToHostPage } from '../../../../../assets/js/turnToHostPage';
    import { selectWxCode, selectOpenId } from '../../../../../assets/js/wxUtils';
    import Base64 from '../../../../../assets/js/base64.min';
    export default {
        components: {
            maHead, swiperBanner
        },
        data () {
            return {
                openId: '',
                groupOpenId: '',
                packCulsterId: '',
                isMySelf: true,
                openState: 0,
                packCulsterData: {
                    businessPackage: {}
                },
                openRecordId: '',
                openCulsterData: {
                    packCluster: {},
                    packClusterPerDtos: []
                },
                description: '',
                culsterList: [],
                wxCode: '',
                images: [],
                countdown: 0,

                //底部
                activityState: 0, //该状态个人为进入开团活动页时的状态
                groupState: 0, //该状态个人为进入已有的开团活动页时的状态

                //展示分享
                shareModel: false,
                shareDescription: '',
                attachedText: ''
            };
        },
        computed: {
            //处理倒计时时间
            handCountdownH(){
                let ret = parseInt(this.countdown / 60 / 60);

                ret = ret > 9 ? ret : '0' + ret;
                return ret || '00';
            },
            handCountdownM(){
                let ret = parseInt(this.countdown / 60 % 60);

                ret = ret > 9 ? ret : '0' + ret;
                return ret || '00';
            },
            handCountdownS(){
                let ret = this.countdown % 60;

                ret = ret > 9 ? ret : '0' + ret;
                return ret || '00';
            },
            //拼团说明文字变动
            handDescriptionText(){
                let ret = '';

                if (!isEmpty(this.description)) {
                    let list = this.description.split('\n');

                    list.forEach((x) => {
                        ret += `<li>${x}</li>`;
                    });
                }
                return ret;
            }
        },
        watch: {},
        methods: {
            //获取团活动信息
            getData(fn, loading){
                this.$ajax(
                    this.$joggle.customer.packcluster.selectPackClusterInfo,
                    { id: this.packCulsterId },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.packCulsterData = data.data;
                            this.shareDescription = data.data.shareDescription;
                            this.attachedText = decodeURIComponent(data.data.attachedText);
                            document.title = this.packCulsterData.businessName || '门店';

                            this.description = data.data.description;
                            this.images = data.data.packClusterImgs;
                            this.handActivityState(this.packCulsterData.beginDay, this.packCulsterData.endDay);
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
            //获取已有的团的数据
            getGroupData(fn, loading){
                this.$ajax(
                    this.$joggle.customer.packcluster.selectPackClusterPeron,
                    { buyRecordId: this.openRecordId },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.openCulsterData = data.data;
                            let list = data.data.packClusterPerDtos;

                            list.sort(function (a, b) {
                                let aNum = a.header ? 1 : 0;
                                let bNum = b.header ? 1 : 0;

                                return bNum - aNum;
                            });
                            this.culsterList = list;
                            this.isMySelf = JSON.stringify(list).indexOf(this.openId) > -1;
                            this.openState = data.data.openState;
                            let now = DateUtils.dateToRegular(data.data.serviceTime).getTime();
                            let endTime = DateUtils.dateToRegular(data.data.openCreateTime).getTime() + data.data.packCluster.clusterHour * 1000 * 60 * 60;

                            this.handRemainingTime(now, endTime);

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
            //获取微信openId后需操作
            handGetOpenId(){
                const loading = this.$loading();

                //根据openId与活动id查找该用户参团buyRecordId
                const f1 = new Promise((resolve) => {
                    this.certJoinGroup(() => {
                        if (this.openRecordId) {
                            this.getGroupData(() => {
                                resolve();
                            }, loading);
                        } else {
                            resolve();
                        }
                    }, loading);
                });

                const f2 = new Promise((resolve) => {
                    this.getData(() => {
                        resolve();
                    }, loading);
                });

                Promise.all([f2, f1]).then(() => {
                    if (this.openRecordId) {
                        if (this.isMySelf) {
                            this.activityState = this.openState + 2;
                            if (this.openState == 1 && this.countdown == 0) {
                                this.activityState = 5;
                            }
                        } else if (this.packCulsterData.state == 2) {
                            this.activityState = 2;
                        }
                        this.groupState = this.openState == 1 ? 0 : 1;
                        if (this.openState == 1 && this.countdown == 0) {
                            this.groupState = 1;
                        }
                    } else {
                        this.activityState = this.packCulsterData.state == 2 ? 2 : this.activityState;
                    }
                    this.setWxConfig(() => {
                        loading.close();
                    });
                });
            },
            //设置wxsdk的配置信息
            setWxConfig(fn){
                this.$ajax(
                    this.$joggle.open.selectWxJsAPI,
                    { url: window.location.href },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            let host = window.location.host; //获取地址栏
                            let wxDesc = this.shareDescription;
                            let wxImgUrl = this.packCulsterData.shareImgPath ? this.packCulsterData.shareImgPathUrl : this.packCulsterData.packClusterImgs[0].imgPathUrl;
                            let wxTitle = this.packCulsterData.clusterName;
                            let wxLink = `http://${host}/wap/customer/open/group/activity.html?id=${this.packCulsterId}`;

                            if(this.openRecordId){
                                wxTitle = `【仅剩${this.openCulsterData.packCluster.clusterNum - this.openCulsterData.packClusterPerDtos.length}个名额】 ${this.packCulsterData.clusterName}`;
                                wxLink = `http://${host}/wap/customer/open/group/activity.html?id=${this.packCulsterId}&openRecordId=${this.openRecordId}`;
                            }
                            wx.config({
                                debug: false,
                                appId: data.data.appId,
                                timestamp: data.data.timestamp,
                                nonceStr: data.data.nonce,
                                signature: data.data.signature,
                                jsApiList: ['checkJsApi', 'onMenuShareTimeline', 'onMenuShareAppMessage']
                            });
                            wx.ready(() => {
                                wx.onMenuShareTimeline({
                                    title: wxTitle, // 分享标题
                                    desc: wxDesc, // 分享描述
                                    link: wxLink, //分享的链接
                                    imgUrl: wxImgUrl, // 分享图标
                                    type: 'link'// 分享类型,music、video或link，不填默认为link
                                });
                                wx.onMenuShareAppMessage({
                                    title: wxTitle, // 分享标题
                                    desc: wxDesc, // 分享描述
                                    link: wxLink, //分享的链接
                                    imgUrl: wxImgUrl, // 分享图标
                                    type: 'link'// 分享类型,music、video或link，不填默认为link
                                });
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
            },
            //验证是否有参过团
            certJoinGroup(fn, loading){
                this.$ajax(
                    this.$joggle.customer.packcluster.selectHaveBuyPackCluster,
                    {
                        carNumber: '',
                        mobile: '',
                        packClusterId: this.packCulsterId,
                        wxOpenId: this.openId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS012048') {
                            if (this.openRecordId && this.openRecordId != data.data) {
                                this.$message({
                                    type: 'error',
                                    message: '您已开过团'
                                });
                                setTimeout(() => {
                                    let para = { id: this.packCulsterId, openRecordId: data.data };

                                    turnToNextPage('/wap/customer/open/group/activity.html', para);
                                }, 1000);
                            } else {
                                this.openRecordId = data.data;
                            }
                        }
                        fn && fn();
                    }
                );
            },
            //处理各头像
            handWeChatImg(data){
                let ret = '';

                if (data) {
                    ret = data.wxHeadImg ? `background-image: url(${data.wxHeadImg})` : `background-image: url(/wap/static/img/user-default.0f04b42.jpg)`;
                }
                return ret;
            },
            //已开团的剩余时间
            handRemainingTime(now, date){
                let passTime = date;

                if (passTime > now) {
                    this.countdown = parseInt((passTime - now) / 1000);
                    let t = setInterval(() => {
                        if (this.countdown > 0) {
                            this.countdown--;
                        } else {
                            clearInterval(t);
                            if (this.isMySelf) {
                                this.activityState = 5;
                            } else {
                                this.groupState = 1;
                            }
                        }

                    }, 1000);
                }
            },
            //根据开团的时间生成对应状态
            handActivityState(start, end){
                let now = new Date().getTime();
                let s = DateUtils.dateToRegular(start).getTime();
                let e = DateUtils.dateToRegular(end).getTime();

                if (now < s) {
                    this.activityState = 1; //未开始
                } else if (now > e) {
                    this.activityState = 2; //已过期
                }
            },
            //开团跳转页面
            handOpenGroup(){
                if (this.activityState == 0 && this.packCulsterData.state != 2) {
                    let para = { i: Base64.encode(this.openId), id: this.packCulsterId };
                    turnToNextPage('/wap/customer/open/group/join.html', para);
                } else if (this.openState == 1) {
                    this.shareModel = true;
                }
            },
            //已开团跳转页面
            handOpenGroup2(index){
                let para = { i: Base64.encode(this.openId), id: this.packCulsterId };

                if (index == 1) {
                    if (this.activityState != 0 && this.activityState != 5 && this.packCulsterData.state == 2) return;
                } else if (index == 2) {
                    if (this.groupState == 1) return;
                    para.openRecordId = this.openCulsterData.openRecordId;
                }
                turnToNextPage('/wap/customer/open/group/join.html', para);
            }

        },
        mounted() {
            this.packCulsterId = getDataFromParam('id') ? parseInt(getDataFromParam('id')) : '';
            this.openRecordId = getDataFromParam('openRecordId') ? parseInt(getDataFromParam('openRecordId')) : '';
            this.shareModel = !!getDataFromParam('showShare');
            if (!this.packCulsterId) {
                this.$message({
                    type: 'error',
                    message: '请从正常路径进入页面'
                });
                setTimeout(() => {
                    turnToHostPage('/open/user_center.html', 'emkt');
                }, 1000);
            }

            if (window.sessionStorage.i) {
                this.openId = window.sessionStorage.i;
                this.handGetOpenId();
            } else {
                const loading = this.$loading();

                selectWxCode((code) => {
                    this.wxCode = code;
                    if (isEmpty(code))return;
                    selectOpenId(code, (openId) => {
                        window.sessionStorage.i = openId;
                        this.openId = openId;
                        this.handGetOpenId();
                    }, loading);
                });
            }
        }
    };
</script>
<style lang="less">
    @import './style.less';
</style>