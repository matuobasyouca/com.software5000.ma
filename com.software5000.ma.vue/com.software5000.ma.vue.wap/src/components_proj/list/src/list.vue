<template>
    <ul class="ma-list" :style="style" @click="handleClick">
        <li v-if="$slots.left" class="ma-list-left">
            <slot name="left"></slot>
        </li>
        <li class="ma-list-mid" v-if="$slots.default">
            <slot></slot>
        </li>
        <li class="ma-list-right" v-if="$slots.right">
            <slot name="right"></slot>
        </li>
    </ul>
</template>

<script>
    export default {
        name: 'list',
        props: {
            paddingLeft: {
                type: [Number, String],
                default: 140
            },
            height: {
                type: [Number, String],
                default : 44
            },
            paddingRight: {
                type: [Number, String],
                default: 80
            },
        },
        computed: {
            style(){
                let style = {};
                let mid = this.$slots.default;
                style.paddingLeft = mid ? `${parseInt(this.paddingLeft)}px` : '';
                style.paddingRight = mid ? `${parseInt(this.paddingRight)}px` : '';
                style.height = `${parseInt(this.height)}px`;
                style.lineHeight = `${parseInt(this.height)}px`;

                return style;
            }
        },
        methods: {
            handleClick(){
                this.$emit('click')
            }
        }
    }
</script>

<style scoped lang="less">
    @import '../list.less';
</style>