<template>
    <div class="member-page">
        <search placeholder="请输入姓名、手机号或车牌" @search="searchData"></search>
        <ul class="item-list">
            <template v-if="pageData.length > 0">
                <li class="list" v-for="member in pageData" :key="member.id" @click="handleToMemberDetail(member.id)">
                    <p class="list-p-img" :class="setBackgroundClass(member.user.wxOpenId ? true : false)">{{member.user.wxOpenId ? '已绑定微信' : '未绑定微信'}}</p>
                    <div class="list-div">
                        <span class="list-div-mobile" v-if="member.user.mobile" >{{member.user.mobile}}</span>
                        <zs-icon size="17" class="vip" icon="member" :text="member.memberLvl.lvlName" v-if="member.memberLvlId"></zs-icon>
                        <zs-icon size="17" class="vip" icon="member" text="普通会员" v-else></zs-icon>
                        <span class="list-div-date">{{member.createTime.slice(0,10)}}</span>
                    </div>
                    <p class="list-p-car">{{getCarNumbers(member.cars)}}
                    </p>
                </li>
                <li
                    class="show-more"
                    :class="[{'no-more' : !hasNextPage}]"
                    @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                </li>
            </template>
            <li v-else class="list-empty">暂无客户</li>
        </ul>
        <div class="create-label" @click="handleBuyPackage"></div>
        <ma-foot :current="2"></ma-foot>
    </div>
</template>

<script type="text/ecmascript-6">

    import {turnToNextPage} from '../../../../../assets/js/utils';
    import search from '../../../../../components_proj/search/app.vue';
    import maFoot from '../../../../../components_proj/ma_foot/app.vue';

    export default {
        components: {
            search,
            maFoot
        },
        data() {
            return {
                //查询条件
                searchInfo: '',
                //会员数据
                pageData: [],
                startPage: 1,
                pageSize: 10,
                hasNextPage: false
            }
        },
        methods : {
            //跳转购买套餐
            handleBuyPackage() {
                turnToNextPage("/wap/merchant/home/member/buy_package.html",{});
            },
            getData(isShowMore) {
                if (isShowMore) {
                    this.startPage++;
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.member.selectMemberInfo,
                    {
                        searchInfo: this.searchInfo,
                        startPage: this.startPage,
                        pageSize: this.pageSize
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.hasNextPage = data.data.hasNextPage;
                            this.pageData = isShowMore ? this.pageData.concat(data.data.list) : data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            //搜索
            searchData(keyWords) {
                this.searchInfo = keyWords;
                this.getData(false);
            },
            //显示更多
            handleShowMore() {
                if(this.hasNextPage) {
                    this.getData(true);
                }
            },
            //跳转会员详情
            handleToMemberDetail(id) {
                turnToNextPage("/wap/merchant/home/member/detail.html",{"id" : id});
            },
            //获取车牌号
            getCarNumbers(cars) {
                const temp = [];
                cars.forEach((car)=>{
                    temp.push(car.carNumber)
                })
                return temp.join('、');
            },
            //设置背景图片
            setBackgroundClass(flag){
                if(flag) {
                    return 'list-p-wx-img';
                }
                return '';
            }
        },
        created(){
            //获取会员信息
            this.getData(false);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
