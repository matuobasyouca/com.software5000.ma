<template>
    <div
            class="business-detail-page"
            :class="[{'has-head' : sTop > 0,'is-tab-fixed' : tabFix}]"
            v-show="!isLoading"
            @scroll="handleScroll">
        <transition name="head-fade">
            <ma-head :home="false" v-show="sTop > 0">{{ businessName }}</ma-head>
        </transition>
        <div class="detail-head" :style="businessPic" v-show="!tabFix">
            <p class="arrow" @click="handleGoBack" v-if="sTop === 0"><i></i></p>
            <div class="business-info">
                <div class="info-p1">
                    <p>{{ businessName }}
                    </p>
                </div>
                <p class="info-p4"><span v-if="packageCount > 0">有卡</span><span>余额:{{ memberBalance || 0 }}</span></p>
                <p class="info-p2"><span>地址：</span>{{ businessAddress }}</p>
                <ul class="info-p3">
                    <li @click="handleDial(businessTel)">{{ businessTel || '----' }}</li>
                    <li @click="handleToMap">{{ _distance || '--' }}(查看地图)</li>
                </ul>
            </div>
        </div>
        <p class="detail-tab" :class="[{'tab-fix' : tabFix}]">
            <span class="tab1" :class="[{'curr' : currTab == 1}]" @click="handleTabChange(1)">服务项目</span>
            <span class="tab2" :class="[{'curr' : currTab == 2}]" @click="handleTabChange(2)">服务套餐</span>
        </p>
        <div class="detail-item-select" v-show="currTab == 1" :style="{minHeight : '300px'}">
            <ul class="menu" :class="[{'menu-fix' : tabFix}]">
                <li
                        v-for="type in itemTypes"
                        :key="type[0]"
                        :class="[{'curr' : currType === type[0]}]"
                        @click="handleSelectItemType(type[0])">{{ type[1] }}
                </li>
            </ul>
            <ul class="list" v-if="businessItems.length > 0">
                <li
                        v-for="item in businessItems"
                        :key="item.id">{{ item.itemName }}
                    <span>￥{{ item.salePrice ? item.salePrice.toFixed(2) : 0 }}</span></li>
            </ul>
            <div class="list business-item-empty" v-else>
                暂无相关项目
            </div>
        </div>
        <div class="detail-package" v-show="currTab == 2">
            <ul class="package-list" v-if="packages.length > 0">
                <li class="package" v-for="package in packages" :key="package.id">
                    <div class="package-info">
                        <p class="p1">
                            {{ package.packageName }}
                        </p>
                        <p class="p2">
                            ￥{{ package.salePrice }}
                            <span class="original-price">价值：{{ package.originalPrice }}</span>
                            <zs-icon
                                    class="validate-time"
                                    size="12"
                                    icon="time4"
                                    icon-dis="4"
                                    :text="`有效期：${package.validTerm == 0 ? '永久' : package.validTerm + '年'}`"></zs-icon>
                        </p>
                    </div>
                    <div class="item-info">
                        <p
                                v-for="(item,index) in package.packageAndItems"
                                v-if="index < 3 || package.showAll"
                                :key="item.id">{{ item.serviceItem.itemName }}<span>{{ item.useTimes }}次</span>
                        </p>
                    </div>
                    <p
                            class="show-more-item"
                            v-show="!package.showAll && package.packageAndItems.length>3"
                            @click="package.showAll = true"><span>查看更多</span></p>
                </li>
                <li
                        v-if="packages.length > 0"
                        class="show-more"
                        :class="[{'no-more' : !packagePage.hasNextPage}]"
                        @click="handleShowMore">{{ packagePage.hasNextPage ? '查看更多' : '已显示全部' }}
                </li>
            </ul>
            <p class="package-list package-empty" v-else>暂无服务套餐</p>
        </div>
        <div id="map"></div>
    </div>
