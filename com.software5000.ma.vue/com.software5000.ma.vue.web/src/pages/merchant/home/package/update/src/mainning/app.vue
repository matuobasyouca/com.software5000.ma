<template>
    <div class="page-main">
        <div class="page-left">
            <div class="page-left-top">
                <div class="list">
                    <div class="list-item detail-list-item">
                        <div class="list-item-text"><i class="request"></i>套餐名称</div>
                        <div class="list-item-con">
                            <zs-input v-model="packageBaseData.packageName" class="list-item-con-input" :maxlength="10"
                                      placeholder="请输入套餐名称"></zs-input>
                            <span class="list-item-con-input-length">{{ `${packageBaseData.packageName.length}/10`}}</span>
                        </div>
                    </div>
                    <div class="list-item detail-list-item">
                        <div class="list-item-text"><i class="request"></i>套餐价格</div>
                        <div class="list-item-con">
                            <zs-input v-model="packageBaseData.salePrice" class="list-item-con-input"
                                      placeholder="请输入套餐价格"></zs-input>
                        </div>
                    </div>
                    <div class="list-item detail-list-item">
                        <div class="list-item-text"><i class="request"></i>有效期</div>
                        <div class="list-item-con">
                            <zs-select v-model="packageBaseData.validTerm">
                                <zs-option :value="1" label="1年"></zs-option>
                                <zs-option :value="2" label="2年"></zs-option>
                                <zs-option :value="3" label="3年"></zs-option>
                                <zs-option :value="0" label="永久"></zs-option>
                            </zs-select>
                        </div>
                    </div>

                </div>
                <div class="page-left-bottom">
                    <p class="package-words">套餐项目总价：<span class="red">{{packageCount}}</span></p>
                    <zs-table
                            class="package-table"
                            :data="itemSelectDatas"
                            :columns="packageColumns"
                            :context="context"
                            :no-data-colspan="4"
                            noDataText="暂无数据"
                            border>
                    </zs-table>
                    <p class="package-words">使用说明：</p>
                    <zs-input
                            class="package-textarea"
                            type="textarea"
                            resize="none"
                            v-model="packageBaseData.instructions"
                            :maxlength="200"
                    ></zs-input>
                    <div class="package-control">
                        <zs-button type="cancel" @click="turnPage('/web/merchant/home/package/list.html')">取消
                        </zs-button>
                        <zs-button type="primary" @click="savePackage">保存</zs-button>
                    </div>
                </div>
            </div>
        </div>
        <!--可选项目列表-->
        <div class="page-right">
            <p class="block-5-title">可选项目</p>
            <div class="search-wrapper">
                <zs-input
                        v-model="itemSearchKey"
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
                    <zs-pagination
                            :current="itemPage.startPage"
                            :total="itemPage.total"
                            :page-size="itemPage.pageSize"
                            @on-change="handlePageChange(2,$event)"
                            simple
                    ></zs-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { isEmpty, doCopy, turnToNextPage, getDataFromParam } from '../../../../../../../assets/js/utils';

    const pageChangeMap = ['employeePage', 'memberPage', 'itemPage'];
    const pageChangeFnMap = ['selectEmployeeDatas', 'memberSearch', 'selectServiceItem'];

    export default {
        name: 'maining',
        data() {
            return {
                id: '',
                packageBaseData: {
                    packageName: '',
                    salePrice: '',
                    validTerm: '',
                    originalPrice: '',
                    instructions: '',
                    packageAndItems: []
                },

                //套餐项目
                context: this,
                packageColumns: [
                    {
                        title: '套餐项目',
                        key: 'serviceItemName',
                        width: 254
                    },
                    {
                        title: '数量',
                        width: 68,
                        className: 'item-times',
                        render(row, col, index, _this) {
                            return row._readonly ? `<p @click="handleOpenInsertItemTimes(${index})">${_this.calcItemTimes(row.useTimes)}</p>` : `
                                    <input
                                        class="item-times"
                                        type="number"
                                        autofocus
                                        value="${_this.calcItemTimes(row.useTimes)}"
                                        @blur="handleCloseInsertItemTimes(${index})"
                                        @change="handleChangeItemTimes(${index},$event)">`;
                        }
                    },
                    {
                        title: '单价',
                        key: 'salePrice',
                        width: 210
                    },
                    {
                        title: '操作',
                        key: 'serviceItemName',
                        width: 118,
                        className: 'control',
                        render(row, col, index) {
                            let ret = `<i @click="delItem(${index})" class="zs-icon-delete2"></i>`;

                            return ret;
                        }
                    }
                ],

                //客户查询
                userResultVisible: false,

                //客户套餐

                //服务项目
                itemSearchKey: '',
                itemDatas: [],
                itemColumns: [
                    {
                        title: '服务项目',
                        key: 'itemName'
                    },
                    {
                        title: '售价',
                        key: 'salePrice'
                    }
                ],
                itemTypes: [],
                itemType: '',
                //已选服务项目
                itemSelectDatas: [],
                itemSelectColumns: [
                    {
                        title: '已选项目',
                        key: '',
                        width: 200
                    },
                    {
                        title: '售价',
                        key: '',
                        width: 68
                    },
                    {
                        title: '数量',
                        key: '',
                        width: 68
                    },
                    {
                        title: '套餐抵扣数',
                        key: '',
                        width: 108
                    },
                    {
                        title: '总金额',
                        key: '',
                        width: 104
                    },
                    {
                        title: '派工人员',
                        key: '',
                        width: 102
                    },
                    {
                        title: '销售人员',
                        key: '',
                        width: 102
                    },
                    {
                        title: '操作',
                        key: '',
                        width: 90
                    }
                ],
                //车牌地区选择
                carNumSelectVisible: false,
                currCarArea: '闽',
                currCarLetter: 'D',
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
                //新建临时项目
                tempItemVisible: false,
                //选择员工
                employeeSelectVisible: false,
                employeeDatas: [
                    {
                        name: '大波波',
                        type: '管理员',
                        selected: false
                    }, {
                        name: '小波波',
                        type: '操作员',
                        selected: true
                    }
                ],
                employeeColumns: [
                    {
                        title: '',
                        width: 114,
                        render(row) {
                            return `<span class="table-radio ${row.selected ? 'curr' : ''}"></span>`;
                        }
                    },
                    {
                        title: '员工姓名',
                        key: 'name',
                        width: 226
                    },
                    {
                        title: '工种',
                        key: 'type',
                        width: 184
                    }
                ],

                //可选项目部分
                itemPage: {
                    startPage: 1,
                    pageSize: 10,
                    total: 0
                }

            };
        },
        computed: {
            packageWrapStyle() {
                return 'max-height:138px;';
            },
            packageCount() {
                let ret = 0;

                for (let x in this.itemSelectDatas) {
                    ret += this.itemSelectDatas[x].useTimes * this.itemSelectDatas[x].salePrice;
                }
                return Number(Number(ret).toFixed(2));
            }
        },
        //监听数值变动时触发的事件， value(val){}
        watch: {},
        methods: {
            //根据id获取套餐详情
            getPackageData(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageById,
                    {
                        businessPackageId: this.id
                    },
                    loading,
                    (data, model) => {
                        if (loading) model.close();
                        if (data.code === 'ZS011000') {
                            this.packageBaseData = data.data;
                            let list = data.data.packageAndItems;
                            let arr = [];

                            for (let i = 0; i < list.length; i++) {
                                arr[i] = {
                                    id: '',
                                    serviceItemId: list[i].serviceItemId,
                                    serviceItemName: list[i].serviceItem.itemName,
                                    useTimes: list[i].useTimes,
                                    usePackageTimes: 0,
                                    salePrice: list[i].serviceItem.salePrice,
                                    workerId: '',
                                    salerId: '',
                                    itemType: list[i].serviceItem.itemType,
                                    _readonly: true
                                };
                            }
                            this.itemSelectDatas = arr;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                            setTimeout(() => {
                                turnToNextPage('/web/merchant/home/package/list.html');
                            }, 1200);
                        }
                        fn && fn();
                    }
                );
            },
            //获取商家服务项
            selectServiceItem(fn, loading) {
                this.$ajax(
                    this.$joggle.merchant.workorder.selectServiceItem,
                    {
                        startPage: this.itemPage.startPage,
                        pageSize: this.itemPage.pageSize,
                        searchValue: this.itemSearchKey,
                        itemType: this.itemType
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
            //删除套餐项目已选项中的一项
            delItem(index) {
                const arr = doCopy(this.itemSelectDatas);

                this.$delete(arr, index);
                this.itemSelectDatas = arr;
            },
            handleCloseSearchResult() {
                this.userResultVisible = false;
            },
            //设置默认车牌所属城市字母
            handleSetCarLetter(l) {
                if (!isEmpty(this.currCarArea)) {
                    this.currCarLetter = l;
                    this.carNumSelectVisible = false;
                }
            },
            //设置默认车牌
            handleSetDefault(l) {
                if (!isEmpty(this.currCarArea)) {
                    this.currCarLetter = l;
                    this.defaultCarNum = `${this.currCarArea}${l}`;
                    this.carNumSelectVisible = false;
                }
            },
            //设置默认操作的显示文字
            handleSetDefaultText(l) {
                return this.currCarArea === this.defaultCarNum[0] && l === this.defaultCarNum[1] ? '默认' : '设为默认';
            },

            //搜索服务项目
            handleItemSearch() {
                this.itemPage.startPage = 1;
                this.selectServiceItem(() => {
                }, false);
            },
            //清空服务项搜索框,重新筛选
            handleEmptyItemSearchKey() {
                this.itemSearchKey = '';
                this.handleItemSearch();
            },
            //选择服务项目类别
            handleSelectItemType(id) {
                this.itemType = id;
                this.itemPage.startPage = 1;
                const loading = this.$loading();

                this.selectServiceItem(() => {
                    loading.close();
                }, loading);
            },
            //选中服务项目
            handleItemSelect(item) {
                let canInsert = true;

                for (let i = 0; i < this.itemSelectDatas.length; i++) {
                    if (item.id === this.itemSelectDatas[i].serviceItemId) {
                        canInsert = false;
                        this.itemSelectDatas[i].useTimes++;
                        break;
                    }
                }
                if (canInsert) {
                    const insertItem = {
                        id: '',
                        serviceItemId: item.id,
                        serviceItemName: item.itemName,
                        useTimes: 1,
                        usePackageTimes: 0,
                        salePrice: item.salePrice,
                        workerId: '',
                        salerId: '',
                        itemType: item.itemType,
                        _readonly: true
                    };
                    const arr = doCopy(this.itemSelectDatas);

                    arr.unshift(insertItem);
                    this.itemSelectDatas = arr;
                }
            },
            //startPage切换
            handlePageChange(type, page) {
                this[pageChangeMap[type]].startPage = page;
                const loading = this.$loading();

                this[pageChangeFnMap[type]](() => {
                    loading.close();
                }, loading);
            },
            //获取服务项类别
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
            //打开项目次数输入框
            handleOpenInsertItemTimes(i) {
                if (this.itemSelectDatas[i]._readonly) {
                    this.$set(this.itemSelectDatas[i], '_readonly', false);
                }
            },
            //关闭项目次数输入框
            handleCloseInsertItemTimes(i) {
                this.$set(this.itemSelectDatas[i], '_readonly', true);
            },
            //修改项目次数
            handleChangeItemTimes(i, e) {
                this.$set(this.itemSelectDatas[i], 'useTimes', this.calcItemTimes(e.target.value));
            },
            //控制项目次数不能小于1
            calcItemTimes(i) {
                return isEmpty(i) || parseInt(i) <= 0 ? 1 : parseInt(i);
            },
            //验证套餐卡必填项目
            certPackage() {
                let errorPackageMsg = {
                    packageName: '请输入套餐名',
                    salePrice: '请输入正确的套餐价格',
                    validTerm: '请选择有效期',
                    originalPrice: '请选择服务项目'
                };

                for (let x in errorPackageMsg) {
                    let data = this.packageBaseData[x] + '';
                    let salePrice = x == 'salePrice' && !data.isInReg(/^\d+(\.\d+)?$/);
                    let originalPrice = x == 'originalPrice' && data == 0;

                    if (isEmpty(data) || salePrice || originalPrice) {
                        this.$message({
                            type: 'error',
                            message: errorPackageMsg[x],
                            duration: 1200
                        });
                        return false;
                    }
                }
                return true;
            },
            //全角转半角
            ToCDB(str) {
                let tmp = '';

                for (let i = 0; i < str.length; i++) {
                    if (str.charCodeAt(i) == 12288) {
                        tmp += String.fromCharCode(str.charCodeAt(i) - 12256);
                        continue;
                    }
                    if (str.charCodeAt(i) > 65280 && str.charCodeAt(i) < 65375) {
                        tmp += String.fromCharCode(str.charCodeAt(i) - 65248);
                    }
                    else {
                        tmp += String.fromCharCode(str.charCodeAt(i));
                    }
                }
                return tmp;
            },
            //保存套餐卡
            savePackage() {
                this.packageBaseData.packageAndItems = this.itemSelectDatas;
                this.packageBaseData.originalPrice = this.packageCount;

                if (!this.certPackage()) return;
                this.packageBaseData.salePrice = (this.packageBaseData.salePrice + '').indexOf('.') > -1 ? parseFloat(this.packageBaseData.salePrice).toFixed(2) : this.packageBaseData.salePrice;
                if (this.id) {
                    //修改该套餐
                    this.$ajax(
                        this.$joggle.merchant.businessPackage.updateBusinessPackage,
                        this.packageBaseData,
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
                                    turnToNextPage('/web/merchant/home/package/list.html');
                                }, 1200);
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        }
                    );
                } else {
                    //新建套餐
                    this.$ajax(
                        this.$joggle.merchant.businessPackage.insertBusinessPackage,
                        this.packageBaseData,
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
                                    turnToNextPage('/web/merchant/home/package/list.html');
                                }, 1200);
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: data.msg
                                });
                            }
                        }
                    );
                }

            },

            //页面跳转
            turnPage(url) {
                turnToNextPage(url);
            }
        },
        created() {
            this.id = getDataFromParam('id');

            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                if (this.id) {
                    this.getPackageData(() => {
                        resolve();
                    }, false);
                } else {
                    resolve();
                }
            });
            const f2 = new Promise((resolve) => {
                this.selectServiceItem(() => {
                    resolve();
                }, false);
            });
            const f3 = new Promise((resolve) => {
                this.selectItemType(() => {
                    resolve();
                }, false);
            });

            Promise.all([f1, f2, f3]).then(() => {
                loading.close();
            });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
