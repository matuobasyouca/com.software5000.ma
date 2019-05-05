<template>
    <div class="workorder-page">
        <ma-head></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="/web/merchant/home/package/list.html">会员套餐</zs-breadcrumb-item>
            <zs-breadcrumb-item>套餐详情</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="detail"></component>
    </div>
</template>

<script>
    import detail from './src/detail/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';

    export default {
        components: {
            detail,
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
