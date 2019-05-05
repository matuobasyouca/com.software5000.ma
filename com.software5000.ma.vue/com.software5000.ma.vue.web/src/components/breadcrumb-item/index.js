import ZsBreadcrumbItem from './src/breadcrumb-item';

/* istanbul ignore next */
ZsBreadcrumbItem.install = function (Vue){
    Vue.component(ZsBreadcrumbItem.name, ZsBreadcrumbItem);
};

export default ZsBreadcrumbItem;
