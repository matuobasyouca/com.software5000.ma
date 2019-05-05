<template>
    <div class="lottery-page">
        <p class="page-notice">请输入订单号码进行搜索！</p>
        <div class="order-input-wrap">
            <span class="title">订单号：</span>
            <zs-input placeholder="请输入订单号码" v-model="orderNo" icon="circle-cross"></zs-input>
        </div>
        <zs-button class="page-control" type="primary" @click="handleConfirm">确定</zs-button>
    </div>
</template>

<script>
    import {turnToNextPage, isEmpty} from '../../../../assets/js/utils';
    export default{
        data(){
            return {
                orderNo: ''
            }
        },
        methods: {
            handleConfirm(){
                this.$ajax(
                    this.$joggle.customer.lottery.selectPayOrderByOrderNo,
                    {orderNo: this.orderNo},
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            if (isEmpty(data.data)) {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    message: '数据不存在！'
                                })
                            } else {
                                turnToNextPage('/wap/customer/open/lottery.html', {id: data.data.id})
                            }
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            }
        }
    }
</script>

<style lang="less">
    @import "./style.less";
</style>