<template>
    <div class="financial-accounts">
        <div class="account">
            <div v-if="message.nickName" class="account-already">
                <zs-icon class="success" icon="circle-success" :size="18"></zs-icon>
                <span class="p">该账户绑定微信昵称：{{message.nickName}}</span>
            </div>
            <div v-else class="account-already">
                <zs-icon class="danger" icon="danger" :size="18"></zs-icon>
                <span class="p">该账户还未绑定提现微信昵称</span>
                <span class="go" @click="handleBinding">去绑定 ></span>
            </div>
        </div>
        <div class="account-header">
            <div class="can-reflect">
                <div class="can-t">可提现金额（元）</div>
                <em class="can-m">{{moneyChange(moneyDatas.canCheckMoney)}}</em>
                <p class="can-p">每日只可提现一次，1≤提现金额≤2万元</p>
                <div class="can-b" @click="checkMoney">提现
                </div>
            </div>
            <div class="list-already">
                <div class="list-img"></div>
                <div class="list-t">已提现金额（元）</div>
                <div class="list-m">{{moneyChange(moneyDatas.haveCheckMoney)}}</div>
            </div>
        </div>
        <div class="account-table">
            <div class="table-select">
                <p class="title">财务明细</p>
                <div class="getTime">
                    <date-picker
                            class="select-new-time"
                            v-model="startTime"
                            placeholder="请选择时间"
                            :disabledDate="-1"
                            :value="startTime"
                            @on-change="handleSetStartTime"
                    ></date-picker>
                    <span class="search_label">至</span>
                    <date-picker
                            class="select-new-time"
                            v-model="endTime"
                            placeholder="请选择时间"
                            :disabledDate="-1"
                            :value="endTime"
                            @on-change="handleSetEndTime"
                    ></date-picker>
                    <div class="search-btn" @click="handleSearchMsg">
                        <zs-icon icon="search2" size="20"></zs-icon>
                    </div>
                </div>

            </div>
            <div class="list-content">
                <zs-table
                        :data="formDatas"
                        :columns="columns"
                        :context="context"
                        :no-data-colspan="3"
                        border
                        class="employee-table"
                >
                </zs-table>
            </div>
            <div class="page-div" v-if="formDatas.length>0">
                <zs-pagination
                        :current="formCondition.startPage"
                        :total="total"
                        :page-size="formCondition.pageSize"
                        show-total
                        show-elevator
                        show-sizer
                        @on-change="handlePageNum"
                        @on-page-size-change="handlePageSize"
                ></zs-pagination>
            </div>
        </div>
        <zs-dialog
                class="em-dialog"
                v-model="bindingDialog"
                show-close>
            <p slot="title">提现</p>
            <div class="dialog-body">
                <p class="p1">您账户还未绑定微信，请用提现微信进行扫码绑定！</p>
                <p class="p2">绑定后，提现金额将转入该 <em>微信零钱</em></p>
                <div class="QR-code" ref="barCodeWrap">
                    <img class="barcode-loading" src="../../../../../../../assets/img/three-dots.svg">
                </div>
            </div>
        </zs-dialog>
        <zs-dialog
                class="give-dialog"
                v-model="incarnateDialog"
                show-close>
            <p slot="title">绑定提现微信</p>
            <div class="dialog-body">
                <div class="time" v-if="step == 1">
                    <p class="p1">抱歉，每日只能提现一次！</p>
                    <p class="p2">您今日已提现，请明日再进行提现</p>
                </div>
                <div class="short" v-else-if="step ==2">
                    <div class="num">可提现金额 <em>￥{{moneyDatas.canCheckMoney/100}}</em></div>
                    <p class="p1">抱歉，由于微信提现限额限制，每笔提现最低为1元，</p>
                    <p class="p1">您可提现余额不足，无法提现。</p>
                </div>
                <div class="can" v-else-if="step == 3">
                    <div class="num"><span>可提现金额</span> <em>￥{{moneyDatas.canCheckMoney/100}}</em></div>
                    <div class="num"><span>到账微信昵称</span> <em>{{message.nickName}}</em></div>
                    <div class="num"><span>到账金额</span> <em>{{moneyDatas.canCheckMoney/100>20000 ? '20000.00' :
                        moneyDatas.canCheckMoney/100}}</em>
                        <p v-if="moneyDatas.canCheckMoney/100>20000">每日提现上限为2万元</p></div>
                </div>
            </div>
            <template slot="footer">
                <zs-button
                        v-if="step == 3"
                        type="cancel"
                        @click="incarnateDialog=false;">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="handleCheckMoney">{{step == 3 ? '确定提现' : '确认'}}
                </zs-button>
            </template>
        </zs-dialog>

    </div>
</template>

