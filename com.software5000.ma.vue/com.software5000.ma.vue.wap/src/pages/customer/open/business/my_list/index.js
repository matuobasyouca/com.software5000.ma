import Vue from 'vue';
import App from './app';
import '../../../../../assets/css/src/style.css';
import ZsUI from '../../../../../components/index';
import '../../../../../assets/js/ajax';
Vue.use(ZsUI);
new Vue({
    el: '#app',
    render: h => h(App)
});
