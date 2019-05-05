import ZsPagination from './src/pagination';

ZsPagination.install = function(Vue) {
    Vue.component(ZsPagination.name, ZsPagination);
};

export default ZsPagination;