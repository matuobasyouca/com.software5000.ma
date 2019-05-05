<template>
    <li class="list-base">
        <div class="list-base__top clr">
            <slot name="list-base-top"></slot>
        </div>
        <slot name="list-base-middle"></slot>
        <div class="list-base__bottom clr">
            <slot name="list-base-bottom"></slot>
        </div>
    </li>
</template>
<script>
    export default {
        name : 'listBase',
    };
</script>
<style scoped>
    .list-base{
        position: relative;
        padding:0 10px;
        margin-bottom:10px;
        background-color:#fff;
    }
    .list-base__top{
        height:44px;
        line-height:44px;
        border-bottom:1px solid #ebebeb;
        overflow: hidden;
    }
    .list-base__bottom{
        height:36px;
        line-height:36px;
        overflow: hidden;
    }
</style>