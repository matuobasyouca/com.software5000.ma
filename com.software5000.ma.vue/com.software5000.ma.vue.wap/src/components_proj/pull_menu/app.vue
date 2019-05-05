<template>
    <div class="pull-menu-wrap">
        <div class="pull-menu-marsk" v-show="currTab != ''" @click="currTab = ''"></div>
        <ul class="pull-menu-tab" :class="[{'only2':p2 && !p3}]">
            <li :class="[{'curr':currTab == 1}]" @click="handleTab(1)"><span>{{ p1 }}</span></li>
            <li :class="[{'curr':currTab == 2}]" @click="handleTab(2)" v-if="p2"><span>{{ p2 }}</span></li>
            <li :class="[{'curr':currTab == 3}]" @click="handleTab(3)" v-if="p3"><span>{{ p3 }}</span></li>
        </ul>
        <div class="pull-menu-main" v-show="currTab == 1">
            <slot name="p1"></slot>
        </div>
        <div class="pull-menu-main" v-show="currTab == 2" v-if="p2">
            <slot name="p2"></slot>
        </div>
        <div class="pull-menu-main" v-show="currTab == 3" v-if="p3">
            <slot name="p3"></slot>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'pullMenu',
        props: {
            p1: String,
            p2: String,
            p3: String,
            value: [Boolean, String, Number]
        },
        data(){
            return {
                currTab: ''
            }
        },
        watch: {
            value(val){
                this.currTab = !val ? '' : val;
            }
        },
        methods: {
            handleTab(i){
                if(this.currTab == i){
                    this.currTab = '';
                }else{
                    this.currTab = i;
                    this.$emit('input', i);
                }
            }
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>