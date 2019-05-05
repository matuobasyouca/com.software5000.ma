<template>
    <div class="page-404" v-show="!isLoading">
        <ma-head></ma-head>
        <div class="page-404-body">
            <p class="p1">这个页面找不到了</p>
            <p class="p2">您可以继续等待或者可以进行以下操作</p>
            <zs-button class="p3" type="primary" @click="handleToHome">返回首页</zs-button>
        </div>
    </div>
</template>

<script>
    import maHead from '../../../../components_proj/ma-head/app.vue';
    import {turnToNextPage} from '../../../../assets/js/utils';
    import {filterAgent} from '../../../../assets/js/turnToHostPage';
    export default{
        components: {
            maHead
        },
        data(){
            return {
                isLoading: true
            }
        },
        methods: {
            handleToHome(){
                turnToNextPage('/web/merchant/open/login.html');
            }
        },
        created(){
            filterAgent(() => {
                this.isLoading = false
            });
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>