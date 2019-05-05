<template>
    <div class="pt manage">
        <ma-head :currId="03"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="/web/merchant/home/member/list.html">会员资料</zs-breadcrumb-item>
            <zs-breadcrumb-item>会员详情</zs-breadcrumb-item>
        </zs-breadcrumb>
        <tab :tabs="tabs" :current="tabCurrent" @change="handleTab"></tab>
        <component :is="componentCurr"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import detail from './src/detail/app.vue';
    import payList from './src/pay_list/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    export default {
        components: {
            maHead,
            detail,
            payList,
            tab,
        },
        data(){
            return {
                tabCurrent: 1,
                tabs: ["会员详情", "消费记录"]
            }
        },
        computed : {
            componentCurr(){
                return [detail,payList][this.tabCurrent-1];
            }
        },
        methods: {
            handleTab(val){
                this.tabCurrent = val
            },
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
