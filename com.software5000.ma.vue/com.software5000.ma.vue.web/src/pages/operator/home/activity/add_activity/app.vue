<template>
    <div class="workorder-page">
        <ma-head :currId="11" :operator="true"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="/web/operator/home/activity/manage.html">拼团列表</zs-breadcrumb-item>
            <zs-breadcrumb-item>{{ id ? '修改拼团' : '新建拼团'}}</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="list"></component>
    </div>
</template>

<script>
    import list from './src/list/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    import { getDataFromParam } from '../../../../../assets/js/utils';
    export default {
        components: {
            maHead,
            list
        },
        data(){
            return {
                id: ''
            };
        },
        computed: {},
        methods: {},
        mounted(){
            this.id = getDataFromParam('id');
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
