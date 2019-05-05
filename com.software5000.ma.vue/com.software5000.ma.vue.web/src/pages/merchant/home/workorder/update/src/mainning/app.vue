<template>
    <div class="page-main">
        <div class="page-left">
            <div class="page-left-top">
                <div class="page-block-1" :class="[{'is-edit':isEdit}]">
                    <!--用户信息-->
                    <ul class="block-1-list">
                        <li class="block-input-wrap required car-number-input"
                            v-clickoutside="_=>{this.memberResultVisible = false}">
                            <zs-icon
                                    class="block-input-label"
                                    :right-icon="isEdit ? '' : 'caret-bottom'"
                                    :text="`${currCarArea} ${currCarLetter}`"
                                    @click="!isEdit && (carNumSelectVisible = true)"></zs-icon>
                            <zs-input
                                    v-model.trim="currCarNo"
                                    placeholder="请输入车牌"
                                    :readonly="isEdit"
                                    autofocus
                                    :maxlength="5"
                                    @change="handleMemberSearch"></zs-input>
                            <zs-icon
                                    v-if="!isEdit"
                                    class="search-label"
                                    :class="[{'is-open':memberResultVisible}]"
                                    :icon="memberResultVisible ? 'user2':'user1'"
                                    size="18"
                                    :icon2="`arrow-${memberResultVisible ? 'top':'bottom'}`"
                                    size2="8"
                                    icon-dis="6"
                                    @click="memberResultVisible = !memberResultVisible"></zs-icon>
                            <!--用户搜索列表-->
                            <div class="search-result" v-show="memberResultVisible">
                                <zs-table
                                        :data="memberDatas"
                                        :columns="memberColumns"
                                        :context="context"
                                        :no-data-colspan="4"
                                        no-data-text="暂无客户"
                                        no-data-icon="data-empty-member"
                                        @on-row-click="handleMemeberSelect"
                                        border
                                ></zs-table>
                                <zs-pagination
                                        :current="this.memberPage.startPage"
                                        :total="this.memberPage.total"
                                        :page-size="this.memberPage.pageSize"
                                        @on-change="handlePageChange(1,$event)"
                                        simple
                                ></zs-pagination>
                            </div>
                        </li>
                        <li class="block-input-wrap user-name">
                            <span class="block-input-label">姓名</span>
                            <zs-input ref="carNumberInput" v-model="finalData.user.realName"
                                      @change="handleMemberNameSearch"
                                      placeholder="请输入姓名"></zs-input>
                            <span class="user-name-remark">{{ userNameRemark  ? `(${userNameRemark})` : '' }}</span>
                        </li>
                        <li class="block-input-wrap pl10">
                            <span class="total-income">总到店:{{memberVisitCount || ''}}次</span>
                            <span>上次到店: {{memberVisitTime ? memberVisitTime.slice(0,10) : ''}}</span>
                        </li>
                        <li class="block-input-wrap">
                            <span class="block-input-label">手机号</span>
                            <zs-input v-model="finalData.mobile" placeholder="请输手机号" :maxlength="11"></zs-input>
                        </li>
                        <li class="block-input-wrap">
                            <span class="block-input-label">欠款</span>
                            <zs-input value="0" disabled placeholder="请输入欠款金额"></zs-input>
                        </li>
                        <li class="block-input-wrap pl66">
                            <span class="block-input-label">会员等级</span>
                            <p class="pl12">{{ memberLvl }}</p>
                        </li>
                    </ul>
                    <div class="block-1-remark">
                        <span>备注</span>
                        <zs-input v-model="finalData.comment" icon="circle-cross" placeholder="请输入备注"></zs-input>
                    </div>
                </div>
                <div class="page-block-2">
                    <!--用户套餐-->
                    <table>
                        <colgroup>
                            <col width="150">
                            <col width="68">
                            <col width="68">
                            <col width="82">
                        </colgroup>
                        <tr class="table-head">
                            <th>
                                <div class="table-cell">套餐项目</div>
                            </th>
                            <th>
                                <div class="table-cell">剩余次数</div>
                            </th>
                            <th>
                                <div class="table-cell">消费次数</div>
                            </th>
                            <th>
                                <div class="table-cell">到期时间</div>
                            </th>
                        </tr>
                    </table>
                    <div class="block-2-package-body">
                        <zs-scrollbar wrap-class="package-select-scrollbar">
                            <table>
                                <colgroup>
                                    <col width="150">
                                    <col width="68">
                                    <col width="68">
                                    <col width="82">
                                </colgroup>
                                <tr v-for="p in packageDatas" :key="p.id" v-if="p.remainTimes > 0">
                                    <td>
                                        <div class="table-cell">{{ p.itemName }}</div>
                                    </td>
                                    <td>
                                        <div class="table-cell">{{ p.remainTimes }}</div>
                                    </td>
                                    <td>
                                        <div class="table-cell">{{ p.useTimes - p.remainTimes }}</div>
                                    </td>
                                    <td>
                                        <div class="table-cell">{{ p.validTime }}</div>
                                    </td>
                                </tr>
                            </table>
                        </zs-scrollbar>
                    </div>
                    <table>
                        <tr>
                            <td class="block-2-add" colspan="4" :rowspan="addRowspan">
                                <zs-button
                                        type="primary"
                                        @click="handleTurnToBuyPackage">购买套餐
                                </zs-button>
                            </td>
                        </tr>
                        <tr v-for="(r,i) in addRowspanArr" :key="i"></tr>
                    </table>
                </div>
            </div>
            <div class="page-left-bottom">
                <!--已选项目列表-->
                <div class="page-block-3">
                    <zs-table
                            :data="itemSelectDatas"
                            :columns="itemSelectColumns"
                            :context="context"
                            :no-data-colspan="9"
                            border></zs-table>
                    <div class="add-new-item">
                        <zs-icon icon="ten-cross" size="10" text="添加临时项目" @click="tempItemVisible=true"></zs-icon>
                    </div>
                </div>
                <div class="page-block-4">
                    <div class="block-4-inner">
                        <div class="block-input-wrap">
                            <span class="block-input-label">附加费用</span>
                            <zs-input
                                    v-model="finalData.materialCost"
                                    type="number"
                                    :auto-select="finalData.materialCost == 0"
                                    placeholder="请输入附加费用"></zs-input>
                        </div>
                        <div class="block-input-wrap">
                            <span class="block-input-label">商家扣减</span>
                            <zs-input
                                    v-model="finalData.businessDeduct"
                                    type="number"
                                    :auto-select="finalData.businessDeduct == 0"
                                    placeholder="请输入扣减金额"></zs-input>
                        </div>
                        <div class="block-input-wrap member-balance" v-if="memberBalance > 0">
                            <zs-checkbox
                                    v-model="memberBalanceDeduct"
                                    :true-label="memberBalance"
                                    :false-label="0">会员余额支付 <i>(余额:{{ parseFloat(memberBalance).toFixed(2) }})</i>
                            </zs-checkbox>
                        </div>
                        <span class="block-4-summary">合计费用：<i>￥{{ totalPrice }}</i></span>
                        <div class="block-4-btn" @click="updateWorkOrder">提交工单</div>
                    </div>
                </div>
            </div>
        </div>
        <!--可选项目列表-->
        <div class="page-right">
            <p class="block-5-title">可选项目</p>
            <div class="search-wrapper">
                <zs-input
                        v-model.trim="itemSearchKey"
                        icon="circle-cross"
                        placeholder="请输入服务项目关键词进行搜索"
                        @on-icon-click="handleEmptyItemSearchKey"
                        @change="handleItemSearch"></zs-input>
            </div>
            <div class="block-5-item">
                <dl class="block-5-menu">
                    <dt>项目分类</dt>
                    <dd
                            :class="[{'curr':t[0] === itemType}]"
                            v-for="t in itemTypes"
                            :key="t[0]"
                            @click="handleSelectItemType(t[0])">{{ t[1] }}
                    </dd>
                </dl>
                <div class="block-5-main">
                    <zs-table
                            :data="itemDatas"
                            :columns="itemColumns"
                            :context="context"
                            :no-data-colspan="2"
                            @on-row-click="handleItemSelect"
                            border></zs-table>
                    <div class="pagination-wrap" v-if="itemDatas.length > 0">
                        <zs-pagination
                                :current="itemPage.startPage"
                                :total="itemPage.total"
                                :page-size="itemPage.pageSize"
                                @on-change="handlePageChange(2,$event)"
                                simple
                        ></zs-pagination>
                        <span class="item-total">共 {{itemPage.total}} 条</span>
                    </div>
                </div>
            </div>
        </div>
        <!--车牌地区选择-->
        <zs-dialog class="carnum-select-pop" v-model="carNumSelectVisible">
            <p slot="title">选择车牌地区</p>
            <div class="carnum-area-list">
                <zs-scrollbar wrap-class="carnum-select-scrollbar">
                    <p
                            v-for="(area,index) in carNumArea"
                            class="list"
                            :class="[{'curr':currCarArea === area[1]}]"
                            :key="index"
                            @click="currCarArea = area[1]">{{ area }}
                    </p>
                </zs-scrollbar>
            </div>
            <div class="carnum-area-letter">
                <zs-scrollbar wrap-class="carnum-select-scrollbar">
                    <div
                            v-for="(l,index) in carNumLetter"
                            class="list"
                            :class="[{'curr':currCarLetter === l,'disabled':!currCarArea}]"
                            :key="index">
                        <p @click="handleSetCarLetter(l)">{{ `${currCarArea} ${l}` }}</p>
                        <p class="default-btn" @click="updateDefaultCar(l)">{{ handleSetDefaultText(l) }}</p>
                    </div>
                </zs-scrollbar>
            </div>
        </zs-dialog>
        <!--新建临时项目-->
        <zs-dialog class="temp-item-pop" v-model="tempItemVisible">
            <p slot="title">新建临时项目</p>
            <zs-form label-position="right" label-width="100px">
                <zs-form-item class="temp-item-name" label="项目名称" required>
                    <zs-input placeholder="请输入服务项目名称" v-model="tempItemName" :maxlength="10"></zs-input>
                    <span class="length-count">{{ tempItemName.length }}/10</span>
                </zs-form-item>
                <zs-form-item label="项目价格" required>
                    <zs-input type="number" v-model="tempItemPrice" placeholder="请输入服务项目价格"></zs-input>
                </zs-form-item>
            </zs-form>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="tempItemVisible=false">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="handleAddTempItem">确定
                </zs-button>
            </template>
        </zs-dialog>
        <!--选择员工-->
        <zs-dialog class="select-pop" v-model="employeeSelectVisible">
            <p slot="title">选择员工</p>
            <ul class="employee-select-list">
                <li class="employee-select-hd"><span>选择</span><span>员工姓名</span></li>
                <template v-if="employeeDatas.length > 0">
                    <li v-for="e in employeeDatas" :key="e.id" @click="handleEmployeeSelect(e)">
                        <span><i class="table-radio" :class="[{'curr' : e.selected}]"></i></span>
                        <span>{{ e.realName }}</span>
                    </li>
                </template>
                <li v-else class="employee-empty">
                    <p>暂无员工</p>
                </li>
            </ul>
            <zs-pagination
                    v-if="employeeDatas.length > 0"
                    slot="footer"
                    :current="employeePage.startPage"
                    :total="employeePage.total"
                    :page-size="employeePage.pageSize"
                    @on-change="handlePageChange(0,$event)"
                    show-total
            ></zs-pagination>
        </zs-dialog>
        <!--选择优惠券-->
        <zs-dialog class="select-pop coupon-select-pop" v-model="couponSelectVisible">
            <p slot="title">选择优惠券</p>
            <ul class="employee-select-list">
                <li class="employee-select-hd"><span>选择</span><span>优惠券名称</span><span>优惠券金额</span></li>
                <template v-if="couponsItemData.length > 0">
                    <li
                            v-for="(c,index) in couponsItemData"
                            :key="c.id"
                            v-if="isCouponListShow(c)"
                            @click="handleSelectCoupon(c.takeUuid)">
                        <span><i class="table-radio"
                                 :class="[{'curr' : c.couponKey === itemSelectDatas[currItemIndex].couponKey}]"></i></span>
                        <span>{{ c.coupon.cpName }}</span>
                        <span>{{ c.coupon.cpMoney }}</span>
                    </li>
                </template>
                <li v-else class="employee-empty">
                    <p>暂无可用优惠券</p>
                </li>
            </ul>
        </zs-dialog>
    </div>
