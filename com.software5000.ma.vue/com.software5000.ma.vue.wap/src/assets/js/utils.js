/**
 * Created by janzho on 2017/3/28.
 */
import base64 from './base64.min';

// 判断是否为空 {}，[],'','undefined','null',undefined,null
export function isEmpty(v) {
    if (v && typeof v === 'object') {
        return Object.keys(v).length === 0;
    } else if (Array.isArray(v)) {
        return v.length === 0;
    }
    return v === '' || v === 'undefined' || v === undefined || v === null || v === 'null';
}

function paramToObject(str) {
    let obj = {};

    if (str.substring(0, 1) === '?') {
        str = str.slice(1);
    }

    let strArr = str.split('&');
    let param = '';

    for (let i = 0; i < strArr.length; i++) {
        param = strArr[i].split('=');
        obj[param[0]] = param[1] || null;
    }

    return obj;
}

function objectToParam(obj) {
    let str = '';

    for (let key in obj) {
        str += `${key}=${obj[key]}&`;
    }
    str = str.slice(0, -1);
    return str;
}

//获取地址栏参数，转化成json
export function getDataFromParam(key) {
    let param = location.search;

    if (!param) {
        return null;
    }
    param = paramToObject(decodeURIComponent(param.slice(0)));

    return isEmpty(key) ? param : param[key];
}
//路由地址参数，转成json
export function getDataFromRouter(key) {
    let param = location.hash;
    let begin = param.indexOf('?');

    if (!param) {
        return null;
    }
    param = paramToObject(decodeURIComponent(param.slice(begin + 1)));
    return param[key];
}
//带参数跳转到目标页面
export function turnToNextPage(url, opt, newWin) {
    let u = isEmpty(opt) ? url : `${url}?${objectToParam(opt)}`;

    if (newWin) {
        window.open(u);
    } else {
        window.location.href = u;
    }
}

//根据逗号拆分字符串为数组
export function sepByComma(str, emptyTxt) {
    let emptyArray = isEmpty(emptyTxt) ? [] : [emptyTxt];

    return isEmpty(str) ? emptyArray : str.split(',');
}
//将数组根据按逗号组合
export function joinByComma(arr) {
    if (!Array.isArray(arr)) return arr;
    return arr.length === 0 ? '' : arr.join(',');
}

//判断是否为undefined并设置值,不设值时判断是否为空
export function setEmpty(data, val) {
    if (isEmpty(data)) {
        return isEmpty(val) ? false : val;
    }
    return data;
}

export const DateUtils = {
    now() {
        return new Date();
    },
    dateFormat (date, fmt) {
        if (!date) return '';
        let o = {
            'M+': date.getMonth() + 1, // 月份
            'd+': date.getDate(), // 日
            'h+': date.getHours(), // 小时
            'm+': date.getMinutes(), // 分
            's+': date.getSeconds(), // 秒
            'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
            S: date.getMilliseconds() // 毫秒
        };

        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (let k in o) {
            if (new RegExp('(' + k + ')').test(fmt)) {
                fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));
            }
        }
        return fmt;
    },
    //计算 加 i 天 或者减 i 天
    dayCalc(i){
        let date = this.now();

        date.setDate(date.getDate() + i);
        return date;
    },
    //将 yyyy-MM-dd hh:mm:ss 格式转成正确的Date对象;
    dateToRegular (val) {
        let v = val.replace(/\D+/g, ',').split(',');

        v[1] = parseInt(v[1]) - 1;
        for (let i = 0; i < v.length; i++) {
            v[i] = parseInt(v[i]);
        }
        v = v.join(',');
        return eval('(new Date(' + v + '))');
    },
    //比较两个时间,如果开始时间大于结束时间,则交换两个时间
    swapTime(startTime, endTime){
        if (startTime && endTime) {
            const start = DateUtils.dateToRegular(startTime).getTime();
            const end = DateUtils.dateToRegular(endTime).getTime();

            if (start > end) {
                startTime = [endTime, endTime = startTime][0];
            }
        }
        return [startTime, endTime];
    }
};

// 去首尾空格
export function strim(str) {
    if (str === undefined || str === null) return '';
    return str.replace(/(^\s*)|(\s*$)/g, '');
}

