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
            tab
        },
        data() {
            return {
                tabCurrent: 1,
                tabs: ['商家套餐', '诚品合作套餐']
            };
        },
        computed: {
            /**
             *  当前选择的标签值
             */
            componentCurr() {
                return [merchant][this.tabCurrent - 1];
            }
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/

            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/

            /* ------------------- update (改) end -------------------*/

            /* ------------------- insert (查) start -------------------*/

            /* ------------------- insert (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             * 切换标签
             * @param val 标签的位置
             */
            handleTab(val) {
                this.tabCurrent = val;
            }
            /* ------------------- handle (操) end -------------------*/

        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
