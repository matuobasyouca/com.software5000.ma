<template>
    <transition name="zs-dialog-fade">
        <div class="zs-dialog__wrapper" v-show="value" @click.self="handleWrapperClick">
            <div
                    class="zs-dialog"
                    :class="[sizeClass, customClass]"
                    ref="dialog"
                    :style="style">
                <div class="zs-dialog__header" v-if="showTitle">
                    <slot name="title">
                        <span class="zs-dialog__title" v-if="title">{{title}}</span>
                    </slot>
                    <zs-icon v-if="showClose" class="zs-dialog__headerbtn" :size="16" icon="cross" @click='close()'></zs-icon>
                </div>
                <div class="zs-dialog__body" v-if="rendered">
                    <slot></slot>
                </div>
                <div class="zs-dialog__footer" v-if="$slots.footer">
                    <slot name="footer"></slot>
                </div>
            </div>
        </div>
    </transition>
</template>

<script>
    import Popup from '../../src/utils/popup/index';
    import zsIcon from '../../icon/index';
    export default {
        name: 'ZsDialog',
        components:{
            zsIcon
        },

        mixins: [Popup],

        props: {
            title: {
                type: String,
                default: ''
            },

            modal: {
                type: Boolean,
                default: true
            },

            modalAppendToBody: {
                type: Boolean,
                default: true
            },

            lockScroll: {
                type: Boolean,
                default: true
            },

            closeOnClickModal: {
                type: Boolean,
                default: true
            },

            closeOnPressEscape: {
                type: Boolean,
                default: true
            },

            showClose: {
                type: Boolean,
                default: false
            },

            size: {
                type: String,
                default: 'large'
            },

            customClass: {
                type: String,
                default: ''
            },

            top: {
                type: String,
                default: '50%'
            }
        },

        watch: {
            value(val) {
                this.$emit('input', val);
                if (val) {
                    this.$emit('open');
                    this.$nextTick(() => {
                        this.$refs.dialog.scrollTop = 0;
                    });
                } else {
                    this.$emit('close');
                }
            }
        },
        computed: {
            sizeClass() {
                return `zs-dialog__${ this.size }`;
            },
            style() {
                return this.size === 'full' ? {} : {'margin-bottom': '50px', 'top': this.top};
            },
            showTitle(){
                return this.showClose || this.title || this.$slots.title;
            }
        },

        methods: {
            handleWrapperClick() {
                if (this.closeOnClickModal) {
                    this.close();
                }
            }
        },

        mounted() {
            if (this.value) {
                this.rendered = true;
                this.open();
            }
        }
    };
</script>
