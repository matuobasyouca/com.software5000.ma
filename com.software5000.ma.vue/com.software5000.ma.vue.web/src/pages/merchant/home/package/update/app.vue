<template>
    <div class="workorder-page">
        <ma-head></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="/web/merchant/home/member/package.html">会员套餐</zs-breadcrumb-item>
            <zs-breadcrumb-item>{{ id ? '修改套餐' : '新建套餐'}}</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="maining"></component>
    </div>
</template>

<script>
    import { getDataFromParam } from '../../../../../assets/js/utils';
    import maining from './src/mainning/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';

    export default {
        components: {
            maining,
            maHead
        },
        data() {
            return {
                id: ''
            };
        },
        computed: {},
        methods: {},
        mounted() {
            this.id = getDataFromParam('id');
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
