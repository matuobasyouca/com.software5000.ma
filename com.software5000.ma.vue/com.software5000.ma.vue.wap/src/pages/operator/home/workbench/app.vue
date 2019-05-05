<template>
    <div>
        <div class="welcome-title">
            <div class="head-title">中晟诚品管理后台</div>
            <div class="logout_css" @click="logout()">退出</div>
            <div class="name-css">
                <div class="logo-css"><img src="../../../../assets/img/head.png" width="70px" height="70px"></div>
                <div class="span-css">{{userName}}</div>
                <div class="we-css">欢迎您的到来！</div>
            </div>
        </div>
        <div class="comobo_css" @click="turnToMerchant">
            <zs-icon icon="bus" :size="24" class="left_icon_css"></zs-icon>
            <p class="p_css">商家管理</p>
            <zs-icon icon="arrow-right" :size="10" class="right_icon_css"></zs-icon>
        </div>
        <div class="order_css" @click="turnToMerchantData">
            <zs-icon icon="bus-data" :size="24" class="left_icon_css"></zs-icon>
            <p class="p_css">商家数据</p>
            <zs-icon icon="arrow-right" :size="10" class="right_icon_css"></zs-icon>
        </div>
        <div class="order_css" @click="turnToShouzhi">
            <zs-icon icon="shouzhi" :size="24" class="left_icon_css"></zs-icon>
            <p class="p_css">收支数据</p>
            <zs-icon icon="arrow-right" :size="10" class="right_icon_css"></zs-icon>
        </div>
    </div>
</template>
<script>
    import maHeader from '../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHeader
        },
        data() {
            return {
                userName: ''
            };
        },
        created() {
            this.userName = localStorage.realName;
        },
        methods: {
            logout() {
                this.$confirm({
                    customClass: 'shut-down-confirm',
                    type: 'warning',
                    title: '退出',
                    showClose: true,
                    message: '确定退出系统？'
                }).then(() =>{
                    this.$ajax(
                        this.$joggle.operator.open.logout,
                        {},
                        true,
                        (data, modal) =>{
                            modal.close();
                            if (data.code === 'ZS011000') {
                                modal.close();
                                localStorage.realName = '';
                                window.location = '/wap/operator/open/login.html';
                            } else {
                                this.$message({
                                    type: 'error',
                                    text: data.msg,
                                    duration: 1200
                                });
                            }
                        });
                });

            },
            turnToCombo() {
                window.location = '/wap/operator/home/combo/manage.html';
            },
            turnToOrder() {
                window.location = '/wap/operator/home/combo_order/manage.html';
            },
            turnToMerchant() {
                window.location = '/wap/operator/home/merchant_manage/merchant_list.html';
            },
            turnTo99Record() {
                window.location = '/wap/operator/home/99record.html';
            },
            turnToShouzhi() {
                window.location = '/wap/operator/home/shouzhi.html';
            },
            turnToMerchantData() {
                window.location = '/wap/operator/home/merchant_data/data_list.html';
            }
        }
    };
</script>

<style>
    .welcome-title {
        color: #222222;
        height: 170px;
        background-color: #ffffff;
        position: relative;
        background-image: url("../../../../assets/img/manage_title_bg.png");
        background-size: 100% 100px;
    }

    .head-title {
        font-size: 18px;
        color: #FFFFFF;
        height: 44px;
        line-height: 44px;
        text-align: center;
    }

    .logout_css {
        height: 44px;
        font-size: 16px;
        color: #ffffff;
        line-height: 44px;
        position: absolute;
        right: 10px;
        top: 0;
    }

    .logo-css {
        position: absolute;
        width: 70px;
        height: 70px;
        border-radius: 35px 35px 35px 35px;
        top: -35px;
        left: 50%;
        transform: translateX(-50%);
    }

    .name-css {
        position: relative;
        background-color: #FFFFFF;
        height: 120px;
        margin-left: 35px;
        margin-right: 35px;
        bottom: -44px;
        border-radius: 3px 3px 3px 3px;
        text-align: center;
        box-shadow: 0 4px 6px rgba(0, 0, 0, .05);
    }

    .span-css {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        top: 46px;
        font-size: 16px;
        color: #232d3c;
        font-weight: bold;
        height: 28px;
        line-height: 28px;
    }

    .we-css {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        top: 74px;
        font-size: 14px;
        color: #666666;
        height: 30px;
        line-height: 30px;
    }

    .comobo_css {
        position: relative;
        margin-top: 64px;
        height: 50px;
        border-bottom: 1px solid #ebebeb;
        background-color: #ffffff;
        font-size: 16px;
        color: #333333;
        width: 100%;
        line-height: 50px;
    }

    .order_css {
        position: relative;
        height: 50px;
        background-color: #ffffff;
        font-size: 16px;
        color: #333333;
        border-bottom: 1px solid #ebebeb;
    }

    .p_css {
        line-height: 50px;
        width: 200px;
        margin-left: 50px;
        font-size: 16px;
        color: #333333;
    }

    .right_icon_css {
        float: right;
        margin-right: 10px;
        margin-top: -30px;
        border-color: #000000;
    }

    .left_icon_css {
        float: left;
        margin-top: 15px;
        margin-left: 15px;
    }

    .zs-icon-arrow-right {
        border-top: 1px solid #bbbbbb !important;
        border-right: 1px solid #bbbbbb !important;
    }

    .shut-down-confirm {
        width: 80% !important;
        height: 270px;
    }

    .zs-message-box__content {
        padding: 14px 20px 16px !important;
    }
</style>