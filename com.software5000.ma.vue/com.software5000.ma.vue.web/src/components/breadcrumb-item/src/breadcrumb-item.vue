<template>
    <span class="zs-breadcrumb__item">
        <span class="zs-breadcrumb__item-inner" @click="handleLinkTo">
            <slot></slot>
        </span>
        <span class="zs-breadcrumb__separator">
            <zs-icon :icon="separator" size="8"></zs-icon>
        </span>
    </span>
</template>
<script>
    export default {
        name : 'ZsBreadcrumbItem',
        props : {
            to : {},
            replace : Boolean
        },
        data() {
            return {
                separator : ''
            };
        },
        methods:{
            handleLinkTo(){
                if (this.to) {
                    window.location = this.to;
                }
            }
        },
        mounted() {
            this.separator = this.$parent.separator;
        }
    };
</script>
