import ZsScrollbar from './src/scrollbar';

/* istanbul ignore next */
ZsScrollbar.install = function(Vue) {
  Vue.component(ZsScrollbar.name, ZsScrollbar);
};

export default ZsScrollbar;
