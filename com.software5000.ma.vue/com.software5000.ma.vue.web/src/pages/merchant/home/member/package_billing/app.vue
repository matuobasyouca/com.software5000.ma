<template>
    <div class="workorder-page">
        <ma-head></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="/web/merchant/home/member/buy_package.html">套餐购买</zs-breadcrumb-item>
            <zs-breadcrumb-item>套餐结算</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="billing"></component>
    </div>
</template>

<script>
    import billing from './src/billing/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    export default {
        components: {
            billing,
            maHead
        },
        data(){
            return {}
        },
        computed: {},
        methods: {}
    };
</script>

<style lang="less">
    @import './style.less';
</style>
