<template>
    <div class="binding-page">
        <ma-head left-icon="">绑定</ma-head>
        <div class="binding-wrap">
            <span class="binding-title">车牌号码：</span>
            <car-number @get-car-number="handleGetCarNumber"></car-number>
        </div>
        <div class="binding-wrap mobile-wrap">
            <span class="binding-title">手机号码：</span>
            <zs-input
                    class="binding-mobile"
                    v-model="mobile"
                    type="tel"
                    :maxlength="11"
                    placeholder="请输入手机号码"
                    icon="circle-cross"
            ></zs-input>
        </div>
        <div class="binding-btn" @click="handleConfirm">确定</div>
    </div>
</template>

<script>
    import carNumber from '../../../../components_proj/car_number/app.vue';
    import maHead from '../../../../components_proj/ma_head/app.vue';
    import {isMobile, isCarNum, isEmpty, turnToNextPage} from '../../../../assets/js/utils';
    import {selectWxCode, selectWxCodeAgain, selectOpenId} from '../../../../assets/js/wxUtils';
    export default{
        components: {
            carNumber,
            maHead
        },
        data(){
            return {
                mobile: '',
                carNumber: '',
                openId: '',
                id: ''
            }
        },
        methods: {
            handleGetCarNumber(carNumber){
                this.carNumber = carNumber;
                if (isCarNum(carNumber)) {
                    this.selectUserByCarNumber()
                }
            },
            selectUserByCarNumber(){
                this.$ajax(
                    this.$joggle.mzscp.selectUserByCarNumber,
                    {carNumber: this.carNumber},
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            if (!isEmpty(data.data)) {
                                this.mobile = data.data.mobile;
                                this.id = data.data.id || '';
                            } else {
                                this.mobile = '';
                                this.id = '';
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            handleConfirm(){
                if (!isCarNum(this.carNumber)) {
                    this.$modal({
                        type: 'error',
                        message: '车牌号码有误！'
                    })
                    return;
                } else if (!isMobile(this.mobile)) {
                    this.$modal({
                        type: 'error',
                        message: '手机号码有误！'
                    })
                    return;
                }
                let url, params;
                if (isEmpty(this.id)) {
                    url = this.$joggle.customer.binding.insertUser;
                    params = {
                        mobile: this.mobile,
                        carNumber: this.carNumber,
                        wxOpenId: this.openId
                    }
                } else {
                    url = this.$joggle.mzscp.updateUser;
                    params = {
                        id: this.id,
                        mobile: this.mobile,
                        wxOpenId: this.openId
                    }
                }
                this.$ajax(
                    url,
                    params,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: '绑定成功'
                            })
                            setTimeout(() => {
                                turnToNextPage('/wap/customer/open/scan.html', {type: 1})
                            }, 1200)
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            }
        },
        created(){
            selectWxCode((code) => {
                selectOpenId(code, (openId) => {
                    this.openId = openId;
                })
            })
        }
    }
</script>

<style lang="less">
    @import "./style.less";
</style>