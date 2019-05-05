<template>
    <div class="page-base merchant">
        <div class="merchant-top">
            <zs-button type="primary" @click="turnPage('/web/merchant/home/package/update.html','')">新建套餐</zs-button>
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
        components: {},
        data() {
            return {
                datas: [],
                columns: [
                    {
                        title: '套餐名称',
                        key: 'packageName',
                        width: 168
                    },
                    {
                        title: '套餐价格/元',
                        key: 'salePrice',
                        width: 136
                    },
                    {
                        title: '有效期',
                        key: 'validTerm',
                        width: 170,
                        render(row) {
                            let validTerm = row.validTerm != 0 ? row.validTerm + '年' : '永久';

                            validTerm = validTerm != 'null年' ? validTerm : '';
                            return validTerm;
                        }
                    },
                    {
                        title: '套餐项目',
                        key: 'packageAndItems',
                        width: 480,
                        render(row) {
                            let ret = `<p class='package-item-name'>`;

                            for (let x in row.packageAndItems) {
                                ret += `${row.packageAndItems[x].serviceItem.itemName} ${row.packageAndItems[x].useTimes}次、`;
                            }
                            ret = ret.slice(0, ret.length - 1);
                            ret += `</p>`;
                            return ret;
                        }
                    },
                    {
                        title: '状态',
                        key: 'packageState',
                        width: 102,
                        render(row) {
                            return row.packageState == 1 ? '启用' : `<span class="gray">停用</span>`;
                        }
                    },
                    {
                        title: '操作',
                        key: 'like',
                        width: 196,
                        className: 'control',
                        render(row) {
                            let state = `<span @click="changeState(${row.id},${row.packageState})">${row.packageState == 1 ? '停用' : '启用'}</span>`;
                            let detail = `<span @click="turnPage('/web/merchant/home/package/detail.html','${row.id}',true)">查看</span>`;
                            let modify = `<span @click="turnPage('/web/merchant/home/package/update.html','${row.id}',true)">修改</span>`;
                            let del = `<span @click="delPackage(${row.id})">删除</span>`;

                            return `${state}${detail}${modify}${del}`;
                        }
                    }
                ],
                context: this,
                currIndex: 1,

                current: 1,
                pageSize: 10,
                total: 0

            };
        },
        methods: {
            //获取套餐数据
            getDatas(fn) {
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageByPage,
                    {
                        starPage: this.current,
                        pageSize: this.pageSize
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.total = data.data.total;
                            this.datas = data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        fn && fn();
                    }
                );
            },
            //跳转页面
            turnPage(url, id, newPage) {
                let para = id ? { id } : {};

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
            },
            /*
             操作部分
             */
            //改变该套餐的状态
            changeState(id, state) {
                state = state == 1 ? 2 : 1;

                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackageState,
                    {
                        id,
                        state
                    },
                    true,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                            this.getDatas();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            //删除该套餐
            delPackage(id) {
                this.$confirm({
                    type: 'delete',
                    title: '删除',
                    showClose: true,
                    message: '确定删除该套餐？'
                }).then(() => {
                    this.$ajax(
                        this.$joggle.merchant.businessPackage.deleteBusinessPackage,
                        {
                            businessPackageId: id
                        },
                        true,
                        (data) => {
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: data.msg
                                });
                                this.current = 1;
                                this.getDatas();
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        }
                    );
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
