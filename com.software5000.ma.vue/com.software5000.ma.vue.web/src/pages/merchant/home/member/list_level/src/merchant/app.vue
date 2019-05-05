<template>
    <div class="page-base merchant">
        <div class="merchant-top">
            <zs-button type="primary" @click="handleShowCreateDialog">新建会员等级</zs-button>
            <zs-button type="success" @click="handleOpenDiscountPage">会员折扣设置</zs-button>
        </div>
        <zs-table
                class="package-table"
                :data="datas"
                :columns="columns"
                :context="context"
                :no-data-colspan="10"
                border>
        </zs-table>
        <div class="pay_list-pagination">
            <zs-pagination
                    class="main-bottom"
                    :current="current"
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
        <zs-dialog class="create-dialog" v-model="createDialog">
            <span slot="title">{{createMemberLvData.id ? '编辑会员等级' : '新建会员等级'}}</span>
            <div class="list">
                <div class="list-item detail-list-item">
                    <div class="list-item-text"><i class="request"></i>等级名称</div>
                    <div class="list-item-con">
                        <zs-input v-model="createMemberLvData.lvlName" class="list-item-con-input" :maxlength="5"
                                  placeholder="请输入会员等级名称"></zs-input>
                        <span class="list-item-con-input-length">{{ `${createMemberLvData.lvlName.length}/5`}}</span>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-text"><i class="request"></i>单次充值</div>
                    <div class="list-item-con">
                        <zs-input v-model="createMemberLvData.rechargeMoney" class="list-item-con-input"
                                  placeholder="请输入该等级单次充值金额"></zs-input>
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <zs-button type="cancel" @click="createDialog=false">取消</zs-button>
                <zs-button type="primary" @click="updateCreateLv">确定</zs-button>
            </span>
        </zs-dialog>
    </div>
</template>

<script>
    import { isEmpty, turnToNextPage } from '../../../../../../../assets/js/utils.js';

    export default {
        components: {},
        data() {
            return {
                datas: [],
                columns: [
                    {
                        title: '会员等级名称',
                        key: 'lvlName',
                        width: 360
                    },
                    {
                        title: '单次充值金额',
                        key: 'rechargeMoney',
                        width: 338,
                        render(row) {
                            return row.rechargeMoney || 0;
                        }
                    },
                    {
                        title: '状态',
                        key: 'state',
                        width: 280,
                        render(row) {
                            return row.state == 1 ? '启用' : `<span class="gray">停用</span>`;
                        }
                    },
                    {
                        title: '操作',
                        key: 'state',
                        width: 276,
                        className: 'control',
                        render(row, col, index) {
                            let remain = `<span @click="handleShowRemainDialog('${index}')">修改</span>`;
                            let state = row.state == 1 ? '停用' : '启用';

                            state = `<span @click="handleChangeState('${index}')">${state}</span>`;
                            return `${remain}${state}`;
                        }
                    }
                ],
                context: this,
                currIndex: 1,

                current: 1,
                pageSize: 10,
                total: 0,

                createDialog: false,
                createMemberLvData: {
                    id: '',
                    lvlName: '',
                    totalConsume: ''
                }
            };
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/

            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/
            /**
             *  创建或修改会员等级
             */
            updateCreateLv() {
                if (!this.handleCertCreateLv()) return;
                this.createMemberLvData.rechargeMoney=Number(this.createMemberLvData.rechargeMoney).toFixed(2);
                if (this.createMemberLvData.id) {
                    this.remainMemberLv();
                } else {
                    this.$ajax(
                        this.$joggle.merchant.businessPackage.insertMemberLvl,
                        this.createMemberLvData,
                        true,
                        (data, loading) => {
                            if (data.code === 'ZS011000') {
                                this.selectPackageLvData();
                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                });
                                this.createDialog = false;
                            } else {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        }
                    );
                }

            },
            /**
             * 修改会员等级
             */
            remainMemberLv() {
                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateMemberLvl,
                    this.createMemberLvData,
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.selectPackageLvData();
                            this.$message({
                                type: 'success',
                                message: data.msg,
                                duration: 1200
                            });
                            this.createDialog = false;
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
            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/
            /**
             *  会员等级列表数据
             * @param fn 后续操作
             */
            selectPackageLvData(fn) {
                this.$ajax(
                    this.$joggle.merchant.setting.selectMemberLvlList,
                    {
                        startPage: this.current,
                        pageSize: this.pageSize
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.total = data.data.total;
                            this.datas = data.data.list;
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
            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             * 页码切换
             * @param currPage  指定当前页面号码
             */
            handlePageChange(currPage) {
                this.current = currPage;
                let loading = this.$loading();

                this.selectPackageLvData(() => {
                    loading.close();
                });
            },
            /**
             * 每页数据条数切换
             * @param pageSize  列表大小
             */
            handlePageSizeChange(pageSize) {
                this.current = 1;
                this.pageSize = pageSize;
                let loading = this.$loading();

                this.selectPackageLvData(() => {
                    loading.close();
                });
            },
            /**
             * 打开修改等级弹窗
             * @param index  当前列表的第几项
             */
            handleShowRemainDialog(index) {
                this.createMemberLvData = { ...this.datas[index] };
                this.createDialog = true;
            },
            /**
             * 改变该套餐的状态
             * @param index  当前列表的第几项
             */
            handleChangeState(index) {
                this.createMemberLvData = { ...this.datas[index] };
                this.createMemberLvData.state = this.createMemberLvData.state == 1 ? 2 : 1;
                this.remainMemberLv();
            },
            /**
             * 打开新建会员等级弹窗
             */
            handleShowCreateDialog() {
                for (let x in this.createMemberLvData) {
                    this.createMemberLvData[x] = '';
                }
                this.createDialog = true;
            },
            /**
             * 打开会员折扣页面
             */
            handleOpenDiscountPage() {
                turnToNextPage('/web/merchant/home/member/update_discount.html');
            },
            /**
             * 验证新建会员等级数据
             */
            handleCertCreateLv() {
                let certErrorMsg = {
                    lvlName: '请输入等级名称',
                    rechargeMoney: '请输入正确的充值金额'
                };

                for (let x in certErrorMsg) {
                    let data = this.createMemberLvData[x] + '';

                    let totalConsume = x == 'rechargeMoney' && (!data.isInReg(/^\d+(\.\d+)?$/) || data==0);

                    if (isEmpty(data) || totalConsume ) {
                        this.$message({
                            type: 'error',
                            message: certErrorMsg[x],
                            duration: 1200
                        });
                        return false;
                    }
                }
                return true;
            }
            /* ------------------- handle (操) end -------------------*/
        },
        mounted() {
            this.selectPackageLvData();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
