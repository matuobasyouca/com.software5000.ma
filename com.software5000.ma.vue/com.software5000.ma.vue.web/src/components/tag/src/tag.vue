<template>
    <transition :name="closeTransition ? '' : 'zs-zoom-in-center'">
        <p
                class="zs-tag"
                :class="[customClass,type ? 'zs-tag__' + type : '', {'is-hit': hit},]"
                :style="{backgroundColor: color}"
                @click="handleClick">
            <slot></slot>
            <i class="zs-tag__icon zs-tag__icon-close"
               v-if="closable"
               @click="handleClose"></i>
            <i v-if="!closable && icon" :class="['zs-tag__icon',`zs-icon-${icon}`]"></i>
        </p>
    </transition>
</template>
<script>
    export default {
        name : 'ZsTag',
        props : {
            customClass : '',
            text : String,
            closable : Boolean,
            icon : String,
            type : String,
            hit : Boolean,
            closeTransition : Boolean,
            color : String
        },
        methods : {
            handleClose(event) {
                this.$emit('close', event);
            },
            handleClick(event){
                this.$emit('click',event);
            }
        }
    };
</script>
