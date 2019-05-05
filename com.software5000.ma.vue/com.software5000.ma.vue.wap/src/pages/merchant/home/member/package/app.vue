<template>
    <div class="package-page">
        <ma-head @home-click="handleGoHome">套餐购买记录</ma-head>
        <zs-tab v-model="currTab" :tabs="tabs" @change="handleTabChange"></zs-tab>
        <div v-if="pageData.length > 0">
            <ul class="item-list" v-for="pack in pageData" @click="handleGoDetail(pack.id)">
                <li class="pack-member">
                    <zs-icon icon="user" class="member" size="16"></zs-icon>
                    <span class="pack-member-name">{{pack.realName}}</span>
                    <span class="pack-member-mobile">{{pack.mobile}}</span>
                    <div class="pack-member-vip">
                        <zs-icon class="vip" size="16" icon="member" :text="pack.lvlName" v-if="pack.lvlName"></zs-icon>
                        <zs-icon class="vip" size="16" icon="member" text="普通会员" v-else></zs-icon>
                    </div>
                </li>
                <li class="pack-info">
                    <div class="pack-info-pack">
                        <zs-icon icon="pack_green" size="20"></zs-icon>
                    </div>
                    <span class="pack-info-name">{{pack.packageName}}</span>
                    <div class="pack-info-time">
                        <zs-icon icon="time3" size="13" text="有效期1年"></zs-icon>
                    </div>
                </li>
                <li class="pack-pay">
                    <div>
                        <zs-icon v-if="currTab == 2 && pack.payType == '2'" icon="wechat-pay" size="14" text="微信收款"></zs-icon>
                        <zs-icon v-if="currTab == 2 && pack.payType != '2'" icon="cash-pay" size="14" text="现金收款"></zs-icon>
                        <span class="pack-pay-text" v-if="currTab == 1">合计</span>
                        <span class="pack-pay-money">￥{{pack.salePrice}}</span>
                    </div>
                </li>
                <li class="pack-btn" v-if="currTab == 1">
                    <zs-button>去收款</zs-button>

                </li>
                <li class="pack-pay-time" v-if="currTab == 2">
                    <span>{{pack.updateTime.slice(0,19)}}</span>
                </li>
            </ul>
            <ul>
                <li
                    class="show-more"
                    :class="[{'no-more' : !hasNextPage}]"
                    @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                </li>
            </ul>
        </div>
        <div v-else>
            <ul class="list-empty">
                <li>{{currTab == 1 ? '暂无待支付套餐' : '暂无已收款套餐'}}</li>
            </ul>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">

    import {turnToNextPage} from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import maFoot from '../../../../../components_proj/ma_foot/app.vue';

    export default {
        components: {
            maHead,
            maFoot
        },
        data() {
            return {
                currTab: 1,
                tabs: [
                    {
                        title: '待支付',
                        count: 0
                    },
                    {
                        title: '已收款',
                        count: 0
                    }
                ],
                //会员数据
                pageData: [],
                starPage: 1,
                pageSize: 10,
                hasNextPage: false
            }
        },
        methods : {
            //跳转首页
            handleGoHome() {
                turnToNextPage("/wap/merchant/home/manage/index.html");
            },
            getData(fn, loading, isShowMore) {
                if (isShowMore) {
                    this.starPage++;
                } else {
                    this.starPage = 1;
                }
                let url = this.currTab == 1 ? this.$joggle.merchant.businessPackage.selectNoPaidBusinessPackageOrder : this.$joggle.merchant.businessPackage.selectPaidBusinessPackageOrder;
                this.$ajax(
                    url,
                    {
                        searchInfo: this.searchInfo,
                        starPage: this.starPage,
                        pageSize: this.pageSize
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.hasNextPage = data.data.hasNextPage;
                            this.pageData = isShowMore ? this.pageData.concat(data.data.list) : data.data.list;

                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            //获取列表数
            getDataCount(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectCountByState,
                    {},
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            const temp = data.data;
                            temp.forEach((t) => {
                                this.$set(this.tabs[t.state - 1], 'count', t.count);
                            });

                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            //tab切换
            handleTabChange(t){
                const loading = this.$loading();
                //获取会员信息
                this.getData(() => {
                    loading.close();
                    this.isLoading = false;
                }, loading);
            },
            //显示更多
            handleShowMore() {
                if (this.hasNextPage) {
                    const loading = this.$loading();
                    this.getData(() => {
                        loading.close();
                    }, loading, true);
                }
            },
            //跳转详情页
            handleGoDetail(id) {
                turnToNextPage("/wap/merchant/home/member/package_detail.html",{"id" : id});
            }
        },
        created(){
            const loading = this.$loading();
            //获取会员信息
            const f0 = new Promise((resolve) => {
                this.getData(() => {
                    resolve();
                }, loading)
            });
            //获取列表数
            const f1 = new Promise((resolve) => {
                this.getDataCount(() => {
                    resolve();
                }, loading)
            });
            Promise.all([f0, f1]).then(() => {
                loading.close();
                this.isLoading = false;
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
