<template>
    <div class="workorder-page" v-show="!isLoading">
        <ma-head curr-id="02"></ma-head>
        <zs-breadcrumb separator="arrow-right">
            <zs-breadcrumb-item to="/web/merchant/home/workorder/list_balance.html?tab=2#eyJ0YWIiOjJ9">工单结算</zs-breadcrumb-item>
            <zs-breadcrumb-item>已结算详情</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="maining" v-model="isLoading"></component>
    </div>
</template>

<script>
    import maining from './src/mainning/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    export default {
        components: {
            maining,
            maHead
        },
        data(){
            return {
                isLoading: true
            }
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
