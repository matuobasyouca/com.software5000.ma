<template>
    <ul class="zs-select-group__wrap">
        <li class="zs-select-group__title" v-show="visible">{{ label }}</li>
        <li>
            <ul class="zs-select-group">
                <slot></slot>
            </ul>
        </li>
    </ul>
</template>

<script>
    import Emitter from '../../src/utils/emitter';

    export default {
        mixins : [Emitter],

        name : 'ZsOptionGroup',

        componentName : 'ZsOptionGroup',

        props : {
            label : String,
            disabled : {
                type : Boolean,
                default : false
            }
        },

        data() {
            return {
                visible : true
            };
        },

        watch : {
            disabled(val) {
                this.broadcast('ZsOption', 'handleGroupDisabled', val);
            }
        },

        methods : {
            queryChange() {
                this.visible = this.$children &&
                    Array.isArray(this.$children) &&
                    this.$children.some(option => option.visible === true);
            }
        },

        created() {
            this.$on('queryChange', this.queryChange);
        },

        mounted() {
            if (this.disabled) {
                this.broadcast('ZsOption', 'handleGroupDisabled', this.disabled);
            }
        }
    };
</script>
