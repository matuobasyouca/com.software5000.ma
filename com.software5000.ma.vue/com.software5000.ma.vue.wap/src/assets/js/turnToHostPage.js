/**
 * Created by janzho on 2017/7/4.
 */
import { turnToNextPage, isEmpty, isPhone, isPC } from './utils';
const host = window.location.host;
const hostArr = host.split('.');
const pathnameArr = window.location.pathname.split('/');
const search = window.location.search;
const isLocal = hostArr[0] == '192' || hostArr[0].indexOf('localhost') > -1;
const appointUrl = {};

appointUrl.crm = isLocal ? `http://localhost:81${isPhone() ? '81' : '80'}` : `http://crm.${hostArr[1]}.com`;
appointUrl.ma = isLocal ? `http://localhost:82${isPhone() ? '81' : '80'}` : `http://ma.${hostArr[1]}.com`;
appointUrl.emkt = isLocal ? `http://localhost:83${isPhone() ? '80' : '80'}` : `http://emkt.${hostArr[1]}.com`;

/**
 * 跨域名链接跳转
 * @param url
 * @param hostName
 * @param params
 */
export function turnToHostPage(url, hostName, params) {
    let fullUrl = isEmpty(hostName) ? url : `${appointUrl[hostName]}${url}`;

    turnToNextPage(fullUrl, params);
}

/**
 * 获取当前访问的项目域名
 */
export { appointUrl };

/**
 * wap和web端自适应跳转
 * @param fn
 * @returns {boolean}
 */
export function filterAgent(fn) {
    let project = '';

    if (host.indexOf('localhost') > -1) {
        project = { 81: 'crm', 82: 'ma', 83: 'emkt' }[host.split(':')[1].slice(0, 2)];
    } else {
        project = hostArr[0];
    }
    if (isPC() && pathnameArr[1] === 'wap') {
        pathnameArr[1] = 'web';
    } else if (isPhone() && pathnameArr[1] === 'web') {
        pathnameArr[1] = 'wap';
    } else {
        fn && fn();
        return false;
    }
    const reUrl = `${pathnameArr.join('/')}${search}`;

    turnToHostPage(reUrl, project);
}