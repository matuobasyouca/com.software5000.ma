import ZsTag from './src/tag';

ZsTag.install = function(Vue) {
  Vue.component(ZsTag.name, ZsTag);
};

export default ZsTag;
