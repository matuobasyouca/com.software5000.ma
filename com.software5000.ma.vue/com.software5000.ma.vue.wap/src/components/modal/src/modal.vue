<template>
    <transition name="zs-modal-fade" @after-leave="handleAfterLeave">
        <div
                v-show="visible"
                class="zs-modal-mask"
                :class="[customClass, { 'is-fullscreen': fullscreen }]">
            <div class="zs-modal-spinner">
                <svg v-if="type==='loading'" width="60" height="60" viewBox="0 0 135 135"
                     xmlns="http://www.w3.org/2000/svg" fill="#fff">
                    <path d="M67.447 58c5.523 0 10-4.477 10-10s-4.477-10-10-10-10 4.477-10 10 4.477 10 10 10zm9.448 9.447c0 5.523 4.477 10 10 10 5.522 0 10-4.477 10-10s-4.478-10-10-10c-5.523 0-10 4.477-10 10zm-9.448 9.448c-5.523 0-10 4.477-10 10 0 5.522 4.477 10 10 10s10-4.478 10-10c0-5.523-4.477-10-10-10zM58 67.447c0-5.523-4.477-10-10-10s-10 4.477-10 10 4.477 10 10 10 10-4.477 10-10z">
                        <animateTransform
                                attributeName="transform"
                                type="rotate"
                                from="0 67 67"
                                to="-360 67 67"
                                dur="2.5s"
                                repeatCount="indefinite"/>
                    </path>
                    <path d="M28.19 40.31c6.627 0 12-5.374 12-12 0-6.628-5.373-12-12-12-6.628 0-12 5.372-12 12 0 6.626 5.372 12 12 12zm30.72-19.825c4.686 4.687 12.284 4.687 16.97 0 4.686-4.686 4.686-12.284 0-16.97-4.686-4.687-12.284-4.687-16.97 0-4.687 4.686-4.687 12.284 0 16.97zm35.74 7.705c0 6.627 5.37 12 12 12 6.626 0 12-5.373 12-12 0-6.628-5.374-12-12-12-6.63 0-12 5.372-12 12zm19.822 30.72c-4.686 4.686-4.686 12.284 0 16.97 4.687 4.686 12.285 4.686 16.97 0 4.687-4.686 4.687-12.284 0-16.97-4.685-4.687-12.283-4.687-16.97 0zm-7.704 35.74c-6.627 0-12 5.37-12 12 0 6.626 5.373 12 12 12s12-5.374 12-12c0-6.63-5.373-12-12-12zm-30.72 19.822c-4.686-4.686-12.284-4.686-16.97 0-4.686 4.687-4.686 12.285 0 16.97 4.686 4.687 12.284 4.687 16.97 0 4.687-4.685 4.687-12.283 0-16.97zm-35.74-7.704c0-6.627-5.372-12-12-12-6.626 0-12 5.373-12 12s5.374 12 12 12c6.628 0 12-5.373 12-12zm-19.823-30.72c4.687-4.686 4.687-12.284 0-16.97-4.686-4.686-12.284-4.686-16.97 0-4.687 4.686-4.687 12.284 0 16.97 4.686 4.687 12.284 4.687 16.97 0z">
                        <animateTransform
                                attributeName="transform"
                                type="rotate"
                                from="0 67 67"
                                to="360 67 67"
                                dur="8s"
                                repeatCount="indefinite"/>
                    </path>
                </svg>
                <i v-else :class="typeClass"></i>
                <p v-if="message" class="zs-loading-text">{{ message }}</p>
            </div>
        </div>
    </transition>
</template>

<script>
    let typeMap = {
        error : 'type-error',
        success : 'type-success',
        warning : 'type-warning'
    }
    export default {
        data() {
            return {
                message : null,
                fullscreen : true,
                visible : false,
                customClass : '',
                type : "loading",
                duration : 2500
            }
        },
        computed : {
            typeClass(){
                return this.type && typeMap[this.type] ? `zs-modal-${typeMap[this.type]}` : '';
            }
        },
        methods : {
            handleAfterLeave() {
                this.$emit('after-leave');
            },
            startTimer() {
                if (this.type != 'loading' && this.duration > 0) {
                    this.timer = setTimeout(() =>{
                        if (!this.closed) {
                            this.close();
                        }
                    }, this.duration);
                }
            },
            set(opts){
                for (let prop in opts) {
                    if (opts.hasOwnProperty(prop)) {
                        let val = opts[prop];
                        if (val !== undefined) {
                            this[prop] = val;
                        }
                    }
                }
            }
        },
        mounted() {
            this.startTimer();
        },
        updated(){
            this.startTimer();
        }
    };
</script>
<style>
    .zs-modal-spinner {
        min-width: 100px;
        min-height: 100px;
        max-width: 140px;
        left: 50%;
        transform: translateX(-50%) translateY(-50%);
        padding: 10px;
        background-color: rgba(0, 0, 0, .7);
        border-radius: 4px;
    }
    [class*=zs-modal-type-] {
        display: block;
        height: 33px;
        width: 33px;
        margin: 10px auto;
        background-size: cover;
    }
    .zs-modal-type-error {
        background-image: url("../assets/error.png");
    }
    .zs-modal-type-success {
        background-image: url("../assets/success.png");
    }
    .zs-modal-type-warning {
        background-image: url("../assets/order-remind.png");
    }
    svg {
        margin-top: 10px;
    }
</style>
