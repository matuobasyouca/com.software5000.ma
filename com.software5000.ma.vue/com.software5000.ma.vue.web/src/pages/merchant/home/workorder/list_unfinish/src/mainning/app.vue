<template>
    <div class="page-main">
        <div class="page-left">
            <zs-table
                    :data="orderDatas"
                    :columns="orderColumns"
                    :context="context"
                    :no-data-colspan="6"
                    highlight-row
                    no-data-text="暂无工单"
                    border
                    @on-row-click="handleOrderSelect"></zs-table>
            <zs-pagination
                    v-if="orderDatas.length > 0"
                    slot="footer"
                    :current="orderPage.startPage"
                    :total="orderPage.total"
                    :page-size="orderPage.pageSize"
                    show-total
                    show-elevator
                    show-sizer
                    @on-change="handlePageChange"
                    @on-page-size-change="handlePageSizeChagne"
            ></zs-pagination>
        </div>
        <div class="page-right">
            <zs-table
                    :data="itemDatas"
                    :columns="itemColumns"
                    :context="context"
                    :no-data-colspan="5"
                    no-data-text="暂无服务项目"
                    border></zs-table>
            <zs-button v-if="itemDatas.length > 0" type="success" @click="handleSetFinish">施工完成</zs-button>
        </div>
    </div>
</template>

<script>
    import { turnToNextPage, doCopy } from '../../../../../../../assets/js/utils';
    export default {
        name: 'maining',
        data(){
            return {
                context: this,
                currOrderId: '',
                currCarNumber: '',
                //工单列表
                orderDatas: [],
                orderColumns: [
                    {
                        title: '工单编号',
                        key: 'orderNo',
                        width: 146
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
                        title: '开单时间',
                        key: 'createTime',
                        width: 146
                    },
                    {
                        title: '操作',
                        width: 110,
                        className: 'table-control',
                        render(row){
                            return `<a @click.stop="handleEdit(${row.id})">编辑</a><a @click.stop="handleOrderDel(${row.id})">删除</a>`;
                        }
                    }

                ],
                orderPage: {
                    startPage: 1,
                    pageSize: 10,
                    total: 0
                },
                //服务项目
                itemDatas: [],
                itemColumns: [
                    {
                        title: '服务项目',
                        key: 'serviceItemName',
                        width: 144
                    },
                    {
                        title: '数量',
                        key: 'itemsTimes',
                        width: 80
                    },
                    {
                        title: '单价',
                        width: 80,
                        render(row){
                            return parseFloat(row.salePrice).toFixed(2);
                        }
                    },
                    {
                        title: '施工人员',
                        width: 100,
                        render(row){
                            return row.workerName || '/';
                        }
                    },
                    {
                        title: '操作',
                        width: 114,
                        className: 'table-control',
                        render(row, rol, index){
                            return `<a @click.stop="handleItemDel(${row.id},${index})">删除</a>`;
                        }
                    }
                ]
            };
        },
        methods: {
            //startPage切换
            handlePageChange(page){
                this.orderPage.startPage = page;
                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //pageSize切换
            handlePageSizeChagne(pageSize){
                this.orderPage.pageSize = pageSize;
                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //获取未完成工单
            selectWorkOrder(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderByPage,
                    {
                        state: 1,
                        startPage: this.orderPage.startPage,
                        pageSize: this.orderPage.pageSize
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.orderPage.total = data.data.total;
                            const temp = data.data.list;

                            if (temp.length > 0) {
                                this.$set(temp[0], '_highlight', true);
                                this.currOrderId = temp[0].id;
                                this.currCarNumber = temp[0].carNumber;
                            }
                            this.orderDatas = temp;
                            fn && fn(temp);
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
            //去编辑
            handleEdit(id){
                turnToNextPage('/web/merchant/home/workorder/update.html', { id });
            },
            //删除工单
            handleOrderDel(id){
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
                                if (this.orderDatas.length === 1 && this.orderPage.startPage > 1) {
                                    this.orderPage.startPage -= 1;
                                }
                                this.selectWorkOrder((data) => {
                                    if (data.length > 0) {
                                        this.selectItems(() => {
                                            this.$message({
                                                type: 'success',
                                                message: '删除成功!',
                                                duration: 1200
                                            });
                                            loading.close();
                                        }, loading);
                                    } else {
                                        this.itemDatas = [];
                                        this.$message({
                                            type: 'success',
                                            message: '删除成功!',
                                            duration: 1200
                                        });
                                        loading.close();
                                    }
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
            },
            //选中工单
            handleOrderSelect(row){
                this.currOrderId = row.id;
                this.currCarNumber = row.carNumber;
                const loading = this.$loading();

                this.selectItems(() => {
                    loading.close();
                }, loading);
            },
            //获取工单项目
            selectItems(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderById,
                    { orderId: this.currOrderId },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.itemDatas = data.data.workOrder.workOrderDetails;
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
            //删除项目
            handleItemDel(id, index){
                this.$confirm({
                    title: '删除',
                    type: 'delete',
                    message: this.itemDatas.length === 1 ? '最后一个项目删除后该工单将被删除,确认删除?' : '确认删除该施工项目'
                }).then(() => {
                    this.$ajax(
                        this.$joggle.merchant.workorder.deleteItemById,
                        { orderId: this.currOrderId, detailId: id },
                        true,
                        (data, loading) => {
                            if (data.code === 'ZS011000') {
                                if (this.itemDatas.length === 1) {
                                    if (this.orderDatas.length === 1 && this.orderPage.startPage > 1) {
                                        this.orderPage.startPage -= 1;
                                    }
                                    this.selectWorkOrder((data) => {
                                        if (data.length > 0) {
                                            this.selectItems(() => {
                                                this.$message({
                                                    type: 'success',
                                                    message: '删除成功!',
                                                    duration: 1200
                                                });
                                                loading.close();
                                            }, loading);
                                        } else {
                                            this.itemDatas = [];
                                            this.$message({
                                                type: 'success',
                                                message: '删除成功!',
                                                duration: 1200
                                            });
                                            loading.close();
                                        }
                                    }, loading);
                                } else {
                                    const temp = doCopy(this.itemDatas);

                                    this.$delete(temp, index);
                                    this.itemDatas = temp;
                                    loading.close();
                                    this.$message({
                                        type: 'success',
                                        message: '删除成功!',
                                        duration: 1200
                                    });
                                }
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
            },
            //施工完成
            handleSetFinish(){
                this.$ajax(
                    this.$joggle.merchant.workorder.updateWorkOrderForFinish,
                    { orderId: this.currOrderId },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: '施工完成',
                                duration: 1200
                            });
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/workorder/detail_pay.html', { id: this.currOrderId });
                            }, 1200);
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
            }
        },
        created(){
            const loading = this.$loading();

            this.selectWorkOrder((data) => {
                if (data.length > 0) {
                    this.selectItems(() => {
                        loading.close();
                        this.$emit('input', false);
                    }, loading);
                } else {
                    loading.close();
                    this.$emit('input', false);
                }
            }, loading);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
