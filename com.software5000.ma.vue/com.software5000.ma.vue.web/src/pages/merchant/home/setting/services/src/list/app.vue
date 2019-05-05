<template>
    <div class="services-list">
        <div class="services-header">
            <zs-button type="primary" @click="showDialog">新建项目</zs-button>
        </div>
        <div class="list-content">
            <zs-table
                    :data="datas"
                    :columns="columns"
                    :context="context"
                    :no-data-colspan="6"
                    border
                    class="employee-table"
            >
            </zs-table>
        </div>
        <div class="page-div" v-if="datas.length>0">
            <zs-pagination
                    :current="searchTable.startPage"
                    :total="total"
                    :page-size="searchTable.pageSize"
                    show-total
                    show-elevator
                    show-sizer
                    @on-change="changePageNum"
                    @on-page-size-change="changePageSize"
            ></zs-pagination>
        </div>
        <!-- 新建项目 -->
        <zs-dialog
                class="new-dialog"
                v-model="showNewDialog"
                show-close>
            <p v-if="revamp==false" slot="title">新建项目</p>
            <p v-else slot="title">修改项目</p>
            <div class="dialog-body">
                <zs-form>
                    <ul class="dialog-input">
                        <li>
                            <zs-input :maxlength="10" placeholder="请输入服务项目名称" v-model="newProject.itemName"></zs-input>
                        </li>
                        <li class="hint">{{`${newProject.itemName.length}/10`}}</li>
                        <li><em>*</em> 项目名称</li>
                    </ul>
                    <ul class="dialog-input">
                        <li>
                            <zs-input :maxlength="25" placeholder="请输入服务项目价格" v-model="newProject.salePrice"></zs-input>
                        </li>
                        <li><em>*</em> 项目价格</li>
                    </ul>
                    <ul class="dialog-input">
                        <li>
                            <zs-select
                                    class="select-form-select"
                                    placeholder="请选择项目所属分类"
                                    v-model="newProject.itemType"
                                    @change="">
                                <zs-option value="" label="请选择项目所属分类"></zs-option>
                                <zs-option v-for="(val,index) in ServiceItemType" :key="index" :value="val[0]"
                                           :label="val[1]"></zs-option>
                            </zs-select>
                        </li>
                        <li><em>*</em> 所属分类</li>
                    </ul>
                    <ul class="dialog-input">
                        <li>
                            <zs-input :maxlength="10" value="1" v-model="newProject.sort"></zs-input>
                        </li>
                        <li><em>*</em> 排序</li>
                    </ul>
                </zs-form>
            </div>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="showNewDialog=false;">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="insertJudge">确定
                </zs-button>
            </template>
        </zs-dialog>
        <!-- 删除 -->
        <zs-dialog
                class="del-dialog"
                v-model="showDeleteDialog"
                show-close>
            <p slot="title">删除</p>
            <div class="dialog-body">
                <div class="del-img"></div>
                <div class="del-p">确认删除该项目?</div>
            </div>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="showDeleteDialog=false;">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="deleteMsg">确定
                </zs-button>
            </template>
        </zs-dialog>
    </div>
</template>

