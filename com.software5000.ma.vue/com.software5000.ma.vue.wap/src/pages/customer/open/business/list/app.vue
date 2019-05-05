<template>
    <div class="business-list-page" v-show="!isLoading">
        <ma-head
                :home="false"
                right-icon="search1"
                right-icon-size="18"
                @right-icon-click="searchVisible=true">商家列表
        </ma-head>
        <div class="business-filter" :class="[{'show':searchVisible}]">
            <i class="arrow" @click="handleSearchHide"></i>
            <zs-input icon="circle-cross" v-model="businessName" placeholder="请输入商家名称关键词搜索">
                <i class="search-label" slot="prepend"></i>
            </zs-input>
            <span class="filter-control" @click="handleSearch">搜索</span>
        </div>
        <ul class="business">
            <li
                    class="list"
                    v-for="list in businessList"
                    :key="list.id"
                    @click="turnToBusinessDetail(list.id)">
                <p class="business-pic" :style="setBusinessPic(list.imageShowPath)"></p>
                <p
                        class="business-name"
                        :class="[{'has-card' : list.packageCount > 0}]">{{ list.businessName }}</p>
                <p class="business-location">{{ `${list.businessAreaCodeDes}${list.businessAddress}` }}</p>
                <p class="business-tel">
                    <zs-icon
                            class="distance"
                            icon="location2"
                            icon-dis="4"
                            size="10"
                            :text="setDistance(list.distance)"></zs-icon>
                    <zs-icon
                            v-if="list.memberBalance"
                            class="balance"
                            icon="balance"
                            icon-dis="4"
                            size="10"
                            :text="list.memberBalance"></zs-icon>
                </p>
            </li>
            <li
                    v-if="businessList.length > 0"
                    class="show-more"
                    :class="[{'no-more' : !hasNextPage}]"
                    @click="handleShowMore">{{ hasNextPage ? '查看更多' : '已显示全部' }}
            </li>
            <li v-else class="list-empty">
                <p>暂无商家</p>
            </li>
        </ul>
        <div id="map"></div>
    </div>
</template>
<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import { isEmpty, getDataFromParam, turnToNextPage, Hash, Cookie } from '../../../../../assets/js/utils';
    import { turnToHostPage } from  '../../../../../assets/js/turnToHostPage';
    import Base64 from '../../../../../assets/js/base64.min';
    export default {
        components: {
            maHead
        },
        data(){
            return {
                isLoading: true,
                map: null,
                openId: '',
                businessName: '',
                businessList: [],
                startPage: 1,
                pageSize: 10,
                hasNextPage: false,
                searchVisible: false
            };
        },
        methods: {
            //搜索商家
            handleSearch(){
                this.startPage = 1;
                const loading = this.$loading();
                this.getBusinessList(() => {
                    loading.close();
                }, loading);
            },
            //关闭搜索框，重置搜索
            handleSearchHide(){
                this.searchVisible = false;
                if (!isEmpty(this.businessName)) {
                    this.businessName = '';
                    this.handleSearch();
                }
            },
            //查看更多
            handleShowMore(){
                if (this.hasNextPage) {
                    const loading = this.$loading();
                    this.getBusinessList(() => {
                        loading.close();
                    }, loading, true);
                }
            },
            //获取商家列表
            getBusinessList(fn, loading, isShowMore){
                if (isShowMore) {
                    this.startPage++;
                } else {
                    this.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.customer.business.selectBusinessByPage,
                    {
                        wxOpenId: this.openId,
                        lng: this.lng,
                        lat: this.lat,
                        businessName: this.businessName,
                        startPage: this.startPage,
                        pageSize: this.pageSize,
                        enable: 1,
                        lineState: 1
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.hasNextPage = data.data.hasNextPage;
                            this.businessList = isShowMore ? this.businessList.concat(data.data.list) : data.data.list;
                            fn && fn();
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg
                            });
                        }
                    }
                );
            },
            //设置商家图片展示
            setBusinessPic(image){
                const style = {};
                style.backgroundImage = `url(${image})`;
                return style;
            },
            //设置距离
            setDistance(d){
                return d > 1000 ? `${(d / 1000).toFixed(2)}km` : `${d}m`;
            },
            //查看商家详情
            turnToBusinessDetail(id){
                turnToNextPage('/wap/customer/open/business/detail.html', { i: Base64.encode(this.openId), id: id });
            }
        },
        mounted(){
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            if (isEmpty(this.openId)) {
                turnToHostPage('/open/user_center.html', 'emkt');
                return;
            }
            //地图对象
            const loading = this.$loading();
            const point = Cookie.get('point');
            const _this = this;
            this.map = new BMap.Map('#map');
            const geolocation = new BMap.Geolocation();
            //定位当前位置
            geolocation.getCurrentPosition(function (r) {
                if (this.getStatus() == BMAP_STATUS_SUCCESS) {
                    _this.lng = r.point.lng;
                    _this.lat = r.point.lat;
                    Cookie.set('point', `${_this.lng},${_this.lat}`, 5);
                    _this.getBusinessList(() => {
                        loading.close();
                        _this.isLoading = false;
                    }, loading);
                } else {
                    _this.$message({
                        type: 'error',
                        message: '无法获取当前位置'
                    });
                }
            }, { enableHighAccuracy: true });
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>