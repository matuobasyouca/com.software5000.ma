<template>
    <div class="page-base main" @click="closePop">
        <div class="list">
            <div class="list-title">
                <zs-icon icon="member" text="基本信息" :size="32"></zs-icon>
            </div>
            <div class="list-lock" v-if="isLoginWeChat">
                <zs-icon icon="danger" text="该客户已绑定个人中心，不能修改其个人信息，请客户自行修改"></zs-icon>
            </div>
            <div class="list-item detail-list-item">
                <div class="list-item-text">姓名</div>
                <div class="list-item-con">
                    <zs-input class="list-item-con-input" v-model="member.realName" :disabled="isLoginWeChat"
                              placeholder="请输入姓名"></zs-input>
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">手机号</div>
                <div class="list-item-con">
                    <zs-input class="list-item-con-input" @input="certOnlyOneMobile" :disabled="isLoginWeChat"
                              v-model="member.mobile" :maxlength="11" placeholder="请输入手机号"></zs-input>
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">客户备注</div>
                <div class="list-item-con">
                    <zs-input class="list-item-con-input" v-model="member.remarks" :maxlength="5"
                              placeholder="请输入客户备注"></zs-input>
                    <span class="member-remarks-count">{{ member.remarks.length }} / 5</span>
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-text">会员等级</div>
                <div class="list-item-con">
                    <zs-select class="list-item-con-input" v-model="member.memberLvlId">
                        <zs-option v-for="(item,index) in memberLvOpt" :key="index" :value="item.id"
                                   :label="item.lvlName"></zs-option>
                    </zs-select>
                </div>
            </div>
            <div class="list-item">
                <div class="list-item-con">
                    <zs-button type="primary" class="list-item-save" @click="saveMember">保存</zs-button>
                </div>
            </div>
            <div class="list-item list-item-bind">
                <div class="list-item-text">绑定车牌</div>
                <div class="list-item-con add-car" v-if="!isLoginWeChat">
                    <div class="add-car-box list-item-con-input">
                        <span class="add-car-box-text" @click.stop="showPop('carAreaShow')">{{carNumItems.carNumArea}}<i
                                class="arrow-bottom"></i></zs-icon></span>
                        <span class="add-car-box-text" @click.stop="showPop('carLetterShow')">{{carNumItems.carNumLetter}}<i
                                class="arrow-bottom"></i></zs-icon></span>
                        <input class="add-car-box-input" @keyup.enter="addCarNum" v-model="carNumItems.carNumCode"
                               maxlength="5" type="text" placeholder="请输入车牌号">
                        <div class="add-car-floating add-car-source clr" v-show="carAreaShow">
                            <div class="floating-item" :class="carNumItems.carNumArea==item.slice(1,2) ? 'curr' : '' "
                                 @click="selectArea(item)" v-for="(item,index) in carNumArea" :key="index">
                                {{item}}
                            </div>
                        </div>
                        <div class="add-car-floating add-car-word clr" v-show="carLetterShow">
                            <div class="floating-item" :class=" carNumItems.carNumLetter==item ? 'curr' : '' "
                                 @click="selectLetter(item)" v-for="(item,index) in carNumLetter" :key="index">
                                {{item}}
                            </div>
                        </div>
                    </div>
                    <zs-button type="primary" class="list-item-add" @click="addCarNum">添加绑定</zs-button>
                </div>
                <div class="member-cars" :class="{'cannot-edit' : isLoginWeChat}">
                    <div class="car-num" v-for="(item,index) in memberCars" @click="delCarNum(index)" :key="index">
                        <p>{{item.carNumber.slice(0,2)}}<span class="spot">·</span>{{item.carNumber.slice(2)}}<span
                                class="sign">×</span></p>
                    </div>
                </div>
            </div>


        </div>
        <div class="list">
            <div class="list-title">
                <zs-icon icon="package-card" text="套餐信息" :size="32"></zs-icon>
            </div>
            <div class="list-table" :class="listState(item)" v-if="datas.length!=0" v-for="(item,index) in datas"
                 :key="index">
                <div class="list-table-title">
                    <div class="list-table-title-left">{{item.businessPackage ? item.businessPackage.packageName :
                        ''}}<span>￥{{item.businessPackage ? item.businessPackage.salePrice : ''}}</span></div>
                    <div class="list-table-title-time">
                        <div class="title-time-top">{{item.createTime ? `购买时间：${item.createTime.slice(0,10)}` : ''}}
                        </div>
                        <div class="title-time-bottom">过期时间：{{ item.validTime ? item.validTime.slice(0,10) : '永不过期'}}
                        </div>
                    </div>
                </div>
                <zs-table
                        class="package-table"
                        :data="item.memberItemUseRecords"
                        :columns="columns"
                        :context="context"
                        :no-data-colspan="10"
                        border>
                </zs-table>
                <div class="hit-icon" v-if="listState(item)">
                    <zs-icon :icon="listState(item)=='list-table-expired' ? 'used' : 'dated'" :size="110"></zs-icon>
                </div>
            </div>
            <div v-if="datas.length==0" class="list-table-empty">
                <zs-icon icon="data-empty" :size="50"></zs-icon>
                <p>暂无套餐</p>
            </div>
        </div>
    </div>
