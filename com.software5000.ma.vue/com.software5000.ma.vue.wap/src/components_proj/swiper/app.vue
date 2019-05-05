<template>
    <div class="image-view-box" v-show="value" @click="close">
        <ul class="image-view-list" :style="imageListStyle"
            @touchstart.stop="handleMoveStart"
            @touchend.stop="handleMoveEnd">
            <li v-for="image in images" :key="image.id" :style="[imageStyle,backgroundImage(image.showImg)]"></li>
        </ul>
        <p class="image-view-index">{{ `${imageIndex + 1} / ${images.length}` }}</p>
    </div>
</template>

<script>
    export default {
        name: 'UploadPhoto',
        props: {
            images: {
                type: Array,
                default(){
                    return []
                }
            },
            index: {
                type: [Number, String],
                default: 0
            },
            value: Boolean
        },
        data () {
            return {
                winW: 0,
                startX: 0,
                startY: 0,
                endX: 0,
                endY: 0,
                imageIndex: 0,
            }
        },
        computed: {
            imageListStyle(){
                const style = {};
                style.width = `${this.images.length * this.winW}px`;
                style.left = `-${this.imageIndex * this.winW}px`;
                return style;
            },
            imageStyle(){
                const style = {};
                style.width = `${this.winW}px`;
                return style;
            }
        },
        watch: {
            value(val){
                val && (this.imageIndex = this.index || 0);
            }
        },
        methods: {
            handleMoveStart(e){
                this.startX = e.touches[0].pageX;
                this.startY = e.touches[0].pageY;
            },
            handleMoveEnd(e){
                this.endX = e.changedTouches[0].pageX;
                this.endY = e.changedTouches[0].pageY;
                const direction = this.getDirection(this.startX, this.startY, this.endX, this.endY);
                switch (direction) {
                    case 0:
                        break;
                    case -1: //向左滑动
                        if (this.imageIndex < this.images.length - 1) {
                            this.imageIndex++;
                        }
                        break;
                    case 1://向右滑动
                        if (this.imageIndex > 0) {
                            this.imageIndex--;
                        }
                        break;
                }
            },
            getDirection(startX, startY, endX, endY){
                const dy = startY - endY;
                const dx = endX - startX;
                let result = 0;
                if (Math.abs(dx) < 5) {
                    return result;
                }
                const angle = this.getAngle(dx, dy);
                if (angle >= -45 && angle < 45) {
                    result = 1;
                } else if ((angle >= 45 && angle < 135) || (angle >= -135 && angle < -45)) {
                    result = 0;
                } else if ((angle >= 135 && angle <= 180) || (angle >= -180 && angle < -135)) {
                    result = -1;
                }
                return result;
            },
            getAngle(dx, dy){
                return Math.atan2(dy, dx) * 180 / Math.PI;
            },
            backgroundImage(image){
                const style = {};
                style.backgroundImage = `url(${image})`;
                return style;
            },
            close(){
                this.$emit('input', false);
            }
        },
        created(){
            this.winW = document.body.clientWidth;
        }
    }
</script>
<style lang="less">
    @import './style.less';
</style>