</template>

<script>
    import {
        isEmpty,
        isMobile,
        isCarNum,
        doCopy,
        turnToNextPage,
        getDataFromParam
    } from '../../../../../../../assets/js/utils';
    import clickoutside from '../../../../../../../components/src/directives/clickoutside';

    const pageChangeMap = ['employeePage', 'memberPage', 'itemPage'];
    const pageChangeFnMap = ['selectEmployeeDatas', 'handleMemberSearch', 'selectServiceItem'];
    const workerTypeIdMap = ['workerId', 'salerId'];
    const workerTypeMap = ['workerName', 'salerName'];

    export default {
        name: 'maining',
        directives: { clickoutside },
        props: {
            value: Boolean
        },
        data() {
            return {
                context: this,
                isEdit: false,
                firstLoad: false,
                //会员查询
                memberResultVisible: false,
                memberDatas: [],
                memberColumns: [
                    {
                        title: '车牌',
                        key: 'carNumber',
                        width: 100
                    },
                    {
                        title: '客户姓名',
                        key: 'realName',
                        width: 126
                    },
                    {
                        title: '手机号',
                        key: 'mobile',
                        width: 128
                    },
                    {
                        title: '会员等级',
                        width: 90,
                        render(row) {
                            return row.recordId ? row.lvlName || '普通会员' : '非会员';
                        }
                    }
                ],
                memberPage: {
                    startPage: 1,
                    pageSize: 10,
                    total: 0
                },
                lastMemberSendData: {},
                memberRecordId: '',
                memberLvl: '----',
                memberVisitCount: '--',
                memberVisitTime: '----',
                userMobile: '',
                userNameRemark: '',
                memberBalance: 0,
                memberBalanceDeduct: 0,
                //会员套餐
                packageDatas: [],
                packageRemain: {},
                //可选服务项目
                itemDatas: [],
                itemColumns: [
                    {
                        title: '服务项目',
                        width: '160',
                        key: 'itemName'
                    },
                    {
                        title: '售价',
                        width: '70',
                        render(row) {
                            return parseFloat(row.salePrice).toFixed(2);
                        }
                    }
                ],
                itemSearchKey: '',
                itemTypes: [],
                itemType: '',
                itemPage: {
                    startPage: 1,
                    pageSize: 10,
                    total: 0
                },
                //新建临时项目
                tempItemVisible: false,
                tempItemName: '',
                tempItemPrice: '',
                //已选服务项目
                itemSelectDatas: [],
                itemSelectColumns: [
                    {
                        title: '已选项目',
                        key: 'serviceItemName',
                        width: 140
                    },
                    {
                        title: '售价',
                        width: 64,
                        render(row) {
                            return parseFloat(row.salePrice).toFixed(2);
                        }
                    },
                    {
                        title: '数量',
                        width: 44,
                        className: 'item-times',
                        render(row, col, index, _this) {
                            return row._readonly ? `<p @click="handleOpenInsertItemTimes(${index})">${_this.handleCalcItemTimes(row.itemsTimes)}</p>` : `
                                    <input
                                        class="item-times"
                                        type="number"
                                        autofocus
                                        onfocus="this.select()"
                                        value="${_this.handleCalcItemTimes(row.itemsTimes)}"
                                        @blur="handleCloseInsertItemTimes(${index})"
                                        @change="handleChangeItemTimes(${index},$event)">`;
                        }
                    },
                    {
                        title: '卡券抵扣',
                        width: 190,
                        render(row, col, index, _this) {
                            const itemCoupons = [];

                            _this.couponsData.forEach((coupon) => {
                                if (_this.isCouponListShow(coupon, index)) {
                                    itemCoupons.push(coupon);
                                }
                            });
                            return !isEmpty(row.couponUuid) ? `<span class="worker-name" @click="handlePopCoupon(${row.itemType},${index})">${row.couponName} (-${row.couponDeduct})</span>` : itemCoupons.length > 0 ? `<p class="ten-cross" @click="handlePopCoupon(${row.itemType},${index})"><i></i></p>` : `<span>无可用卡券</span>`;
                        }
                    },
                    {
                        title: '会员扣减',
                        width: 66,
                        render(row, col, index, _this) {
                            let discountPrice = 0;
                            const priceBeforeDiscount = (_this.handleCalcItemTimes(row.itemsTimes) - (row._useCard ? _this.handleCalcUsePackageTimes(row, index) : 0)) * row.salePrice - (row.couponDeduct || 0);

                            //row.discountType : 1 -> 折扣; 2 -> 会员价
                            if (row.discountType == 1) {
                                discountPrice = parseFloat((1 - row.discountNumber / 10) * priceBeforeDiscount).toFixed(2);
                            } else if (row.discountType == 2) {
                                const temp = (_this.handleCalcItemTimes(row.itemsTimes) - (row._useCard ? _this.handleCalcUsePackageTimes(row, index) : 0)) * (row.salePrice - row.discountNumber);

                                discountPrice = parseFloat(temp - priceBeforeDiscount > 0 ? priceBeforeDiscount : temp).toFixed(2);
                            }

                            _this.$set(_this.itemSelectDatas[index], 'discountPrice', parseFloat(discountPrice));
                            return `-${discountPrice}`;
                        }
                    },
                    {
                        title: '总金额',
                        width: 80,
                        render(row, col, index, _this) {
                            const totalPrice = parseFloat((_this.handleCalcItemTimes(row.itemsTimes) - (row._useCard ? _this.handleCalcUsePackageTimes(row, index) : 0)) * row.salePrice - (row.couponDeduct || 0) - row.discountPrice).toFixed(2);

                            _this.$set(_this.itemSelectDatas[index], 'totalPrice', parseFloat(totalPrice));
                            return totalPrice;
                        }
                    },
                    {
                        title: '派工人员',
                        width: 80,
                        render(row, col, index) {
                            return isEmpty(row.workerName) ? `<p class="ten-cross" @click="handleAddWorker(0,${index})"><i></i></p>` : `<span class="worker-name" @click="handleAddWorker(0,${index})">${row.workerName}</span>`;
                        }
                    },
                    {
                        title: '销售人员',
                        width: 80,
                        render(row, col, index) {
                            return isEmpty(row.salerName) ? `<p class="ten-cross" @click="handleAddWorker(1,${index})"><i></i></p>` : `<span class="worker-name" @click="handleAddWorker(1,${index})">${row.salerName}</span>`;
                        }
                    },
                    {
                        title: '操作',
                        width: 156,
                        className: 'item-control',
                        render(row, col, index, _this) {
                            const switchHtml = row._hasCard ? `
                                <p class="table-switch-wrap ${row._useCard ? 'is-usecard' : ''}"><span class="table-switch" @click="handleToggleUseCard(${index})"></span>${row._useCard ? `用卡*${_this.handleCalcUsePackageTimes(row, index)}` : '不用卡'}</p>` : '<p class="table-switch-wrap"></p>';
                            const delHtml = `<i class="delete-icon" @click="handleDeleteItem(${index})"></i>`;

                            return `${switchHtml}${delHtml}`;
                        }
                    }
                ],
                itemIdList: [],//记录所有员工已创建的项目
                //车牌选择
                carNumSelectVisible: false,
                currCarArea: '闽',
                currCarLetter: 'D',
                currCarNo: '',
                defaultCarNum: '闽D',
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
                //选择员工
                employeeSelectVisible: false,
                employeeDatas: [],
                employeePage: {
                    startPage: 1,
                    pageSize: 10,
                    total: 0
                },
                currItemIndex: '',
                currItemType: '',
                currItemWorkerType: '',
                //创建或编辑工单
                finalData: {
                    id: '',
                    balanceDeduct: '',
                    totalPrice: '',
                    carNumber: '',
                    workOrderDetails: [],
                    userId: '',
                    user: {
                        realName: ''
                    },
                    state: 1,
                    mobile: '',
                    businessDeduct: '',
                    materialCost: '',
                    payType: '',
                    comment: ''
                },
                //优惠券情况
                couponSelectVisible: false,
                couponsData: [],
                couponsItemData: [],
                couponKey: 1
            };
        },
        computed: {
            //控制购买套餐按钮的居中显示
            addRowspan() {
                let length = 0;

                this.packageDatas.forEach((p) => {
                    p.remainTimes > 0 && length++;
                });
                return 4 - (length > 3 ? 3 : length);
            },
            //控制购买套餐按钮的居中显示
            addRowspanArr() {
                const arr = [];

                arr.length = this.addRowspan - 1;
                return arr;
            },
            //商家扣减前的总价格
            totalPriceBeforeDeduct() {
                let price = 0;

                this.itemSelectDatas.forEach((item) => {
                    if (item.totalPrice) {
                        price += parseFloat(item.totalPrice);
                    }
                });
                price = price + parseFloat(this.finalData.materialCost || 0);
                return price.toFixed(2);
            },
            //使用余额前的总价格
            totalPriceBeforeBalance() {
                let price = this.totalPriceBeforeDeduct - (this.finalData.businessDeduct || 0);

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            },
            //计算总价格
            totalPrice() {
                let price = this.totalPriceBeforeBalance - this.memberBalanceDeduct;

                return parseFloat(price > 0 ? price : 0).toFixed(2);
            }
        },
        watch: {
            tempItemVisible(val) {
                if (!val) {
                    this.tempItemName = '';
                    this.tempItemPrice = '';
                }
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /**
             * 更新默认车牌所属地区
             * @param l 选中的车牌字母
             */
            updateDefaultCar(l) {
                if (!isEmpty(this.currCarArea) && this.defaultCarNum !== `${this.currCarArea}${l}`) {
                    this.currCarLetter = l;
                    this.defaultCarNum = `${this.currCarArea}${l}`;
                    this.carNumSelectVisible = false;
                    this.$ajax(
                        this.$joggle.merchant.workorder.updateDefaultCar,
                        { defaultCar: this.defaultCarNum },
                        true,
                        (data, loading) => {
                            if (data.code === 'ZS011000') {
                                if (this.currCarNo.length < 5) {
                                    loading.close();
                                    this.$message({
                                        type: 'success',
                                        message: '设置成功!',
                                        duration: 1200
                                    });
                                    return;
                                }
                                this.handleCarNoChange(() => {
                                    this.$message({
                                        type: 'success',
                                        message: '设置成功!',
                                        duration: 1200
                                    });
                                });
                            } else {
                                loading.close();
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
             * 更新工单
             */
            updateWorkOrder() {
                this.itemSelectDatas.forEach((item) => {
                    item.usePackageTimes = item._useCard ? item.usePackageTimes : 0;
                });
                this.finalData.workOrderDetails = this.itemSelectDatas;
                this.finalData.totalPrice = this.totalPrice;
                this.finalData.businessDeduct = Math.min(parseFloat(this.finalData.businessDeduct || 0), parseFloat(this.totalPriceBeforeDeduct)).toFixed(2);
                this.finalData.materialCost = parseFloat(this.finalData.materialCost || 0).toFixed(2);
                this.finalData.balanceDeduct = Math.min(parseFloat(this.memberBalanceDeduct), parseFloat(this.totalPriceBeforeBalance)).toFixed(2);
                if (!isCarNum(this.finalData.carNumber)) {
                    this.$message({
                        type: 'error',
                        message: '车牌号码有误!',
                        duration: 1200
                    });
                } else if (!isEmpty(this.finalData.mobile) && !isMobile(this.finalData.mobile)) {
                    this.$message({
                        type: 'error',
                        message: '请输入正确的手机号码!',
                        duration: 1200
                    });
                } else if (this.finalData.workOrderDetails.length === 0) {
                    this.$message({
                        type: 'error',
                        message: '请选择项目!',
                        duration: 1200
                    });
                } else {
                    this.$ajax(
                        this.$joggle.merchant.workorder.updateWorkOrder,
                        this.finalData,
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                });
                                setTimeout(() => {
                                    turnToNextPage('/web/merchant/home/workorder/list_unfinish.html');
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
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

            /**
             * 获取车牌的所属地区
             * @param fn
             * @param loading
             */
            selectDefaultCarArea(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.workorder.selectBusinessInfo,
                    {},
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            if (!isEmpty(data.data)) {
                                this.defaultCarNum = data.data.defaultCar || '闽D';
                                this.currCarArea = data.data.defaultCar ? data.data.defaultCar[0] : '闽';
                                this.currCarLetter = data.data.defaultCar ? data.data.defaultCar[1] : 'D';
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                        fn && fn();
                    },
                    () => {
                        fn && fn();
                    }
                );
            },
            /**
             * 模糊搜索会员信息
             * @param fn
             * @param loading
             */
            selectMemberByCarNumber(paras, fn, loading) {
                let sendData = {
                    startPage: this.memberPage.startPage,
                    pageSize: this.memberPage.pageSize
                };
                if(isEmpty(paras)){
                    sendData = { ...this.lastMemberSendData,...sendData };
                }else{
                    this.lastMemberSendData = sendData = { ...sendData, ...paras };
                }
                this.$ajax(
                    this.$joggle.merchant.workorder.selectMemberByCarNumber,
                    sendData,
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.memberPage.total = data.data.total;
                            this.memberDatas = data.data.list;
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
            },
            /**
             * 根据工单id/车牌获取工单信息
             * @param fn
             * @param loading
             * @param id 工单id
             */
            selectWorkOrder(fn, loading, id) {
                this.$ajax(
                    this.$joggle.merchant.workorder[isEmpty(id) ? 'selectWorkOrderByCarNumber' : 'selectWorkOrderById'],
                    isEmpty(id) ? { carNumber: this.finalData.carNumber } : { orderId: id },
                    loading,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            const workOrder = data.data.workOrder;
                            const user = data.data.user;
                            const userMobile = isEmpty(user) ? '' : user.mobile;

                            this.itemIdList = data.data.serviceIdList || [];
                            //工单信息
                            if (!isEmpty(id)) {
                                if (isEmpty(workOrder)) {
                                    this.$message({
                                        type: 'error',
                                        message: '工单不存在！',
                                        duration: 1200
                                    });
                                    return;
                                }
                                this.finalData.carNumber = isEmpty(workOrder) ? '' : workOrder.carNumber;
                                this.currCarArea = isEmpty(workOrder) ? '闽' : workOrder.carNumber[0];
                                this.currCarLetter = isEmpty(workOrder) ? 'D' : workOrder.carNumber[1];
                                this.currCarNo = isEmpty(workOrder) ? '' : workOrder.carNumber.slice(2);
                            }
                            this.finalData.id = isEmpty(workOrder) ? '' : workOrder.id;
                            this.finalData.mobile = isEmpty(workOrder) ? userMobile : workOrder.mobile;
                            this.finalData.businessDeduct = isEmpty(workOrder) ? '' : workOrder.businessDeduct || '';
                            this.finalData.materialCost = isEmpty(workOrder) ? '' : workOrder.materialCost || '';
                            this.finalData.comment = isEmpty(workOrder) ? '' : workOrder.comment || '';
                            //会员信息
                            this.userNameRemark = isEmpty(user) ? '' : user.remarks || '';
                            this.userMobile = userMobile || '';
                            this.finalData.user.realName = isEmpty(user) ? '' : user.realName;
                            this.finalData.userId = isEmpty(user) ? '' : user.userId || '';
                            this.memberRecordId = isEmpty(user) ? '' : user.recordId || '';
                            this.memberLvl = isEmpty(this.memberRecordId) ? '非会员' : user.lvlName || '普通会员';
                            this.memberVisitCount = isEmpty(user) ? '--' : user.visitCount || '--';
                            this.memberVisitTime = isEmpty(user) ? '----' : user.lastVisitTime || '----';
                            //会员余额
                            this.memberBalanceDeduct = this.memberBalance = isEmpty(user) ? 0 : user.memberBalance || 0;
                            //套餐信息
                            const packageInfo = this.handlePackageMerge(isEmpty(user) ? [] : user.packageList);

                            this.packageDatas = packageInfo.packages;
                            this.packageRemain = packageInfo.remainTimes;
                            //卡券信息

                            const f1 = new Promise((resolve) => {
                                this.selectCouponsByCarNumber(() => {
                                    //工单项目
                                    const itemArr = isEmpty(workOrder) ? [] : workOrder.workOrderDetails || [];

                                    itemArr.forEach((it) => {
                                        it._readonly = true;
                                        it._hasCard = it._useCard = false;
                                        if (isEmpty(it.itemType) || isEmpty(it.serviceItemId)) {
                                            it.itemType = -1;
                                        }
                                        //编辑时已使用套餐的不自动带出优惠券
                                        it._autoCoupon = true;
                                        //设置套餐使用情况
                                        for (let id in this.packageRemain) {
                                            if (this.packageRemain[id] > 0 && id == it.serviceItemId) {
                                                it._hasCard = true;
                                                it._useCard = parseInt(it.usePackageTimes || 0) > 0;
                                                this.$set(it, '_autoCoupon', !it._useCard);
                                                break;
                                            }
                                        }
                                        //标记项目中已经选中的优惠券
                                        this.$set(it, 'couponKey', this.couponKey);
                                        //该变量用来控制项目中的优惠券是否已失效
                                        let itCouponRemain = false;

                                        if (!isEmpty(it.couponUuid)) {
                                            this.couponsData.forEach((coupon) => {
                                                if (it.couponUuid === coupon.takeUuid) {
                                                    this.$set(coupon, 'couponKey', this.couponKey);
                                                    itCouponRemain = true;
                                                }
                                            });
                                            //已失效，清空项目中的优惠券
                                            if (!itCouponRemain) {
                                                this.$set(it, 'couponUuid', '');
                                                this.$set(it, 'couponName', '');
                                                this.$set(it, 'couponDeduct', '');
                                            }
                                        }
                                        this.couponKey++;
                                    });
                                    this.itemSelectDatas = itemArr;
                                    resolve();
                                }, loading);
                            });
                            //获取包含用户优惠数据的项目信息
                            const f2 = new Promise((resolve) => {
                                this.selectServiceItem(() => {
                                    resolve();
                                }, loading);
                            });

                            Promise.all([f1, f2]).then(() => {
                                fn && fn();
                            });

                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duraton: 1200
                            });
                        }
                    },
                    () => {
                        //如果获取工单失败,清空所有相关数据
                        this.finalData.id = '';
                        this.finalData.totalPrice = 0;
                        this.finalData.workOrderDetails = [];
                        this.finalData.userId = '';
                        this.finalData.user.realName = '';
                        this.finalData.mobile = '';
                        this.finalData.businessDeduct = '';
                        this.finalData.materialCost = '';
                        this.finalData.comment = '';
                        this.itemSelectDatas = [];
                        this.memberLvl = '';
                        this.memberVisitCount = '--';
                        this.memberVisitTime = '----';
                        this.packageDatas = [];
                        this.packageRemain = {};
                    }
                );
            },
            /**
             * 通过车牌获取优惠券信息
             * @param fn
             * @param loading
             */
            selectCouponsByCarNumber(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.workorder.selectCouponsByCarNumber,
                    { carNumber: this.finalData.carNumber },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.couponsData = data.data;
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
            },
            /**
             * 获取服务项类别
             * @param fn
             * @param loading
             */
            selectItemType(fn, loading) {
                this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    { codeTypes: 'ServiceItemType' },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const temp = data.data.ServiceItemType;

                            temp.unshift({ 0: '', 1: '全部' });
                            this.itemTypes = temp;
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
            },
            /**
             * 获取商家服务项
             * @param fn
             * @param loading
             */
            selectServiceItem(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.workorder.selectServiceItem,
                    {
                        startPage: this.itemPage.startPage,
                        pageSize: this.itemPage.pageSize,
                        searchValue: this.itemSearchKey,
                        itemType: this.itemType,
                        userId: this.finalData.userId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.itemPage.total = data.data.total;
                            this.itemDatas = data.data.list;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                        fn && fn();
                    }
                );
            },
            /**
             * 获取员工信息
             * @param fn
             * @param loading
             */
            selectEmployeeDatas(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.employee.selectByPage,
                    {
                        startPage: this.employeePage.startPage,
                        pageSize: this.employeePage.pageSize,
                        bossType: 2,
                        state: 1
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            data.data.list.forEach((d) => {
                                this.$set(d, 'selected', false);
                            });
                            this.employeePage.total = data.data.total;
                            this.employeeDatas = data.data.list;
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
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/

            /**
             *  跳转页面至购买套餐页
             */
            handleTurnToBuyPackage() {
                turnToNextPage('/web/merchant/home/member/buy_package.html', { mobile: this.userMobile });
            },
            /**
             *  startPage切换
             *  @param type 切换的数据所属表格
             *  @param page 页码
             */
            handlePageChange(type, page) {
                this[pageChangeMap[type]].startPage = page;
                const loading = this.$loading();

                if (pageChangeFnMap[type] === 'handleMemberSearch') {
                    this.selectMemberByCarNumber({}, () => {
                        loading.close();
                    }, loading);
                } else {
                    this[pageChangeFnMap[type]](() => {
                        loading.close();
                    }, loading);
                }
            },
            /**
             *  会员搜索输入触发搜索
             */
            handleMemberSearch() {
                this.finalData.carNumber = `${this.currCarArea}${this.currCarLetter}${this.currCarNo.toUpperCase()}`;
                this.memberPage.total = 0;
                this.memberDatas = [];
                this.memberResultVisible = true;
                //输入第二个字符后开始查询
                if (this.currCarNo.length > 1) {
                    this.memberPage.startPage = 1;
                    let sendData = {
                        carNumber: this.currCarNo.toUpperCase(),
                        carSelect: `${this.currCarArea}${this.currCarLetter}`
                    };
                    this.selectMemberByCarNumber(sendData, () => {
                    }, false);
                }
                if (this.currCarNo.length === 5) {
                    this.handleCarNoChange();
                    this.memberResultVisible = false;
                }
            },
            /**
             *  会员搜索输入姓名触发搜索
             */
            handleMemberNameSearch() {
                this.memberResultVisible = true;
                this.memberPage.total = 0;
                this.memberDatas = [];
                //输入第二个字符后开始查询
                if (this.finalData.user.realName.length > 0) {
                    this.memberPage.startPage = 1;
                    let sendData = {
                        realName: this.finalData.user.realName,
                    };

                    this.selectMemberByCarNumber(sendData, () => {
                    }, false);
                }
            },
            /**
             *  选中会员
             *  @param data 选中会员的信息对象
             */
            handleMemeberSelect(data) {
                this.currCarArea = data.carNumber[0];
                this.currCarLetter = data.carNumber[1];
                this.currCarNo = data.carNumber.slice(2);
                this.memberResultVisible = false;

                this.handleCarNoChange();
            },
            /**
             *  设置选择车牌窗口默认状态显示的文字
             *  @param l 选中的车牌字母
             */
            handleSetDefaultText(l) {
                return this.currCarArea === this.defaultCarNum[0] && l === this.defaultCarNum[1] ? '默认' : '设为默认';
            },
            /**
             *  选择车牌所属城市字母
             *  @param l 选中的车牌字母
             */
            handleSetCarLetter(l) {
                if (!isEmpty(this.currCarArea)) {
                    this.currCarLetter = l;
                    this.carNumSelectVisible = false;
                    this.handleCarNoChange();
                }
            },
            /**
             *  车牌号码发生改变时触发的事件
             */
            handleCarNoChange(fn) {
                this.finalData.carNumber = `${this.currCarArea}${this.currCarLetter}${this.currCarNo.toUpperCase()}`;
                if (this.finalData.carNumber.length === 7) {
                    if (!isCarNum(this.finalData.carNumber)) {
                        this.$message({
                            type: 'error',
                            message: '车牌号码有误!',
                            duration: 1200
                        });
                        return;
                    }
                    const loading = this.$loading();

                    this.selectWorkOrder(() => {
                        typeof fn === 'function' && fn();
                        loading.close();
                    }, loading);
                }
            },
            /**
             *  合并套餐
             *  @param arr 套餐列表
             */
            handlePackageMerge(arr) {
                const packages = [];
                const remainTimes = { 1: 0 };

                if (arr.length > 0) {
                    for (let i = 0; i < arr.length; i++) {
                        if (arr[i].memberItemUseRecords.length > 0) {
                            let memberItemRC = arr[i].memberItemUseRecords;

                            for (let j = 0; j < memberItemRC.length; j++) {
                                let canCreate = true;

                                packages.push({
                                    id: memberItemRC[j].serviceItem.id,
                                    itemName: memberItemRC[j].serviceItem.itemName,
                                    remainTimes: memberItemRC[j].remainTimes,
                                    useTimes: memberItemRC[j].useTimes,
                                    validTime: arr[i].validTime ? arr[i].validTime.slice(0, 10) : '永久'
                                });

                                for (let k in remainTimes) {
                                    if (parseInt(k) === parseInt(memberItemRC[j].serviceItem.id)) {
                                        remainTimes[k] = parseInt(remainTimes[k]) + parseInt(memberItemRC[j].remainTimes);
                                        canCreate = false;
                                        break;
                                    }
                                }
                                if (canCreate) {
                                    remainTimes[memberItemRC[j].serviceItem.id] = memberItemRC[j].remainTimes;
                                }
                            }
                        }
                    }
                }
                return {
                    packages,
                    remainTimes
                };
            },
            /**
             *  选择服务项目类别操作
             *  @param type 服务项类别
             */
            handleSelectItemType(type) {
                this.itemType = type;
                this.itemPage.startPage = 1;
                const loading = this.$loading();

                this.selectServiceItem(() => {
                    loading.close();
                }, loading);
            },
            /**
             *  搜索服务项目操作
             */
            handleItemSearch() {
                this.itemPage.startPage = 1;
                this.selectServiceItem(() => {
                }, false);
            },
            /**
             *  清空服务项搜索框,重新筛选
             */
            handleEmptyItemSearchKey() {
                this.itemSearchKey = '';
                this.handleItemSearch();
            },
            /**
             *  选中服务项目
             */
            handleItemSelect(item) {
                if (this.itemIdList.indexOf(item.id) > -1) {
                    this.$message({
                        type: 'error',
                        message: '已有员工创建该项目'
                    });
                    return;
                }
                let canInsert = true;

                for (let i = 0; i < this.itemSelectDatas.length; i++) {
                    if (item.id === this.itemSelectDatas[i].serviceItemId) {
                        canInsert = false;
                        this.$set(this.itemSelectDatas[i], 'itemsTimes', this.itemSelectDatas[i].itemsTimes + 1);
                        const coupon = this.handleChooseMaxCoupon(
                            this.itemSelectDatas[i],
                            this.itemSelectDatas[i].couponKey
                        );

                        if (!isEmpty(coupon.takeUuid) && this.itemSelectDatas[i]._autoCoupon) {
                            this.$set(this.itemSelectDatas[i], 'couponUuid', coupon.takeUuid);
                            this.$set(this.itemSelectDatas[i], 'couponName', coupon.coupon.cpName);
                            this.$set(this.itemSelectDatas[i], 'couponDeduct', coupon.coupon.cpMoney);
                            this.$set(this.itemSelectDatas[i], '_useCard', false);
                        }
                        break;
                    }
                }
                if (canInsert) {
                    const hasCard = this.packageRemain[item.id] && this.packageRemain[item.id] > 0;
                    const couponKey = this.couponKey;

                    item._autoCoupon = true;
                    const coupon = this.handleChooseMaxCoupon(item, couponKey);
                    const insertItem = {
                        id: '',
                        serviceItemId: item.id,
                        serviceItemName: item.itemName,
                        itemsTimes: 1,
                        usePackageTimes: 0,
                        salePrice: item.salePrice,
                        workerId: '',
                        salerId: '',
                        itemType: item.itemType,
                        discountType: item.discountType || '',
                        discountPrice: 0,
                        discountNumber: item.discountNumber || (item.discountType == 1 ? 10 : 0),
                        couponKey,
                        couponUuid: coupon.takeUuid || '',
                        couponName: coupon.coupon ? coupon.coupon.cpName || '' : '',
                        couponDeduct: coupon.coupon ? coupon.coupon.cpMoney || 0 : 0,
                        totalPrice: 0,
                        _autoCoupon: true,
                        _readonly: true,
                        _hasCard: hasCard,
                        _useCard: hasCard && isEmpty(coupon.takeUuid)
                    };
                    const arr = doCopy(this.itemSelectDatas);

                    arr.push(insertItem);
                    this.itemSelectDatas = arr;
                    this.couponKey++;
                }
            },
            /**
             *  计算套餐抵扣数
             */
            handleCalcUsePackageTimes(row, index) {
                if (row._useCard) {
                    const arr = [this.handleCalcItemTimes(row.itemsTimes), this.packageRemain ? this.packageRemain[row.serviceItemId] || 0 : 0];

                    this.$set(this.itemSelectDatas[index], 'usePackageTimes', Math.min.apply(Math, arr));
                }
                return parseInt(row.usePackageTimes);
            },
            /**
             *  切换套餐使用状态操作
             */
            handleToggleUseCard(index) {
                const currSelectItem = this.itemSelectDatas[index];

                this.$set(currSelectItem, '_useCard', !currSelectItem._useCard);
                this.$set(currSelectItem, '_autoCoupon', false);
                if (currSelectItem._useCard) {
                    this.$set(currSelectItem, 'couponUuid', '');
                    this.$set(currSelectItem, 'couponName', '');
                    this.$set(currSelectItem, 'couponDeduct', 0);
                    for (let i = 0; i < this.couponsData.length; i++) {
                        if (this.couponsData[i].couponKey === currSelectItem.couponKey) {
                            this.$set(this.couponsData[i], 'couponKey', '');
                            break;
                        }
                    }
                }
            },
            /**
             *  打开选择员工弹窗,记录当前项目索引和员工类别
             *  @param key 员工类别
             *  @param index 项目索引
             */
            handleAddWorker(key, index) {
                this.employeeSelectVisible = true;
                this.currItemIndex = index;
                this.currItemType = this.itemSelectDatas[index].itemType || '-1';
                this.currItemWorkerType = key;
                this.employeeDatas.forEach((d) => {
                    this.$set(d, 'selected', d.id === this.itemSelectDatas[index][workerTypeIdMap[key]]);
                });
            },
            /**
             *  选中员工操作
             *  @param data 员工信息对象
             */
            handleEmployeeSelect(data) {
                if (data.selected) {
                    this.$set(this.itemSelectDatas[this.currItemIndex], workerTypeIdMap[this.currItemWorkerType], '');
                    this.$set(this.itemSelectDatas[this.currItemIndex], workerTypeMap[this.currItemWorkerType], '');
                } else {
                    this.$set(this.itemSelectDatas[this.currItemIndex], workerTypeIdMap[this.currItemWorkerType], data.id);
                    this.$set(this.itemSelectDatas[this.currItemIndex], workerTypeMap[this.currItemWorkerType], data.realName);
                }
                this.employeeSelectVisible = false;
            },
            /**
             *  删除项目操作
             *  @param index 项目索引
             */
            handleDeleteItem(index) {
                for (let i = 0; i < this.couponsData.length; i++) {
                    if (this.couponsData[i].couponKey === this.itemSelectDatas[index].couponKey) {
                        this.$set(this.couponsData[i], 'couponKey', '');
                        break;
                    }
                }
                const arr = doCopy(this.itemSelectDatas);

                this.$delete(arr, index);
                this.itemSelectDatas = arr;
            },
            /**
             *  打开项目次数输入框操作
             *  @param i 项目索引
             */
            handleOpenInsertItemTimes(i) {
                if (this.itemSelectDatas[i]._readonly) {
                    this.$set(this.itemSelectDatas[i], '_readonly', false);
                }
            },
            /**
             *  关闭项目次数输入框
             *  @param i 项目索引
             */
            handleCloseInsertItemTimes(i) {
                this.$set(this.itemSelectDatas[i], '_readonly', true);
            },
            /**
             *  修改项目次数
             *  @param index 项目索引
             *  @param e 输入框事件对象
             */
            handleChangeItemTimes(index, e) {
                this.$set(this.itemSelectDatas[index], 'itemsTimes', this.handleCalcItemTimes(e.target.value));
                const coupon = this.handleChooseMaxCoupon(
                    this.itemSelectDatas[index],
                    this.itemSelectDatas[index].couponKey
                );

                if (!isEmpty(coupon.takeUuid) && this.itemSelectDatas[index]._autoCoupon) {
                    this.$set(this.itemSelectDatas[index], 'couponUuid', coupon.takeUuid);
                    this.$set(this.itemSelectDatas[index], 'couponName', coupon.coupon.cpName);
                    this.$set(this.itemSelectDatas[index], 'couponDeduct', coupon.coupon.cpMoney);
                    this.$set(this.itemSelectDatas[index], '_useCard', false);
                } else {
                    this.$set(this.itemSelectDatas[index], 'couponUuid', '');
                    this.$set(this.itemSelectDatas[index], 'couponName', '');
                    this.$set(this.itemSelectDatas[index], 'couponDeduct', 0);
                    for (let i = 0; i < this.couponsData.length; i++) {
                        if (this.couponsData[i].couponKey === this.itemSelectDatas[index].couponKey) {
                            this.$set(this.couponsData[i], 'couponKey', '');
                            break;
                        }
                    }
                }
            },
            /**
             *  控制项目次数不能小于1
             *  @param i 输入的次数
             */
            handleCalcItemTimes(i) {
                return isEmpty(i) || parseInt(i) <= 0 ? 1 : parseInt(i);
            },
            /**
             *  添加临时项目操作
             */
            handleAddTempItem() {
                const errorMsg = {
                    tempItemName: '请输入临时项目名称',
                    tempItemPrice: '请输入临时项目价格'
                };

                for (let key in errorMsg) {
                    if (isEmpty(this[key])) {
                        this.$message({
                            type: 'error',
                            message: errorMsg[key],
                            duration: 1200
                        });
                        return;
                    }
                }
                for (let i = 0; i < this.itemSelectDatas.length; i++) {
                    if (this.tempItemName === this.itemSelectDatas[i].serviceItemName) {
                        this.$message({
                            type: 'error',
                            message: '项目名称已存在',
                            duration: 1200
                        });
                        return;
                    }
                }
                const couponKey = this.couponKey;
                const item = {
                    itemType: -1,
                    salePrice: parseFloat(this.tempItemPrice).toFixed(2),
                    itemsTimes: 1,
                    _autoCoupon: true
                };
                const coupon = this.handleChooseMaxCoupon(item, couponKey);
                const insertItem = {
                    id: '',
                    serviceItemId: '',
                    serviceItemName: this.tempItemName,
                    itemsTimes: 1,
                    usePackageTimes: 0,
                    salePrice: item.salePrice,
                    workerId: '',
                    salerId: '',
                    itemType: -1,
                    discountType: '',
                    discountPrice: 0,
                    discountNumber: '',
                    couponKey,
                    couponUuid: coupon.takeUuid || '',
                    couponName: coupon.coupon ? coupon.coupon.cpName || '' : '',
                    couponDeduct: coupon.coupon ? coupon.coupon.cpMoney || 0 : 0,
                    totalPrice: 0,
                    _autoCoupon: true,
                    _readonly: true
                };
                const arr = doCopy(this.itemSelectDatas);

                arr.unshift(insertItem);
                this.itemSelectDatas = arr;
                this.tempItemVisible = false;
                this.couponKey++;
            },
            /**
             *  筛选出能用的最大面额的优惠券
             *  @param item 需要筛选的项目
             *  @param couponKey
             */
            handleChooseMaxCoupon(item, couponKey) {
                let itemType = item.itemType;
                let itemPrice = item.salePrice * (item.itemsTimes || 1);
                let maxCoupon = {
                    coupon: {
                        cpMoney: 0
                    }
                };
                let maxCouponIndex = -1;

                this.couponsData.forEach((coupon, index) => {
                    if (
                        item._autoCoupon &&
                        (isEmpty(coupon.couponKey) || coupon.couponKey == item.couponKey ) &&
                        (!coupon.coupon.itemType || itemType == coupon.coupon.itemType) &&
                        coupon.coupon.cpMoney - itemPrice <= 0 &&
                        coupon.coupon.cpMoney - maxCoupon.coupon.cpMoney >= 0) {
                        maxCoupon = coupon;
                        maxCouponIndex = index;
                    }
                });
                if (maxCouponIndex > -1) {
                    this.$set(this.couponsData[maxCouponIndex], 'couponKey', couponKey);
                    return this.couponsData[maxCouponIndex];
                }
                return {};
            },
            /**
             *  打开卡券选择弹窗
             *  @param itemType 当前项目类型
             *  @param index 当前项目索引
             */
            handlePopCoupon(itemType, index) {

                if (this.itemSelectDatas[index]._useCard) {
                    this.$message({
                        type: 'warning',
                        message: '已选择使用套餐，不能使用优惠券！'
                    });
                    return;
                }
                this.currItemIndex = index;
                this.currItemType = itemType || '-1';

                const temp = [];

                this.couponsData.forEach((coupon, index) => {
                    if (this.isCouponListShow(coupon)) {
                        coupon._index = index;
                        temp.push(coupon);
                    }
                });
                this.couponsItemData = temp;
                this.couponSelectVisible = true;
            },
            /**
             *  控制优惠券选择列表的展示
             *  @param c 优惠券对象
             *  @param index 当前项目索引
             */
            isCouponListShow(c, index) {
                index = isEmpty(index) ? this.currItemIndex : index;
                const currItem = this.itemSelectDatas[index];

                if (isEmpty(currItem)) return false;
                const itemType = currItem.itemType || -1;

                return (!c.couponKey || c.couponKey == currItem.couponKey) &&
                    (!c.coupon.itemType || c.coupon.itemType == itemType) &&
                    c.coupon.cpMoney - currItem.itemsTimes * currItem.salePrice <= 0;
            },
            /**
             *  选中和取消选中优惠券
             *  @param uuid 优惠券uuid
             */
            handleSelectCoupon(uuid) {
                const currItem = this.itemSelectDatas[this.currItemIndex];

                this.$set(currItem, '_autoCoupon', false);
                for (let i = 0; i < this.couponsData.length; i++) {
                    if (this.couponsData[i].takeUuid === uuid) {
                        if (this.couponsData[i].couponKey === currItem.couponKey) {
                            this.$set(this.couponsData[i], 'couponKey', '');
                            this.$set(currItem, 'couponUuid', '');
                            this.$set(currItem, 'couponName', '');
                            this.$set(currItem, 'couponDeduct', 0);
                        } else {
                            this.couponsItemData.forEach((itemCoupon) => {
                                this.$set(itemCoupon, 'couponKey', '');
                                this.$set(this.couponsData[itemCoupon._index], 'couponKey', '');
                            });
                            this.$set(this.couponsData[i], 'couponKey', currItem.couponKey);
                            this.$set(currItem, 'couponUuid', this.couponsData[i].takeUuid);
                            this.$set(currItem, 'couponName', this.couponsData[i].coupon.cpName);
                            this.$set(currItem, 'couponDeduct', this.couponsData[i].coupon.cpMoney);
                        }
                    }
                }
                this.couponSelectVisible = false;
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        mounted() {
            this.finalData.id = getDataFromParam('id') || '';
            this.isEdit = !isEmpty(this.finalData.id);
            const loading = this.$loading();
            const f0 = new Promise((resolve) => {
                if (isEmpty(this.finalData.id)) {
                    resolve();
                } else {
                    this.selectWorkOrder(() => {
                        resolve();
                    }, loading, this.finalData.id);
                }
            });
            const f1 = new Promise((resolve) => {
                this.selectEmployeeDatas(() => {
                    resolve();
                }, loading);
            });
            const f2 = new Promise((resolve) => {
                if (isEmpty(this.finalData.id)) {
                    this.selectDefaultCarArea(() => {
                        resolve();
                    }, loading);
                } else {
                    resolve();
                }
            });
            const f3 = new Promise((resolve) => {
                this.selectServiceItem(() => {
                    resolve();
                }, loading);
            });
            const f4 = new Promise((resolve) => {
                this.selectItemType(() => {
                    resolve();
                }, loading);
            });

            Promise.all([f0, f1, f2, f3, f4]).then(() => {
                this.$emit('input', false);
                loading.close();
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
