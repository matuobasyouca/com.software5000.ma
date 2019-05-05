<template>
    <div class="page-base maining">
        <div class="maining-part-1">
            <zs-button type="primary" @click="batchDiscountDialog=true">批量设置折扣</zs-button>
        </div>
        <div class="maining-part-2">
            <div class="part2-item item-left" ref="refList1" :style="`height:${autoHeight}`">
                <ul class="item-table">
                    <li class="table-title">会员等级</li>
                    <li class="table-item" :class="{ selected : serviceSendData.memberLvlId==item.id }"
                        v-for="(item,index) in levelList"
                        @click="handSelectedLv(item)" :key="item.id">{{item.lvlName}}
                    </li>
                </ul>
            </div>
            <div class="part2-item item-middle" ref="refList2" :style="`height:${autoHeight}`">
                <ul class="item-table">
                    <li class="table-title">项目类别</li>
                    <li class="table-item" :class="{ selected : serviceSendData.itemType==item[0] }"
                        v-for="(item,index) in typeList"
                        @click="handSelectedType(item)" :key="item[0]">{{item[1]}}
                    </li>
                </ul>
            </div>
            <div class="part2-item item-right" ref="refList3" :style="`height:${autoHeight}`">
                <zs-table
                        class="package-table"
                        :data="serviceList"
                        :columns="columns"
                        :context="context"
                        :no-data-colspan="5"
                        border>
                </zs-table>
            </div>
        </div>
        <zs-dialog class="create-dialog dialog" v-model="batchDiscountDialog">
            <span slot="title">批量设置折扣</span>
            <div class="list">
                <div class="list-item detail-list-item">
                    <div class="list-item-text"><i class="request"></i>会员等级</div>
                    <div class="list-item-con">
                        <zs-select placeholder="请选择会员等级" v-model="batchSendData.memberLvlId">
                            <zs-option v-for="item in levelList" :value="item.id" :label="item.lvlName"
                                       :key="item.id"></zs-option>
                        </zs-select>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-text"><i class="request"></i>项目类别</div>
                    <div class="list-item-con">
                        <zs-select placeholder="请选择会员等级" v-model="batchSendData.itemType">
                            <zs-option value="" label="全部"></zs-option>
                            <zs-option v-for="item in typeList" :value="item[0]" :label="item[1]"
                                       :key="item[0]"></zs-option>
                        </zs-select>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-text"><i class="request"></i>会员折扣</div>
                    <div class="list-item-con">
                        <zs-input v-model.trim="batchSendData.discountNumber" class="list-item-con-input"
                                  placeholder="请输入0-10位，可保留1位小数"></zs-input>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-con hit">
                        设置之后，之前设置的折扣将失效
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <zs-button type="cancel" @click="batchDiscountDialog=false">取消</zs-button>
                <zs-button type="primary" @click="updateBatchDiscount">确定</zs-button>
            </span>
        </zs-dialog>
        <zs-dialog class="create-dialog dialog" v-model="singleDiscountDialog">
            <span slot="title">设置会员折扣</span>
            <div class="list">
                <div class="list-item detail-list-item">
                    <div class="list-item-text">项目名称</div>
                    <div class="list-item-con">
                        <p class="text">{{singleDiscountData.itemName}}</p>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-text">售价</div>
                    <div class="list-item-con">
                        <p class="text">{{singleDiscountData.salePrice}}</p>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-text"><i class="request"></i>类型</div>
                    <div class="list-item-con">
                        <zs-radio-group class="radio" @change="handleDiscountTypeChange"
                                        v-model="singleDiscountData.discountType">
                            <zs-radio :label="1">折扣</zs-radio>
                            <zs-radio :label="2">会员价</zs-radio>
                        </zs-radio-group>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-text"><i class="request"></i>会员价</div>
                    <div class="list-item-con">
                        <zs-input v-model.trim="singleDiscountData.discountNumber" class="list-item-con-input"
                                  :placeholder="singleDiscountData.discountType==1 ? '请输入0-10位，可保留1位小数' : '请输入该项目会员价格' "></zs-input>
                    </div>
                </div>
                <div class="list-item detail-list-item">
                    <div class="list-item-con hit">
                        设置之后，之前设置的折扣将失效
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <zs-button type="cancel" @click="singleDiscountDialog=false">取消</zs-button>
                <zs-button type="primary" @click="updateSingleDiscount">确定</zs-button>
            </span>
        </zs-dialog>
    </div>
