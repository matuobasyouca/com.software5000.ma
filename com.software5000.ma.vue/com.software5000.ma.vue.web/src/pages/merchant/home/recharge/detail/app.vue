<template>
    <div class="workorder-page">
        <ma-head></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="/web/merchant/home/recharge/update.html?curr=2">充值记录</zs-breadcrumb-item>
            <zs-breadcrumb-item>充值详情</zs-breadcrumb-item>
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
        data() {
            return {};
        },
        computed: {},
        methods: {}
    };
</script>

<style lang="less">
    @import './style.less';
</style>
