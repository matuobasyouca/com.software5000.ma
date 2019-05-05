<template>
    <div class="merchant-insert-page">
        <ma-head :home="false">添加商家</ma-head>
        <ul class="insert-detail">
            <li class="show-inline">
                <span class="insert-detail-title">商家名称</span>
                <zs-input v-model="finalData.businessName" :maxlength="15" placeholder="请输入商家名称" icon="circle-cross"></zs-input>
            </li>
            <li class="show-inline">
                <span class="insert-detail-title">联系电话</span>
                <zs-input v-model="finalData.businessTel" placeholder="请输入电话号码" icon="circle-cross"></zs-input>
            </li>
            <li class="show-inline address" @click="cityPickerVisible=true">
                <span class="insert-detail-title">商家地址</span>
                <em :class="[{'color-black':tempAddr.length>0}]">{{ addressShow }}</em>
                <zs-icon icon="arrow-right" size="10"></zs-icon>
            </li>
            <li class="show-inline address-detail">
                <zs-input v-model="finalData.businessAddress" type="textarea" resize="none"
                          placeholder="详细地址"></zs-input>
            </li>
            <li class="show-inline merchant-state">
                <span class="insert-detail-title">商家状态</span>
                <em>{{ merchantState ? '上架' : '下架' }}</em>
                <zs-switch v-model="merchantState" class="merchant-state-switch"></zs-switch>
            </li>
            <li class="merchant-pic">
                <p class="insert-detail-title">
                    <span>商家图片</span>
                    <label for="imgInput" class="reset-pic" v-if="finalData.imgPath">重新上传</label>
                </p>
                <label
                        class="image-label"
                        :class="[{'add-label':!finalData.imgPath}]"
                        for="imgInput"
                        :style="{backgroundImage:'url('+imgShow+')'}">
                    <input
                            class="imgInput"
                            id="imgInput"
                            type="file"
                            accept="image/*"
                            @change="hangleUpLoad">
                </label>
            </li>
            <li class="merchant-desc">
                <span class="insert-detail-title">商家描述</span>
                <zs-input
                        v-model="finalData.businessDetail"
                        type="textarea"
                        resize="none"
                        placeholder="请输入商家描述"
                ></zs-input>
            </li>
        </ul>
        <div class="confirm-button" @click="handleSubmit">保存</div>

        <city-picker
                v-model="cityPickerVisible"
                @get-address="handleGetAddress"></city-picker>
        <div id="map"></div>
    </div>
</template>
<script>
    import maHead from '../../../../../components_proj/ma_head/app.vue';
    import cityPicker from '../../../../../components_proj/city_picker/city-picker.vue';
    import turnToBase from '../../../../../assets/js/imgTurnToBase';
    import {isMobile, isTel, turnToNextPage} from '../../../../../assets/js/utils';
    export default {
        components: {
            maHead,
            cityPicker
        },
        data(){
            return {
                //操作的数据
                tempAddr: [],
                pcd: '',//保持省市区文字
                addressShow: '请选择',
                merchantState: true,
                imgShow: '',
                cityPickerVisible: false,
                map: null,
                geo: null,
                //保存数据
                finalData: {
                    businessName: '',
                    businessTel: '',
                    businessAreaCode: '',
                    businessAddress: '',
                    businessDetail: '',
                    imgPath: '',
                    businessCoordinate: '',
                    businessLineState: 1,
                }
            }
        },
        watch: {
            tempAddr(val){
                this.pcd = [];
                let code = [];
                val.forEach((d)=> {
                    this.pcd.push(d.fullName)
                    code.push(d.id);
                })
                this.finalData.businessAreaCode = code.join(',')
            },
            merchantState(val){
                this.finalData.businessLineState = val ? 1 : 2
            }
        },
        methods: {
            //上传图片
            hangleUpLoad(e){
                let loading = this.$modal();
                turnToBase(e.target, 600, (baseData) => {
                    this.finalData.imgPath = baseData.slice(23)
                    this.imgShow = baseData
                    loading.close();
                })

            },
            //获取地址
            handleGetAddress(addr){
                this.tempAddr = [];
                this.tempAddr = addr;
                this.setAddressShow(addr);
            },
            //设置显示地址
            setAddressShow(addr){
                if (addr.length == 0) {
                    this.addressShow = '请选择'
                } else {
                    let arr = [];
                    addr.forEach((d)=> {
                        arr.push(d.areaName)
                    })
                    this.addressShow = arr.join(' | ')
                }
            },
            //解析地址坐标
            handleGetPoint(pro, address, fn){
                this.geo.getPoint(address, (point)=> {
                    if (point) {
                        fn && fn(point)
                    } else {
                        this.$modal({
                            type: 'error',
                            message: '你输入的地址获取不到地理位置',
                            duration: 1200
                        })
                        return
                    }
                }, pro);
            },
            //验证数据完整性
            isValidateFull(){
                let msg = {
                    businessName: '请输入商家名',
                    businessTel: '请输入电话号码',
                    businessAreaCode: '请选择地址',
                    businessAddress: '请输入详细地址',
                    businessDetail: '请输入描述',
                    imgPath: '请上传图片',
                }
                for (let key in msg) {
                    if (!this.finalData[key] || this.finalData[key] == '') {
                        this.$modal({
                            type: 'error',
                            message: msg[key],
                            duration: 1200
                        })
                        return false
                    }
                }
                return true
            },
            //验证数据正确性
            isValidateRight(){
                let msg = {
                    businessTel: '电话号码有误'
                }
                let isTelError = !isMobile(this.finalData.businessTel) && !isTel(this.finalData.businessTel);
                for (let key in msg) {
                    if (key == 'businessTel' && isTelError) {
                        this.$modal({
                            type: 'error',
                            message: msg[key],
                            duration: 1200
                        })
                        return false
                    }
                }
                return true
            },
            //提交数据
            handleSubmit(){
                if (this.isValidateFull() && this.isValidateRight()) {
                    let address = this.pcd.join('') + this.finalData.businessAddress;
                    let pro = this.pcd[0]
                    this.handleGetPoint(pro, address, (point)=> {
                        this.finalData.businessCoordinate = `${point.lng},${point.lat}`;
                        this.$ajax(
                                this.$joggle.business.insertBusiness,
                                this.finalData,
                                true,
                                (data, loading)=> {
                                    loading.close();
                                    if (data.code === 'ZS011000') {
                                        this.$message({
                                            type: 'success',
                                            message: '创建成功',
                                            duration: 1200
                                        })
                                        setTimeout(()=> {
                                            turnToNextPage('/wap/operator/home/merchant_manage/merchant_list.html')
                                        }, 1200)
                                    } else {
                                        this.$message({
                                            type: 'error',
                                            message: data.msg,
                                            duration: 1200
                                        })
                                    }
                                }
                        )

                    })

                }

            }
        },
        mounted(){
            //创建百度地图对象
            this.map = new BMap.Map("map");
            //创建地址解析器
            this.geo = new BMap.Geocoder();


        }
    }
</script>
<style lang="less">
    @import './insert.less';
</style>