<template>
    <div class="consume-list-page" v-show="!isLoading">
        <ma-head :home="false">消费记录</ma-head>
        <ul class="consume-tab">
            <li v-for="(t,i) in tabs" @click="changTab(i)">
                <span :class="[{'curr' : currTab == i}]">{{ t }}</span>
            </li>
        </ul>
        <ul class="consume-list" v-if="consumeList.length>0">
            <li class="consume-info" v-for="info in consumeList" :key="info.id" @click="handleToDetail(info.id)">
                <div class="merchant-name">
                    <zs-icon icon="shang3" size="19" icon-dist="10" class="name-icon" :text="info.businessName"></zs-icon>
                    <span class="consume-time" >{{info.updateTime.slice(0,10) }}</span>
                </div>
                <div v-for="(item,i) in info.workOrderDetails" v-if="i<=2">
                    {{item.serviceItem.itemName}}
                    <span class="price">￥{{ item.salePrice }}</span>
                    <div class="consume-more" v-if="i == 2" ></div>
                </div>

                <div class="consume-total">
                    <span class="state" :class="stateClass(info.state)">{{ setState(info.state) }}</span>
                    <zs-icon :icon="payType(info.payType)" size="18" :text="payTypeText(info.payType)" class="pay-type" v-if="info.state == 4"></zs-icon>
                    <span v-if="info.state == 4">￥{{ info.totalPrice }}</span>
                </div>
            </li>
        </ul>
        <p class="list-empty" v-else>暂无数据</p>
        <div class="list-show-more" v-if="hasNextPage" @click="handleShowMore">显示更多</div>
    </div>
</template>
<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import {getDataFromParam, isEmpty, turnToNextPage} from '../../../../../assets/js/utils';
    import Base64 from '../../../../../assets/js/base64.min';
    import {turnToHostPage} from '../../../../../assets/js/turnToHostPage';
    export default {
        components: {
            maHead
        },
        props: [],
        data: function () {
            return {
                isLoading: true,
                consumeList: [],
                hasNextPage: false,
                openId: '',
                startPage: 1,
                pageSize: 5,
                currTab: 0,
                tabs: ['全部', '未完工', '待支付', '已完成'],
                stateMap: [[1, 2, 3, 4], [1, 2], [3], [4]]
            }
        },
        computed: {},
        methods: {
            changTab(tab){
                this.currTab = tab;
                this.getConsumeList(false)
            },
            handleShowMore(){
                this.getConsumeList(true)
            },
            handleToDetail(id){
                turnToNextPage('/wap/customer/open/consume/detail.html', {id: id})
            },
            getConsumeList(isShowMore, fn){
                if (isShowMore) {
                    this.startPage++
                } else {
                    this.startPage = 1
                }
                this.$ajax(
                    this.$joggle.customer.consume.selectConsumeListByOpenId,
                    {
                        wxOpenId: this.openId,
                        state: this.stateMap[this.currTab],
                        startPage: this.startPage,
                        pageSize: this.pageSize
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.consumeList = isShowMore ? this.consumeList.concat(data.data.list) : data.data.list;
                            this.hasNextPage = data.data.hasNextPage;
                            fn && fn()
                        } else {
                            this.$message({
                                type: 'error',
                                message: '操作失败'
                            })
                        }
                    }
                )
            },
            setState(state){
                let stateMap = ['', '未完工', '未完工', '待支付', '已完成']
                return stateMap[state];
            },
            payType(type){
                let payTypeMap = ['', 'wechat-pay', 'cash-pay'];
                return payTypeMap[type];
            },
            payTypeText(type){
                return ['', '微信支付', '现金支付'][type];
            },
            stateClass(state){
                return state == 4 ? 'green' :
                    state == 3 ? 'gray' : '';
            }
        },
        created() {
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            if (isEmpty(this.openId)) {
                turnToHostPage('/open/user_center.html', 'emkt');
            } else {
                this.getConsumeList(false, () => {
                    this.isLoading = false;
                })
            }

        }
    }
</script>

<style lang="less">
    @import "./style.less";
</style>