<template>
    <div class="workorder-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">添加车牌号</ma-head>
        <div class="car-number-show" v-if="isEdit">
            <zs-icon icon="car" size="20" text="服务车辆"></zs-icon>
            {{ this.finalData.carNumber }}
        </div>
        <car-number v-else photo-icon @get-car-number="handleGetCarNumber"></car-number>
        <div>
            <div class="save-add">
                <zs-button @click="addCarNum" slot="left"  type="primary" size="large">添加</zs-button>
            </div>
        </div>
        <div class="div_item">
            <list :height="65" v-for="(item,index) in carList" :key="index">
                <div class="create__item__plate-number" slot="left"><span>{{item.carNumber.substring(0,2)}} {{item.carNumber.substring(2)}}</span></div>
                <div slot="right" @click="delCarNum(index,item)"><zs-icon icon="cross" :size="12"></zs-icon></div>
            </list>
        </div>
    </div>
</template>

<script>
    import {turnToNextPage, getDataFromParam, isEmpty, isCarNum, isMobile, strim} from '../../../../../assets/js/utils';
    import {defaultInfo} from '../../../../../assets/js/defaultInfo';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import carNumber from '../../../../../components_proj/car_number/app.vue';
    import list from '../../../../../components_proj/list/index';
    export default {
        components: {
            maHead,
            carNumber,
            list
        },
        data(){
            return {
                isLoading: false,
                isEdit: false,
                carNumber:"",
                carList:[],
                userId:'',
                recordId:''
            }
        },
        computed: {
        },
        methods: {
            //返回首页
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/workorder/list.html');
            },
            //输入或获取车牌号码
            handleGetCarNumber(carNumber){
                this.carNumber = carNumber;
                if (carNumber.length < 7) return;
                if (!isCarNum(carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '车牌号码有误!'
                    })
                }
            },
            //根据会员等级记录id查询会员信息
            getMemberData(){
                this.$ajax(
                        this.$joggle.merchant.member.selectMemberDetailInfo,
                        {
                            'memberLvlRecordId': this.recordId,
                        },
                        true,
                        (data,model) => {
                            model.close();
                            if (data.code === 'ZS011000') {
                                this.userId=data.data.userId;
                                this.carList=data.data.user.cars;
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                })
                                setTimeout(()=>{
                                    turnToNextPage('/web/merchant/home/member/manage.html');
                                },1200);
                            }
                        }
                )

            },
            delCarNum(index,item){
                this.$confirm({
                    message:'是否删除该车牌号？'
                }).then(()=>{
                    if(this.carList.length<2){
                        this.$message({
                            type: 'error',
                            message: "至少需留1辆车",
                            duration: 1200
                        });
                        return;
                    }
                    let sendData={
                        userId : this.userId,
                        carNumber : item.carNumber
                    };
                    this.$ajax(
                            this.$joggle.merchant.member.updateCarStateByParam,
                            JSON.stringify(sendData),
                            true,
                            (data,model) => {
                                model.close();
                                if (data.code === 'ZS011000') {
                                    this.carList.splice(index,1);
                                    this.$modal({
                                        type:'success',
                                        message:'删除成功',
                                        duration:'1000'
                                    });
                                } else {
                                    this.$modal({
                                        type:'error',
                                        message: data.msg,
                                        duration: 1000
                                    });
                                }
                            });

                }).catch(()=>{

                })
            },
            addCarNum(){
                if(!isCarNum(this.carNumber)){
                    this.$modal({
                        type:'error',
                        message:'请完善车牌号',
                        duration:'1000'
                    });
                    return;
                }
                if(this.carList.length>9){
                    this.$modal({
                        type:'error',
                        message:'最多添加10辆车',
                        duration:'1000'
                    });
                    return;
                }
                for (let i = 0; i < this.carList.length; i++) {
                    if(this.carList[i]==this.carNumber){
                        this.$modal({
                            type:'error',
                            message:'该车已添加',
                            duration:'1000'
                        });
                        this.$refs.refCarNumber.currNum='';
                        return;
                    }
                }
                let sendData={
                    userId : this.userId,
                    carNumber : this.carNumber
                };
                this.$ajax(
                        this.$joggle.merchant.member.insertCarNumberByParam,
                        JSON.stringify(sendData),
                        true,
                        (data,model) => {
                            model.close();
                            if (data.code === 'ZS011000') {
                                if(data.data){
                                    this.carList=this.carList.concat(data.data);
                                    this.$modal({
                                        type:'success',
                                        message:'添加成功',
                                        duration:1000
                                    });
                                }else{
                                    this.$modal({
                                        type:'error',
                                        message: "该车牌号已绑定",
                                        duration: 1000
                                    });
                                }

                            } else {
                                this.$modal({
                                    type:'error',
                                    message: data.msg,
                                    duration: 1000
                                });
                            }
                        });

            }
        },
        created(){
            //获取记录ID
            this.recordId = getDataFromParam('recordId');
            //获取会员信息
            this.getMemberData();
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
