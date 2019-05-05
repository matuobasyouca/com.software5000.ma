<template>
    <div class="unpaid buy_history">
        <div class="search-box clr">
            <span>结算时间</span>
            <date-picker placeholder="请选择开始时间" v-model="condition.beginTime"></date-picker>
            <span>-</span>
            <date-picker placeholder="请选择结束时间" v-model="condition.endTime"></date-picker>
            <zs-input
                    v-model.trim="condition.keyWord"
                    @keydown.enter="handleConditionGetData"
                    placeholder="请输入客户姓名、手机号、车牌关键词"></zs-input>
            <zs-button type="primary" @click="handleConditionGetData()">
                <zs-icon icon="search2" :size="20"></zs-icon>
            </zs-button>
        </div>
        <zs-table
                class="package-table"
                :data="datas"
                :columns="columns"
                :context="context"
                :no-data-colspan="10"
                border>
        </zs-table>
        <div class="pay_list-pagination">
            <zs-pagination
                    class="main-bottom"
                    :current="current"
                    :total="total"
                    :page-size-opts="[10,20,50,100]"
                    show-total
                    show-elevator
                    show-sizer
                    @on-change="handlePageChange"
                    @on-page-size-change="handlePageSizeChange"
            >
            </zs-pagination>
        </div>
    </div>
</template>

<script>
    import { turnToNextPage } from '../../../../../../../assets/js/utils.js';

    export default {
        name: 'buy_history',
        components: {},
        data() {
            return {
                datas: [],
                columns: [
                    {
                        title: '订单编号',
                        key: 'orderNumber',
                        width: 134
                    },
                    {
                        title: '车牌',
                        key: 'carNumbers',
                        className: 'text-hide',
                        width: 224
                    },
                    {
                        title: '客户手机号',
                        key: 'mobile',
                        width: 114
                    },
                    {
                        title: '客户姓名',
                        key: 'user',
                        width: 114,
                        render(row) {
                            return row.user.realName;
                        }
                    },
                    {
                        title: '充值金额',
                        key: 'reChargeMoney',
                        width: 124
                    },
                    {
                        title: '赠送金额',
                        key: 'grantMoney',
                        width: 124
                    },
                    {
                        title: '收款方式',
                        key: 'payType',
                        width: 124,
                        render(row) {
                            return row.payType == 2 ? `<span class="gray">微信收款</span>` : '现金收款';
                        }
                    },
                    {
                        title: '结算时间',
                        key: 'updateTime',
                        width: 150,
                        render(row) {
                            return row.updateTime.slice(0, 19);
                        }
                    },
                    {
                        title: '操作',
                        key: 'like',
                        width: 140,
                        className: 'control',
                        render(row) {
                            let details = `<span @click="handleTurnPage('/web/merchant/home/recharge/detail.html',${row.id},true)">查看</span>`;

                            return `${details}`;
                        }
                    }
                ],
                context: this,
                currIndex: 1,

                current: 1,
                pageSize: 10,
                total: 0,
                condition: {
                    keyWord: '',
                    beginTime: '',
                    endTime: ''
                },
                sendCondition: {
                    keyWord: '',
                    beginTime: '',
                    endTime: ''
                }
            };
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/

            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/

            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/
            /**
             *  获取购买记录数据
             * @param fn  后续操作
             */
            selectDatas(fn) {
                let paras = this.sendCondition;

                paras.startPage = this.current;
                paras.pageSize = this.pageSize;
                this.$ajax(
                    this.$joggle.merchant.member.selectRechargeOrderPage,
                    paras,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            Object.assign(this.condition, this.sendCondition);
                            this.datas = data.data.list;
                            this.total = data.data.total;
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
            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             *  根据条件查找数据点击事件
             */
            handleConditionGetData() {
                this.current = 1;
                Object.assign(this.sendCondition, this.condition);
                this.selectDatas();
            },
            //跳转至会员详情
            handleTurnPage(url, id, newPage) {
                let para = { id };

                turnToNextPage(url, para, newPage);
            },
            /**
             * 页码切换
             */
            handlePageChange(currPage) {
                this.current = currPage;
                let loading = this.$loading();

                this.selectDatas(() => {
                    loading.close();
                });
            },
            /**
             * 每页数据条数切换
             * @param pageSize  当前列表显示的条数
             */
            handlePageSizeChange(pageSize) {
                this.current = 1;
                this.pageSize = pageSize;
                let loading = this.$loading();

                this.selectDatas(() => {
                    loading.close();
                });
            }
            /* ------------------- handle (操) end -------------------*/

        },
        mounted() {
            this.selectDatas();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
