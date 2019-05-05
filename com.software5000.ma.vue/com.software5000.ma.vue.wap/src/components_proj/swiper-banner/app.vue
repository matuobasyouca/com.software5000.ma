<template>
    <div class="image-view-box" :style="{ height : swiperHeight}">
        <ul class="image-view-list" :style="imageListStyle"
            @touchstart.stop="handleMoveStart"
            @touchend.stop="handleMoveEnd">
            <li v-for="(image,index) in images" :key="image.id" :style="[imageStyle,backgroundImage(image.imgPathUrl)]"
                @click="handImgClick(image,index)"></li>
        </ul>
        <p class="image-view-index">{{ `${imageIndex + 1} / ${images.length}` }}</p>
    </div>
</template>

<script>
    export default {
        name: 'swiperBanner',
        props: {
            images: {
                type: Array,
                default() {
                    return [];
                }
            },
            indexDefault: {
                type: [Number, String],
                default: 0
            },
            speed: {
                type: [Number, String],
                default: 2500
            },
            value: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
                winW: 0,
                swiperHeight: '280px',
                startX: 0,
                startY: 0,
                endX: 0,
                endY: 0,
                imageIndex: 0,
                auto: ''
            };
        },
        computed: {
            imageListStyle() {
                const style = {};

                style.width = `${this.images.length * this.winW}px`;
                style.left = `-${this.imageIndex * this.winW}px`;
                this.swiperHeight = `${this.winW * 0.6}px`;
                return style;
            },
            imageStyle() {
                const style = {};

                style.width = `${this.winW}px`;
                return style;
            }
        },
        watch: {
            value(val) {
                val && (this.imageIndex = this.index || 0);
            }
        },
        methods: {
            //自动切换图片
            handImgAutoTurn() {
                this.auto = setInterval(() => {
                    if (this.imageIndex == this.images.length - 1) {
                        this.imageIndex = 0;
                    } else {
                        this.imageIndex++;
                    }
                }, this.speed);
            },
            handleMoveStart(e) {
                clearInterval(this.auto);
                this.startX = e.touches[0].pageX;
                this.startY = e.touches[0].pageY;
            },
            handleMoveEnd(e) {
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
                    default:
                        break;
                }
                this.handImgAutoTurn();
            },
            //点击对应轮播图执行事件
            handImgClick(data, index) {
                this.$emit('click', index);
            },
            getDirection(startX, startY, endX, endY) {
                const dy = startY - endY;
                const dx = endX - startX;
                let result = 0;

                if (Math.abs(dx) < 5) {
                    return result;
                }
                const angle = this.getAngle(dx, dy);

                if (angle >= -45 && angle < 45) {
                    result = 1;
                } else if (angle >= 45 && angle < 135 || angle >= -135 && angle < -45) {
                    result = 0;
                } else if (angle >= 135 && angle <= 180 || angle >= -180 && angle < -135) {
                    result = -1;
                }
                return result;
            },
            getAngle(dx, dy) {
                return Math.atan2(dy, dx) * 180 / Math.PI;
            },
            backgroundImage(image) {
                const style = {};

                style.backgroundImage = `url(${image})`;
                return style;
            }
        },
        created() {
            this.winW = document.body.clientWidth;
            this.handImgAutoTurn();
        }
    };
</script>
<style lang="less">
    @import './style.less';
</style>