</template>

<script>
    import {
        turnToNextPage,
        isCarNum,
        getDataFromParam,
        isMobile,
        isEmpty,
        doCopy,
        DateUtils
    } from '../../../../../../../assets/js/utils.js';

    export default {
        components: {},
        data() {
            return {
                //基本信息

                oldMobile: '',
                isLoginWeChat: false,
                member: {
                    userId: '',
                    memberLvlId: '',
                    mobile: '',
                    remarks: '',
                    memberLvlRecordId: ''
                },
                memberCars: [],
                memberLvOpt: [],
                datas: [],
                columns: [
                    {
                        title: '套餐项目',
                        key: 'serviceItem',
                        width: 144,
                        render(row) {
                            return row.serviceItem.itemName;
                        }
                    },
                    {
                        title: '套餐次数',
                        key: 'useTimes',
                        width: 330
                    },
                    {
                        title: '剩余次数/已使用次数',
                        key: 'like',
                        width: 234,
                        className: 'control',
                        render(row) {
                            return `${row.remainTimes}/${row.useTimes - row.remainTimes}`;
                        }
                    }
                ],
                context: this,
                currIndex: 1,

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

                current: 1,
                pageSize: 10,
                total: 0,

                isKeyUp: false
            };
        },
        computed: {},
        methods: {
            //根据会员等级记录id查询会员信息
            getMemberData() {
                this.$ajax(
                    this.$joggle.merchant.member.selectMemberDetailInfo,
                    {
                        memberLvlRecordId: this.member.memberLvlRecordId
                    },
                    true,
                    (data, model) => {
                        if (data.code === 'ZS011000') {
                            this.member.userId = data.data.userId;
                            this.member.realName = data.data.user.realName;
                            this.member.mobile = data.data.user.mobile;
                            this.member.remarks = data.data.remarks;
                            this.oldMobile = data.data.user.mobile;
                            this.member.memberLvlId = data.data.memberLvlId;
                            this.memberCars = data.data.user.cars;
                            this.isLoginWeChat = !isEmpty(data.data.user.wxOpenId);
                            this.getMemberPackageData();
                        } else {
                            model.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/member/manage.html');
                            }, 1200);
                        }
                    }
                );

            },
            //获取商家设置等级
            getMemberLvlList() {
                this.$ajax(
                    this.$joggle.merchant.setting.selectMemberLvlList,
                    {
                        starPage: 1,
                        pageSize: 999,
                        state: 1
                    },
                    true,
                    (data, model) => {
                        if (data.code === 'ZS011000') {
                            this.memberLvOpt = data.data.list;
                        } else {
                            model.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            //根据会员id查找该会员套餐
            getMemberPackageData() {
                this.$ajax(
                    this.$joggle.merchant.member.selectMemberBusinessPackage,
                    {
                        userId: this.member.userId
                    },
                    true,
                    (data, model) => {
                        model.close();
                        if (data.code === 'ZS011000') {
                            this.datas = data.data;
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
            //查找该手机号是否为唯一性
            certOnlyOneMobile() {
                if (this.member.mobile.length == 11 && this.oldMobile != this.member.mobile) {
                    if (isMobile(this.member.mobile)) {
                        this.$ajax(
                            this.$joggle.merchant.member.selectCarNumber,
                            {
                                mobile: this.member.mobile,
                                carNumber: this.memberCars[0].carNumber.toUpperCase()
                            },
                            true,
                            (data, model) => {
                                model.close();
                                if (data.code !== 'ZS011000') {
                                    this.member.mobile = '';
                                    this.$message({
                                        type: 'error',
                                        message: '手机号已存在',
                                        duration: 1200
                                    });
                                }
                            }
                        );
                    } else {
                        this.$message({
                            type: 'error',
                            message: '请输入正确的手机号',
                            duration: 1200
                        });
                    }
                }
            },
            //选取地区
            selectArea(item) {
                this.carNumItems.carNumArea = item.slice(1, 2);
                this.closePop();
            },
            //选取字母
            selectLetter(item) {
                this.carNumItems.carNumLetter = item;
                this.closePop();
            },
            //关闭车牌选择弹窗
            closePop() {
                this.carAreaShow = false;
                this.carLetterShow = false;
            },
            //添加车牌号
            addCarNum() {
                if (!this.oldMobile) {
                    this.$message({
                        type: 'error',
                        message: '请输入并保存手机号'
                    });
                    return;
                }
                if (this.isKeyUp) return;
                this.isKeyUp = true;
                let errorMsg = '';
                let carNum = this.carNumItems.carNumArea + this.carNumItems.carNumLetter + this.carNumItems.carNumCode;

                if (this.memberCars.length > 9) {
                    errorMsg = '最多添加10辆车';
                } else if (!isCarNum(carNum)) {
                    errorMsg = '请完善车牌号！';
                } else if (JSON.stringify(this.memberCars).indexOf(carNum) > -1) {
                    errorMsg = '该车牌号已添加！';
                }
                if (errorMsg) {
                    this.$message({
                        type: 'error',
                        message: errorMsg,
                        duration: 1200
                    });
                    setTimeout(() => {
                        this.isKeyUp = false;
                    }, 1200);
                } else {
                    this.$ajax(
                        this.$joggle.merchant.member.insertCarNumberByParam,
                        {
                            userId: this.member.userId,
                            carNumber: carNum,
                            memberLvlRecordId: this.member.memberLvlRecordId
                        },
                        true,
                        (data, model) => {
                            model.close();
                            setTimeout(() => {
                                this.isKeyUp = false;
                            }, 1200);
                            if (data.code === 'ZS011000') {
                                this.memberCars.push(data.data);
                                this.carNumItems.carNumCode = '';
                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                });
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
            //删除车牌号
            delCarNum(index) {
                if (!this.oldMobile) {
                    this.$message({
                        type: 'error',
                        message: '请先绑定手机号'
                    });
                    return;
                }
                if (this.isLoginWeChat) return;
                if (this.memberCars.length < 2) {
                    this.$message({
                        type: 'error',
                        message: '至少需留1辆车',
                        duration: 1200
                    });
                    return;
                }
                this.$confirm({
                    customClass: 'shut-down-confirm',
                    type: 'warning',
                    title: '删除',
                    showClose: true,
                    message: '确定删除该车？'
                }).then(() => {
                    this.$ajax(
                        this.$joggle.merchant.member.updateCarStateByParam,
                        {
                            userId: this.member.userId,
                            carNumber: this.memberCars[index].carNumber
                        },
                        true,
                        (data, model) => {
                            model.close();
                            if (data.code === 'ZS011000') {

                                const obj = doCopy(this.memberCars);

                                this.$delete(obj, index);
                                this.memberCars = obj;

                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                });
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                });
                            }
                        }
                    );
                });
            },
            //选择对应下拉窗口
            showPop(obj) {
                let boolean = this[obj];

                this.closePop();
                if (!boolean) {
                    this[obj] = true;
                }
            },
            /**
             * 页码切换
             */
            handlePageChange(currPage) {
                this.current = currPage;
                let loading = this.$loading();

                this.getDatas(() => {
                    loading.close();
                });
            },
            /**
             * 每页数据条数切换
             */
            handlePageSizeChange(pageSize) {
                this.current = 1;
                this.pageSize = pageSize;
                let loading = this.$loading();

                this.getDatas(() => {
                    loading.close();
                });
            },
            //套餐状态
            listState(data) {
                let ret = '';
                let usedTime = 0;
                let now = new Date().getTime();
                let passTime = data.validTime ? DateUtils.dateToRegular(data.validTime).getTime() : '';
                let item = data.memberItemUseRecords;

                for (let x in item) {
                    usedTime += item[x].remainTimes;
                }
                if (usedTime == 0) {
                    ret = 'list-table-expired';
                } else if (passTime && now > passTime) {
                    ret = 'list-table-pass';
                }
                return ret;
            },
            //会员数据验证
            certMember() {
                let errorMsg = {
                    userId: '请以正常路径进入页面'
                };

                if (!isEmpty(this.oldMobile)) {
                    errorMsg.mobile = '请输入正确的手机号';
                }
                for (let x in errorMsg) {
                    let data = this.member[x];
                    let mobile = x == 'mobile' && !isMobile(data);

                    if (isEmpty(data) || mobile) {
                        this.$message({
                            type: 'error',
                            message: errorMsg[x],
                            duration: 1200
                        });
                        return false;
                    }
                }
                return true;
            },
            //更新会员数据
            saveMember() {
                if (!this.certMember()) return;
                this.$ajax(
                    this.$joggle.merchant.member.updateMemberInfo,
                    this.member,
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
                                turnToNextPage('/web/merchant/home/member/manage.html');
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
            }
        },
        mounted() {
            this.getMemberLvlList();
            this.member.memberLvlRecordId = getDataFromParam('id');
            this.getMemberData();
        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
