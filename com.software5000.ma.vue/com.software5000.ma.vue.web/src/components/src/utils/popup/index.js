import Vue from 'vue';
import merge from '../merge';
import PopupManager from '../popup/popup-manager';
import getScrollBarWidth from '../scrollbar-width';

let idSeed = 1;
let scrollBarWidth;

const getDOM = function (dom){
    if (dom.nodeType === 3) {
        dom = dom.nextElementSibling || dom.nextSibling;
        getDOM(dom);
    }
    return dom;
};

export default {
    props : {
        value : {
            type : Boolean,
            default : false
        },
        zIndex : {},
        modal : {
            type : Boolean,
            default : false
        },
        modalFade : {
            type : Boolean,
            default : true
        },
        modalClass : {},
        modalAppendToBody : {
            type : Boolean,
            default : false
        },
        lockScroll : {
            type : Boolean,
            default : true
        },
        closeOnPressEscape : {
            type : Boolean,
            default : false
        },
        closeOnClickModal : {
            type : Boolean,
            default : false
        }
    },

    beforeMount() {
        this._popupId = 'popup-' + idSeed++;
        PopupManager.register(this._popupId, this);
    },

    beforeDestroy() {
        PopupManager.deregister(this._popupId);
        PopupManager.closeModal(this._popupId);
        if (this.modal && this.bodyOverflow !== null && this.bodyOverflow !== 'hidden') {
            document.body.style.overflow = this.bodyOverflow;
            document.body.style.paddingRight = this.bodyPaddingRight;
        }
        this.bodyOverflow = null;
        this.bodyPaddingRight = null;
    },

    data() {
        return {
            opened : false,
            bodyOverflow : null,
            bodyPaddingRight : null,
            rendered : false
        };
    },

    watch : {
        value(val) {
            if (val) {
                if (this._opening) return;
                if (!this.rendered) {
                    this.rendered = true;
                    Vue.nextTick(() =>{
                        this.open();
                    });
                } else {
                    this.open();
                }
            } else {
                this.close();
            }
        }
    },

    methods : {
        open(options) {
            if (!this.rendered) {
                this.rendered = true;
                this.$emit('input', true);
            }
            const props = merge({}, this.$props || this, options);
            this.doOpen(props);
        },

        doOpen(props) {
            if (this.$isServer) return;
            if (this.opened) return;

            this._opening = true;
            this.$emit('input', true);

            const dom = getDOM(this.$el);

            const modal = props.modal;

            const zIndex = props.zIndex;
            if (zIndex) {
                PopupManager.zIndex = zIndex;
            }

            if (modal) {
                if (this._closing) {
                    PopupManager.closeModal(this._popupId);
                    this._closing = false;
                }
                PopupManager.openModal(this._popupId, PopupManager.nextZIndex(), this.modalAppendToBody ? undefined : dom, props.modalClass, props.modalFade);
                if (props.lockScroll) {
                    if (!this.bodyOverflow) {
                        this.bodyPaddingRight = document.body.style.paddingRight;
                        this.bodyOverflow = document.body.style.overflow;
                    }
                    scrollBarWidth = getScrollBarWidth();
                    let bodyHasOverflow = document.documentElement.clientHeight < document.body.scrollHeight;
                    if (scrollBarWidth > 0 && bodyHasOverflow) {
                        document.body.style.paddingRight = scrollBarWidth + 'px';
                    }
                    document.body.style.overflow = 'hidden';
                }
            }

            if (getComputedStyle(dom).position === 'static') {
                dom.style.position = 'absolute';
            }

            dom.style.zIndex = PopupManager.nextZIndex();
            this.opened = true;
            this.doAfterOpen();
        },

        doAfterOpen() {
            this._opening = false;
        },

        close() {
            this.doClose();
        },

        doClose() {
            this.$emit('input', false);
            this._closing = true;

            if (this.lockScroll) {
                setTimeout(() =>{
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
        },

        doAfterClose() {
            PopupManager.closeModal(this._popupId);
            this._closing = false;
        }
    }
};
export {
    PopupManager
};
