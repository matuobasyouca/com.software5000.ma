import service from './src/main.js';
export default {
    install(Vue) {
        Vue.prototype.$alert = service.alert;
        Vue.prototype.$confirm = service.confirm;
    },
    service
}