<template>
    <div
            :class="[prefixCls]"
            v-clickoutside="handleClose">
        <div ref="reference" :class="[prefixCls + '-rel']">
            <slot>
                <zs-input
                        :class="[prefixCls + '-editor']"
                        readonly
                        :disabled="disabled"
                        :size="size"
                        :placeholder="placeholder"
                        :value="visualValue"
                        @change="handleInputChange"
                        @focus="handleFocus"
                        @on-icon-click="handleIconClick"
                        @mouseenter.native="handleInputMouseenter"
                        @mouseleave.native="handleInputMouseleave"
                        :icon="iconType"
                        :icon-size="16"></zs-input>
            </slot>
        </div>
        <transition :name="transition">
            <Drop v-show="opened" :placement="placement" ref="drop">
                <div ref="picker">
                    <date-panel
                            ref="datePanel"
                            v-show="panelVisible"
                            :disabledDate="disabledDate"
                    ></date-panel>
                </div>
            </Drop>
        </transition>
    </div>
</template>
<script>
    import Vue from 'vue';
    import ZsInput from '../../components/input';
    import Drop from '../../components/select/src/select-dropdown.vue';
    import clickoutside from '../src/directives/clickoutside';
    import {oneOf} from '../src/utils/util';
    import {formatDate, parseDate} from './util';
    import Emitter from '../src/utils/emitter';
    import DatePanel from './panel/date.vue';
    import {DateUtils} from '../../assets/js/utils';

    const prefixCls = 'ivu-date-picker';

    const DEFAULT_FORMATS = {
        date: 'yyyy-MM-dd',
        month: 'yyyy-MM',
        year: 'yyyy',
        datetime: 'yyyy-MM-dd HH:mm:ss',
        time: 'HH:mm:ss',
        timerange: 'HH:mm:ss',
        daterange: 'yyyy-MM-dd',
        datetimerange: 'yyyy-MM-dd HH:mm:ss'
    };

    const RANGE_SEPARATOR = ' - ';

    const DATE_FORMATTER = function (value, format) {
        return formatDate(value, format);
    };
    const DATE_PARSER = function (text, format) {
        return parseDate(text, format);
    };
    const RANGE_FORMATTER = function (value, format) {
        if (Array.isArray(value) && value.length === 2) {
            const start = value[0];
            const end = value[1];

            if (start && end) {
                return formatDate(start, format) + RANGE_SEPARATOR + formatDate(end, format);
            }
        }
        return '';
    };
    const RANGE_PARSER = function (text, format) {
        const array = text.split(RANGE_SEPARATOR);
        if (array.length === 2) {
            const range1 = array[0];
            const range2 = array[1];

            return [parseDate(range1, format), parseDate(range2, format)];
        }
        return [];
    };

    const TYPE_VALUE_RESOLVER_MAP = {
        default: {
            formatter(value) {
                if (!value) return '';
                return '' + value;
            },
            parser(text) {
                if (text === undefined || text === '') return null;
                return text;
            }
        },
        date: {
            formatter: DATE_FORMATTER,
            parser: DATE_PARSER
        },
        datetime: {
            formatter: DATE_FORMATTER,
            parser: DATE_PARSER
        },
        daterange: {
            formatter: RANGE_FORMATTER,
            parser: RANGE_PARSER
        },
        datetimerange: {
            formatter: RANGE_FORMATTER,
            parser: RANGE_PARSER
        },
        timerange: {
            formatter: RANGE_FORMATTER,
            parser: RANGE_PARSER
        },
        time: {
            formatter: DATE_FORMATTER,
            parser: DATE_PARSER
        },
        month: {
            formatter: DATE_FORMATTER,
            parser: DATE_PARSER
        },
        year: {
            formatter: DATE_FORMATTER,
            parser: DATE_PARSER
        },
        number: {
            formatter(value) {
                if (!value) return '';
                return '' + value;
            },
            parser(text) {
                let result = Number(text);

                if (!isNaN(text)) {
                    return result;
                } else {
                    return null;
                }
            }
        }
    };

    export default {
        name: 'DatePicker',
        mixins: [Emitter],
        components: {ZsInput, Drop, DatePanel},
        directives: {clickoutside},
        props: {
            type: {
                validator (value) {
                    return oneOf(value, ['year', 'month', 'date', 'daterange', 'datetime', 'datetimerange']);
                },
                default: 'date'
            },
            value: {},
            format: {
                type: String
            },
            readonly: {
                type: Boolean,
                default: false
            },
            disabled: {
                type: Boolean,
                default: false
            },
            editable: {
                type: Boolean,
                default: true
            },
            clearable: {
                type: Boolean,
                default: true
            },
            confirm: {
                type: Boolean,
                default: false
            },
            open: {
                type: Boolean,
                default: null
            },
            size: {
                validator (value) {
                    return oneOf(value, ['small', 'large']);
                }
            },
            placeholder: {
                type: String,
                default: ''
            },
            placement: {
                validator (value) {
                    return oneOf(value, ['top', 'top-start', 'top-end', 'bottom', 'bottom-start', 'bottom-end', 'left', 'left-start', 'left-end', 'right', 'right-start', 'right-end']);
                },
                default: 'bottom-start'
            },
            options: {
                type: Object
            },
            disabledDate: {
                type: [Number, String],
                default: -1
            },
            icon: ''
        },
        data () {
            return {
                panelVisible: false,
                prefixCls: prefixCls,
                showClose: false,
                visible: false,
                picker: null,
                internalValue: '',
                disableClickOutSide: false,    // fixed when click a date,trigger clickoutside to close picker
                currentValue: this.value
            };
        },
        computed: {
            opened () {
                return this.open === null ? this.visible : this.open;
            },
            iconType () {
                let icon = 'date';
                if (this.type === 'time' || this.type === 'timerange') icon = 'ios-clock-outline';
                if (this.showClose) icon = 'circle-cross';
                if (this.icon) icon = this.icon;
                return icon;
            },
            transition () {
                if (this.placement === 'bottom-start' || this.placement === 'bottom' || this.placement === 'bottom-end') {
                    return 'slide-up';
                } else {
                    return 'slide-down';
                }
            },
            selectionMode() {
                if (this.type === 'month') {
                    return 'month';
                } else if (this.type === 'year') {
                    return 'year';
                }

                return 'day';
            },
            visualValue: {
                get () {
                    const value = this.internalValue;
                    if (!value) return;
                    const formatter = (
                        TYPE_VALUE_RESOLVER_MAP[this.type] ||
                        TYPE_VALUE_RESOLVER_MAP['default']
                    ).formatter;
                    const format = DEFAULT_FORMATS[this.type];

                    return formatter(value, this.format || format);
                },

                set (value) {
                    if (value) {
                        const type = this.type;
                        const parser = (
                            TYPE_VALUE_RESOLVER_MAP[type] ||
                            TYPE_VALUE_RESOLVER_MAP['default']
                        ).parser;
                        const parsedValue = parser(value, this.format || DEFAULT_FORMATS[type]);
                        if (parsedValue) {
                            if (this.picker) this.picker.value = parsedValue;
                        }
                        return;
                    }
                    if (this.picker) this.picker.value = value;
                }
            }
        },
        methods: {
            handleClose () {
                if (this.open !== null) return;
                if (!this.disableClickOutSide) this.visible = false;
                this.disableClickOutSide = false;
            },
            handleFocus () {
                if (this.readonly) return;
                this.visible = true;
            },
            handleInputChange (val) {
                const oldValue = this.visualValue;
                const value = val;

                let correctValue = '';
                let correctDate = '';
                const type = this.type;
                const format = this.format || DEFAULT_FORMATS[type];

                if (type === 'daterange' || type === 'timerange' || type === 'datetimerange') {
                    const parser = (
                        TYPE_VALUE_RESOLVER_MAP[type] ||
                        TYPE_VALUE_RESOLVER_MAP['default']
                    ).parser;

                    const formatter = (
                        TYPE_VALUE_RESOLVER_MAP[type] ||
                        TYPE_VALUE_RESOLVER_MAP['default']
                    ).formatter;

                    const parsedValue = parser(value, format);

                    if (parsedValue[0] instanceof Date && parsedValue[1] instanceof Date) {
                        if (parsedValue[0].getTime() > parsedValue[1].getTime()) {
                            correctValue = oldValue;
                        } else {
                            correctValue = formatter(parsedValue, format);
                        }
                        // todo 判断disabledDate
                    } else {
                        correctValue = oldValue;
                    }

                    correctDate = parser(correctValue, format);
                } else if (type === 'time') {
                    const parsedDate = parseDate(value, format);

                    if (parsedDate instanceof Date) {
                        if (this.disabledHours.length || this.disabledMinutes.length || this.disabledSeconds.length) {
                            const hours = parsedDate.getHours();
                            const minutes = parsedDate.getMinutes();
                            const seconds = parsedDate.getSeconds();

                            if ((this.disabledHours.length && this.disabledHours.indexOf(hours) > -1) ||
                                (this.disabledMinutes.length && this.disabledMinutes.indexOf(minutes) > -1) ||
                                (this.disabledSeconds.length && this.disabledSeconds.indexOf(seconds) > -1)) {
                                correctValue = oldValue;
                            } else {
                                correctValue = formatDate(parsedDate, format);
                            }
                        } else {
                            correctValue = formatDate(parsedDate, format);
                        }
                    } else {
                        correctValue = oldValue;
                    }

                    correctDate = parseDate(correctValue, format);
                } else {
                    const parsedDate = parseDate(value, format);

                    if (parsedDate instanceof Date) {
                        const options = this.options || false;
                        if (options && options.disabledDate && typeof options.disabledDate === 'function' && options.disabledDate(new Date(parsedDate))) {
                            correctValue = oldValue;
                        } else {
                            correctValue = formatDate(parsedDate, format);
                        }
                    } else {
                        correctValue = oldValue;
                    }

                    correctDate = parseDate(correctValue, format);
                }

                this.visualValue = correctValue;
                this.internalValue = correctDate;
                this.currentValue = correctDate;

                if (correctValue !== oldValue) this.emitChange(correctDate);
            },
            handleInputMouseenter () {
                if (this.readonly || this.disabled) return;
                if (this.visualValue && this.clearable) {
                    this.showClose = true;
                }
            },
            handleInputMouseleave () {
                this.showClose = false;
            },
            handleIconClick () {
                if (this.showClose) {
                    this.handleClear();
                } else {
                    this.handleFocus();
                }
            },
            handleClear () {
                this.visible = false;
                this.internalValue = '';
                this.currentValue = '';
                this.$emit('on-clear');
                this.dispatch('FormItem', 'on-form-change', '');
            },
            setPicker () {
                if (true) {
                    let isConfirm = this.confirm;
                    const type = this.type;
                    this.picker = this.$refs.datePanel;
                    if (type === 'datetime' || type === 'datetimerange') {
                        isConfirm = true;
                        this.picker.showTime = true;
                    }
                    this.picker.value = this.internalValue;
                    this.picker.confirm = isConfirm;
                    this.picker.selectionMode = this.selectionMode;
                    if (this.format) this.picker.format = this.format;

                    // TimePicker
                    if (this.disabledHours) this.picker.disabledHours = this.disabledHours;
                    if (this.disabledMinutes) this.picker.disabledMinutes = this.disabledMinutes;
                    if (this.disabledSeconds) this.picker.disabledSeconds = this.disabledSeconds;
                    if (this.hideDisabledOptions) this.picker.hideDisabledOptions = this.hideDisabledOptions;

                    const options = this.options;
                    for (const option in options) {
                        this.picker[option] = options[option];
                    }

                    this.picker.$on('on-pick', (date, visible = false) => {
                        if (!isConfirm) this.visible = visible;
                        this.currentValue = date;
                        this.picker.value = date;
                        this.picker.resetView && this.picker.resetView();
                        this.emitChange(date);
                    });

                    this.picker.$on('on-pick-clear', () => {
                        this.handleClear();
                    });
                    this.picker.$on('on-pick-success', () => {
                        this.visible = false;
                        this.$emit('on-ok');
                    });
                    this.picker.$on('on-pick-click', () => this.disableClickOutSide = true);
                }
                if (this.internalValue instanceof Date) {
                    this.picker.date = new Date(this.internalValue.getTime());
                } else {
                    this.picker.value = this.internalValue;
                }
                this.picker.resetView && this.picker.resetView();
            },
            emitChange (date) {
                const newDate = this.formattingDate(date);

                this.$emit('on-change', newDate);
                this.dispatch('ZsFormItem', 'el.form.change', newDate);
                this.dispatch('FormItem', 'on-form-change', newDate);
            },
            formattingDate (date) {
                const type = this.type;
                const format = this.format || DEFAULT_FORMATS[type];
                const formatter = (
                    TYPE_VALUE_RESOLVER_MAP[type] ||
                    TYPE_VALUE_RESOLVER_MAP['default']
                ).formatter;

                let newDate = formatter(date, format);
                if (type === 'daterange' || type === 'timerange') {
                    newDate = [newDate.split(RANGE_SEPARATOR)[0], newDate.split(RANGE_SEPARATOR)[1]];
                }
                return newDate;
            }
        },
        watch: {
            visible (val) {
                if (val) {
                    this.panelVisible = true;
                    this.setPicker();
                    //                                        this.$refs.drop.update();
//                    if (this.open === null) this.$emit('on-open-change', true);
                } else {
                    this.panelVisible = false;
//                    if (this.picker) this.picker.resetView && this.picker.resetView(true);

//                    if (this.open === null) this.$emit('on-open-change', false);
                }
            },
            internalValue(val) {
                if (!val && this.picker && typeof this.picker.handleClear === 'function') {
                    this.picker.handleClear();
                }
                //                this.$emit('input', val);
            },
            value (val) {
                this.currentValue = val;
            },
            currentValue: {
                immediate: true,
                handler (val) {
                    const type = this.type;
                    const parser = (
                        TYPE_VALUE_RESOLVER_MAP[type] ||
                        TYPE_VALUE_RESOLVER_MAP['default']
                    ).parser;

                    if (val && type === 'time' && !(val instanceof Date)) {
                        val = parser(val, this.format || DEFAULT_FORMATS[type]);
                    } else if (val && type === 'timerange' && Array.isArray(val) && val.length === 2 && !(val[0] instanceof Date) && !(val[1] instanceof Date)) {
                        val = val.join(RANGE_SEPARATOR);
                        val = parser(val, this.format || DEFAULT_FORMATS[type]);
                    }

                    this.internalValue = val;
                    let v = val && typeof val != 'string' ? DateUtils.dateFormat(val, 'yyyy-MM-dd') : val;
                    this.$emit('input', v);
                }
            },
            open (val) {
                if (val === true) {
                    this.visible = val;
                    this.$emit('on-open-change', true);
                } else if (val === false) {
                    this.$emit('on-open-change', false);
                }
            }
        },
        beforeDestroy () {
//            if (this.picker) {
//                this.picker.$destroy();
//            }
        },
        mounted () {
            if (this.open !== null) this.visible = this.open;
        }
    };
</script>
