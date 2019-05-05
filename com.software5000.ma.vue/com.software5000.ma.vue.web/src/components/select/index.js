import ZsSelect from './src/select';

/* istanbul ignore next */
ZsSelect.install = function(Vue) {
  Vue.component(ZsSelect.name, ZsSelect);
};

export default ZsSelect;
