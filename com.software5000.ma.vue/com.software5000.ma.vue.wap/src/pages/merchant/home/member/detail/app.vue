<template>
    <div class="member-info" v-show="!isLoading">
        <ma-head @home-click="handleGoHome" rightIconText="办理套餐" @right-icon-click="handleBuyPackage">客户详情</ma-head>
        <zs-tab v-model="currTab" :tabs="tabs"></zs-tab>
        <div class="member-info-1" v-show="currTab==2">
            <div class="member-detail">
                <ul>
                    <li @click="handleRealNameDialog" :class="{'arrow-right' : updateFlag}">
                        <zs-icon icon="user" text="姓名" size="14"></zs-icon>
                        {{ member.user.realName }}
                    </li>
                    <li @click="handleopenMobileDialog" :class="{'arrow-right' : updateFlag}">
                        <zs-icon icon="mobile" text="手机号" size="14"></zs-icon>
                        {{ member.user.mobile }}
                    </li>
                    <li class="arrow-right" @click="remarkVisible = true">
                        <zs-icon icon="remark" text="客户备注" size="14"></zs-icon>
                        {{ member.remarks }}
                    </li>
                    <li>
                        <zs-icon icon="money" text="会员余额" size="14"></zs-icon>
                        {{ member.memberBalance || 0 }}
                    </li>
                    <li class="arrow-right">
                        <zs-icon icon="dengji" text="会员等级" size="14"></zs-icon>
                        <span>{{member.memberLvlId ? member.memberLvl.lvlName : '普通会员'}}</span>
                        <select @change="updateEditMemberLvl" v-model="memberLvlId" ref="lvName" v-if="!isLoading">
                            <option value="">普通会员</option>
                            <option v-for="lvl in memberLvlList" :value="lvl.id" :key="lvl.id">{{ lvl.lvlName}}</option>
                        </select>
                    </li>
                </ul>
            </div>
            <div class="member-car">
                <ul>
                    <li class="member-car-btn">
                        <zs-icon icon="serving" text="车辆" size="25"></zs-icon>
                        <a @click="handleGoCar" v-if="updateFlag">添加更换</a>
                    </li>
                    <li class="member-car-num">
                        <span v-for="(car,index) in member.user.cars" key="car.id">{{car.carNumber}}</span>
                    </li>
                </ul>
            </div>
            <div class="member-pack">
                <ul class="pack-info">
                    <li>
                        <zs-icon icon="pack" text="套餐信息" size="25"></zs-icon>
                    </li>
                </ul>
                <ul class="pack-detail" v-if="packages.length > 0">
                    <li class="item" v-for="p in packages" :key="p.id" :class="handleListState(p)">
                        <div class="item-head">
                            <p class="head-info">
                                <span class="item-name"
                                      v-if="p.businessPackage">{{ p.businessPackage.packageName }}</span>
                                <span class="item-name" v-else></span>
                                <zs-icon class="item-time" icon="time" size="13" icon-dis="5"
                                         :text="handleValidTime(p)"></zs-icon>
                            </p>
                            <p class="head-info">
                                <zs-icon icon="wechat" size="13" icon-dis="5" text="微信付款"
                                         v-if="p.payType && p.payType == 2"></zs-icon>
                                <zs-icon icon="cash" size="13" icon-dis="5" text="现金付款" v-else></zs-icon>
                                <span class="item-pay">￥{{p.totalAmount || p.businessPackage.salePrice}}</span>
                                <zs-icon class="item-time" icon="time" size="13" icon-dis="5"
                                         :text="handlePayTime(p)"></zs-icon>
                            </p>
                        </div>
                        <div class="service-info">
                            <p v-for="(item,index) in p.memberItemUseRecords" :key="item.id"
                               v-show="index < 3 || p.showAll">
                                {{ item.serviceItem.itemName }}
                                <span>
                            <i>总 {{item.useTimes}}</i> -
                            <i>用 {{item.useTimes - item.remainTimes}}</i> -
                            <i>余 {{item.remainTimes}}</i>
                        </span>
                            </p>
                        </div>
                        <p class="show-more-item" v-show="!p.showAll && p.memberItemUseRecords.length>3"
                           @click="p.showAll = true">
                            查看更多</p>
                    </li>
                </ul>
                <ul v-else>
                    <li class="list-empty">暂无套餐</li>
                </ul>
            </div>
        </div>
        <div class="member-info-2" v-show="currTab==1">
            <div class="pay-search arrow-right">
                <zs-icon icon="order-pay" text="工单消费记录" :size="25"></zs-icon>
                <span class="gray">(共{{total}}条)</span>
                <span class="car-search-text r">{{searchKey ? searchKey : '所有车辆'}}</span>
                <select class="car-search " name="" id="" v-model="searchKey" @change="selectPayList(()=>{},false)">
                    <option value="">不限</option>
                    <option :value="item.carNumber" v-for="(item,index) in member.user.cars" :key="index">
                        {{item.carNumber}}
                    </option>
                </select>
            </div>
            <ul class="item-list" :class="[{'is-operator' : isOperator}]">
                <template v-if="workOrders.length > 0">
                    <li class="list" v-for="order in workOrders" :key="order.id" @click="handleCheck(order.id)">
                        <div class="list-info">
                            <zs-icon class="car-number"
                                     icon="car"
                                     size="20"
                                     icon-dis="10"
                                     :text="order.carNumber"></zs-icon>
                        </div>
                        <ul class="items">
                            <li
                                    v-for="(item,index) in order.workOrderDetails"
                                    v-if="index < 3">
                                {{ `${item.serviceItemName}${isOperator ? '' : ' * ' + item.itemsTimes}` }}
                                <span>{{ isOperator ? `x ${item.itemsTimes}` : item.workerName || '' }}</span>
                            </li>
                            <li v-if="order.workOrderDetails.length > 3">···</li>
                        </ul>
                        <div class="control">
                            <zs-icon
                                    :icon="['','cash-pay','wechat-pay'][order.payType]"
                                    :text="['','现金收款','微信收款'][order.payType]"
                                    icon-dis="6"
                                    size="17"></zs-icon>
                            <span class="total-price">￥{{ order.totalPrice }}</span>
                        </div>
                        <div class="control" v-if="isOperator && order.state == 4">
                            <span class="operator-detail">项目金额<i>{{ calcItemsPrice(order.workOrderDetails) }}</i></span>
                            <!--<span class="operator-detail">提成金额<i> -&#45;&#45; </i></span>-->
                        </div>
                    </li>
                    <li
                            class="show-more"
                            :class="[{'no-more' : !hasNextPage}]"
                            @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                    </li>
                </template>
                <li v-else class="list-empty">暂无消费记录</li>
            </ul>
        </div>

        <!--修改姓名弹窗-->
        <zs-dialog class="edit-dialog" v-model="realNameVisible" show-close>
            <p>姓名</p>
            <p>
                <zs-input placeholder="请输入姓名" v-model="realName"></zs-input>
            </p>
            <ul slot="footer">
                <li>
                    <div @click="realNameVisible = false">取消</div>
                </li>
                <li>
                    <div @click="handleEditRealName">确定</div>
                </li>
            </ul>
        </zs-dialog>

        <!--修改手机号弹窗-->
        <zs-dialog class="edit-dialog" v-model="mobileVisible" show-close>
            <p>手机号</p>
            <p>
                <zs-input placeholder="请输入手机号" v-model="mobile"></zs-input>
            </p>
            <ul slot="footer">
                <li>
                    <div @click="mobileVisible = false">取消</div>
                </li>
                <li>
                    <div @click="handleEditMobile">确定</div>
                </li>
            </ul>
        </zs-dialog>

        <!--客户备注弹窗-->
        <zs-dialog class="edit-dialog" v-model="remarkVisible" show-close>
            <p>客户备注</p>
            <p>
                <zs-input :maxlength="5" placeholder="请输入客户备注" v-model="remarks"></zs-input>
            </p>
            <ul slot="footer">
                <li>
                    <div @click="remarkVisible = false">取消</div>
                </li>
                <li>
                    <div @click="updateEditRemark">确定</div>
                </li>
            </ul>
        </zs-dialog>
    </div>
