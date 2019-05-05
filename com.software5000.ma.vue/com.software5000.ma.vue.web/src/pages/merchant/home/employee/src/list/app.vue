<template>
    <div class="employee">
        <div class="employee-content">
            <zs-button type="primary" @click="handleShowDialog">新建员工</zs-button>
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
                        :current="getListCondition.startPage"
                        :total="totalPage"
                        :page-size="getListCondition.pageSize"
                        show-totalPage
                        show-elevator
                        show-sizer
                        @on-change="handlePageNum"
                        @on-page-size-change="handlePageSize"
                ></zs-pagination>
            </div>
            <!-- 新建员工 -->
            <zs-dialog
                    class="em-dialog"
                    v-model="showNewEmployeeDialog"
                    show-close>
                <p v-if="revamp" slot="title">修改员工</p>
                <p v-else slot="title">新建员工</p>
                <div class="dialog-body">
                    <zs-form :model="employeeForm">
                        <ul class="dialog-input">
                            <li>
                                <zs-input :maxlength="25" placeholder="请输入员工姓名"
                                          v-model="employeeForm.realName"></zs-input>
                            </li>
                            <li><em>*</em> 员工姓名</li>
                        </ul>
                        <ul class="dialog-input">
                            <li>
                                <zs-input :maxlength="11" placeholder="员工手机号将作为登录账号"
                                          v-model="employeeForm.mobile"></zs-input>
                            </li>
                            <li><em>*</em> 手机号</li>
                        </ul>
                        <ul class="dialog-sex">
                            <li>
                                <zs-radio-group v-model="employeeForm.sex">
                                    <zs-radio label="男">男</zs-radio>
                                    <zs-radio label="女">女</zs-radio>
                                </zs-radio-group>
                            </li>
                            <li>性别</li>
                        </ul>
                        <ul class="dialog-input">
                            <li>
                                <zs-input :maxlength="10" placeholder="请输入底薪" v-model="employeeForm.salary"></zs-input>
                            </li>
                            <li>底薪</li>
                        </ul>
                        <ul class="dialog-input" v-if="!revamp">
                            <li>
                                <zs-input :maxlength="25" placeholder="字母或者数字组合" v-model="employeeForm.pwd"></zs-input>
                            </li>
                            <li><em>*</em> 登录密码</li>
                        </ul>
                        <ul class="dialog-limits">
                            <li>
                                <zs-select
                                        class="select-form__select"
                                        placeholder="请选择员工"
                                        v-model="employeeForm.mercharType"
                                        @change="">
                                    <zs-option value="merchant" label="管理员"></zs-option>
                                    <zs-option value="operator" label="操作员"></zs-option>
                                </zs-select>
                            </li>
                            <li><em>*</em> 项目权限</li>
                        </ul>
                        <ul v-if="employeeForm.mercharType == 'operator'">
                            <li>
                                <zs-checkbox-group v-model="employeeForm.itemTypes">
                                    <zs-checkbox v-for="(item,index) in recordServiceItemType" :key="index"
                                                 :label="item[0]">{{item[1]}}
                                    </zs-checkbox>
                                </zs-checkbox-group>
                            </li>
                            <li><em>*</em> 开单项目</li>
                        </ul>
                    </zs-form>
                </div>
                <template slot="footer">
                    <zs-button
                            type="cancel"
                            @click="showNewEmployeeDialog=false">取消
                    </zs-button>
                    <zs-button
                            type="primary"
                            @click="handleInsertEmployee">确定
                    </zs-button>
                </template>
            </zs-dialog>
            <!-- 修改密码 -->
            <zs-dialog
                    class="em-dialog"
                    v-model="showResetPwdDialog"
                    show-close>
                <p slot="title">修改密码</p>
                <div class="dialog-body">
                    <zs-form>
                        <ul class="dialog-input">
                            <li>
                                <zs-input :maxlength="25" :disabled="true" :value="userName"></zs-input>
                            </li>
                            <li>登录账号</li>
                        </ul>
                        <ul class="dialog-input">
                            <li>
                                <zs-input :maxlength="25" placeholder="请输入新密码" v-model="employeeForm.pwd"></zs-input>
                            </li>
                            <li>密码</li>
                        </ul>
                    </zs-form>
                </div>
                <template slot="footer">
                    <zs-button
                            type="cancel"
                            @click="showResetPwdDialog=false;">取消
                    </zs-button>
                    <zs-button
                            type="primary"
                            @click="handleChangePwd">确定
                    </zs-button>
                </template>
            </zs-dialog>
        </div>
    </div>
