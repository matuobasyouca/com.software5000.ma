import ZsBreadcrumb from './src/breadcrumb';

/* istanbul ignore next */
ZsBreadcrumb.install = function(Vue) {
    Vue.component(ZsBreadcrumb.name, ZsBreadcrumb);
};

export default ZsBreadcrumb;
