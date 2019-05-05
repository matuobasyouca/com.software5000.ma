<template>
    <div class="unpaid buy_history">
        <div class="search-box clr">
            <span>结算时间</span>
            <date-picker placeholder="请选择开始时间" v-model="condition.beginTime"></date-picker>
            <span>-</span>
            <date-picker placeholder="请选择结束时间" v-model="condition.endTime"></date-picker>
            <zs-input v-model.trim="condition.keyWord" @keydown.enter="conditionGetData"
                      placeholder="请输入客户姓名、手机号、车牌关键词"></zs-input>
            <zs-button type="primary" @click="conditionGetData()">
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
                        width: 136
                    },
                    {
                        title: '客户手机号',
                        key: 'mobile',
                        width: 120
                    },
                    {
                        title: '客户姓名',
                        key: 'realName',
                        width: 112
                    },
                    {
                        title: '购买套餐',
                        key: 'packageName',
                        width: 158
                    },
                    {
                        title: '实收金额',
                        key: 'salePrice',
                        width: 130
                    },
                    {
                        title: '收款方式',
                        key: 'payType',
                        width: 98,
                        render(row) {
                            return row.payType == 2 ? `<span class="gray">微信收款</span>` : '现金收款';
                        }
                    },
                    {
                        title: '提成人员',
                        key: 'saleName',
                        width: 112
                    },
                    {
                        title: '下单时间',
                        key: 'createTime',
                        width: 136,
                        render(row) {
                            return row.createTime.slice(0, 19);
                        }
                    },
                    {
                        title: '结算时间',
                        key: 'updateTime',
                        width: 136,
                        render(row) {
                            return row.updateTime.slice(0, 19);
                        }
                    },
                    {
                        title: '操作',
                        key: 'like',
                        width: 110,
                        className: 'control',
                        render(row) {
                            let details = `<span @click="turnPage('/web/merchant/home/member/package_billing.html',${row.id},true)">查看</span>`;

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
            //获取购买记录数据
            getDatas(fn) {
                let paras = this.sendCondition;

                paras.starPage = this.current;
                paras.pageSize = this.pageSize;
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectPaidBusinessPackageOrder,
                    paras,
                    true,
                    (data, model) => {
                        model.close();
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
            conditionGetData() {
                this.current = 1;
                Object.assign(this.sendCondition, this.condition);
                this.getDatas();
            },
            //跳转至会员详情
            turnPage(url, id, newPage) {
                let para = { id };

                turnToNextPage(url, para, newPage);
            },
            /**
             * 页码切换
             */
            handlePageChange(currPage) {
                this.current = currPage;
                let loading = this.$loading();

                this.getDatas(() => {
                    loading.close();
                });
            },
            /**
             * 每页数据条数切换
             */
            handlePageSizeChange(pageSize) {
                this.current = 1;
                this.pageSize = pageSize;
                let loading = this.$loading();

                this.getDatas(() => {
                    loading.close();
                });
            }
        },
        mounted() {
            this.getDatas();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
