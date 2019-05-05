<template>
    <div class="record-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">提现记录</ma-head>
        <div class="record-header">
            <ul>
                <li>
                    <div class="nr">
                        <div class="num">{{handleChangeMoney(moneyDatas.canCheckMoney)}}</div>
                        <div class="zi">可提现金额（元）</div>
                    </div>
                    <div class="xian"></div>
                </li>
                <li>
                    <div class="nr">
                        <div class="num">{{handleChangeMoney(moneyDatas.haveCheckMoney)}}</div>
                        <div class="zi">已提现金额（元）</div>
                    </div>
                </li>
            </ul>
        </div>
        <div class="record-title">提现记录</div>
        <div class="record-body">
            <ul v-if="formDatas.length > 0" class="record-ul">
                <li v-for="(item,index) in formDatas">
                    <div class="row">
                        <div class="row-h3">提现</div>
                        <div class="row-money">{{item.checkMoneyFee/100}}</div>
                    </div>
                    <div class="row-time">{{item.createTime}}</div>
                </li>
                <div
                        class="show_all_css"
                        :class="[{'show_more_css' : hasNextPage}]"
                        @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                </div>
            </ul>
            <div v-else>
                <ul class="data-empty">
                    <li>暂无提现记录</li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">
    import { turnToNextPage, moneyFormat } from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead
        },
        data(){
            return {
                isLoading: false,
                formCondition: {
                    orderBy: 'id desc',
                    startPage: 1,
                    pageSize: 10,
                    tradingTimeStart: '',
                    tradingTimeEnd: ''
                },
                formDatas: [],
                hasNextPage: false,
                moneyDatas: []
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 获取体现记录列表
             * @param isShowMore
             * @param fn
             */
            selectFormDatas(isShowMore, fn){
                if (isShowMore) {
                    this.formCondition.startPage++;
                } else {
                    this.formCondition.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.finance.selectPageWechatPayOrder,
                    this.formCondition,
                    false,
                    (data) => {
                        fn && fn();
                        if (data.code == 'ZS011000') {
                            this.formDatas = isShowMore ? this.formDatas.concat(data.data.list) : data.data.list;
                            this.hasNextPage = data.data.hasNextPage;
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
            /**
             * 获取可体现余额和已体现余额
             * @param fn
             */
            selectMoneyDatas(fn){
                this.$ajax(
                    this.$joggle.merchant.finance.selectBusinessCheckMoney,
                    {},
                    false,
                    (data) => {
                        fn && fn();
                        if (data.code == 'ZS011000') {
                            this.moneyDatas = data.data;
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
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 点击查看更多
             */
            handleShowMore(){
                if (this.hasNextPage) {
                    const loading = this.$loading();

                    this.selectFormDatas(true, () => {
                        loading.close();
                    })
                }
            },
            /**
             * 返回首页
             */
            handleGoHome(){turnToNextPage('/wap/merchant/home/manage/index.html');},
            /**
             * 获取可体现余额和已体现余额
             * @param money
             */
            handleChangeMoney(money){
                return moneyFormat(money);
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                this.selectMoneyDatas(() => {
                    resolve();
                })
            });
            const f2 = new Promise((resolve) => {
                this.selectFormDatas(false, () => {
                    resolve();
                })
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
