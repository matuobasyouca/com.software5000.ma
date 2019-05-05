<template>
    <div class="page-main">
        <div class="page-main-form">
            <zs-button class="download-btn" type="primary">
                <zs-icon icon="download" size="20" text="导出表格" @click="handleDownLoad"></zs-icon>
            </zs-button>
            <zs-button class="search-btn fr" type="primary" @click="handleSearch">
                <zs-icon icon="search2" size="19"></zs-icon>
            </zs-button>
            <zs-input
                    class="search-input fr"
                    icon="circle-cross"
                    v-model.trim="tempData.searchKey"
                    placeholder="请输入手机号、车牌号关键词"></zs-input>
            <zs-select class="pay-type-select fr" v-model="tempData.payType" placeholder="收款方式选择">
                <zs-option value="-1" label="不限"></zs-option>
                <zs-option :value="1" label="现金收款"></zs-option>
                <zs-option :value="2" label="微信支付"></zs-option>
            </zs-select>
            <date-picker class="date-picker fr" v-model="tempData.endTime" placeholder="请选择结束时间"></date-picker>
            <span class="fr">-</span>
            <date-picker class="date-picker fr" v-model="tempData.startTime" placeholder="请选择开始时间"></date-picker>
            <span class="fr">结算时间</span>
        </div>
        <zs-table
                :data="settledData"
                :columns="settledColumns"
                :context="context"
                :no-data-colspan="9"
                no-data-text="暂无工单"
                border></zs-table>
        <zs-pagination
                v-if="settledData.length > 0"
                slot="footer"
                :current="settledPage.startPage"
                :total="settledPage.total"
                :page-size="settledPage.pageSize"
                show-total
                show-elevator
                show-sizer
                @on-change="handlePageChange"
                @on-page-size-change="handlePageSizeChange"
        ></zs-pagination>
    </div>
</template>

<script>
    import { isEmpty, DateUtils, turnToNextPage } from '../../../../../../../assets/js/utils';
    export default {
        name: 'settled',
        data(){
            return {
                context: this,
                closingDate: 100,
                settledData: [],
                settledColumns: [
                    {
                        title: '工单编号',
                        key: 'orderNo',
                        width: 146
                    },
                    {
                        title: '车牌号码',
                        key: 'carNumber',
                        width: 88
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
                        width: 102,
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
                        title: '实收金额',
                        key: 'totalPrice',
                        width: 92
                    },
                    {
                        title: '收款方式',
                        width: 88,
                        render(row){
                            const payTypeMap = ['', '现金收款', '微信支付'];

                            return payTypeMap[row.payType || 0];
                        }
                    },
                    {
                        title: '结算时间',
                        key: 'payTime',
                        width: 178
                    },
                    {
                        title: '操作',
                        width: 146,
                        className: 'table-control',
                        render(row, col, index){
                            const untiSettleHtml = row.opposite ? `<a @click.stop="handleAntiSettled(${row.id},${index})">反结算</a>` : '';

                            return `<a @click.stop="handleCheck(${row.id})">查看</a>${untiSettleHtml}`;
                        }
                    }
                ],
                settledPage: {
                    startPage: 1,
                    pageSize: 10,
                    total: 10
                },
                tempData: {
                    state: 4,
                    startTime: '',
                    endTime: '',
                    payType: '',
                    searchKey: ''
                },
                excelData: {
                    state: 4,
                    startTime: '',
                    endTime: '',
                    payType: '',
                    searchKey: ''
                }
            };
        },
        methods: {
            //startPage切换
            handlePageChange(page){
                this.settledPage.startPage = page;
                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //pageSize切换
            handlePageSizeChange(pageSize){
                this.settledPage.pageSize = pageSize;
                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //搜索
            handleSearch(){
                this.settledPage.startPage = 1;
                const newTimeArr = DateUtils.swapTime(this.tempData.startTime, this.tempData.endTime);

                this.tempData.startTime = newTimeArr[0];
                this.tempData.endTime = newTimeArr[1];

                Object.assign(this.excelData, this.tempData);

                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //获取结算工单
            selectWorkOrder(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderByPage,
                    {
                        state: 4,
                        startPage: this.settledPage.startPage,
                        pageSize: this.settledPage.pageSize,
                        startTime: isEmpty(this.excelData.startTime) ? '' : `${this.excelData.startTime} 00:00:00`,
                        endTime: isEmpty(this.excelData.endTime) ? '' : `${this.excelData.endTime} 23:59:59`,
                        searchKey: this.excelData.searchKey,
                        payType: this.excelData.payType == -1 ? '' : this.excelData.payType
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.settledPage.total = data.data.total;
                            this.settledData = data.data.list;
                            Object.assign(this.tempData, this.excelData);
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
            //获取商家关账日
            selectClosingDate(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectBusinessInfo,
                    {},
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            !isEmpty(data.data) && (this.closingDate = data.data.closingDateNum || 100);
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
            //查看结算工单详情
            handleCheck(id){
                turnToNextPage('/web/merchant/home/workorder/detail_balance.html', { id }, true);
            },
            //反结算
            handleAntiSettled(id, index){
                if (this.settledData[index].payType === 2) {
                    this.$message({
                        type: 'error',
                        message: '微信支付不能反结算!',
                        duration: 1200
                    });
                    return;
                } else if (!isEmpty(this.settledData[index].couponUuid)) {
                    this.$message({
                        type: 'error',
                        message: '该单已使用卡券不能反结算!',
                        duration: 1200
                    });
                    return;
                }
                this.$confirm({
                    title: '反结算',
                    type: 'warning',
                    message: '确认对该工单进行反结算?'
                }).then(() => {
                    this.$ajax(
                        this.$joggle.merchant.workorder.antiSettle,
                        { orderId: id },
                        true,
                        (data, loading) => {
                            if (data.code === 'ZS011000') {

                                if (this.settledData.length === 1 && this.settledPage.startPage > 1) {
                                    this.startPage -= 1;
                                }
                                this.selectWorkOrder(() => {
                                    this.$message({
                                        type: 'success',
                                        message: data.msg,
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
            },
            //导出工单列表
            handleDownLoad(){
                if (this.settledData.length === 0) {
                    this.$message({
                        type: 'error',
                        message: '暂无数据，无需导出表格！',
                        duration: 1200
                    });
                } else {
                    const tempData = {
                        state: 4,
                        startTime: isEmpty(this.excelData.startTime) ? '' : `${this.excelData.startTime} 00:00:00`,
                        endTime: isEmpty(this.excelData.endTime) ? '' : `${this.excelData.endTime} 23:59:59`,
                        searchKey: this.excelData.searchKey,
                        payType: this.excelData.payType == -1 ? '' : this.excelData.payType
                    };

                    turnToNextPage(this.$joggle.merchant.workorder.downLoadWorkOrder, tempData);
                }
            }
        },
        created(){
            const loading = this.$loading();

            this.selectClosingDate(() => {
                this.selectWorkOrder(() => {
                    this.$emit('input', false);
                    loading.close();
                }, loading);
            }, loading);

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
