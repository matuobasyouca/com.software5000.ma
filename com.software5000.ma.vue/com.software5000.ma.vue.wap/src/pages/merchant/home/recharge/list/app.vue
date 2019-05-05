<template>
    <div class="page-recharge" @scroll="handlePageScroll" ref="scrollEle">
        <ma-head :go-back="false" @left-icon-click="handleGoBack" @home-click="handleGoHome">充值记录</ma-head>
        <ul class="recharge-sec-1">
            <template v-if="rechargeList.length > 0">
                <li
                        class="recharge-info-list"
                        v-for="list in rechargeList"
                        :key="list.id"
                        @click="handleTurnToDetail(list.id)">
                    <p class="recharge-info-1">
                        <zs-icon icon="user" size="16" icon-dis="4" :text="list.user.realName || '--'"></zs-icon>
                        {{ list.mobile }}
                        <span class="right">{{ list.updateTime }}</span>
                    </p>
                    <p class="recharge-info-2">
                        {{ parseFloat(list.reChargeMoney).toFixed(2) }}
                        <span class="right">{{ parseFloat(list.grantMoney).toFixed(2) }}</span>
                    </p>
                    <p class="recharge-info-3">
                        充值金额(元)
                        <span class="right">赠送金额(元)</span>
                    </p>
                    <p class="recharge-info-4">
                        {{ ['','现金收款','微信收款'][list.payType] }}
                        <span>￥{{ parseFloat(list.reChargeMoney).toFixed(2) }}</span>
                    </p>
                </li>
                <li
                        class="show-more"
                        :class="[{'no-more' : !hasNextPage}]"
                        @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                </li>
            </template>
            <li v-else class="list-empty">暂无充值记录</li>
        </ul>
    </div>
</template>

<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import { turnToNextPage, Hash } from '../../../../../assets/js/utils';
    import { debounce } from 'throttle-debounce';
    export default {
        components: {
            maHead
        },
        props: {},
        data(){
            return {
                isLoading: true,
                rechargeList: [],
                hasNextPage: false,
                startPage: 1,
                pageSize: 2,
                sTop: 0
            };
        },
        computed: {},
        watch: {},
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 查询充值记录
             * @param fn
             * @param loading
             * @param isShowMore 是否加载更多
             */
            selectRechargeOrder(fn, loading, isShowMore){
                if (isShowMore) {
                    this.startPage++;
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.member.selectRechargeOrderPage,
                    {
                        startPage: this.startPage,
                        pageSize: this.pageSize
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.rechargeList = isShowMore ? this.rechargeList.concat(data.data.list) : data.data.list;
                            this.hasNextPage = data.data.hasNextPage;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                        fn && fn();
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 页面滚动
             */
            handlePageScroll(){
                debounce(300, () => {
                    this.sTop = this.$refs.scrollEle.scrollTop;
                })();
            },
            /**
             * 返回上一页
             */
            handleGoBack(){
                turnToNextPage('/wap/merchant/home/recharge/update.html');
            },
            /**
             * 跳转到首页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 查看详情
             * @param id 充值记录详情
             */
            handleTurnToDetail(id){
                const tempHash = Hash.parse();

                tempHash.pageSize = this.startPage * this.pageSize;
                tempHash.sTop = this.sTop;
                Hash.add(tempHash);
                turnToNextPage('/wap/merchant/home/recharge/detail.html', { id });
            },
            /**
             * 查看更多
             */
            handleShowMore(){
                if (this.hasNextPage) {
                    this.pageSize = 10;
                    const loading = this.$loading();

                    this.selectRechargeOrder(() => {
                        loading.close();
                    }, loading, true);
                }
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/

        },
        created(){

        },
        mounted(){
            this.pageSize = Hash.parse().pageSize || 10;
            this.sTop = Hash.parse().sTop || 0;

            const loading = this.$loading();

            this.selectRechargeOrder(() => {
                loading.close();
                this.isLoading = false;
                this.pageSize > 10 && (this.startPage = this.pageSize / 10);
                setTimeout(() => {
                    this.$refs.scrollEle.scrollTop = this.sTop;
                }, 0);
            }, loading);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
