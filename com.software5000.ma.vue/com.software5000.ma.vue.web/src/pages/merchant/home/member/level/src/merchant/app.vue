<template>
    <div class="page-base merchant">
        <div class="merchant-top">
            <zs-button type="primary" @click="showCreateDialog">新建会员等级</zs-button>
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
                            <zs-input v-model="createMemberLvData.lvlName" class="list-item-con-input" :maxlength="5" placeholder="请输入会员等级名称"></zs-input>
                            <span class="list-item-con-input-length">{{ `${createMemberLvData.lvlName.length}/5`}}</span>
                        </div>
                    </div>
                    <div class="list-item detail-list-item">
                        <div class="list-item-text"><i class="request"></i>累计消费</div>
                        <div class="list-item-con">
                            <zs-input v-model="createMemberLvData.totalConsume" class="list-item-con-input" placeholder="请输入该等级需累计消费金额"></zs-input>
                        </div>
                    </div>
                    <div class="list-item detail-list-item">
                        <div class="list-item-text"><i class="request"></i>会员折扣</div>
                        <div class="list-item-con">
                            <zs-input v-model="createMemberLvData.discount" class="list-item-con-input" placeholder="请输入0-10位，可保留一位小数"></zs-input>
                        </div>
                    </div>
                </div>
            <span slot="footer" class="dialog-footer">
                <zs-button type="cancel" @click="createDialog=false">取消</zs-button>
                <zs-button type="primary" @click="createLv">确定</zs-button>
            </span>
        </zs-dialog>
    </div>
</template>

<script>
    import {turnToNextPage,isEmpty} from '../../../../../../../assets/js/utils.js';

    export default {
        components: {
        },
        data(){
            return {
                datas: [],
                columns: [
                    {
                        title: '会员等级名称',
                        key: 'lvlName',
                        width: 266
                    },
                    {
                        title: '累计消费金额',
                        key: 'totalConsume',
                        width: 356,
                    },
                    {
                        title: '会员折扣',
                        key: 'discount',
                        width: 242
                    },
                    {
                        title: '状态',
                        key: 'state',
                        width: 184,
                        render(row, col, index, _this){
                            return row.state==1? '启用' : `<span class="gray">停用</span>`;
                        }
                    },
                    {
                        title: '操作',
                        key: 'state',
                        width: 205,
                        className: 'control',
                        render(row, col, index, _this){
                            let remain=`<span @click="showRemainDialog(${index})">修改</span>`;
                            let state= row.state==1 ? '停用' : '启用';
                            state=`<span @click="changeState(${index})">${state}</span>`;
                            return `${remain}${state}`;
                        }
                    }
                ],
                context: this,
                currIndex: 1,

                current : 1,
                pageSize : 10,
                total : 0,

                createDialog :　false,
                createMemberLvData : {
                    id : '',
                    lvlName: '',
                    totalConsume: '',
                    discount: ''
                },
            }
        },
        methods  : {
            //获取套餐数据
            getDatas(fn){
                this.$ajax(
                    this.$joggle.merchant.setting.selectMemberLvlList,
                    {
                        "startPage":this.current,
                        "pageSize":this.pageSize
                    },
                    true,
                    (data,loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.total = data.data.total;
                            this.datas = data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                        fn && fn();
                    }
                )
            },

            /**
             * 页码切换
             */
            handlePageChange(currPage){
                this.current = currPage;
                let loading = this.$loading();
                this.getDatas(() => {
                    loading.close();
                });
            },
            /**
             * 每页数据条数切换
             */
            handlePageSizeChange(pageSize){
                this.current = 1;
                this.pageSize = pageSize;
                let loading = this.$loading();
                this.getDatas(() => {
                    loading.close();
                });
            },
            /*
             操作部分
             */
            //打开修改等级弹窗
            showRemainDialog(index){
                this.createMemberLvData={...this.datas[index]};
                this.createDialog=true;
            },
            //改变该套餐的状态
            changeState(index){
                this.createMemberLvData={...this.datas[index]};
                this.createMemberLvData.state=this.createMemberLvData.state==1 ? 2 : 1;
                this.remainMemberLv();
            },


            //打开新建会员等级弹窗
            showCreateDialog(){
                for(let x in this.createMemberLvData){
                    this.createMemberLvData[x]='';
                }
                this.createDialog=true;
            },
            //验证新建会员等级数据
            certCreateLv(){
                let certErrorMsg={
                    lvlName : '请输入等级名称',
                    totalConsume : '请输入正确的累计金额',
                    discount : '请输入正确的会员折扣',
                };
                for(let x in certErrorMsg){
                    let data=this.createMemberLvData[x]+'';
                    let totalConsume=x=='totalConsume'  && !data.isInReg(/^\d+(\.\d+)?$/);
                    let discount=false;
                    if(x=='discount'){
                        discount=!data.isInReg(/^\d+(\.\d+)?$/);
                        if(!discount){
                            discount=parseFloat(data)>10;
                        }
                    }
                    if(isEmpty(data) || totalConsume || discount){
                        this.$message({
                            type: 'error',
                            message: certErrorMsg[x],
                            duration: 1200
                        });
                        return false;
                    }
                }
                return true;
            },
            //创建或修改会员等级
            createLv(){
                if(!this.certCreateLv()) return;
                let num=this.createMemberLvData.discount+'';
                this.createMemberLvData.discount=num.indexOf('.')>-1 ? num.substring(0,num.indexOf('.')+2) : num;
                if(this.createMemberLvData.id){
                    this.remainMemberLv();
                }else{
                    this.$ajax(
                        this.$joggle.merchant.businessPackage.insertMemberLvl,
                        this.createMemberLvData,
                        true,
                        (data,loading) => {
                            if (data.code === 'ZS011000') {
                                this.getDatas();
                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                })
                                this.createDialog=false;
                            } else {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                })
                            }
                        }
                    )
                }

            },
            //修改会员等级
            remainMemberLv(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateMemberLvl,
                    this.createMemberLvData,
                    true,
                    (data,loading) => {
                        if (data.code === 'ZS011000') {
                            this.getDatas();
                            this.$message({
                                type: 'success',
                                message: data.msg,
                                duration: 1200
                            });
                            this.createDialog=false;
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            }

        },
        mounted () {
            this.getDatas();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
