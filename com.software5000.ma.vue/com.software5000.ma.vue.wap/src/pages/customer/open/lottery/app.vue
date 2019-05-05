<template>
    <div class="lottery-page" v-show="!isLoading">
        <div class="lottery-head">
            支付成功<span @click="jumpToNext">跳过</span>
        </div>
        <img class="lottery-title" src="./assets/title.png">
        <div class="lottery-roll-wrap">
            <div class="background" :class="[{'bg2':bgToggle}]">
                <ul class="list-wrap">
                    <li
                            v-for="prize in list"
                            :key="prize.i"
                            @click="handleStart(prize.i)">
                        <div
                                v-if="prize.i != -1"
                                class="image"
                                :class="[{'active':prize.i == index,'modal':hasClick}]"
                                :style="{'backgroundImage':`url('${prize.img}')`}"></div>
                        <p
                                :class="[{'start-control':prize.i == -1,'on-press':onTouch}]"
                                v-else
                                @touchstart="!hasClick && (onTouch = true)"
                                @touchend="onTouch = false">
                            <span>(共{{remain}}次机会)</span>
                        </p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="lottery-instruction-wrap">
            <div class="lottery-instruction">
                <p class="instruction-title"></p>
                <ul class="instruction-list">
                    <li>1.您可点击抽奖进行抽奖</li>
                    <li>2.一二三等奖以优惠券形式发放到您账户</li>
                    <li>3.四五等奖请与店铺老板进行兑换</li>
                    <li>4.点击跳过表示您放弃本次抽奖</li>
                </ul>
            </div>
        </div>
        <!--实物弹窗-->
        <zs-dialog
                class="prize-pop"
                :show-close="false"
                v-model="prizePop1"
                :close-on-click-modal="false">
            <p slot="title">恭喜您，您抽中{{ setPrize().name }}</p>
            <div class="prize-pop-show"
                 :style="{'backgroundImage':`url(${setPrize().show})`}"></div>
            <p class="prize-pop-notice">请与店铺老板兑换奖品</p>
            <p class="prize-pop-notice1">注：将此页面提供给老板核实奖品</p>
            <div class="prize-confirm-btn" @click="jumpToNext">确定</div>
        </zs-dialog>
        <!--卡券弹窗-->
        <zs-dialog
                class="prize-pop prize-coupon-pop"
                :show-close="false"
                :close-on-click-modal="false"
                v-model="prizePop2">
            <p slot="title">恭喜您，您抽中{{ setPrize().name }}</p>
            <zs-input
                    class="lottery-mobile"
                    v-model="mobile"
                    type="tel"
                    :maxlength="11"
                    placeholder="请输入手机号码"
                    icon="circle-cross"
            ></zs-input>
            <car-number
                    @get-car-number="handleGetCarNumber"
                    :carProvince="setCarProvince"
                    :carLetter='setCarLetter'
                    :carNum="setCarNum"></car-number>
            <div class="prize-confirm-btn" @click="handleInfoConfirm">确定</div>
        </zs-dialog>
    </div>
</template>

