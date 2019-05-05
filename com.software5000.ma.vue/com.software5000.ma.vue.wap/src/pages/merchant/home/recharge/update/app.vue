<template>
    <div class="page-recharge">
        <ma-head
                right-icon-text="充值记录"
                @home-click="handleGoHome"
                @right-icon-click="handleTurnToRecord">会员充值
        </ma-head>
        <div class="recharge-sec-1">
            <div class="recharge-card">
                <p class="recharge-card-p1">
                    姓名<span class="user-name">{{ realName|| 'XXX' }}</span>
                    <span class="user-mobile right">{{ mobile || 'XXXXXXXXXXX' }}</span>
                </p>
                <p class="recharge-card-p2">
                    {{ reChargeMoney || 'XXXXXX' }}
                    <span class="right">{{ grantMoney || 'XXX' }}</span>
                </p>
                <p class="recharge-card-p3">
                    充值金额
                    <span class="right">赠送金额</span>
                </p>
                <p class="recharge-card-p4">
                    绑定车牌
                    <span class="car-number">{{ cars.join(',') || 'XXXXXXX' }}</span>
                </p>
            </div>
        </div>
        <div class="recharge-sec-2">
            <ul class="user-info">
                <li>
                    <zs-icon icon="mobile" text="手机号" size="16"></zs-icon>
                    <zs-input
                            @input="selectUserInfo"
                            v-model="mobile"
                            :maxlength="11"
                            placeholder="请输入手机号"></zs-input>
                </li>
                <li>
                    <zs-icon icon="user" text="姓名" size="16"></zs-icon>
                    <zs-input
                            v-model="realName"
                            :disabled="disableName"
                            placeholder="请输入姓名"></zs-input>
                </li>
                <li class="bind-car" :class="!isMember ? 'arrow-right' :  '' " @click="handleCarPop">
                    <zs-icon icon="car2" text="绑定车牌" size="16"></zs-icon>
                    <span :class="cars.length ? 'text-hide' : 'gray'">{{cars.join(',') || '请选择'}}</span>
                </li>
            </ul>
        </div>
        <div class="recharge-sec-3">
            <ul class="recharge-info">
                <li>
                    <zs-icon icon="recharge" text="充值金额" size="16"></zs-icon>
                    <zs-input
                            v-model="reChargeMoney"
                            type="number"
                            placeholder="请输入充值金额"></zs-input>
                </li>
                <li>
                    <zs-icon icon="largess" text="赠送金额" size="16"></zs-icon>
                    <zs-input
                            v-model="grantMoney"
                            type="number"
                            placeholder="请输入赠送金额"></zs-input>
                </li>
            </ul>
        </div>
        <zs-slide-page class="recharge-sec-4" v-model="bindingVisible" title="添加车牌">
            <car-number
                    photo-icon
                    ref="carPlate"
                    @get-car-number="handleGetCarNumber"
            ></car-number>
            <div class="binding-btn">
                <zs-button type="primary" @click="handleAddCarNumber">添加</zs-button>
            </div>
            <ul class="cars-wrapper">
                <li class="car-list" v-for="(car,index) in cars" :key="index">
                    <div class="car-plate">
                        <span class="car-plate-inner">{{car.substring(0,2)}} {{car.substring(2)}}</span>
                    </div>
                    <div class="car-delete-label" @click="handleDeleteCar(index)">
                        <zs-icon icon="cross"></zs-icon>
                    </div>
                </li>
            </ul>
        </zs-slide-page>
        <div class="recharge-sec-5" @click="updateRechargeOrder">提交</div>
    </div>
</template>

