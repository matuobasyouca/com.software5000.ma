<template>
    <div class="pay-page">
        <ma-head
                :goBack="false"
                :home="false"
                @left-icon-click="handGoBack"
        >我参与的拼团
        </ma-head>
        <zs-tab v-model="tabCurr" :tabs="tabs"></zs-tab>
        <div class="list-item" v-for="(item,index) in groupList" :key="item.id">
            <div class="store-name">
                <zs-icon :size="15" icon="shang3" :text="item.businessName"></zs-icon>
                <div class="r" v-if="item.state==1 && !item.refundState" @click="handShare(item)">
                    <zs-icon :size="15" icon="more-people" text="邀请好友拼单"></zs-icon>
                </div>
            </div>
            <div class="package">
                <div class="package-left" :style="`background-image: url(${item.imgSrc})`"></div>
                <div class="package-right" @click="handDetails(item)">
                    <div class="package-title">{{item.packageName}}(有效期{{item.validTerm!=0 ? item.validTerm+'年' :
                        '永久'}})
                    </div>
                    <div class="package-price">
                        <span class="gray">拼团价</span>
                        <span class="red">￥{{item.psalePrice}}</span>
                        <span class="del">原价:￥{{item.bsalePrice}}</span>
                    </div>
                    <div class="package-state">
                        <div class="package-state-item"
                             :class="(item.state==1 && item.refundState) ? 'fail' : ['','grouping','success','fail'][item.state || '1']">
                            {{ (item.state==1 && item.refundState) ? '拼团失败' : ['','拼团中','拼团成功','拼团失败'][item.state ||
                            '1']}}
                        </div>
                        <div class="package-state-item fail" v-if="item.state==1 && item.refundState || item.state==3">
                            {{ item.state==3 ? '已退款' : '退款中' }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="more-item">
            <div class="list-more" v-if="groupList.length!=0 && !isLastPage" @click="handMore">显示更多</div>
            <div class="list-all" v-if="groupList.length!=0 && isLastPage">已显示全部</div>
            <div class="list-empty" v-if="groupList.length==0">暂无数据</div>
        </div>
    </div>
</template>

<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import {
        getDataFromParam,
        turnToNextPage,
        isEmpty
    } from '../../../../../assets/js/utils';
    import Base64 from '../../../../../assets/js/base64.min';

    import { turnToHostPage } from '../../../../../assets/js/turnToHostPage.js';

    export default {
        components: {
            maHead
        },
        data() {
            return {
                tabCurr: 1,
                tabs: [{
                    title: '拼团中',
                    count: 0
                }, {
                    title: '拼团成功',
                    count: 0
                }, {
                    title: '拼团失败',
                    count: 0
                }],
                isLoading: true,
                openId: '',
                groupList: [],
                isLastPage: true,
                startPage: 1,

                businessId: '',
                packId: '',
                mobile: '',
                carNumber: '',
                businessName: '',
                imgSrc: ''
            };
        },
        computed: {
            tabListStyle() {
                let style = {};
                if (this.tabs.length > 0) {
                    style.width = `${100 / this.tabs.length}%`;
                }
                return style;
            }
        },
        watch: {
            tabCurr() {
                let loading = this.$loading();
                this.getGroupList(() => {
                    loading.close();
                }, false, loading);
            }
        },
        methods: {
            /*
             返回列表页面
             */
            handGoBack() {
                turnToHostPage('/open/user_center.html', 'emkt');
            },
            //获取参团列表
            getGroupList(fn, isShowMore, loading) {
                if (isShowMore) {
                    this.startPage++;
                } else {
                    this.startPage = 1;
                }
                let stateObj=['state','succeedState','failState'][this.tabCurr-1];
                let sendData={
                    startPage: this.startPage,
                    openId: this.openId
                };

                sendData[stateObj]=this.tabCurr;
                this.$ajax(
                    this.$joggle.customer.packcluster.selectPagePackClusterBuyRecordByOpenId,
                    sendData,
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.isLastPage = data.data.isLastPage;
                            this.groupList = isShowMore ? this.groupList.concat(data.data.list) : data.data.list;
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
            //获取参团各状态数据
            handgetGroupStateNum(fn, loading) {
                this.$ajax(
                    this.$joggle.customer.packcluster.selectPackClusterBuyRecordCount,
                    {
                        openId: this.openId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (data.data.length != 0) {
                                data.data.forEach((val) => {
                                    let state=val.des==='拼团中' ? 1 :
                                        val.des==='拼团成功' ? 2 :
                                            val.des==='退款中' ? 3 :
                                                val.des==='拼团失败' ? 3 : 1;
                                    this.tabs[state-1].count =this.tabs[state-1].count + Number(val.count);
                                });
                            }
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
            //获取该车牌号
            handleGetCarNumber(carNumber) {
                this.carNumber = carNumber;
            },
            //加载更多
            handMore() {
                let loading = this.$loading();
                this.getGroupList(() => {
                    loading.close();
                }, true, loading);
            },

            //跳转至参团详情
            handDetails(data) {
                let para = { i: Base64.encode(this.openId), id: data.id };
                if (data.payState == 6) {
                    para.payState = 6;
                }
                turnToNextPage('/wap/customer/open/group/details.html', para);
            },
            //跳转至分享页
            handShare(data) {

                turnToNextPage('/wap/customer/open/group/details.html', {
                    i: Base64.encode(this.openId),
                    id: data.id,
                    showShare: 1
                });
            }
        },
        created() {
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            if (!this.openId) {
                this.$message({
                    type: 'error',
                    message: '请从用户中心进入',
                    duration: '1000'
                });
                setTimeout(() => {
                    turnToHostPage('/open/user_center.html', 'emkt');
                }, 1000);
            }

            let loading = this.$loading();
            let p1 = new Promise((resolve) => {
                this.getGroupList(() => {
                    resolve();
                });
            });
            let p2 = new Promise((resolve) => {
                this.handgetGroupStateNum(() => {
                    resolve();
                });
            });

            Promise.all([p1, p2]).then(() => {
                loading.close();
            });

        }
    };
</script>
<style lang="less">
    @import 'style.less';
</style>