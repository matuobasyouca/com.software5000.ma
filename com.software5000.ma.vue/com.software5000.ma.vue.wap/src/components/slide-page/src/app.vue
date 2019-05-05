<template>
    <transition name="slide">
        <div class="slide-page" v-show="value">
            <h4 class="slide-page-title">
                <i class="go-back" @click="handleArrowClick"></i>
                <i class="go-home" v-if="homeIcon" @click="handleGoHome"></i>
                {{ title }}
                <div class="slide-page-title-main">
                    <slot name="slide-page-title"></slot>
                </div>
            </h4>
            <div class="slide-page-main">
                <slot></slot>
            </div>
        </div>
    </transition>
</template>
<script>
    export default {
        name: 'zsSlidePage',
        props: {
            title: [String, Number],
            value: Boolean,
            homeIcon: {
                type: [Boolean],
                default: true
            }
        },
        data () {
            return {}
        },
        watch: {
            value(val){
                document.body.style.overflow = val ? 'hidden' : '';
            }
        },
        methods: {
            handleArrowClick(){
                this.$emit('input', false);
                this.$emit('go-back')
            },
            handleGoHome(){
                this.$emit('go-home');
            }
        }
    }
</script>
<style lang="less">
    @import '../style.less';
</style>