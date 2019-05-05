const defaults = {
    title : undefined,
    message : '',
    type : '',
    showClose : true,
    modalFade : true,
    lockScroll : true,
    closeOnClickModal : true,
    closeOnPressEscape : true,
    showConfirmButton : true,
    showCancelButton : false,
    confirmButtonPosition : 'right',
    confirmButtonHighlight : false,
    cancelButtonHighlight : false,
    confirmButtonText : '确定',
    cancelButtonText : '取消',
    confirmButtonClass : '',
    cancelButtonClass : '',
    customClass : '',
    beforeClose : null
};

import Vue from 'vue';
import msgboxVue from './message-box.vue';
import merge from '../../src/utils/merge';

const MessageBoxConstructor = Vue.extend(msgboxVue);

let currentMsg, instance;
let msgQueue = [];

const defaultCallback = action =>{
    if (currentMsg) {
        let callback = currentMsg.callback;
        if (typeof callback === 'function') {
            callback(action);
        }
        if (currentMsg.resolve) {
            let $type = currentMsg.options.$type;
            if ($type === 'confirm') {
                if (action === 'confirm') {
                    currentMsg.resolve(action);
                } else if (action === 'cancel' && currentMsg.reject) {
                    currentMsg.reject(action);
                }
            } else {
                currentMsg.resolve(action);
            }
        }
    }
};

const initInstance = () =>{
    instance = new MessageBoxConstructor({
        el : document.createElement('div')
    });

    instance.callback = defaultCallback;
};

const showNextMsg = () =>{
    if (!instance) {
        initInstance();
    }
    instance.action = '';

    if (!instance.value) {
        if (msgQueue.length > 0) {
            currentMsg = msgQueue.shift();

            let options = currentMsg.options;
            for (let prop in options) {
                if (options.hasOwnProperty(prop)) {
                    instance[prop] = options[prop];
                }
            }
            if (options.callback === undefined) {
                instance.callback = defaultCallback;
            }

            let oldCb = instance.callback;
            instance.callback = action =>{
                oldCb(action);
                showNextMsg();
            };
            ['modal', 'showClose', 'closeOnClickModal', 'closeOnPressEscape'].forEach(prop =>{
                if (instance[prop] === undefined) {
                    instance[prop] = true;
                }
            });
            document.body.appendChild(instance.$el);

            Vue.nextTick(() =>{
                instance.value = true;
            });
        }
    }
};

const MessageBox = function (options, callback){
    if (Vue.prototype.$isServer) return;

    if (options.callback && !callback) {
        callback = options.callback;
    }

    if (typeof Promise !== 'undefined') {
        return new Promise((resolve, reject) =>{
            msgQueue.push({
                options : merge({}, defaults, options),
                callback : callback,
                resolve : resolve,
                reject : reject
            });

            showNextMsg();
        });
    } else {
        msgQueue.push({
            options : merge({}, defaults, options),
            callback : callback
        });

        showNextMsg();
    }
};

MessageBox.alert = (options) =>{
    return MessageBox(merge({
        $type : 'alert',
        closeOnPressEscape : false,
        closeOnClickModal : false
    }, options));
};

MessageBox.confirm = (options) =>{
    return MessageBox(merge({
        $type : 'confirm',
        showCancelButton : true
    }, options));
};

MessageBox.close = () =>{
    instance.value = false;
    msgQueue = [];
    currentMsg = null;
};

export default MessageBox;
