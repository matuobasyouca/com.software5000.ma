<template>
    <p class="icon_wrap" :style="wrapStyle" @click="handleClick">
        <i
                :class="'zs-icon-' + icon"
                :style="iconStyle"
                v-if="icon"
                @click="handleIconClick">{{ firstLetter }}</i>
        <i
                :class="'zs-icon-' + icon2"
                :style="[iconStyle,icon2Style]"
                v-if="icon2"
                @click="handleIconClick">{{ _icon2 }}</i>
        <span v-if="text" v-html="text" @click="handleTextClick"></span>
        <i
                class="zs-right-icon"
                :class="'zs-icon-' + rightIcon"
                :style="iconStyle" v-if="rightIcon"
                @click="handleRightIconClick"></i>
    </p>
</template>

<script>
    import {isChinese} from '../../../assets/js/utils';
    export default {
        name: 'ZsIcon',

        props: {
            icon: String,
            iconDis: {
                type: [String, Number],
                default: 6
            },
            icon2: String,
            iconBackGround: {
                type: String,
                default: '#f39114'
            },
            icon2BackGround: {
                type: String,
                default: '#1daae5'
            },
            rightIconDis: {
                type: [String, Number],
                default: 6
            },
            rightIcon: String,
            size: {
                type: [String, Number],
                default: 14
            },
            size2: [String, Number],
            text: {
                type: [String, Number],
                default: ''
            }
        },
        computed: {
            wrapStyle(){
                let style = {};
                style.paddingLeft = this.icon && this.text ? `${parseInt(this.size) + parseInt(this.iconDis)}px` : 0;
                if (this.icon2 && this.text) {
                    style.paddingLeft = `${parseInt(this.size) * 2 + parseInt(this.iconDis) * 2}px`
                }
                style.paddingRight = this.rightIcon && this.text ? `${parseInt(this.size) + parseInt(this.rightIconDis)}px` : 0;
                style.minHeight = this.icon ? `${parseInt(this.size)}px` : 0;
                style.minWidth = this.icon ? `${parseInt(this.size)}px` : 0;
                if(this.icon2 && !this.size2){
                    style.minWidth = `${parseInt(this.size)*2 + parseInt(this.iconDis)}px`;
                }else if(this.icon2 && this.size2){
                    style.minWidth = `${parseInt(this.size)+ parseInt(this.size2) + parseInt(this.iconDis)}px`;
                }
                style.lineHeight = `${parseInt(this.size)}px`;
                return style;
            },
            iconStyle(){
                let style = {};
                style.width = style.height = `${parseInt(this.size)}px`;
                if (this.icon === 'first-letter' || (this.icon && isChinese(this.icon))) {
                    style.backgroundColor = this.iconBackGround;
                    style.borderRadius = '2px';
                    style.lineHeight = `${parseInt(this.size)}px`;
                    style.fontSize = '12px';
                }
                return style;
            },
            icon2Style(){
                let style = {};
                style.width = style.height = `${parseInt(this.size2 || this.size)}px`;
                if (this.icon2) {
                    isChinese(this.icon2) && (style.backgroundColor = this.icon2BackGround);
                    style.left = `${parseInt(this.size) + parseInt(this.iconDis)}px`
                }
                return style;
            },
            firstLetter(){
                if (this.icon == 'first-letter') {
                    return this.text[0]
                } else if (isChinese(this.icon)) {
                    return this.icon[0]
                } else {
                    return ''
                }
            },
            _icon2(){
                if (isChinese(this.icon2)) {
                    return this.icon2[0]
                } else {
                    return ''
                }
            }
        },
        methods: {
            handleClick(event){
                this.$emit('click', event)
            },
            handleIconClick(){
                this.$emit('on-icon-click')
            },
            handleRightIconClick(){
                this.$emit('on-right-icon-click')
            },
            handleTextClick(){
                this.$emit('on-text-click')
            }
        }

    };
</script>
<style lang="less">
    @import 'style.less';
</style>