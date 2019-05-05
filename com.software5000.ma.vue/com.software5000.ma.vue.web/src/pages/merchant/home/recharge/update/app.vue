<template>
    <div class="workorder-page">
        <ma-head :currId="03"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item>{{ tabs[tabCurrent-1]}}</zs-breadcrumb-item>
        </zs-breadcrumb>
        <tab :tabs="tabs" :current="tabCurrent" @change="handleTab"></tab>
        <component :is="componentCurr"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import recharge from './src/recharge/app.vue';
    import rechargeHistory from './src/recharge_history/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    import { getDataFromParam } from '../../../../../assets/js/utils';

    export default {
        components: {
            tab,
            recharge,
            maHead,
            rechargeHistory
        },
        data() {
            return {
                tabCurrent: 1,
                tabs: ['会员充值', '充值记录']
            };
        },
        computed: {
            /**
             *  当前选择的标签值
             */
            componentCurr() {
                return [recharge, rechargeHistory][this.tabCurrent - 1];
            }
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/

            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/

            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/

            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             * 切换标签
             * @param val 标签的位置
             */
            handleTab(val) {
                this.tabCurrent = val;
            }
            /* ------------------- handle (操) end -------------------*/

        },
        created() {
            let curr = getDataFromParam('curr');

            this.tabCurrent = curr ? curr : this.tabCurrent;
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
