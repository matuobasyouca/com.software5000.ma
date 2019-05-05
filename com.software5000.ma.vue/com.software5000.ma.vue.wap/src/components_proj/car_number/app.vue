<template>
    <div class="car-number-wrap" :class="{'car-number-icon':photoIcon}">
        <p class="provincials-short" @click="provincialsChoose">{{ currProvincials }}</p>
        <p class="provincials-short-letter" @click="provincialsLetterChoose">{{ currLetter }}</p>
        <input ref="input"
               class="car-number-input"
               type="text" maxlength="5"
               placeholder="请输入车牌号"
               v-model="currNum"
               @blur="handBlur"
               @focus="handleFocus">
        <div v-if="photoIcon" class="camera-icon">
            <label for="carNumInput">
                <zs-icon icon="camera" size="25"></zs-icon>
            </label>
            <input class="img-input" type="file" accept="image/*" id="carNumInput" @change="handleCameraImg">
        </div>

        <div class="model-mark" @click="closeAlert" v-show="showAlert"></div>
        <transition name="page-slide">
            <div class="modalForCarLicence" v-show="provincialsVisible">
                <h2 class="title">车牌选择</h2>
                <div class="province_wrap">
                    <ul class="modalForCarLicence-ul clr">
                        <li v-for="provincial in provincials" @click="handleProvincial(provincial)"
                            :class="{'is-curr' : currProvincials === provincial.slice(1,2)}">{{provincial}}
                        </li>
                    </ul>
                </div>
            </div>
        </transition>
        <transition name="page-slide">
            <div class="modalForCarLicence" v-show="provincialsLetterVisible">
                <h2 class="title">车牌选择</h2>
                <div class="province_wrap">
                    <ul class="modalForCarLicence-ul clr">
                        <li v-for="letter in letters" @click="handleLetter(letter)"
                            :class="['letter-float',{'is-curr' : currLetter === letter}]">{{letter}}
                        </li>
                    </ul>
                </div>
            </div>
        </transition>
    </div>
</template>
<script>
    import turnToBase from '../../assets/js/imgTurnToBase';
    import { isCarNum, isEmpty } from '../../assets/js/utils';
    export default {
        name: 'carNumber',
        props: {
            photoIcon: {
                default: false,
                type: [Boolean]
            },
            dialogTitle: {
                default: '选择车牌',
                type: [String]
            },
            clearNum: {
                default: false,
                type: [Boolean]
            },
            allnum: {
                default: false,
                type: [Boolean]
            },
            carProvince: String,
            carLetter: String,
            carNum: String
        },
        data () {
            return {
                carNumber: '',
                letters: [
                    'A', 'B', 'C', 'D', 'E',
                    'F', 'G', 'H', 'I', 'J',
                    'K', 'L', 'M', 'N', 'O',
                    'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y',
                    'Z'
                ],
                provincials: [
                    '（京）北京', '（津）天津', '（沪）上海', '（渝）重庆', '（冀）河北', '（豫）河南',
                    '（云）云南', '（辽）辽宁', '（黑）黑龙江', '（湘）湖南', '（皖）安徽', '（鲁）山东',
                    '（新）新疆', '（苏）江苏', '（浙）浙江', '（赣）江西', '（鄂）湖北', '（桂）广西',
                    '（甘）甘肃', '（晋）山西', '（蒙）内蒙古', '（陕）陕西', '（吉）吉林', '（闽）福建',
                    '（贵）贵州', '（粤）广东', '（青）青海', '（藏）西藏', '（川）四川', '（宁）宁夏', '（琼）海南'
                ],
                provincialsVisible: false,
                provincialsLetterVisible: false,
                showAlert: false,
                currProvincials: '闽',
                currLetter: 'D',
                currNum: ''
            };
        },
        computed: {
            fullCarNum(){
                let fullCarNum = `${this.currProvincials}${this.currLetter}${this.currNum}`;

                fullCarNum = fullCarNum.toUpperCase();
                return fullCarNum;
            }
        },
        watch: {
            carProvince (val){
                this.currProvincials = val;
            },
            carLetter (val){
                this.currLetter = val;
            },
            carNum (val){
                this.currNum = val;
            },
            currNum(){
                this.dispatchCarNum();
            },
            clearNum(val){
                if (val) {
                    this.currNum = '';
                }
            }
        },
        methods: {
            closeAlert(){
                this.showAlert = false;
                this.provincialsVisible = false;
                this.provincialsLetterVisible = false;
            },
            handleCameraImg(e){
                let loading = this.$modal({
                    message: '车牌识别中...'
                });

                turnToBase(e.target, 600, (baseDate) => {
                    this.$ajax(
                        this.$joggle.merchant.workorder.imgRecUrl,
                        { carNumber: baseDate.slice(23) },
                        loading,
                        (data, loading) => {
                            if (data.code === 'ZS011000') {
                                let _data = JSON.parse(data.data);
                                let carNumberData = _data.data.item.platenumber;

                                if (!isEmpty(carNumberData)) {
                                    this.currNum = carNumberData.slice(2);
                                    this.currProvincials = carNumberData[0];
                                    this.currLetter = carNumberData[1];

                                    let fullCarNum = `${this.currProvincials}${this.currLetter}${this.currNum}`;

                                    if (!isCarNum(fullCarNum)) {
                                        loading.set({
                                            type: 'error',
                                            duration: 1200,
                                            message: '车牌不正确'
                                        });
                                        return;
                                    }
                                    loading.set && loading.set({
                                        type: 'success',
                                        duration: 1200,
                                        message: '识别成功'
                                    });
                                    this.$refs.input.focus();
                                    this.dispatchCarNum();

                                } else {
                                    loading.set({
                                        type: 'error',
                                        duration: 1200,
                                        message: '车牌识别出错'
                                    });
                                }
                            } else {
                                loading.set({
                                    type: 'error',
                                    duration: 1200,
                                    message: data.msg
                                });
                            }
                        });
                });
            },
            dispatchCarNum(){
                this.$emit('get-car-number', this.fullCarNum);
            },
            provincialsChoose(){
                this.showAlert = true;
                this.provincialsVisible = true;
                this.provincialsLetterVisible = false;
            },
            provincialsLetterChoose(){
                this.showAlert = true;
                this.provincialsVisible = false;
                this.provincialsLetterVisible = true;
            },
            handleProvincial(provincial){
                this.currProvincials = provincial.slice(1, 2);
                this.provincialsVisible = false;
                this.provincialsLetterVisible = true;
//                this.$refs.input.focus();
                this.dispatchCarNum();
            },
            handleLetter(letter){
                this.currLetter = letter;
                this.provincialsLetterVisible = false;
                this.showAlert = false;
                this.$refs.input.focus();
                this.dispatchCarNum();
            },
            handBlur(){
                this.$emit('blur', this.fullCarNum);
//                this.$emit('get-car-number', this.fullCarNum);
            },
            handleFocus(){
                this.$emit('focus');
            }
        }
    };
</script>
<style lang="less">
    @import './style.less';
</style>