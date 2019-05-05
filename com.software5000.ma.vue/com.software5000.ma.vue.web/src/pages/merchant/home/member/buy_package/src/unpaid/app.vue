<template>
    <div class="unpaid">
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
    import {turnToNextPage} from '../../../../../../../assets/js/utils.js';
    export default {
        name: 'unpaid',
        components: {

        },
        data(){
            return {
                datas: [],
                columns: [
                    {
                        title: '订单编号',
                        key: 'orderNumber',
                        width: 164
                    },
                    {
                        title: '客户手机号',
                        key: 'mobile',
                        width: 150
                    },
                    {
                        title: '客户姓名',
                        key: 'realName',
                        width: 128
                    },
                    {
                        title: '购买套餐',
                        key: 'packageName',
                        width: 208
                    },
                    {
                        title: '应收金额',
                        key: 'salePrice',
                        width: 170
                    },
                    {
                        title: '提成人员',
                        key: 'saleName',
                        width: 118
                    },
                    {
                        title: '下单时间',
                        key: 'createTime',
                        width: 156,
                        render(row, col, index, _this){
                            return row.createTime.slice(0,19);
                        }

                    },
                    {
                        title: '操作',
                        key: 'like',
                        width: 154,
                        className: 'control',
                        render(row, col, index, _this){
                            let cancel=`<span @click="cancelOrder(${row.id})">取消订单</span>`;
                            let payee=`<span @click="payee(${row.id})">去收款</span>`;
                            return `${cancel}${payee}`;
                        }
                    }
                ],
                context: this,
                currIndex: 1,

                current : 1,
                pageSize : 10,
                total : 0,

                member : {
                    lv : "",
                },
                memberLvOpt : [{
                    name : 'abc',
                    id : 1,
                    value : 1
                },
                    {
                    name : 'bbb',
                    id : 2,
                    value : 2
                }],

            }
        },
        methods  : {
            //根据会员等级记录id查询会员信息
            getDatas(fn){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectNoPaidBusinessPackageOrder,
                    {
                        "starPage":this.current,
                        "pageSize":this.pageSize
                    },
                    true,
                    (data,model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            this.datas=data.data.list;
                            this.total=data.data.total;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                            setTimeout(()=>{
                                turnToNextPage('/web/merchant/home/member/list.html');
                            },1200);
                        }
                        fn && fn();
                    }
                )

            },
            /**
             * 页码切换
             */
            handlePageChange(currPage){
                this.current = currPage;
                let loading = this.$loading();
                this.getDatas(() => {
                    loading.close();
                });
            },
            /**
             * 每页数据条数切换
             */
            handlePageSizeChange(pageSize){
                this.current = 1;
                this.pageSize = pageSize;
                let loading = this.$loading();
                this.getDatas(() => {
                    loading.close();
                });
            },

            //取消订单
            cancelOrder(id){
                this.$confirm({
                    type:'draft3',
                    title: '取消订单',
                    showClose: true,
                    message:'确定取消该套餐订单？'
                }).then((action) => {
                    this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackageOrderCancle,
                    {
                        "businessPackageOrderId":id
                    },
                    true,
                    (data,loading) => {
                    if (data.code === 'ZS011000') {
                    this.$message({
                        type: 'success',
                        message: data.msg
                    });
                    this.current =1;
                    this.getDatas();
                } else {
                    this.$message({
                        type: 'error',
                        message: data.msg
                    })
                }
            }
            )
            })

            },
            //取消订单
            payee(id){
                turnToNextPage('/web/merchant/home/member/package_billing.html',{ id : id },true);
            },
        },
        mounted (){
            this.getDatas();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
