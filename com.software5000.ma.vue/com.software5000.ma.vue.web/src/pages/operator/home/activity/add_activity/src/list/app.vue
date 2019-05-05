<template>
    <div class="add_activity">
        <ul class="add-form">
            <li class="form-li">
                <p class="h3"><em>*</em> 活动名称</p>
                <zs-input
                        v-model.trim="activity.clusterName"
                        :maxlength="20"
                        autofocus
                        placeholder="请输入拼团活动名称">
                </zs-input>
                <span class="length">{{activity.clusterName.length}}/20</span>
            </li>
            <li class="form-li">
                <p class="h3"><em>*</em> 活动图片<span class="more">（最多上传5张）</span></p>
                <div class="getPhotos">
                    <upload @change="handlePhotos" :images="activityPhoto" :index="1" :max="5"
                            @delete="deletePhoto"></upload>
                    <div class="photo-info">（图片尺寸大小：750*560px）</div>
                </div>
            </li>
            <li class="form-li li-img">
                <p class="h3">分享图片</p>
                <div class="getPhotos">
                    <upload @change="handlePhotosShare" :images="sharePhote" :index="2" :max="1"
                            @delete="deletePhotoShare"></upload>
                    <div class="photo-info">（图片尺寸大小：300*300px）</div>
                </div>
            </li>
            <li class="form-li">
                <p class="h3"><em>*</em> 分享描述</p>
                <zs-input class="textarea-li" type="textarea" v-model="activity.shareDescription" :maxlength="40"
                          resize="none"></zs-input>
                <span class="length">{{activity.shareDescription.length}}/40</span>
            </li>
            <li class="form-li">
                <p class="h3"><em>*</em> 活动时间</p>
                <date-picker
                        class="select-new-time"
                        v-model="activity.beginDay"
                        placeholder="请选择时间"
                        :disabledDate="-1"
                        :value="activity.beginDay"
                        @on-change="handleSetStartTime"
                ></date-picker>
                <span class="search_label">至</span>
                <date-picker
                        class="select-new-time"
                        v-model="activity.endDay"
                        placeholder="请选择时间"
                        :disabledDate="-1"
                        :value="activity.endDay"
                        @on-change="handleSetEndTime"
                ></date-picker>
            </li>
            <li class="form-li">
                <p class="h3"><em>*</em> 活动商家套餐</p>
                <zs-button v-if="choosePackageData.length===0" class="choose-merchant" type="primary"
                           @click="showDialogMerchanr">
                    <zs-icon icon="ten-cross" text="选择参与商家"></zs-icon>
                </zs-button>
                <div class="package-msg" v-else>
                    <p class="business-name">{{businessName}}</p>
                    <p class="choose-again" @click="showDialogMerchanr">重新选择</p>
                    <div class="package-box curr">
                        <div class="package-box-top">
                            <p class="package-box-top-left">{{choosePackageData.packageName}}<span>￥{{choosePackageData.salePrice}}</span>
                            </p>
                            <p class="package-box-top-right">有效期：{{choosePackageData.validTerm!=0 ?
                                choosePackageData.validTerm+'年' : '永久'}}</p>
                        </div>
                        <ul class="package-box-bottom" ref="max">
                            <li v-for="(item2,index2) in choosePackageData.packageAndItems" :key="index2">
                                <p class="package-box-bottom-item">{{item2.serviceItem.itemName}}<span class="r">{{item2.useTimes}}次</span>
                                </p>
                            </li>
                        </ul>
                        <ul>
                            <li class="package-box-more" @click.stop="handleChoosePackage"
                                v-if="choosePackageData.packageAndItems.length>3">
                                <zs-icon :rightIcon="choosePackageData.isShowMore ? 'arrow-top' : 'arrow-bottom'"
                                         :text="choosePackageData.isShowMore ? '收起' : '查看更多'" :size="8"></zs-icon>
                            </li>
                        </ul>
                    </div>

                </div>
            </li>
            <li class="form-li">
                <p class="h3"><em>*</em> 拼团价格</p>
                <zs-input
                        v-model.trim="activity.salePrice"
                        type="number"
                        placeholder="请输入拼团价格">
                </zs-input>
            </li>
            <li class="form-li">
                <p class="h3">补贴价格</p>
                <zs-input
                        v-model.trim="activity.subsidyPrice"
                        type="number"
                        placeholder="请输入补贴价格">
                </zs-input>
            </li>
            <li class="form-li">
                <p class="h3"><em>*</em> 拼团人数</p>
                <zs-input
                        v-model.trim="activity.clusterNum"
                        type="number"
                        placeholder="请输入拼团人数">
                </zs-input>
            </li>
            <li class="form-li">
                <p class="h3"><em>*</em> 拼团时限</p>
                <zs-input
                        v-model.trim="activity.clusterHour"
                        type="number"
                        placeholder="请输入拼团时间显示，单位小时">
                </zs-input>
                <span class="hour">小时</span>
            </li>
            <li class="form-li">
                <p class="h3">拼团说明</p>
                <zs-input
                        v-model.trim="activity.description"
                        type="textarea"
                        :maxlength="100"
                        placeholder="请输入拼团使用说明">
                </zs-input>
            </li>
            <li class="form-li">
                <p class="h3">商品详情</p>
                <div class="ue">
                    <editor :config="editorConfig" @change="handleEditor" :defaultContent="defaultContent"></editor>
                </div>
            </li>
            <li class="form-li form-post">
                <zs-button type="cancel" @click="turnBackActivity">取消</zs-button>
                <zs-button type="primary" @click="handleSaveActivity">保存</zs-button>
            </li>
        </ul>

        <!-- 选择商家 -->
        <zs-dialog
                class="merchant-dialog"
                v-model="merchantDialog"
                show-close>
            <p slot="title">选择商家</p>
            <div class="dialog-body">
                <div class="half border">
                    <zs-icon class="title" icon="merchant" :size="26" text="商家列表"></zs-icon>
                    <div class="search">
                        <zs-input class="search-inp" v-model.trim="keyWord" placeholder="请输入商家名关键词进行搜索"
                                  icon="circle-cross"></zs-input>
                        <zs-button class="search-btn" type="primary" @click="handleGoSearch">搜索</zs-button>
                    </div>
                    <div class="merchant-list">
                        <zs-table
                                :data="merchantDates"
                                :columns="itemColumns"
                                :context="context"
                                :no-data-colspan="1"
                                highlight-row
                                border></zs-table>
                        <span class="total-num">共 {{merchantTotal}} 条</span>
                        <zs-pagination
                                class="page-num"
                                :current="merchantDatesCondition.startPage"
                                :total="merchantTotal"
                                :page-size="merchantDatesCondition.pageSize"
                                @on-change="handlePageChange"
                                :showTotal="true"
                                simple
                        ></zs-pagination>
                    </div>
                </div>
                <div class="half">
                    <zs-icon class="title" icon="package-card" :size="26" text="商家套餐"></zs-icon>
                    <div class="package-scroll">
                        <zs-scrollbar>
                            <div class="package-box curr" :class="{'more-item' :  packageData[index].isShowMore  }"
                                 v-for="(item,index) in packageData" :key="index" @click="handleSelectPackage(index)">
                                <div class="package-box-top">
                                    <p class="package-box-top-left">{{item.packageName}}<span>￥{{item.salePrice}}</span>
                                    </p>
                                    <p class="package-box-top-right">有效期：{{item.validTerm!=0 ? item.validTerm+'年' :
                                        '永久'}}</p>
                                </div>
                                <div v-if="!packageData[index].isShowMore">
                                    <ul class="package-box-bottom">
                                        <li v-for="(item2,index2) in item.packageAndItems" :key="index2">
                                            <p class="package-box-bottom-item">{{item2.serviceItem.itemName}}<span
                                                    class="r">{{item2.useTimes}}次</span></p>
                                        </li>
                                    </ul>
                                    <ul>
                                        <li class="package-box-more" @click.stop="handlePackageShowMore(index)"
                                            v-if="item.packageAndItems.length>3">
                                            <zs-icon rightIcon="arrow-bottom"
                                                     :text="packageData[index].isShowMore ? '收起' : '查看更多'"
                                                     :size="8"></zs-icon>
                                        </li>
                                    </ul>
                                </div>
                                <div v-else>
                                    <ul class="more-package">
                                        <li v-for="(item2,index2) in item.packageAndItems" :key="index2">
                                            <p class="package-box-bottom-item">{{item2.serviceItem.itemName}}<span
                                                    class="r">{{item2.useTimes}}次</span></p>
                                        </li>
                                        <li class="package-box-more" @click.stop="handlePackageShowMore(index)"
                                            v-if="item.packageAndItems.length>3">
                                            <zs-icon rightIcon="arrow-bottom"
                                                     :text="packageData[index].isShowMore ? '收起' : '查看更多'"
                                                     :size="8"></zs-icon>
                                        </li>
                                    </ul>
                                </div>
                                <zs-icon class="package-box-select" icon="selected" v-show="packageCurr==index"
                                         :size="22"></zs-icon>
                            </div>
                        </zs-scrollbar>
                    </div>
                </div>
            </div>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="merchantDialog=false">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="handleChooseMerchant">确认
                </zs-button>
            </template>
        </zs-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
    import { isEmpty, DateUtils, turnToNextPage } from '../../../../../../../assets/js/utils';
    import editor from '../../../../../../../components_proj/ue/app.vue';
    import Upload from '../../../../../../../components_proj/upload/app.vue'
    export default{
        name: 'list',
        components: {
            Upload, editor
        },
        props: {},
        data(){
            return {
                activity: {
                    clusterName: '',
                    packClusterImgs: [], //[{"imgPath":"","imgSort":1},{"imgPath":"","imgSort":2}]
                    shareImgPath: '',
                    beginDay: '',
                    endDay: '',
                    salePrice: '',
                    subsidyPrice: '',
                    clusterNum: '',
                    clusterHour: '',
                    description: '',
                    packId: '',
                    businessId: '',
                    shareDescription: '',
                    attachedText: ''
                },
                activityPhoto: [], //临时存储活动图片
                sharePhote: [], //临时储存分享
                merchantDialog: false, //选择商家弹窗
                context: this,
                itemColumns: [
                    {
                        title: '商家名称',
                        render(row, col, index){
                            return `<a @click="selectPackageList(${row.id},${index})">${row.businessName}</a>`;
                        }
                    }
                ],
                businessName: '',
                businessIndex: '',
                itemPage: {
                    startPage: 1,
                    pageSize: 6,
                    total: 0
                },
                //商家数据
                merchantDatesCondition: {
                    startPage: 1,
                    pageSize: 6,
                    businessName: '',
                    orderBy: 'id desc'
                },
                keyWord: '',
                merchantDates: [],
                merchantTotal: 0,
                //套餐数据
                packageData: [],
                packageCurr: -1,
                packageMask: false,
                choosePackageData: [], //选中的套餐数据
                id: '',

                editorConfig: [
                    'bold', // 粗体
                    'italic', // 斜体
                    'underline', // 下划线
                    'strikeThrough', // 删除线
                    'foreColor', // 文字颜色
                    'backColor', // 背景颜色
                    'list', // 列表
                    'justify', // 对齐方式
                    'image', // 插入图片
                    'undo', // 撤销
                    'redo' // 重复
                ],
                defaultContent: '',
                strMerchant: '', //临时存储商家名称
                strMerchantId: '' //临时存储商家Id
            }
        },
        computed: {},
        watch: {

        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
            /**
             * 新增和修改
             */
            insertMessage(){
                this.activity.salePrice = parseFloat(this.activity.salePrice).toFixed(2);
                this.activity.subsidyPrice = this.activity.subsidyPrice ? parseFloat(this.activity.subsidyPrice).toFixed(2) : '';
                if (isEmpty(this.id)) {
                    this.$ajax(
                        this.$joggle.operator.activity.insertPackCluster,
                        this.activity,
                        true,
                        (data, loading) => {
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                })
                                turnToNextPage('/web/operator/home/activity/manage.html')
                            } else {
                                loading.close();
                                this.$message({
                                    type: 'error',
                                    message: data.msg,
                                    duration: 1200
                                })
                            }
                        }
                    )
                } else {
                    let newCondition = { ...this.activity };

                    newCondition.id = this.id;
                    this.$ajax(
                        this.$joggle.operator.activity.updatePackCluster,
                        newCondition,
                        true,
                        (data, loading) => {
                            loading.close();
                            if (data.code === 'ZS011000') {
                                this.$message({
                                    type: 'success',
                                    message: data.msg,
                                    duration: 1200
                                })
                                turnToNextPage('/web/operator/home/activity/manage.html')
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
            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
            /**
             * 删除活动图片
             * @param index
             */
            deletePhoto(index){
                this.$delete(this.activityPhoto, index);
                this.$delete(this.activity.packClusterImgs, index);
            },
            /**
             * 删除分享图片
             * @param index
             */
            deletePhotoShare(index){
                this.$delete(this.sharePhote, index);
                this.activity.shareImgPath = '';
            },
            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 商家列表信息
             * @param fn
             * @param loading
             */
            selectMerchantData(fn, loading){
                this.$ajax(
                    this.$joggle.operator.selectBusinessListPage,
                    this.merchantDatesCondition,
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.merchantDates = data.data.list;
                            this.merchantTotal = data.data.total;
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
             * 获取套餐卡数据
             * @param id
             * @param index
             */
            selectPackageList(id, index){
                this.businessIndex = index;
                this.packageCurr = -1;
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageList,
                    {
                        starPage: 1,
                        pageSize: 100,
                        businessId: id
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.packageData = data.data.list;
                            for (let x in this.packageData) {
                                this.$set(this.packageData[x], 'isShowMore', false);
                            }
                            this.strMerchant = this.merchantDates[this.businessIndex].businessName;
                            this.strMerchantId = this.merchantDates[this.businessIndex].id;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                )
            },
            /**
             * 选择商家
             */
            selectMessageMsg(){
                this.$ajax(
                    this.$joggle.operator.activity.selectPackClusterInfoById,
                    {
                        id: this.id
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.activity.clusterName = data.data.clusterName;
                            this.activity.packClusterImgs = data.data.packClusterImgs;
                            this.activity.shareImgPath = data.data.shareImgPath;
                            this.activity.beginDay = data.data.beginDay;
                            this.activity.endDay = data.data.endDay;
                            this.activity.salePrice = data.data.salePrice;
                            this.activity.subsidyPrice = data.data.subsidyPrice;
                            this.activity.clusterNum = data.data.clusterNum;
                            this.activity.clusterHour = data.data.clusterHour;
                            this.activity.description = data.data.description;
                            this.activity.packId = data.data.packId;
                            this.activity.businessId = data.data.businessId;
                            this.activity.shareDescription = data.data.shareDescription;
                            this.defaultContent = decodeURIComponent(data.data.attachedText);
                            this.activity.attachedText = data.data.attachedText;

                            this.activity.packClusterImgs.forEach((item) => {
                                this.activityPhoto.push({ showImg: item.imgPathUrl });
                            });
                            if (this.activity.shareImgPath) {
                                this.sharePhote.push({ showImg: data.data.shareImgPathUrl });
                            }
                            this.businessName = data.data.businessName;
                            this.choosePackageData = data.data.businessPackage
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            })
                        }
                    }
                )
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 保存验证表单
             */
            handleSaveActivity(){
                let errorMsg = {
                    clusterName: '请输入活动名称',
                    packClusterImgs: '请选择活动图片',
                    shareDescription: '请输入分享描述',
                    beginDay: '请选择活动时间',
                    endDay: '请选择活动时间',
                    packId: '请选择商家套餐',
                    salePrice: '请输入拼团价格',
                    clusterNum: '请输入拼团人数',
                    clusterHour: '请输入拼团时限'
                };
                let begin = DateUtils.dateToRegular(this.activity.beginDay).getTime();
                let end = DateUtils.dateToRegular(this.activity.endDay).getTime();
                let now = new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate() + ' 00:00:00';
                let nowData = DateUtils.dateToRegular(now).getTime();
                let _num = /^\+?[1-9][0-9]*$/;
                let msgError = '';

                for (let x in errorMsg) {
                    let data = this.activity[x];

                    if (isEmpty(data)) {
                        this.$message({
                            type: 'error',
                            message: errorMsg[x]
                        });
                        return false;
                    }
                }
                if (begin >= end) {
                    msgError = '活动开始时间不能大于结束时间'
                } else if (begin < nowData) {
                    msgError = '活动开始时间不能小于当前时间'
                } else if (parseFloat(this.activity.salePrice) < 0) {
                    msgError = '请输入正确的拼团价格'
                } else if (parseFloat(this.activity.subsidyPrice) < 0) {
                    msgError = '请输入正确的补贴价格'
                } else if (!_num.test(this.activity.clusterNum)) {
                    msgError = '请输入正确的拼团人数'
                } else if (parseFloat(this.activity.clusterNum) <= 1) {
                    msgError = '拼团人数需大于1'
                } else if (!_num.test(this.activity.clusterHour)) {
                    msgError = '请输入正确的活动时限'
                }

                if (msgError) {
                    this.$message({
                        type: 'error',
                        message: msgError
                    });
                    return false;
                }
                this.insertMessage();
            },
            /**
             * 活动图片
             * @param photoDatas
             */
            handlePhotos(photoDatas){
                this.$ajax(
                    this.$joggle.mzscp.uploadImgForBase64,
                    {
                        linkType: 'packClusterImg',
                        img: photoDatas
                    },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const temp = [...this.activityPhoto];

                            temp.push({ showImg: data.data.imgSrc });
                            this.activityPhoto = temp;
                            this.activity.packClusterImgs.push({
                                imgPath: data.data.imgLink,
                                imgSort: this.activity.packClusterImgs.length + 1
                            });
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            /**
             * 分享图片
             * @param photoDatas
             */
            handlePhotosShare(photoDatas){
                this.$ajax(
                    this.$joggle.mzscp.uploadImgForBase64,
                    {
                        linkType: 'packClusterImg',
                        img: photoDatas
                    },
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const temp = [...this.sharePhote];

                            temp.push({ showImg: data.data.imgSrc });
                            this.sharePhote = temp;
                            this.activity.shareImgPath = data.data.imgLink;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            /**
             * 开始时间
             * @param time
             */
            handleSetStartTime(time){
                this.activity.beginDay = time ? time + ' 00:00:00' : '';
            },
            /**
             * 结束时间
             * @param time
             */
            handleSetEndTime(time){
                this.activity.endDay = time ? time + ' 23:59:59' : '';
            },
            /**
             * 购买套餐选中
             * @param index
             */
            handleSelectPackage(index){
                this.packageCurr = this.packageCurr != index ? index : -1;
                this.activity.businessId = this.strMerchantId;
                this.businessName = this.strMerchant;
            },
            /**
             * 购买套餐查看更多
             * @param index
             */
            handlePackageShowMore(index){
                this.packageData[index].isShowMore = !this.packageData[index].isShowMore;
            },
            /**
             * 已选中套餐查看更多
             */
            handleChoosePackage(){
                this.choosePackageData.isShowMore = !this.choosePackageData.isShowMore;
                this.$refs.max.style.maxHeight = this.choosePackageData.isShowMore ? '100%' : '96px'
            },
            /**
             * 商家列表页面切换
             * @param currPage
             */
            handlePageChange(currPage){
                this.merchantDatesCondition.startPage = currPage;
                const loading = this.$loading();

                this.selectMerchantData(() => {
                    loading.close()
                }, loading);
            },
            /**
             * 商家搜索
             */
            handleGoSearch(){
                this.merchantDatesCondition.startPage = 1;
                this.merchantDatesCondition.businessName = this.keyWord;
                const loading = this.$loading();

                this.selectMerchantData(() => {
                    loading.close()
                }, loading);
            },
            /**
             * 取消保存
             */
            turnBackActivity(){
                turnToNextPage('/web/operator/home/activity/manage.html')
            },
            /**
             * 点击选择商机按钮
             */
            showDialogMerchanr(){
                this.merchantDialog = true;
            },
            /**
             * 点击选择商机按钮
             */
            handleChooseMerchant(){
                if (this.packageCurr != -1) {
                    this.activity.packId = this.packageData[this.packageCurr].id;
                    /*this.activity.businessId = this.merchantDates[this.businessIndex].id;*/
                   /* this.businessName = this.merchantDates[this.businessIndex].businessName;*/
                    this.choosePackageData = this.packageData[this.packageCurr];
                    this.choosePackageData.isShowMore = false;
                } else {
                    this.activity.packId = '';
                    this.choosePackageData = [];
                    this.activity.businessId = '';
                    this.businessName = '';
                }
                /*this.packageCurr = -1;*/
                this.merchantDialog = false;
            },
            /**
             * 文本框的内容
             * @param html
             */
            handleEditor(html){
                this.activity.attachedText = encodeURIComponent(html);
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.selectMerchantData(false, false);

            this.id = window.location.search ? window.location.search.split('=')[1] : '';
            if (!isEmpty(this.id)) {
                this.selectMessageMsg();

            }
        },
        mounted(){

        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>