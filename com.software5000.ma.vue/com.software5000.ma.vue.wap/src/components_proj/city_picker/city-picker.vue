<template>
    <div>
        <div class="city-picker-marsk" v-show="value" @click="handleClose"></div>
        <transition name="city-picker-slide">
            <div v-show="value" class="city-picker-container">
                <ul class="city-picker-btn-box">
                    <li class="city-picker-btn" @click="handleClose">返回</li>
                    <li class="city-picker-btn" @click="handleSubmit">提交</li>
                </ul>
                <div class="city-picker-content">
                    <div class="city-picker">
                        <ul
                                class="city-picker-province"
                                ref="provincePicker"
                                :style="provinceStyle">
                            <li></li>
                            <li></li>
                            <li v-for="pro in provinces" :key="pro.id">{{ pro.areaName }}</li>
                            <li></li>
                            <li></li>
                        </ul>
                    </div>
                    <div class="city-picker">
                        <ul
                                class="city-picker-city"
                                ref="cityPicker"
                                :style="cityStyle">
                            <li></li>
                            <li></li>
                            <li v-for="city in citys" :key="city.id">{{ city.areaName }}</li>
                            <li></li>
                            <li></li>
                        </ul>
                    </div>
                    <div class="city-picker">
                        <ul
                                class="city-picker-district"
                                ref="districtPicker"
                                :style="districtStyle">
                            <li></li>
                            <li></li>
                            <li v-for="dis in districts" :key="dis.id">{{ dis.areaName }}</li>
                            <li></li>
                            <li></li>
                        </ul>
                    </div>
                    <div class="city-picker-up-shadow"></div>
                    <div class="city-picker-down-shadow"></div>
                    <div class="city-picker-line"></div>
                </div>
            </div>
        </transition>
    </div>
