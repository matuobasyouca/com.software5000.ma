<template>
    <div class="page-main"
         :class="{'page-main-w600' : packageData.length<2 , 'page-main-w940' : packageData.length==2}">
        <div class="page-left">
            <div class="page-left-top">
                <!--车牌关键词搜索-->
                <div class="search-wrapper-box">
                    <div class="search-wrapper" v-clickoutside="_=>{this.userResultVisible = false}">
                        <zs-input icon="circle-cross" v-model="memberListCondition.keyWord" @input="getMemberList"
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
                        <div class="list-item-text">手机号</div>
                        <div class="list-item-con">
                            <zs-input
                                    class="list-item-con-input"
                                    :maxlength="11"
                                    @input="searchMobile"
                                    v-model="member.user.mobile" placeholder="请输入手机号"></zs-input>
                        </div>
                    </div>
                    <div class="list-item">
                        <div class="list-item-text">姓名</div>
                        <div class="list-item-con">
                            <zs-input class="list-item-con-input" :disabled="memberName" v-model="member.user.realName"
                                      placeholder="请输入姓名"></zs-input>
                        </div>
                    </div>
                    <div class="list-item">
                        <div class="list-item-text">绑定车牌</div>
                        <div class="list-item-con add-car" v-show="!isMember">
                            <div class="add-car-box list-item-con-input" v-clickoutside="_=>{this.closePop()}">
                                <span class="add-car-box-text" @click.stop="showPop('carAreaShow')">{{carNumItems.carNumArea}}<i
                                        class="arrow-bottom"></i></zs-icon></span>
                                <span class="add-car-box-text" @click.stop="showPop('carLetterShow')">{{carNumItems.carNumLetter}}<i
                                        class="arrow-bottom"></i></zs-icon></span>
                                <input class="add-car-box-input" v-model="carNumItems.carNumCode" maxlength="5"
                                       type="text" placeholder="请输入车牌号">
                                <div class="add-car-floating add-car-source clr" v-show="carAreaShow">
                                    <div class="floating-item"
                                         :class="carNumItems.carNumArea==item.slice(1,2) ? 'curr' : '' "
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
                        <div class="member-cars">
                            <div class="car-num" v-for="(item,index) in member.cars" @click="delCarNum(index)"
                                 :key="index">
                                <p>{{item.carNumber.slice(0,2)}}<span
                                        class="spot">·</span>{{item.carNumber.slice(2)}}<span class="sign">×</span></p>
                            </div>
                            <div class="no-car-num" v-if="member.cars.length==0">&nbsp;</div>
                        </div>
                    </div>
                    <div class="list-item">
                        <div class="list-item-text">购买套餐</div>
                        <div class="list-item-con clr">
                            <div class="package-box curr" :class="{'more-item' :  packageData[index].isShowMore  }"
                                 v-for="(item,index) in packageData" :key="index" @click="selectPackage(index)">
                                <div class="package-box-top">
                                    <p class="package-box-top-left">{{item.packageName}}<span>￥{{item.salePrice}}</span>
                                    </p>
                                    <p class="package-box-top-right">有效期：{{item.validTerm!=0 ? item.validTerm+'年' :
                                        '永久'}}</p>
                                </div>
                                <ul class="package-box-bottom">
                                    <li v-for="(item2,index2) in item.packageAndItems" :key="index2">
                                        <p class="package-box-bottom-item">{{item2.serviceItem.itemName}}<span
                                                class="r">{{item2.useTimes}}次</span></p>
                                    </li>
                                    <li class="package-box-more" @click.stop="packageShowMore(index)"
                                        v-if="item.packageAndItems.length>3">
                                        <zs-icon rightIcon="arrow-bottom"
                                                 :text="packageData[index].isShowMore ? '收起' : '查看更多'"
                                                 :size="8"></zs-icon>
                                    </li>
                                </ul>
                                <zs-icon class="package-box-select" icon="selected" v-show="packageCurr==index"
                                         :size="22"></zs-icon>
                            </div>
                            <div class="package-box-empty" v-if="packageData.length==0"
                                 @click="turnPage('/web/merchant/home/member/add_package.html','',false)">
                                <p>暂无套餐，点击新建</p>
                            </div>
                        </div>
                    </div>
                    <div class="package-box-Mask" v-show="packageMask" @click="hideMask"></div>
                    <div class="list-item list-item-last">
                        <div class="list-item-con">
                            <zs-button type="primary" class="list-item-save" @click="saveBuy">去收款</zs-button>
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
        data(){
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
                        render(row){
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
                        render(row){
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

                //套餐部分
                packageCurr: -1,
                packageMoreCurr: -1,
                packageData: [],
                packageMask: false,
                member: {
                    id: '',
                    user: {
                        mobile: '',
                        realName: ''
                    },
                    cars: []
                },
                memberName: false,
                memberLvOpt: [{
                    name: 'abc',
                    id: 1,
                    value: 1
                },
                    {
                        name: 'bbb',
                        id: 2,
                        value: 2
                    }
                ],
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

                isKeyUp: false
            };
        },
        computed: {},
        methods: {
            //输入后搜索会员信息
            getMemberList(val){
                if (val.length < 2) {
                    this.userDatas = [];
                    this.userTotal = 0;
                    return;
                }
                this.memberListCondition.startPage = 1;
                this.userResultVisible = true;
                this.getMemberListData();
            },
            //根据会员等级记录id查询会员信息
            getMemberData(fn, load){
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
                                turnToNextPage('/web/merchant/home/member/manage.html');
                            }, 1200);
                        }
                        fn && fn();
                    }
                );

            },
            //获取套餐卡数据
            getPackageList(fn, load){
                let loading = isEmpty(load);

                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageByPage,
                    {
                        starPage: 1,
                        pageSize: 100,
                        packageState: 1
                    },
                    loading,
                    (data, model) => {
                        if (loading) model.close();
                        if (data.code === 'ZS011000') {
                            this.packageData = data.data.list;
                            for (let x in this.packageData) {
                                this.$set(this.packageData[x], 'isShowMore', false);
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/member/manage.html');
                            }, 1200);
                        }
                        fn && fn();
                    }
                );
            },

            //切换页面
            handlePageChange(currPage){
                this.memberListCondition.startPage = currPage;
                this.getMemberListData();
            },
            //选中员工
            handleEmployeeSelect(data){
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
            //会员列表信息
            getMemberListData(){
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
            //选取地区
            selectArea(item){
                this.carNumItems.carNumArea = item.slice(1, 2);
                this.closePop();
            },
            //选取字母
            selectLetter(item){
                this.carNumItems.carNumLetter = item;
                this.closePop();
            },
            //关闭车牌选择弹窗
            closePop(){
                this.carAreaShow = false;
                this.carLetterShow = false;
            },
            //根据输入的手机号进行搜索该会员信息
            searchMobile(val, fn, load){
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
            //添加车牌号
            addCarNum(){
                if (this.isKeyUp) return;
                this.isKeyUp = true;
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
                    setTimeout(() => {this.isKeyUp = false;}, 1200);
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
                            this.isKeyUp = false;
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
            //删除车牌号
            delCarNum(index){
                if (this.isMember) return;
                this.member.cars.splice(index, 1);
            },
            //选择对应下拉窗口
            showPop(obj){
                let boolean = this[obj];

                this.closePop();
                if (!boolean) {
                    this[obj] = true;
                }
            },
            //购买套餐选中
            selectPackage(index){
                this.packageCurr = this.packageCurr != index ? index : -1;
            },
            //购买套餐查看更多
            packageShowMore(index){
                this.packageData[index].isShowMore = !this.packageData[index].isShowMore;
                this.packageMask = this.packageData[index].isShowMore;
                this.packageMoreCurr = index;
            },
            //隐藏蒙版
            hideMask(){
                this.packageMask = false;
                this.packageData[this.packageMoreCurr].isShowMore = false;
            },
            //检验数据
            certBuyData(){
                let errorMsg = '';

                if (!isMobile(this.member.user.mobile)) {
                    errorMsg = '请输入正确的手机号';
                } else if (this.packageCurr == -1) {
                    errorMsg = '请选择一个套餐';
                } else if (!this.isMember && this.member.cars.length == 0) {
                    errorMsg = '请添加车牌号';
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
            //获取套餐卡数据
            saveBuy(){
                if (!this.certBuyData()) return;
                let para = {
                    quantity: 1,
                    packageId: this.packageData[this.packageCurr].id
                };

                if (this.isMember) {
                    para.userId = this.member.user.userId;
                    para.realName = this.member.user.realName;
                } else {
                    para.realName = this.member.user.realName;
                    para.mobile = this.member.user.mobile;
                    let cars = '';

                    for (let x in this.member.cars) {
                        cars += this.member.cars[x].carNumber + ',';
                    }
                    para.carNums = cars.slice(0, cars.length - 1);
                }
                this.$ajax(
                    this.$joggle.merchant.businessPackage.insertBusinessPackageOrder,
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
                                turnToNextPage('/web/merchant/home/member/package_billing.html', { id: data.data });
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
            //页面跳转
            turnPage(url, para){
                if (!para) {
                    turnToNextPage(url);
                }
            }
        },
        created(){
            this.id = getDataFromParam('id');
            this.mobile = getDataFromParam('mobile');
            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                if (this.id) {
                    this.getMemberData(() => {
                        resolve();
                    }, 'on');
                } else if (this.mobile) {
                    this.searchMobile(this.mobile, () => {
                        resolve();
                    }, 'on');
                } else {
                    resolve();
                }
            });
            const f2 = new Promise((resolve) => {
                this.getPackageList(() => {
                    resolve();
                }, 'on');
            });

            Promise.all([f1, f2]).then(() => {
                loading.close();
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
