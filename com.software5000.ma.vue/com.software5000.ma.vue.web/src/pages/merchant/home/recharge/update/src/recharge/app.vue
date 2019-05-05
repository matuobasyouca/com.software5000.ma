<template>
    <div class="page-main page-main-w620">
        <div class="page-left">
            <div class="page-left-top">
                <!--车牌关键词搜索-->
                <div class="search-wrapper-box">
                    <div class="search-wrapper" v-clickoutside="_=>{this.userResultVisible = false}">
                        <zs-input icon="circle-cross" v-model.trim="memberListCondition.keyWord"
                                  @input="handleMemberList"
                                  placeholder="请输入手机号、车牌关键词进行搜索"></zs-input>
                        <zs-icon
                                class="search-label"
                                :class="[{'is-open':userResultVisible}]"
                                :icon="userResultVisible ? 'user2':'user1'"
                                size="18"
                                :icon2="`arrow-${userResultVisible ? 'top':'bottom'}`"
                                size2="8"
                                icon-dis="6"
                                @click="userResultVisible = !userResultVisible"></zs-icon>
                        <div class="search-result" v-show="userResultVisible">
                            <zs-table
                                    :data="userDatas"
                                    :columns="userColumns"
                                    :context="context"
                                    :no-data-colspan="4"
                                    no-data-text="暂无客户"
                                    no-data-icon="data-empty-member"
                                    @on-row-click="handleEmployeeSelect"
                                    border
                            ></zs-table>
                            <zs-pagination
                                    :current="memberListCondition.startPage"
                                    :total="userTotal"
                                    :page-size="memberListCondition.pageSize"
                                    show-total
                                    @on-change="handlePageChange"
                            ></zs-pagination>
                        </div>
                    </div>
                </div>
                <div class="list" :class="{'have-member' : isMember}">
                    <div class="list-item">
                        <div class="list-item-text"><span class="required">手机号</span></div>
                        <div class="list-item-con">
                            <zs-input
                                    class="list-item-con-input"
                                    :maxlength="11"
                                    @input="selectSearchMobile"
                                    v-model.trim="member.user.mobile" placeholder="请输入客户手机号"></zs-input>
                        </div>
                    </div>
                    <div class="list-item">
                        <div class="list-item-text">姓名</div>
                        <div class="list-item-con">
                            <zs-input class="list-item-con-input" :disabled="memberName"
                                      v-model.trim="member.user.realName"
                                      placeholder="请输入客户姓名"></zs-input>
                        </div>
                    </div>
                    <div class="list-item">
                        <div class="list-item-text"><span class="required">车牌</span></div>
                        <div class="list-item-con add-car" v-show="!isMember">
                            <div class="add-car-box list-item-con-input" v-clickoutside="_=>{this.handleClosePop()}">
                                <span class="add-car-box-text" @click.stop="handleShowPop('carAreaShow')">{{carNumItems.carNumArea}}<i
                                        class="arrow-bottom"></i></zs-icon></span>
                                <span class="add-car-box-text" @click.stop="handleShowPop('carLetterShow')">{{carNumItems.carNumLetter}}<i
                                        class="arrow-bottom"></i></zs-icon></span>
                                <input class="add-car-box-input" v-model.trim="carNumItems.carNumCode" maxlength="5"
                                       type="text" placeholder="请输入车牌号">
                                <div class="add-car-floating add-car-source clr" v-show="carAreaShow">
                                    <div class="floating-item"
                                         :class="carNumItems.carNumArea==item.slice(1,2) ? 'curr' : '' "
                                         @click="handleSelectArea(item)" v-for="(item,index) in carNumArea"
                                         :key="index">
                                        {{item}}
                                    </div>
                                </div>
                                <div class="add-car-floating add-car-word clr" v-show="carLetterShow">
                                    <div class="floating-item" :class=" carNumItems.carNumLetter==item ? 'curr' : '' "
                                         @click="handleSelectLetter(item)" v-for="(item,index) in carNumLetter"
                                         :key="index">
                                        {{item}}
                                    </div>
                                </div>
                            </div>
                            <zs-button type="primary" class="list-item-add" @click="insertCarNum">添加绑定</zs-button>
                        </div>
                        <div class="member-cars" v-if="member.cars.length!=0">
                            <div class="car-num" v-for="(item,index) in member.cars" @click="deleteCarNum(index)"
                                 :key="index">
                                <p>{{item.carNumber.slice(0,2)}}<span
                                        class="spot">·</span>{{item.carNumber.slice(2)}}<span class="sign">×</span></p>
                            </div>
                        </div>
                    </div>
                    <div class="list-item" :class="{ 'recharge-money' : member.cars.length!=0 }">
                        <div class="list-item-text"><span class="required">充值金额</span></div>
                        <div class="list-item-con">
                            <zs-input class="list-item-con-input" v-model.trim="insertBuyRecordPara.reChargeMoney"
                                      placeholder="请输入充值金额"></zs-input>
                        </div>
                    </div>
                    <div class="list-item">
                        <div class="list-item-text">赠送金额</div>
                        <div class="list-item-con">
                            <zs-input class="list-item-con-input" v-model.trim="insertBuyRecordPara.grantMoney"
                                      placeholder="请输入赠送金额"></zs-input>
                        </div>
                    </div>
                    <div class="list-item list-item-last">
                        <div class="list-item-con">
                            <zs-button type="primary" class="list-item-save" @click="insertBuyRecord">去收款</zs-button>
                        </div>
                    </div>

                </div>


            </div>
        </div>
    </div>
