<template>
    <div class="page-main">
        <ul class="user-detail">
            <li><span>工单编号：</span>{{ orderNo }}</li>
            <li><span>车牌：</span>{{ carNumber }}</li>
            <li><span>姓名：</span>{{ realName }}</li>
            <li><span>手机号：</span>{{ mobile }}</li>
            <li><span>会员等级：</span>{{ memberLvl }}</li>
            <li><span>备注：</span>{{ comment }}</li>
        </ul>
        <zs-table
                class="item-table"
                :data="itemDatas"
                :columns="itemColumns"
                :context="context"
                :no-data-colspan="7"
                border></zs-table>
        <ul class="account-detail">
            <li>
                <div class="account-detail-item">
                    <span class="account-1">项目费用</span>
                    <span class="account-2">{{ parseFloat(itemTotalPrice).toFixed(2)}}</span>
                </div>
            </li>
            <li>
                <div class="account-detail-item" v-if="materialCost > 0">
                    <span class="account-1">附加费用</span>
                    <span class="account-2">{{ materialCost.toFixed(2) }}</span>
                </div>
            </li>
            <li>
                <div class="account-detail-item" v-if="businessDeduct > 0">
                    <span class="account-1">商家扣减</span>
                    <span class="account-2">- {{ businessDeduct.toFixed(2) }}</span>
                </div>
            </li>
            <li>
                <div class="account-detail-item" v-if="memberBalanceDeduct > 0">
                    <span class="account-1">会员余额支付</span>
                    <span class="account-2">- {{ memberBalanceDeduct.toFixed(2) }}</span>
                </div>
            </li>
            <!--以下兼容旧数据，以后可删除-->
            <li>
                <div class="account-detail-item" v-if="discountDeduct > 0">
                    <span class="account-1">会员折扣（{{ discountNum }}折）</span>
                    <span class="account-2">- {{ discountDeduct.toFixed(2) }}</span>
                </div>
            </li>
            <template v-if="couponDeduct > 0">
                <li v-for="coupon in couponShow" :key="coupon.couponUuid">
                    <div class="account-detail-item account-detail-coupon">
                        <span class="account-1">{{ coupon.couponName || '优惠券' }}</span>
                        <span class="account-2">- {{ parseFloat(coupon.couponDeduct).toFixed(2) }}</span>
                    </div>
                </li>
            </template>
            <!--以上兼容旧数据-->
        </ul>
        <ul class="account-detail">
            <li class="account-sum">
                <div class="account-detail-item">
                    <span class="account-2">{{ ['','现金收款：','微信收款：'][payType] }}<i>￥{{ totalPrice.toFixed(2) }}</i></span>
                </div>
            </li>
            <li class="balance-time">
                <div class="account-detail-item">
                    <span class="account-1">结算时间</span>
                    <span class="account-2">{{ payTime }}</span>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
    import { isEmpty, getDataFromParam } from '../../../../../../../assets/js/utils';
    export default {
        name: 'maining',
        data(){
            return {
                context: this,
                //基础信息
                orderId: '',
                orderNo: '',
                carNumber: '',
                realName: '',
                mobile: '',
                memberRecordId: '',
                memberLvl: '',
                comment: '',
                materialCost: '',
                businessDeduct: '',
                payType: '',
                totalPrice: 0,
                payTime: '',
                memberBalanceDeduct: 0,
                //工单列表
                itemDatas: [],
                itemColumns: [
                    {
                        title: '服务项目',
                        key: 'serviceItemName',
                        width: 152
                    },
                    {
                        title: '售价',
                        width: 76,
                        render(row){
                            return parseFloat(row.salePrice).toFixed(2);
                        }
                    },
                    {
                        title: '数量',
                        key: 'itemsTimes',
                        width: 64
                    },
                    {
                        title: '卡券抵扣',
                        width: 198,
                        render(row){
                            return row.couponName ? `<span class="use-coupon zs-coupon">${row.couponName} (-${row.couponDeduct})</span>` : '/';
                        }
                    },
                    {
                        title: '会员扣减',
                        width: 82,
                        render(row){
                            return row.discountPrice == 0 ? 0 : parseFloat(row.discountPrice).toFixed(2);
                        }
                    },
                    {
                        title: '总金额',
                        width: 84,
                        render(row){
                            return row.totalPrice == 0 ? 0 : parseFloat(row.totalPrice).toFixed(2);
                        }
                    },
                    {
                        title: '派工人员',
                        width: 84,
                        render(row){
                            return row.workerName || '/';
                        }
                    },
                    {
                        title: '销售人员',
                        width: 84,
                        render(row){
                            return row.salerName || '/';
                        }
                    },
                    {
                        title: '使用套餐',
                        key: 'usePackageTimes',
                        width: 126
                    }
                ],
                //以下变量兼容老数据，以后可以删除
                discountNum: '',
                discountDeduct: '',
                couponName: '',
                couponUuid: '',
                couponDeduct: 0,
                couponShow: []
            };
        },
        computed: {
            //项目费用
            itemTotalPrice(){
                let price = 0;

                this.itemDatas.forEach((item) => {
                    price += parseFloat(item.totalPrice || 0);
                });
                return parseFloat(price).toFixed(2);
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
             * 根据工单id获取工单详情
             * @param fn
             * @param loading
             */
            selectWorkOrder(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectWorkOrderById,
                    { orderId: this.orderId },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            const workOrder = data.data.workOrder;
                            const user = data.data.user;

                            if (isEmpty(workOrder)) {
                                this.$message({
                                    type: 'error',
                                    message: '工单不存在!',
                                    duration: 1200
                                });
                            } else if (workOrder.state != 4) {
                                this.$message({
                                    type: 'error',
                                    message: '该工单未结算!',
                                    duration: 1200
                                });
                            } else {
                                //基础信息
                                this.orderNo = workOrder.orderNo;
                                this.carNumber = workOrder.carNumber;
                                this.comment = workOrder.comment;
                                this.mobile = workOrder.mobile || '----';
                                this.realName = isEmpty(user) ? '--' : user.realName || '--';
                                this.memberRecordId = isEmpty(user) ? '' : user.recordId || '';
                                this.memberLvl = isEmpty(this.memberRecordId) ? '非会员' : user.lvlName || '普通会员';
                                this.businessDeduct = workOrder.businessDeduct || 0;
                                this.materialCost = workOrder.materialCost || 0;
                                this.payType = workOrder.payType || 0;
                                this.memberBalanceDeduct = workOrder.balanceDeduct || 0;
                                this.totalPrice = workOrder.totalPrice || 0;
                                this.payTime = workOrder.payTime || '';
                                //展示会员折扣（老数据使用，以后可以删除）
                                this.discountNum = workOrder.discountNum || 10;
                                this.discountDeduct = workOrder.discountDeduct || 0;
                                //展示卡券（老数据使用，以后可以删除）
                                this.couponName = workOrder.couponName ? workOrder.couponName.replace(/,/g, '， ') : '';
                                this.couponUuid = workOrder.couponUuid || '';
                                this.couponDeduct = workOrder.couponDeduct || 0;
                                const tempCouponUuid = workOrder.couponUuid.split(',');
                                const tempCouponName = workOrder.couponName.split(',');
                                const tempCouponDeduct = workOrder.couponEveryDeduct.split(',');

                                for (let i = 0, len = tempCouponUuid.length; i < len; i++) {
                                    this.couponShow.push(
                                        {
                                            couponUuid: tempCouponUuid[i],
                                            couponName: tempCouponName[i],
                                            couponDeduct: tempCouponDeduct[i]
                                        }
                                    );
                                }
                                //工单项目
                                this.itemDatas = workOrder.workOrderDetails || [];
                            }
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duraton: 1200
                            });
                        }
                        fn && fn();
                    }
                );
            }

            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/

        },
        created(){
            this.orderId = getDataFromParam('id') || '';
            if (isEmpty(this.orderId)) {
                this.$message({
                    type: 'error',
                    message: '工单不存在!',
                    duration: 1200
                });
                return;
            }
            const loading = this.$loading();

            this.selectWorkOrder(() => {
                loading.close();
                this.$emit('input', false);
            }, loading);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
