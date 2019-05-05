/**
 * 合并多个对象
 * @param target 目标对象
 * @param 后面的参数为对象形式，会合并到target中
 * @returns {*} 返回target对象
 */
export default function (target){
    for (let i = 1, j = arguments.length; i < j; i++) {
        let source = arguments[i] || {};
        for (let prop in source) {
            if (source.hasOwnProperty(prop)) {
                let value = source[prop];
                if (value !== undefined) {
                    target[prop] = value;
                }
            }
        }
    }

    return target;
};