<script>
    import {isEmpty} from '../../../../../../../assets/js/utils';
    export default{
        name: 'list',
        data(){
            return {
                datas: [],
                context: this,
                columns: [
                    {
                        title: '服务项目',
                        key: 'itemName',
                        align: 'center',
                        width: 284
                    },
                    {
                        title: '价格',
                        key: 'salePrice',
                        align: 'center',
                        width: 244
                    },
                    {
                        title: '所属分类',
                        key: 'itemType',
                        align: 'center',
                        width: 254,
                        render(row, col, index, _this){
                            let s = '';
                            for (let x in _this.ServiceItemType) {
                                if (_this.ServiceItemType[x][0] == row.itemType) {
                                    s = _this.ServiceItemType[x][1];
                                }
                            }
                            return s;
                        },
                    }, {
                        title: '排序',
                        key: 'sort',
                        align: 'center',
                        className: 'item-times',
                        width: 220,
                        render(row, col, index, _this){
                            let s = !row.sortState ? `<p @click="handleOpenInsertItemTimes(${index})">${row.sort ? row.sort : ''}</p>` : `<input
                                        class="item-times"
                                        type="number"
                                        autofocus
                                        key="${row.id}"
                                        value="${row.sort}"
                                        @focus="handleOldInsertItemTimes(${index})"
                                        @blur="handleCloseInsertItemTimes(${index},$event)"
                                        @change="handleChangeItemTimes(${index},$event)">`
                            return s;
                        }
                    },
                    {
                        title: '操作',
                        key: 'topState',
                        align: 'center',
                        className: 'account-operation',
                        width: 250,
                        render(row, col, index, _this){
                            let s = row.topState == 1 ? `<a class="istop" @click="isTop(${index},0)">取消置顶</a>` : `<a class="istop" @click="isTop(${index},1)">置顶</a>`;
                            let e = `<a @click="revampMsg(${index})">修改</a>`;
                            let d = `<a @click="deleteBtn(${index})">删除</a>`;

                            return s + e + d;
                        },
                    }
                ],
                total: 0,
                showNewDialog: false,
                showDeleteDialog: false,
                newProject: {
                    itemName: '',
                    salePrice: '',
                    originalPrice: '',
                    itemType: '',
                    sort: 1,
                    topState: 0
                },
                searchTable: {
                    orderBy: 'topState desc,sort asc,updateTime desc',
                    startPage: 1,
                    pageSize: 10,
                    searchValue: '',     //根据名称搜索
                    itemType: ''
                },
                ServiceItemType: [],   //下拉框的值
                nameLength: 0,
                id: 0,
                revamp: false,         //修改的标致
                sortNum: 0,
                patchNum: 0            //临时数据
            }
        },
        methods: {
            getDates(){
                this.$ajax(
                        this.$joggle.merchant.setting.selectPageServiceItem,
                        this.searchTable,
                        true,
                        (data, loading)=> {
                            loading.close();
                            if (data.code == 'ZS011000') {
                                this.datas = data.data.list;
                                this.total = data.data.total;
                                this.datas.forEach((item)=> {
                                    this.$set(item, 'sortState', false);
                                });
                            } else {
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                })
                            }
                        }
                )
            },
            insertProject(){
                this.newProject.salePrice = parseFloat(this.newProject.salePrice).toFixed(2);
                this.newProject.originalPrice = this.newProject.salePrice;
                this.$ajax(
                        this.$joggle.merchant.setting.insertServiceItem,
                        this.newProject,
                        true,
                        (data, loading)=> {
                            loading.close();
                            if (data.code == 'ZS011000') {
                                this.showNewDialog = false;
                                this.searchTable.startPage = 1;
                                this.getDates();
                                this.$message({
                                    type: 'success',
                                    duration: 1200,
                                    message: '新建成功'
                                })
                            } else {
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                })
                            }
                        }
                )
            },
            deleteItem(isCan){
                if (isCan) {
                    this.$ajax(
                            this.$joggle.merchant.setting.deleteServiceItem,
                            {id: this.id},
                            true,
                            (data, loading)=> {
                                loading.close();
                                if (data.code == 'ZS011000') {
                                    this.showDeleteDialog = false;
                                    this.searchTable.startPage = 1;
                                    this.getDates();
                                    this.$message({
                                        type: 'success',
                                        duration: 1200,
                                        message: '删除成功'
                                    })
                                } else {
                                    this.$message({
                                        type: 'error',
                                        duration: 1200,
                                        message: '删除失败'
                                    })
                                }
                            }
                    )
                } else {
                    this.$message({
                        type: 'error',
                        duration: 1200,
                        message: '服务项正在被使用，无法删除'
                    })
                }
            },
            updateMessage(msg){
                let condition = msg;
                this.$ajax(
                        this.$joggle.merchant.setting.updateServiceItem,
                        condition,
                        true,
                        (data, loading)=> {
                            loading.close();
                            if (data.code == 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    duration: 1200,
                                    message: this.revamp ? '修改成功' : condition.topState == 1 ? '置顶成功' : '取消置顶成功'
                                });
                                this.revamp = false;
                                this.showNewDialog = false;
                            } else {
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: this.revamp ? '修改失败' : condition.topState == 1 ? '置顶成功' : '取消置顶成功'
                                });
                                this.revamp = false;
                                this.showNewDialog = false;
                            }
                        }
                )
            },
            deleteMsg(){
                this.$ajax(
                        this.$joggle.merchant.setting.selectWhetherServiceItemEdit,
                        {id: this.id},
                        true,
                        (data, loading)=> {
                            loading.close();
                            if (data.code == 'ZS011000') {
                                this.deleteItem(data.data);
                            } else {
                                this.$message({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                })
                            }
                        }
                )
            },
            insertJudge(){
                let _num = /(\D)/g;
                let _price = /^\d+(\.\d+)?$/;
                let errorMsg = '';
                if (isEmpty(this.newProject.itemName)) {
                    errorMsg = '请输入项目名称';
                } else if (isEmpty(this.newProject.salePrice)) {
                    errorMsg = '请输入项目价格';
                } else if (!_price.test(this.newProject.salePrice)) {
                    errorMsg = '请输入正确的价格';
                } else if (isEmpty(this.newProject.itemType)) {
                    errorMsg = '请选择项目所属分类';
                } else if (isEmpty(this.newProject.sort)) {
                    errorMsg = '排序不能为空';
                } else if (_num.test(this.newProject.sort) || parseFloat(this.newProject.sort) <= 0) {
                    errorMsg = '请输入正确的排序';
                }
                if (errorMsg != "") {
                    this.$message({
                        type: 'error',
                        duration: 1000,
                        message: errorMsg
                    });
                    return false;
                }
                //判断新增还是修改，如果id为空表示新增，
                if (this.revamp) {
                    let changeCondition = {};
                    changeCondition = {...this.newProject};
                    changeCondition.id = this.id;
                    changeCondition.salePrice = parseFloat(changeCondition.salePrice).toFixed(2);
                    changeCondition.originalPrice = changeCondition.salePrice;

                    this.$set(this.datas[this.patchNum], 'itemName', changeCondition.itemName);
                    this.$set(this.datas[this.patchNum], 'salePrice', changeCondition.salePrice);
                    this.$set(this.datas[this.patchNum], 'originalPrice', changeCondition.originalPrice);
                    this.$set(this.datas[this.patchNum], 'itemType', changeCondition.itemType);
                    this.$set(this.datas[this.patchNum], 'sort', changeCondition.sort);
                    this.$set(this.datas[this.patchNum], 'topState', changeCondition.topState);

                    this.updateMessage(changeCondition);
                } else {
                    this.insertProject();
                }
            },
            //获取原来的sort
            handleOldInsertItemTimes(i){
                this.sortNum = this.datas[i].sort;
            },
            //打开项目次数输入框
            handleOpenInsertItemTimes(i){
                this.datas.forEach((item)=> {
                    this.$set(item, 'sortState', false);
                });
                this.$set(this.datas[i], 'sortState', true)
            },
            //关闭项目次数输入框
            handleCloseInsertItemTimes(i, e){
                this.$set(this.datas[i], 'sortState', false);
                if (isEmpty(e.target.value) || parseInt(e.target.value) < 1) {
                    this.$message({
                        type: 'error',
                        duration: 1200,
                        message: '您输入有误'
                    });
                    this.$set(this.datas[i], 'sort', this.sortNum);
                    return;
                } else if (parseInt(e.target.value) == this.sortNum) {
                    return;
                }
                let person = this.datas[i];
                let condition = {};
                condition.itemName = person.itemName;
                condition.salePrice = person.salePrice;
                condition.originalPrice = person.originalPrice;
                condition.itemType = person.itemType;
                condition.sort = person.sort;
                condition.id = person.id;
                condition.topState = person.topState;
                this.revamp = true;
                this.updateMessage(condition);
            },
            //修改项目次数
            handleChangeItemTimes(i, e){
                this.$set(this.datas[i], 'sort', this.calcItemTimes(e.target.value));
            },
            //控制项目次数不能小于1
            calcItemTimes(i){
                return isEmpty(i) || parseInt(i) <= 0 ? this.sortNum : parseInt(i);
            },
            showDialog(){
                this.recoverForm();
                this.showNewDialog = true;
            },
            /* 删除 */
            deleteBtn(index){
                let person = this.datas[index];
                this.id = person.id;
                this.showDeleteDialog = true;
            },
            /* 置顶 */
            isTop(index,i){
                let person = this.datas[index];
                let condition = {};
                condition.itemName = person.itemName;
                condition.salePrice = person.salePrice;
                condition.originalPrice = person.originalPrice;
                condition.itemType = person.itemType;
                condition.sort = person.topState == 0 ? 1 : person.sort;
                condition.id = person.id;
                condition.topState = person.topState == 0 ? 1 : 0;
                this.$set(this.datas[index],'topState',i);
                this.updateMessage(condition);
            },
            /* 修改 */
            revampMsg(index){
                this.patchNum = index;
                this.leadingForm(index);
                this.revamp = true;
                this.showNewDialog = true;
            },
            /* 恢复空表单状态 */
            recoverForm(){
                this.newProject.itemName = '';
                this.newProject.salePrice = '';
                this.newProject.itemType = '';
                this.newProject.sort = 1;
            },
            /* 把信息导入到表单 */
            leadingForm(index){
                let person = this.datas[index];
                this.newProject.itemName = person.itemName;
                this.newProject.salePrice = person.salePrice;
                this.newProject.originalPrice = person.originalPrice;
                this.newProject.itemType = person.itemType ? person.itemType + '' : '';
                this.newProject.sort = person.sort;
                this.newProject.topState = person.topState;
                this.id = person.id;

            },
            changePageNum(pageNum){
                this.searchTable.startPage = pageNum;
                this.getDates();
            },
            changePageSize(pageSize){
                this.searchTable.startPage = 1
                this.searchTable.pageSize = pageSize;
                this.getDates();
            }
        },
        mounted(){
            //获取下拉筛选数据
            let data = {"codeTypes": 'ServiceItemType'};
            this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    JSON.stringify(data),
                    false,
                    (data) => {
                        if (data.code == 'ZS011000') {
                            let datas = data.data;
                            this.ServiceItemType = datas.ServiceItemType;
                            this.getDates();
                        } else {
                            this.$message({
                                type: 'error',
                                duration: 1200,
                                message: data.msg
                            })
                        }
                    });

        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>