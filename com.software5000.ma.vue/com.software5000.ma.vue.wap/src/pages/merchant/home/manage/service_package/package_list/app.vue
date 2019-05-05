<template>
    <div class="package-page" v-show="!isLoading">
        <ma-head @home-click="handleGoHome">服务套餐<span class="new-package" @click="handleTurnToPackagePage">新建套餐</span>
        </ma-head>
        <div class="package-body">
            <ul v-if="datas.length > 0" class="package-ul">
                <li v-for="(item,index) in datas" :key="index" @click="handlePackageDetail(item)">
                    <div class="li-top">
                        <p class="li-title">{{item.packageName}}</p>
                        <div class="li-base">
                            <div class="li-money">金额<em>￥{{parseFloat(item.originalPrice).toFixed(2)}}</em></div>
                            <div class="li-indate">
                                <zs-icon icon="time" size="13"></zs-icon>
                                有效期：{{handleChangeYear(item.validTerm)}}
                            </div>
                        </div>
                    </div>
                    <div class="li-bottom">
                        <div class="li-list">
                            <div class="li-item" v-for="(it,_it) in item.packageAndItems" :key="_it">
                                <p>{{it.serviceItem.itemName}}</p>
                                <span>x{{it.useTimes}}</span>
                            </div>
                        </div>
                        <p class="li-item-more" v-if="item.packageAndItems.length>3">···</p>
                        <div class="li-make">
                            <zs-icon icon="write" size="12" text="编辑" @click.stop="handleSetAgain(item)"></zs-icon>
                            <zs-icon icon="delete3" size="12" text="删除" @click.stop="handleDelMessage(index)"></zs-icon>
                            <span class="switch-h3" v-if="item._top">启用</span>
                            <span class="switch-h4" v-else>停用</span>
                            <zs-switch class="switch-btn" v-model="item._top" @change="handleSwitchBtn(index)"></zs-switch>
                        </div>
                    </div>
                </li>
                <div
                        class="show_all_css"
                        :class="[{'show_more_css' : hasNextPage}]"
                        @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
                </div>
            </ul>
            <div v-else>
                <ul class="data-empty">
                    <li>暂无服务套餐信息</li>
                </ul>
            </div>
        </div>

        <zs-dialog
                class="delete-dialog"
                v-model="showDeleteDialog"
                show-close>
            <div class="dialog-body">
                <div class="money-img"></div>
                <p class="h3">是否删除该套餐？</p>
            </div>
            <template slot="footer">
                <zs-button
                        type="cancel"
                        @click="showDeleteDialog=false">取消
                </zs-button>
                <zs-button
                        type="primary"
                        @click="deletePackage">确定
                </zs-button>
            </template>
        </zs-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
    import { turnToNextPage, isEmpty } from '../../../../../../assets/js/utils';
    import maHead from '../../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead
        },
        data(){
            return {
                isLoading: false,
                datas: [],
                hasNextPage: false,
                revamp: false,
                starPage: 1,
                pageSize: 10,
                packageState: 0,
                id: 0,
                showDeleteDialog: false,
                indexNum: 0
            }
        },
        methods: {
            /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
            /**
             * 删除套餐
             */
            deletePackage(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.deleteBusinessPackage,
                    {
                        businessPackageId: this.datas[this.indexNum].id
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                            this.showDeleteDialog = false;
                            this.selectDatas();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
            /**
             * 改变该套餐的状态
             */
            updateState(){
                this.$ajax(
                    this.$joggle.merchant.businessPackage.updateBusinessPackageState,
                    {
                        id: this.id,
                        state: this.packageState
                    },
                    true,
                    (data, loading) => {
                        if (data.code === 'ZS011000') {
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                            this.selectDatas();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            })
                        }
                    }
                )
            },
            /* ----------------------------------------------------------- update (改) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
            /**
             * 获取服务套餐数据信息
             * @param isShowMore
             * @param fn
             */
            selectDatas(isShowMore, fn){
                if (isShowMore) {
                    this.starPage++;
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.merchant.businessPackage.selectBusinessPackageByPage,
                    {
                        starPage: this.starPage,
                        pageSize: this.pageSize
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            document.body.scrollTop = 0
                            this.hasNextPage = data.data.hasNextPage;
                            const temp = isShowMore ? this.datas.concat(data.data.list) : data.data.list;

                            temp.forEach((item) => {
                                this.$set(item, '_top', item.packageState == 1);
                            });
                            this.datas = temp;
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
             * 点击删除按钮
             * @param index
             */
            handleDelMessage(index){
                this.showDeleteDialog = true;
                this.indexNum = index;
            },
            /**
             * 点击编辑
             * @param item
             */
            handleSetAgain(item){
                this.id = item.id;
                turnToNextPage('/wap/merchant/home/manage/service_package/new_package.html', { id: this.id });
            },
            /**
             * 点击新建套餐
             */
            handleTurnToPackagePage(){
                turnToNextPage('/wap/merchant/home/manage/service_package/new_package.html');
            },
            /**
             * 进入套餐详情
             * @param item
             */
            handlePackageDetail(item){
                turnToNextPage('/wap/merchant/home/manage/service_package/package_detail.html', { id: item.id });
            },
            /**
             * 启动开关
             * @param index
             */
            handleSwitchBtn(index){
                let preson = this.datas[index];

                this.packageState = preson.packageState == 1 ? 2 : 1;
                this.id = preson.id;
                this.updateState();
            },
            /**
             * 有效期的转换
             * @param n
             */
            handleChangeYear(n){
                if (isEmpty(n))return '';
                return n == 0 ? '永久' : `${n}年`;
            },
            /**
             * 点击查看更多
             */
            handleShowMore(){
                if (this.hasNextPage) {
                    this.selectDatas(this.hasNextPage);
                }
            },
            /**
             * 返回首页
             */
            handleGoHome(){turnToNextPage('/wap/merchant/home/manage/index.html');}
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.selectDatas(false);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
