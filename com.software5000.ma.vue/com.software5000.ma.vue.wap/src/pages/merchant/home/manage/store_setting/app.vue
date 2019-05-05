<template>
    <div class="store-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">店铺设置</ma-head>
        <div class="store-body">
            <div class="store-logo" :style="`background-image: url(${datas.imageShowPath || ''})`"></div>
            <div class="store-msg">
                <div class="store-name"><zs-icon icon="shang" :size="15"></zs-icon>{{datas.businessName}}</div>
                <div class="store-addr"><zs-icon icon="location" :size="15"></zs-icon> {{`${datas.businessAreaCodeDes}${datas.businessAddress}`}}</div>
                <div class="store-tel"><zs-icon icon="call" :size="15"></zs-icon>{{datas.businessTel}}</div>
            </div>
            <div class="page-title">关账日设置</div>
            <div class="day-select">
                <span>关账日</span>
                <select class="li-select" v-model="day" @change="saveSetting">
                    <option v-for="(it,index) in arrDay" :value="index+1" :key="index">{{index+1}}</option>
                </select>
                <zs-icon icon="arrow-right" size="12"></zs-icon>
            </div>
        </div>
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
                day: 28,
                datas: [],
                arrDay: []
            }
        },
        methods: {
            getBusiness(){
                this.$ajax(
                        this.$joggle.business.selectBusinessInfo,
                        {},
                        true,
                        (data,loading)=>{
                            loading.close();
                            if(data.code == 'ZS011000'){
                                this.datas = data.data;
                                this.day = data.data.closingDateNum ? data.data.closingDateNum : 28;
                            }else {
                                this.$message({
                                    type : 'error',
                                    duration : 1200,
                                    message : data.msg
                                })
                            }
                        }
                )
            },
            saveSetting(){
                this.$ajax(
                        this.$joggle.business.updateBusiness,
                        {
                            id: this.datas.id,
                            closingDateNum: this.day
                        },
                        true,
                        (data,loading)=>{
                            loading.close();
                            if(data.code == 'ZS011000'){
                                this.$message({
                                    type : 'success',
                                    duration : 1200,
                                    message : data.msg
                                })
                            }else {
                                this.$message({
                                    type : 'error',
                                    duration : 1200,
                                    message : data.msg
                                })
                            }
                        }
                )
            },
            //返回首页
            handleGoHome(){turnToNextPage('/wap/merchant/home/manage/index.html');},
        },
        created(){
            this.arrDay.length = 28;

            this.getBusiness();
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
