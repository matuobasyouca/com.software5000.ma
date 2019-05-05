import Vue from 'vue';
import App from './ue.vue';
// import App from './app.vue';
// import App from './test.vue';
import '../../assets/js/ajax';
import '../../assets/css/src/style.css';
import ZsUI from '../../components/index';
Vue.use(ZsUI);
new Vue({
    el: '#app',
    render: h => h(App)
});
