<template>
    <div class="login" v-show="!isLoading">
        <div class="login-header">
            <div class="login-head">
                <p class="title-h1">诚品车服</p>
                <p class="title-h3">中晟诚品好车网</p>
            </div>
        </div>
        <div class="login-body">
            <div class="login-bg"></div>
            <div class="login-margin">
                <zs-form class="login-box" :rules="rules" :model="form" ref="loginForm">
                    <p class="login-box__title">诚品车服--运营后台</p>
                    <p class="user-css">
                        <zs-input icon="circle-cross" v-model="form.userName" placeholder="请输入账号"
                                  @blur="isDataEmpty('userName')" @keydown.enter="loginCheck"></zs-input>
                    </p>
                    <p class="pwd-css">
                        <zs-input icon="circle-cross" v-model="form.password" placeholder="请输入密码" type="password"
                                  @blur="isDataEmpty('password')" @keydown.enter="loginCheck"></zs-input>
                    </p>
                    <div class="error-div" v-show="errorMsg.length>0">
                        <zs-icon icon="danger" :size="14" class="danger-icon-css"></zs-icon>
                        <span class="error-message">{{errorMsg}}</span>
                    </div>
                    <zs-form-item>
                        <zs-button type="primary" class="login-box__loginButton" @click="loginCheck">登录</zs-button>
                    </zs-form-item>
                </zs-form>
            </div>
        </div>
        <div class="index-footer">
            <p>版权所有 © 厦门中晟诚品信息科技有限公司，未经许可不得复制、转载或摘编，违者必究!</p>
            <p>Copyright © 2016 mmgoodcar.com ，LTD. All Rights Reserved ICP备案证书号： 闽ICP备16000732号</p>
        </div>
    </div>
</template>

<script>
    import base64 from '../../../../assets/js/base64.min';
    import { turnToNextPage } from '../../../../assets/js/utils';
    import { filterAgent } from '../../../../assets/js/turnToHostPage';
    export default{
        data(){
            return {
                isLoading: true,
                form: {
                    userName: '',
                    password: '',
                    userType: 'zsoperator'
                },
                rules: {
                    userName: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
                },
                errorMsg: '',
                errorMsgArr: {
                    userName: '请输入用户名',
                    password: '请输入密码'
                }
            };
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
                const postData = {
                    userId: this.form.userName,
                    password: base64.encode(this.form.password),
                    userType: this.form.userType
                };

                this.$ajax(
                    this.$joggle.operator.open.login,
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
                                turnToNextPage('/web/operator/home/activity/manage.html');
                            }, 200);
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
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>