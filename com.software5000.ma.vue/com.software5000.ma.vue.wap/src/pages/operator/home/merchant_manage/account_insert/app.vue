<template>
    <div class="account_insert">
        <ma-header :home="false">账号管理</ma-header>
        <list-wrap>
            <list :height="50" :paddingLeft="94" :paddingRight="0">
                <template slot="left">
                    <span class="must-css">*</span>登录账号
                </template>
                <template>
                    <zs-input icon="circle-cross" placeholder="请输入登录账号" v-model="accountData.userName" ></zs-input>
                </template>
            </list>
            <list :height="50" :paddingLeft="110" :paddingRight="0">
                <template slot="left">
                    <span class="must-css">*</span>性别
                </template>
                <template>
                    <ul>
                        <li @click="accountData.sex='男'" class="man-css" :class="accountData.sex=='男'?'click-back-color':'noclick-back-color'"></li><li @click="accountData.sex='女'" class="femal-css" :class="accountData.sex!='男'?'click-back-color':'noclick-back-color'"></li>
                    </ul>
                </template>
            </list>
            <list :height="50" :paddingLeft="94" :paddingRight="0">
                <template slot="left">
                    <span class="must-css">*</span>联系电话
                </template>
                <template>
                    <zs-input icon="circle-cross" :maxlength="11"   placeholder="请输入电话号码" v-model="accountData.mobile" ></zs-input>
                </template>
            </list>
            <list :height="50" :paddingLeft="94" :paddingRight="0">
                <template slot="left">
                    <span class="must-css">*</span>密码
                </template>
                <template>
                    <zs-input icon="circle-cross" type="password"  :placeholder="accountData.id ? '为空不修改密码' : '请输入密码' " v-model="accountData.pwd" ></zs-input>
                </template>
            </list>
        </list-wrap>
        <div class="account_foot">
            <div class="save_button" @click="doSave">
                保存
            </div>
        </div>
    </div>
</template>
<script>
    import maHeader from '../../../../../components_proj/ma_head/app.vue';
    import listWrap from '../../../../../components_proj/list_wrap/index';
    import list from '../../../../../components_proj/list/index';
    import {
        turnToNextPage,
        getDataFromParam,
        isEmpty,
        isMobile
    } from '../../../../../assets/js/utils';
    export default {
        components:{
            maHeader,listWrap,list
        },
        data(){
            return{
                accountData :{
                    id : "",
                    businessId : "",
                    realName : "",
                    userName:"",
                    sex : "男",
                    mobile : "",
                    pwd : "",
                    mercharType : "merchant",
                    bossType:1,
                },
            }
        },
        methods : {
            // 根据id获取数据
            getData(){
                this.$ajax(
                    this.$joggle.business.selectBusinessUser,
                    JSON.stringify({"id" :this.accountData.id}),
                    true,
                    (data,modal)=>{
                        modal.close();
                        if (data.code == "ZS011000") {
                            data=data.data;
                            this.accountData.realName=data.realName;
                            this.accountData.userName=data.userName;
                            this.accountData.sex=data.sex;
                            this.accountData.mobile=data.mobile;
                        } else {
                            this.$modal({
                                type:'error',
                                message:data.msg,
                                duration:'1000'
                            });
                        }
                    });
            },
            // 验证输入框是否通过
            verificationInput(){
                let errorText="";
                if(isEmpty(this.accountData.userName)){
                    errorText="请完善登录账号";
                }else if(!isMobile(this.accountData.mobile)){
                    errorText="请完善联系电话";
                }else if(isEmpty(this.accountData.pwd) && this.accountData.id==""){
                    errorText="请完善密码";
                }
                if(errorText!=""){
                    this.$message({
                        type:'error',
                        message:errorText,
                        duration:'1000'
                    });
                    return true;
                }else{
                    return false;
                }

            },
            // 保存
            doSave(){
                if(this.verificationInput()){
                    return;
                }
                this.accountData.userName = this.accountData.userName.toLowerCase();
                this.accountData.realName='管理员';
                let joggle=this.accountData.id=="" ? this.$joggle.business.insertBusinessUser : this.$joggle.business.updateBusinessUser;
                this.$ajax(
                    joggle,
                    JSON.stringify(this.accountData),
                    true,
                    (data,modal)=>{
                        modal.close();
                        if (data.code == "ZS011000") {
                            this.$message({
                                type:'success',
                                message:'操作成功',
                                duration:'2000'
                            });
                            setTimeout(()=>{
                                window.history.go(-1);
                            },2000);
                        } else {
                            this.$message({
                                type:'error',
                                message:data.msg,
                                duration:'1000'
                            });
                        }
                    });
            }
        },
        mounted(){
            this.accountData.businessId=getDataFromParam("businessId");
            this.accountData.id=getDataFromParam("id");
            if(this.accountData.id!=null){
                this.getData();
            }else{
                this.accountData.id="";
            }
        }
    }
</script>
<style lang="less">
    @import "account_insert.less";
</style>
