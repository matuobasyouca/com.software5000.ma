<template>
    <div class="newPackage-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">{{id ? '修改套餐' : '新建套餐'}}</ma-head>

        <div class="new-body1">
            <ul>
                <li>
                    <div class="li-title"><em>*</em> 套餐名称</div>
                    <zs-input :maxlength="10" placeholder="请输入套餐名称" v-model="packageBaseData.packageName"></zs-input>
                    <span class="li-length">{{packageBaseData.packageName.length}}/10</span>
                </li>
                <li class="row-select">
                    <div class="li-title"><em>*</em> 套餐有效期</div>
                    <select class="li-select" placeholder="请选择有效期" v-model="packageBaseData.validTerm">
                        <option value="" label="请选择有效期">请选择有效期</option>
                        <option :value="1" label="1年">1年</option>
                        <option :value="2" label="2年">2年</option>
                        <option :value="3" label="3年">3年</option>
                        <option :value="0" label="永久">永久</option>
                    </select>
                    <zs-icon class="li-deal" icon="arrow-right" size="10"></zs-icon>
                </li>
            </ul>
        </div>
        <div class="new-body2">
            <div class="title">套餐项目</div>
            <div class="content">
                <ul>
                    <li v-for="(it,index) in itemSelectDatas" :key="it.id">
                        <p class="li-h3">{{it.itemName}}</p>
                        <div class="li-price">
                            <em>￥{{it.salePrice}}</em>
                            <zs-input-number
                                    class="li-number"
                                    :min="0"
                                    v-model="it.itemsTimes"
                                    @change="handleCalcTotalPrice"></zs-input-number>
                        </div>
                    </li>
                </ul>
                <div class="money-total">项目总金额<em>￥{{packageBaseData.originalPrice}}</em></div>
                <div class="add-more" @click="handleSelectItemShow">
                    <zs-icon icon="ten-cross" size="16" text="添加新项目"></zs-icon>
                </div>
            </div>
        </div>
        <div class="new-body3">
            <div class="row">
                <div class="row-h3"><em>*</em> 套餐售价</div>
                <zs-input placeholder="请输入套餐售价" v-model.toFixed(2)="packageBaseData.salePrice"></zs-input>
            </div>
            <div class="row-textarea">
                <div class="row-h4">使用说明</div>
                <textarea placeholder="请输入使用说明" v-model="packageBaseData.instructions"></textarea>
            </div>

        </div>
        <div class="new-bottom">
            <zs-button class="btn-click" type="primary" @click="handleInsertJudge">提交</zs-button>
        </div>

        <!--下一页-->
        <zs-slide-page
                class="PackagePage"
                v-model="chooseItems"
                title="选择项目"
                @go-home="handleGoHome">
            <div class="item-select">
                <ul class="menu">
                    <li
                            v-for="(it,index) in itemTypes"
                            :key="index"
                            :class="[{'curr' : itemType === it[0]}]" @click="handleSelectItemType(it[0])">{{it[1]}}
                    </li>
                </ul>
                <ul class="list">
                    <li v-for="(it,index) in itemDatas" :key="it.id">
                        <p class="li-h3">{{it.itemName}}</p>
                        <div class="li-price">
                            <em>￥{{it.salePrice}}</em>
                            <zs-input-number
                                    class="li-number"
                                    :min="0"
                                    v-model="it.itemsTimes"
                                    @change="handleItemTimes(it,$event)"></zs-input-number>
                        </div>
                    </li>
                </ul>
                <div class="new-bottom">
                    <zs-button :class="itemTempDatas.length == 0 ? 'btn-click-bg' : 'btn-click'" type="primary"
                               @click="handleSelectSure">确定
                    </zs-button>
                </div>
            </div>
        </zs-slide-page>
    </div>
</template>

