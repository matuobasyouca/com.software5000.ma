import Vue from 'vue';
import ModalVue from './modal.vue';
import merge from '../../src/utils/merge';

const ModalConstructor = Vue.extend(ModalVue);

const defaults = {
    text : null,
    fullscreen : true,
    body : false,
    lock : false,
    customClass : ''
};

let fullscreenModal;

ModalConstructor.prototype.originalPosition = '';
ModalConstructor.prototype.originalOverflow = '';

ModalConstructor.prototype.close = function (){
    if (this.fullscreen && this.originalOverflow !== 'hidden') {
        document.body.style.overflow = this.originalOverflow;
    }
    if (this.fullscreen || this.body) {
        document.body.style.position = this.originalPosition;
    } else {
        this.target.style.position = this.originalPosition;
    }
    if (this.fullscreen) {
        fullscreenModal = undefined;
    }
    this.$on('after-leave', _ =>{
        this.$el &&
        this.$el.parentNode &&
        this.$el.parentNode.removeChild(this.$el);
        this.$destroy();
    });
    this.visible = false;
};
const addStyle = (options, parent, instance) =>{
    let maskStyle = {};

    instance.originalPosition = parent.style.position;
    instance.originalOverflow = parent.style.overflow;
    if (options.body) {
        ['top', 'left'].forEach(property =>{
            let scroll = property === 'top' ? 'scrollTop' : 'scrollLeft';
            maskStyle[property] = parent.getBoundingClientRect()[property] +
                document.body[scroll] +
                document.documentElement[scroll] +
                'px';
        });
    }
    Object.keys(maskStyle).forEach(property =>{
        instance.$el.style[property] = maskStyle[property];
    });
};

const Modal = (options = {}) =>{
    if (Vue.prototype.$isServer) return;
    options = merge({}, defaults, options);
    if (typeof options.target === 'string') {
        options.target = document.querySelector(options.target);
    }
    options.target = options.target || document.body;
    if (options.target !== document.body) {
        options.fullscreen = false;
    } else {
        options.body = true;
    }
    if (options.fullscreen && fullscreenModal) {
        return fullscreenModal;
    }

    let parent = options.target;
    let instance = new ModalConstructor({
        el : document.createElement('div'),
        data : options
    });

    addStyle(options, parent, instance);

    if (instance.originalPosition !== 'absolute') {
        parent.style.position = 'relative';
    }
    if (options.fullscreen && options.lock) {
        parent.style.overflow = 'hidden';
    }
    parent.appendChild(instance.$el);
    Vue.nextTick(() =>{
        instance.visible = true;
    });
    if (options.fullscreen) {
        fullscreenModal = instance;
    }
    return instance;
};

export default Modal;