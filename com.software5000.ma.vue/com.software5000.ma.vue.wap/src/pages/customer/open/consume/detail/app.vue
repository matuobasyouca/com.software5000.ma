<template>
    <div class="consume-detail-page" v-show="!isLoading">
        <div class="manage-header">
            <div>
                <ma-head
                        :home="false"
                        right-icon-text="个人中心"
                        @right-icon-click="handleTurnToUserCenter">消费详情
                </ma-head>

            </div>
            <div class="detail-head">
                <zs-icon
                        :icon="handleStateTypeIcon(detailData.state)"
                        icon-dis="15"
                        size="40"
                        :class="handleStateTypeClass(detailData.state)"
                        :text="handleStateTypeText(detailData.state)"></zs-icon>

                <p class="car-number">服务车辆：{{ detailData.carNumber }}</p>
            </div>
        </div>
        <ul class="detail-info page-margin">
            <li class="info-title">服务项目</li>
            <li
                    class="info-service"
                    v-for="item in detailData.workOrderDetails"
                    :key="item.id">
                <div class="info-service-p1">
                    {{ item.serviceItemName }} <i>x{{ item.itemsTimes }}</i>
                    <span class="user-card" v-if="item.usePackageTimes > 0">用卡 x{{ item.usePackageTimes }}</span>
                    <span class="price">￥{{ item.salePrice }}</span>
                </div>
                <div class="info-service-p2" v-if="item.couponDeduct > 0">
                    卡券抵扣 <i>({{item.couponName}})</i>
                    <span class="price">- ￥{{ item.couponDeduct }}</span>
                </div>
                <div class="info-service-p3" v-if="item.discountPrice > 0">
                    会员扣减
                    <span class="price">- ￥{{ item.discountPrice }}</span>
                </div>
                <div class="info-service-p4">
                    总价
                    <span class="price"> ￥{{ item.totalPrice }}</span>
                </div>
            </li>
            <li class="info-service-sum">
                <!--<template v-if="setPackageDiscount()>0">套餐抵扣 <span>￥{{ setPackageDiscount() }}</span></template>-->
                项目合计金额 <span>￥{{ handleSetOriginalTotalPrice() }}</span>
            </li>
            <li class="info-material" v-if="materialCost > 0">
                <zs-icon
                        icon="附"
                        icon-background="#e75845"
                        size="16"
                        icon-dis="10"
                        text="附加费用"></zs-icon>
                <span class="price">￥{{ materialCost }}</span>
            </li>
            <li class="info-finish first" v-if="showBusinessDeduct">
                <zs-icon
                        icon="减"
                        icon-background="#2cc068"
                        size="16"
                        icon-dis="10"
                        text="商家扣减"></zs-icon>
                <span class="info-deduct">-￥{{ businessDeduct }}</span>
            </li>
            <li class="info-finish first" v-if="detailData.balanceDeduct">
                <zs-icon
                        icon="会"
                        icon-background="#4b8dde"
                        size="16"
                        icon-dis="10"
                        text="会员余额支付"></zs-icon>
                <span class="info-deduct">-￥{{ detailData.balanceDeduct }}</span>
            </li>
            <!--以下兼容旧数据-->
            <template v-if="isFinish">
                <li class="info-finish" v-if="showDiscount">
                    <zs-icon
                            icon="折"
                            icon-background="#04c668"
                            size="16"
                            icon-dis="10"
                            text="会员折扣"></zs-icon>
                    <span class="info-discount-num">{{ detailData.discountNum }}折</span>
                    <span class="info-deduct">-￥{{ detailData.discountDeduct || 0 }}</span>
                </li>
                <template v-if="showCouponDeduct">
                    <li class="info-finish" v-for="coupon in couponShow" v-if="coupon.couponDeduct > 0">
                        <zs-icon
                                icon="券"
                                icon-background="#50b6f0"
                                size="16"
                                icon-dis="10"
                                :text="coupon.couponName || '优惠券'"></zs-icon>
                        <span class="info-deduct">-￥{{ coupon.couponDeduct }}</span>
                    </li>
                </template>
            </template>
            <!--以上兼容旧数据-->
            <li class="info-sum">
                <zs-icon :icon="payType" size="18" :text="payTypeText" v-if="isFinish"></zs-icon>
                <span v-else>总金额</span>
                <span class="info-sum-price">￥{{ detailData.totalPrice }}</span>
            </li>
        </ul>

        <div class="detail-image page-margin" >
            <p class="image-title">施工图片</p>
            <ul class="image-wrap" v-if="detailData.workOrderImgs && detailData.workOrderImgs.length > 0">
                <li v-for="pic in detailData.workOrderImgs" @click="handleViewPic(index)">
                    <i :style="{'backgroundImage':`url(${pic.orderImgSrc})`}"></i>
                </li>
            </ul>
            <p class="image-empty" v-else>未上传图片</p>
        </div>
        <div class="detail-other page-margin">
            <p>工单编号<span>{{ detailData.orderNo }}</span></p>
            <p>新建时间<span>{{ detailData.createTime ? detailData.createTime.slice(0,19) : '' }}</span></p>
            <p v-if="isNopay">完工时间<span>{{ detailData.commitTime ? detailData.commitTime.slice(0,19) : '' }}</span></p>
            <p v-if="isFinish">支付时间<span>{{ detailData.payTime ? detailData.payTime.slice(0,19) : '' }}</span></p>
        </div>
        <!--查看大图-->
        <swiper v-model="imageViewVisible" :images="images" :index="imageIndex"></swiper>
    </div>
