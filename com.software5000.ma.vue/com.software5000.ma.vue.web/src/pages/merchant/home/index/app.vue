<template>
    <div class="user-center">
        <ma-head :currId="01"></ma-head>
        <div class="center-content">
            <ul>
                <li v-for="(item,index) in lists" :key="index" :class="`list${index+1}`" @click="turnTo(index)">
                    <div class="list-logo" :class="`list-logo${index+1}`"></div>
                    <div class="list-title">{{item.title}}</div>
                    <div class="label" v-if="item.labelOne">{{`${item.labelOne}：${moneyChange(item.key1)}`}}</div>
                    <div class="label" v-if="item.labelTwo">{{`${item.labelTwo}：${moneyChange(item.key2)}`}}</div>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    import maHead from '../../../../components_proj/ma-head/app.vue';
    import { turnToNextPage, strim, isEmpty } from '../../../../assets/js/utils';
    export default{
        components: {
            maHead
        },
        data(){
            return {
                lists: [
                    {
                        title: '快捷开单',
                        labelOne: '未结算',
                        key1: 'noPayOrder'
                    },
                    {
                        title: '财务明细',
                        labelOne: ' 今日收入',
                        labelTwo: ' 昨日收入',
                        key1: 'todayCount',
                        key2: 'yeCount'
                    },
                    {
                        title: '服务项目',
                        labelOne: '项目数',
                        key1: 'itemNum'
                    },
                    {
                        title: '购买套餐',
                        labelOne: '套餐数',
                        labelTwo: '待结算',
                        key1: 'buyPackNum',
                        key2: 'noPayPackNum'
                    },
                    {
                        title: '会员',
                        labelOne: '会员数',
                        key1: 'memberNum'
                    },
                    {
                        title: '服务评价',
                        labelOne: '好评数',
                        labelTwo: '总评数',
                        key1: 'allEvaluationNum',
                        key2: 'goodEvaluationNum'
                    }
                ],
                data: {}
            };
        },
        methods: {
            getData(){
                this.$ajax(
                    this.$joggle.merchant.index.workbench,
                    {},
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.data = data.data;
                        } else {
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* 钱格式转化 */
            moneyChange(key){
                let money = this.data[key];
                if (isEmpty(money)) return 0;
                const unit = key === 'todayCount' || key === 'yeCount' ? ' 元' : '';
                let previous = (money).toString().split('.');
                let len = previous[0].length;
                let lastIndex = 0;
                let arr = [];
                let newMoney = 0;
                while (len > 0) {
                    lastIndex = len;
                    len -= 3;
                    arr.unshift(previous[0].substring(len, lastIndex));
                }
                newMoney = previous[1] ? arr.join(',') + '.' + previous[1] : arr.join(',');
                return `${newMoney}${unit}`;
            },
            turnTo(index){
                const addr = [
                    '/web/merchant/home/workorder/update.html',
                    '/web/merchant/home/finance/list_financial.html',
                    '/web/merchant/home/setting/services.html',
                    '/web/merchant/home/member/buy_package.html',
                    '/web/merchant/home/member/list.html'
                ];

                if (index == 5) {
                    return this.$message({ message: '该服务暂未开放，敬请期待' });
                }
                return index < addr.length ? turnToNextPage(addr[index]) : '';
            }
        },
        mounted(){
            this.getData();
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>