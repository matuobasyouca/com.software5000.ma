<template>
    <div class="workorder-page" v-show="!isLoading">
        <ma-head curr-id="02"></ma-head>
        <zs-breadcrumb separator="arrow-right">
            <zs-breadcrumb-item :to="`${isEdit ? '/web/merchant/home/workorder/list_unfinish.html' : ''}`">{{ isEdit ? '未完成工单' : '快捷开单' }}</zs-breadcrumb-item>
            <zs-breadcrumb-item v-if="isEdit">修改工单</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component is="maining" v-model="isLoading"></component>
    </div>
</template>

<script>
    import maining from './src/mainning/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    import {getDataFromParam, isEmpty} from '../../../../../assets/js/utils';
    export default {
        components: {
            maining,
            maHead
        },
        data(){
            return {
                isLoading: true,
                isEdit: false
            }
        },
        created(){
            this.isEdit = !isEmpty(getDataFromParam('id'));
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
