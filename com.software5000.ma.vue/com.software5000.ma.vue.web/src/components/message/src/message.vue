<template>
    <div class="zs-message__wrap">
        <transition name="zs-message-fade">
            <div
                    class="zs-message"
                    v-show="visible"
                    @mouseenter="clearTimer"
                    @mouseleave="startTimer">
                <zs-icon :icon="iconType" :text="message" size="26"></zs-icon>
            </div>
        </transition>
    </div>
</template>

<script type="text/babel">
    import zsIcon from '../../icon/index';
    const typeMap = {
        error: 'circle-error',
        success: 'circle-success',
        warning: 'warning',
        danger: 'danger',
    }
    export default {
        components: {
            zsIcon
        },
        data() {
            return {
                visible: false,
                message: '',
                duration: 1200,
                type: '',
                customClass: '',
                onClose: null,
                showClose: false,
                closed: false,
                timer: null
            };
        },

        computed: {
            iconType(){
                return typeMap[this.type] ? typeMap[this.type] : '';
            },
        },

        watch: {
            closed(newVal) {
                if (newVal) {
                    this.visible = false;
                    this.$el.addEventListener('transitionend', this.destroyElement);
                }
            }
        },

        methods: {
            destroyElement() {
                this.$el.removeEventListener('transitionend', this.destroyElement);
                this.$destroy(true);
                this.$el.parentNode.removeChild(this.$el);
            },

            close() {
                this.closed = true;
                if (typeof this.onClose === 'function') {
                    this.onClose(this);
                }
            },

            clearTimer() {
                clearTimeout(this.timer);
            },

            startTimer() {
                if (this.duration > 0) {
                    this.timer = setTimeout(() => {
                        if (!this.closed) {
                            this.close();
                        }
                    }, this.duration);
                }
            }
        },

        mounted() {
            this.startTimer();
        }
    };
</script>
