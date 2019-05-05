import listBase from './src/list-base';

listBase.install = function(Vue) {
  Vue.component(listBase.name, listBase);
};

export default listBase;
