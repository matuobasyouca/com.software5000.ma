<template>
    <div class="pt manage">
        <ma-head :currId="03"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="/web/merchant/home/member/list_level.html">会员等级</zs-breadcrumb-item>
            <zs-breadcrumb-item>会员折扣设置</zs-breadcrumb-item>
        </zs-breadcrumb>
        <component :is="componentCurr"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import maining from './src/maining/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';

    export default {
        components: {
            maHead,
            maining,
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
                return [maining][this.tabCurrent - 1];
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
