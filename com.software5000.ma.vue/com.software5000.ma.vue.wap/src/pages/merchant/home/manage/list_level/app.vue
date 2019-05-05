<template>
    <div class="level-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">会员等级<span class="new-project" @click="handleNewProject">新建会员等级</span></ma-head>
        <div class="record-body">
            <ul v-if="formDatas.length > 0" class="record-ul">
                <li v-for="(item,index) in formDatas" :key="index">
                    <div class="row-one">
                        <div class="vip">
                            <zs-icon icon="member" size="17" :text="item.lvlName"></zs-icon>
                        </div>
                        <div class="cost">单次充值：{{item.rechargeMoney || 0}}</div>
                    </div>
                    <div class="row-two">
                        <zs-switch class="switch-btn" v-model="item._do" @change="handleSwitchBtn(index)"></zs-switch>
                        <span class="switch-h3" v-if="item._do">启用</span>
                        <span class="switch-h4" v-else>停用</span>
                        <zs-icon class="make" icon="write" text="编辑" size="12" @click="handleEditMsg(index)"></zs-icon>
                    </div>
                </li>
                <div
                        class="show_all_css"
                        :class="[{'show_more_css' : hasNextPage}]"
                        @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                </div>
            </ul>

            <div v-else>
                <ul class="data-empty">
                    <li>暂无会员等级信息</li>
                </ul>
            </div>
        </div>

        <!--下一页-->
        <zs-slide-page class="ProjectPage" v-model="newProjectPage" :title="revamp == true ? '修改会员等级' : '新建会员等级'"
                       @go-home="handleGoHome">
            <div class="newProject-page">
                <div class="new-body">
                    <ul>
                        <li>
                            <div class="li-title"><em>*</em> 等级名称</div>
                            <zs-input :maxlength="10" placeholder="请输入会员等级名称" v-model="lvlName"></zs-input>
                        </li>
                        <li>
                            <div class="li-title"><em>*</em> 单次充值</div>
                            <zs-input :maxlength="25" placeholder="请输入该等级单次充值金额" v-model="rechargeMoney"></zs-input>
                        </li>
                    </ul>
                </div>
                <div class="new-bottom">
                    <zs-button class="btn-click" type="primary" @click="handleJudge">确定</zs-button>
                </div>
            </div>
        </zs-slide-page>
    </div>
</template>

<script type="text/ecmascript-6">
    import { turnToNextPage, isEmpty } from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';

    export default {
        components: {
            maHead
        },
        data() {
            return {
                isLoading: false,
                formDatas: [],
                formDatasIndex : 0,
                hasNextPage: false,
                startPage: 1,
                pageSize: 10,
                revamp: false,
                newProjectPage: false,
                lvlName: '',
                rechargeMoney: '',
                presonMsg: []
            };
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/
            /**
             *  新增会员等级
             */
            insertNewMemberLv() {
                let condition = {
                    lvlName: this.lvlName,
                    rechargeMoney: this.rechargeMoney
                };

                this.$ajax(
                    this.$joggle.business.insertMemberLvl,
                    condition,
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.newProjectPage = false;
                            this.selectGetFormDatas(false);
                            this.$message({
                                type: 'success',
                                message: data.msg,
                                duration: 1200
                            });
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
            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/
            /**
             *  修改会员等级
             */
            updateRemainMemberLv() {
                this.$ajax(
                    this.$joggle.business.updateMemberLvl,
                    this.presonMsg,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            Object.assign(this.formDatas[this.formDatasIndex],this.presonMsg);
                            this.newProjectPage = false;
                            this.$message({
                                type: 'success',
                                message: data.msg,
                                duration: 1200
                            });
                        } else {
                            if(!this.newProjectPage){
                                this.formDatas[this.formDatasIndex].state=this.presonMsg.state == 1 ? 2 : 1;
                                this.formDatas[this.formDatasIndex]._do=!this.presonMsg._do;
                            }
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
             *  获取等级列表数据
             * @param isShowMore  是否加载下一页数据
             */
            selectGetFormDatas(isShowMore) {
                if (isShowMore) {
                    this.startPage++;
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.business.selectMemberLvlList,
                    {
                        startPage: this.startPage,
                        pageSize: this.pageSize
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            const temp = isShowMore ? this.formDatas.concat(data.data.list) : data.data.list;

                            temp.forEach((item) => {
                                this.$set(item, '_do', item.state == 1);
                            });
                            this.formDatas = temp;
                            this.hasNextPage = data.data.hasNextPage;
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
            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             *  效验会员等级值是否正确
             */
            handleJudge() {
                let errorMsg = '';

                if (isEmpty(this.lvlName)) {
                    errorMsg = '请输入等级名称';
                } else if (isEmpty(this.rechargeMoney)) {
                    errorMsg = '请输入充值金额';
                } else if (!Number(this.rechargeMoney) || Number(this.rechargeMoney)<=0) {
                    errorMsg = '请输入正确的充值金额';
                }
                if (errorMsg != '') {
                    this.$message({
                        type: 'error',
                        duration: 1000,
                        message: errorMsg
                    });
                    return false;
                }
                this.rechargeMoney=Number(this.rechargeMoney).toFixed(2);
                //判断新增还是修改
                if (this.revamp) {
                    this.presonMsg.lvlName = this.lvlName;
                    this.presonMsg.rechargeMoney = this.rechargeMoney;
                    this.updateRemainMemberLv();
                } else {
                    this.insertNewMemberLv();
                }
            },
            /* 打开新建会员等级弹窗 */
            handleNewProject() {
                this.newProjectPage = true;
                this.lvlName = '';
                this.rechargeMoney = '';
            },
            /**
             *  打开修改会员等级弹窗
             * @param index  该会员等级列表的index
             */
            handleEditMsg(index) {
                let preson = { ...this.formDatas[index] };

                this.formDatasIndex=index;
                this.presonMsg = preson;
                this.lvlName = preson.lvlName;
                this.rechargeMoney = preson.rechargeMoney;
                this.revamp = true;
                this.newProjectPage = true;
            },
            /* 启动开关 */
            handleSwitchBtn(index) {
                let preson = { ...this.formDatas[index] };

                this.formDatasIndex=index;
                preson.state = preson.state == 1 ? 2 : 1;
                this.presonMsg = preson;
                this.updateRemainMemberLv();
            },
            /* 点击查看更多 */
            handleShowMore() {
                if (this.hasNextPage) {
                    const loading = this.$loading();

                    this.selectGetFormDatas(true, () => {
                        loading.close();
                    });
                }
            },
            //返回首页
            handleGoHome() {turnToNextPage('/wap/merchant/home/manage/index.html');}
            /* ------------------- handle (操) end -------------------*/
        },
        created() {
            this.selectGetFormDatas(false);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
