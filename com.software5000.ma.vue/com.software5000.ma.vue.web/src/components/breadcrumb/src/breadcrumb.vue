<template>
    <div class="zs-breadcrumb">
        <div class="zs-breadcrumb-inner">
            <span class="zs-breadcrumb__item">您当前的位置：</span>
            <slot></slot>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'ZsBreadcrumb',

        props: {
            separator: {
                type: String,
                default: 'arrow-right'
            }
        }
    };
</script>
