<template>
    <div class="withdraw-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">去提现</ma-head>
        <div class="withdraw-img"></div>
        <div class="withdraw-body">
            <ul>
                <li class="money-h3"><span>可提现金额</span><p>{{changeMoney(moneyDatas.canCheckMoney)}}</p></li>
                <li class="name-h3"><span>到账微信</span><p>{{message.nickName}}</p></li>
                <li class="money-h3"><span>到账金额</span><p>{{moneyDatas.canCheckMoney > message.maxCheckMoney ? changeMoney(message.maxCheckMoney) : changeMoney(moneyDatas.canCheckMoney)}}</p></li>
            </ul>
        </div>
        <zs-button class="bind-btn" type="primary" @click="checkMoney">确定提现</zs-button>
    </div>
</template>

<script>
    import {turnToNextPage, isEmpty} from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead,
        },
        data(){
            return {
                isLoading: false,
                moneyDatas: [],
                message: []
            }
        },
        methods: {
            getMoneyDatas(fn,loading){
                this.$ajax(
                        this.$joggle.merchant.finance.selectBusinessCheckMoney,
                        {},
                        loading,
                        (data, loading) => {
                            if (data.code == 'ZS011000') {
                                this.moneyDatas = data.data;
                                fn && fn();
                            } else {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                })
                            }
                        }
                )
            },
            getMessage(fn, loading){
                this.$ajax(
                        this.$joggle.merchant.finance.selectCheckMoneyDto,
                        {},
                        loading,
                        (data, loading) => {
                            if (data.code == 'ZS011000') {
                                this.message = data.data;
                                fn && fn();
                            } else {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                })
                            }

                        }
                )
            },
            checkMoney(){
                let checkMoney = this.moneyDatas.canCheckMoney > this.message.maxCheckMoney ? this.message.maxCheckMoney : this.moneyDatas.canCheckMoney;
                this.$ajax(
                        this.$joggle.merchant.finance.insertDrawMoney,
                        {money: checkMoney},
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
                                })
                            }

                        }
                )
            },
            //返回首页
            handleGoHome(){turnToNextPage('/wap/merchant/home/manage/index.html');},
            changeMoney(money){
                if(isEmpty(money))return 0;
                let num = (money/100).toString().split('.');
                let arr = [];
                let len = num[0].length;
                let lastIndex = 0;
                while( len>0 ){
                    lastIndex = len;
                    len -= 3;
                    arr.unshift(num[0].substring(len,lastIndex));
                }
                return num[1] ? arr.join(',')+'.'+num[1] : arr.join(',');
            }
        },
        created(){
            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                this.getMoneyDatas(() => {
                    resolve();
                }, loading)
            });
            const f2 = new Promise((resolve) => {
                this.getMessage(() => {
                    resolve();
                }, loading)
            });
            Promise.all([f1, f2]).then(() => {
                loading.close();
            })
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
