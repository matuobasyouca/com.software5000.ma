<template>
    <transition name="zs-loading-fade" @after-leave="handleAfterLeave">
        <div
                v-show="visible"
                class="zs-loading-mask"
                :class="[{ 'is-fullscreen': fullscreen }]">
            <div class="zs-loading-spinner">
                <svg class="circular" viewBox="25 25 50 50">
                    <circle class="path" cx="50" cy="50" r="20" fill="none"/>
                </svg>
                <p v-if="message" class="zs-loading-text">{{ message }}</p>
            </div>
        </div>
    </transition>
</template>

<script>
    export default {
        data() {
            return {
                message : null,
                fullscreen : true,
                visible : false
            }
        },
        methods: {
            handleAfterLeave() {
                this.$emit('after-leave');
            }
        }
    };
</script>

