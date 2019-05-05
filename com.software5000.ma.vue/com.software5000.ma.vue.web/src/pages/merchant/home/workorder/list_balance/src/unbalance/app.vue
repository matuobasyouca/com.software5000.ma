<template>
    <div class="page-main">
        <zs-table
                :data="unbalanceData"
                :columns="unbalanceColumns"
                :context="context"
                :no-data-colspan="9"
                no-data-text="暂无工单"
                border></zs-table>
        <zs-pagination
                v-if="unbalanceData.length > 0"
                slot="footer"
                :current="unbalancePage.startPage"
                :total="unbalancePage.total"
                :page-size="unbalancePage.pageSize"
                @on-change="handlePageChange"
                @on-page-size-change="handlePageSizeChange"
                show-total
                show-elevator
                show-sizer
        ></zs-pagination>
    </div>
</template>

<script>
    import { turnToNextPage } from '../../../../../../../assets/js/utils';
    export default {
        name: 'unbalance',
        data(){
            return {
                context: this,
                unbalanceData: [],
                unbalanceColumns: [
                    {
                        title: '工单编号',
                        key: 'orderNo',
                        width: 130
                    },
                    {
                        title: '车牌号码',
                        key: 'carNumber',
                        width: 90
                    },
                    {
                        title: '手机号',
                        width: 108,
                        render(row){
                            return row.mobile || '----';
                        }
                    },
                    {
                        title: '客户姓名',
                        width: 100,
                        render(row){
                            return row.realName || '--';
                        }
                    },
                    {
                        title: '施工项目',
                        key: 'itemNames',
                        className: 'item-name',
                        width: 300
                    },
                    {
                        title: '费用',
                        key: 'totalPrice',
                        width: 96
                    },
                    {
                        title: '开单时间',
                        key: 'createTime',
                        width: 158
                    },
                    {
                        title: '完工时间',
                        key: 'commitTime',
                        width: 160
                    },
                    {
                        title: '操作',
                        width: 98,
                        className: 'table-control',
                        render(row){
                            return `<a @click.stop="handleToSettle(${row.id})">去结算</a>
                                    <a @click.stop="handleDelOrder(${row.id})">删除</a>`;
                        }
                    }
                ],
                unbalancePage: {
                    startPage: 1,
                    pageSize: 10,
                    total: 0
                }
            };
        },
        methods: {
            //startPage切换
            handlePageChange(page){
                this.unbalancePage.startPage = page;
                this.selectWorkOrder();
            },
            //pageSize切换
            handlePageSizeChange(pageSize){
                this.unbalancePage.pageSize = pageSize;
                this.selectWorkOrder();
            },
            //获取未结算工单
            selectWorkOrder(fn){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderByPage,
                    {
                        state: 3,
                        startPage: this.unbalancePage.startPage,
                        pageSize: this.unbalancePage.pageSize
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.unbalancePage.total = data.data.total;
                            this.unbalanceData = data.data.list;
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
            //去结算
            handleToSettle(id){
                turnToNextPage('/web/merchant/home/workorder/detail_pay.html', { id });
            },
            //删除工单
            handleDelOrder(id){
                this.$confirm({
                    title: '删除',
                    type: 'delete',
                    message: '确认删除该工单?'
                }).then(() => {
                    this.$ajax(
                        this.$joggle.merchant.workorder.deleteWorkOrderById,
                        { orderId: id },
                        true,
                        (data, loading) => {
                            if (data.code === 'ZS011000') {
                                if (this.unbalanceData.length === 1 && this.unbalancePage.startPage > 1) {
                                    this.unbalancePage.startPage -= 1;
                                }
                                this.selectWorkOrder((data) => {
                                    this.$message({
                                        type: 'success',
                                        message: '删除成功!',
                                        duration: 1200
                                    });
                                    loading.close();
                                }, loading);

                            } else {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                });
                            }
                        }
                    );
                }).catch(() => {
                });
            }
        },
        created(){
            this.selectWorkOrder(() => {
                this.$emit('input', false);
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
