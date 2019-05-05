<template>
    <div class="zs-radio-group">
        <slot></slot>
    </div>
</template>
<script>
    import Emitter from '../../src/utils/emitter';

    export default {
        name: 'ZsRadioGroup',
        componentName: 'ZsRadioGroup',
        mixins: [Emitter],
        props: {
            value: {},
            size: String,
            fill: String,
            textColor: String,
            disabled: Boolean
        },
        watch: {
            value(val) {
                this.$emit('change', val);
                this.dispatch('ZsFormItem', 'el.form.change', [this.value]);
            }
        }
    };
</script>

