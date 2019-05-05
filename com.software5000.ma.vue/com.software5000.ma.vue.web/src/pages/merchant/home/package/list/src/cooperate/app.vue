<template>
    <div class="cooperate page-base">
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
        components: {
        },
        data(){
            return {
                datas: [],
                columns: [
                    {
                        title: '套餐名称',
                        key: 'sex',
                        width: 178
                    },
                    {
                        title: '套餐内容',
                        key: 'age',
                        width: 162
                    },
                    {
                        title: '销售价格',
                        key: 'age',
                        width: 130
                    },
                    {
                        title: '佣金',
                        key: 'age',
                        width: 98
                    },
                    {
                        title: '已售',
                        key: 'age',
                        width: 94
                    },
                    {
                        title: '状态',
                        key: 'state',
                        width: 90,
                        render(row, col, index, _this){
                            return row.state==1 ? '上架' : '下架';
                        }
                    },
                    {
                        title: '使用说明',
                        key: 'like',
                        width: 500,
                        className: 'control',
                        render(row, col, index, _this){
                            return "1/5";
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
            //获取套餐数据
            getDatas(fn){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectCooperComboPage,
                    {
                        "startPage":this.current,
                        "pageSize":this.pageSize
                    },
                    true,
                    (data,loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.total = data.data.total;
                            this.datas = data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
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
        },
        mounted () {
            this.getDatas();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
