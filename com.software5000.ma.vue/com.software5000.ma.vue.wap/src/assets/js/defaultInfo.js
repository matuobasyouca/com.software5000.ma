/**
 * Created by janzho on 2017/7/27.
 */
import { Vue } from './ajax';
import { isEmpty } from './utils';

export function insertDefaultInfo(fn) {
    Vue.prototype.$ajax(
        Vue.prototype.$joggle.business.selectBusinessUser,
        {},
        false,
        (data) => {
            if (data.code === 'ZS011000') {
                window.localStorage.defaultInfo = JSON.stringify(data.data);
                fn && fn(data.data);
            } else {
                fn && fn({});
            }
        }
    );
}
export function defaultInfo(fn) {
    let info = window.localStorage.defaultInfo;
    
    if (isEmpty(info)) {
        insertDefaultInfo((data) => {
            fn && fn(data);
        });
    } else {
        info = JSON.parse(info);
        fn && fn(info);
    }
}
export function deleteDefaultInfo() {
    window.localStorage.defaultInfo = '';
}