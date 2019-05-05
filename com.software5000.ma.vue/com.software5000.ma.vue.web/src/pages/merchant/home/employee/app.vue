<template>
    <div class="workorder-page">
        <ma-head :currId="04"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item>员工</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="list"></component>
    </div>
</template>

<script>
    import list from './src/list/app.vue';
    import maHead from '../../../../components_proj/ma-head/app.vue';
    export default {
        components: {
            maHead,
            list
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
