<template>
    <div
            class="zs-select-dropdown"
            :class="[{ 'is-multiple': multiple }, popperClass]"
            :style="{ minWidth: minWidth }">
        <slot></slot>
    </div>
</template>

<script>
    import Popper from '../../src/utils/vue-popper';

    export default {
        name : 'ZsSelectDropdown',

        componentName : 'ZsSelectDropdown',

        mixins : [Popper],

        props : {
            placement : {
                default : 'bottom-start'
            },

            boundariesPadding : {
                default : 0
            },

            popperOptions : {
                default() {
                    return {
                        forceAbsolute : true,
                        gpuAcceleration : false
                    };
                }
            },

            popperClass : String,

            multiple : Boolean
        },

        data() {
            return {
                minWidth : ''
            };
        },

        watch : {
            '$parent.inputWidth'() {
                this.minWidth = this.$parent.$el.getBoundingClientRect().width + 'px';
            }
        },

        mounted() {
            this.referenceElm = this.$parent.$refs.reference.$el;
            this.$parent.popperElm = this.popperElm = this.$el;
            this.$on('updatePopper', this.updatePopper);
            this.$on('destroyPopper', this.destroyPopper);
        }
    };
</script>