</template>

<script>
    import { isEmpty } from '../../../../../../../assets/js/utils.js';

    export default {
        components: {},
        data() {
            return {
                columns: [
                    {
                        title: '项目名称',
                        key: 'itemName',
                        width: 192
                    },
                    {
                        title: '售价',
                        key: 'salePrice',
                        width: 126
                    },
                    {
                        title: '会员折扣',
                        key: 'discountNumber',
                        width: 130,
                        render(row) {
                            return row.discountType == 1 ? row.discountNumber : '';
                        }
                    },
                    {
                        title: '会员价',
                        key: 'discountNumber',
                        width: 114,
                        render(row) {
                            return row.discountType == 2 ? row.discountNumber : '';
                        }
                    },
                    {
                        title: '操作',
                        key: 'state',
                        width: 130,
                        className: 'control',
                        render(row, col, index) {
                            return `<span @click="handleSingleDialog('${index}')">设置</span>`;
                        }
                    }
                ],
                context: this,

                levelList: [],
                typeList: [],
                serviceList: [],
                serviceSendData: {
                    memberLvlId: '-1',
                    itemType: '-1'
                },
                autoHeight: 'auto',

                batchDiscountDialog: false,
                batchSendData: {
                    memberLvlId: '',
                    itemType: '',
                    discountNumber: ''
                },
                createMemberLvData: {
                    id: '',
                    lvlName: '',
                    totalConsume: '',
                    discount: ''
                },

                singleDiscountData: {
                    itemName: '',
                    salePrice: '',
                    discountType: 1,
                    discountNumber: ''
                },
                singleDiscountDialog: false
            };
        },
        methods: {

            /* ------------------- insert (增) start -------------------*/

            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/
            /**
             *  批量修改会员等级折扣
             */
            updateBatchDiscount() {
                if (!this.handCertBatchDiscount()) return;
                this.batchSendData.discountNumber = Number(this.batchSendData.discountNumber).toFixed(1);
                this.$ajax(
                    this.$joggle.merchant.member.insertItemAndMemberLvl,
                    this.batchSendData,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                            this.batchDiscountDialog = false;
                            if (this.serviceSendData.memberLvlId == this.batchSendData.memberLvlId && (this.serviceSendData.itemType == this.batchSendData.itemType || this.batchSendData.itemType == '')) {
                                this.selectServiceItemList();
                            }
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /**
             *  单个修改会员等级折扣
             */
            updateSingleDiscount() {
                if (!this.handCertSingleDiscount()) return;
                this.singleDiscountData.discountNumber = Number(this.singleDiscountData.discountNumber).toFixed(1);
                this.singleDiscountData.memberLvlId = this.serviceSendData.memberLvlId;
                this.singleDiscountData.itemType = this.serviceSendData.itemType;
                this.singleDiscountData.serviceItemId = this.singleDiscountData.id;
                this.$ajax(
                    this.$joggle.merchant.member.insertItemAndMemberLvl,
                    this.singleDiscountData,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                            this.singleDiscountDialog = false;
                            this.selectServiceItemList();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/
            /**
             *  会员等级列表数据
             * @param fn 后续操作
             */
            selectPackageLvData(fn) {
                this.$ajax(
                    this.$joggle.merchant.setting.selectMemberLvlList,
                    {
                        startPage: 1,
                        pageSize: 100
                    },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.levelList = data.data.list;
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
             *  服务项分类列表数据
             * @param fn 后续操作
             */
            selectServiceTypeList(fn) {
                this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    { codeTypes: 'ServiceItemType' },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.typeList = data.data.ServiceItemType;
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
             *  服务项列表数据
             */
            selectServiceItemList() {
                if (this.serviceSendData.memberLvlId == '-1' || this.serviceSendData.itemType == '-1') {
                    return;
                }
                this.$ajax(
                    this.$joggle.merchant.member.selectServiceItemDiscountNumber,
                    this.serviceSendData,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.serviceList = data.data;
                            this.handleListHeight();
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             *  效验批量设置折扣数据
             * @returns {Boolean} 效验是否失败
             */
            handCertBatchDiscount() {
                let errorMsg = {
                    memberLvlId: '请选择会员等级',
                    discountNumber: '请输入正确的会员折扣'
                };

                for (let x in errorMsg) {
                    let data = this.batchSendData[x];
                    let num = false;

                    if (x == 'discountNumber') {
                        num = Number(data);
                        if (num) {
                            num = !(num <= 10 && num >= 0);
                        } else {
                            num = true;
                        }
                    }
                    if (isEmpty(data) || num) {
                        this.$message({
                            type: 'error',
                            message: errorMsg[x]
                        });
                        return false;
                    }
                }
                return true;
            },
            /**
             *  效验单个设置折扣数据
             * @returns {Boolean} 效验是否失败
             */
            handCertSingleDiscount() {
                let errorMsg = {
                    discountType: '请选择类型',
                    discountNumber: '请输入正确的会员折扣'
                };
                let type = this.singleDiscountData.discountType;

                if (type == 2) {
                    errorMsg.discountNumber = '请输入正确的会员价格';
                }
                for (let x in errorMsg) {
                    let data = this.singleDiscountData[x];
                    let num = false;

                    if (x == 'discountNumber') {
                        num = Number(data);
                        if (num) {
                            if (type == 1) {
                                num = !(num <= 10 && num >= 0);
                            } else {
                                num = num > this.singleDiscountData.salePrice;
                                errorMsg.discountNumber = num ? '该会员价格大于该售价' : errorMsg.discountNumber;
                            }
                        } else {
                            num = true;
                        }
                    }
                    if (isEmpty(data) || num) {
                        this.$message({
                            type: 'error',
                            message: errorMsg[x]
                        });
                        return false;
                    }
                }
                return true;
            },
            /**
             *  计算表格高度并修改对应高度
             */
            handleListHeight() {
                setTimeout(() => {
                    let listHeight = [];

                    listHeight[0] = this.$refs.refList1.offsetHeight;
                    listHeight[1] = this.$refs.refList2.offsetHeight;
                    listHeight[2] = this.$refs.refList3.offsetHeight;
                    this.autoHeight = Math.max.apply(null, listHeight) + 'px';
                }, 30);
            },
            /**
             *  选择等级
             * @param data 当前选中的数据
             */
            handSelectedLv(data) {
                this.serviceSendData.memberLvlId = parseInt(data.id);
                this.batchSendData.memberLvlId = parseInt(data.id);
                this.selectServiceItemList();
            },
            /**
             *  选择服务类型
             * @param data 当前选中的数据
             */
            handSelectedType(data) {
                this.serviceSendData.itemType = parseInt(data[0]);
                this.batchSendData.itemType = data[0];
                this.selectServiceItemList();
            },
            /**
             *  打开单个服务项目修改弹窗
             * @param index  选中服务项目列表的index
             */
            handleSingleDialog(index) {
                this.singleDiscountData = { ...this.serviceList[index] };
                this.singleDiscountData.discountType = this.singleDiscountData.discountType ? this.singleDiscountData.discountType : 1;
                setTimeout(() => {
                    this.singleDiscountDialog = true;
                }, 30);
            },
            /**
             *  折扣方式变动触发
             */
            handleDiscountTypeChange() {
                if (this.singleDiscountDialog == true) {
                    this.singleDiscountData.discountNumber = '';
                }
            }

            /* ------------------- handle (操) end -------------------*/
        },
        mounted() {
            let loadind = this.$loading();
            let p1 = new Promise((resolve) => {
                this.selectPackageLvData(() => {
                    resolve();
                });
            });
            let p2 = new Promise((resolve) => {
                this.selectServiceTypeList(() => {
                    resolve();
                });
            });

            Promise.all([p1, p2]).then(() => {
                if (!isEmpty(this.levelList) && !isEmpty(this.typeList)) {
                    this.serviceSendData.memberLvlId = this.levelList[0].id;
                    this.serviceSendData.itemType = this.typeList[0][0];
                    this.batchSendData.memberLvlId = this.levelList[0].id;
                    this.batchSendData.itemType = this.typeList[0][0];
                    this.selectServiceItemList();
                } else {
                    this.handleListHeight();
                    loadind.close();
                }
            });

        }
    };
</script>

<style lang="less">
    @import 'style.less';
</style>
