<template>
    <div class="workorder-page" v-show="!isLoading">
        <ma-head curr-id="02"></ma-head>
        <zs-breadcrumb separator="arrow-right">
            <zs-breadcrumb-item>工单结算</zs-breadcrumb-item>
        </zs-breadcrumb>
        <tab :tabs="tabs" :current="currTab" @change="handleTabChange"></tab>
        <component :is="comps" v-model="isLoading"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import unbalance from './src/unbalance/app.vue';
    import settled from './src/settled/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    import {getDataFromParam, Hash} from '../../../../../assets/js/utils';
    export default {
        components: {
            maHead,
            tab,
            unbalance,
            settled
        },
        data(){
            return {
                tabs: ['未结算', '已结算'],
                currTab: 1,
                isLoading: true
            }
        },
        computed: {
            comps(){
                return [unbalance, settled][this.currTab - 1];
            }
        },
        methods: {
            handleTabChange(i){
                Hash.add({tab: i})
                this.currTab = i;
            }
        },
        created(){
            this.currTab = Hash.parse() ? Hash.parse().tab || 1 : 1;
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
