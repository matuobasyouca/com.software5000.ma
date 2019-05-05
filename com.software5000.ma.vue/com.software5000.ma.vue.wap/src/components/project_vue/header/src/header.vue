<template>
    <h1 class="pageTitle">
        <go-back :targetUrl="targetUrl" :routerUrl="routerUrl" v-if="isGoBack"></go-back>
        {{title}}
        <p class="ma-header--operation" v-if="$slots.default" @click="operation"><slot></slot></p>
    </h1>
</template>
<script>
    import goBack from '../../go_back'
    export default {
        name : 'maHeader',
        components : {
            goBack
        },
        props : {
            title : {
                type : String
            },
            isGoBack : {
                type : Boolean,
                default : false
            },
            targetUrl : {
                type : String
            },
            routerUrl:{
                type : String
            }
        },
        methods:{
            operation(){
                this.$emit('operation','add')
            }
        }
    };
</script>
<style scoped>
    .pageTitle {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 21;
        height: 44px;
        background-color: #1daae5;
        color: #fff;
        text-align: center;
        line-height: 44px;
    }
    .ma-header--operation{
        position: absolute;
        right:10px;
        top:0;
        font-size: 16px;
    }
</style>