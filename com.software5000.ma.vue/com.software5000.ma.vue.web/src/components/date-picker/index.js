import DatePicker from './picker.vue';
DatePicker.install = function(Vue) {
    Vue.component(DatePicker.name, DatePicker);
};
export default DatePicker;