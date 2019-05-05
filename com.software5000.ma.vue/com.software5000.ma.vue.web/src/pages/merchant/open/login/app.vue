<template>
    <div class="login-body" v-show="!isLoading">
        <div class="login-img"></div>
        <div class="login-content">
            <span class="login-logo"></span>
            <p class="login-title">中晟诚品-诚品车服</p>
            <div class="login-form">
                <zs-form :model="form" :rules="rules">
                    <p class="form-css user-css"><span class="border"></span>
                        <zs-icon icon="user" :size="20" class="left-icon-css"></zs-icon>
                        <zs-input icon="circle-cross" v-model="form.userName" placeholder="请输入账号"
                                  @blur="isDataEmpty('userName')" @keydown.enter="loginCheck"></zs-input>
                    </p>
                    <p class="form-css"><span class="border"></span>
                        <zs-icon icon="pwd" :size="20" class="left-icon-css"></zs-icon>
                        <zs-input icon="circle-cross" v-model="form.password" placeholder="请输入密码" type="password"
                                  @blur="isDataEmpty('password')" @keydown.enter="loginCheck"></zs-input>
                    </p>
                    <div class="error-div" v-show="errorMsg.length>0">
                        <zs-icon icon="danger" :size="14" class="danger-icon-css"></zs-icon>
                        <span class="error-message">{{errorMsg}}</span></div>
                    <zs-button class="form-btn" type="primary" @click="loginCheck">登录</zs-button>
                </zs-form>
            </div>
        </div>
    </div>
</template>

<script>
    import base64 from '../../../../assets/js/base64.min';
    import {turnToNextPage} from '../../../../assets/js/utils';
    import {filterAgent} from '../../../../assets/js/turnToHostPage';
    export default{
        data(){
            return {
                isLoading: true,
                form: {
                    userName: '',
                    password: '',
                    userType: "merchant"
                },
                rules: {
                    userName: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ]
                },
                errorMsg: '',
                errorMsgArr: {
                    userName: '请输入用户名',
                    password: '请输入密码'
                }
            }
        },
        methods: {
            isDataEmpty (key){
                if (this.form[key].trim() === '') {
                    this.errorMsg = this.errorMsgArr[key];
                    return false;
                }
                this.errorMsg = '';
                return true;
            },
            isEmpty(){
                for (let key in this.form) {
                    if (this.form[key].trim() === '') {
                        this.errorMsg = this.errorMsgArr[key];
                        return false;
                    }
                }
                this.errorMsg = '';
                return true;
            },
            loginCheck(){
                let isError = this.isEmpty();
                if (isError) {
                    this.$loading();
                    this.login();
                } else {
                    return false;
                }
            },
            login(){
                let postData = {
                    "userId": this.form.userName,
                    "password": base64.encode(this.form.password),
                    "userType": this.form.userType
                };
                this.$ajax(
                    this.$joggle.merchant.open.login,
                    JSON.stringify(postData),
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS001000') {
                            localStorage.businessId = data.data.businessId;
                            localStorage.realName = data.data.realName;
                            localStorage.userType = data.data.userType;
                            localStorage.userName = data.data.userName;
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/index.html');
                            }, 200)
                        } else {
                            this.errorMsg = data.msg;
                        }
                    }
                );
            }
        },
        created(){
            this.form.userName = localStorage.userName || '';
            filterAgent(() => {
                this.isLoading = false;
            })
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>