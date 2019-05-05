<template>
    <div class="map" v-show="!isLoading">
        <ma-head :home="false">地图</ma-head>
        <div id="map"></div>
    </div>
</template>
<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import {getDataFromParam} from '../../../../../assets/js/utils';
    export default {
        components: {
            maHead
        },
        data(){
            return {
                //操作数据
                isLoading: false,
                map: null,
                geo: null,
                currPoint: null,
                currMarker: null,
                //保存数据
                startPoint: '',
                endPoint: '',
                storeName: '',
                storeAddress: '',
                storeImgPath: '',
                storeTel: '',
                storeDescription: '',
            }
        },
        methods: {
            /**
             * 获取当前位置
             */
            handleGetPosition(){
                let _this = this;
                this.geo.getCurrentPosition(function (r) {
                    if (this.getStatus() == BMAP_STATUS_SUCCESS) {
                        if (_this.currPoint == null) {
                            _this.map.panTo(r.point);
                            if (_this.startPoint != "" && _this.startPoint != null) {
                                let link_point = _this.startPoint.split(",");
                                _this.map.centerAndZoom(new BMap.Point(link_point[0], link_point[1]), 14);
                            } else {
                                _this.map.centerAndZoom(r.point, 14);
                            }
                        }
                        if (_this.startPoint != "" && _this.startPoint != null) {
                            let link_point = _this.startPoint.split(",");
                            _this.currPoint = new BMap.Point(link_point[0], link_point[1]);
                        } else {
                            _this.currPoint = new BMap.Point(r.point.lng, r.point.lat);
                        }
                        let point=_this.startPoint ? _this.startPoint : r.point.lng+','+r.point.lat;

                        _this.myCarLoad(point, _this.endPoint);
                        _this.createCurrMarker();
                    }
                    else {
                        this.$message({
                            type: 'error',
                            message: 'failed' + this.getStatus(),
                            duration: 1200
                        });
                    }
                }, {enableHighAccuracy: true});
            },
            /**
             * 创建当前标注
             */
            createCurrMarker(){
                if (this.currMarker != null) {
                    this.map.removeOverlay(this.currMarker);
                }
                this.currMarker = new BMap.Marker(
                    this.currPoint,
                    {
                        icon: new BMap.Icon(require("./assets/marker_person.png"), new BMap.Size(30, 40))
                    }
                );
                this.map.addOverlay(this.currMarker);
            },
            /**
             * 生成两点间路线
             * @param point1
             * @param point2
             */
            myCarLoad(point1, point2) {
                this.map.clearOverlays();//清除所有导航路线
                let p_start = point1.split(",");
                let p_end = point2.split(",");
                let start = new BMap.Point(p_start[0], p_start[1]);//起点
                let end = new BMap.Point(p_end[0], p_end[1]);//终点
                let driving = new BMap.DrivingRoute(this.map, {renderOptions: {map: this.map, autoViewport: true}});
                driving.search(start, end);
            }
        },
        mounted(){
            this.startPoint = getDataFromParam('startPoint');
            this.endPoint = getDataFromParam('endPoint');
            this.storeName = getDataFromParam('storeName');
            this.storeAddress = getDataFromParam('storeAddress');
            this.storeImgPath = getDataFromParam('storeImgPath');
            this.storeTel = getDataFromParam('storeTel');
            this.storeDescription = getDataFromParam('storeDescription');

            //地图对象
            this.map = new BMap.Map("map");
            this.geo = new BMap.Geolocation();
            //初始化地图
            let p = this.endPoint.split(',');
            let point = new BMap.Point(p[0], p[1]);
            this.map.centerAndZoom(point, 15);
            this.map.enableScrollWheelZoom(true);

            //获取初始位置
            this.handleGetPosition();

            //定时刷新位置
//            setInterval(()=> {
//                this.handleGetPosition();
//            }, 10000)

            //添加地图混合
            this.map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP, BMAP_HYBRID_MAP]}));

            //添加第三方控件
            let zscp_copyright = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT});
            this.map.addControl(zscp_copyright);
            let zscp_bounds = this.map.getBounds();
            zscp_copyright.addCopyright({
                id: 1,
                content: "<a style='font-size:12px; text-decoration: none; color: #333; margin-right: 10px; margin-top: 2px;'>联盟商家</a>",
                bounds: zscp_bounds
            });

            //放大缩小控制
            let navigationControl = new BMap.NavigationControl({
                anchor: BMAP_ANCHOR_TOP_LEFT,
                type: BMAP_NAVIGATION_CONTROL_LARGE,
                enableGeolocation: true
            });
            this.map.addControl(navigationControl);

        }

    }
</script>

<style lang="less">
    @import './style.less';
</style>