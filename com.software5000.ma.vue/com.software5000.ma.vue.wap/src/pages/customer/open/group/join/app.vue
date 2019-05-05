<template>
    <div class="pay-page">
        <ma-head
                :home="false"
        >拼团
        </ma-head>
        <div class="store-name">
            <zs-icon :size="15" icon="merchant" :text="packCulsterData.businessName"></zs-icon>
        </div>
        <div class="package">
            <div class="package-left" v-if="packCulsterData.packClusterImgs.length>0"
                 :style="`background-image: url(${packCulsterData.shareImgPath ? packCulsterData.shareImgPathUrl : packCulsterData.packClusterImgs[0].imgPathUrl })`"></div>
            <div class="package-right">
                <div class="package-title">
                    <span class="title">{{packCulsterData.clusterName}}</span>
                    <span class="r">{{packCulsterData.clusterNum}}人团</span>
                </div>
                <div class="package-date">
                    {{packCulsterData.businessPackage.packageName}}(有效期{{packCulsterData.businessPackage.validTerm !=0 ?
                    packCulsterData.businessPackage.validTerm+'年': '永久'}})
                </div>
                <div class="package-item">{{packageItemText}}</div>
            </div>
        </div>
        <div class="price">
            <span class="del">原价：￥{{packCulsterData.businessPackage.salePrice}}</span><span class="gray">拼团价</span><span
                class="black">￥{{packCulsterData.salePrice}}</span>
        </div>
        <ul class="input-wrapper">
            <li class="input-wrapper-li">
                <span class="input-label">手机号</span>
                <zs-input :maxlength="11" v-model="mobile" placeholder="请输入手机号" icon="circle-cross"></zs-input>
            </li>
            <li class="input-wrapper-li">
                <car-number @get-car-number="handleGetCarNumber"></car-number>
            </li>
        </ul>
        <div class="footer">
            <div class="footer-main">
                <div class="footer-left">
                    实收款:<span class="red">￥{{packCulsterData.salePrice}}</span>
                </div>
                <div class="confirm-btn" @click="handleConfirm">去支付</div>
            </div>
        </div>
    </div>
</template>

<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';

    import carNumber from '../../../../../components_proj/car_number/app.vue';
    import {
        isCarNum,
        getDataFromParam,
        turnToNextPage,
        isMobile,
        isEmpty
    } from '../../../../../assets/js/utils';
    import { turnToHostPage } from '../../../../../assets/js/turnToHostPage.js';
    import { selectWxCode, selectOpenId, selectWxCodeAgain } from '../../../../../assets/js/wxUtils';

    export default {
        components: {
            carNumber, maHead
        },
        data () {
            return {
                isLoading: true,
                openId: '',
                id: '',
                openRecordId: '',
                packCulsterData: {
                    businessPackage: {}
                },
                businessId: '',
                packId: '',
                mobile: '',
                carNumber: '',
                businessName: '',
                imgSrc: ''
            };
        },
        //定义事件，从缓存上取数据，速度快，但事件不可以带参数，可在html区内直接运行
        computed: {
            //处理卡包包含的各项服务
            packageItemText(){
                let ret = '';
                if (!isEmpty(this.packCulsterData.businessPackage)) {
                    let list = this.packCulsterData.businessPackage.packageAndItems;
                    for (let i = 0; i < list.length; i++) {
                        ret += list[i].serviceItem.itemName + ' *' + list[i].useTimes + '、';
                    }
                    ret = ret.substring(0, ret.length - 1);
                }
                return ret;
            }
        },
        methods: {
            //获取团活动信息
            getData(fn){
                this.$ajax(
                    this.$joggle.customer.packcluster.selectPackClusterInfo,
                    { id: this.id },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            loading.close();
                            this.packCulsterData = data.data;
                            this.images = data.data.packClusterImgs;
                            fn && fn();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            //获取该车牌号
            handleGetCarNumber(carNumber){
                this.carNumber = carNumber;
            },

            //去收款
            handleConfirm(){
                let errorMsg = '';
                if (!isMobile(this.mobile)) {
                    errorMsg = '手机号有误!';
                } else if (!isCarNum(this.carNumber)) {
                    errorMsg = '车牌号码有误!';
                }
                if (errorMsg) {
                    this.$message({
                        type: 'error',
                        message: errorMsg,
                        duration: '1000'
                    });
                } else {
                    this.certJoinGroup(() => {
                        this.handGroupOrder();
                    });
                }
            },
            //验证是否有参过团
            certJoinGroup(fn){
                this.$ajax(
                    this.$joggle.customer.packcluster.selectHaveBuyPackCluster,
                    {
                        packClusterId: this.id,
                        wxOpenId: this.openId,
                        carNumber: this.carNumber,
                        mobile: this.mobile
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            if (data.data) {
                                loading.close();
                                let msg = data.data;
                                msg = '该车牌已被尾号为' + msg.substring(msg.length - 4) + '的手机号绑定，确认继续支付？';
                                this.$confirm({
                                    title: '警告',
                                    message: msg,
                                    customClass: 'join-message'
                                }).then((action) => {
                                    fn && fn();
                                }).catch(() => {
                                });
                            } else {
                                fn && fn();
                            }
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: '1000'
                            });
                        }
                    }
                );
            },
            //生成订单
            handGroupOrder(){
                this.$ajax(
                    this.$joggle.customer.packcluster.insertPackClusterBuyRecordByPay,
                    {
                        'wxOpenId': this.openId,
                        'mobile': this.mobile,
                        'carNumber': this.carNumber,
                        'payMoney': this.packCulsterData.salePrice * 100,
                        'packClusterId': this.id,
                        'openRecordId': this.openRecordId
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            turnToNextPage(data.data);
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: '1000'
                            });
                        }
                    }
                );
            }
        },
        created() {
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            this.id = getDataFromParam('id') ? parseInt(getDataFromParam('id')) : '';
            this.openRecordId = getDataFromParam('openRecordId') ? parseInt(getDataFromParam('openRecordId')) : '';
            if (!this.openId || !this.id) {
                this.$message({
                    type: 'error',
                    message: '请从拼团活动页面进入',
                    duration: '1000'
                });
                setTimeout(() => {
                    turnToHostPage('/open/user_center.html', 'emkt');
                }, 1000);
            }
            this.getData();
        }
    };
</script>
<style lang="less">
    @import 'style.less';
</style>