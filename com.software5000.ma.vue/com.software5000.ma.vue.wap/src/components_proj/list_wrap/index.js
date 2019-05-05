import listWrap from './src/list_wrap.vue';

listWrap.install = function(Vue) {
  Vue.component(listWrap.name, listWrap);
};

export default listWrap;
