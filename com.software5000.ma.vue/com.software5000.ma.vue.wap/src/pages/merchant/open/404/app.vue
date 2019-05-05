<template>
    <div class="page-404" v-show="!isLoading">
        <div class="image"></div>
        <p class="notice">这个页面找不到了</p>
        <zs-button type="primary" @click="handleGoHome">返回首页</zs-button>
    </div>
</template>
<script>
    import {turnToNextPage} from '../../../../assets/js/utils';
    import {filterAgent} from '../../../../assets/js/turnToHostPage';
    export default {
        data(){
            return {
                isLoading: true
            }
        },
        methods: {
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
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