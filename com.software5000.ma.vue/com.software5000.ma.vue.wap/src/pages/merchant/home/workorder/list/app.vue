<template>
    <div class="workorder-page" @scroll="pageScroll" ref="scrollEle">
        <template v-show="!isLoading">
            <zs-tab v-model="currTab" :tabs="tabs" @change="handleTabChange"></zs-tab>
            <pull-menu
                    v-model="menuVisible"
                    v-show="currTab == 3"
                    p1="时间筛选"
                    p2="付款方式">
                <template slot="p1">
                    <div class="create-time-select">
                        <label for="startTime" class="time-wrap">
                            <span>{{ startTime || '开始日期' }}</span>
                            <input type="date" id="startTime" v-model="startTime">
                        </label>
                        <span class="time-wrap-sep">-</span>
                        <label for="endTime" class="time-wrap">
                            <span>{{ endTime || '结束日期'}}</span>
                            <input type="date" id="endTime" v-model="endTime">
                        </label>
                    </div>
                    <ul class="create-time-btn">
                        <li @click="handleTimeControl(-1)">清空</li>
                        <li @click="handleTimeControl">确定</li>
                    </ul>
                </template>
                <ul slot="p2" class="pay-type-list">
                    <li :class="[{'curr':payType == -1}]" @click="handlePayTypeSelect(-1)">不限</li>
                    <li
                            :class="[{'curr':payType == t[0]}]"
                            v-for="t in workOrderPayTypes"
                            :key="t[0]"
                            v-if="t[0] != 3"
                            @click="handlePayTypeSelect(t[0])">{{ t[1] }}
                    </li>
                </ul>
            </pull-menu>
            <ul class="item-list" :class="[{'is-operator' : isOperator,'pt110' : currTab == 3}]">
                <template v-if="workOrders.length > 0">
                    <li class="list" v-for="order in workOrders" :key="order.id" @click="handleCheck(order.id)">
                        <div class="list-info">
                            <zs-icon class="car-number"
                                     icon="car"
                                     size="20"
                                     icon-dis="10"
                                     :text="order.carNumber"></zs-icon>
                            <zs-icon
                                    class="member-label"
                                    :class="[{'is-member':order.recordId}]"
                                    :icon="order.recordId ? 'member' : 'no-member'"
                                    size="16"
                                    icon-dis="4" :text="order.recordId ? order.lvlName || '普通会员' : '非会员'"></zs-icon>
                            <span v-if="order.state == 4" class="pay-time">{{ order.payTime ? order.payTime.slice(0,10) : '' }}</span>
                        </div>
                        <ul class="items">
                            <li
                                    v-for="(item,index) in order.workOrderDetails"
                                    v-if="index < 3">
                                {{ `${item.serviceItemName}${isOperator ? '' : ' * ' + item.itemsTimes}` }}
                                <span>{{ isOperator ? `x ${item.itemsTimes}` : item.workerName || '' }}</span>
                            </li>
                            <li v-if="order.workOrderDetails.length > 3">···</li>
                        </ul>
                        <div class="control" v-if="!isOperator">
                            <zs-button v-if="order.state == 3" type="primary" @click="handleCheck(order.id)">
                                去结算
                            </zs-button>
                            <template v-if="order.state == 1">
                                <zs-button type="success" v-if="!isOperator" @click.stop="handleEdit(order.id)">
                                    修改工单
                                </zs-button>
                                <zs-button type="primary" @click.stop="handleFinish(order.id)">施工完成</zs-button>
                            </template>
                            <template v-if="order.state == 4">
                                <zs-icon
                                        :icon="['','cash-pay','wechat-pay'][order.payType]"
                                        :text="['','现金收款','微信收款'][order.payType]"
                                        icon-dis="6"
                                        size="17"></zs-icon>
                                <span class="total-price">￥{{ order.totalPrice }}</span>
                            </template>
                        </div>
                        <div class="control" v-if="isOperator && order.state == 4">
                            <span class="operator-detail">项目金额<i>{{ calcItemsPrice(order.workOrderDetails) }}</i></span>
                            <!--<span class="operator-detail">提成金额<i> -&#45;&#45; </i></span>-->
                        </div>
                    </li>
                    <li
                            class="show-more"
                            :class="[{'no-more' : !hasNextPage}]"
                            @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                    </li>
                </template>
                <li v-else class="list-empty">暂无工单</li>
            </ul>
            <template v-if="!isOperator">
                <div class="create-label" @click="handleEdit('')"></div>
                <ma-foot :current="1"></ma-foot>
            </template>
            <ul class="operator-menu" v-else>
                <li @click="handleQuit"><p class="p1">退出</p></li>
                <li @click="handleEdit('')"><p class="p2">创建工单</p></li>
            </ul>
        </template>
    </div>