</template>
<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import swiper from '../../../../../components_proj/swiper/app.vue';
    import { isEmpty, getDataFromParam } from '../../../../../assets/js/utils';
    import { turnToHostPage } from '../../../../../assets/js/turnToHostPage';
    export default {
        components: {
            maHead,
            swiper
        },
        props: [],
        data() {
            return {
                isLoading: true,
                id: '',
                detailData: {},
                //大图展示
                images: [],
                imageViewVisible: false,
                imageIndex: 0,
                //兼容旧数据
                couponShow: []
            };
        },
        computed: {
            showBusinessDeduct(){
                return this.detailData.businessDeduct && this.detailData.businessDeduct > 0;
            },
            isFinish(){
                return this.detailData.state == 4;
            },
            isNopay(){
                return this.detailData.state == 3;
            },
            materialCost(){
                return this.detailData.materialCost ? parseFloat(this.detailData.materialCost).toFixed(2) : 0;
            },
            businessDeduct(){
                return parseFloat(this.detailData.businessDeduct).toFixed(2);
            },
            payType(){
                return ['', 'cash-pay', 'wechat-pay'][this.detailData.payType];
            },
            payTypeText(){
                return ['', '现金支付', '微信支付'][this.detailData.payType];
            },
            //以下属性兼容旧数据
            showCouponDeduct(){
                return this.detailData.couponDeduct && this.detailData.couponDeduct > 0;
            },
            showDiscount(){
                return this.detailData.discountDeduct && this.detailData.discountDeduct > 0;
            },
            couponDeduct(){
                return parseFloat(this.detailData.couponDeduct).toFixed(2);
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 获取消费详情
             * @param fn
             */
            selectConsumeDetail(fn){
                this.$ajax(
                    this.$joggle.customer.consume.selectConsumeDetailById,
                    { orderId: this.id },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.detailData = data.data.workOrder;

                            //工单图片
                            const imgTemp = this.detailData.workOrderImgs || [];
                            const imgArr = [];

                            imgTemp.forEach((img) => {
                                imgArr.push(
                                    {
                                        id: img.id,
                                        showImg: img.orderImgSrc
                                    }
                                );
                            });
                            this.images = imgArr;

                            //设置优惠券展示信息
                            //-----------------------以下兼容旧数据--------------
                            const couponUuid = this.detailData.couponUuid.split(',');
                            const couponName = this.detailData.couponName.split(',');
                            const couponDeduct = this.detailData.couponEveryDeduct.split(',');

                            for (let i = 0, len = couponUuid.length; i < len; i++) {
                                this.couponShow.push(
                                    {
                                        couponUuid: couponUuid[i],
                                        couponName: couponName[i],
                                        couponDeduct: couponDeduct[i]
                                    }
                                );
                            }
                            //-----------------------以上兼容旧数据--------------
                            loading.close();
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
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/

            /**
             * 点击查看大图
             * @param index 图片索引
             */
            handleViewPic(index){
                this.imageViewVisible = true;
                this.imageIndex = index;
            },
            /**
             * 工单状态图标显示
             * @param state 工单状态
             * @returns {string}
             */
            handleStateTypeIcon(state){
                let stateIconMap = ['', 'serving2', 'serving2', 'nopay2', 'finish2'];

                return stateIconMap[state];
            },
            /**
             * 工单状态文字显示
             * @param state 工单状态
             * @returns {string}
             */
            handleStateTypeText(state){
                let stateTextMap = ['', '未完工', '未完工', '待支付', '已完工'];

                return stateTextMap[state];
            },
            /**
             * 工单状态样式
             * @param state 工单状态
             * @returns {string}
             */
            handleStateTypeClass(state){
                let stateClassMap = ['', 'serving-cls', 'serving-cls', 'nopay-cls', 'finish-cls'];

                return stateClassMap[state];
            },
            /**
             * 计算项目总价
             * @returns {string}
             */
            handleSetOriginalTotalPrice(){
                let price = 0;

                (this.detailData.workOrderDetails || []).forEach((it) => {
                    price += it.totalPrice;
                });
                return parseFloat(price).toFixed(2);
            },
            /**
             * 跳转个人中心
             */
            handleTurnToUserCenter(){
                turnToHostPage('/open/user_center.html', 'emkt');
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
            //计算套餐抵扣金额
//            setPackageDiscount(){
//                let price = 0;
//                (this.detailData.workOrderDetails || []).forEach((it) => {
//                    price += it.salePrice * it.usePackageTimes;
//                })
//                return parseFloat(price).toFixed(2)
//            },
        },
        created() {
            this.id = getDataFromParam('id') || '';
            if (isEmpty(this.id)) {
                this.$message({
                    type: 'error',
                    message: '数据不存在'
                });
            } else {
                this.selectConsumeDetail(() => {
                    this.isLoading = false;
                });
            }
        }
    };
</script>

<style lang="less">
    @import "./style.less";
</style>