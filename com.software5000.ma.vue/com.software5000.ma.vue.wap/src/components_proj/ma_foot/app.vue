<template>
    <ul class="bottom-nav-wrap">
        <li
                class="nav"
                :class="[nav.className,{'is-curr' : current == index + 1}]"
                v-for="(nav,index) in navs"
                @click="turnToPage(nav.link)"
                :key="index">
            <p class="nav-icon"></p>
            <p class="nav-text">{{ nav.title }}</p>
        </li>
    </ul>
</template>
<script>
    import {turnToNextPage} from '../../assets/js/utils';
    export default {
        name: 'maFoot',
        props: {
            current: {
                type: [Number, String],
                default: 1
            }
        },
        data(){
            return {
                navs: [
                    {
                        title: '工单',
                        link: '/wap/merchant/home/workorder/list.html',
                        className: 'work_order'
                    },
                    {
                        title: '客户',
                        link: '/wap/merchant/home/member/manage.html',
                        className: 'member'
                    },
                    {
                        title: '管理',
                        link: '/wap/merchant/home/manage/index.html',
                        className: 'manage'
                    }
                ]
            }
        },
        methods: {
            turnToPage(url){
                turnToNextPage(url);
            }
        }
    };
</script>
<style lang="less">
    @import './style.less';
</style>