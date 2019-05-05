import ZsDialog from './src/component';

/* istanbul ignore next */
ZsDialog.install = function(Vue) {
  Vue.component(ZsDialog.name, ZsDialog);
};

export default ZsDialog;
