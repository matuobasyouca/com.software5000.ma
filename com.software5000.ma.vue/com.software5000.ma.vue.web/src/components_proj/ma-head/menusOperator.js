/**
 * Created by janzho on 2017/5/15.
 */

import pages from '../../../app.json';
let role = window.localStorage.role;

function setAuthority(authority) {
    return authority.length == 0 || authority.indexOf(role) > -1
}
const menuList = [
    {
        text: '活动',
        id: '11',
        show: setAuthority([]),
        subMenu: [
            {
                id: '11_1',
                show: setAuthority([]),
                text: "拼团列表"
            },
            {
                id: '11_2',
                show: setAuthority([]),
                text: "拼团记录"
            },
        ]
    },
]

for (let i = 0; i < menuList.length; i++) {
    for (let h = 0; h < pages.pages.length; h++) {
        let p = pages.pages[h];
        if (menuList[i].id != "" && menuList[i].id == p.id && typeof (menuList[i].subMenu) == "undefined") {
            menuList[i].url = `/web/${p.entry}.html`;
        }
    }
    if (typeof (menuList[i].subMenu) != "undefined") {
        for (let j = 0; j < menuList[i].subMenu.length; j++) {
            let sm = menuList[i].subMenu[j]
            for (let k = 0; k < pages.pages.length; k++) {
                let p = pages.pages[k]
                if (sm.id == p.id) {
                    sm.url = `/web/${p.entry}.html`;
                    if (typeof (sm.text) == "undefined") {
                        sm.text = p.title;
                    }
                }
            }
        }
    }

}
export default menuList;