<script>
    import carNumber from '../../../../components_proj/car_number/app.vue';
    import {isMobile, isCarNum, getDataFromParam, isEmpty, turnToNextPage} from '../../../../assets/js/utils';
    import {turnToHostPage} from '../../../../assets/js/turnToHostPage';
    export default{
        components: {
            carNumber
        },
        data(){
            return {
                index: -1,//当前位置转动到的位置
                count: 8,//总共有多少位置
                timer: null,//定时器
                speed: 20,//初始转动速度
                times: 0,//已经转动的次数
                cycle: 50,//至少转动的次数
                prize: -1,//中奖的位置
                realPrize: '',//后台获取的中奖位置
                remain: 0,//剩余次数
                hasClick: false,//是否已经点击
                list: [
                    {i: 0, img: require('./assets/0.png'), show: require('./assets/0_.jpg'), name: '免费清洗空调一次'},
                    {i: 1, img: require('./assets/1.png'), show: require('./assets/1_.jpg'), name: '手机支架'},
                    {i: 2, img: require('./assets/2.png'), show: require('./assets/2_.jpg'), name: '10元现金券'},
                    {i: 7, img: require('./assets/7.png'), show: require('./assets/7_.jpg'), name: '二维码停车贴'},
                    {i: -1},
                    {i: 3, img: require('./assets/3.png'), show: require('./assets/3_.jpg'), name: '免费洗车一次'},
                    {i: 6, img: require('./assets/6.png'), show: require('./assets/6_.jpg'), name: '10元现金券'},
                    {i: 5, img: require('./assets/5.png'), show: require('./assets/5_.jpg'), name: '手机支架'},
                    {i: 4, img: require('./assets/4.png'), show: require('./assets/4_.jpg'), name: '二维码停车贴'}
                ],
                prizeIdMap: {
                    0: [0],
                    1: [3],
                    2: [2, 6],
                    3: [1, 5],
                    4: [4, 7]
                },//奖品id对应的位置
                bgToggle: false,//灯光闪烁
                onTouch: false,//控制点击按钮效果
                prizePop1: false,//实物奖品弹窗
                prizePop2: false,//卡券奖品弹窗
                mobile: '',//用户的手机号码
                carNumber: '',//用户车牌号码
                tradeNo: '',//订单号码
                isLoading: true
            }
        },
        computed: {
            setCarProvince(){
                return this.carNumber[0] || '';
            },
            setCarLetter(){
                return this.carNumber[1] || ''
            },
            setCarNum(){
                return isEmpty(this.carNumber) ? '' : this.carNumber.slice(2)
            }
        },
        watch: {
            prizePop1(val){
                this.hasClick = val;
            },
            prizePop2(val){
                this.hasClick = val;
            }
        },
        methods: {
            getList(r){
                return this[`list${r}`];
            },
            setActive(){
                this.index++;
                if (this.index > this.count - 1) {
                    this.index = 0;
                }
            },
            roll(){
                this.times++;
                this.setActive();
                if (this.times > this.cycle + 10 && this.prize == this.index) {
                    clearTimeout(this.timer);
                    this.timer = null;
                    this.speed = 20;
                    this.prize = -1;
                    this.times = 0;
                    setTimeout(this.handleNotice, 600)
                } else {
                    if (this.times < this.cycle) {
                        this.speed -= 10
                    } else if (this.times == this.cycle) {
                        this.prize = this.realPrize;
                    } else {
                        if (this.times > this.cycle + 10 &&
                            ((this.prize == 0 && this.index == 4) || (this.prize == this.index + 1))) {
                            this.speed += 110;
                        } else {
                            this.speed += 20;
                        }
                    }
                    if (this.speed < 40) {
                        this.speed = 40;
                    }
                    this.timer = setTimeout(this.roll, this.speed);
                }
            },
            handleStart(i){
                if (i > -1 || this.remain < 1 || this.hasClick) return;

                this.getRealPrize(() => {
                    this.remain--;
                    this.speed = 150;
                    this.hasClick = true
                    this.roll();
                })

            },
            //跳过
            jumpToNext(){
                this.prizePop1 = false;
                if (this.subscribe == 0) {
                    turnToNextPage('/wap/customer/open/scan.html')
                } else {
                    turnToHostPage('/open/user_center.html', 'emkt')
                }
            },
            //获取抽奖次数、车牌、手机号码
            selectTimes(fn){
                this.$ajax(
                    this.$joggle.customer.lottery.selectLotteryTimes,
                    {id: this.payOrderId},
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            if (isEmpty(data.data)) {
                                this.remain = 0;
                                return;
                            }
                            this.remain = data.data.lotteryTimes;
                            this.carNumber = isEmpty(data.data.carNumber) ? '' : data.data.carNumber;
                            this.mobile = isEmpty(data.data.mobile) ? '' : data.data.mobile;
                            fn && fn(data.data);
                        } else {
                            this.$message({
                                type: 'error',
                                message: '操作失败'
                            })
                        }
                    }
                )
            },
            //向后台获取奖品ID
            getRealPrize(fn){
                this.$ajax(
                    this.$joggle.customer.lottery.selectPrize,
                    {
                        id: this.payOrderId,
                        mobile: this.mobile,
                        carNumber: this.carNumber
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            let prizeId = data.data;
                            this.realPrize = this.prizeIdMap[prizeId].sort(() => {
                                return Math.random() - 0.5
                            })[0];
                            fn && fn();
                        } else {
                            this.$messsage({
                                type: 'error',
                                message: '操作失败'
                            })
                        }
                    }
                )
            },
            //中奖结果提醒
            handleNotice(){
                if ([1, 4, 5, 7].indexOf(this.realPrize) > -1) {
                    //实物奖品
                    this.prizePop1 = true;
                } else if ([0, 2, 3, 6].indexOf(this.realPrize) > -1) {
                    //卡券奖品
                    this.prizePop2 = true;
                }
            },
            //设置提示的奖品名
            setPrize(){
                for (let i = 0; i < this.list.length; i++) {
                    if (this.list[i].i == this.realPrize) {
                        return {
                            name: this.list[i].name,
                            show: this.list[i].show
                        }
                    }
                }
                return {}
            },
            //获取车牌
            handleGetCarNumber(carNum){
                this.carNumber = carNum
            },
            //确认用户信息
            handleInfoConfirm(){
                if (!isMobile(this.mobile)) {
                    this.$message({
                        type: 'error',
                        message: '手机号码有误!'
                    })
                    return;
                } else if (!isCarNum(this.carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '车牌号码有误!'
                    })
                    return;
                }
                this.$ajax(
                    this.$joggle.customer.lottery.CouponinsertUsed,
                    {id: this.payOrderId, carNumber: this.carNumber, mobile: this.mobile},
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: '领取成功'
                            })
                            this.prizePop2 = false
                            this.jumpToNext();
                        } else {
                            this.$message({
                                type: 'error',
                                message: '操作失败'
                            })
                        }
                    }
                )
            }
        },
        created(){
            this.payOrderId = getDataFromParam('id') || '';
            this.subscribe = getDataFromParam('subscribe') || '';
            this.selectTimes((data) => {
                if (data.receiveState == 1) {
                    this.realPrize = this.prizeIdMap[data.prizeNum].sort(() => {
                        return Math.random() - 0.5
                    })[0];
                    this.handleNotice();
                }
                this.isLoading = false;
            })
            setInterval(() => {
                this.bgToggle = !this.bgToggle;
            }, 300)
        }
    }
</script>

<style lang="less">
    @import "./style.less";
</style>