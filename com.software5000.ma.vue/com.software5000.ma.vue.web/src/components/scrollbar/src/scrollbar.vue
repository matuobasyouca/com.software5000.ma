<template>
    <div class="zs-scrollbar">
        <div
                class="zs-scrollbar__wrap"
                :class="[wrapClass,  gutter && !native ? '' : 'el-scrollbar__wrap--hidden-default']"
                :style="_wrapStyle"
                ref="wrap"
                @scroll="handleScroll">
            <Scrollview
                    :tag="tag"
                    :viewClass="viewClass"
                    :viewStyle="viewStyle"
                    ref="resize">
                <slot></slot>
            </Scrollview>
        </div>
        <template v-if="!native">
            <bar :move="moveX" :size="sizeWidth"></bar>
            <bar vertical :move="moveY" :size="sizeHeight"></bar>
        </template>
    </div>
</template>

<script>
    import {debounce} from 'throttle-debounce';
    import {addResizeListener, removeResizeListener} from '../../src/utils/resize-event';
    import scrollbarWidth from '../../src/utils/scrollbar-width';
    import {toObject, extend} from '../../src/utils/util';
    import * as util from './util';
    import Bar from './bar';
    import Scrollview from './view';
    export default {
        name: 'ZsScrollbar',
        components: {Bar, Scrollview},
        props: {
            native: Boolean,
            fullScreen: Boolean,
            wrapStyle: {
                default: ''
            },
            wrapClass: {},
            viewClass: {},
            viewStyle: {},
            canresize: Boolean,
            tag: {
                type: String,
                default: 'div'
            }
        },
        data() {
            return {
                sizeWidth: '0',
                sizeHeight: '0',
                moveX: 0,
                moveY: 0
            };
        },
        computed: {
            wrap() {
                return this.$refs.wrap;
            },
            gutter(){
                return scrollbarWidth()
            },
            _wrapStyle(){
                let style = this.wrapStyle;
                const wrapHeight = this.fullScreen ? document.body.clientHeight || document.documentElement.clientHeight : '';
                if (this.gutter) {
                    const gutterWith = `-${this.gutter}px`;
                    const gutterStyle = `margin-bottom:${gutterWith};margin-right:${gutterWith};`;
                    if (typeof style === 'object') {
                        style.marginRight = style.marginBottom = gutterWith;
                        style.height = wrapHeight + 'px';
                    } else {
                        style += gutterStyle;
                        style += `height:${wrapHeight}px`
                    }
                }

                return style;
            }
        },
        methods: {
            handleScroll() {
                if (this.native)return;
                const wrap = this.wrap;
                this.moveY = ((wrap.scrollTop * 100) / wrap.clientHeight);
                this.moveX = ((wrap.scrollLeft * 100) / wrap.clientWidth);
            },

            update() {
                let heightPercentage, widthPercentage;
                const wrap = this.wrap;
                if (!wrap) return;
                heightPercentage = (wrap.clientHeight * 100 / wrap.scrollHeight);
                widthPercentage = (wrap.clientWidth * 100 / wrap.scrollWidth);
                this.sizeHeight = (heightPercentage < 100) ? (heightPercentage + '%') : '';
                this.sizeWidth = (widthPercentage < 100) ? (widthPercentage + '%') : '';

            }
        },
        mounted() {
            if (this.native) return;
            this.$nextTick(this.update);
            this.canresize && addResizeListener(this.$refs.resize.$el, this.update);
            this.$refs.wrap.addEventListener('scroll', debounce(300, () => {
                this.$emit('scroll')
                this.update();
            }))
        },
        destroyed() {
            if (this.native) return;
            this.canresize && removeResizeListener(this.$refs.resize.$el, this.update);
        }
    };

</script>