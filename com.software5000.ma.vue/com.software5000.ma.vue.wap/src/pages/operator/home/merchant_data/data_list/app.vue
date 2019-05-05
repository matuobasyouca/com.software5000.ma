<template>
    <div class="merchant-list-page" v-show="!isLoading">
        <ma-head :home="false" @left-icon-click="handleGoBack" right-icon-text="" @right-icon-click="">商家数据</ma-head>
        <div class="data-chose">
            <ul class="data-chose-tab">
                <li>
                    <label for="createTimeStart" class="time-label" @click="timeLabelClick('createTimeStart')">
                        <span :class="[{'date-have-value':createTimeStart}]">{{createTimeStart?createTimeStart:'请选择开始时间'}}</span>
                        <input type="date" id="createTimeStart" ref="createTimeStart" v-model="createTimeStart">
                    </label>
                </li>
                <li>
                    <label for="createTimeEnd" class="time-label" @click="timeLabelClick('createTimeEnd')">
                        <span :class="[{'date-have-value':createTimeEnd}]">{{createTimeEnd?createTimeEnd:'请选择结束时间'}}</span>
                        <input type="date" id="createTimeEnd" ref="createTimeEnd" v-model="createTimeEnd">
                    </label>
                </li>
            </ul>
        </div>

        <ul class="total-data">
            <li>
                <div>
                    <p>{{totalWorkOrderNum}}</p>
                    <p>工单数</p>
                </div>
            </li>
            <li>
                <div>
                    <p>{{totalWxPayNum}}</p>
                    <p>支付数</p>
                </div>
            </li>
            <li>
                <div>
                    <p>{{totalOrderRate}}</p>
                    <p>支付率</p>
                </div>
            </li>
        </ul>
        <ul class="merchant-list">
            <li v-for="paymentRateDto in paymentRateDtoList" :key="paymentRateDto.id">
                <div class="payment-div">
                    <div class="business-name">
                        <zs-icon icon="shang2" :text="paymentRateDto.businessName" ></zs-icon>
                    </div>
                    <div class="rate-css">
                        <ul class="rate-css-ul">
                            <li>
                                <div>
                                    <p class="work-num">{{paymentRateDto.workOrderNum}}</p>
                                    <p>工单数</p>
                                </div>
                            </li>
                            <li>
                                <div>
                                    <p class="pay-num">{{paymentRateDto.wxPayNum}}</p>
                                    <p>支付数</p>
                                </div>
                            </li>
                            <li>
                                <div>
                                    <p class="order-rate">{{paymentRateDto.orderRate}}</p>
                                    <p>支付率</p>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </li>
            <li class="list-empty" v-if="paymentRateDtoList.length==0">
                <img src="../../../../../assets/img/no_data.png" width="43px" height="43px">
                <p>暂无商家数据</p>
            </li>
        </ul>
        <p v-show="paymentRateDtoList.length>0" class="show-more" :class="[{'no-more': !hasNextPage}]" @click="handleShowMore">
            {{ hasNextPage ? '查看更多' : '已显示全部'}}</p>
    </div>
</template>
<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import pullMenu from '../../../../../components_proj/pull_menu/app.vue';
    import {turnToNextPage,isEmpty} from '../../../../../assets/js/utils';
    export default {
        components: {
            maHead,pullMenu
        },
        data(){
            return {
                //操作数据
                isLoading: false,
                startPage: 1,
                pageSize: 10,
                hasNextPage: false,
                createTimeStart: '',
                createTimeEnd: '',
                paymentRateDtoList: [],
                menuTab: false,
                totalWorkOrderNum:0,
                totalWxPayNum:0,
                totalOrderRate:0,
            }
        },
        watch: {
            createTimeStart(val){
                this.selectData();
            },
            createTimeEnd(val){
                this.selectData();
            }
        },
        methods: {
            //
            handleGoBack(){
                turnToNextPage('/wap/operator/home/workbench.html');
            },
            //搜索
            handleSearch(){
                this.getpaymentRateDtoList(false)
            },
            timeLabelClick(refVal){
                if(this[refVal]){
                    this[refVal]='';
                }
                this.$refs[refVal].focus();
            },
            //获取收支列表数据
            getpaymentRateDtoList(isShowMore, fn,loading){
                if (isShowMore) {
                    this.startPage++
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                        this.$joggle.operator.workorder.selectPagePaymentRateDto,
                        {
                            startPage: this.startPage,
                            pageSize: this.pageSize,
                            createTimeStart: isEmpty(this.createTimeStart)?this.createTimeStart:(this.createTimeStart+" 00:00:00"),
                            createTimeEnd: isEmpty(this.createTimeEnd)?this.createTimeEnd:(this.createTimeEnd+" 23:59:59")
                        },
                        loading,
                        (data, loading)=> {
                        if (data.code === 'ZS011000') {
                                this.hasNextPage = data.data.hasNextPage;

                                if (isShowMore) {
                                    this.paymentRateDtoList = this.paymentRateDtoList.concat(data.data.list)
                                } else {
                                    this.paymentRateDtoList = data.data.list;
                                }
                                fn && fn()
                            } else {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                })
                            }
                        }
                )
            },
            selectWorkOrderPaymentRate(fn,loading){
                this.$ajax(
                    this.$joggle.operator.workorder.selectWorkOrderPaymentRate,
                    {
                        createTimeStart: isEmpty(this.createTimeStart)?this.createTimeStart:(this.createTimeStart+" 00:00:00"),
                        createTimeEnd: isEmpty(this.createTimeEnd)?this.createTimeEnd:(this.createTimeEnd+" 23:59:59")
                    },
                    loading,
                    (data, loading)=> {
                        if (data.code === 'ZS011000') {
                            this.totalWorkOrderNum=data.data.workOrderNum;
                            this.totalWxPayNum=data.data.wxPayNum;
                            this.totalOrderRate=data.data.orderRate;
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                )
            },
            //显示更多
            handleShowMore(){
                if (!this.hasNextPage)return;
                this.getpaymentRateDtoList(true);
            },
            //跳转导出数据
            selectDownloadBusinessData(){
                turnToNextPage('/home/workOrder/selectDownloadBusinessData?createTimeStart='+this.createTimeStart+'&createTimeEnd='+this.createTimeEnd)
            },
            selectData(){
                const loading = this.$loading();
                const f1 = new Promise((resolve) => {
                    this.getpaymentRateDtoList(false,() => {
                        resolve();
                    },loading)
                });
                const f2 = new Promise((resolve) => {
                    this.selectWorkOrderPaymentRate(() => {
                        resolve();
                    },loading)
                });
                Promise.all([f1, f2]).then(() => {
                    loading.close();
                })
            }
        },
        created(){
            this.selectData();
        }
    }
</script>
<style lang="less">
    @import "data_list.less";
</style>