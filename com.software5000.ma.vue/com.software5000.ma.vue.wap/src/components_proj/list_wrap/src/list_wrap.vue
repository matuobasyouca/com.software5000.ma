<template>
    <div class="ma-list-wrap" :style="style">
        <slot></slot>
    </div>
</template>

<script>
    export default {
        name: 'listWrap',
        props: {
            paddingLeft: {
                type: [Number, String],
                default : 15
            }
        },
        computed:{
            style(){
                let style = {};
                style.paddingLeft = `${parseInt(this.paddingLeft)}px`
                return style;
            }
        },
        methods: {}
    }
</script>

<style scoped>
    @import '../list_wrap.css';
</style>