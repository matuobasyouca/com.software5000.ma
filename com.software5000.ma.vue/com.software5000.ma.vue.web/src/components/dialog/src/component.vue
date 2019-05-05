<template>
    <transition name="zs-dialog-fade">
        <div class="zs-dialog__wrapper" v-show="value" @click.self="handleWrapperClick">
            <div
                    class="zs-dialog"
                    :class="[sizeClass, customClass]"
                    ref="dialog">
                <div class="zs-dialog__header">
                    <slot name="title">
                        <span class="zs-dialog__title">{{title}}</span>
                    </slot>
                    <zs-icon icon="cross" class="zs-dialog__close" @click='close()'
                             v-if="showClose"></zs-icon>
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
    import Popup from '../../src/utils/popup';

    export default {
        name: 'ZsDialog',

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
                default: true
            },

            size: {
                type: String,
                default: 'small'
            },

            customClass: {
                type: String,
                default: ''
            },

            top: {
                type: String,
                default: '15%'
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
