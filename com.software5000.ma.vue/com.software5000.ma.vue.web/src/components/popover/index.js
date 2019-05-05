/**
 * Created by JANZ on 2017/3/19.
 */
import ZsPopover from './src/popover';
ZsPopover.install = function (Vue){
    Vue.component(ZsPopover.name, ZsPopover);
};
export default ZsPopover;
