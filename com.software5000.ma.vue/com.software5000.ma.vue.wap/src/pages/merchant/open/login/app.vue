<template>
    <div class="page" v-show="!isLoading">
        <div class="loginLogo"></div>
        <form class="loginFormWrap" id="loginForm">
            <div class="formGroup loginUser">
                <i class="icon loginIcon">
                    <zs-icon icon="merchant-user" size="18"></zs-icon>
                </i>
                <div class="formInputWrap">
                    <zs-input v-model="form.name" class="input" name="userId" icon="circle-cross" type="text"
                              placeholder="请输入登录账号"></zs-input>
                </div>
            </div>
            <div class="formGroup loginPassword">
                <i class="icon loginIcon">
                    <zs-icon icon="merchant-pwd" size="18"></zs-icon>
                </i>
                <div class="formInputWrap">
                    <zs-input v-model="form.password" class="input" name="password" icon="circle-cross" type="password"
                              placeholder="请输入账号密码"></zs-input>
                </div>
            </div>
            <div class="loginBtn" :class="[{'is-active' : form.name && form.password}]" id="loginSubmit"
                 @click="loginCheck()">登录
            </div>
        </form>
    </div>
</template>
<script>
    import base64 from '../../../../assets/js/base64.min';
    import { turnToNextPage, getDataFromParam } from '../../../../assets/js/utils';
    import { insertDefaultInfo } from '../../../../assets/js/defaultInfo';
    import { filterAgent } from '../../../../assets/js/turnToHostPage';
    import { selectWxCode, selectOpenId } from '../../../../assets/js/wxUtils';
    export default {
        data() {
            return {
                isLoading: true,
                form: {
                    name: '',
                    password: '',
                    userType: 'merchant'
                },
                info: {
                    codeImg: '',
                    openId: ''
                },
                type: 0,
                openId: ''

            };
        },
        methods: {
            //显示错误信息
            showError (isError){
                const errorMsg = {
                    name: '请输入用户名',
                    password: '请输入密码'
                };

                this.$message({
                    type: 'error',
                    message: errorMsg[isError],
                    duration: '1200'
                });
            },
            isDataEmpty (){
                for (let key in this.form) {
                    if (key !== 'code' && this.form[key].trim() === '') {
                        return key;
                    }
                }
                return false;
            },
            loginCheck(){
                let isError = this.isDataEmpty();

                if (isError) {
                    this.showError(isError);
                    return false;
                }
                this.login();
            },
            login(){
                const postData = {
                    userId: this.form.name,
                    password: base64.encode(this.form.password),
                    userType: this.form.userType,
                    wxOpenId: this.openId
                };

                this.$ajax(
                    this.$joggle.merchant.open.login,
                    JSON.stringify(postData),
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS001000') {
                            insertDefaultInfo(() => {
                                window.localStorage.userId = this.form.name;
                                if (this.type == 1) {
                                    turnToNextPage('/wap/merchant/home/manage/binding.html');
                                } else {
                                    turnToNextPage('/wap/merchant/home/workorder/list.html');
                                }
                            });
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            });
                        }
                    }
                );
            }
        },
        created() {
            this.type = getDataFromParam('type');
            this.form.name = window.localStorage.userId || '';
            filterAgent(() => {
                selectWxCode((code) => {
                    selectOpenId(code, (openId) => {
                        this.openId = openId;
                        this.isLoading = false;
                    });
                });
            });
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>