<script>
    import { isEmpty, DateUtils, imgPreLoad } from '../../../../../../../assets/js/utils';
    import { appointUrl } from '../../../../../../../assets/js/turnToHostPage';
    export default{
        name: 'list',
        components: {},
        props: {},
        data(){
            return {
                context: this,
                total: 0,
                timer: null,
                columns: [
                    {
                        title: '提现时间',
                        key: 'createTime',
                        align: 'center',
                        width: 490
                    },
                    {
                        title: '提现金额',
                        key: 'checkMoneyFee',
                        align: 'center',
                        width: 490,
                        render(row){
                            let money = `${row.checkMoneyFee / 100}`;

                            return row.checkMoneyFee ? money : 0;
                        }
                    }
                ],
                bindingDialog: false,
                incarnateDialog: false,
                step: 1,
                moneyDatas: [],
                formCondition: {
                    orderBy: 'id desc',
                    startPage: 1,
                    pageSize: 10,
                    startTime: '',
                    endTime: ''
                },
                formDatas: [],
                message: [],
                startTime: '',
                endTime: ''
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
             * 获取可提现金额和已体现金额信息
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
             * 获取提现记录列表信息
             * @param fn
             * @param loading
             */
            selectFormDatas(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.finance.selectPageWechatPayOrder,
                    this.formCondition,
                    loading,
                    (data, loading) => {
                        if (data.code == 'ZS011000') {
                            this.formDatas = data.data.list;
                            this.total = data.data.total;
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
             * 获取用户信息
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
             * 确认提现
             */
            handleCheckMoney(){
                this.incarnateDialog = false;
                if (this.step != 3)return;
                let num = this.moneyDatas.canCheckMoney / 100 > 20000 ? '2000000' : this.moneyDatas.canCheckMoney;

                this.$ajax(
                    this.$joggle.merchant.finance.insertDrawMoney,
                    { money: num },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.handleUpload();
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
            /**
             * 搜索
             */
            handleSearchMsg(){
                let begin = DateUtils.dateToRegular(this.startTime).getTime();
                let end = DateUtils.dateToRegular(this.endTime).getTime();

                if (end < begin) {
                    this.$message({
                        type: 'error',
                        message: '浏览结束时间不能大于开始时间'
                    });
                } else {
                    this.formCondition.startPage = 1;
                    this.formCondition.startTime = this.startTime;
                    this.formCondition.endTime = this.endTime;
                    const loading = this.$loading();

                    this.selectFormDatas(() => {
                        loading.close();
                    }, loading);
                }
            },
            /**
             * 绑定微信
             */
            handleBinding(){
                this.bindingDialog = true;
                this.handleShowBarCode();
                clearInterval(this.timer);
                this.timer = setInterval(() => {
                    this.selectUserMessage(() => {
                        if (!isEmpty(this.message.nickName)) {
                            this.bindingDialog = false;
                            clearInterval(this.timer);
                        }
                        if (this.bindingDialog === false) {
                            clearInterval(this.timer);
                        }
                    }, false);
                }, 2000);
            },
            /**
             * 显示二维码
             */
            handleShowBarCode(){
                const url = `${appointUrl.ma}/wap/merchant/open/login.html?type=1&t=${new Date().getTime()}`;

                imgPreLoad(
                    `${appointUrl.emkt}/open/coupons/selectQr?type=&url=${url}`,
                    (img) => {
                        this.$refs.barCodeWrap.innerHTML = '';
                        this.$refs.barCodeWrap.appendChild(img);
                    });
            },
            /**
             * 开始时间
             * @param time
             */
            handleSetStartTime(time){
                this.startTime = time ? time + ' 00:00:00' : '';
            },
            /**
             * 开始时间
             * @param time
             */
            handleSetEndTime(time){
                this.endTime = time ? time + ' 23:59:59' : '';
            },
            /**
             * 提现
             */
            checkMoney(){
                if (!this.message.nickName) {
                    this.handleBinding();
                } else if (this.message.haveCheck) {
                    this.step = 1;
                    this.incarnateDialog = true;
                } else if (this.moneyDatas.canCheckMoney <= this.message.minCheckMoney) {
                    this.step = 2;
                    this.incarnateDialog = true;
                } else {
                    this.step = 3;
                    this.incarnateDialog = true;
                }
            },
            /**
             * 钱格式转化，辅助操作
             */
            moneyChange(money){
                if (isEmpty(money))return 0;
                let previous = (money / 100).toString().split('.');
                let len = previous[0].length;
                let lastIndex = 0;
                let arr = [];

                while (len > 0) {
                    lastIndex = len;
                    len -= 3;
                    arr.unshift(previous[0].substring(len, lastIndex));
                }
                return previous[1] ? arr.join(',') + '.' + previous[1] : arr.join(',');
            },
            /**
             * 切换页码
             * @param pageNum
             */
            handlePageNum(pageNum){
                this.formCondition.startPage = pageNum;
                this.startTime = this.formCondition.startTime;
                this.endTime = this.formCondition.endTime;
                const loading = this.$loading();

                this.selectFormDatas(() => {
                    loading.close();
                }, loading);
            },
            /**
             * 切换当前页的数量
             * @param pageSize
             */
            handlePageSize(pageSize){
                this.formCondition.startPage = 1;
                this.formCondition.pageSize = pageSize;
                this.startTime = this.formCondition.startTime;
                this.endTime = this.formCondition.endTime;
                const loading = this.$loading();

                this.selectFormDatas(() => {
                    loading.close();
                }, loading);
            },
            /**
             * 辅助操作
             */
            handleUpload(){
                const loading = this.$loading();
                const f1 = new Promise((resolve) => {
                    this.selectMoneyDatas(() => {
                        resolve();
                    }, loading);
                });
                const f2 = new Promise((resolve) => {
                    this.selectFormDatas(() => {
                        resolve();
                    }, loading);
                });
                const f3 = new Promise((resolve) => {
                    this.selectUserMessage(() => {
                        resolve();
                    }, loading);
                });

                Promise.all([f1, f2, f3]).then(() => {
                    loading.close();
                });
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.handleUpload();
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>