//验证规则
const Regs = {
    isChinese: /^[\u4e00-\u9fa5]+$/,
    isEmail: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
    isURL: /^[a-zA-z]+:\/\/[^\s]+$/,
    isTel: /^((\d{3,4})-)?(\d{3,8})(-(\d{3,}))?$/,
    isMobile: /^(?:13\d|14\d|15\d|16\d|17\d|18\d)\d{5}(\d{3}|\*{3})$/,
    isLegal: /^[a-zA-Z0-9]{6,20}$/, //(允许6-20字节，允许字母数字下划线)
    isIP: /^\d+\.\d+\.\d+\.\d+$/,
    isEmpty: /^\s*$/,
    isCarNum: /^[a-zA-Z0-9]{4}[a-zA-Z0-9学警港澳]$/,
    isFullCarNum: /^[\u4e00-\u9fa5][a-zA-Z][a-zA-Z0-9]{4}[a-zA-Z0-9学警港澳]$/,
    isName: /^[a-zA-Z0-9\u4e00-\u9fa5]*$/ //姓名和昵称格式
};

//按规则验证
String.prototype.isInReg = function (reg) {
    let _i = reg;
    let _s = this;

    return _i.test(_s);
};
//移动电话验证
export function isMobile(mobile) {
    return mobile.isInReg(Regs.isMobile);
}
//固定电话验证
export function isTel(tel) {
    return tel.isInReg(Regs.isTel);
}
//验证完整车牌
export function isCarNum(carNum) {
    return carNum.isInReg(Regs.isFullCarNum);
}
//验证部分车牌
export function isSimpleCarNum(carNum) {
    return carNum.isInReg(Regs.isCarNum);
}
//汉字验证
export function isChinese(val) {
    return val.isInReg(Regs.isChinese);
}
//姓名验证
export function isName(val) {
    return val.isInReg(Regs.isName);
}

//图片预加载
export function imgPreLoad(url, fn) {
    let img = new Image();

    img.src = url;
    img.onload = function () {
        fn && fn(img);
    };
}

//数组分组
export function groupArray(arr, num) {
    num = num * 1 || 1;
    let ret = [];

    arr.forEach((item, i) => {
        if (i % num === 0) {
            ret.push([]);
        }
        ret[ret.length - 1].push(item);
    });
    return ret;
}

//优化JSON.parse方法
export function parseJSON(str) {
    return typeof str === 'string' ? JSON.parse(str) : str;
}

//浅复制对象或者数组
export function doCopy(data) {
    const t = Array.isArray(data) ? 'array' : typeof data;
    let o = data;

    if (t === 'array') {
        o = [];
        for (let i = 0; i < data.length; i++) {
            o.push(data[i]);
        }
    } else if (t === 'object') {
        o = {};
        for (let i in data) {
            o[i] = data[i];
        }
    }
    return o;
}

//hashTag
export const Hash = {
    //向地址栏中添加base64编码过的hash值，值为json格式
    add(obj){
        window.location.hash = base64.encode(JSON.stringify(obj));
    },
    //从地址栏中获取hash，并反base64，返回为json
    parse(){
        return window.location.hash ? JSON.parse(base64.decode(window.location.hash)) : {};
    }
};

//Cookie
export const Cookie = {
    get(name){
        const arr = document.cookie.split(';');

        for (let i = 0; i < arr.length; i++) {
            let arr2 = arr[i].split('=');

            if (arr2[0] === name) {
                return arr2[1];
            }
        }
        return '';
    },
    set(name, val, min){
        const oTime = new Date();

        oTime.setTime(oTime.getTime() + 60 * min * 1000);
        document.cookie = name + '=' + val + ';expires=' + oTime;
    }
};

//判断手机是否为安卓系统
export function isAndroid() {
    let u = navigator.userAgent;

    return u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;
}

//判断移动端还是pc端
export function selectUserAgent() {
    const userAgentInfo = navigator.userAgent.toLowerCase();
    const Agents = ['android', 'iphone', 'symbianos', 'windows phone', 'ipad', 'ipod'];
    let flag = 'pc';

    for (let v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = 'phone';
            break;
        }
    }
    return flag;
}
export function isPC() {
    return selectUserAgent() === 'pc';
}
export function isPhone() {
    return selectUserAgent() === 'phone';
}
//金钱的格式化
export function moneyFormat(money) {
    if (isEmpty(money))return 0;
    let num = (money / 100).toString().split('.');
    let arr = [];
    let len = num[0].length;
    let lastIndex = 0;

    while (len > 0) {
        lastIndex = len;
        len -= 3;
        arr.unshift(num[0].substring(len, lastIndex));
    }
    return num[1] ? arr.join(',') + '.' + num[1] : arr.join(',') + '.00';
}

