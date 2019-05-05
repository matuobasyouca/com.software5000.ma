<template>
    <div class="manage-page" v-show="!isLoading">
        <div class="manage-header">
            <div v-if="message.nickName" class="presonMsg">
                <div class="header-top">可提现金额（元）<span @click="handleGoRecord">提现记录</span></div>
                <div class="all-money">{{handleChangeMoney(moneyDatas.canCheckMoney)}}</div>
                <p class="userName">{{message.nickName ? `（绑定微信：${message.nickName}）` : ''}}</p>
                <div class="btn-get" @click="handleCheckMoney">去提现</div>
            </div>
            <div v-else class="binding">
                <p class="bind-title">该账户还未绑定提现微信昵称</p>
                <div class="btn-get" @click="handleBinding">去绑定</div>
            </div>
            <div class="notice-board">
                <div class="board">
                    <div class="half">
                        <div class="mid">
                            <div>{{message.countTodayWorkOrder ? message.countTodayWorkOrder : 0}}</div>
                            <p>今日开单数</p>
                        </div>
                    </div>
                    <div class="half">
                        <div class="mid">
                            <div>{{message.countTodayTotalPrice ? handleChangeMoney(message.countTodayTotalPrice) :
                                0}}
                            </div>
                            <p>今日营业额</p>
                        </div>
                    </div>
                    <span class="xian"></span>
                </div>
            </div>
        </div>
        <div class="manage-body">
            <div class="enter-list">
                <ul>
                    <li v-for="(item,index) in enterList" @click="handleClickUrl(item.url)">
                        <div class="li-content">
                            <div :class="`img${index+1}`"></div>
                            <p class="title">{{item.title}}</p>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <zs-dialog
                class="hit-dialog"
                v-model="showHintDialog"
                show-close>
            <div class="dialog-body">
                <div class="money-img"></div>
                <div v-if="status == 1">
                    <p class="h3">您可提现金额小于1元</p>
                    <p class="h4">1≤提现金额≤2万元</p>
                </div>
                <div v-else-if="status == 2">
                    <p class="h3">每日限提现一次，您今日已提现</p>
                </div>
            </div>
            <template slot="footer">
                <zs-button
                        type="primary"
                        @click="showHintDialog=false">确定
                </zs-button>
            </template>
        </zs-dialog>
        <zs-dialog
                class="exit-dialog"
                v-model="showExitDialog"
                show-close>
            <div class="dialog-body">
                <div class="money-img"></div>
                <p class="h3"> 确定退出？</p>
            </div>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="showExitDialog=false">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="handleExit">确定
                </zs-button>
            </template>
        </zs-dialog>
        <div class="block">
            <ma-foot :current="3"></ma-foot>
        </div>
    </div>
</template>

<script>
    import { turnToNextPage, isEmpty, moneyFormat } from '../../../../../assets/js/utils';
    import maFoot from '../../../../../components_proj/ma_foot/app.vue';
    export default {
        components: {
            maFoot
        },
        props: {},
        data(){
            return {
                isLoading: false,
                enterList: [
                    { title: '套餐购买记录', url: '/wap/merchant/home/member/package.html' },
                    { title: '财务对账', url: '/wap/merchant/home/manage/list_financial.html' },
                    { title: '会员等级', url: '/wap/merchant/home/manage/list_level.html' },
                    { title: '服务项目', url: '/wap/merchant/home/manage/services_available.html' },
                    { title: '服务套餐', url: '/wap/merchant/home/manage/service_package/package_list.html' },
                    { title: '会员充值', url: '/wap/merchant/home/recharge/update.html' },
                    { title: '店铺设置', url: '/wap/merchant/home/manage/store_setting.html' },
                    { title: '送车贴', url: '/wap/merchant/home/manage/car_pad.html' },
                    { title: '退出', url: '-1' }
                ],
                showHintDialog: false,
                status: 1,
                showExitDialog: false,
                message: [],
                moneyDatas: []
            };
        },
        computed: {},
        watch: {},
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 查找用户可体现余额信息
             * @param fn
             * @param loading
             */
            selectMoneyDatas(fn, loading){
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
                            });
                        }
                    }
                );
            },
            /**
             * 查找用户信息
             * @param fn
             * @param loading
             */
            selectUserMessage(fn, loading){
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
                            });
                        }

                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 首页各个图标点击的跳转
             * @param url
             */
            handleClickUrl(url){
                if (isEmpty(url)) return;
                if (url == -1) {
                    this.showExitDialog = true;
                } else {
                    turnToNextPage(url);
                }
            },
            /**
             * 去提现
             */
            handleCheckMoney(){
                if (!this.message.nickName) {
                    turnToNextPage('/wap/merchant/home/manage/binding.html');
                } else if (this.message.haveCheck) {
                    this.status = 2;
                    this.showHintDialog = true;
                } else if (this.moneyDatas.canCheckMoney <= this.message.minCheckMoney) {
                    this.status = 1;
                    this.showHintDialog = true;
                } else {
                    turnToNextPage('/wap/merchant/home/manage/withdraw.html');
                }
            },
            /**
             * 去绑定
             */
            handleBinding(){
                turnToNextPage('/wap/merchant/home/manage/binding.html');
            },
            /**
             * 提现记录
             */
            handleGoRecord(){
                turnToNextPage('/wap/merchant/home/manage/record.html');
            },
            /**
             * 钱格式的转换
             */
            handleChangeMoney(money){
                return moneyFormat(money);
            },
            /**
             * 退出
             */
            handleExit(){
                this.showExitDialog = false;
                this.$ajax(
                    this.$joggle.merchant.open.logout,
                    {},
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            turnToNextPage('/wap/merchant/open/login.html');
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
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                this.selectMoneyDatas(() => {
                    resolve();
                }, loading);
            });
            const f2 = new Promise((resolve) => {
                this.selectUserMessage(() => {
                    resolve();
                }, loading);
            });

            Promise.all([f1, f2]).then(() => {
                loading.close();
            });
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
