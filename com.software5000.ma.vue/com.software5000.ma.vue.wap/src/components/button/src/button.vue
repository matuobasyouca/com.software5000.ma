<template>
    <div :disabled="disabled" class="zs-button"
         @click="handleClick"
         :autofocus="autofocus"
         :class="[customClass,
      type ? 'zs-button--' + type : '',
      size ? 'zs-button--' + size : '',
      {
        'is-disabled': disabled,
        'is-loading': loading,
        'is-plain': plain
      }
    ]"
    >
        <i class="zs-icon-loading" v-if="loading"></i>
        <i :class="'zs-icon-' + icon" v-if="icon && !loading"></i>
        <span v-if="$slots.default"><slot></slot></span>
    </div>
</template>
<script>
    export default {
        name: 'ZsButton',

        props: {
            type: {
                type: String,
                default: 'default'
            },
            size: String,
            icon: {
                type: String,
                default: ''
            },
            loading: Boolean,
            disabled: Boolean,
            plain: Boolean,
            autofocus: Boolean,
            customClass: {
                type: String,
                default: ''
            }
        },

        methods: {
            handleClick(evt) {
                this.$emit('click', evt);
            }
        }
    };
</script>
<!--<style scoped>-->
    <!--[class*=zs-icon&#45;&#45;] {-->
        <!--display: inline-block;-->
        <!--transform: translateY(1px);-->
        <!--background-size: cover;-->
    <!--}-->

    <!--[class*=zs-icon&#45;&#45;mini-] {-->
        <!--width: 12px;-->
        <!--height: 12px;-->
    <!--}-->

    <!--.zs-icon&#45;&#45;mini-on-sale-b {-->
        <!--background-image: url(../assets/onsale-b.png)-->
    <!--}-->

    <!--.zs-icon&#45;&#45;mini-on-sale {-->
        <!--background-image: url(../assets/onsale.png)-->
    <!--}-->

    <!--.zs-icon&#45;&#45;mini-dis-sale-b {-->
        <!--background-image: url(../assets/disSale-b.png)-->
    <!--}-->

    <!--.zs-icon&#45;&#45;mini-dis-sale {-->
        <!--background-image: url(../assets/disSale.png)-->
    <!--}-->

    <!--.zs-icon&#45;&#45;mini-edit {-->
        <!--background-image: url("../assets/edit.png");-->
    <!--}-->

    <!--.zs-icon&#45;&#45;mini-del {-->
        <!--background-image: url("../assets/del.png");-->
    <!--}-->
<!--</style>-->
