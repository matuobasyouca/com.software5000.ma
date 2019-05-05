/**
 * Created by JANZ on 2017/2/27.
 */

import Loading from './loading/index';
import Message from './message/index';
import MessageBox from './message_box/index';

import ZsIcon from './icon/index';
import ZsDialog from './dialog/index';
import ZsButton from './button/index';
import ZsInput from './input/index';
import ZsScrollbar from './scrollbar/index';
import ZsSelectMenu from './select/src/select-dropdown.vue';
import ZsOptionGroup from './select/src/option-group.vue';
import ZsOption from './select/src/option.vue';
import ZsSelect from './select/index';

import ZsForm from './form/index';
import ZsFormItem from './form-item/index';

import ZsPopover from './popover/index';
import ZsPagination from './pagination/index';

import ZsTable from './table/index';

import ZsCheckbox from './checkbox/index';
import ZsCheckboxGroup from './checkbox-group/index';
import ZsRadio from './radio/index';
import ZsRadioGroup from './radio-group/index';
import ZsTag from './tag/index';
import ZsBreadcrumb from './breadcrumb';
import ZsBreadcrumbItem from './breadcrumb-item';

import DatePicker from './date-picker';


const components = [
    ZsIcon,
    ZsDialog,
    ZsButton,
    ZsInput,
    ZsScrollbar,
    ZsSelectMenu,
    ZsOptionGroup,
    ZsOption,
    ZsSelect,
    ZsForm,
    ZsFormItem,
    ZsPopover,
    ZsPagination,
    ZsTable,
    ZsCheckbox,
    ZsCheckboxGroup,
    ZsRadio,
    ZsRadioGroup,
    ZsTag,
    ZsBreadcrumb,
    ZsBreadcrumbItem,
    DatePicker
]

const ZsUI = function (Vue, opts = {}){

    if (ZsUI.installed) return;

    components.map(component =>{
        Vue.component(component.name, component);
    });

    Vue.prototype.$loading = Loading.service;
    Vue.prototype.$message = Message.service;
    Vue.prototype.$alert = MessageBox.service.alert;
    Vue.prototype.$confirm = MessageBox.service.confirm;
}

if (typeof window !== 'undefined' && window.Vue) {
    install(window.Vue);
}
export default ZsUI;
export {
    ZsIcon,
    Loading,
    Message,
    MessageBox,
    ZsDialog,
    ZsButton,
    ZsInput,
    ZsScrollbar,
    ZsSelectMenu,
    ZsOptionGroup,
    ZsOption,
    ZsSelect,
    ZsForm,
    ZsFormItem,
    ZsPopover,
    ZsPagination,
    ZsTable,
    ZsCheckbox,
    ZsCheckboxGroup,
    ZsRadio,
    ZsRadioGroup,
    ZsTag,
    ZsBreadcrumb,
    ZsBreadcrumbItem,
    DatePicker
};
