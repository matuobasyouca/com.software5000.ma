<template>
    <p
            class="icon-label__wrap"
            :class="customClass"
            :style="{paddingLeft:wrapPaddingLeft}"
            @click="doClick"
    >
        <i
                :class="[
                    typeClass,
                    bgColorClass
                ]"
                :style="setSize"
                :data-text="message | firstLetter"
        ></i>
        <span v-if="message" :style="{width:maxLength}">{{ message}}</span>
    </p>
</template>
<script>
    let bgColorMap = {
        primary : "primary",
        adorn : "adorn"
    }
    export default {
        name : 'iconLabel',
        props : {
            customClass : {
                type : String
            },
            message : {
                type : String
            },
            maxLetters : {
                type : Number
            },
            type : {
                type : String
            },

            bgColor : {
                type : String
            },
            size : {
                type : Number,
                default : 16
            }
        },
        computed : {
            typeClass(){
                return this.type ? 'icon-label--' + this.type : ''
            },
            setSize(){
                return {
                    width : this.size + 'px',
                    height : this.size + 'px',
                    lineHeight : this.size + 'px'
                }
            },
            maxLength(){
                return this.maxLetters  + 'em';
            },
            wrapPaddingLeft(){
                return this.size + 4 + 'px';
            },
            bgColorClass(){
                return this.bgColor && bgColorMap[this.bgColor] ? bgColorMap[this.bgColor] : '';
            }
        },
        filters : {
            firstLetter(val){
                return val ? val[0] : '';
            }
        },
        methods : {
            doClick(){
                this.$emit('click')
            }
        }
    };
</script>
<style>
    .icon-label__wrap {
        display: inline-block;
        position: relative;
    }
    [class*="icon-label-"] {
        display: block;
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        background-size: cover;
    }
    [class*="icon-label-"] + span {
        display: block;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
    }
    .icon-label--text:before {
        content: attr(data-text);
        display: inline-block;
        width: 16px;
        height: 16px;
        border-radius: 2px;
        background-color: #f39114;
        color: #fff;
        font-size: 12px;
        text-align: center;
    }
    .icon-label--text.primary {
        background-color: #1daae5;
    }
    .icon-label-text.primary {
        background-color: #f39114;
    }
    .icon-label--min-del {
        background-image: url("../assets/del.png")
    }
    .icon-label--min-edit {
        background-image: url("../assets/edit.png")
    }
    [class*="icon-label--min"] {
        width: 12px;
        height: 12px;
        background-size:cover;
    }
    .icon-label--min-dot-primary {
        background-image: url("../assets/dot.png")
    }
    .icon-label--dot-gray {

    }
    .icon-label--min-v {
        background-image: url("../assets/v.png")
    }
    .icon-label--min-v + span{
        color:#f39114;
    }
    .icon-label--min-member {
        background-image: url("../assets/member.png")
    }
    .icon-label--min-member + span{
        color:#f39114;
    }
    .icon-label--min-validity {
        background-image:url("../assets/validity.png");
    }
    [class*="icon-label--middle"] {
        width: 14px;
        height: 14px;
    }
    .icon-label--middle-v{
        background-image:url("../assets/middle_v.png");
    }
    .icon-label--middle-dot-gray{
        background-image:url("../assets/middle_dot_gray.png");
    }
    .icon-label--select-tip-selected{
        background-image:url("../assets/select_tip.png");
    }
    .icon-label--select-tip-unselected{
        background-image:url("../assets/unselect.png");
    }
    .icon-label--manage-title{
        background-image:url('../assets/manage_title.png');
    }
    .icon-label--manage-account{
        background-image:url('../assets/manage_account.png');
    }
    .icon-label--manage-item{
        background-image:url('../assets/manage_item.png');
    }
    .icon-label--manage-package{
        background-image:url('../assets/manage_package.png');
    }
    .icon-label--manage-member{
        background-image:url('../assets/manage_member.png');
    }
</style>