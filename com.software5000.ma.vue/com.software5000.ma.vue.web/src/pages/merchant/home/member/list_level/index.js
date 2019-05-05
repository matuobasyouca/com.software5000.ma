import Vue from 'vue';
import App from './app.vue';
import '../../../../../assets/css/src/style.css';
import '../../../../../assets/js/ajax';
import ZsUI from '../../../../../components/index';

Vue.use(ZsUI);
new Vue({
    el: '#app',
    render: h => h(App)
});
