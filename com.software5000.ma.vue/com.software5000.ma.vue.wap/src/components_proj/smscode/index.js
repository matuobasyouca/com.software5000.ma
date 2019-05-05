import smsCode from './src/smsCode';
import "../../assets/js/ajax";

smsCode.install = function(Vue) {
  Vue.component(smsCode.name, smsCode);
};

export default smsCode;
