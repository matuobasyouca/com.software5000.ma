<template>
    <div class="serve-item-wrap" :style="wrapStyle">
        <i class="item-name-label" :class="labelClass" :style="labelStyle">{{ firstLetter }}</i>
        <div class="item-text">
            <p class="item-name">{{ name }}</p>
            <p class="item-price"><em>￥</em>{{ price }}</p>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'item',
        props: {
            size: [String, Number],
            paddingLeft: [String, Number],
            labelClass: [String],
            name:[String],
            price:[String,Number]
        },
        computed: {
            wrapStyle(){
                let style = {};
                style.paddingLeft = `${parseInt(this.size) + parseInt(this.paddingLeft)}px`
                return style;
            },
            labelStyle(){
                let style = {};
                style.width = style.height = style.lineHeight = `${parseInt(this.size)}px`;

                return style;
            },
            firstLetter(){
                return this.name ? this.name[0] : '';
            }
        },
        methods: {}
    }
</script>

<style lang="less">
    @import '../item.less';
</style>