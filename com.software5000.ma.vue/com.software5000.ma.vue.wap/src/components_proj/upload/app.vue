<template>
    <ul class="upload-photo__wrap clr">
        <li v-for="(image,index) in images" @click="handleView(index)">
            <div class="image-label" :style="{backgroundImage:`url('${image.showImg}')`}">
                <span class="image-del-icon" @click.stop="handleDelImg(index,image.id)"></span>
            </div>
        </li>
        <li v-if="!isFull" class="upload-photo__btn">
            <label class="image-label add-label" for="imgInput">
                <input
                        class="imgInput"
                        id="imgInput"
                        type="file"
                        accept="image/*"
                        @change="handlePushImg">
            </label>
        </li>
    </ul>
</template>

<script>
    import turnToBase from '../../assets/js/imgTurnToBase';
    export default {
        name: 'UploadPhoto',
        props: {
            reset: Boolean,
            images: {
                type: Array,
                default(){
                    return []
                }
            },
            max: {
                type: [Number, String],
                default: 6
            }
        },
        data () {
            return {
//                photos: []
            }
        },
        computed: {
            isFull(){
                return this.images.length >= this.max;
            }
        },
        methods: {
            handlePushImg(e){
                turnToBase(e.target, 600, (baseData) => {
                    if (!this.isFull) {
                        this.$emit('change', baseData.slice(23));
                        e.target.value = '';
                    }
                })
            },
            handleDelImg(index, id){
                this.$confirm({
                    type: 'delete2',
                    message: '确认删除该图片？'
                }).then(() => {
                    this.$emit('delete', index, id);
                }).catch(() => {
                })

            },
            handleView(index){
                this.$emit('view', index);
            }
        }
    }
</script>
<style lang="less">
    @import './style.less';
</style>