</template>
<script>
    import { turnToHostPage } from '../../../../../assets/js/turnToHostPage';
    import { isEmpty, getDataFromParam, turnToNextPage } from '../../../../../assets/js/utils';
    import { on, off } from '../../../../../components/src/utils/dom';
    import Base64 from '../../../../../assets/js/base64.min';
    import { debounce } from 'throttle-debounce';
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    export default {
        components: {
            maHead
        },
        data(){
            return {
                isLoading: true,
                currPoint: '',//当前地理位置坐标
                currTab: 1,//当前选中的tab索引
                tabFix: false,//控制tab固定
                sTop: 0,//滚动距离
                winH: 0,//浏览器高度
                openId: '',
                lng: '',
                lat: '',
                businessId: '',
                //商家信息
                businessName: '',
                businessAddress: '',
                packageCount: 0,
                imageShowPath: '',
                businessTel: '',
                distance: 0,
                targetPoint: '',
                businessDescript: '',
                //商家项目
                currType: '',
                itemTypes: [],
                businessItems: [],
                memberBalance: 0,
                //商家套餐
                packages: [],
                packagePage: {
                    startPage: 1,
                    pageSize: 10,
                    hasNextPage: false
                }
            };
        },
        computed: {
            _distance(){
                return this.distance > 1000 ? `${(this.distance / 1000).toFixed(2)}km` : `${this.distance}m`;
            },
            businessPic(){
                const style = {};
                style.backgroundImage = `url(${this.imageShowPath})`;
                return style;
            }
//            itemSelectStyle(){
//                const style = {};
//                style.minHeight = `${this.winH - 270}px`;
//                return style;
//            }
        },
        methods: {
            //滚动
            handleScroll(){
                debounce(30, () => {
                    this.sTop = document.body.scrollTop;
                    this.tabFix = this.sTop >= 280;
                })();

            },
            //tab切换
            handleTabChange(i){
                this.currTab = i;
                document.body.scrollTop = this.sTop > 280 ? 280 : this.sTop;
            },
            //获取商家信息
            selectBusiness(fn, loading){
                this.$ajax(
                    this.$joggle.customer.business.selectBusiness,
                    {
                        lng: this.lng,
                        lat: this.lat,
                        wxOpenId: this.openId,
                        businessId: this.businessId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.businessName = data.data.businessName;
                            this.businessAddress = `${data.data.businessAreaCodeDes}${data.data.businessAddress}`;
                            this.packageCount = data.data.packageCount;
                            this.imageShowPath = data.data.imageShowPath;
                            this.businessTel = data.data.businessTel;
                            this.distance = data.data.distance;
                            this.targetPoint = data.data.businessCoordinate;
                            this.businessDescript = data.data.businessDetail;
                            this.memberBalance = data.data.memberBalance;
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
            //获取服务项类别
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
                                message: data.msg
                            });
                        }
                        fn && fn();
                    }
                );
            },
            //选择商家项目类别
            handleSelectItemType(type){
                this.currType = type;
                const loading = this.$loading();
                this.selectBusinessItem(() => {
                    loading.close();
                }, loading);
            },
            //获取商家项目
            selectBusinessItem(fn, loading){
                this.$ajax(
                    this.$joggle.customer.business.selectBusinessItem,
                    {
                        itemType: this.currType,
                        businessId: this.businessId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            this.businessItems = data.data;
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
            //获取商家套餐
            businessPackage(fn, loading, isShowMore){
                if (isShowMore) {
                    this.packagePage.startPage++;
                } else {
                    this.packagePage.startPage = 1;
                }
                this.$ajax(
                    this.$joggle.customer.business.selectBusinessPackage,
                    {
                        startPage: this.packagePage.startPage,
                        pageSize: this.packagePage.pageSize,
                        businessId: this.businessId
                    },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            const tempData = [];
                            data.data.list.forEach((d) => {
                                d.showAll = false;
                                tempData.push(d);
                            });
                            this.packagePage.hasNextPage = data.data.hasNextPage;
                            this.packages = isShowMore ? this.packages.concat(tempData) : tempData;
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
            //查看更多商家套餐
            handleShowMore(){
                if (this.packagePage.hasNextPage) {
                    const loading = this.$loading();
                    this.businessPackage(() => {
                        loading.close();
                    }, loading, true);
                }
            },
            //返回
            handleGoBack(){
                window.history.go(-1);
            },
            //拨号
            handleDial(mobile){
                if (!isEmpty(mobile)) {
                    window.location.href = `tel:${mobile}`;
                }
            },
            //查看地图
            handleToMap(){
                const data = {
                    startPoint: encodeURIComponent(this.currPoint),
                    endPoint: encodeURIComponent(this.targetPoint),
                    storeName: encodeURIComponent(this.businessName),
                    storeAddress: encodeURIComponent(this.businessAddress),
                    storeImgPath: this.imageShowPath,
                    storeTel: this.businessTel,
                    storeDescription: encodeURIComponent(this.businessDescript)
                };
                turnToNextPage('/wap/customer/open/business/map.html', data);
            }
        },
        mounted(){
            this.openId = getDataFromParam('i') ? Base64.decode(getDataFromParam('i')) : '';
            this.businessId = getDataFromParam('id') || '';
            this.winH = document.body.clientHeight;
            if (isEmpty(this.openId)) {
                turnToHostPage('/open/user_center.html', 'emkt');
                return;
            } else if (isEmpty(this.businessId)) {
                this.$message({
                    type: 'error',
                    message: '数据不存在'
                });
                return;
            }
            //地图对象
            const loading = this.$loading();
            const _this = this;
            this.map = new BMap.Map('#map');
            const geolocation = new BMap.Geolocation();
            //定位当前位置
            geolocation.getCurrentPosition(function (r) {
                if (this.getStatus() == BMAP_STATUS_SUCCESS) {
                    _this.currPoint = `${r.point.lng},${r.point.lat}`;
                    _this.lng = r.point.lng;
                    _this.lat = r.point.lat;
                    const f0 = new Promise((resolve) => {
                        _this.selectBusiness(() => {
                            resolve();
                        }, loading);
                    });
                    const f1 = new Promise((resolve) => {
                        _this.selectItemType(() => {
                            resolve();
                        }, loading);
                    });
                    const f2 = new Promise((resolve) => {
                        _this.selectBusinessItem(() => {
                            resolve();
                        }, loading);
                    });
                    const f3 = new Promise((resolve) => {
                        _this.businessPackage(() => {
                            resolve();
                        }, loading);
                    });
                    Promise.all([f0, f1, f2, f3]).then(() => {
                        loading.close();
                        _this.isLoading = false;
                    });

                } else {
                    _this.$message({
                        type: 'error',
                        message: '无法获取当前位置'
                    });
                }
            }, { enableHighAccuracy: true });
            //监听滚动事件
            on(window, 'scroll', this.handleScroll);
        },
        beforeDestroy(){
            //解除滚动事件监听
            off(window, 'scroll', this.handleScroll);
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>