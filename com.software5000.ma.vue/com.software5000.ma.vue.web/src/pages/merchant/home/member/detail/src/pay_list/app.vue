<template>
    <div class="pay_list">
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
    import {turnToNextPage,getDataFromParam} from '../../../../../../../assets/js/utils.js';

    export default {
        components: {
        },
        data(){
            return {
                id : '',
                userId : '',
                datas: [],
                columns: [
                    {
                        title: '工单编号',
                        key: 'orderNo',
                        align: 'center',
                        width: 166
                    },
                    {
                        title: '消费车牌',
                        key: 'carNumber',
                        width: 130
                    },
                    {
                        title: '服务项目',
                        key: 'itemNames',
                        width: 360
                    },
                    {
                        title: '实收金额',
                        key: 'totalPrice',
                        width: 168
                    },
                    {
                        title: '收款方式',
                        key: 'payType',
                        width: 118,
                        render(row, col, index, _this){
                            return row.payType==1? '现金收款' : '微信收款';
                        }
                    },
                    {
                        title: '消费时间',
                        key: 'payTime',
                        width: 158
                    },
                    {
                        title: '操作',
                        key: 'id',
                        width: 152,
                        className: 'control',
                        render(row){
//                            /merchant/home/workorder/balance_detail.html?id=933
                            let detail=`<span class="blue" @click="turnPage('/web/merchant/home/workorder/detail_balance.html',${row.id},true)">查看</span>`;

                            return detail;
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
            getMemberData(){
                this.$ajax(
                    this.$joggle.merchant.member.selectMemberDetailInfo,
                    {
                        'memberLvlRecordId': this.id,
                    },
                    true,
                    (data,model) => {
                        if (data.code === 'ZS011000') {
                            this.userId=data.data.userId;
                            this.payRecords();
                        } else {
                            model.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                            setTimeout(()=>{
                                turnToNextPage('/web/merchant/home/member/list.html');
                            },1200);
                        }
                    }
                )
            },
            //查询该会员消费记录
            payRecords(){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderByPage,
                    {
                        'userId': this.userId,
                        'state' : 4,
                        "startPage": this.current,
                        "pageSize":this.pageSize
                    },
                    true,
                    (data,model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            this.datas=data.data.list;
                            this.total=data.data.total;
//                            this.getMemberPackageData();
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
                    }
                )
            },
            //跳转至会员详情
            turnPage(url,id,newPage){
                let para={'id' : id};
                turnToNextPage(url,para,newPage);
            },
            /**
             * 页码切换
             */
            handlePageChange(currPage){
                this.current = currPage;
                this.payRecords();
            },
            /**
             * 每页数据条数切换
             */
            handlePageSizeChange(pageSize){
                this.current = 1;
                this.pageSize = pageSize;
                this.payRecords();
            },
        },
        mounted(){
            this.id=getDataFromParam('id');
            this.getMemberData();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
