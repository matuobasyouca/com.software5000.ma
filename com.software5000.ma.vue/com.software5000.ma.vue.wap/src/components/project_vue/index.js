/**
 * Created by JANZ on 2017/2/27.
 */

import maHeader from './header';
import maFooter from './footer';
import iconLabel from './icon_label';
import goBack from './go_back';
import listBase from './list_base';

const components = [
    maHeader,
    maFooter,
    iconLabel,
    goBack,
    listBase

]

const maVue = function (Vue, opts = {}){

    if (maVue.installed) return;

    components.map(component =>{
        Vue.component(component.name, component);
    });

}

if (typeof window !== 'undefined' && window.Vue) {
    install(window.Vue);
}

export default maVue;
export {
    maHeader,
    maFooter,
    iconLabel,
    goBack,
    listBase
};
