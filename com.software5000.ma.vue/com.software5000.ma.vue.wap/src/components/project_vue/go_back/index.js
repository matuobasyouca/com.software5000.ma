import goBack from './src/go-back';

goBack.install = function(Vue) {
    Vue.component(goBack.name, goBack);
};

export default goBack;
