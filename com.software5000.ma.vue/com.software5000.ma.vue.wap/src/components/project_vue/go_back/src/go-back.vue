<template>
    <router-link v-if="routerUrl" :to="routerUrl" class="goBackWrap">
        <i class="goBack"></i>
    </router-link>
    <a v-else class="goBackWrap" :href="targetUrl" @click="go">
        <i class="goBack"></i>
    </a>
</template>
<script>
    export default {
        name : 'goBack',
        props : {
            targetUrl:{
                type:String
            },
            routerUrl:{
                type:String
            }
        },
        methods : {
            go(){
                if(this.targetUrl || this.routerUrl)return;
                window.history.go(-1);
            }
        }
    };
</script>
<style scoped>
    .goBackWrap {
        position: absolute;
        display: block;
        line-height: 44px;
        width: 44px;
        height: 44px;
    }
    .goBack{
        position: absolute;
        left: 13px;
        top: 50%;
        width: 12px;
        height: 12px;
        border: solid #fff;
        border-width: 2px 0 0 2px;
        transform:translateY(-50%) rotate(-45deg);
    }
</style>