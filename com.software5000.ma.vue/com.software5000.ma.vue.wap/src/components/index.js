/**
 * Created by JANZ on 2017/2/27.
 */

import Loading from './loading/index';
import Modal from './modal/index';
import Message from './message/index';
import MessageBox from './message_box/index';

import ZsIcon from './icon/index';
import ZsDialog from './dialog/index';
import ZsButton from './button/index';
import ZsInput from './input/index';
import ZsInputNumber from './input_number/index';
import ZsSwitch from './switch/index';
import ZsTab from './tab/index';
import ZsRadio from './radio/index';
import ZsRadioGroup from './radio-group/index';

import zsSlidePage from './slide-page/index';


const components = [
    ZsDialog,
    ZsButton,
    ZsInput,
    ZsInputNumber,
    ZsSwitch,
    ZsIcon,
    ZsTab,
    ZsRadio,
    ZsRadioGroup,
    zsSlidePage
];

const ZsUI = function (Vue, opts = {}) {

    if (ZsUI.installed) return;

    components.map(component => {
        Vue.component(component.name, component);
    });

    Vue.prototype.$loading = Loading.service;
    Vue.prototype.$message = Message.service;
    Vue.prototype.$modal = Modal.service;
    Vue.prototype.$alert = MessageBox.service.alert;
    Vue.prototype.$confirm = MessageBox.service.confirm;
};


if (typeof window !== 'undefined' && window.Vue) {
    install(window.Vue);
}

export default ZsUI;
export {
    Modal,
    MessageBox,
    ZsDialog,
    ZsButton,
    ZsInput,
    ZsInputNumber,
    ZsSwitch,
    ZsIcon,
    ZsTab,
    ZsRadio,
    ZsRadioGroup,
    zsSlidePage
};
