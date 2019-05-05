<template>
    <div class="ma-appraise">
        <h3 class="appraise-title">
            <slot></slot>
        </h3>
        <ul class="star">
            <li :class="changeBg(index)" v-for="(val,index) in starNum" :key="index" @click="getScore(index)"></li>
        </ul>
        <span class="score">{{score}}分</span>
    </div>
</template>

<script>
    export default {
        name: 'maAppraise',
        data(){
            return {
                starNum: [],
                score: 0
            }
        },
        props: {
            starLength: {
                type: [Number],
                default: 5
            },
            /* leftIcon: {
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
             }*/
        },
        methods: {
            getScore(index){
                this.score = index+1;
            },
            changeBg(index){
                if(index<this.score){
                    return 'starChange';
                }
            }
        },
        created(){
            this.starNum.length = this.starLength;
        },
    }
</script>

<style lang="less">
    @import 'style.less';
</style>