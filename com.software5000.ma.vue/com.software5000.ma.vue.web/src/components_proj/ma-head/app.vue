<template>
    <div class="crm-head__wrap">
        <div class="crm-head__main page-w">
            <p class="crm-head__logo"><zs-icon icon="logo-small" :size="34" :text="titleText"></zs-icon></p>
            <ul class="crm-head__menu">
                <li v-for="(item,index) in menuData" class="crm-head__user-pull" v-if="item.show" :class="[{curr : currId==item.id}]"
                    :key="index">
                    <p class="user-pull__left-icon" v-if="item.leftIcon">
                        <i :class="'zs-icon-'+item.leftIcon"></i>
                    </p>
                    <span class="crm-head__title" @click="turnPage(item.url)">{{item.text}}</span>
                    <ul class="user-pull__box" v-if="item.subMenu">
                        <li v-for="(item2,index2) in item.subMenu" v-if="item2.show" :key="index2"  @click="turnPage(item2.url)">{{item2.text}}</li>
                    </ul>
                </li>
            </ul>
            <div class="crm-head__user clr">
                <span class="crm-head__user-name">{{realName}}</span>
                <div class="crm-head__exit"  @click="handleShutDown"></div>
            </div>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">
    import menuList from './menus';
    import menuOperator from './menusOperator';
    import {turnToNextPage} from '../../assets/js/utils';

    export default {
        name: 'CrmHead',
        props: {
            currId: [String, Number],
            operator: {
                type: Boolean,
                default: false
            },
        },
        data(){
            return {
                notice : 0,
                userName: '中晟诚品',
                menuData : menuList,
                realName : '',
                titleText: ''
            }
        },
        computed: {

        },
        methods: {
            handleShutDown(){
                this.$confirm({
                    customClass:'shut-down-confirm',
                    type:'warning',
                    title: '退出',
                    showClose: true,
                    message:'确定退出系统？'
                }).then((action) => {
                    this.$ajax(
                        this.$joggle.operator.open.logout,
                        {},
                        true,
                        (data,model) => {
                            model.close();
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                })
                                setTimeout(()=>{
                                    if(this.operator){
                                        turnToNextPage('/web/operator/open/login.html');
                                    }else {
                                        turnToNextPage('/web/merchant/open/login.html');
                                    }
                                },1200);
                            }else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                })
                            }
                        }
                    );
                })
            },
            turnPage(url){
                if(url){
                    turnToNextPage(url);
                }
            }
        },
        created(){
            this.realName = localStorage.realName;
            this.menuData = this.operator == true ? menuOperator : menuList;
            this.titleText = this.operator == true ? '诚品车服-运营后台' : '诚品车服';
        }
    }
</script>

<style lang="less">
    @import 'style.less';
</style>