<script type="text/ecmascript-6">
    import { turnToNextPage, isEmpty, getDataFromParam, doCopy } from '../../../../../../assets/js/utils';
    import maHead from '../../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead
        },
        props: {},
        data(){
            return {
                isLoading: false,
                packageBaseData: {
                    packageName: '',
                    validTerm: '',
                    originalPrice: '',
                    salePrice: '',
                    instructions: '',
                    packageAndItems: []
                },
                chooseItems: false, //下一页
                itemTypes: [],
                itemType: '',
                itemDatas: [],
                itemTempDatas: [],
                itemSelectDatas: [], //已选项目列表
                selectDatas: [], //确定选择的项目列表
                itemTotalPrice: 0,
                id: 0
            }
        },
        computed: {},
        watch: {},
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 新建套餐
             */
            insertNewPackage(){
                this.packageBaseData.salePrice = parseFloat(this.packageBaseData.salePrice).toFixed(2);
                this.packageBaseData.originalPrice = parseFloat(this.packageBaseData.originalPrice).toFixed(2);
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
                            turnToNextPage('/wap/merchant/home/manage/service_package/package_list.html');
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 修改套餐
             */
            updateMessage(){
                let condition = this.packageBaseData;

                condition.id = this.id;
                if (condition.salePrice) {
                    condition.salePrice = parseFloat(condition.salePrice).toFixed(2);
                    condition.originalPrice = parseFloat(condition.originalPrice).toFixed(2);
                }
                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackage,
                    condition,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg,
                                duration: 1200
                            });
                            turnToNextPage('/wap/merchant/home/manage/service_package/package_list.html');
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 获取服务项类别
             * @param fn
             * @param loading
             */
            selectItemType(fn, loading){
                this.$ajax(
                    this.$joggle.mzscp.selectConstantTypes,
                    { codeTypes: 'ServiceItemType' },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const temp = data.data.ServiceItemType;

                            temp.unshift(['', '全部']);
                            this.itemTypes = temp;

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
            },
            /**
             * 获取商家服务项
             * @param fn
             * @param loading
             */
            selectServiceItem(fn, loading){
                this.$ajax(
                    this.$joggle.merchant.workorder.selectServiceItem,
                    {
                        itemType: this.itemType
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.itemDatas = data.data;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                        fn && fn();
                    }
                )
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 计算总价
             */
            handleCalcTotalPrice(){
                this.$nextTick(() => {
                    for (let i = 0; i < this.itemSelectDatas.length; i++) {
                        let it = this.itemSelectDatas[i];

                        if (it.itemsTimes === 0) {
                            this.$delete(this.itemSelectDatas, i);
                        }
                    }
                    let price = 0;
                    let baseData = [];

                    this.itemSelectDatas.forEach((item) => {
                        price += item.itemsTimes * item.salePrice;
                        baseData.push({ useTimes: item.itemsTimes, serviceItemId: item.id })
                    });
                    this.packageBaseData.packageAndItems = baseData;
                    this.packageBaseData.originalPrice = parseFloat(isEmpty(price) ? 0 : price).toFixed(2);
                })
            },
            /**
             * 打开项目选择目录
             */
            handleSelectItemShow(){
                this.itemTempDatas = doCopy(this.itemSelectDatas);
                this.itemDatas.forEach((oitem) => {
                    let needEmpty = true;

                    for (let i = 0; i < this.itemSelectDatas.length; i++) {
                        if (this.itemSelectDatas[i].id === oitem.id) {
                            this.$set(oitem, 'itemsTimes', this.itemSelectDatas[i].itemsTimes);
                            needEmpty = false;
                            break;
                        }
                    }
                    if (needEmpty) {
                        this.$set(oitem, 'itemsTimes', 0)
                    }
                });
                this.chooseItems = true;
            },
            /**
             * 点击目录
             */
            handleItemTimes(item, times){
                let canInsert = true;

                for (let i = 0; i < this.itemTempDatas.length; i++) {
                    let it = this.itemTempDatas[i];

                    if (it.id == item.id) {
                        if (times === 0) {
                            this.$delete(this.itemTempDatas, i);
                        } else {
                            this.itemTempDatas[i].itemsTimes = times;
                        }
                        canInsert = false;
                        break;
                    }
                }
                if (canInsert) {
                    this.itemTempDatas.push(item);
                }
            },
            /**
             * 项目类别选择
             * @param num
             */
            handleSelectItemType(num){
                this.itemType = num;
                const loading = this.$loading();

                this.selectServiceItem(() => {
                    loading.close();
                }, loading)
            },
            /**
             * 表单保存的验证
             */
            handleInsertJudge(){
                let _num = /^\d+(\.\d+)?$/;
                let errorMsg = '';

                if (isEmpty(this.packageBaseData.packageName)) {
                    errorMsg = '请输入套餐名称';
                } else if (isEmpty(this.packageBaseData.validTerm)) {
                    errorMsg = '请输入套餐有效期';
                } else if (isEmpty(this.packageBaseData.salePrice)) {
                    errorMsg = '请输入套餐售价';
                } else if (!_num.test(this.packageBaseData.salePrice)) {
                    errorMsg = '请输入正确的套餐售价';
                } else if (isEmpty(this.packageBaseData.packageAndItems)) {
                    errorMsg = '请选择套餐项目';
                }
                if (errorMsg != '') {
                    this.$message({
                        type: 'error',
                        duration: 1000,
                        message: errorMsg
                    });
                    return false;
                }
                //判断新增还是修改，如果id为空表示新增，
                if (!isEmpty(this.id)) {
                    this.updateMessage();
                } else {
                    this.insertNewPackage();
                }
            },
            /**
             * 确认添加
             */
            handleSelectSure(){
                if (this.itemTempDatas.length > 0) {
                    this.itemSelectDatas = doCopy(this.itemTempDatas);
                    this.handleCalcTotalPrice();
                    this.chooseItems = false
                }
            },
            /**
             * 编辑时的设置表单对应的值
             * @param idData
             */
            handleUpdataForm(idData){
                this.packageBaseData.packageName = idData.packageName;
                this.packageBaseData.validTerm = idData.validTerm;
                this.packageBaseData.salePrice = idData.salePrice;
                this.packageBaseData.instructions = idData.instructions;
                for (let i = 0; i < idData.packageAndItems.length; i++) {
                    let it = idData.packageAndItems[i];

                    it.serviceItem.itemsTimes = it.useTimes;
                    this.itemSelectDatas.push(it.serviceItem);
                }
                let price = 0;
                let baseData = [];

                this.itemSelectDatas.forEach((item) => {
                    price += item.itemsTimes * item.salePrice;
                    baseData.push({ useTimes: item.itemsTimes, serviceItemId: item.id })
                });
                this.packageBaseData.packageAndItems = baseData;
                this.packageBaseData.originalPrice = parseFloat(isEmpty(price) ? 0 : price).toFixed(2);

            },
            /**
             * 返回首页
             */
            handleGoHome(){
                turnToNextPage('/wap/merchant/home/manage/index.html');
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.id = getDataFromParam('id') ? getDataFromParam('id') : '';
            if (!isEmpty(this.id)) {
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageById,
                    { businessPackageId: this.id },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            let idData = data.data;

                            this.handleUpdataForm(idData);
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                );
            }

            const loading = this.$loading();
            const f1 = new Promise((resolve) => {
                this.selectItemType(() => {
                    resolve();
                }, loading)
            });
            const f2 = new Promise((resolve) => {
                this.selectServiceItem(() => {
                    resolve();
                }, loading)
            });

            Promise.all([f1, f2]).then(() => {
                loading.close();
            });
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
