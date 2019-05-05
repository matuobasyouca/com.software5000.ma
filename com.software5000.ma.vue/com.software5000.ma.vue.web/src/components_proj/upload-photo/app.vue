<template>
    <ul class="upload-photo__wrap clr">
        <li v-for="(photo,index) in photos" :style="{backgroundImage:`url('${photo}')`}">
            <div class="delete-icon-mark">
                <i class="zs-icon-delete2" @click="handleDelImg(index)"></i>
            </div>
        </li>
        <li v-if="!isFull" class="upload-photo__btn">
            <label :for="`imgInput${index}`">
                <input
                        class="imgInput"
                        :id="`imgInput${index}`"
                        type="file"
                        accept="image/jpg,image/jpeg,image/png,image/gif,image/bmp"
                        @change="handlePushImg">
            </label>
        </li>
    </ul>
</template>

<script>
    import turnToBase from '../../assets/js/imgTurnToBase';
    import {isEmpty} from  '../../assets/js/utils';
    export default {
        name: 'UploadPhoto',
        props: {
            reset: Boolean,
            index: {
                type: Number,
                default: 1
            },
            max: {
                type: [String, Number],
                default: 5
            },
            originalPhotos: String
        },
        data () {
            return {
                photos: []
            }
        },
        computed: {
            isFull(){
                return this.photos.length >= this.max;
            },
            photoDatas(){
                return this.photos.map((photo) => {
                    return photo.slice(23);
                })
            }
        },
        watch: {
            reset(val){
                !val && (this.photos = []);
            },
            originalPhotos(val){
                if (!isEmpty(val)) {
                    this.photos = val.indexOf('http://') > -1 ? [val] : [`data:image/jpeg;base64, `];
                }
            }
        },
        methods: {
            handlePushImg(e){
                turnToBase(e.target, 600, (baseData) => {
                    if (!this.isFull) {
                        this.photos.push(baseData)
                        this.$emit('get-photos', this.photoDatas);
                    }
                })
            },
            handleDelImg(index){
                this.$delete(this.photos, index);
                this.$emit('get-photos', this.photoDatas);
            }
        }
    }
</script>
<style>
    @import 'style.css';
</style>