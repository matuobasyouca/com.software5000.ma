<template>
    <div class="package-page">
        <ma-head @home-click="handleGoHome">套餐详情</ma-head>
        <div class="package-detail">
            <ul>
                <li class="package-name">
                    <span class="package-name-left">套餐名称</span>
                    <span class="package-name-right">{{packageName}}</span>
                </li>
                <li class="package-validTime">
                    <span class="package-validTime-left">套餐有效期</span>
                    <span class="package-validTime-right">{{validTerm}}</span>
                </li>
            </ul>
        </div>
        <div class="package-item">套餐项目</div>
        <div class="package-items">
            <ul>
                <li v-for="packageAndItem in packageAndItems" :key="packageAndItem.id" class="package-item-lists">
                    <p class="package-item-next">
                        <span class="package-items-itemName">{{packageAndItem.serviceItem.itemName}}</span>
                        <span class="package-items-useTimes">{{`×${packageAndItem.useTimes}`}}</span>
                    </p>
                    <p class="package-items-salePrice">{{`￥${packageAndItem.serviceItem.salePrice}`}}</p>
                </li>
            </ul>
            <div class="package-salePrice">
                <div class="package-salePrice-left">
                    项目总金额<span>{{`￥${originalPrice}`}}</span>
                </div>
            </div>
        </div>
        <div class="package-detail-second">
            <p class="package-price">
                <span class="package-price-left">套餐售价</span>
                <span class="package-price-right">{{salePrice}}</span>
            </p>
            <div class="package-useInfo">使用说明</div>
            <div class="package-inputUseInfo-box">
                <div class="package-inputUseInfo">{{instructions}}</div>
            </div>
        </div>
        <div class="package-detail-third">
            <p class="package-createtime">
                <span class="package-createtime-left">新建时间</span>
                <span class="package-createtime-right">{{createTime}}</span>
            </p>
        </div>
    </div>
</template>

<script>
    import { turnToNextPage, getDataFromParam, isEmpty } from '../../../../../../assets/js/utils';
    import maHead from '../../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead
        },
        props: {},
        data(){
            return {
                packageId: '',
                memberPackage: '',
                packageName: '',
                validTerm: '',
                packageAndItems: [],
                originalPrice: '',
                salePrice: '',
                instructions: '',
                createTime: '',
                businessId: ''
            };
        },
        computed: {},
        watch: {},
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 查询卡包详情
             */
            selectData(){
                this.$ajax(
                    '/home/businessPackage/selectBusinessPackageById',
                    { businessPackageId: this.packageId },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.memberPackage = data.data;
                            this.packageName = this.memberPackage.packageName;
                            this.validTerm = this.memberPackage.validTerm == 0 ? '永久' : this.memberPackage.validTerm + '年';
                            this.packageAndItems = this.memberPackage.packageAndItems;
                            this.originalPrice = this.memberPackage.originalPrice;
                            this.salePrice = this.memberPackage.salePrice;
                            this.instructions = this.memberPackage.instructions;
                            this.createTime = this.memberPackage.createTime;
                            this.businessId = this.memberPackage.id;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 跳转主主页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            //获取套餐id
            this.packageId = getDataFromParam('id') ? getDataFromParam('id') : '';
            //获取套餐信息
            if (!isEmpty(this.packageId)) {
                this.selectData();
            }
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
