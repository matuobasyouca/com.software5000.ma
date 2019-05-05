<template>
    <div class="services-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">服务项目<span class="new-project" @click="projectClick">新建项目</span></ma-head>
        <div class="services-body">
            <ul v-if="datas.length > 0" class="services-ul">
                <li v-for="(item,index) in datas" :key="index">
                    <div class="row-top">
                        <div class="sort">{{item.itemTypeDes}}类</div>
                        <div class="mid">
                            <p>{{item.itemName}}</p>
                            <em>￥{{item.salePrice}}</em>
                        </div>
                        <zs-switch v-model="item._top" @change="isTop(index)"></zs-switch>
                        <span class="top" :class="[{'curr':item._top}]">置顶</span>
                    </div>
                    <div class="row-bottom">
                        <zs-icon icon="write" text="编辑" @click="setAgain(index)"></zs-icon>
                        <zs-icon icon="delete3" text="删除" @click="deleteBtn(index)"></zs-icon>
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
                    <li>暂无服务项目信息</li>
                </ul>
            </div>
        </div>
        <zs-dialog
                class="delete-dialog"
                v-model="showDeleteDialog"
                show-close>
            <div class="dialog-body">
                <div class="money-img"></div>
                <p class="h3">是否删除该项目？</p>
            </div>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="showDeleteDialog=false">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="deleteMsg">确定
                </zs-button>
            </template>
        </zs-dialog>

        <!--下一页-->
        <zs-slide-page class="ProjectPage" v-model="newProjectPage" :title="revamp == true ? '修改项目' : '新建项目'"
                       @go-home="handleGoHome">
            <div class="newProject-page">
                <div class="new-body">
                    <ul>
                        <li>
                            <div class="li-title"><em>*</em> 项目名称</div>
                            <zs-input :maxlength="10" placeholder="请输入项目名称" v-model="newProject.itemName"></zs-input>
                            <span class="li-length">{{newProject.itemName.length}}/10</span>
                        </li>
                        <li>
                            <div class="li-title"><em>*</em> 价格</div>
                            <zs-input :maxlength="25" placeholder="请输入项目价格" v-model="newProject.salePrice"></zs-input>
                        </li>
                        <li>
                            <div class="li-title"><em>*</em> 所属分类</div>
                            <select class="li-select" v-model="newProject.itemType" placeholder="请选择所属分类">
                                <option value="">请选择所属分类</option>
                                <option v-for="(it,key,index) in ServiceItemType" :value="it[0]" :key="index">
                                    {{it[1]}}类
                                </option>
                            </select>
                            <!-- 请选择异常结果-->
                            <zs-icon class="li-deal" icon="arrow-right" size="10"></zs-icon>
                        </li>
                    </ul>
                </div>
                <div class="new-bottom">
                    <zs-button class="btn-click" type="primary" @click="insertJudge">确定</zs-button>
                </div>
            </div>
        </zs-slide-page>
    </div>
</template>

