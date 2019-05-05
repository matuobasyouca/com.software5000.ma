<template>
    <div class="binding-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome" :go-back="false" @left-icon-click="goBackHome">绑定微信</ma-head>
        <div class="WXmessage">
            <div class="module-one">
                <div class="wx-img"></div>
                <p class="h3">绑定微信</p>
            </div>
            <div class="module-two">
                <ul>
                    <li>
                        <zs-icon size="16" icon="user" text="绑定账号"></zs-icon>
                        <p class="detail">{{account.userName}}</p></li>
                    <li>
                        <zs-icon size="16" icon="merchant" text="商家名称"></zs-icon>
                        <p class="detail">{{account.business ? account.business.businessName : ''}}</p></li>
                    <li>
                        <zs-icon size="16" icon="binding" text="绑定状态"></zs-icon>
                        <p class="detail">未绑定</p></li>
                </ul>
            </div>
        </div>
        <p class="hit">是否将商家绑定该微信号，绑定后提现金额将提现至该微信号</p>
        <zs-button class="bind-btn" type="primary" @click="insertBind">确定绑定</zs-button>
    </div>
</template>

<script>
    import { turnToNextPage, isEmpty } from '../../../../../assets/js/utils';
    import { selectOpenId, selectWxCodeAgain, selectWxCode } from '../../../../../assets/js/wxUtils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead
        },
        props: {},
        data(){
            return {
                isLoading: false,
                account: '',
                openId: '',
                businessId: ''
            };
        },
        computed: {},
        watch: {},
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 添加绑定
             */
            insertBind(){
                this.$ajax(
                    this.$joggle.business.updateUserOpenId,
                    { openId: this.openId },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.$message({
                                type: 'success',
                                duration: 1200,
                                message: data.msg
                            });
                            turnToNextPage('/wap/merchant/home/manage/index.html');
                        } else {
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 获取个人信息
             */
            selectAccount(){
                this.$ajax(
                    this.$joggle.business.selectBusinessUser,
                    {},
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.account = data.data;
                        } else {
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 返回首页
             */
            goBackHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 返回首页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            selectWxCode((code) => {
                if (isEmpty(code)) {
                    return;
                    selectWxCodeAgain();
                } else {
                    selectOpenId(code, (openId) => {
                        this.openId = openId;
                    });
                }
            });

            this.selectAccount();
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
