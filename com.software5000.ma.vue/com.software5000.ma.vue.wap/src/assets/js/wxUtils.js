/**
 * Created by janzho on 2017/7/4.
 */
import { Vue } from './ajax';
import { getDataFromParam, isEmpty, turnToNextPage } from './utils';

const REDIRET_URL = Vue.prototype.$joggle.mzscp.selectWxCode;
const OPENID_URL = Vue.prototype.$joggle.mzscp.selectWxOpenId;

let path = `${window.location.host}${window.location.pathname}`;

function clearCode(params) {
    let _params = {};

    for (let key in params) {
        if (key !== 'code') {
            _params[key] = params[key];
        }
    }
    return _params;
}
export function selectWxCode(fn) {
    let params = getDataFromParam() || {};
    let wxCode = params.code;

    if (isEmpty(wxCode)) {
        turnToNextPage(`${REDIRET_URL}?toURI=${path}`, params);
    } else {
        fn && fn(wxCode);
    }
}
export function selectWxCodeAgain() {
    let params = getDataFromParam();
    let _params = clearCode(params);

    turnToNextPage(`${REDIRET_URL}?toURI=${path}`, _params);
}
export function selectOpenId(code, fn) {
    Vue.prototype.$ajax(
        OPENID_URL,
        { wxCode: code },
        true,
        (data, loading) => {
            if (data.code === 'ZS011000') {
                loading.close();
                if (isEmpty(data.data)) {
                    selectWxCodeAgain();
                } else {
                    fn && fn(data.data);
                }
            } else if (data.code === 'ZS012042') {
                selectWxCodeAgain();
            } else {
                loading.close();
                Vue.prototype.$message({
                    type: 'error',
                    message: data.msg,
                    duration: 1200
                });
            }
        }
    );
}