import service from './src/main';

export default {
    install(Vue) {
        Vue.prototype.$loading = service;
    },
    service
}