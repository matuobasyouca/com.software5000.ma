import maAppraise from './src/appraise';

maAppraise.install = function(Vue) {
    Vue.component(maAppraise.name, maAppraise);
};

export default maAppraise;
