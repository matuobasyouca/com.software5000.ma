<template>
    <div class="merchant-list-page" v-show="!isLoading">
        <ma-head :home="false" @left-icon-click="handleGoBack" :go-back="false" right-icon-text="添加商家" @right-icon-click="handleToInsert">商家管理</ma-head>
        <search placeholder="请输入商家名称关键字搜索" @search="handleSearch" v-model="keyWord"></search>
        <ul class="merchant-list">
            <li v-for="merchant in merchantList" :key="merchant.id">
                <div class="merchant-list-item">
                    <div class="merchant-pic" :style="{backgroundImage:'url('+merchant.imageShowPath+')'}"></div>
                    <span class="state-label" :class="setLabelClass(merchant.businessLineState)">{{merchant.businessLineStateDes}}</span>
                    <p class="merchant-name">{{ merchant.businessName }}</p>
                    <zs-icon
                            class="merchant-address"
                            icon="address"
                            size="14"
                            :text="handleSetAddress(merchant)"></zs-icon>
                    <zs-icon
                            class="merchant-tel"
                            icon="tel-gray"
                            size="14"
                            :text="merchant.businessTel"></zs-icon>
                </div>
                <p class="merchant-list-control clr">
                    <zs-icon icon="account" size="14" text="账号管理" @click="handleToAccountManage(merchant.id,merchant.bossId)"></zs-icon>
                    <zs-icon icon="edit" size="14" text="编辑" @click="handleToEdit(merchant.id)"></zs-icon>
                </p>
            </li>
            <li class="list-empty" v-if="merchantList.length==0">暂无记录</li>
        </ul>
        <p v-show="merchantList.length>0" class="show-more" :class="[{'no-more': !hasNextPage}]" @click="handleShowMore">
            {{ hasNextPage ? '查看更多' : '已显示全部'}}</p>
    </div>
</template>
<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import search from '../../../../../components_proj/search/app.vue';
    import {turnToNextPage} from '../../../../../assets/js/utils';
    export default {
        components: {
            maHead,search
        },
        data(){
            return {
                //操作数据
                isLoading: true,
                startPage: 1,
                pageSize: 10,
                hasNextPage: false,
                //保存数据
                keyWord: '',
                merchantList: []
            }
        },
        methods: {
            //
            handleGoBack(){
                turnToNextPage('/wap/operator/home/workbench.html');
            },
            //搜索
            handleSearch(){
                this.getMerchantList(false)
            },
            //获取商家数据
            getMerchantList(isShowMore, fn){
                if (isShowMore) {
                    this.startPage++
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                        this.$joggle.business.selectBusinessListPage,
                        {
                            startPage: this.startPage,
                            pageSize: this.pageSize,
                            businessName: this.keyWord,
                            orderBy:'businessLineState ASC ,createTime DESC '
                        },
                        true,
                        (data, modal)=> {
                            modal.close();
                        if (data.code === 'ZS011000') {
                                this.hasNextPage = data.data.hasNextPage;

                                if (isShowMore) {
                                    this.merchantList = this.merchantList.concat(data.data.list)
                                } else {
                                    this.merchantList = data.data.list;
                                }
                                fn && fn()
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                })
                            }
                        }
                )
            },
            //设置状态标签颜色
            setLabelClass(state){
                if (state == 1) {
                    return 'label-green'
                } else if (state == 2) {
                    return 'label-gray'
                }
            },
            //设置地址
            handleSetAddress(merchant){
                let addr = '';
                merchant.areaList.forEach((a)=> {
                    addr += a.areaName;
                })
                addr += merchant.businessAddress;
                return addr;
            },
            //显示更多
            handleShowMore(){
                if (!this.hasNextPage)return;
                this.getMerchantList(true);
            },
            //跳转到账号管理
            handleToAccountManage(businessId,bossId){
                if(bossId){
                    turnToNextPage("/wap/operator/home/merchant_manage/account_insert.html",{"businessId":businessId,"id" : bossId});
                }else{
                    turnToNextPage("/wap/operator/home/merchant_manage/account_insert.html",{"businessId":businessId});
                }
            },
            //跳转到编辑
            handleToEdit(id){
                turnToNextPage('/wap/operator/home/merchant_manage/merchant_edit.html',{businessId : id})
            },
            //跳转去添加商家
            handleToInsert(){
                turnToNextPage('/wap/operator/home/merchant_manage/merchant_insert.html')
            }
        },
        created(){
            this.getMerchantList(false,()=> {
                this.isLoading = false;
            })
        }
    }
</script>
<style lang="less">
    @import "merchant_list.less";
</style>