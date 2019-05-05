<template>
    <transition name="msgbox-fade">
        <div class="zs-message-box__wrapper" v-show="value" @click.self="handleWrapperClick">
            <div class="zs-message-box" :class="customClass">
                <div class="zs-message-box__header" v-if="title !== undefined">
                    <div class="zs-message-box__title" v-html="title"></div>
                    <zs-icon icon="cross" class="zs-message-box__close" @click="handleAction('cancel')"
                             v-if="showClose"></zs-icon>
                </div>
                <div class="zs-message-box__content" v-if="message !== ''">
                    <div
                            class="zs-message-box__message">
                        <zs-icon :icon="confirmType" size="60" v-if="type"></zs-icon>
                        <p class="confirm-text">{{ message }}</p>
                    </div>
                </div>
                <ul class="zs-message-box__btns">
                    <li>
                        <zs-button
                                :loading="cancelButtonLoading"
                                :class="[ cancelButtonClasses ]"
                                type="cancel"
                                v-show="showCancelButton"
                                @click.native="handleAction('cancel')">
                            {{ cancelButtonText }}
                        </zs-button>
                    </li>
                    <li>
                        <zs-button
                                :loading="confirmButtonLoading"
                                ref="confirm"
                                :class="[ confirmButtonClasses ]"
                                type="primary"
                                v-show="showConfirmButton"
                                @click.native="handleAction('confirm')">
                            {{ confirmButtonText }}
                        </zs-button>
                    </li>
                </ul>
            </div>
        </div>
    </transition>
</template>

<script type="text/babel">
    import Popup from '../../src/utils/popup';
    import ZsButton from '../../button';

    let typeMap = {
        delete: 'delete',
        warning: 'warning',
        danger: 'danger'
    };

    export default {
        mixins: [Popup],

        props: {
            modal: {
                default: true
            },
            lockScroll: {
                default: true
            },
            showClose: {
                type: Boolean,
                default: true
            },
            closeOnClickModal: {
                default: true
            },
            closeOnPressEscape: {
                default: true
            }
        },

        components: {
            ZsButton
        },

        computed: {
            confirmType() {
                return this.type && typeMap[this.type] ? typeMap[this.type] : this.type;
            },

            confirmButtonClasses() {
                return `${ this.confirmButtonClass }`;
            },
            cancelButtonClasses() {
                return `${ this.cancelButtonClass }`;
            }
        },

        methods: {
            getSafeClose() {
                const currentId = this.uid;
                return () => {
                    this.$nextTick(() => {
                        if (currentId === this.uid) this.doClose();
                    })
                }
            },
            doClose() {
                if (!this.value) return;
                this.value = false;
                this._closing = true;
                if (this.lockScroll) {
                    setTimeout(() => {
                        if (this.modal && this.bodyOverflow !== 'hidden') {
                            document.body.style.overflow = this.bodyOverflow;
                            document.body.style.paddingRight = this.bodyPaddingRight;
                        }
                        this.bodyOverflow = null;
                        this.bodyPaddingRight = null;
                    }, 200);
                }

                this.opened = false;
                this.doAfterClose();
                if (this.action) this.callback(this.action, this);
            },

            handleWrapperClick() {
                if (this.closeOnClickModal) {
                    this.action = '';
                    this.doClose();
                }
            },

            handleAction(action) {
                this.action = action;
                if (typeof this.beforeClose === 'function') {
                    this.close = this.getSafeClose();
                    this.beforeClose(action, this, this.close);
                } else {
                    this.doClose();
                }
            }

        },
        watch: {
            value(val) {
                if (val) this.uid++;
                if (this.$type === 'alert' || this.$type === 'confirm') {
                    this.$nextTick(() => {
                        this.$refs.confirm.$el.focus();
                    })
                }
            }
        },
        data() {
            return {
                uid: 1,
                title: undefined,
                message: '',
                type: '',
                customClass: '',
                showConfirmButton: true,
                showCancelButton: false,
                action: '',
                confirmButtonText: '',
                cancelButtonText: '',
                confirmButtonLoading: false,
                cancelButtonLoading: false,
                confirmButtonClass: '',
                confirmButtonDisabled: false,
                cancelButtonClass: '',
                callback: null
            };
        }
    };
</script>