<script>
    import { isMobile, isCarNum, turnToNextPage, isEmpty } from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import carNumber from '../../../../../components_proj/car_number/app.vue';
    export default {
        components: {
            maHead,
            carNumber
        },
        props: {},
        data(){
            return {
                bindingVisible: false,
                singleCarNumber: '',
                isMember: false,
                disableName: false,
                //交互数据
                mobile: '',
                realName: '',
                cars: [],
                reChargeMoney: '',
                grantMoney: ''

            };
        },
        computed: {
            carsText(){

            }
        },
        watch: {},
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 提交充值
             */
            updateRechargeOrder(){
                const errorMap = {
                    mobile: '请输入手机号',
                    cars: '请绑定车牌',
                    reChargeMoney: '请输入充值金额'
                };

                for (let key in errorMap) {
                    if (isEmpty(this[key])) {
                        this.$message({
                            type: 'error',
                            message: errorMap[key],
                            druration: 1200
                        });
                        return;
                    }
                }

                this.$ajax(
                    this.$joggle.merchant.member.insertRechargeOrder,
                    {
                        carNums: this.cars.join(','),
                        mobile: this.mobile,
                        reChargeMoney: this.reChargeMoney,
                        grantMoney: this.grantMoney || 0,
                        realName: this.realName || ''
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg,
                                duration: 1200
                            });
                            setTimeout(() => {
                                turnToNextPage('/wap/merchant/home/recharge/detail.html', { id: data.data });
                            }, 1200);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 通过手机查询相关信息
             */
            selectUserInfo(){
                this.realName = '';
                this.cars = [];
                this.isMember = false;
                this.disableName = false;
                if (this.mobile.length === 11) {
                    if (isMobile(this.mobile)) {
                        this.$ajax(
                            this.$joggle.merchant.member.selectBusinessUserByParam,
                            {
                                startPage: 1,
                                pageSize: 8,
                                keyWord: this.mobile
                            },
                            true,
                            (data, loading) => {
                                loading.close();
                                if (data.code === 'ZS011000') {
                                    if (data.data.list.length > 0) {
                                        this.realName = data.data.list[0].realName;
                                        this.cars = data.data.list[0].carNumber.split(',');
                                        this.isMember = true;
                                        this.disableName = !isEmpty(this.realName) || !isEmpty(data.data.list[0].wxOpenId);
                                    }
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: data.msg,
                                        duration: 1200
                                    });
                                }
                            }
                        );
                    } else {
                        this.$message({
                            type: 'error',
                            message: '请输入正确的手机号码',
                            duration: 1200
                        });
                    }
                }
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 跳转到首页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 跳转到充值记录
             */
            handleTurnToRecord(){
                turnToNextPage('/wap/merchant/home/recharge/list.html');
            },
            /**
             * 显示车辆添加弹窗
             */
            handleCarPop(){
                if (this.isMember) return;
                if (isMobile(this.mobile)) {
                    this.bindingVisible = true;
                } else {
                    this.$message({
                        type: 'error',
                        message: '请输入正确的手机号码',
                        duration: 1200
                    });
                }
            },
            /**
             * 选择和输入车牌号码
             */
            handleGetCarNumber(carNumber){
                this.singleCarNumber = carNumber;
            },
            /**
             * 添加车牌号码
             */
            handleAddCarNumber(){
                if (this.singleCarNumber.length < 7) return;

                if (!isCarNum(this.singleCarNumber)) {
                    this.$message({
                        type: 'error',
                        message: '请完善车牌号',
                        duration: '1200'
                    });
                    return;
                }
                if (this.cars.length > 9) {
                    this.$message({
                        type: 'error',
                        message: '最多添加10辆车',
                        duration: '1200'
                    });
                    return;
                }
                for (let i = 0; i < this.cars.length; i++) {
                    if (this.cars[i] === this.singleCarNumber) {
                        this.$message({
                            type: 'error',
                            message: '该车已添加',
                            duration: '1200'
                        });
                        return;
                    }
                }

                this.$ajax(
                    this.$joggle.merchant.member.selectCarNumber,
                    {
                        carNumber: this.singleCarNumber,
                        mobile: this.mobile
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.cars.push(this.singleCarNumber);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                        this.$refs.carPlate.currNum = '';
                    }
                );
            },
            /**
             * 删除车牌号码
             */
            handleDeleteCar(index){
                this.$delete(this.cars, index);
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){

        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
