<template>
    <div class="mobile-page">
        <p class="mobile-notice">请输入车牌或拍照识别成功后，点击拨打电话</p>
        <car-number photo-icon @blur="handleGetCarNumber"></car-number>
        <div class="mobile-control">
            <zs-icon
                    class="action-shade"
                    :class="[{'action-shake':isShake}]"
                    icon="parking-call"
                    size="100"
                    @click="handleCall"></zs-icon>
            <p style="font-weight: bold">点击拨打</p>
        </div>
    </div>
</template>
<script>
    import carNumber from '../../../../components_proj/car_number/app.vue';
    import {isCarNum} from  '../../../../assets/js/utils';
    export default {
        components: {
            carNumber
        },
        data(){
            return {
                isShake: false,
                carNumber: ''
            }
        },
        methods: {
            handleGetCarNumber(carNumber){
                this.carNumber = carNumber;
            },
            handleGetMobile(fn){
                this.$ajax(
                    this.$joggle.mzscp.selectUserByCarNumber,
                    {carNumber: this.carNumber},
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.mobile = data.data ? data.data.mobile : '';
                            fn && fn();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            handleCall(){

                this.isShake = true;
                setTimeout(() => {
                    this.isShake = false;
                }, 300);
                if (!isCarNum(this.carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '输入的车牌有误！'
                    })
                } else {
                    this.handleGetMobile(() => {
                        if (!this.mobile) {
                            this.$message({
                                type: 'error',
                                message: '无关联手机号码!'
                            })
                        } else {
                            location.href = `tel:${this.mobile}`;
                        }
                    })
                }

            }
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>