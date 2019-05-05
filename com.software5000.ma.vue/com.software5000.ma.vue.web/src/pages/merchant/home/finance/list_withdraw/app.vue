<template>
    <div class="workorder-page">
        <ma-head :currId="05"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item>微信提现</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="list"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import list from './src/list/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    export default {
        components: {
            maHead,
            list,
            tab
        },
        data(){
            return {

            }
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
