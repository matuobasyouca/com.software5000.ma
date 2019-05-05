<template>
    <div
            class="zs-select"
            v-clickoutside="handleClose">
        <div
                class="zs-select__tags"
                v-if="multiple"
                @click.stop="toggleMenu"
                ref="tags"
                :style="{ 'max-width': inputWidth - 32 + 'px' }">
            <transition-group @after-leave="resetInputHeight">
                <zs-tag
                        v-for="item in selected"
                        :key="typeof(item.value) == 'object' ? item.value.id : item.value"
                        closable
                        :hit="item.hitState"
                        type="primary"
                        @close="deleteTag($event, item)"
                        close-transition>
                    <span class="zs-select__tags-text">{{ item.currentLabel }}</span>
                </zs-tag>
            </transition-group>

            <input
                    type="text"
                    class="zs-select__input"
                    :class="`is-${ size }`"
                    @focus="visible = true"
                    :disabled="disabled"
                    @keyup="managePlaceholder"
                    @keydown="resetInputState"
                    @keydown.down.prevent="navigateOptions('next')"
                    @keydown.up.prevent="navigateOptions('prev')"
                    @keydown.enter.prevent="selectOption"
                    @keydown.esc.prevent="visible = false"
                    @keydown.delete="deletePrevTag"
                    v-model="query"
                    :debounce="remote ? 300 : 0"
                    v-if="filterable"
                    :style="{ width: inputLength + 'px', 'max-width': inputWidth - 42 + 'px' }"
                    ref="input">
        </div>
        <zs-input
                ref="reference"
                v-model="selectedLabel"
                type="text"
                :placeholder="currentPlaceholder"
                :name="name"
                :size="size"
                :disabled="disabled"
                :readonly="!filterable || multiple"
                :validate-event="false"
                @focus="handleFocus"
                @click="toggleMenu"
                @mousedown.native="handleMouseDown"
                @keyup.native="debouncedOnInputChange"
                @keydown.native.down.prevent="navigateOptions('next')"
                @keydown.native.up.prevent="navigateOptions('prev')"
                @keydown.native.enter.prevent="selectOption"
                @keydown.native.esc.prevent="visible = false"
                @keydown.native.tab="visible = false"
                @paste.native="debouncedOnInputChange"
                @mouseenter.native="inputHovering = true"
                @mouseleave.native="inputHovering = false"
                @on-icon-click="handleIconClick"
                :icon="iconClass">
        </zs-input>
        <transition
                name="zs-zoom-in-top"
                @after-leave="doDestroy"
                @after-enter="handleMenuEnter">
            <zs-select-menu
                    :popperClass="popperClass"
                    :multiple="multiple"
                    ref="popper"
                    v-show="visible && emptyText !== false">
                <zs-scrollbar
                        tag="ul"
                        wrap-class="zs-select-dropdown__wrap"
                        view-class="zs-select-dropdown__list"
                        :class="{ 'is-empty': !allowCreate && filteredOptionsCount === 0 }"
                        v-show="options.length > 0 && !loading">
                    <zs-option
                            :value="query"
                            created
                            v-if="showNewOption">
                    </zs-option>
                    <slot></slot>
                </zs-scrollbar>
                <p class="zs-select-dropdown__empty" v-if="emptyText && !allowCreate">{{ emptyText }}</p>
            </zs-select-menu>
        </transition>
    </div>
</template>