</template>

<script type="text/ecmascript-6">

    import {
        turnToNextPage,
        getDataFromParam,
        isMobile,
        isEmpty,
        isName,
        DateUtils
    } from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';

    export default {
        components: {
            maHead
        },
        props: {},
        data() {
            return {
                isLoading: true,
                recordId: '',
                currTab: 1,
                classTab: 1, //用来控制显示用完或者过期的标志，值同currTab，延迟赋值
                tabs: [
                    {
                        title: '消费记录'
                    },
                    {
                        title: '客户信息'
                    }
                ],
                updateFlag: true,
                member: {
                    user: {}
                },
                memberLvlList: [],
                userId: '',
                //修改姓名
                realNameVisible: false,
                realName: '',
                //修改手机号
                mobileVisible: false,
                mobile: '',
                //会员等级
                memberLvlId: '',
                memberLv: '',
                //客户备注
                remarkVisible: false,
                remarks: '',
                //套餐
                packages: [],
                //工单数据
                isOperator: false,
                workOrders: [],
                startPage: 1,
                pageSize: 10,
                total: '',
                searchKey: '',
                hasNextPage: false
            }
        },
        computed: {},
        watch: {
            remarkVisible(val) {
                val && (this.remarks = this.member.remarks);
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 修改手机
             */
            updateEditMobile() {
                this.$ajax(
                    this.$joggle.merchant.member.updateMemberInfo,
                    { userId: this.userId, mobile: this.mobile },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.mobileVisible = false;
                            this.member.user.mobile = this.mobile;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            /**
             * 修改会员等级
             */
            updateEditMemberLvl() {
                //校验手机号
                if (!this.member.user.mobile) {
                    this.$message({
                        type: 'error',
                        message: '请先填写手机号'
                    });
                    return false;
                }

                this.$ajax(
                    this.$joggle.merchant.member.updateMemberInfo,
                    { memberLvlRecordId: this.recordId, memberLvlId: this.memberLvlId },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            //获取会员信息
                            this.selectData();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            /**
             * 修改客户备注
             */
            updateEditRemark() {
                this.$ajax(
                    this.$joggle.merchant.member.updateMemberInfo,
                    { memberLvlRecordId: this.recordId, remarks: this.remarks },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.remarkVisible = false;
                            this.member.remarks = this.remarks;
                            this.$message({
                                type: 'successs',
                                message: '操作成功'
                            });
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 查找会员信息
             */
            selectData() {
                this.$ajax(
                    this.$joggle.merchant.member.selectMemberDetailInfo,
                    { memberLvlRecordId: this.recordId },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.member = data.data;
                            this.userId = this.member.userId;
                            this.realName = this.member.user.realName ? this.member.user.realName : '';
                            this.mobile = this.member.user.mobile ? this.member.user.mobile : '';
                            this.memberLvlId = this.member.memberLvlId;
                            this.selectMemberPackages();
                            this.selectPayList(() => {
                            }, false);

                            //设置是否可以修改
                            if (this.member.user.wxOpenId) {
                                this.updateFlag = false;
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        setTimeout(() => {
                            this.isLoading = false
                        }, 100);
                    }
                )
            },
            /**
             * 获取套餐
             */
            selectMemberPackages() {
                this.$ajax(
                    this.$joggle.merchant.member.selectMemberBusinessPackage,
                    { userId: this.userId },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            let finalData = [];

                            data.data.forEach((d) => {
                                d.showAll = false;
                                finalData.push(d);
                            });
                            this.packages = finalData;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            /**
             * 获取会员等级列表
             */
            selectMemberLvlList() {
                this.$ajax(
                    this.$joggle.business.selectMemberLvlList,
                    { pageSize: 500, state: 1 },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.memberLvlList = data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            /**
             * 消费记录
             * @param fn
             * @param isShowMore
             */
            selectPayList(fn, isShowMore) {
                if (isShowMore) {
                    this.startPage++;
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderByPage,
                    {
                        state: '4',
                        startPage: this.startPage,
                        pageSize: this.pageSize,
                        userId: this.userId,
                        searchKey: this.searchKey
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.hasNextPage = data.data.hasNextPage;
                            this.total = data.data.total;
                            this.workOrders = isShowMore ? this.workOrders.concat(data.data.list) : data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        fn && fn();
                    }
                )
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 跳转首页
             */
            handleGoHome() {
                turnToNextPage('/wap/merchant/home/manage/index.html');
            },
            /**
             * 跳转购买套餐
             */
            handleBuyPackage() {
                turnToNextPage('/wap/merchant/home/member/buy_package.html', { mobile: this.mobile });
            },
            /**
             * 跳转至车牌编辑页面
             */
            handleGoCar() {
                if (!this.member.user.mobile) {
                    this.$message({
                        type: 'error',
                        message: '请先填写手机号'
                    });
                    return false;
                }
                turnToNextPage('/wap/merchant/home/member/bing_car.html', { recordId: this.recordId });
            },
            /**
             * 打开修改姓名的弹窗
             */
            handleRealNameDialog() {
                if (this.updateFlag) {
                    this.realNameVisible = true;
                }
            },
            /**
             * 修改姓名
             */
            handleEditRealName() {
                if (isEmpty(this.realName)) {
                    this.$message({
                        type: 'error',
                        message: '请输入姓名'
                    });
                    return false;
                }
                if (!isName(this.realName)) {
                    this.$message({
                        type: 'error',
                        message: '姓名只能输入数字、字母或汉字'
                    });
                    return false;
                }
                this.$ajax(
                    this.$joggle.merchant.member.updateMemberInfo,
                    { userId: this.userId, realName: this.realName },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.realNameVisible = false;
                            this.member.user.realName = this.realName;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            /**
             * 打开修改手机号的弹窗
             */
            handleopenMobileDialog() {
                if (this.updateFlag) {
                    this.mobileVisible = true;
                }
            },
            /**
             * 修改手机号
             */
            handleEditMobile() {
                if (isEmpty(this.mobile)) {
                    this.$message({
                        type: 'error',
                        message: '请输入手机号'
                    });
                    return false;
                }
                if (!isMobile(this.mobile)) {
                    this.$message({
                        type: 'error',
                        message: '请输入正确的手机号'
                    });
                    return false;
                }
                //判断手机号是否已经存在
                if (this.member.user.mobile !== this.mobile) {
                    this.$ajax(
                        this.$joggle.merchant.member.selectCarNumber,
                        {
                            mobile: this.mobile,
                            carNumber: this.member.user.cars[0].carNumber.toUpperCase()
                        },
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.updateEditMobile();
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: '该手机号已经存在'
                                });
                            }
                        }
                    )
                }

            },
            /**
             * 设置有效期
             */
            handleValidate(createTime, validateTime) {
                if (isEmpty(validateTime)) {
                    return '永久'
                }
                let t1 = createTime.replace(/\D+/g, ',').split(',');
                let t2 = validateTime.replace(/\D+/g, ',').split(',');

                return `${t2[0] - t1[0]}年`;
            },
            /**
             * 查看详情
             */
            handleCheck(id) {
                turnToNextPage('/wap/merchant/home/workorder/detail.html', { id });
            },
            /**
             * 查看更多
             */
            handleShowMore() {
                if (this.hasNextPage) {
                    this.pageSize = 10;
                    this.selectPayList(() => {
                    }, true);
                }
            },
            /**
             * 获取购买时间
             * @param p
             */
            handlePayTime(p) {
                return `购买时间：` + p.createTime.slice(0, 10);
            },
            /**
             * 获取到期时间
             * @param p
             */
            handleValidTime(p) {
                let validTime = `永久`;

                if (p.businessPackage && p.businessPackage.validTerm) {
                    if (p.businessPackage.validTerm > 0) {
                        validTime = p.businessPackage.validTerm + `年`;
                    }
                }
                return `有效期：` + validTime;
            },
            /**
             * 套餐状态
             * @param data
             */
            handleListState(data) {
                let ret = '';
                let usedTime = 0;
                let now = new Date().getTime();
                let passTime = data.validTime ? DateUtils.dateToRegular(data.validTime).getTime() : '';
                let item = data.memberItemUseRecords;

                for (let x in item) {
                    usedTime += item[x].remainTimes;
                }
                if (usedTime === 0) {
                    ret = 'use-up';
                } else if (passTime && now > passTime) {
                    ret = 'time-out';
                }
                return ret;
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created() {
            //获取记录ID
            this.recordId = getDataFromParam('id');

            //获取会员信息
            this.selectData();

            //获取会员等级列表
            this.selectMemberLvlList();
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