</template>

<script>
    import {
        isEmpty,
        turnToNextPage,
        getDataFromParam,
        isMobile,
        isCarNum
    } from '../../../../../../../assets/js/utils';
    import clickoutside from '../../../../../../../components/src/directives/clickoutside';

    export default {
        name: 'buy',
        directives: { clickoutside },
        data() {
            return {
                id: '',
                mobile: '',
                context: this,
                isMember: false,
                memberListCondition: {
                    startPage: 1,
                    pageSize: 8,
                    keyWord: ''
                },
                //客户查询
                userResultVisible: false,
                userDatas: [],
                userColumns: [
                    {
                        title: '车牌',
                        key: 'carNumber',
                        render(row) {
                            let ret = '';
                            let arr = row.carNumber.split(',');

                            arr.forEach((data, index) => {
                                if (index < 2) {
                                    if (arr.length > 2 && index == 1) {
                                        ret += `<p>${data}...</p>`;
                                    } else {
                                        ret += `<p>${data}</p>`;
                                    }
                                }
                            });
                            return ret;
                        }
                    },
                    {
                        title: '客户姓名',
                        key: 'realName'
                    },
                    {
                        title: '手机号',
                        key: 'mobile'
                    },
                    {
                        title: '会员等级',
                        key: 'lvlName',
                        render(row) {
                            let ret = '';

                            if (!isEmpty(row.recordId)) {
                                ret = !isEmpty(row.lvlName) ? row.lvlName : '普通会员';
                            } else {
                                ret = '非会员';
                            }
                            return ret;
                        }
                    }
                ],
                userTotal: 0,

                member: {
                    id: '',
                    user: {
                        mobile: '',
                        realName: ''
                    },
                    cars: []
                },
                memberName: false,

                carNumItems: {
                    carNumArea: '闽',
                    carNumLetter: 'D',
                    carNumCode: ''
                },
                carAreaShow: false,
                carLetterShow: false,
                carNumArea: [
                    '（京）北京', '（津）天津', '（沪）上海', '（渝）重庆', '（冀）河北', '（豫）河南',
                    '（云）云南', '（辽）辽宁', '（黑）黑龙江', '（湘）湖南', '（皖）安徽', '（鲁）山东',
                    '（新）新疆', '（苏）江苏', '（浙）浙江', '（赣）江西', '（鄂）湖北', '（桂）广西',
                    '（甘）甘肃', '（晋）山西', '（蒙）内蒙古', '（陕）陕西', '（吉）吉林', '（闽）福建',
                    '（贵）贵州', '（粤）广东', '（青）青海', '（藏）西藏', '（川）四川', '（宁）宁夏', '（琼）海南'
                ],
                carNumLetter: [
                    'A', 'B', 'C', 'D', 'E',
                    'F', 'G', 'H', 'I', 'J',
                    'K', 'L', 'M', 'N', 'O',
                    'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y',
                    'Z'
                ],

                insertBuyRecordPara: {
                    reChargeMoney: '',
                    grantMoney: ''
                },

                isKeyUp: false
            };
        },
        computed: {},
        methods: {
            /* ------------------- insert (增) start -------------------*/
            /**
             *  添加车牌号
             */
            insertCarNum() {
                let errorMsg = '';
                let carNum = this.carNumItems.carNumArea + this.carNumItems.carNumLetter + this.carNumItems.carNumCode;

                if (!isMobile(this.member.user.mobile)) {
                    errorMsg = '请先完善上面的手机号';
                } else if (this.member.cars.length > 9) {
                    errorMsg = '最多添加10辆车';
                } else if (!isCarNum(carNum)) {
                    errorMsg = '请完善车牌号！';
                } else if (JSON.stringify(this.member.cars).indexOf(carNum) > -1) {
                    errorMsg = '该车牌号已添加！';
                }
                if (errorMsg) {
                    this.$message({
                        type: 'error',
                        message: errorMsg,
                        duration: 1200
                    });
                } else {
                    let para = {
                        carNumber: carNum.toUpperCase(),
                        mobile: this.member.user.mobile
                    };

                    this.$ajax(
                        this.$joggle.merchant.member.selectCarNumber,
                        para,
                        true,
                        (data, model) => {
                            model.close();
                            this.carNumItems.carNumCode = '';
                            if (data.code === 'ZS011000') {
                                this.member.cars.push(para);
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                });
                            }
                        }
                    );
                }
            },
            /**
             *  新增购买套餐记录
             */
            insertBuyRecord() {
                if (!this.handleCertBuyData()) return;
                let para = {
                    realName: this.member.user.realName
                };

                para = { ...this.insertBuyRecordPara, ...para };
                para.grantMoney = para.grantMoney ? Number(para.grantMoney).toFixed(2) : 0;
                para.reChargeMoney = Number(para.reChargeMoney).toFixed(2);
                if (this.isMember) {
                    para.userId = this.member.user.userId;
                } else {
                    para.mobile = this.member.user.mobile;
                    let cars = '';

                    for (let x in this.member.cars) {
                        cars += this.member.cars[x].carNumber + ',';
                    }
                    para.carNums = cars.slice(0, cars.length - 1);
                }
                this.$ajax(
                    this.$joggle.merchant.member.insertRechargeOrder,
                    para,
                    true,
                    (data, model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg,
                                duration: 1200
                            });
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/recharge/detail.html', { id: data.data });
                            }, 1200);
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
            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/
            /**
             *  删除车牌号
             * @param index  具体的车牌号位置
             */
            deleteCarNum(index) {
                if (this.isMember) return;
                this.member.cars.splice(index, 1);
            },
            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/

            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/
            /**
             *  根据会员等级记录id查询会员信息
             * @param fn  ajax完成后的函数块
             * @param load  是否显示loading图
             */
            selectMemberData(fn, load) {
                let loading = !isEmpty(load);

                this.$ajax(
                    this.$joggle.merchant.member.selectMemberDetailInfo,
                    {
                        memberLvlRecordId: this.id
                    },
                    loading,
                    (data, model) => {
                        if (loading) model.close();
                        if (data.code === 'ZS011000') {
                            if (data.data && !isEmpty(data.data.user.mobile)) {
                                this.member.id = data.data.id;
                                this.member.user = data.data.user;
                                this.member.user.userId = data.data.userId;
                                this.member.cars = data.data.user.cars;

                                this.memberName = !isEmpty(data.data.user.realName) || !isEmpty(data.data.user.wxOpenId);
                                this.isMember = true;
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/member/list.html');
                            }, 1200);
                        }
                        fn && fn();
                    }
                );

            },
            /**
             *  会员列表信息
             */
            selectMemberListData() {
                this.$ajax(
                    this.$joggle.merchant.member.selectBusinessUserByParam,
                    JSON.stringify(this.memberListCondition),
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.userDatas = data.data.list;
                            this.userTotal = data.data.total;
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
            /**
             *  根据输入的手机号进行搜索该会员信息
             * @param val  当前输入的手机号
             * @param fn  ajax完成后的函数块
             * @param load  是否显示loading图
             */
            selectSearchMobile(val, fn, load) {
                let loading = isEmpty(load);

                this.isMember = false;
                this.member.user.realName = '';
                this.member.cars = [];
                this.member.userId = '';
                this.memberName = false;

                if (val.length == 11 && !this.isKeyUp) {
                    this.isKeyUp = true;
                    if (isMobile(val)) {
                        this.$ajax(
                            this.$joggle.merchant.member.selectBusinessUserByParam,
                            {
                                startPage: 1,
                                pageSize: 8,
                                keyWord: val
                            },
                            loading,
                            (data, loads) => {
                                this.isKeyUp = false;
                                if (loading) loads.close();
                                if (data.code === 'ZS011000') {
                                    if (data.data.list.length != 0) {
                                        this.member.user = data.data.list[0];
                                        this.memberName = !isEmpty(data.data.list[0].realName) || !isEmpty(data.data.list[0].wxOpenId);
                                        this.isMember = true;
                                        let cars = data.data.list[0].carNumber.split(',');

                                        cars.forEach((carNum) => {
                                            this.member.cars.push({ carNumber: carNum });
                                        });
                                    }
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: data.msg,
                                        duration: 1200
                                    });
                                }
                                fn && fn();
                            }
                        );
                    } else {
                        this.$message({
                            type: 'error',
                            message: '请输入正确的手机号',
                            duration: 1200
                        });
                        setTimeout(() => {this.isKeyUp = false;}, 1200);
                        fn && fn();
                    }
                } else {
                    fn && fn();
                }
            },
            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             *  输入后搜索会员信息
             * @param val  当前输入值
             */
            handleMemberList(val) {
                if (val.length < 2) {
                    this.userDatas = [];
                    this.userTotal = 0;
                    return;
                }
                this.memberListCondition.startPage = 1;
                this.userResultVisible = true;
                this.selectMemberListData();
            },
            /**
             *  切换所搜框的页面
             * @param currPage  搜索会员信息的当前页
             */
            handlePageChange(currPage) {
                this.memberListCondition.startPage = currPage;
                this.selectMemberListData();
            },
            /**
             *  选中员工
             * @param data  选中的数据
             */
            handleEmployeeSelect(data) {
                this.member.user = data;
                this.memberName = !isEmpty(data.realName) || !isEmpty(data.wxOpenId);
                this.isMember = true;
                this.userResultVisible = false;
                this.member.cars = [];
                let cars = data.carNumber.split(',');

                cars.forEach((carNum) => {
                    this.member.cars.push({ carNumber: carNum });
                });
            },
            /**
             *  车牌号的选取地区
             * @param item  选中的地区值
             */
            handleSelectArea(item) {
                this.carNumItems.carNumArea = item.slice(1, 2);
                this.handleClosePop();
            },
            /**
             *  车牌号的选取字母
             * @param item  选中的选取字母
             */
            handleSelectLetter(item) {
                this.carNumItems.carNumLetter = item;
                this.handleClosePop();
            },
            /**
             *  关闭车牌选择弹窗
             */
            handleClosePop() {
                this.carAreaShow = false;
                this.carLetterShow = false;
            },
            /**
             *  选择对应下拉窗口
             * @param obj  展开的位置
             */
            handleShowPop(obj) {
                let boolean = this[obj];

                this.handleClosePop();
                if (!boolean) {
                    this[obj] = true;
                }
            },
            /**
             *  检验新增数据
             * @returns {Boolean} 验证是否正确
             */
            handleCertBuyData() {
                let errorMsg = '';

                if (!isMobile(this.member.user.mobile)) {
                    errorMsg = '请输入正确的手机号';
                } else if (!this.isMember && this.member.cars.length === 0) {
                    errorMsg = '请添加车牌号';
                } else if (!Number(this.insertBuyRecordPara.reChargeMoney)) {
                    errorMsg = '请输入正确的充值金额';
                } else if (this.insertBuyRecordPara.grantMoney) {
                    if (!Number(this.insertBuyRecordPara.grantMoney)) {
                        errorMsg = '请输入正确的赠送金额';
                    }
                }

                if (errorMsg) {
                    this.$message({
                        type: 'error',
                        message: errorMsg,
                        duration: 1200
                    });
                    return false;
                }
                return true;
            },
            /**
             *  页面跳转
             * @param url  跳转地址
             * @param para  跳转带的参数
             */
            handleTurnPage(url, para) {
                if (!para) {
                    turnToNextPage(url);
                }
            }
            /* ------------------- handle (操) end -------------------*/
        },
        created() {
            this.id = getDataFromParam('id');
            this.mobile = getDataFromParam('mobile');
            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                if (this.id) {
                    this.selectMemberData(() => {
                        resolve();
                    }, 'on');
                } else if (this.mobile) {
                    this.selectSearchMobile(this.mobile, () => {
                        resolve();
                    }, 'on');
                } else {
                    resolve();
                }
            });

            Promise.all([f1]).then(() => {
                loading.close();
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
