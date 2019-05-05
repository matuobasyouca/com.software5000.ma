<template>
    <div class="setting-store">
        <div class="stroe-detail">
            <div class="store-logo" :style="`background-image: url(${datas.imageShowPath || ''})`"></div>
            <div class="store-msg">
                <div class="store-name">{{datas.businessName}}</div>
                <div class="store-tel">
                    <zs-icon icon="phone" :size="22"></zs-icon>
                    {{datas.businessTel}}
                </div>
                <div class="store-addr">
                    <zs-icon icon="location" :size="18"></zs-icon>
                    {{`${datas.businessAreaCodeDes}${datas.businessAddress}`}}
                </div>
                <p class="hintting">(店铺信息需修改请联系中晟诚品)</p>
            </div>
        </div>
        <div class="store-set">
            <div class="set-title">
                <zs-icon icon="history" :size="20"></zs-icon>
                <p>关账日设置</p></div>
            <ul class="set-inp">
                <li><em>*</em> 关账日</li>
                <li>
                    <zs-input min="0" :maxlength="2" v-model.number="day"></zs-input>
                    <p class="hit">输入1~28数字，每月关账日过后，之前订单将不可进行反结算</p>
                </li>
            </ul>
            <zs-button class="save-button" type="primary" @click="saveSetting">保存</zs-button>
        </div>
    </div>
</template>

<script>
    import {isEmpty} from '../../../../../../../assets/js/utils';
    export default{
        name: 'list',
        data(){
            return {
                day: 28,
                datas: []
            }
        },
        methods: {
            getBusiness(){
                this.$ajax(
                        this.$joggle.merchant.workorder.selectBusinessInfo,
                        {},
                        true,
                        (data, loading)=> {
                            loading.close();
                            if (data.code == 'ZS011000') {
                                this.datas = data.data;
                                this.day = data.data.closingDateNum ? data.data.closingDateNum : 28;
                            } else {
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                })
                            }
                        }
                )
            },
            saveSetting(){
                let errorMsg = '';
                if (isEmpty(this.day)) {
                    errorMsg = '请输入关账日';
                } else if (this.day > 28 || this.day <= 0) {
                    errorMsg = '请输入1~28数字';
                }
                if (errorMsg) {
                    this.$message({duration: 1200, message: errorMsg});
                    return;
                }
                this.$ajax(
                        this.$joggle.merchant.setting.updateBusiness,
                        {
                            id: this.datas.id,
                            closingDateNum: this.day
                        },
                        true,
                        (data, loading)=> {
                            loading.close();
                            if (data.code == 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    duration: 1200,
                                    message: data.msg
                                })
                            } else {
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                })
                            }
                        }
                )
            }
        },
        mounted(){
            this.getBusiness();
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>