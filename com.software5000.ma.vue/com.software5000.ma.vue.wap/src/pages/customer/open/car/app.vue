<template>
    <div class="insert-carnumber">
        <h2 class="title">
            <div class="plate-number__arrow-left" @click="closeModal">
                <zs-icon icon="arrow-left" :size="12"></zs-icon>
            </div>
            添加车牌号
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
                    <div class="save-add">
                        <zs-button @click="addCarNum" slot="left" type="primary" size="large">添加</zs-button>
                    </div>
                </div>
                <list-wrap class="create__item">
                    <list :height="65" v-for="(item,index) in carList" :key="index">
                        <div class="create__item__plate-number" slot="left"><span>{{item.carNumber.substring(0,2)}} {{item.carNumber.substring(2)}}</span>
                        </div>
                        <div slot="right" @click="delCarNum(index,item)">
                            <zs-icon icon="cross" :size="16"></zs-icon>
                        </div>
                    </list>
                </list-wrap>
            </div>
        </div>

    </div>
</template>

<script>
    import list from '../../../../components_proj/list/index';
    import listWrap from '../../../../components_proj/list_wrap/index';
    import carNumber from '../../../../components_proj/car_number/app.vue';
    import {isCarNum, getDataFromParam, turnToNextPage} from '../../../../assets/js/utils';
    import {turnToHostPage} from '../../../../assets/js/turnToHostPage';
    import Base64 from '../../../../assets/js/base64.min';
    export default {
        components: {
            listWrap, list, carNumber
        },
        data () {
            return {
                carNumber: '',
                carList: [],
                clearNum: false,
            }
        },
        computed: {},
        watch: {},
        methods: {
            //获取该openId数据
            getData(){
                this.$ajax(
                    this.$joggle.customer.car.selectCarListByOpenId,
                    JSON.stringify({wxOpenId: this.openId}),
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.carList = data.data ? data.data : "";
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    });
            },
            closeModal(){
                turnToHostPage('/open/user_edit.html', 'emkt', {i: Base64.encode(this.openId)});
            },
            //获取该车牌号
            handleGetCarNumber(carNumber){
                this.carNumber = carNumber;
            },
            //删除车辆
            delCarNum(index, item){
                if(this.carList.length==1){
                    this.$message({
                        type: 'error',
                        message: '至少需绑定1辆车。'
                    });
                    return;
                }
                this.$confirm({
                    message: '是否删除该车牌号？'
                }).then(() => {
                    let sendData = {
                        id: item.id,
                        carNumber: item.carNumber
                    };
                    this.$ajax(
                        this.$joggle.customer.car.deleteUserCar,
                        JSON.stringify(sendData),
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.carList.splice(index, 1);
                                this.$message({
                                    type: 'success',
                                    message: '删除成功'
                                });
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        });

                }).catch(() => {

                })
            },
            //添加车辆
            addCarNum(){
                if (!isCarNum(this.carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '请完善车牌号'
                    });
                    return;
                }
                if (this.carList.length > 9) {
                    this.$message({
                        type: 'error',
                        message: '最多添加10辆车'
                    });
                    return;
                }
                for (let i = 0; i < this.carList.length; i++) {
                    if (this.carList[i].carNumber == this.carNumber) {
                        this.$message({
                            type: 'error',
                            message: '该车已添加'
                        });
                        this.$refs.refCarNumber.currNum = '';
                        return;
                    }
                }

                let sendData = {
                    wxOpenId: this.openId,
                    carNumber: this.carNumber
                };
                this.$ajax(
                    this.$joggle.customer.car.insertUserCar,
                    JSON.stringify(sendData),
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            if (data.data) {
                                this.carList = this.carList.concat(data.data);
                                this.$message({
                                    type: 'success',
                                    message: '添加成功'
                                });
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: "该车牌号已绑定"
                                });
                            }
                            this.$refs.refCarNumber.currNum = '';

                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    });

            },

        },
        mounted() {
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            if (this.openId) {
                this.getData();
            } else {
                turnToHostPage('/open/user_center.html', 'emkt');
            }
        }
    }
</script>
<style lang="less">
    @import './style.less';
</style>