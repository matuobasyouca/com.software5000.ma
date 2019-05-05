<template>
    <div class="financial-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">财务对账<span class="new-project" @click="makePagRecord=true">记支出</span>
        </ma-head>
        <pull-menu
                v-model="menuTab"
                p1="来源"
                p2="日期">
            <ul slot="p1" class="merchant-list">
                <li :class="[{'curr':financialCondition.financeType == ''}]" @click="handleFinanceType('')">不限</li>
                <li :class="[{'curr':financialCondition.financeType == msg[0]}]"
                    v-for="(msg,index) in selectFinanceType"
                    :key="index"
                    @click="handleFinanceType(msg[0])">
                    {{msg[1]}}
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
        <div class="financial-header">
            <ul>
                <li>
                    <div class="nr">
                        <div class="num">{{handleChangeMoney(moneyDatas.inComeMoney)}}</div>
                        <div class="zi">总收入（元）</div>
                    </div>
                    <div class="xian"></div>
                </li>
                <li>
                    <div class="nr">
                        <div class="num">{{handleChangeMoney(moneyDatas.outComeMoney)}}</div>
                        <div class="zi">总支出（元）</div>
                    </div>
                </li>
            </ul>
        </div>
        <div class="financial-title">财务明细</div>
        <div class="financial-body">
            <ul v-if="formDatas.length > 0" class="financial-ul">
                <li v-for="(item,index) in formDatas">
                    <div class="row">
                        <div class="row-h3">{{item.financeTypeDes}}<span class="explainInfo" v-if="item.explainInfo">（{{item.explainInfo}}）</span>
                        </div>
                        <div class="row-money">{{item.money != 0 ? item.inOrOutCome == 1 ? `+${item.money/100}` :
                            `-${item.money/100}` : 0}}
                        </div>
                    </div>
                    <div class="row">
                        <div class="row-time">{{item.createTime}}</div>
                        <div class="row-payWay">{{item.payTypeDes}}</div>
                    </div>
                </li>
                <div
                        class="show_all_css"
                        :class="[{'show_more_css' : hasNextPage}]"
                        @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                </div>
            </ul>
            <div v-else>
                <ul class="data-empty">
                    <li>暂无财务信息</li>
                </ul>
            </div>
        </div>

        <!--下一页-->
        <zs-slide-page class="pay-record" v-model="makePagRecord" title="记支出">
            <div class="new-pay-record">
                <ul class="sec1-ul">
                    <li class="sec1-li"><span class="li-title"><em>*</em>支出金额</span>
                        <zs-input placeholder="请输入支出金额" v-model="money" :maxlength="20" type="number"></zs-input>
                    </li>
                    <li class="sec1-li"><span class="li-title">支出说明</span>
                        <zs-input placeholder="请输入支出说明" v-model="explainInfo" :maxlength="15"></zs-input>
                    </li>
                </ul>
                <div class="new-botton">
                    <zs-button class="btn-click" type="primary" @click="insertPayRecord">确定</zs-button>
                </div>
            </div>
        </zs-slide-page>
    </div>
</template>

