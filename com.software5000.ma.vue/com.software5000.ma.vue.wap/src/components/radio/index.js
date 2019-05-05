import ZsRadio from './src/radio';

/* istanbul ignore next */
ZsRadio.install = function(Vue) {
  Vue.component(ZsRadio.name, ZsRadio);
};

export default ZsRadio;
