import ZsTable from './src/table';

ZsTable.install = function(Vue) {
    Vue.component(ZsTable.name, ZsTable);
};

export default ZsTable;
