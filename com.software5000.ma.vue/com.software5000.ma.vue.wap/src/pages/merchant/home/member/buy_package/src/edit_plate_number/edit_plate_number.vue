<template>
    <transition name="page-slide">
        <div class="edit-box" v-if="value">
            <h2 class="title">
                <div class="plate-number__arrow-left" @click="closeModal"><zs-icon icon="arrow-left" :size="12"></zs-icon></div>添加车牌号
            </h2>
            <div class="province_wrap">
                <div class="modalForCarLicence-ul clr">
                    <div class="">
                        <car-number
                                    ref="refCarNumber"
                                    photo-icon
                                    @get-car-number="handleGetCarNumber"
                                    :clearNum="clearNum"
                        ></car-number>
                        <div>
                            <div class="save-add">
                                <zs-button @click="addCarNum" slot="left"  type="primary" size="large">添加</zs-button>
                            </div>
                        </div>

                    </div>
                    <ul class="create__item">
                        <li :height="65" v-for="(item,index) in carList" :key="index">
                            <div class="create__item__plate-number"><span>{{item.carNumber.substring(0,2)}} {{item.carNumber.substring(2)}}</span></div>
                            <div class="create__item-right" @click="delCarNum(index,item)"><zs-icon icon="cross" ></zs-icon></div>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
    </transition>
</template>

<script>
    import list from '../../../../../../../components_proj/list/index';
    import listWrap from '../../../../../../../components_proj/list_wrap/index';
    import carNumber from '../../../../../../../components_proj/car_number/app';
    import { isCarNum } from '../../../../../../../assets/js/utils';

    export default {
        name: 'add_plate_number',
        //将导入的vue生成  CarList
        components : {
            listWrap,list,carNumber
        },
        props: {
            carNumList : {
                type: Array,
                default: []
            },
            showModel : {
                type: Boolean,
                default: false
            },
            mobile : [String,Number],
            value:Boolean,
            userId : [String,Number]
        },
        data () {
            return {
                carNumber : "",
                carList : [],
                clearNum : false
            }
        },
        computed: {

        },
        watch: {
            carNumList(val){
                this.carList=val;
            },
            carList(val){
                this.$emit("getCarNumList",val);
            }
        },
        methods: {
            closeModal(){
                this.$emit("input",false);
            },
            handleGetCarNumber(carNumber){
                this.carNumber = carNumber;
            },
            delCarNum(index,item){
                this.carList.splice(index,1);
/*                let sendData={
                    id : item.id,
                    carNumber : item.carNumber
                };
                this.$ajax(
                    this.$joggle.deleteCar,
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
                    });*/
            },
            addCarNum(){
                if(!isCarNum(this.carNumber)){
                    this.$message({
                        type:'error',
                        message:'请完善车牌号',
                        duration:'1000'
                    });
                    return;
                }
                if(this.carList.length>9){
                    this.$message({
                        type:'error',
                        message:'最多添加10辆车',
                        duration:'1000'
                    });
                    return;
                }
                for (let i = 0; i < this.carList.length; i++) {
                    if(this.carList[i].carNumber==this.carNumber){
                        this.$message({
                            type:'error',
                            message:'该车已添加',
                            duration:'1000'
                        });
                        this.$refs.refCarNumber.currNum='';
                        return;
                    }
                }
                let sendData={
                    carNumber : this.carNumber,
                    mobile: this.mobile
                };

                this.$ajax(
                    this.$joggle.merchant.member.selectCarNumber,
                    sendData,
                    true,
                    (data,model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            this.carList=this.carList.concat(sendData);
                            this.$refs.refCarNumber.currNum='';
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                )


            }
        },
        mounted() {
        }
    }
</script>
<style lang="less">
    @import 'edit_plate_number.less';
</style>