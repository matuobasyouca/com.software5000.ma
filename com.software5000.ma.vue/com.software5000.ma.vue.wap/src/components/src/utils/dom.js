import Vue from 'vue';

const isServer = Vue.prototype.$isServer;
const SPECIAL_CHARS_REGEXP = /([\:\-\_]+(.))/g;
const MOZ_HACK_REGEXP = /^moz([A-Z])/;
const ieVersion = isServer ? 0 : Number(document.documentMode);

/* 去掉首尾空格 */
const trim = function (string){
    return (string || '').replace(/^[\s\uFEFF]+|[\s\uFEFF]+$/g, '');
};
/* 将带有"-","_",":" 的字符串转成驼峰式字符串 */
const camelCase = function (name){
    return name.replace(SPECIAL_CHARS_REGEXP, function (_, separator, letter, offset){
        return offset ? letter.toUpperCase() : letter;
    }).replace(MOZ_HACK_REGEXP, 'Moz$1');
};

/* 封装on事件监听方法 */
export const on = (function (){
    if (!isServer && document.addEventListener) {
        return function (element, event, handler){
            if (element && event && handler) {
                element.addEventListener(event, handler, false);
            }
        };
    } else {
        return function (element, event, handler){
            if (element && event && handler) {
                element.attachEvent('on' + event, handler);
            }
        };
    }
})();

/* 封装off事件解绑方法 */
export const off = (function (){
    if (!isServer && document.removeEventListener) {
        return function (element, event, handler){
            if (element && event) {
                element.removeEventListener(event, handler, false);
            }
        };
    } else {
        return function (element, event, handler){
            if (element && event) {
                element.detachEvent('on' + event, handler);
            }
        };
    }
})();

/* 封装once绑定方法，只执行一次事件后解绑 */
export const once = function (el, event, fn){
    var listener = function (){
        if (fn) {
            fn.apply(this, arguments);
        }
        off(el, event, listener);
    };
    on(el, event, listener);
};

/* 封装hasClass方法 */
export function hasClass(el, cls){
    if (!el || !cls) return false;
    if (cls.indexOf(' ') !== -1) throw new Error('className should not contain space.');
    if (el.classList) {
        return el.classList.contains(cls);
    } else {
        return (' ' + el.className + ' ').indexOf(' ' + cls + ' ') > -1;
    }
};

/* 封装addClass方法 */
export function addClass(el, cls){
    if (!el) return;
    var curClass = el.className;
    var classes = (cls || '').split(' ');

    for (var i = 0, j = classes.length; i < j; i++) {
        var clsName = classes[i];
        if (!clsName) continue;

        if (el.classList) {
            el.classList.add(clsName);
        } else {
            if (!hasClass(el, clsName)) {
                curClass += ' ' + clsName;
            }
        }
    }
    if (!el.classList) {
        el.className = curClass;
    }
};

/* 封装removeClass方法*/
export function removeClass(el, cls){
    if (!el || !cls) return;
    var classes = cls.split(' ');
    var curClass = ' ' + el.className + ' ';

    for (var i = 0, j = classes.length; i < j; i++) {
        var clsName = classes[i];
        if (!clsName) continue;

        if (el.classList) {
            el.classList.remove(clsName);
        } else {
            if (hasClass(el, clsName)) {
                curClass = curClass.replace(' ' + clsName + ' ', ' ');
            }
        }
    }
    if (!el.classList) {
        el.className = trim(curClass);
    }
};

/* 封装获取样式方法*/
export const getStyle = ieVersion < 9 ? function (element, styleName){
        if (isServer) return;
        if (!element || !styleName) return null;
        styleName = camelCase(styleName);
        if (styleName === 'float') {
            styleName = 'styleFloat';
        }
        try {
            switch (styleName) {
                case 'opacity':
                    try {
                        return element.filters.item('alpha').opacity / 100;
                    } catch (e) {
                        return 1.0;
                    }
                default:
                    return (element.style[styleName] || element.currentStyle ? element.currentStyle[styleName] : null);
            }
        } catch (e) {
            return element.style[styleName];
        }
    } : function (element, styleName){
        if (isServer) return;
        if (!element || !styleName) return null;
        styleName = camelCase(styleName);
        if (styleName === 'float') {
            styleName = 'cssFloat';
        }
        try {
            var computed = document.defaultView.getComputedStyle(element, '');
            return element.style[styleName] || computed ? computed[styleName] : null;
        } catch (e) {
            return element.style[styleName];
        }
    };

/* 设置样式 */
export function setStyle(element, styleName, value){
    if (!element || !styleName) return;

    if (typeof styleName === 'object') {
        for (var prop in styleName) {
            if (styleName.hasOwnProperty(prop)) {
                setStyle(element, prop, styleName[prop]);
            }
        }
    } else {
        styleName = camelCase(styleName);
        if (styleName === 'opacity' && ieVersion < 9) {
            element.style.filter = isNaN(value) ? '' : 'alpha(opacity=' + value * 100 + ')';
        } else {
            element.style[styleName] = value;
        }
    }
};