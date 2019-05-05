<template>
    <div class="zs-input-number"
         :class="[
         customClass,
            size ? 'zs-input-number__' + size : '',
            { 'is-disabled': disabled },
            { 'is-without-controls': !controls}
         ]">
        <span
                v-if="controls"
                class="zs-input-number__decrease"
                :class="{'is-disabled': minDisabled}"
                @click="decrease"
        ></span>
        <span
                v-if="controls"
                class="zs-input-number__increase"
                :class="{'is-disabled': maxDisabled}"
                @click="increase"
        ></span>
        <zs-input
                :value="currentValue"
                @keydown.up.native.prevent="increase"
                @keydown.down.native.prevent="decrease"
                @blur="handleBlur"
                @input="handleInput"
                :disabled="disabled"
                :size="size"
                :max="max"
                :min="min"
                ref="input"
        >
            <template slot="prepend" v-if="$slots.prepend">
                <slot name="prepend"></slot>
            </template>
            <template slot="append" v-if="$slots.append">
                <slot name="append"></slot>
            </template>
        </zs-input>
    </div>
</template>
<script>
    import ZsInput from '../../input';
    import {once, on} from '../../src/utils/dom';

    export default {
        name: 'ZsInputNumber',

        components: {
            ZsInput
        },
        props: {
            customClass: '',
            dataId: '',
            step: {
                type: Number,
                default: 1
            },
            max: {
                type: Number,
                default: Infinity
            },
            min: {
                type: Number,
                default: -Infinity
            },
            value: {
                default: 0
            },
            disabled: Boolean,
            size: String,
            controls: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
                currentValue: 0
            };
        },
        watch: {
            value: {
                immediate: true,
                handler(value) {
                    let newVal = Number(value);
                    if (isNaN(newVal)) return;
                    if (newVal >= this.max) newVal = this.max;
                    if (newVal <= this.min) newVal = this.min;
                    this.currentValue = newVal;
                    this.$emit('input', newVal);
                }
            }
        },
        computed: {
            minDisabled() {
                return this._decrease(this.currentValue, this.step) < this.min;
            },
            maxDisabled() {
                return this._increase(this.currentValue, this.step) > this.max;
            },
            precision() {
                const {currentValue, step, getPrecision} = this;
                return Math.max(getPrecision(currentValue), getPrecision(step));
            }
        },
        methods: {
            toPrecision(num, precision) {
                if (precision === undefined) precision = this.precision;
                return parseFloat(parseFloat(Number(num).toFixed(precision)));
            },
            getPrecision(val) {
                const valueString = val.toString();
                const dotPosition = valueString.indexOf('.');
                let precision = 0;
                if (dotPosition !== -1) {
                    precision = valueString.length - dotPosition - 1;
                }
                return precision;
            },
            _increase(val, step) {

                if (typeof val !== 'number') return this.currentValue;

                const precisionFactor = Math.pow(10, this.precision);

                return this.toPrecision((precisionFactor * val + precisionFactor * step) / precisionFactor);
            },
            _decrease(val, step) {
                if (typeof val !== 'number') return this.currentValue;

                const precisionFactor = Math.pow(10, this.precision);

                return this.toPrecision((precisionFactor * val - precisionFactor * step) / precisionFactor);
            },
            increase() {
                this.$emit('click');
                if (this.disabled || this.maxDisabled) return;
                const value = this.currentValue || 0;
                const newVal = this._increase(value, this.step);
                if (newVal > this.max) return;
                this.setCurrentValue(newVal);
            },
            decrease() {
                if (this.disabled || this.minDisabled) return;
                const value = this.currentValue || 0;
                const newVal = this._decrease(value, this.step);
                if (newVal < this.min) return;
                this.setCurrentValue(newVal);
            },
            handleBlur() {
                this.setCurrentValue(this.$refs.input.value);
            },
            setCurrentValue(newVal) {
                const oldVal = this.currentValue;
                if (newVal >= this.max) newVal = this.max;
                if (newVal <= this.min) newVal = this.min;
                if (oldVal === newVal) return;
                this.$emit('change', newVal, oldVal);
                this.$emit('input', newVal);
                this.currentValue = newVal;
            },
            handleInput(val) {
                const newVal = Number(val);
                if (!isNaN(newVal)) {
                    this.setCurrentValue(newVal);
                }
            },
            mutateVal(newVal){
                this.$nextTick(() => {
                    this.currentValue = newVal;
                })
            }
        }
    };
</script>
