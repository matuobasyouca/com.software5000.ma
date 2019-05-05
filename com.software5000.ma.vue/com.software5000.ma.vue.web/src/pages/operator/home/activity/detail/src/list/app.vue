<template>
    <div class="activity-detail">
        <div class="body-top">
            <div class="activity-image">
                <div class="show-img" :style="{backgroundImage:`url('${imgChoose}')`}"></div>
                <ul class="img-list">
                    <li @click="handleChooseImg(index)" v-for="(item,index) in activityData.packClusterImgs"
                        :key="index"
                        :class="imgIndex===index ? 'choose-img' : ''">
                        <div class="img-li" :style="{backgroundImage:`url('${item.imgPathUrl}')`}"></div>
                    </li>
                </ul>
            </div>
            <div class="body-msg">
                <div class="msg-title">
                    <div class="title-width">
                          <span class="state"
                                :class="activityData.state == 2 ? 'state2' : ''">{{activityData.stateDesc}}</span>
                        <span class="title">{{activityData.clusterName}}</span>
                    </div>
                    <span class="time"><zs-icon icon="time" :size="14"
                                                :text="`活动时间： ${activityData.beginDay ? activityData.beginDay.slice(0,10) : ''} ~ ${activityData.endDay ? activityData.endDay.slice(0,10): ''}`"></zs-icon></span>
                </div>
                <ul class="msg-detail">
                    <li><span>拼团价格：</span><em>￥{{activityData.salePrice}}</em></li>
                    <li><span>补贴价格：</span>￥{{activityData.subsidyPrice ? activityData.subsidyPrice : 0}}</li>
                    <li><span>成团人数：</span>{{activityData.clusterNum}}</li>
                    <li><span>拼团时限：</span>{{activityData.clusterHour}}h</li>
                    <li><span>已成团数：</span>{{activityData.successNum}}</li>
                    <li><span>拼团说明：</span>{{activityData.description}}</li>
                    <li>
                        <span>分享图片:</span>
                        <div v-if="activityData.shareImgPathUrl" class="share-img"
                             :style="{backgroundImage:`url('${activityData.shareImgPathUrl}')`}"></div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="body-bottom">
            <div class="activity-merchant">
                <zs-icon icon="merchant" :size="26" text="活动商家套餐："></zs-icon>
                <span class="merchant-title">{{activityData.businessName}}</span>
            </div>
            <div class="package-box curr">
                <div class="package-box-top">
                    <p class="package-box-top-left">{{packageData.packageName}}<span>￥{{packageData.salePrice}}</span>
                    </p>
                    <p class="package-box-top-right">有效期：{{packageData.validTerm!=0 ? packageData.validTerm+'年' :
                        '永久'}}</p>
                </div>
                <div>
                    <ul class="package-box-bottom" ref="max">
                        <li v-for="(item2,index2) in packageData.packageAndItems" :key="index2">
                            <p class="package-box-bottom-item">{{item2.serviceItem.itemName}}<span class="r">{{item2.useTimes}}次</span>
                            </p>
                        </li>
                    </ul>
                    <ul>
                        <li class="package-box-more" @click.stop="handleShowMore" v-if="itemLong > 3">
                            <zs-icon :rightIcon="packageData.isShowMore ? 'arrow-top' : 'arrow-bottom'"
                                     :text="packageData.isShowMore ? '收起' : '查看更多'" :size="8"></zs-icon>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { isEmpty } from '../../../../../../../assets/js/utils';
    export default{
        name: 'list',
        components: {},
        props: {},
        data(){
            return {
                id: 0,
                activityData: [],
                imgIndex: 0,
                imgChoose: '',
                packageData: [],
                itemLong: 0
            };
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
             * 获取活动信息
             */
            selectActiveData(){
                this.$ajax(
                    this.$joggle.operator.activity.selectPackClusterInfoById,
                    {
                        id: this.id
                    },
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.activityData = data.data;
                            this.imgChoose = this.activityData.packClusterImgs[this.imgIndex].imgPathUrl;
                            this.packageData = data.data.businessPackage;
                            this.$set(this.packageData, 'isShowMore', false);
                            this.itemLong = this.packageData.packageAndItems.length;
                        } else {
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            },
            /* ----------------------------------------------------------- select (查) end ----------------------------------------------------------------*/

            /* ----------------------------------------------------------- handle (操) start ----------------------------------------------------------------*/
            /**
             * 购买套餐查看更多
             */
            handleShowMore(){
                this.packageData.isShowMore = !this.packageData.isShowMore;
                this.$refs.max.style.maxHeight = this.packageData.isShowMore ? '100%' : '96px';
            },
            /**
             * 图片选择处理
             */
            handleChooseImg(index){
                this.imgChoose = this.activityData.packClusterImgs[index].imgPathUrl;
                this.imgIndex = index;
            }
            /* ----------------------------------------------------------- handle (操) end ----------------------------------------------------------------*/
        },
        created(){
            this.id = window.location.search.split('=')[1];
            this.selectActiveData();
        },
        mounted(){

        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>