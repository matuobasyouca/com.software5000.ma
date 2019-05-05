<template>
    <div class="zs-input" :class="[
        customClass,
        type === 'textarea' ? 'zs-textarea' : '',
        size ? 'zs-input__' + size : '',
        {
          'is-disabled': disabled,
          'zs-input-group': $slots.prepend || $slots.append,
          'zs-input-group-append': $slots.append,
          'zs-input-group-prepend': $slots.prepend
        }
      ]" v-clickoutside="handleHideReset">
        <template v-if="type !== 'textarea'">
            <!-- 前置元素 -->
            <div class="zs-input-group__prepend" v-if="$slots.prepend">
                <slot name="prepend"></slot>
            </div>
            <!-- input 图标 -->
            <slot name="preIcon">
                <zs-icon
                        class="zs-input__icon zs-input__icon-prev"
                        :icon="preIcon"
                        :size="preSize"
                        v-if="preIcon"></zs-icon>
            </slot>
            <slot name="icon">
                <zs-icon
                        class="zs-input__icon"
                        :class="onIconClick ? 'is-clickable' : ''"
                        :icon="icon"
                        :size="iconSize"
                        v-show="showReset || icon && icon.indexOf('circle-cross')==-1"
                        v-if="icon"
                        @click="handleIconClick"></zs-icon>
            </slot>
            <input
                    class="zs-input__inner"
                    ref="input"
                    :type="type"
                    :name="name"
                    :placeholder="placeholder"
                    :disabled="disabled"
                    :readonly="readonly"
                    :maxlength="maxlength"
                    :minlength="minlength"
                    :autofocus="autofocus"
                    :min="min"
                    :max="max"
                    :step="step"
                    :value="currentValue"
                    @input="handleInput"
                    @focus="handleFocus"
                    @blur="handleBlur"
                    @keydown="handleKeyDown"
            >
            <!-- 后置元素 -->
            <div class="zs-input-group__append" v-if="$slots.append">
                <slot name="append"></slot>
            </div>
        </template>
        <textarea
                v-else
                class="zs-textarea__inner"
                ref="textarea"
                :style="textareaStyle"
                :value="currentValue"
                :name="name"
                :placeholder="placeholder"
                :disabled="disabled"
                :readonly="readonly"
                :rows="rows"
                :autofocus="autofocus"
                :maxlength="maxlength"
                :minlength="minlength"
                @input="handleInput"
                @focus="handleFocus"
                @blur="handleBlur">
    </textarea>
    </div>
</template>
<script>
    import calcTextareaHeight from './calcTextareaHeight';
    import merge from '../../src/utils/merge';
    import emitter from '../../src/utils/emitter';
    import zsIcon from '../../icon/index';
    import clickoutside from '../../src/directives/clickoutside'
    export default {
        name: 'zsInput',
        components: {
            zsIcon
        },
        directives: {
            clickoutside
        },
        mixins: [emitter],
        data(){
            return {
                currentValue: this.value,
                textareaCalcStyle: {},
                showReset: false
            }
        },
        props: {
            customClass: '',
            size: '',
            iconSize: {
                type: [String, Number],
                default: 15
            },
            preSize: [String, Number],
            value: {
                type: [String, Number],
                default: ''
            },
            placeholder: String,
            readonly: Boolean,
            autofocus: Boolean,
            icon: String,
            preIcon: String,
            disabled: Boolean,
            type: {
                type: String,
                default: 'text'
            },
            name: String,
            maxlength: Number,
            minlength: Number,
            max: {},
            min: {},
            step: {},
            onIconClick: Function,
            autosize: {
                type: [Boolean, Object],
                default: false
            },
            resize: String,
            rows: {
                type: Number,
                default: 2
            },
            validateEvent: {
                type: Boolean,
                default: true
            },
            autoSelect: Boolean
        },
        computed: {
            textareaStyle() {
                return merge({}, this.textareaCalcStyle, {resize: this.resize});
            }
        },
        watch: {
            value(val, oldVal){
                if (val === oldVal) return;
                this.currentValue = val;
            },
            currentValue(value){
                this.handleShowReset();
            }
        },
        methods: {
            handleKeyDown(e){
                this.$emit('keydown', e);
                if (e.keyCode == 9 || e.keyCode == 13 || e.keyCode == 84 || e.keyCode == 66) {
                    this.$emit('enter', e);
                }
            },
            handleBlur(event) {
                this.$emit('blur', event);
                if (this.validateEvent) {
                    this.dispatch('ZsFormItem', 'el.form.blur', [this.currentValue]);
                }
            },
            handleFocus(event) {
                this.autoSelect && this.$refs.input.select();
                this.handleShowReset();
                this.$emit('focus', event);
            },
            handleInput(event) {
                const value = event.target.value;
                this.$emit('input', value);
                this.$emit('change', value);
                this.setCurrentValue(value);
                if (this.validateEvent) {
                    this.dispatch('ZsFormItem', 'el.form.input', [this.currentValue]);
                }
            },
            handleIconClick(event) {
                if (this.onIconClick) {
                    this.onIconClick(event);
                }
                this.$emit('on-icon-click', this.currentValue, () => {
                    this.currentValue = ''
                });
                if (this.icon && this.icon.indexOf('circle-cross') > -1) {
                    this.currentValue = ''
                    this.$emit('input', this.currentValue)
                }
            },
            resizeTextarea() {
                if (this.$isServer) return;
                var {autosize, type} = this;
                if (!autosize || type !== 'textarea') return;
                const minRows = autosize.minRows;
                const maxRows = autosize.maxRows;

                this.textareaCalcStyle = calcTextareaHeight(this.$refs.textarea, minRows, maxRows);
            },
            setCurrentValue(value) {
                if (value === this.currentValue) return;
                this.$nextTick(_ => {
                    this.resizeTextarea();
                });
                this.currentValue = value;
                if (this.validateEvent) {
                    this.dispatch('ZsFormItem', 'el.form.change', [value]);
                }
            },
            handleShowReset(){
                if (this.icon && this.icon.indexOf('circle-cross') > -1) {
                    this.currentValue.length > 0 && (this.showReset = true);
                    this.currentValue.length === 0 && (this.showReset = false);
                }
            },
            handleHideReset(){
                this.showReset = false
            }
        },
        created(){
            this.icon && this.icon.indexOf('circle-cross') > -1 && (this.showReset = false);
        },
        mounted() {
            this.resizeTextarea();
        }
    };
</script>
