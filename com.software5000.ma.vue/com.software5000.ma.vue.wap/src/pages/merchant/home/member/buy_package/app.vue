<template>
    <div class="page-main">
        <ma-head @home-click="handleGoHome">办理套餐</ma-head>
        <div class="member-detail">
            <ul>
                <li>
                    <zs-icon icon="mobile" text="手机号" size="16"></zs-icon>
                    <zs-input @input="searchMobile" v-model="member.user.mobile" :maxlength="11"
                              placeholder="请输入手机号"></zs-input>
                </li>
                <li>
                    <zs-icon icon="user" text="姓名" size="16"></zs-icon>
                    <zs-input v-model="member.user.realName" :disabled="memberName" placeholder="请输入姓名"></zs-input>
                </li>
                <li class="bind-car" :class="!isMember ? 'arrow-right' :  '' " @click="openEditCar">
                    <zs-icon icon="car2" text="绑定车牌" size="16"></zs-icon>
                    <span :class="member.cars.length!=0 ? 'text-hide' : 'gray'">{{carNumListHtml}}</span>
                </li>
            </ul>
        </div>
        <div class="member-pack">
            <ul class="open-package-list" v-if="packageData.length > 0">
                <li class="item" v-for="(p,pIndex) in packageData" :key="p.id" :class="{'curr' : packageCurr==pIndex}"
                    @click="packageCurr=pIndex">
                    <div class="item-head">
                        <p class="head-info">
                            <span class="item-name">{{ p.packageName || '' }}</span>
                            <zs-icon icon="time" size="13" class="item-createtime"
                                     icon-dis="6"
                                     :text="`有效期：${p.validTerm!=0 ? p.validTerm+'年' : '永久'  }`"></zs-icon>
                        </p>
                        <p class="head-info">
                            <span class="item-name">￥{{ p.salePrice || '' }}</span>
                        </p>
                    </div>
                    <div class="service-info">
                        <p v-for="(item,index) in p.packageAndItems" :key="item.id" v-show="index < 3">
                            {{ item.serviceItem.itemName}}<span>X{{item.useTimes}}</span>
                        </p>
                        <p class="show-more-item" v-show="p.packageAndItems.length>3">...</p>
                        <div class="curr-icon">
                            <zs-icon icon="check2" size="20"></zs-icon>
                        </div>
                    </div>
                </li>
            </ul>
            <div class="open-package-list-empty" v-if="packageData.length == 0">
                暂无套餐
            </div>
        </div>
        <edit-car
                v-model="showEditCar"
                :carNumList="member.cars"
                :mobile="member.user.mobile"
                @getCarNumList="getCarNumList"
        ></edit-car>
        <div class="page-main-bottom" @click="saveBuy">提交</div>
    </div>
</template>

<script type="text/ecmascript-6">

    import { turnToNextPage, getDataFromParam, isMobile, isEmpty } from '../../../../../assets/js/utils';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import editCar from './src/edit_plate_number/edit_plate_number.vue';

    export default {
        components: {
            maHead,
            editCar
        },
        data() {
            return {
                recordId: '',
                isMember: false,
                member: {
                    userId: '',
                    user: {
                        mobile: '',
                        realName: '',
                        wxOpenId: ''
                    },
                    cars: []
                },
                mobile: '',
                packageData: [],
                showEditCar: false,
                memberName: '',
                packageCurr: -1,

                hasNextPage: false,

                isKeyUp: false
            }
        },
        computed: {
            carNumListHtml(){
                const temp = [];

                for (let i = 0; i < this.member.cars.length; i++) {
                    temp.push(this.member.cars[i].carNumber)
                }
                return temp.join(',') || '请选择';
            }
        },
        methods: {
            //跳回主页
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
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
                    (data, loads) => {
                        if (loading) loads.close();
                        if (data.code === 'ZS011000') {
                            this.packageData = data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                            setTimeout(() => {
                                turnToNextPage('/wap/merchant/home/member/manage.html');
                            }, 1200);
                        }
                        fn && fn();
                    }
                )
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
                                    if (data.data.list.length > 0) {
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
                                    })
                                }
                                fn && fn();
                            }
                        )
                    } else {
                        this.$message({
                            type: 'error',
                            message: '请输入正确的手机号',
                            duration: 1200
                        })
                        setTimeout(() => {
                            this.isKeyUp = false
                        }, 1200);
                        fn && fn();
                    }
                } else {
                    fn && fn();
                }
            },
            //打开添加车辆弹窗
            openEditCar(){
                if (!isMobile(this.member.user.mobile)) {
                    this.$message({
                        type: 'error',
                        message: '请先输入手机号',
                        duration: 1200
                    })
                } else if (!this.isMember) {
                    this.showEditCar = true;
                }
            },
            getData() {
                this.$ajax(
                    this.$joggle.merchant.member.selectMemberDetailInfo,
                    { memberLvlRecordId: this.recordId },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            console.log(this.data);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                )
            },
            //跳转至，消费记录
            handleTabChange() {
            },
            //获取添加的车牌号
            getCarNumList(val){
                this.member.cars = val;
            },
            //设置有效期
            getValidate(createTime, validateTime){
                if (isEmpty(validateTime)) {
                    return '永久'
                }
                let t1 = createTime.replace(/\D+/g, ',').split(',');
                let t2 = validateTime.replace(/\D+/g, ',').split(',');

                return `${t2[0] - t1[0]}年`;
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
                    })
                    return false;
                }
                return true;
            },
            //保存购买套餐
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
                            })
                            setTimeout(() => {
                                turnToNextPage('/wap/merchant/home/member/package_detail.html', { id: data.data })
                            }, 1200);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                )
            }
        },
        created(){
            //获取记录ID
            this.recordId = getDataFromParam('id');
            this.mobile = getDataFromParam('mobile');

            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                if (this.id) {
//                    this.getMemberData(()=>{
                    resolve();
//                    },'on');
                } else if (this.mobile) {
                    this.searchMobile(this.mobile, () => {
                        resolve();
                    }, 'on');
                } else {
                    resolve();
                }
            })
            const f2 = new Promise((resolve) => {
                this.getPackageList(() => {
                    resolve();
                }, 'on');
            })

            Promise.all([f1, f2]).then(() => {
                loading.close();
            })

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
