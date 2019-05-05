<template>
    <div class="financial-accounts">
        <div class="account">
            <div class="get">
                <span class="search_label">时间</span>
                <date-picker
                        class="select-new-time"
                        placeholder="请选择时间"
                        :disabledDate="-1"
                        @on-change="handleSetStartTime"
                ></date-picker>
                <span class="search_label">至</span>
                <date-picker
                        class="select-new-time"
                        placeholder="请选择时间"
                        :disabledDate="-1"
                        @on-change="handleSetEndTime"
                ></date-picker>
            </div>
            <div class="get">
                <span class="search_label">收入/支出方式</span>
                <zs-select
                        class="select-form-select"
                        placeholder="选择收入/支出方式"
                        v-model="makeFinancialCondition.payType"
                        @change="">
                    <zs-option value="">不限</zs-option>
                    <zs-option v-for="(item,index) in workOrderPayType" :key="index" :value="item[0]"
                               :label="item[1]"></zs-option>
                </zs-select>
            </div>
            <div class="get">
                <span class="search_label">来源</span>
                <zs-select
                        class="select-form-select form-long"
                        placeholder="选择来源"
                        v-model="makeFinancialCondition.financeType"
                        @change="">
                    <zs-option value="">不限</zs-option>
                    <zs-option v-for="(item,index) in financeType" :key="index" :value="item[0]"
                               :label="item[1]"></zs-option>
                </zs-select>
            </div>
            <div class="get">
                <div class="search-btn" @click="handleSearchDatas">
                    <zs-icon icon="search2" size="20"></zs-icon>
                </div>
            </div>
        </div>
        <div class="account-header">
            <div class="totalGet header-list">
                <div class="list-img"></div>
                <div class="list-t">总收入（元）</div>
                <div class="list-m">{{handleMoneyChange(totalMoney.inComeMoney)}}</div>
            </div>
            <div class="totalCost header-list">
                <div class="list-img"></div>
                <div class="list-t">总支出（元）</div>
                <div class="list-m">{{handleMoneyChange(totalMoney.outComeMoney)}}</div>
            </div>
        </div>
        <div class="account-table">
            <div class="table-select">
                <p class="title">财务明细</p>
                <p class="aside-btn" @click="payRecord=true">
                    <zs-icon icon="write" :size="20"></zs-icon>
                    <span>记支出</span></p>
            </div>
            <div class="list-content">
                <zs-table
                        :data="fromData"
                        :columns="columns"
                        :context="context"
                        :no-data-colspan="5"
                        border
                        class="employee-table"
                >
                </zs-table>
            </div>
            <div class="page-div" v-if="fromData.length>0">
                <zs-pagination
                        :current="startPage"
                        :total="total"
                        :page-size="pageSize"
                        show-total
                        show-elevator
                        show-sizer
                        @on-change="handlePageNum"
                        @on-page-size-change="handlePageSize"
                ></zs-pagination>
            </div>
        </div>

        <!-- 记支出弹窗 -->
        <zs-dialog
                class="pay-record-dialog"
                v-model="payRecord"
                show-close>
            <p slot="title">记支出</p>
            <div class="dialog-body">
                <ul class="payRecord">
                    <li class="payRecord-li">
                        <div class="li-title"><em>*</em>支出金额</div>
                        <zs-input placeholder="请输入支出金额" v-model="money" type="number" :maxlength="20"></zs-input>
                    </li>
                    <li class="payRecord-li">
                        <div class="li-title">支出说明</div>
                        <zs-input placeholder="请输入支出说明" v-model="explainInfo" :maxlength="15"></zs-input>
                        <span class="info-length">{{explainInfo.length}}/15</span></li>
                </ul>
            </div>
            <template slot="footer">
                <div class="foot-btn">
                    <zs-button
                            type="cancel"
                            @click="payRecord = false">取消
                    </zs-button>
                    <zs-button
                            type="primary"
                            @click="insertPayRecord">确定
                    </zs-button>
                </div>

            </template>
        </zs-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
    import { isEmpty, DateUtils } from '../../../../../../../assets/js/utils';
    export default{
        name: 'list',
        components: {},
        props: {},
        data(){
            return {
                binding: false,
                context: this,
                total: 0,
                columns: [
                    {
                        title: '来源',
                        key: 'financeTypeDes',
                        align: 'center',
                        width: 140
                    },
                    {
                        title: '说明',
                        key: 'explainInfo',
                        align: 'center',
                        width: 300
                    },
                    {
                        title: '金额',
                        key: 'money',
                        align: 'center',
                        width: 198,
                        render(row){
                            let money = row.inOrOutCome == 1 ? `+${row.money / 100}` : `-${row.money / 100}`;

                            return row.money ? money : 0;
                        }
                    },
                    {
                        title: '收入/支出方式',
                        key: 'payTypeDes',
                        align: 'center',
                        width: 164,
                        render(row){
                            return row.payTypeDes ? row.payTypeDes.substring(0, 2) : '';
                        }
                    },
                    {
                        title: '时间',
                        key: 'createTime',
                        align: 'center',
                        width: 172
                    }
                ],
                totalMoney: [],

                orderBy: 'id desc',
                startPage: 1,
                pageSize: 10,
                financialCondition: {
                    createTimeStart: '',
                    createTimeEnd: '',
                    financeType: '',
                    payType: ''
                },
                makeFinancialCondition: {
                    createTimeStart: '',
                    createTimeEnd: '',
                    financeType: '',
                    payType: ''
                },
                fromData: [],
                workOrderPayType: [],
                financeType: [],

                payRecord: false,
                money: '', //记支出金额
                explainInfo: '' //记支出说明
            }
        },
        computed: {},
        watch: {
            payRecord(){
                this.money = '';
                this.explainInfo = '';
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 添加记支出
             */
            insertPayRecord(){
                let errorMsg = '';

                if (isEmpty(this.money)) {
                    errorMsg = '请输入支出金额';

                } else if(parseFloat(this.money)<=0){
                    errorMsg = '输入的金额需大于0元';
                }
                if(!isEmpty(errorMsg)){
                    this.$message({
                        type: 'error',
                        duration: 1000,
                        message: errorMsg
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
                    (data) => {
                        if (data.code == 'ZS011000') {
                            this.$message({
                                type: 'success',
                                duration: 1200,
                                message: data.msg
                            });
                            this.payRecord = false;
                            this.startPage = 1;

                            this.handleUpdate(this.financialCondition);
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
             * 搜索总收入总支出的数据
             * @param fn
             * @param loading
             * @param condition
             */
            selectTotalDatas(fn, loading, condition){
                let totalCondition = {};

                totalCondition = condition ? condition : {};
                this.$ajax(
                    this.$joggle.merchant.finance.selectFinanceInOrOutComeDto,
                    totalCondition,
                    loading,
                    (data, loading) => {
                        if (data.code == 'ZS011000') {
                            this.totalMoney = data.data;
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
             * 搜索财务明细表格的数据
             * @param fn
             * @param loading
             */
            selectFormDatas(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.finance.selectPageFinance,
                    {
                        ...this.financialCondition,
                        orderBy: this.orderBy,
                        startPage: this.startPage,
                        pageSize: this.pageSize
                    },
                    loading,
                    (data, loading) => {
                        if (data.code == 'ZS011000') {
                            this.fromData = data.data.list;
                            this.total = data.data.total;
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
                let data = { codeTypes: 'WorkOrderPayType,FinanceType' };

                this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    JSON.stringify(data),
                    loading,
                    (data) => {
                        if (data.code == 'ZS011000') {
                            this.workOrderPayType = data.data.WorkOrderPayType;
                            this.financeType = data.data.FinanceType;
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
            /**
             * 搜索前判断处理
             */
            handleSearchDatas(){
                let begin = DateUtils.dateToRegular(this.makeFinancialCondition.createTimeStart).getTime();
                let end = DateUtils.dateToRegular(this.makeFinancialCondition.createTimeEnd).getTime();

                if (end < begin) {
                    this.$message({
                        type: 'error',
                        message: '浏览结束时间不能大于开始时间'
                    });
                    return false;
                }
                this.startPage = 1;

                this.financialCondition = { ...this.makeFinancialCondition };
                this.handleUpdate(this.makeFinancialCondition);
            },
            /**
             * 开始时间
             * @param time
             */
            handleSetStartTime(time){
                this.makeFinancialCondition.createTimeStart = time ? time + ' 00:00:00' : '';
            },
            /**
             * 结束时间
             * @param time
             */
            handleSetEndTime(time){
                this.makeFinancialCondition.createTimeEnd = time ? time + ' 23:59:59' : '';
            },
            /**
             * 钱格式转化
             * @param money
             */
            handleMoneyChange(money){
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
            /**
             * 切换当前页
             * @param pageNum
             */
            handlePageNum(pageNum){
                this.startPage = pageNum;
                this.makeFinancialCondition = { ...this.financialCondition };
                const loading = this.$loading();

                this.selectFormDatas(() => {
                    loading.close();
                }, loading);
            },
            /**
             * 改变每页的数量
             * @param pageSize
             */
            handlePageSize(pageSize){
                this.startPage = 1;
                this.pageSize = pageSize;
                this.makeFinancialCondition = { ...this.financialCondition };
                const loading = this.$loading();

                this.selectFormDatas(() => {
                    loading.close();
                }, loading);
            },
            /**
             * 辅助函数
             * @param condition
             */
            handleUpdate(condition){
                const loading = this.$loading();
                const f1 = new Promise((resolve) => {
                    this.selectTotalDatas(() => {
                        resolve();
                    }, loading, condition)
                });
                const f2 = new Promise((resolve) => {
                    this.selectFormDatas(() => {
                        resolve();
                    }, loading)
                });

                Promise.all([f1, f2]).then(() => {
                    loading.close();
                });
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                this.selectTotalDatas(() => {
                    resolve();
                }, loading)
            });
            const f2 = new Promise((resolve) => {
                this.selectFormDatas(() => {
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
        },
        mounted(){

        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>