</template>

<script>
    import { isEmpty, isMobile, joinByComma, sepByComma } from '../../../../../../assets/js/utils';
    export default{
        name: 'list',
        data(){
            return {
                datas: [],
                context: this,
                columns: [
                    {
                        title: '员工姓名',
                        key: 'realName',
                        align: 'center',
                        width: 166
                    },
                    {
                        title: '性别',
                        key: 'sex',
                        align: 'center',
                        width: 116
                    },
                    {
                        title: '手机号/账号',
                        key: 'mobile',
                        align: 'center',
                        width: 196

                    }, {
                        title: '底薪',
                        key: 'salary',
                        align: 'center',
                        width: 218
                    },
                    {
                        title: '状态',
                        key: 'stateDes',
                        align: 'center',
                        width: 230,
                        render(row){
                            let className = row.state == '1' ? '' : 'curr';

                            return `<span class="${className}">${row.stateDes}</span>`;
                        }
                    },
                    {
                        title: '操作',
                        key: 'followTime',
                        align: 'center',
                        className: 'account-operation',
                        width: 326,
                        render(row, col, index){
                            let s = `<a @click="handleAccountRevamp(${index})">修改</a>`;
                            let e = row.state == 1 ? `<a @click="handleJobState(${index})">员工离职</a>` : `<a @click="handleJobState(${index})">启用员工</a>`;
                            let d = `<a @click="handleAccountResetPwd(${index})">重置密码</a>`;

                            return s + e + d;
                        }
                    }
                ],
                totalPage: 0,
                personId: 0,
                userName: '',
                employeeForm: {
                    realName: '',
                    mobile: '',
                    sex: '男',
                    salary: '',
                    pwd: '',
                    mercharType: 'operator', //merchant or operator
                    itemTypes: [], //开单项目id
                    bossType: 2
                },
                getListCondition: {
                    orderBy: 'state asc,createTime desc',
                    startPage: 1,
                    pageSize: 10
                },
                showResetPwdDialog: false, //修改密码dialog
                showNewEmployeeDialog: false, //新建员工dialog
                recordServiceItemType: [], //开单项目下拉值
                revamp: false //修改员工的标致
            };
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 新增员工
             */
            insertNewEmployee(){
                let conditionForm = {};

                conditionForm = this.employeeForm;
                if (this.employeeForm.mercharType == 'merchant') {
                    conditionForm.itemTypes = joinByComma([]);
                } else {
                    conditionForm.itemTypes = joinByComma(this.employeeForm.itemTypes);
                }
                this.$ajax(
                    this.$joggle.merchant.employee.insert,
                    conditionForm,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                duration: 1200,
                                message: data.msg
                            });
                            this.showNewEmployeeDialog = false;
                            this.getListCondition.startPage = 1;
                            this.selectEmloyeeDates();
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
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 更新员工信息
             * @param condition
             */
            updateMessage(condition){
                let conditionForm = {};

                conditionForm = condition;
                if (conditionForm.itemTypes) {
                    if (conditionForm.mercharType == 'merchant') {
                        conditionForm.itemTypes = joinByComma([]);
                    } else {
                        conditionForm.itemTypes = joinByComma(this.employeeForm.itemTypes);
                    }
                }
                this.$ajax(
                    this.$joggle.merchant.employee.update,
                    JSON.stringify(conditionForm),
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.$message({
                                duration: 1200,
                                message: data.msg
                            });
                            this.showNewEmployeeDialog = false;
                            this.selectEmloyeeDates();
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
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 查询员工信息
             */
            selectEmloyeeDates(){
                this.$ajax(
                    this.$joggle.merchant.employee.selectByPage,
                    this.getListCondition,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code == 'ZS011000') {
                            this.datas = data.data.list;
                            this.totalPage = data.data.total;
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
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 新建员工点击弹窗
             */
            handleShowDialog(){
                this.recoverForm();
                this.revamp = false;
                this.showNewEmployeeDialog = true;
            },
            /**
             * 修改新建员工的表单验证
             */
            handleInsertEmployee(){
                let _pwd = /([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+)/g;
                let _num = /^\d+(\.\d+)?$/;
                let errorMsg = '';

                if (isEmpty(this.employeeForm.realName)) {
                    errorMsg = '请输入员工姓名';
                } else if (isEmpty(this.employeeForm.mobile)) {
                    errorMsg = '请输入手机号';
                } else if (isEmpty(this.employeeForm.pwd) && this.revamp == false) {
                    errorMsg = '请输入登录密码';
                } else if (this.employeeForm.mercharType == 'operator' && isEmpty(this.employeeForm.itemTypes)) {
                    errorMsg = '请至少选择一项开单项目';
                } else if (!isMobile(this.employeeForm.mobile)) {
                    errorMsg = '请输入正确手机号';
                } else if (!_pwd.test(this.employeeForm.pwd) && this.revamp == false) {
                    errorMsg = '请输入字母或数字组合密码';
                } else if (!isEmpty(this.employeeForm.salary) && !_num.test(this.employeeForm.salary)) {
                    errorMsg = '您输入的底薪有误';
                }
                if (errorMsg != '') {
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

                    changeCondition = { ...this.employeeForm };
                    changeCondition.id = this.personId;
                    delete changeCondition['pwd'];
                    this.updateMessage(changeCondition);
                } else {
                    this.insertNewEmployee();
                }
            },
            /**
             * 修改员工信息的弹窗操作
             * @param index
             */
            handleAccountRevamp(index){
                this.leadingForm(index);
                this.revamp = true;
                this.showNewEmployeeDialog = true;
            },
            /**
             * 切换在职状态
             * @param index
             */
            handleJobState(index){
                let person = this.datas[index];
                let stateCondition = {};

                stateCondition.id = person.id;
                stateCondition.state = person.state == 1 ? stateCondition.state = 2 : stateCondition.state = 1;
                this.updateMessage(stateCondition);
            },
            /**
             * 修改密码弹窗操作
             * @param index
             */
            handleAccountResetPwd(index){
                this.recoverForm();
                let person = this.datas[index];

                this.userName = person.userName;
                this.personId = person.id;
                this.showResetPwdDialog = true;
            },
            /**
             * 点击确认修改密码的验证
             */
            handleChangePwd(){
                let _pwd = /([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+)/g;
                let pwdCondition = {};

                if (!_pwd.test(this.employeeForm.pwd)) {
                    this.dislogMessage('请输入字母或数字组合账号');
                    return;
                }
                pwdCondition.id = this.personId;
                pwdCondition.pwd = this.employeeForm.pwd;
                this.showResetPwdDialog = false;
                this.updateMessage(pwdCondition);
            },
            /**
             * 恢复空表单状态，辅助操作
             */
            recoverForm(){
                this.employeeForm.realName = '';
                this.employeeForm.mobile = '';
                this.employeeForm.sex = '男';
                this.employeeForm.salary = '';
                this.employeeForm.pwd = '';
                this.employeeForm.mercharType = 'operator';
                this.employeeForm.itemTypes = ['1', '2', '3', '4', '5'];
            },
            /**
             * 把信息导入到表单，辅助操作
             * @param index
             */
            leadingForm(index){
                let person = this.datas[index];

                this.employeeForm.realName = person.realName;
                this.employeeForm.mobile = person.mobile;
                this.employeeForm.sex = person.sex;
                this.employeeForm.salary = person.salary;
                this.employeeForm.mercharType = person.mercharType;
                this.personId = person.id;
                this.employeeForm.itemTypes = person.mercharType == 'operator' ? sepByComma(person.itemTypes) : ['1', '2', '3', '4', '5'];
            },
            recoverForm(){
                this.employeeForm.realName = '';
                this.employeeForm.mobile = '';
                this.employeeForm.sex = '男';
                this.employeeForm.salary = '';
                this.employeeForm.pwd = '';
                this.employeeForm.mercharType = 'operator';
                this.employeeForm.itemTypes = ['1', '2', '3', '4', '5'];
            },
            /**
             * 切换页码
             * @param pageNum
             */
            handlePageNum(pageNum){
                this.getListCondition.startPage = pageNum;
                this.selectEmloyeeDates();
            },
            /**
             * 切换每页的数量
             * @param pageSize
             */
            handlePageSize(pageSize){
                this.getListCondition.startPage = 1;
                this.getListCondition.pageSize = pageSize;
                this.selectEmloyeeDates();
            },
            /**
             * 弹窗，辅助操作
             * @param msg
             */
            dislogMessage(msg){
                this.$message({ message: msg });
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.selectEmloyeeDates();
            //获取下拉筛选数据
            let data = { codeTypes: 'ServiceItemType' };

            this.$ajax(
                this.$joggle.mzscp.selectConstantTypes,
                JSON.stringify(data),
                false,
                (data) => {
                    if (data.code == 'ZS011000') {
                        this.recordServiceItemType = data.data.ServiceItemType;
                    } else {
                        this.$message({
                            type: 'error',
                            duration: 1200,
                            message: data.msg
                        });
                    }
                });
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>