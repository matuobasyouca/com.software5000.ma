<template>
    <div class="body_css" v-if="!isLoading">
        <div class="img_css"><img src="../../../../assets/img/oper_login_logo.png" width="80px" height="80px"></div>
        <div class="span_css">
            <p class="p_css">中晟诚品管理后台</p>
        </div>
        <div class="login_div">
            <p class="user_name_css">
                <zs-icon icon="operator-user" :size="16" class="left_icon_css" icon-dis="15" text="账号"></zs-icon>
                <zs-input v-model="form.userName" icon="circle-cross" placeholder="请输入账号" class="input_css"></zs-input>
            </p>
            <p class="pwd_css">
                <zs-icon icon="operator-pwd" :size="16" class="left_icon_css" icon-dis="15" text="密码"></zs-icon>
                <zs-input v-model="form.password" icon="circle-cross" placeholder="请输入密码" type="password"
                          class="input_css"></zs-input>
            </p>
            <p class="button_css" @click="loginCheck()">登录</p>
        </div>
    </div>
</template>
<script>
    import base64 from '../../../../assets/js/base64.min';
    import { filterAgent } from '../../../../assets/js/turnToHostPage';
    import { selectWxCode, selectOpenId } from '../../../../assets/js/wxUtils';
    export default {
        components: {},
        data() {
            return {
                isLoading: true,
                openId: '',
                form: {
                    userName: '',
                    password: '',
                    userType: 'zsoperator'
                }
            };
        },
        computed: {},
        methods: {
            showError (isError){
                const errorMsg = {
                    userName: '请输入用户名',
                    password: '请输入密码'
                };

                this.$modal({
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
                const loading = this.$loading();

                setTimeout(() => {
                    loading.close();
                }, 1500);
                this.login();
            },
            login(){
                const postData = {
                    userId: this.form.userName,
                    password: base64.encode(this.form.password),
                    userType: this.form.userType,
                    wxOpenId: this.openId
                };

                this.$ajax(
                    this.$joggle.operator.open.login,
                    postData,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS001000') {
                            localStorage.realName = data.data.realName;
                            localStorage.userId = this.form.userName;
                            window.location = '/wap/operator/home/workbench.html';
                        } else {
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
            filterAgent(() => {
                selectWxCode((code) => {
                    selectOpenId(code, (openId) => {
                        this.openId = openId;
                        this.form.userName = localStorage.userId;
                        this.isLoading = false;
                    });
                });
            });
        }
    };
</script>

<style>
    .body_css {
        position: relative;
        height: 100%;
        background-color: #ffffff;
    }

    .img_css {
        padding-top: 48px;
        width: 80px;
        height: 128px;
        margin-left: 50%;
        transform: translateX(-50%);
    }

    .span_css {
        width: 200px;
        margin-left: 50%;
        transform: translateX(-50%);
        height: 40px;
    }

    .p_css {
        margin-top: 0px;
        line-height: 40px;
        font-size: 18px;
        color: #333333;
        text-align: center;
    }

    .login_div {
        margin-top: 30px;
        margin-left: 20px;
        margin-right: 20px;
        height: 189px;
    }

    .user_name_css {
        margin-top: 0px;
        border-bottom: 1px solid #f2f2f2;
        line-height: 50px;
    }

    .pwd_css {
        margin-top: 9px;
        border-bottom: 1px solid #f2f2f2;
        line-height: 50px;
    }

    .button_css {
        border-radius: 5px;
        margin-top: 40px;
        background-color: #0a91f5;
        height: 40px;
        line-height: 40px;
        width: 100%;
        font-size: 17px;
        color: #ffffff;
        text-align: center;
        vertical-align: middle;
    }

    .input_css {
        border: 0;
        font-size: 40px;
        margin-left: 50px;
        color: #333333;
        font-weight: bold;
        width: 160px !important;
    }

    .input_css .zs-input__inner {
        font-size: 16px;

    }

    .i_css {
        margin-left: 15px;
        font-size: 16px;
    }
</style>
