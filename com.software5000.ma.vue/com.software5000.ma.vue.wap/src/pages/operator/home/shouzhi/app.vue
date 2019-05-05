<template>
    <div class="shouzhi-page" v-show="!isLoading">
        <ma-header :home="false">收支数据</ma-header>
        <pull-menu
                v-model="menuTab"
                p1="商家"
                p2="日期">
            <ul slot="p1" class="merchant-list">
                <li :class="[{'curr':businessId == ''}]" @click="handleBusinessSelect('')">不限</li>
                <li :class="[{'curr':businessId == msg.id}]"
                    v-for="(msg,index) in businessDatas"
                    :key="index"
                    @click="handleBusinessSelect(msg.id)">
                    {{msg.businessName}}
                </li>
            </ul>
            <template slot="p2">
                <div class="create-time-select">
                    <label for="startTime" class="time-wrap">
                        <span>{{ startTime || '开始日期' }}</span>
                        <input type="date" id="startTime" v-model="startTime">
                    </label>
                    <span class="time-wrap-sep">-</span>
                    <label for="endTime" class="time-wrap">
                        <span>{{ endTime || '结束日期'}}</span>
                        <input type="date" id="endTime" v-model="endTime">
                    </label>
                </div>
                <ul class="create-time-btn">
                    <li @click="handleCancleTime">清空</li>
                    <li @click="handleConfirmTime">确定</li>
                </ul>
            </template>
        </pull-menu>
        <div class="shouzhi-num">
            <ul>
                <li>
                    <div class="nr">
                        <div class="num">{{moneyChange(totalDatas.incomeSum)}}</div>
                        <div class="zi">总收入</div>
                    </div>
                </li>
                <li>
                    <div class="nr">
                        <div class="num">{{moneyChange(totalDatas.costSum)}}</div>
                        <div class="zi">总支出</div>
                    </div>
                </li>
            </ul>
        </div>
        <div class="shouzhi-list">
            <ul v-if="listDatas.length > 0">
                <li class="list-li" v-for="(item,index) in listDatas" :key="index">
                    <div class="tit">{{item.businessName}}</div>
                    <div class="con">
                        <div class="li"><span class="num">{{item.incomeSum/100}}</span><span class="zi">总收入</span></div>
                        <div class="li"><span class="num">{{item.costSum/100}}</span><span class="zi">总支出</span></div>
                    </div>
                </li>
                <div v-if="hasNextPage" @click="handleShowMore" class="show_all_css">显示更多 <i class="show-more-icon"></i>
                </div>
                <div v-else class="show_all_css">已显示全部</div>
            </ul>
            <div v-else class="data-empty">
                <div class="wushuju"></div>
                <p>暂无收支数据</p>
            </div>
        </div>

    </div>
</template>
<script>
    import maHeader from '../../../../components_proj/ma_head/app.vue';
    import pullMenu from '../../../../components_proj/pull_menu/app.vue';
    import {isEmpty} from '../../../../assets/js/utils';
    export default {
        components: {
            maHeader,
            pullMenu
        },
        data() {
            return {
                isLoading: true,
                startTime: '',
                endTime: '',
                businessId: '',
                hasNextPage: false,
                menuTab: false,
                paymentPage: {
                    businessId: '',
                    startTime: '',         //开始日期
                    endTime: '',           //结束日期
                    startPage: 1,
                    pageSize: 10,
                    orderBy: 'id asc'
                },
                selectTotalSum: {
                    businessId: '',
                    startTime: '',         //开始日期
                    endTime: '',           //结束日期
                },
                listDatas: [],
                totalDatas: [],
                businessDatas: []
            }
        },
        methods: {
            getDatesList(isShowMore, fn){
                if (isShowMore) {
                    this.paymentPage.startPage++;
                } else {
                    this.paymentPage.startPage = 1;
                }
                this.paymentPage.businessId = this.businessId;

                this.$ajax(
                    this.$joggle.operator.finance.selectPaymentPage,
                    this.paymentPage,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.listDatas = isShowMore ? this.listDatas.concat(data.data.list) : data.data.list;
                            this.hasNextPage = data.data.hasNextPage;
                            fn && fn();
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
            getDatesTotal(fn){
                this.selectTotalSum.businessId = this.businessId;
                this.selectTotalSum.startTime = this.paymentPage.startTime;
                this.selectTotalSum.endTime = this.paymentPage.endTime;

                this.$ajax(
                    this.$joggle.operator.finance.selectTotalSum,
                    JSON.stringify(this.selectTotalSum),
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.totalDatas = data.data || {};
                            fn && fn();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                );
            },
            getDatesBusiness(fn){
                this.$ajax(
                    this.$joggle.operator.finance.selecPaymentBusiness,
                    JSON.stringify({}),
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.businessDatas = data.data;
                            fn && fn();
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
            /* 钱格式转化 */
            moneyChange(money){
                if (isEmpty(money))return 0;
                let previous = (money / 100).toString().split('.');
                let len = previous[0].length;
                let lastIndex = 0;
                let arr = [];
                while (len > 0) {
                    lastIndex = len;
                    len -= 3;
                    arr.unshift(previous[0].substring(len, lastIndex));
                }
                return previous[1] ? arr.join(',') + '.' + previous[1] : arr.join(',');
            },
            /* 商家列表 */
            handleBusinessSelect(id){
                this.businessId = id;
                this.menuTab = false;

                this.getMessage();
            },
            /* 点击查看更多 */
            handleShowMore(){
                let loading = this.$modal();
                this.getDatesList(true, () => {
                    loading.close();
                })
            },
            /* 日期确认 */
            handleConfirmTime(){
                let stime = new Date(this.startTime).getTime();
                let etime = new Date(this.endTime).getTime();
                if (stime - etime > 0) {
                    this.$modal({
                        type: 'error',
                        message: '开始时间大于结束时间',
                        duration: 1200
                    });
                    return;
                }
                this.paymentPage.startTime = this.startTime ? `${this.startTime} 00:00:00` : '';
                this.paymentPage.endTime = this.endTime ? `${this.endTime} 23:59:59` : '';
                this.menuTab = false;
                this.getMessage();

            },
            //取消时间
            handleCancleTime(){
                this.paymentPage.startTime = '';
                this.paymentPage.endTime = '';
                this.startTime = '';
                this.endTime = '';
                this.menuTab = false;
                this.getMessage();
            },
            getMessage(){
                const loading = this.$modal();
                const f1 = new Promise((resolve) => {
                    this.getDatesList(false, () => {
                        resolve();
                    }, loading)
                });
                const f2 = new Promise((resolve) => {
                    this.getDatesTotal(() => {
                        resolve();
                    }, loading)
                });
                Promise.all([f1, f2]).then(() => {
                    loading.close();
                    this.isLoading = false;
                })
            }
        },
        created() {
            const loading = this.$modal();
            const f1 = new Promise((resolve) => {
                this.getDatesList(false, () => {
                    resolve();
                }, loading)
            });
            const f2 = new Promise((resolve) => {
                this.getDatesTotal(() => {
                    resolve();
                }, loading)
            });
            const f3 = new Promise((resolve) => {
                this.getDatesBusiness(() => {
                    resolve();
                });
            }, loading);
            Promise.all([f1, f2, f3]).then(() => {
                loading.close();
                this.isLoading = false;
            })


        },
    }
</script>

<style lang="less">
    @import './style.less';
</style>
