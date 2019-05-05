<template>
    <div class="open-package-page" v-if="!isLoading">
        <ma-head :go-back="false" :home="false" @left-icon-click="handleGoBack">套餐卡</ma-head>
        <zs-tab v-model="currTab" :tabs="tabs" @change="handleTabChange"></zs-tab>
        <ul class="open-package-list" v-if="packages.length > 0">
            <li class="item" v-for="p in packages" :key="p.id" :class="['','use-up','time-out'][classTab-1]">
                <div class="item-head">
                    <p class="head-info">
                        <span class="item-name">{{ p.packageName || '' }}</span>
                        <span class="item-createtime">{{ p.createTime.slice(0,10) }}</span>
                    </p>
                    <p class="head-info">
                        <zs-icon icon="shang3" icon-dis="6" class="item-merchant" :text="p.businessName"></zs-icon>
                        <span class="item-validate">有效期：{{ getValidate(p.createTime,p.validTime) }}</span>
                    </p>
                </div>
                <div class="service-info">
                    <p v-for="(item,index) in p.serviceItemRemainList" :key="item.id" v-show="index < 3 || p.showAll">
                        {{ item.itemName }}
                        <span>
                            <i>总{{item.totalTimes}}</i> -
                            <i>用{{item.totalTimes-item.remainTimes}}</i> -
                            <i>余{{item.remainTimes}}</i>
                        </span>
                    </p>
                </div>
                <p class="show-more-item" v-show="p.serviceItemRemainList.length>3" @click="p.showAll = !p.showAll">
                    <zs-icon icon="expansion" icon-dis="6" text="展开全部" v-show="!p.showAll"></zs-icon>
                    <zs-icon icon="packup" icon-dis="6" text="收起部分" v-show="p.showAll"></zs-icon>
                </p>
            </li>
            <li
                    v-if="packages.length > 0"
                    class="show-more"
                    :class="[{'no-more' : !hasNextPage}]"
                    @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
            </li>
        </ul>
        <p class="list-empty" v-else>暂无数据</p>
    </div>
</template>
<script>
    import maHead from '../../../../components_proj/ma_head/app.vue';
    import {getDataFromParam, isEmpty, turnToNextPage, Hash} from '../../../../assets/js/utils';
    import {turnToHostPage} from '../../../../assets/js/turnToHostPage';
    import Base64 from '../../../../assets/js/base64.min';
    export default {
        components: {
            maHead
        },
        data(){
            return {
                isLoading: true,
                currTab: 1,
                classTab: 1,//用来控制显示用完或者过期的标志，值同currTab，延迟赋值
                tabs: [
                    {
                        title: '可用套餐',
                        count: 0
                    },
                    {
                        title: '已用完套餐',
                        count: 0
                    },
                    {
                        title: '过期套餐',
                        count: 0
                    }
                ],
                packages: [],
                hasNextPage: false,
                startPage: 1,
                pageSize: 10
            }
        },
        methods: {
            //返回用户中心
            handleGoBack(){
                turnToHostPage('/open/user_center.html', 'emkt');
            },
            //tab切换
            handleTabChange(t){
                Hash.add({tab: this.currTab});
                const loading = this.$loading();
                this.getPackageData(() => {
                    this.classTab = t;
                    loading.close();
                }, loading);
            },
            //加载更多
            handleShowMore(){
                if (this.hasNextPage) {
                    const loading = this.$loading();
                    this.getPackageData(() => {
                        loading.close();
                    }, loading, true);
                }
            },
            //获取套餐统计数据
            getPackageCount(fn, loading){
                this.$ajax(
                    this.$joggle.customer.package.selectUserPackageCount,
                    {wxOpenId: this.openId},
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const temp = data.data;
                            temp.forEach((t) => {
                                this.$set(this.tabs[t.type - 1], 'count', t.count);
                            })
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                        fn && fn()
                    }
                )
            },
            //获取套餐数据
            getPackageData(fn, loading, isShowMore){
                if (isShowMore) {
                    this.startPage++
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.customer.package.selectUserPackageRecord,
                    {
                        type: this.currTab,
                        wxOpenId: this.openId,
                        startPage: this.startPage,
                        pageSize: this.pageSize
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            let finalData = [];
                            data.data.list.forEach((d) => {
                                d.showAll = false;
                                finalData.push(d);
                            })
                            this.hasNextPage = data.data.hasNextPage;
                            this.packages = isShowMore ? this.packages.concat(finalData) : finalData;
                            fn && fn()
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
            //设置有效期
            getValidate(createTime, validateTime){
                if (isEmpty(validateTime)) {
                    return '永久'
                } else {
                    let t1 = createTime.replace(/\D+/g, ",").split(',');
                    let t2 = validateTime.replace(/\D+/g, ",").split(',');
                    return `${t2[0] - t1[0]}年`;
                }
            }
        },
        created(){
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            this.currTab = Hash.parse().tab || 1;
            this.classTab = this.currTab;
            if (isEmpty(this.openId)) {
                turnToHostPage('/open/user_center.html', 'emkt');
            } else {
                const loading = this.$loading();
                const f0 = new Promise((resolve) => {
                    this.getPackageData(() => {
                        resolve();
                    }, loading)
                })
                const f1 = new Promise((resolve) => {
                    this.getPackageCount(() => {
                        resolve();
                    }, loading)
                })
                Promise.all([f0, f1]).then(() => {
                    loading.close();
                    this.isLoading = false;
                })

            }
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>