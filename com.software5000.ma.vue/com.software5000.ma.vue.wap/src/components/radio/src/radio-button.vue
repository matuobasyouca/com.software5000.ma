<template>
    <label
            class="zs-radio-button"
            :class="[
                  size ? 'zs-radio-button__' + size : '',
                  { 'is-active': value === label },
                  { 'is-disabled': isDisabled }
            ]"
    >
        <input
                class="zs-radio-button__orig-radio"
                :value="label"
                type="radio"
                v-model="value"
                :name="name"
                :disabled="isDisabled">
        <span class="zs-radio-button__inner" :style="value === label ? activeStyle : null">
            <slot><template>{{label}}</template></slot>
        </span>
    </label>
</template>
<script>
    export default {
        name: 'ZsRadioButton',

        props: {
            label: {},
            disabled: Boolean,
            name: String
        },
        computed: {
            value: {
                get() {
                    return this._radioGroup.value;
                },
                set(val) {
                    this._radioGroup.$emit('input', val);
                }
            },
            _radioGroup() {
                let parent = this.$parent;
                while (parent) {
                    if (parent.$options.componentName !== 'ZsRadioGroup') {
                        parent = parent.$parent;
                    } else {
                        return parent;
                    }
                }
                return false;
            },
            activeStyle() {
                return {
                    backgroundColor: this._radioGroup.fill || '',
                    borderColor: this._radioGroup.fill || '',
                    color: this._radioGroup.textColor || ''
                };
            },
            size() {
                return this._radioGroup.size;
            },
            isDisabled() {
                return this.disabled || this._radioGroup.disabled;
            }
        }
    };
</script>
