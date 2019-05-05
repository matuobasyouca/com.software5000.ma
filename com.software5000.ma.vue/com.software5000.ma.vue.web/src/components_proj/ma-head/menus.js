/**
 * Created by janzho on 2017/5/15.
 */

import pages from '../../../app.json';

let role = window.localStorage.role;

function setAuthority(authority) {
    return authority.length == 0 || authority.indexOf(role) > -1;
}

const menuList = [
    {
        text: '首页',
        id: '01',
        show: setAuthority([])
    },
    {
        text: '工单',
        id: '02',
        show: setAuthority([]),
        subMenu: [
            {
                id: '02_1',
                show: setAuthority([]),
                text: '快捷开单'
            },
            {
                id: '02_2',
                show: setAuthority([]),
                text: '未完工工单'
            },
            {
                id: '02_3',
                show: setAuthority([]),
                text: '工单结算'
            }
            /*            {
                            id: '02_4',
                            show: setAuthority([]),
                            text: "客户预约"
                        }*/
        ]
    },
    {
        text: '会员',
        id: '03',
        show: setAuthority([]),
        subMenu: [
            {
                id: '03_1',
                show: setAuthority([]),
                text: '会员资料'
            },
            {
                id: '03_2',
                show: setAuthority([]),
                text: '会员套餐'
            },
            {
                id: '03_3',
                show: setAuthority([]),
                text: '套餐购买'
            },
            {
                id: '03_4',
                show: setAuthority([]),
                text: '会员等级'
            },
            {
                id: '03_5',
                show: setAuthority([]),
                text: '会员充值'
            }
        ]
    },
    {
        text: '员工',
        id: '04',
        show: setAuthority([])
    },
    {
        text: '财务',
        id: '05',
        show: setAuthority([]),
        subMenu: [
            {
                id: '05_1',
                show: setAuthority([]),
                text: '微信提现'
            },
            {
                id: '05_2',
                show: setAuthority([]),
                text: '财务明细'
            }
        ]
    },
    {
        text: '设置',
        id: '06',
        show: setAuthority([]),
        subMenu: [
            {
                id: '06_1',
                show: setAuthority([]),
                text: '服务项目'
            },
            {
                id: '06_2',
                show: setAuthority([]),
                text: '店铺设置'
            },
            {
                id: '06_3',
                show: setAuthority([]),
                text: '数据导入'
            }
        ]
    }
];

for (let i = 0; i < menuList.length; i++) {
    for (let h = 0; h < pages.pages.length; h++) {
        let p = pages.pages[h];

        if (menuList[i].id != '' && menuList[i].id == p.id && typeof (menuList[i].subMenu) == 'undefined') {
            menuList[i].url = `/web/${p.entry}.html`;
        }
    }
    if (typeof (menuList[i].subMenu) != 'undefined') {
        for (let j = 0; j < menuList[i].subMenu.length; j++) {
            let sm = menuList[i].subMenu[j];

            for (let k = 0; k < pages.pages.length; k++) {
                let p = pages.pages[k];

                if (sm.id == p.id) {
                    sm.url = `/web/${p.entry}.html`;
                    if (typeof (sm.text) == 'undefined') {
                        sm.text = p.title;
                    }
                }
            }
        }
    }

}
export default menuList;