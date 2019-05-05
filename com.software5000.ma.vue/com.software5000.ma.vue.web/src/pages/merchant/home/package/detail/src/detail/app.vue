<template>
    <div class="page-main">
        <div class="page-main-title">
            <div class="page-main-title-left">
                <zs-icon icon="card" :text="datas.packageName" :size="32"></zs-icon>
                <span class="price">￥{{datas.salePrice}}</span>
            </div>
            <div class="page-main-title-right">有效期：{{datas.validTerm!=0 ? datas.validTerm+'年' : '永久'}}</div>
        </div>
        <div class="page-main-body">
            <p class="package-price">套餐项目总价：<span class="black">￥{{datas.originalPrice}}</span></p>
            <zs-table
                    class="package-table"
                    :data="datas.packageAndItems"
                    :columns="packageColumns"
                    :context="context"
                    :no-data-colspan="10"
                    noDataText="暂无数据"
                    border>
            </zs-table>
            <p class="package-explain">使用说明：</p>
            <p class="package-text">{{datas.instructions || '&nbsp;'}}</p>
        </div>
    </div>
</template>

<script>
    import { getDataFromParam } from '../../../../../../../assets/js/utils';

    export default {
        name: 'detail',
        data() {

            return {
                id: '',
                context: this,
                datas: {},
                packageColumns: [
                    {
                        title: '套餐项目',
                        key: 'aaa',
                        width: 336,
                        render(row) {
                            return row.serviceItem.itemName;
                        }
                    },
                    {
                        title: '数量',
                        key: 'useTimes',
                        width: 190
                    },
                    {
                        title: '单价',
                        key: 'ccc',
                        width: 130,
                        render(row) {
                            return row.serviceItem.salePrice;
                        }
                    }
                ]
            };
        },
        computed: {},
        methods: {
            //获取套餐详情数据
            getDatas() {
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageById,
                    {
                        businessPackageId: this.id
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.datas = data.data;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            }
        },
        created() {
            this.id = getDataFromParam('id');
            this.getDatas();
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
