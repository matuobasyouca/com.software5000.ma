<template>
    <div class="group-buying">
        <div class="group-header">
            <zs-button type="primary" @click="insertActivity()">新建拼团</zs-button>
            <div class="r">
                <zs-input v-model.trim="searchMag" @keydown.enter="handleSearchData" placeholder="请输入活动名、商家名称关键词"
                          icon="circle-cross"></zs-input>
                <zs-button type="primary" @click="handleSearchData">
                    <zs-icon icon="search2" :size="20"></zs-icon>
                </zs-button>
            </div>
        </div>
        <zs-table
                :data="datas"
                :columns="columns"
                :context="context"
                :no-data-colspan="10"
                border
                class="group-table"
                noDataText="暂无拼团记录"
                noDataIcon="data-empty"
        ></zs-table>
        <div class="page">
            <zs-pagination
                    :current="search.startPage"
                    :total="totalPage"
                    :page-size="search.pageSize"
                    show-total
                    show-elevator
                    show-sizer
                    @on-change="handlePageNum"
                    @on-page-size-change="handlePageSize"
            ></zs-pagination>
        </div>

        <!-- 二维码 -->
        <zs-dialog
                class="ewm-dialog"
                v-model="ewmDialog"
                @close="handleClockEwmDialog"
                show-close>
            <p slot="title">拼团二维码</p>
            <div class="dialog-body">
                <p class="p1">{{indexMsg!=-1 ? datas[indexMsg].clusterName : ''}}</p>
                <div class="QR-code" v-show="!ewmState">
                    <img ref="img" src="../../../../../../../assets/img/three-dots.svg">
                </div>
                <div ref="oImg" class="QR-code" v-show="ewmState"></div>
            </div>
        </zs-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
    import { imgPreLoad, turnToNextPage } from '../../../../../../../assets/js/utils';
    import { appointUrl } from '../../../../../../../assets/js/turnToHostPage';

    export default {
        name: 'list',
        components: {},
        props: {},
        data(){
            return {
                datas: [],
                context: this,
                columns: [
                    {
                        title: '活动名称',
                        key: 'clusterName',
                        align: 'center',
                        width: 180
                    },
                    {
                        title: '活动时间',
                        align: 'center',
                        width: 168,
                        render(row){
                            return row.beginDay && row.endDay ? `<p>${row.beginDay.slice(0, 10)} ~ ${row.endDay.slice(0, 10)}</p>` : '';
                        }
                    },
                    {
                        title: '拼团商家',
                        key: 'businessName',
                        align: 'center',
                        width: 168

                    },
                    {
                        title: '拼团套餐',
                        key: 'packageName',
                        align: 'center',
                        width: 148
                    },
                    {
                        title: '拼团价格',
                        key: 'salePrice',
                        align: 'center',
                        width: 70
                    },
                    {
                        title: '成团人数',
                        key: 'clusterNum',
                        align: 'center',
                        width: 70

                    },
                    {
                        title: '成团时限',
                        key: 'clusterHour',
                        align: 'center',
                        width: 70

                    },
                    {
                        title: '已成团数',
                        key: 'successNum',
                        align: 'center',
                        width: 70
                    },
                    {
                        title: '活动状态',
                        key: 'stateDesc',
                        align: 'center',
                        width: 70,
                        render(row){
                            return row.state == 1 ? `<p>${row.stateDesc} </p>` : `<p style="color: #999">${row.stateDesc} </p>`;
                        }
                    },
                    {
                        title: '操作',
                        key: 'followTime',
                        align: 'center',
                        className: 'account-operation',
                        width: 234,
                        render(row, col, index, _this){
                            let s = `<a @click="updateState(${row.id},${row.state})">{{${row.state} == 1 ? '下架' : '上架'}}</a>`;
                            let e = `<a @click="trunToDetail(${row.id})">查看</a>`;
                            let d = `<a v-if="${row.canEdit} != 2 && ${row.state} != 1" @click="insertActivity(${row.id})">编辑</a>`;
                            let f = `<a v-if="${row.canEdit} != 2 && ${row.state} != 1" @click="showDeleteDialog(${row.id},${index})">删除</a>`;
                            let j = `<a @click="handleEwmDialog(${row.id},${index})">二维码</a>`;

                            return s + e + d + f + j;
                        }
                    }
                ],
                totalPage: 0,
                search: {
                    keyWord: '',
                    startPage: 1,
                    pageSize: 10
                },
                searchMag: '',
                ewmDialog: false,
                indexMsg: -1,
                timer: null,
                ewmState: false
            };
        },
        computed: {},
        watch: {
            ewmDialog(val){
                if (!val) {
                    this.indexMsg = -1;
                }
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 点击编辑和添加活动按钮
             */
            insertActivity(id){
                if (id) {
                    turnToNextPage('/web/operator/home/activity/add_activity.html', { id });
                } else {
                    turnToNextPage('/web/operator/home/activity/add_activity.html');
                }
            },
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
            /**
             * 删除活动
             * @param id
             */
            showDeleteDialog(id){
                this.$confirm({
                    type: 'delete',
                    title: '删除',
                    showClose: true,
                    message: '确定删除该拼团？'
                }).then(() => {
                    this.$ajax(
                        this.$joggle.operator.activity.deletePackCluster,
                        {
                            id
                        },
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: data.msg
                                });
                                if (this.datas.length === 1 && this.search.startPage != 1) {
                                    parseFloat(this.search.startPage) - 1;
                                }
                                this.selectActiveDates();
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        }
                    );
                });
            },
            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 上下架操作
             * @param id
             * @param state
             */
            updateState(id, state){
                this.$ajax(
                    this.$joggle.operator.activity.updatePackClusterState,
                    {
                        id,
                        state: state == 1 ? 2 : 1
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: state == 1 ? '下架成功' : '上架成功',
                                duration: 1200
                            });
                            this.selectActiveDates();

                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 获取活动列表信息
             */
            selectActiveDates(){
                this.$ajax(
                    this.$joggle.operator.activity.selectPackClusterByPage,
                    this.search,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.datas = data.data.list;
                            this.totalPage = data.data.total;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 点击搜索操作
             */
            handleSearchData(){
                this.search.startPage = 1;
                this.search.keyWord = this.searchMag;
                this.selectActiveDates();
            },
            /**
             * 二维码弹窗
             * @param id
             * @param index
             */
            handleEwmDialog(id, index){
                this.indexMsg = index;
                this.handleBarCode(id);
                this.ewmDialog = true;
            },
            /**
             * 获取二维码
             * @param id
             */
            handleBarCode(id){
                const url = `${appointUrl.ma}/wap/customer/open/group/activity.html?id=${id}&t=${new Date().getTime()}`;

                imgPreLoad(
                    `${appointUrl.emkt}/open/coupons/selectQr?type=&url=${url}`,
                    (img) => {
                        this.ewmState = true;
                        this.$refs.oImg.innerHTML = '';
                        this.$refs.oImg.appendChild(img);
                    }
                );
            },
            /**
             * 点击关闭二维码弹窗
             */
            handleClockEwmDialog(){
                this.ewmState = false;
                this.ewmDialog = false;
            },
            /**
             * 跳转详情页
             * @param id
             */
            trunToDetail(id){
                turnToNextPage('/web/operator/home/activity/detail.html', { id });
            },
            /**
             * 切换页码
             * @param pageNum
             */
            handlePageNum(pageNum){
                this.search.startPage = pageNum;
                this.selectActiveDates();
            },
            /**
             * 切换每页展示的个数
             * @param pageSize
             */
            handlePageSize(pageSize){
                this.search.startPage = 1;
                this.search.pageSize = pageSize;
                this.selectActiveDates();
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.selectActiveDates();
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>