<script>
    import { turnToNextPage, isEmpty } from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead
        },
        data(){
            return {
                isLoading: false,
                searchTable: {
                    orderBy: 'topState desc,sort asc,updateTime desc',
                    startPage: 1,
                    pageSize: 10,
                    searchValue: '', //根据名称搜索
                    itemType: ''
                },
                datas: [],
                hasNextPage: false,
                showDeleteDialog: false,
                id: '',
                newProject: {
                    itemName: '',
                    salePrice: '',
                    originalPrice: '',
                    itemType: '',
                    sort: 1,
                    topState: 0
                },
                ServiceItemType: [],
                newProjectPage: false,
                revamp: false,
                getIndex: 0 //临时存储index
            };
        },
        methods: {
            getDates(isShowMore){
                if (isShowMore) {
                    this.searchTable.startPage++;
                } else {
                    this.searchTable.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.setting.selectPageServiceItem,
                    this.searchTable,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            document.body.scrollTop = 0;
                            this.hasNextPage = data.data.hasNextPage;
                            const temp = isShowMore ? this.datas.concat(data.data.list) : data.data.list;

                            temp.forEach((item) => {
                                this.$set(item, '_top', item.topState == 1);
                            });
                            this.datas = temp;
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
            updateMessage(msg){
                let condition = msg;

                if (condition.salePrice) {
                    condition.salePrice = condition.salePrice.toString().split('.')[1] ? parseFloat(condition.salePrice).toFixed(2) : parseFloat(condition.salePrice);
                }
                this.$ajax(
                    this.$joggle.merchant.setting.updateServiceItem,
                    condition,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.newProjectPage = false;
                            this.$message({
                                type: 'success',
                                duration: 1200,
                                message: this.revamp ? '修改成功' : condition.topState == 1 ? '置顶成功' : '取消置顶成功'
                            });
                        } else {
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: this.revamp ? data.msg : condition.topState == 1 ? '置顶失败' : '取消置顶失败'
                            });
                        }
                    }
                );
            },
            deleteItem(isCan){
                if (isCan) {
                    this.$ajax(
                        this.$joggle.merchant.setting.deleteServiceItem,
                        { id: this.id },
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code == 'ZS011000') {
                                this.showDeleteDialog = false;
                                this.getDates();
                                this.$message({
                                    type: 'success',
                                    duration: 1200,
                                    message: '删除成功'
                                });
                            } else {
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                });
                            }
                        }
                    );
                } else {
                    this.showDeleteDialog = false;
                    this.$message({
                        type: 'error',
                        duration: 1200,
                        message: '服务项正在被使用，无法删除'
                    });
                }
            },
            deleteMsg(){
                this.$ajax(
                    this.$joggle.merchant.setting.selectWhetherServiceItemEdit,
                    { id: this.id },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.deleteItem(data.data);
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
            insertProject(){
                this.newProject.salePrice = parseFloat(this.newProject.salePrice).toFixed(2);
                this.newProject.originalPrice = this.newProject.salePrice;
                this.$ajax(
                    this.$joggle.merchant.setting.insertServiceItem,
                    this.newProject,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.$message({
                                type: 'success',
                                duration: 1200,
                                message: '新建成功'
                            });
                            this.newProjectPage = false;
                            this.getDates();
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
            insertJudge(){
                let _num = /^\d+(\.\d+)?$/;
                let errorMsg = '';

                if (isEmpty(this.newProject.itemName)) {
                    errorMsg = '请输入项目名称';
                } else if (isEmpty(this.newProject.salePrice)) {
                    errorMsg = '请输入项目价格';
                } else if (!_num.test(this.newProject.salePrice)) {
                    errorMsg = '请输入正确的价格';
                } else if (isEmpty(this.newProject.itemType)) {
                    errorMsg = '请选择项目所属分类';
                }
                if (errorMsg != '') {
                    this.$message({
                        type: 'error',
                        duration: 1000,
                        message: errorMsg
                    });
                    return false;
                }

                this.newProject.originalPrice = this.newProject.salePrice;
                //判断新增还是修改，如果id为空表示新增，
                if (this.revamp) {
                    let changeCondition = {};

                    changeCondition = { ...this.newProject };
                    changeCondition.id = this.id;
                    this.$set(this.datas[this.getIndex], 'itemName', this.newProject.itemName);
                    this.$set(this.datas[this.getIndex], 'salePrice', this.newProject.salePrice.toString().split('.')[1] ? parseFloat(this.newProject.salePrice).toFixed(2) : parseFloat(this.newProject.salePrice));
                    this.$set(this.datas[this.getIndex], 'originalPrice', this.newProject.salePrice.toString().split('.')[1] ? parseFloat(this.newProject.salePrice).toFixed(2) : parseFloat(this.newProject.salePrice));
                    this.$set(this.datas[this.getIndex], 'itemType', this.newProject.itemType);
                    this.$set(this.datas[this.getIndex], 'itemTypeDes', this.ServiceItemType[this.newProject.itemType - 1][1]);
                    this.$set(this.datas[this.getIndex], 'sort', this.newProject.sort);
                    this.$set(this.datas[this.getIndex], 'topState', this.newProject.topState);
                    this.updateMessage(changeCondition);
                } else {
                    this.insertProject();
                }
            },
            /* 新建套餐按钮 */
            projectClick(){
                this.newProject.itemName = '';
                this.newProject.salePrice = '';
                this.newProject.itemType = '';
                this.revamp = false;
                this.newProjectPage = true;
            },
            /* 删除 */
            deleteBtn(index){
                let person = this.datas[index];

                this.id = person.id;
                this.showDeleteDialog = true;
            },
            /* 点击编辑 */
            setAgain(index){
                let person = this.datas[index];

                this.getIndex = index;
                this.newProject.itemName = person.itemName;
                this.newProject.salePrice = person.salePrice;
                this.newProject.originalPrice = person.originalPrice;
                this.newProject.itemType = person.itemType + '';
                this.newProject.sort = person.sort;
                this.newProject.topState = person.topState;
                this.id = person.id;

                this.revamp = true;
                this.newProjectPage = true;
            },
            /* 置顶 */
            isTop(index){
                let person = this.datas[index];
                let condition = {};

                condition.itemName = person.itemName;
                condition.salePrice = person.salePrice;
                condition.originalPrice = person.originalPrice;
                condition.itemType = person.itemType;
                condition.sort = person.sort;
                condition.id = person.id;
                condition.topState = person.topState == 0 ? 1 : 0;
                this.datas[index].topState = person.topState == 0 ? 1 : 0;
                this.updateMessage(condition);
            },
            /* 点击查看更多 */
            handleShowMore(){
                if (this.hasNextPage) {
                    this.getDates(true);
                }
            },
            //返回首页
            handleGoHome(){turnToNextPage('/wap/merchant/home/manage/index.html');}
        },
        created(){
            this.getDates();

            //获取下拉筛选数据
            let data = { codeTypes: 'ServiceItemType' };

            this.$ajax(
                this.$joggle.mzscp.selectConstantTypes,
                JSON.stringify(data),
                false,
                (data) => {
                    if (data.code == 'ZS011000') {
                        let datas = data.data;
                        this.ServiceItemType = datas.ServiceItemType;
                    } else {
                        this.$message({
                            type: 'error',
                            duration: 1200,
                            message: data.msg
                        });
                    }
                });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
