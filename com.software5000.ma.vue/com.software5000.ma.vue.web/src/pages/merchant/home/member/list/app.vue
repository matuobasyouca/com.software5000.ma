<template>
    <div class="pt manage">
        <ma-head :currId="03"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item>会员资料</zs-breadcrumb-item>
        </zs-breadcrumb>
        <tab :tabs="['开单']" :current="1" v-if="tabCurrent"></tab>
        <component is="list"></component>
    </div>
</template>

<script>
    import tab from '../../../../../components_proj/tab/app.vue';
    import list from './src/list/app.vue';
    import maHead from '../../../../../components_proj/ma-head/app.vue';

    export default {
        components: {
            maHead,
            list,
            tab
        },
        data() {
            return {
                tabCurrent: 0
            };
        },
        methods: {}
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
