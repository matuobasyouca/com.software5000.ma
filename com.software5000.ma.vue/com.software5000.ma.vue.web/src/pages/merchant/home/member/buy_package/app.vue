<template>
    <div class="workorder-page">
        <ma-head :currId="03"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item>{{ pageTitle }}</zs-breadcrumb-item>
        </zs-breadcrumb>
        <tab :tabs="tabs" :current="tabCurrent" @change="handleTab"></tab>
        <component :is="componentCurr"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import buy from './src/buy/app.vue';
    import unpaid from './src/unpaid/app.vue';
    import buyHistory from './src/buy_history/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    import {getDataFromParam, isEmpty} from '../../../../../assets/js/utils';
    export default {
        components: {
            tab,
            buy,
            unpaid,
            maHead,
            buyHistory
        },
        data(){
            return {
                pageTitle: '套餐购买',
                tabCurrent: 1,
                tabs: ["套餐购买", "待支付套餐","套餐购买记录"]
            }
        },
        computed : {
            componentCurr(){
                return [buy,unpaid,buyHistory][this.tabCurrent - 1];
            }
        },
        methods: {
            handleTab(val){
                this.tabCurrent = val
            },
        },
        created(){
            let curr=getDataFromParam('curr');
            this.tabCurrent= curr ? curr : this.tabCurrent;
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
