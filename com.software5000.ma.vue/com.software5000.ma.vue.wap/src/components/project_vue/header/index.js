import maHeader from './src/header';

maHeader.install = function(Vue) {
  Vue.component(maHeader.name, maHeader);
};

export default maHeader;
