import service from './src/main.js';
export default {
    install(Vue) {
        Vue.prototype.$message = service;
    },
    service
}
