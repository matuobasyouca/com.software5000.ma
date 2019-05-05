<template>
    <transition name="slide-in-out">
        <div class="car-pad-page">
            <ma-head @home-click="handleGoHome">送车贴</ma-head>
            <ul class="pay-user-detail">
                <li class="user-detail-list">
                    <span>您的爱车</span>
                    <car-number ref="carNumber" photo-icon @get-car-number="handleGetCarNumber"></car-number>
                </li>
                <li class="user-detail-list">
                    <span>您的手机</span>
                    <zs-input
                            ref="mobileInput"
                            class="user-detail-mobile-input"
                            v-model="mobile"
                            type="tel"
                            :maxlength="11"
                            placeholder="请输入手机号码"
                            icon="circle-cross"
                            v-if="!hasMobile"
                    ></zs-input>
                    <p v-else class="has-mobile">{{ mobile }}</p>
                </li>
            </ul>
            <p v-show="hasMobile" class="has-mobile-notice">客户已存在，无需添加，可直接赠送车贴</p>
            <div v-show="!hasMobile" class="car-pad-control" @click="insertConfirm">确认</div>
        </div>
    </transition>
</template>

<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import maFoot from '../../../../../components_proj/ma_foot/app.vue';
    import carNumber from '../../../../../components_proj/car_number/app.vue';
    import { isMobile, isCarNum, turnToNextPage } from '../../../../../assets/js/utils';
    export default {
        components: {
            maHead,
            carNumber,
            maFoot
        },
        data(){
            return {
                hasMobile: false,
                mobile: '',
                carNumber: ''
            };
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 保存送车贴信息
             */
            insertConfirm(){
                if (!isMobile(this.mobile)) {
                    this.$message({
                        type: 'error',
                        message: '输入的手机号码有误！',
                        duration: 1200
                    });
                } else {
                    this.$ajax(
                        this.$joggle.customer.binding.insertUser,
                        { carNum: this.carNumber, mobile: this.mobile },
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: '添加成功'
                                });
                                this.mobile = '';
                                this.carNumber = '';
                                this.$refs.carNumber.$refs.input.value = '';
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                });
                            }
                        }
                    );
                }
            },
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 搜索用户车牌对应的信息
             */
            selectGetMobile(){
                this.$ajax(
                    this.$joggle.mzscp.selectUserByCarNumber,
                    { carNumber: this.carNumber },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            if (data.data && data.data.mobile) {
                                this.mobile = data.data.mobile;
                                this.hasMobile = true;
                            } else {
                                this.mobile = '';
                                this.hasMobile = false;
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 表单验证
             * @param carNumber
             */
            handleGetCarNumber(carNumber){
                this.carNumber = carNumber;
                if (carNumber.length === 7) {
                    if (!isCarNum(this.carNumber)) {
                        this.$message({
                            type: 'error',
                            message: '输入的车牌有误！',
                            duration: 1200
                        });
                    } else {
                        this.selectGetMobile();
                    }
                }
            },
            /**
             * 返回管理页面
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){

        }
    };
</script>
<style lang="less">
    @import "./style.less";
</style>