<script type="text/ecmascript-6">
    import { turnToNextPage, isEmpty, moneyFormat } from '../../../../../assets/js/utils';
    import pullMenu from '../../../../../components_proj/pull_menu/app.vue';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead, pullMenu
        },
        props: {},
        data(){
            return {
                isLoading: false,
                financialCondition: {
                    orderBy: 'id desc',
                    startPage: 1,
                    pageSize: 10,
                    createTimeStart: '',
                    createTimeEnd: '',
                    financeType: '',
                    payType: ''
                },
                formDatas: [],
                hasNextPage: false,
                moneyDatas: [],
                workOrderPayType: [],
                menuTab: false,
                selectFinanceType: [],
                startTime: '',
                endTime: '',
                makePagRecord: false,
                money: '', //记支出金额
                explainInfo: '' //记支出说明
            }
        },
        computed: {},
        watch: {
            makePagRecord(){
                this.money = '';
                this.explainInfo = ''
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 添加记支出
             */
            insertPayRecord(){
                if (isEmpty(this.money)) {
                    this.$message({
                        type: 'error',
                        duration: 1000,
                        message: '请输入支出金额'
                    });
                    return false;
                }

                this.$ajax(
                    this.$joggle.merchant.finance.InsertFinaceOtherPay,
                    {
                        money: parseFloat(this.money).toFixed(2) * 100,
                        explainInfo: this.explainInfo
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                duration: 1200,
                                message: data.msg
                            });
                            this.makePagRecord = false;
                            this.handleGetMessage(loading);

                        } else {
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            })
                        }
                    }
                )
            },
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 查询财务明细信息
             * @param isShowMore
             * @param fn
             * @param loading
             */
            selectFormDatas(isShowMore, fn, loading){
                if (isShowMore) {
                    this.financialCondition.startPage++;
                } else {
                    this.financialCondition.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.finance.selectPageFinance,
                    this.financialCondition,
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.formDatas = isShowMore ? this.formDatas.concat(data.data.list) : data.data.list;
                            this.hasNextPage = data.data.hasNextPage;
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            })
                        }
                    }
                )
            },
            /**
             * 查询财务明细总支出总收入
             * @param fn
             * @param loading
             */
            selectMoneyDatas(fn, loading){
                let totalCondition = {
                    createTimeStart: this.startTime ? `${this.startTime} 00:00:00` : '',
                    createTimeEnd: this.endTime ? `${this.endTime} 23:59:59` : '',
                    financeType: this.financialCondition.financeType
                };

                this.$ajax(
                    this.$joggle.merchant.finance.selectFinanceInOrOutComeDto,
                    totalCondition,
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.moneyDatas = data.data;
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            })
                        }
                    }
                )
            },
            /**
             * 获取下拉筛选数据
             * @param fn
             * @param loading
             */
            selectOptionMsg(fn, loading){
                let data = { codeTypes: 'FinanceType' };

                this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    JSON.stringify(data),
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.selectFinanceType = data.data.FinanceType;
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            })
                        }
                    });
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
            /**
             * 来源列表点击事件
             * @param financeType
             */
            handleFinanceType(financeType){
                this.financialCondition.financeType = financeType;
                this.menuTab = false;
                const loading = this.$loading();

                this.handleGetMessage(loading);
            },
            /**
             * 日期确认点击事件
             */
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
                this.financialCondition.createTimeStart = this.startTime ? `${this.startTime} 00:00:00` : '';
                this.financialCondition.createTimeEnd = this.endTime ? `${this.endTime} 23:59:59` : '';
                this.menuTab = false;
                const loading = this.$loading();

                this.handleGetMessage(loading);
            },
            /**
             * 取消时间
             */
            handleCancleTime(){
                this.financialCondition.createTimeStart = '';
                this.financialCondition.createTimeEnd = '';
                this.startTime = '';
                this.endTime = '';
                this.menuTab = false;
                const loading = this.$loading();

                this.handleGetMessage(loading);
            },
            /**
             * 点击查看更多
             */
            handleShowMore(){
                if (this.hasNextPage) {
                    const loading = this.$loading();

                    this.selectFormDatas(true, () => {
                        loading.close();
                    }, loading)
                }
            },
            /**
             * 返回首页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 金钱的格式化
             */
            handleChangeMoney(money){
               return moneyFormat(money);
            },
            /**
             * 辅助操作
             */
            handleGetMessage(loading){
                const f1 = new Promise((resolve) => {
                    this.selectMoneyDatas(() => {
                        resolve();
                    }, loading)
                });
                const f2 = new Promise((resolve) => {
                    this.selectFormDatas(false, () => {
                        resolve();
                    }, loading)
                });
                const f3 = new Promise((resolve) => {
                    this.selectOptionMsg(() => {
                        resolve();
                    }, loading)
                });

                Promise.all([f1, f2, f3]).then(() => {
                    loading.close();
                })
            }
        },
        created(){
            const loading = this.$loading();

            this.handleGetMessage(loading);
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
