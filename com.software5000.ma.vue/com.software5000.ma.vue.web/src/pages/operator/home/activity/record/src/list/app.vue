<template>
    <div class="group-record">
        <div class="record-header">
            <div class="summary">
                <zs-icon icon="warning" :size="16" :text="`共${allNum}个拼团，${successNum}个已拼团成功`"></zs-icon>
            </div>
            <div class="form-list">
                <date-picker
                        class="select-new-time"
                        v-model="condition.payTimeBegin"
                        placeholder="请选择开始时间"
                        :disabledDate="-1"
                        :value="condition.payTimeBegin"
                        @on-change="handleSetStartTime"
                ></date-picker>
                <span class="select_label">-</span>
                <date-picker
                        class="select-new-time"
                        v-model="condition.payTimeEnd"
                        placeholder="请选择结束时间"
                        :disabledDate="-1"
                        :value="condition.payTimeEnd"
                        @on-change="handleSetEndTime"
                ></date-picker>
                <zs-select
                        class="select-form__select"
                        plakceholder="拼团状态"
                        v-model="condition.state"
                        @change="">
                    <zs-option value="" label="请选择拼团状态"></zs-option>
                    <zs-option :value="item[0]" :label="item[1]" v-for="(item,index) in PackClusterOpenRecordState"
                               :key="index"></zs-option>
                </zs-select>
                <div class="r">
                    <zs-input v-model.trim="condition.keyWord" placeholder="请输入活动名、车牌、手机号关键词"
                              icon="circle-cross"></zs-input>
                    <zs-button type="primary" @click="handleSearchData">
                        <zs-icon icon="search2" :size="20"></zs-icon>
                    </zs-button>
                </div>
            </div>
        </div>
        <zs-table
                :data="datas"
                :columns="columns"
                :context="context"
                :no-data-colspan="7"
                border
                class="group-table"
                noDataText="暂无拼团记录"
                noDataIcon="data-empty"
        ></zs-table>
        <div class="page">
            <zs-pagination
                    :current="condition.startPage"
                    :total="totalPage"
                    :page-size="condition.pageSize"
                    show-total
                    show-elevator
                    show-sizer
                    @on-change="handlePageNum"
                    @on-page-size-change="handlePageSize"
            ></zs-pagination>
        </div>
    </div>
</template>

<script type="es6">
    import { isEmpty, imgPreLoad, turnToNextPage } from '../../../../../../../assets/js/utils';
    import { appointUrl } from '../../../../../../../assets/js/turnToHostPage';

    export default {
        name: 'list',
        components: {},
        props: {},
        data(){
            return {
                datas: [],
                context: this,
                columns: [
                    {
                        title: '拼团单号',
                        key: 'packClusterNo',
                        align: 'center',
                        width: 186
                    },
                    {
                        title: '拼团活动',
                        key: 'clusterName',
                        align: 'center',
                        width: 228
                    },
                    {
                        title: '车牌',
                        key: 'carNumber',
                        align: 'center',
                        width: 190
                    },
                    {
                        title: '手机号',
                        key: 'mobile',
                        align: 'center',
                        width: 166
                    },
                    {
                        title: '支付金额',
                        align: 'center',
                        width: 152,
                        render(row, col, index, _this){
                            return parseFloat(row.payMoney) / 100;
                        }
                    },
                    {
                        title: '拼团状态',
                        key: 'stateDesc',
                        align: 'center',
                        width: 134,
                        render(row, col, index, _this){
                            return row.state == 3 ? `<p style="color: #999">${row.stateDesc} </p>` : `<p>${row.stateDesc} </p>`;
                        }
                    },
                    {
                        title: '支付时间',
                        key: 'payTime',
                        align: 'center',
                        width: 194,
                        render(row, col, index, _this){
                            return `${row.payTime ? row.payTime.slice(0, 19) : ''}`;
                        }
                    }
                ],
                totalPage: 0,

                condition: {
                    payTimeBegin: '',
                    payTimeEnd: '',
                    state: '',
                    keyWord: '',
                    startPage: 1,
                    pageSize: 10
                },
                PackClusterOpenRecordState: [],
                allNum: '',
                successNum: ''
            };
        },
        computed: {},
        watch: {},
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 获取拼团记录列表
             * @param fn
             * @param loading
             */
            selectDates(fn, loading){
                this.$ajax(
                    this.$joggle.operator.activity.selectPagePackClusterBuyRecordByPage,
                    this.condition,
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.datas = data.data.list;
                            this.totalPage = data.data.total;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                        fn && fn();
                    }
                );
            },
            /**
             * 获取拼团记录成功数量数据
             * @param fn
             * @param loading
             */
            selectDatesTotel(fn, loading){
                this.$ajax(
                    this.$joggle.operator.activity.selectPackClusterOpenNum,
                    {},
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.allNum = data.data.allNum;
                            this.successNum = data.data.successNum;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                        fn && fn();
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 点击搜索
             */
            handleSearchData(){
                this.condition.startPage = 1;
                const loading = this.$loading();
                this.selectDates(() => {
                    loading.close();
                }, true);
            },
            /**
             * 开始日期的选择
             * @param time
             */
            handleSetStartTime(time){
                this.condition.payTimeBegin = time ? time + ' 00:00:00' : '';
            },
            /**
             * 结束日期的选择
             * @param time
             */
            handleSetEndTime(time){
                this.condition.payTimeEnd = time ? time + ' 23:59:59' : '';
            },
            /**
             * 切换页码
             * @param pageNum
             */
            handlePageNum(pageNum){
                this.condition.startPage = pageNum;
                const loading = this.$loading();
                this.selectDates(() => {
                    loading.close();
                }, true);
            },
            /**
             * 切换每页的数量
             * @param pageSize
             */
            handlePageSize(pageSize){
                this.condition.startPage = 1;
                this.condition.pageSize = pageSize;
                const loading = this.$loading();
                this.selectDates(() => {
                    loading.close();
                }, true)
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                this.selectDates(() => {
                    resolve();
                }, false);
            });
            const f2 = new Promise((resolve) => {
                this.selectDatesTotel(() => {
                    resolve();
                }, false);
            });
            Promise.all([f1, f2]).then(() => {
                loading.close();
            });

            //获取下拉筛选数据
            let data = { 'codeTypes': 'PackClusterOpenRecordState' };
            this.$ajax(
                this.$joggle.mzscp.selectConstantTypes,
                JSON.stringify(data),
                false,
                (data) => {
                    if (data.code == 'ZS011000') {
                        let datas = data.data;
                        this.PackClusterOpenRecordState = datas.PackClusterOpenRecordState;
                    } else {
                        this.$message({
                            type: 'error',
                            duration: 1200,
                            message: data.msg
                        });
                    }
                });
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>