<script type="text/babel">
    import Emitter from '../../src/utils/emitter';
    import ZsInput from '../../input';
    import ZsSelectMenu from './select-dropdown.vue';
    import ZsOption from './option.vue';
    import ZsTag from '../../tag';
    import ZsScrollbar from '../../scrollbar';
    import {debounce} from 'throttle-debounce';
    import Clickoutside from '../../src/directives/clickoutside';
    import {addClass, removeClass, hasClass} from '../../src/utils/dom';
    import {addResizeListener, removeResizeListener} from '../../src/utils/resize-event';
    import merge from '../../src/utils/merge';
    const sizeMap = {
        'large': 42,
        'small': 30,
        'mini': 22
    };

    export default {
        mixins: [Emitter],

        name: 'ZsSelect',

        componentName: 'ZsSelect',

        computed: {
            //清空按钮的显示和切换
            iconClass() {
                let criteria = this.clearable && !this.disabled &&
                    this.inputHovering && !this.multiple &&
                    this.value !== undefined &&
                    this.value !== '';
                return criteria ? 'circle-close is-show-close' : (this.remote && this.filterable ? '' : 'caret-top');
            },

            debounce() {
                return this.remote ? 300 : 0;
            },
            //搜索时不同状态显示的文字
            emptyText() {
                if (this.loading) {
                    return this.loadingText
                } else {
                    if (this.remote && this.query === '' && this.options.length === 0) return false;
                    if (this.filterable && this.options.length > 0 && this.filteredOptionsCount === 0) {
                        return this.noMatchText
                    }
                    if (this.options.length === 0) {
                        return this.noDataText
                    }
                }
                return null;
            },
            //是否可以创建显示新的项目
            showNewOption() {
                let hasExistingOption = this.options.filter(option => !option.created)
                    .some(option => option.currentLabel === this.query);
                return this.filterable && this.allowCreate && this.query !== '' && !hasExistingOption;
            }
        },

        components: {
            ZsInput,
            ZsSelectMenu,
            ZsOption,
            ZsTag,
            ZsScrollbar
        },

        directives: {Clickoutside},

        props: {
            name: String,
            value: {},
            size: String,
            disabled: Boolean,
            clearable: Boolean,
            filterable: Boolean,
            allowCreate: Boolean,
            loading: Boolean,
            popperClass: String,
            remote: Boolean,
            loadingText: {
                type: String,
                default: '加载中'
            },
            noMatchText: {
                type: String,
                default: '无匹配数据'
            },
            noDataText: {
                type: String,
                default: '无数据'
            },
            remoteMethod: Function,
            filterMethod: Function,
            multiple: Boolean,
            multipleLimit: {
                type: Number,
                default: 0
            },
            placeholder: {
                type: String,
                default: '请选择'
            },
            validateEvent: {
                type: Boolean,
                default: true
            }
        },

        data() {
            return {
                options: [],//
                cachedOptions: [],
                createdOption: null,
                createdSelected: false,
                selected: this.multiple ? [] : {},
                isSelect: true,
                inputLength: 20,
                inputWidth: 0,

                optionsCount: 0,
                filteredOptionsCount: 0,
                dropdownUl: null,
                visible: false,
                selectedLabel: '',
                hoverIndex: -1,
                query: '',
                bottomOverflow: 0,
                topOverflow: 0,
                optionsAllDisabled: false,
                inputHovering: false,
                cachedPlaceHolder: '',
                currentPlaceholder: ''
            };
        },

        watch: {
            placeholder(val) {
                this.cachedPlaceHolder = this.currentPlaceholder = val;
            },
            value(val) {
                if (this.multiple) {
                    this.resetInputHeight();
                    if (val.length > 0 || (this.$refs.input && this.query !== '')) {
                        this.currentPlaceholder = '';
                    } else {
                        this.currentPlaceholder = this.cachedPlaceHolder;
                    }
                }
                this.setSelected();
                if (this.filterable && !this.multiple) {
                    this.inputLength = 20;
                }
                this.$emit('change', val);

                if (this.validateEvent) {
                    this.dispatch('ZsFormItem', 'el.form.change', val);
                }

            },
            query(val) {
                this.$nextTick(() => {
                    if (this.visible) this.broadcast('ZsSelectDropdown', 'updatePopper');
                });
                this.hoverIndex = -1;
                if (this.multiple && this.filterable) {
                    this.inputLength = this.$refs.input.value.length * 15 + 20;
                    this.managePlaceholder();
                    this.resetInputHeight();
                }
                if (this.remote && typeof this.remoteMethod === 'function') {
                    this.hoverIndex = -1;
                    this.remoteMethod(val);
                    this.broadcast('ZsOption', 'resetIndex');
                } else if (typeof this.filterMethod === 'function') {
                    this.filterMethod(val);
                    this.broadcast('ZsOptionGroup', 'queryChange');
                } else {
                    this.filteredOptionsCount = this.optionsCount;
                    this.broadcast('ZsOption', 'queryChange', val);
                    this.broadcast('ZsOptionGroup', 'queryChange');
                }
            },

            visible(val) {
                this.hoverIndex = -1;
                if (!val) {
                    this.$refs.reference.$refs.input.blur();
                    this.handleIconHide();
                    this.broadcast('ZsSelectDropdown', 'destroyPopper');
                    if (this.$refs.input) {
                        this.$refs.input.blur();
                    }
                    setTimeout(() => {
                        this.query = ''
                    }, 300)
                    this.selectedLabel = '';
                    this.inputLength = 20;
                    this.resetHoverIndex();
                    this.$nextTick(() => {
                        if (this.$refs.input &&
                            this.$refs.input.value === '' &&
                            this.selected.length === 0) {
                            this.currentPlaceholder = this.cachedPlaceHolder;
                        }
                    });
                    if (!this.multiple) {
                        this.getOverflows();
                        if (this.selected) {
                            if (this.filterable && this.allowCreate &&
                                this.createdSelected && this.createdOption) {
                                this.selectedLabel = this.createdOption.currentLabel;
                            } else {
                                this.selectedLabel = this.selected.currentLabel;
                            }
                        }
                    }
                } else {
                    this.handleIconShow();
                    this.broadcast('ZsSelectDropdown', 'updatePopper');
                    if (this.filterable) {
                        this.query = '';
                        if (this.multiple) {
                            this.$refs.input.focus();
                        } else {
                            if (!this.remote) {
                                this.broadcast('ZsOption', 'queryChange', '');
                                this.broadcast('ZsOptionGroup', 'queryChange');
                            }
                        }
                    }
                }
                this.$emit('visible-change', val);
            },

            options(val) {
                if (this.$isServer) return;
                this.optionsAllDisabled = val.length === val.filter(item => item.disabled === true).length;
                if (this.multiple) {
                    this.resetInputHeight();
                }
                let inputs = this.$el.querySelectorAll('input');
                if ([].indexOf.call(inputs, document.activeElement) === -1) {
                    this.setSelected();
                }
            }
        },

        methods: {
            handleIconHide() {
                let icon = this.$el.querySelector('.zs-input__icon');
                if (icon) {
                    removeClass(icon, 'is-reverse');
                }
            },

            handleIconShow() {
                let icon = this.$el.querySelector('.zs-input__icon');
                if (icon && !hasClass(icon, 'zs-icon-circle-close')) {
                    addClass(icon, 'is-reverse');
                }
            },

            handleMenuEnter() {
                if (!this.dropdownUl) {
                    this.dropdownUl = this.$refs.popper.$el.querySelector('.zs-select-dropdown__wrap');
                    this.getOverflows();
                }
                if (!this.multiple && this.dropdownUl) {
                    this.resetMenuScroll();
                }
            },

            getOverflows() {
                if (this.dropdownUl && this.selected && this.selected.$el) {
                    let selectedRect = this.selected.$el.getBoundingClientRect();
                    let popperRect = this.$refs.popper.$el.getBoundingClientRect();
                    this.bottomOverflow = selectedRect.bottom - popperRect.bottom;
                    this.topOverflow = selectedRect.top - popperRect.top;
                }
            },

            resetMenuScroll() {
                if (this.bottomOverflow > 0) {
                    this.dropdownUl.scrollTop += this.bottomOverflow;
                } else if (this.topOverflow < 0) {
                    this.dropdownUl.scrollTop += this.topOverflow;
                }
            },
            getOption(value) {
                let option;
                for (let i = this.cachedOptions.length - 1; i >= 0; i--) {
                    const cachedOption = this.cachedOptions[i];
                    if (cachedOption.value === value) {
                        option = cachedOption;
                        break;
                    }
                }
                if (option) return option;
                const label = typeof value === 'string' || typeof value === 'number'
                    ? value : '';
                let newOption = {
                    value: value,
                    currentLabel: label
                };
                if (this.multiple) {
                    newOption.hitState = false;
                }
                return newOption;
            },

            setSelected() {
                if (!this.multiple) {
                    let option = this.getOption(this.value);
                    if (option.created) {
                        this.createdOption = merge({}, option);
                        this.createdSelected = true;
                    } else {
                        this.createdSelected = false;
                    }
                    this.selectedLabel = option.currentLabel;
                    this.selected = option;
                    return;
                }
                let result = [];
                if (Array.isArray(this.value)) {
                    this.value.forEach(value => {
                        result.push(this.getOption(value));
                    });
                }
                this.selected = result;
                this.$nextTick(() => {
                    this.resetInputHeight();
                });
            },

            handleFocus() {
                this.visible = true;
            },

            handleIconClick(event) {
                if (this.iconClass.indexOf('circle-close') > -1) {
                    this.deleteSelected(event);
                } else {
                    this.toggleMenu();
                }
            },

            handleMouseDown(event) {
                if (event.target.tagName !== 'INPUT') return;
                if (this.visible) {
                    this.handleClose();
                    event.preventDefault();
                }
            },

            doDestroy() {
                this.$refs.popper && this.$refs.popper.doDestroy();
            },

            handleClose() {
                this.visible = false;
            },

            toggleLastOptionHitState(hit) {
                if (!Array.isArray(this.selected)) return;
                const option = this.selected[this.selected.length - 1];
                if (!option) return;

                if (hit === true || hit === false) {
                    option.hitState = hit;
                    return hit;
                }

                option.hitState = !option.hitState;
                return option.hitState;
            },

            deletePrevTag(e) {
                if (e.target.value.length <= 0 && !this.toggleLastOptionHitState()) {
                    this.value.pop();
                }
            },

            managePlaceholder() {
                if (this.currentPlaceholder !== '') {
                    this.currentPlaceholder = this.$refs.input.value ? '' : this.cachedPlaceHolder;
                }
            },

            resetInputState(e) {
                if (e.keyCode !== 8) this.toggleLastOptionHitState(false);
                this.inputLength = this.$refs.input.value.length * 15 + 20;
                this.resetInputHeight();
            },

            resetInputHeight() {
                this.$nextTick(() => {
                    if (!this.$refs.reference) return;
                    let input = this.$refs.reference.$refs.input;
                    input.style.height = Math.max(this.$refs.tags.clientHeight + 6, sizeMap[this.size] || 36) + 'px';
                    if (this.visible && this.emptyText !== false) {
                        this.broadcast('ZsSelectDropdown', 'updatePopper');
                    }
                });
            },

            resetHoverIndex() {
                setTimeout(() => {
                    if (!this.multiple) {
                        this.hoverIndex = this.options.indexOf(this.selected);
                    } else {
                        if (this.selected.length > 0) {
                            this.hoverIndex = Math.min.apply(null, this.selected.map(item => this.options.indexOf(item)));
                        } else {
                            this.hoverIndex = -1;
                        }
                    }
                }, 300);
            },
            handleOptionSelect(option) {
                if (!this.multiple) {
                    this.$emit('input', option.value);
                    this.visible = false;
                } else {
                    let optionIndex = -1;
                    this.value.forEach((item, index) => {
                        if (item === option.value) {
                            optionIndex = index;
                        }
                    });
                    if (optionIndex > -1) {
                        const temp = [];
                        this.value.forEach((item)=>{
                            temp.push(item)
                        })
                        temp.splice(optionIndex, 1);
                        this.$emit('input',temp)
//                        this.value.splice(optionIndex, 1);
                    } else if (this.multipleLimit <= 0 || this.value.length < this.multipleLimit) {
                        const temp = [];
                        this.value.forEach((item)=>{
                            temp.push(item)
                        })
                        temp.push(option.value);
                        this.$emit('input',temp)
//                        this.value.push(option.value);
                    }
                    if (option.created) {
                        this.query = '';
                        this.inputLength = 20;
                    }
                    if (this.filterable) this.$refs.input.focus();
                }
            },

            toggleMenu() {
                if (this.filterable && this.query === '' && this.visible) {
                    return;
                }
                if (!this.disabled) {
                    this.visible = !this.visible;
                }
            },

            navigateOptions(direction) {
                if (!this.visible) {
                    this.visible = true;
                    return;
                }
                if (this.options.length === 0 || this.filteredOptionsCount === 0) return;
                if (!this.optionsAllDisabled) {
                    if (direction === 'next') {
                        this.hoverIndex++;
                        if (this.hoverIndex === this.options.length) {
                            this.hoverIndex = 0;
                        }
                        this.resetScrollTop();
                        if (this.options[this.hoverIndex].disabled === true ||
                            this.options[this.hoverIndex].groupDisabled === true || !this.options[this.hoverIndex].visible) {
                            this.navigateOptions('next');
                        }
                    }
                    if (direction === 'prev') {
                        this.hoverIndex--;
                        if (this.hoverIndex < 0) {
                            this.hoverIndex = this.options.length - 1;
                        }
                        this.resetScrollTop();
                        if (this.options[this.hoverIndex].disabled === true ||
                            this.options[this.hoverIndex].groupDisabled === true || !this.options[this.hoverIndex].visible) {
                            this.navigateOptions('prev');
                        }
                    }
                }
            },

            resetScrollTop() {
                let bottomOverflowDistance = this.options[this.hoverIndex].$el.getBoundingClientRect().bottom -
                    this.$refs.popper.$el.getBoundingClientRect().bottom;
                let topOverflowDistance = this.options[this.hoverIndex].$el.getBoundingClientRect().top -
                    this.$refs.popper.$el.getBoundingClientRect().top;
                if (bottomOverflowDistance > 0) {
                    this.dropdownUl.scrollTop += bottomOverflowDistance;
                }
                if (topOverflowDistance < 0) {
                    this.dropdownUl.scrollTop += topOverflowDistance;
                }
            },

            selectOption() {
                if (this.options[this.hoverIndex]) {
                    this.handleOptionSelect(this.options[this.hoverIndex]);
                }
            },

            deleteSelected(event) {
                event.stopPropagation();
                this.$emit('input', '');
                this.visible = false;
            },

            deleteTag(event, tag) {
                let index = this.selected.indexOf(tag);
                if (index > -1 && !this.disabled) {
                    const temp = [];
                    this.value.forEach((item)=>{
                        temp.push(item)
                    })
                    temp.splice(index, 1);
                    this.$emit('input',temp)
                }
                event.stopPropagation();
            },

            onInputChange() {
                if (this.filterable) {
                    this.query = this.selectedLabel;
                }
            },

            onOptionDestroy(option) {
                this.optionsCount--;
                this.filteredOptionsCount--;
                let index = this.options.indexOf(option);
                if (index > -1) {
                    this.options.splice(index, 1);
                }
                this.broadcast('ZsOption', 'resetIndex');
            },

            resetInputWidth() {
                this.inputWidth = this.$refs.reference.$el.getBoundingClientRect().width;
            },

            handleResize() {
                this.resetInputWidth();
                if (this.multiple) this.resetInputHeight();
            }
        },

        created() {
            this.cachedPlaceHolder = this.currentPlaceholder = this.placeholder;
            if (this.multiple && !Array.isArray(this.value)) {
                this.$emit('input', []);
            }
            if (!this.multiple && Array.isArray(this.value)) {
                this.$emit('input', '');
            }
            this.setSelected();

            this.debouncedOnInputChange = debounce(this.debounce, this.onInputChange);

            this.$on('handleOptionClick', this.handleOptionSelect);
            this.$on('onOptionDestroy', this.onOptionDestroy);
            this.$on('setSelected', this.setSelected);
        },

        mounted() {
            if (this.multiple && Array.isArray(this.value) && this.value.length > 0) {
                this.currentPlaceholder = '';
            }
            addResizeListener(this.$el, this.handleResize);
            if (this.remote && this.multiple) {
                this.resetInputHeight();
            }
            this.$nextTick(() => {
                if (this.$refs.reference && this.$refs.reference.$el) {
                    this.inputWidth = this.$refs.reference.$el.getBoundingClientRect().width;
                }
            });
        },

        destroyed() {
            if (this.handleResize) removeResizeListener(this.$el, this.handleResize);
        }
    };
</script>
