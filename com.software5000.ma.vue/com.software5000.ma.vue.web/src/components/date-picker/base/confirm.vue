<template>
    <div :class="[prefixCls + '-confirm']">
        <span :class="timeClasses" v-if="showTime" @click="handleToggleTime">
            <template v-if="isTime">选择日期</template>
            <template v-else>选择时间</template>
        </span>
        <zs-button size="small" type="text" @click.native="handleClear">清空</zs-button>
        <zs-button size="small" type="primary" @click.native="handleSuccess">确定</zs-button>
    </div>
</template>
<script>
    import ZsButton from '../../button';

    const prefixCls = 'ivu-picker';

    export default {
        components: { ZsButton },
        props: {
            showTime: false,
            isTime: false,
            timeDisabled: false
        },
        data () {
            return {
                prefixCls: prefixCls
            };
        },
        computed: {
            timeClasses () {
                return {
                    [`${prefixCls}-confirm-time-disabled`]: this.timeDisabled
                };
            }
        },
        methods: {
            handleClear () {
                this.$emit('on-pick-clear');
            },
            handleSuccess () {
                this.$emit('on-pick-success');
            },
            handleToggleTime () {
                if (this.timeDisabled) return;
                this.$emit('on-pick-toggle-time');
            }
        }
    };
</script>
