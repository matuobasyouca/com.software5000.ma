import Vue from 'vue';
import loadingVue from './loading.vue';
import merge from '../../src/utils/merge';

const LoadingConstructor = Vue.extend(loadingVue);

const defaults = {
    message : null,
    fullscreen : true,
    body : false,
    lock : true
};

let fullscreenLoading;

LoadingConstructor.prototype.originalPosition = '';
LoadingConstructor.prototype.originalOverflow = '';

LoadingConstructor.prototype.close = function (){
    if (this.fullscreen && this.originalOverflow !== 'hidden') {
        document.body.style.overflow = this.originalOverflow;
    }
    if (this.fullscreen || this.body) {
        document.body.style.position = this.originalPosition;
    } else {
        this.target.style.position = this.originalPosition;
    }
    if (this.fullscreen) {
        fullscreenLoading = undefined;
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

const Loading = (options = {}) =>{
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
    if (options.fullscreen && fullscreenLoading) {
        return fullscreenLoading;
    }

    let parent = options.target;
    let instance = new LoadingConstructor({
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
        fullscreenLoading = instance;
    }
    return instance;
};

export default Loading;