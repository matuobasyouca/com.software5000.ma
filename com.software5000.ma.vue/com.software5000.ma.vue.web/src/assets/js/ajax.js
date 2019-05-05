/**
 * Created by JANZ on 2017/3/25.
 */
import Vue from 'vue';
import joggle from './joggle';
import VueResource from 'vue-resource';

Vue.use(VueResource);
Vue.prototype.$joggle = joggle;
/**
 * ajax封装
 * @param url 接口地址
 * @param sendData 参数
 * @param loading 可以设置的值为 布尔值 true,设置true后，默认使用封装中的loading,loading的关闭不能控制。
 *                  可以传入自定义的loading对象，以便控制loading的关闭时机
 * @param fn 成功获取数据后的执行函数
 */
Vue.prototype.$ajax = (url, sendData, loading, fn, fnErr) =>{

    if (typeof loading === 'boolean' && loading) {
        loading = Vue.prototype.$loading();
    }

    Vue.http.post(url, sendData).then(res =>{

        fn && fn(res.body, loading);

    }, error =>{
        if (loading) loading.close();
        if (JSON.parse(error.body).code === 'ZS004001') {
            Vue.prototype.$message({
                type: 'error',
                duration: 1200,
                message: '权限不足'
            });
            setTimeout(() =>{
                window.location = `/web/${window.location.pathname.split('/')[2]}/open/login.html`;
            }, 1200);
        } else {
            Vue.prototype.$message({
                type: 'error',
                duration: 1200,
                message: JSON.parse(error.body).msg
            });
            fnErr && fnErr(JSON.parse(error.body));
        }

    });
};