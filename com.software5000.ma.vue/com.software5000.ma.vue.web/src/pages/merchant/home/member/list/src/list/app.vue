<template>
    <div class="page-base main">
        <div class="search-box clr">
            <div class="r">
                <zs-input v-model.trim="searchInfo" @keydown.enter="handleConditionData"
                          placeholder="请输入姓名、手机号、车牌关键词搜索"></zs-input>
                <zs-button type="primary" @click="handleConditionData">
                    <zs-icon icon="search2" :size="20"></zs-icon>
                </zs-button>
            </div>
        </div>
        <zs-table
                class="main-body"
                :data="datas"
                :columns="columns"
                :context="context"
                :no-data-colspan="10"
                noDataText="暂无会员"
                noDataIcon="data-empty-member"
                border>
        </zs-table>
        <div class="main-bottom">
            <zs-pagination
                    class="pagination"
                    :current="condition.startPage"
                    :total="total"
                    :page-size-opts="[10,20,50,100]"
                    show-total
                    show-elevator
                    show-sizer
                    @on-change="handlePageChange"
                    @on-page-size-change="handlePageSizeChange"
            >
            </zs-pagination>
        </div>
    </div>
</template>

<script>
    import { turnToNextPage } from '../../../../../../../assets/js/utils.js';

    export default {
        components: {},
        data() {
            return {
                datas: [],
                columns: [
                    {
                        title: '车牌',
                        key: 'cars',
                        width: 260,
                        className: 'car-number',
                        render(row) {
                            let ret = '';

                            for (let x in row.cars) {
                                ret += row.cars[x].carNumber + '、';
                            }
                            ret = ret.slice(0, ret.length - 1);
                            ret = `<p title="${ret}">${ret}</p>`;
                            return ret;
                        }
                    },
                    {
                        title: '手机号',
                        key: 'user',
                        width: 144,
                        render(row) {
                            return row.user ? row.user.mobile : '';
                        }
                    },
                    {
                        title: '客户姓名',
                        key: 'user',
                        width: 96,
                        render(row) {
                            return row.user ? row.user.realName : '';
                        }
                    },
                    {
                        title: '绑定微信',
                        key: 'user',
                        width: 96,
                        render(row) {
                            let ret = '已绑定';

                            if (row.user) {
                                ret = row.user.wxOpenId ? '<span class="gray">已绑定</span>' : '未绑定';
                            }
                            return ret;
                        }
                    },
                    {
                        title: '会员等级',
                        key: 'memberLvl',
                        width: 90,
                        render(row) {
                            return row.memberLvl ? row.memberLvl.lvlName : '普通会员';
                        }
                    },
                    {
                        title: '累计消费',
                        key: 'totalConsume',
                        width: 130
                    },
                    {
                        title: '消费次数',
                        key: 'totalTimes',
                        width: 92,
                        render(row) {
                            return row.totalTimes || 0;
                        }
                    },
                    {
                        title: '加入时间',
                        key: 'createTime',
                        width: 134,
                        render(row) {
                            return row.createTime.slice(0, 10);
                        }
                    },
                    {
                        title: '操作',
                        key: 'like',
                        width: 208,
                        className: 'control',
                        render(row) {
                            let details = `<span @click="handleTurnPage('/web/merchant/home/member/detail.html',${row.id},true)">查看</span>`;
                            let buyPackage = `<span @click="handleTurnPage('/web/merchant/home/member/buy_package.html',${row.id},true)">购买套餐</span>`;

                            return `${details}${buyPackage} `;
                        }
                    }
                ],
                context: this,
                currIndex: 1,
                total: 0,
                searchInfo : '',

                condition: {
                    startPage: 1,
                    pageSize: 10,
                    searchInfo: ''
                }
            };
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/

            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/

            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/
            //
            /**
             *  获取会员数据
             * @param fn 执行函数完成之后的代码
             */
            selectGetDatas(fn) {
                this.$ajax(
                    this.$joggle.merchant.member.selectByPage,
                    JSON.stringify(this.condition),
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            loading.close();
                            this.searchInfo = this.condition.searchInfo;
                            this.datas = data.data.list;
                            this.total = data.data.total;
                            fn && fn();
                        } else {
                            loading.set({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            //
            /**
             *  条件查询搜索
             */
            handleConditionData() {
                this.condition.startPage = 1;
                this.condition.searchInfo = this.searchInfo;
                this.selectGetDatas();
            },
            /**
             *  跳转至会员详情
             */
            handleTurnPage(url, id, newPage) {
                turnToNextPage(url, { id }, newPage);
            },
            /**
             * 页码切换
             */
            handlePageChange(currPage) {
                this.condition.startPage = currPage;
                let loading = this.$loading();

                this.selectGetDatas(() => {
                    loading.close();
                });
            },
            /**
             * 每页数据条数切换
             */
            handlePageSizeChange(pageSize) {
                this.condition.startPage = 1;
                this.condition.pageSize = pageSize;
                let loading = this.$loading();

                this.selectGetDatas(() => {
                    loading.close();
                });
            }
            /* ------------------- handle (操) end -------------------*/

        },
        mounted() {
            this.selectGetDatas();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
