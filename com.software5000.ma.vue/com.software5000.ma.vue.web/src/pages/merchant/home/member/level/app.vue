<template>
    <div class="pt manage">
        <ma-head :currId="03"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item>会员等级</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component :is="componentCurr"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import merchant from './src/merchant/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    export default {
        components: {
            maHead,
            merchant,
            tab,
        },
        data(){
            return {
                tabCurrent: 1,
                tabs: ["商家套餐", "诚品合作套餐"]
            }
        },
        computed : {
            componentCurr(){
                return [merchant][this.tabCurrent-1];
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
