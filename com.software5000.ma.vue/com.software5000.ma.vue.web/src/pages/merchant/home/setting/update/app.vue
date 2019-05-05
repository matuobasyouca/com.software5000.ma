<template>
    <div class="workorder-page">
        <ma-head :currId="06"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item>数据导入</zs-breadcrumb-item>
        </zs-breadcrumb>
        <tab :tabs="tabs" :current="currTab" @change="handleTabChange"></tab>
        <component :is="comps"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import serve from './src/serve/app.vue';
    import member from './src/member/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';
    import { getDataFromParam } from '../../../../../assets/js/utils';

    export default {
        components: {
            maHead,
            serve,
            member,
            tab
        },
        data() {
            return {
                tabs: ['服务项目导入', '会员信息导入'],
                currTab: 1
            };
        },
        computed: {
            /**
             *    返回当前标签选择的页面
             */
            comps() {
                return [serve, member][this.currTab - 1];
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
             *  改变标签
             * @param i 对应标签位置
             */
            handleTabChange(i) {
                this.currTab = i;
            }
            /* ------------------- handle (操) end -------------------*/

        },
        created() {
            this.currTab = getDataFromParam('tab') || 1;
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