</template>
<script>
    import { Hash, turnToNextPage, isEmpty, DateUtils } from '../../../../../assets/js/utils';
    import { defaultInfo } from '../../../../../assets/js/defaultInfo';
    import { debounce } from 'throttle-debounce';
    import maFoot from '../../../../../components_proj/ma_foot/app.vue';
    import pullMenu from '../../../../../components_proj/pull_menu/app.vue';
    export default {
        components: {
            maFoot,
            pullMenu
        },
        data(){
            return {
                isLoading: true,
                isOperator: false,
                workOrderPayTypes: [],
                //tab
                currTab: 1,
                tabs: [
                    {
                        title: '待结算',
                        count: 0
                    },
                    {
                        title: '未完工',
                        count: 0
                    },
                    {
                        title: '已完成',
                        count: 0
                    }
                ],
                //工单数据
                workOrders: [],
                startPage: 1,
                pageSize: 10,
                hasNextPage: false,
                //已完成工单筛选条件
                sTop: 0,
                menuVisible: false,
                startTime: '',
                endTime: '',
                payType: -1
            };
        },
        methods: {
            //页面滚动
            pageScroll(){
                debounce(300, () => {
                    this.sTop = this.$refs.scrollEle.scrollTop;
                })();
            },
            //退出
            handleQuit(){
                this.$confirm({
                    type: 'quit',
                    message: '确定退出'
                }).then(() => {
                    this.$ajax(
                        this.$joggle.merchant.open.logout,
                        {},
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: '退出成功'
                                });
                                setTimeout(() => {
                                    turnToNextPage('/wap/merchant/open/login.html');
                                }, 1200);
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        }
                    );
                }).catch(() => {
                });
            },
            //操作员展示项目金额
            calcItemsPrice(items){
                let price = 0;

                if (isEmpty(items)) return 0;
                items.forEach((item) => {
                    price += parseFloat(item.salePrice);
                });
                return price.toFixed(2);
            },
            //tab切换
            handleTabChange(i){
                Hash.add({ tab: i });
                this.startPage = 1;
                this.pageSize = 10;
                if (this.currTab != 3) {
                    this.payType = '';
                    this.startTime = '';
                    this.endTime = '';
                }
                this.$refs.scrollEle.scrollTop = 0;
                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //查看更多
            handleShowMore(){
                if (this.hasNextPage) {
                    this.pageSize = 10;
                    const loading = this.$loading();

                    this.selectWorkOrder(() => {
                        loading.close();
                    }, loading, true);
                }
            },
            //去修改
            handleEdit(id){
                const tempHash = Hash.parse();

                tempHash.pageSize = this.startPage * this.pageSize;
                Hash.add(tempHash);
                turnToNextPage('/wap/merchant/home/workorder/update.html', !isEmpty(id) ? { id } : {});
            },
            //查看详情
            handleCheck(id){
                const tempHash = Hash.parse();

                tempHash.pageSize = this.startPage * this.pageSize;
                tempHash.payType = this.payType;
                tempHash.startTime = this.startTime;
                tempHash.endTime = this.endTime;
                tempHash.sTop = this.sTop;
                Hash.add(tempHash);
                turnToNextPage('/wap/merchant/home/workorder/detail.html', { id });
            },
            //施工完成
            handleFinish(id){
                this.$ajax(
                    this.$joggle.merchant.workorder.updateWorkOrderForFinish,
                    { orderId: id },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.handleCheck(id);
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            //选择付款方式
            handlePayTypeSelect(type){
                this.payType = type;
                this.menuVisible = false;
                this.startPage = 1;
                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //选择时间
            handleTimeControl(type){
                if (type === -1) {
                    this.startTime = this.endTime = '';
                } else {
                    const newTimeArr = DateUtils.swapTime(this.startTime, this.endTime);

                    this.startTime = newTimeArr[0];
                    this.endTime = newTimeArr[1];
                }
                this.menuVisible = false;
                this.startPage = 1;
                const loading = this.$loading();

                this.selectWorkOrder(() => {
                    loading.close();
                }, loading);
            },
            //获取统计数据
            selectStateCount(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderStateCount,
                    {},
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (this.isOperator) {
                                this.$set(this.tabs[0], 'count', data.data[1] || 0);
                                this.$set(this.tabs[1], 'count', data.data[4] || 0);
                            } else {
                                this.$set(this.tabs[0], 'count', data.data[3] || 0);
                                this.$set(this.tabs[1], 'count', data.data[1] || 0);
                                this.currTab != 3 && this.$set(this.tabs[2], 'count', data.data[4] || 0);
                            }

                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        fn && fn();
                    }
                );
            },
            //获取对应状态的工单
            selectWorkOrder(fn, loading, isShowMore){
                if (isShowMore) {
                    this.startPage++;
                } else {
                    this.startPage = 1;
                }
                const stateMap = this.isOperator ? ['1', '4'] : ['3', '1', '4'];

                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderByPage,
                    {
                        state: stateMap[this.currTab - 1],
                        startPage: this.startPage,
                        pageSize: this.pageSize,
                        startTime: isEmpty(this.startTime) ? '' : `${this.startTime} 00:00:00`,
                        endTime: isEmpty(this.endTime) ? '' : `${this.endTime} 23:59:59`,
                        payType: this.payType === -1 ? '' : this.payType
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.hasNextPage = data.data.hasNextPage;
                            this.workOrders = isShowMore ? this.workOrders.concat(data.data.list) : data.data.list;
                            this.currTab == 3 && this.$set(this.tabs[2], 'count', data.data.total || 0);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        fn && fn(data);
                    }
                );
            },
            //获取下拉列表参数(支付方式)
            selectPayTypes(fn, loading){
                this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    { codeTypes: 'WorkOrderPayType' },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.workOrderPayTypes = data.data.WorkOrderPayType || [];
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        fn && fn();
                    }
                );
            }
        },
        mounted(){
            this.currTab = Hash.parse().tab || 1;
            this.pageSize = Hash.parse().pageSize || 10;
            this.sTop = Hash.parse().sTop || 0;
            if (this.currTab == 3) {
                this.payType = Hash.parse().payType;
                this.startTime = Hash.parse().startTime;
                this.endTime = Hash.parse().endTime;
            }
            const loading = this.$loading();

            defaultInfo((info) => {
                this.isOperator = info.mercharType ? info.mercharType.indexOf('operator') > -1 : false;
                if (this.isOperator) {
                    this.$delete(this.tabs, 0);
                    this.currTab = this.currTab == 3 ? 2 : this.currTab;
                }
                const f0 = new Promise((resolve) => {
                    this.selectStateCount(() => {
                        resolve();
                    }, loading);
                });
                const f1 = new Promise((resolve) => {
                    this.selectWorkOrder(() => {
                        resolve();
                    }, loading);
                });
                const f2 = new Promise((resolve) => {
                    this.selectPayTypes(() => {
                        resolve();
                    }, loading);
                });

                Promise.all([f0, f1, f2]).then(() => {
                    loading.close();
                    this.isLoading = false;
                    this.pageSize > 10 && (this.startPage = this.pageSize / 10);
                    setTimeout(()=>{
                        this.$refs.scrollEle.scrollTop = this.sTop;
                    },0)
                });
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
