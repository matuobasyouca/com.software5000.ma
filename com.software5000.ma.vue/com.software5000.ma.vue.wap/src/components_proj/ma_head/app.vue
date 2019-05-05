<template>
    <div class="ma-head">
        <div class="ma-head__wrap">
            <div
                    class="icon-left"
                    v-if="leftIcon"
                    @click="handleLeftIconClick">
                {{ leftIconText }}
            </div>
            <div
                    class="icon-home"
                    v-if="home"
                    @click="handleHomeClick"></div>
            <!--<div class="icon-left"-->
                 <!--v-if="leftIcon"-->
                 <!--@click="handleLeftIconClick">-->
                <!--<zs-icon-->
                        <!--class="ma-head-icon"-->
                        <!--:icon="leftIcon"-->
                        <!--:text="leftIconText"-->
                        <!--:size="leftIconSize"></zs-icon>-->
            <!--</div>-->
            <!--<div class="icon-home" v-if="home" @click="handleHomeClick">-->
                <!--<zs-icon-->
                        <!--class="ma-head-icon-home"-->
                        <!--icon="home"-->
                        <!--size="20"></zs-icon>-->
            <!--</div>-->
            <p v-if="$slots.default">
                <slot></slot>
            </p>
            <div
                    class="icon-right"
                    v-if="rightIcon || rightIconText"
                    @click="handleRightIconClick">
                <zs-icon
                        class="ma-head-icon"
                        :icon="rightIcon"
                        :size="rightIconSize"
                        :text="rightIconText"></zs-icon>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'maHead',
        props: {
            leftIcon: {
                type: String,
                default: 'arrow-left'
            },
            leftIconSize: {
                type: [String, Number],
                default: 12
            },
            leftIconText: {
                type: [String, Number],
                default: ''
            },
            rightIcon: String,
            rightIconSize: {
                type: [String, Number],
                default: 12
            },
            rightIconText: {
                type: [String, Number],
                default: ''
            },
            goBack: {
                type: Boolean,
                default: true
            },
            home: {
                type: Boolean,
                default: true
            }
        },
        methods: {
            handleLeftIconClick(){
                this.goBack && window.history.go(-1);
                this.$emit('left-icon-click')
            },
            handleRightIconClick(){
                this.$emit('right-icon-click')
            },
            handleHomeClick(){
                this.$emit('home-click')
            }
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>