</template>
<script>

    export default {
        props: {
            value: false
        },
        data(){
            return {
                addressVisible: true,
                liHeight: 40,
                start: {
                    Y: 0,
                    time: ''
                },
                move: {
                    Y: 0,
                    speed: []
                },
                end: {
                    Y: 0,
                    status: true,
                },
                maxHeight: [0, 0, 0],
                distance: [0, 0, 0],
                dis: [0, 0, 0],
                provinces: [],
                citys: [],
                districts: [],
                finalAddr: [
                    {
                        id: 350000,
                        areaName: '福建',
                        fullName: '福建省'
                    },
                    {
                        id: 350200,
                        areaName: '厦门',
                        fullName: '厦门市'
                    },
                    {
                        id: 350203,
                        areaName: '思明',
                        fullName: '思明区'
                    }
                ]

            }
        },
        computed: {
            provinceStyle(){
                let style = {};
                style.transform = `translateY(${this.dis[0]}px)`;
                style.transition = `transform ${this.move.speed[0]}s ease-out`;
                return style;
            },
            cityStyle(){
                let style = {};
                style.transform = `translateY(${this.dis[1]}px)`;
                style.transition = `transform ${this.move.speed[1]}s ease-out`;
                return style;
            },
            districtStyle(){
                let style = {};
                style.transform = `translateY(${this.dis[2]}px)`;
                style.transition = `transform ${this.move.speed[2]}s ease-out`;
                return style;
            }
        },
        methods: {
            handleGetProvinces(fn){
                this.$ajax(
                        this.$joggle.mzscp.selectProvince,
                        {},
                        false,
                        (data)=> {
                            if (data.code === 'ZS011000') {
                                this.maxHeight[0] = (data.data.length + 4) * this.liHeight;
                                this.provinces = data.data
                                fn && fn();
                            }
                        }
                )
            },
            handleGetCitys(id, fn){
                this.$ajax(
                        this.$joggle.mzscp.selectAreaCode,
                        {areaId: id},
                        false,
                        (data)=> {
                            if (data.code === 'ZS011000') {
                                this.maxHeight[1] = (data.data.length + 4) * this.liHeight;
                                this.citys = data.data
                                fn && fn(this.citys);
                            }
                        }
                )
            },
            handleGetDistricts(id, fn){
                this.$ajax(
                        this.$joggle.mzscp.selectAreaCode,
                        {areaId: id},
                        false,
                        (data)=> {
                            if (data.code === 'ZS011000') {
                                this.maxHeight[2] = (data.data.length + 4) * this.liHeight;
                                this.districts = data.data
                                fn && fn(this.districts);
                            }
                        }
                )
            },
            getProvinceIndex(id){
                for (let i = 0; i < this.provinces.length; i++) {
                    if (this.provinces[i].id == id) {
                        return i
                    }
                }
            },
            getCityIndex(id){
                for (let i = 0; i < this.citys.length; i++) {
                    if (this.citys[i].id == id) {
                        return i
                    }
                }
            },
            getDisIndex(id){
                for (let i = 0; i < this.districts.length; i++) {
                    if (this.districts[i].id == id) {
                        return i
                    }
                }
            },
            initPosition: function (dis, max, idx) {
                dis = dis < 0 ? 0 : dis;
                dis = dis > max ? max : dis;
                let sub = dis % this.liHeight;
                if (sub < this.liHeight / 2) {
                    this.distance[idx] = dis - sub;
                } else {
                    this.distance[idx] = dis + (this.liHeight - sub);
                }
                return this;
            },
            initSpeed: function (arr, dir, max, idx) {
                let variance = 0;
                let sum = 0;
                let rate = 0;
                for (let i in arr) {
                    sum += arr[i] - 0;
                }
                for (let j in arr) {
                    variance += (arr[j] - (sum / arr.length)) * (arr[j] - (sum / arr.length));
                }
                if ((variance / arr.length).toFixed(2) > .1) {
                    rate = max > this.liHeight * 15 ? dir * 2 : 0;
                    this.initPosition(this.distance[idx] + rate, max - this.liHeight * 5, idx);
                    this.move.speed[0] = .2;
                } else {
                    this.initPosition(this.distance[idx], max, idx);
                    this.move.speed[0] = this.move.speed[0] > 0.2 ? .2 : this.move.speed[0];
                }
            },
            touch: function (event, idx) {
                event = event || window.event;
                event.preventDefault();
                switch (event.type) {
                    case "touchstart":
                        if (this.end.status) {
                            this.end.status = !this.end.status;
                            event.preventDefault();
                            this.move.speed = [];
                            this.$set(this.start, 'Y', event.touches[0].clientY)
                            this.start.time = Date.now();
                        }
                        break;
                    case "touchmove":
                        event.preventDefault();
                        this.$set(this.move, 'Y', event.touches[0].clientY)
                        let offset = this.start.Y - this.move.Y;
                        if (this.distance[idx] == 0 && offset < 0) {
                            this.$set(this.dis, idx, 1.5 * this.liHeight)
                            this.move.speed[0] = 0.2;
                        } else {
                            let newDis = -(offset + this.distance[idx] * 1);
                            this.$set(this.dis, idx, newDis)
                        }
                        if (Math.abs(offset).toFixed(0) % 5 === 0) {
                            let time = Date.now();
                            this.move.speed.push((Math.abs(offset) / (time - this.start.time)).toFixed(2));
                        }
                        break;
                    case "touchend":
                        this.end.Y = Math.abs(event.changedTouches[0].clientY);
                        let tempDis = this.distance[idx] + (this.start.Y - this.end.Y);

                        this.distance[idx] = tempDis < 0 ? 0 :
                                (tempDis < this.maxHeight[idx] - this.liHeight * 5 ? tempDis :
                                this.maxHeight[idx] - this.liHeight * 5);
                        this.initSpeed(this.move.speed, this.start.Y - this.end.Y, this.maxHeight[idx], idx);
                        this.$set(this.dis, idx, -1 * this.distance[idx]);

                        let _i = this.distance[idx] / this.liHeight;
                        if (idx == 0) {
                            let id = this.provinces[_i].id
                            this.distance[1] = this.dis[1] = 0;
                            this.distance[2] = this.dis[2] = 0;
                            this.finalAddr[0] = this.provinces[_i];
                            this.handleGetCitys(id, (data)=> {

                                if (data.length == 0) {
                                    this.districts = []
                                    this.finalAddr[1] = '';
                                    return;
                                }
                                this.finalAddr[1] = data[0];
                                this.handleGetDistricts(data[0].id, (data)=> {
                                    if (data.length == 0) {
                                        this.finalAddr[2] = '';
                                    } else {
                                        this.finalAddr[2] = data[0];
                                    }
                                })

                            })
                        } else if (idx == 1) {
                            this.distance[2] = this.dis[2] = 0;
                            let id = this.citys[_i].id;

                            this.finalAddr[1] = this.citys[_i];

                            this.handleGetDistricts(id, (data)=> {
                                if (data.length == 0) {
                                    this.finalAddr[2] = '';
                                } else {
                                    this.finalAddr[2] = data[0];
                                }
                            })
                        } else if (idx == 2) {
                            this.finalAddr[2] = this.districts[_i];
                        }

                        setTimeout(()=> {
                            this.end.status = true;
                        }, this.move.speed[0] * 1000);

                        break;

                }
            },
            getPosition(idx, id){
                let p = 0
                if (idx == 0) {
                    p = this.getProvinceIndex(id)
                } else if (idx == 1) {
                    p = this.getCityIndex(id)
                } else if (idx == 2) {
                    p = this.getDisIndex(id)
                }
                this.$set(this.distance, idx, p * this.liHeight)
                this.$set(this.dis, idx, -1 * this.distance[idx])
            },
            initReady(){
                this.$refs.provincePicker.addEventListener('touchstart', ()=> {
                    this.touch(event, 0);
                }, false);
                this.$refs.provincePicker.addEventListener('touchmove', ()=> {
                    this.touch(event, 0);
                }, false);
                this.$refs.provincePicker.addEventListener('touchend', ()=> {
                    this.touch(event, 0);
                }, false);

                this.$refs.cityPicker.addEventListener('touchstart', ()=> {
                    this.touch(event, 1);
                }, false);
                this.$refs.cityPicker.addEventListener('touchmove', ()=> {
                    this.touch(event, 1);
                }, false);
                this.$refs.cityPicker.addEventListener('touchend', ()=> {
                    this.touch(event, 1);
                }, false);

                this.$refs.districtPicker.addEventListener('touchstart', ()=> {
                    this.touch(event, 2);
                }, false);
                this.$refs.districtPicker.addEventListener('touchmove', ()=> {
                    this.touch(event, 2);
                }, false);
                this.$refs.districtPicker.addEventListener('touchend', ()=> {
                    this.touch(event, 2);
                }, false);
            },
            handleSubmit(){
                this.$emit('get-address', this.finalAddr)
                this.$emit('input', false)
            },
            handleClose(){
                this.$emit('input', false)
            }
        },
        mounted(){
            this.handleGetProvinces(()=> {
                let fujianId = 350000;
                this.getPosition(0, fujianId);

                this.handleGetCitys(fujianId, ()=> {
                    let xiamenId = 350200;
                    this.getPosition(1, xiamenId);

                    this.handleGetDistricts(xiamenId, ()=> {
                        let simingId = 350203;
                        this.getPosition(2, simingId);
                    })
                })
            });
            this.initReady()
        }
    }
</script>
<style scoped lang="less">
    @import './city_picker.less';
</style>
