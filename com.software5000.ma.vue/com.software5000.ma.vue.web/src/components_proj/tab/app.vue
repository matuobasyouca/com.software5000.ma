<template>
    <div class="tab-wrapper">
        <ul class="tab-list">
            <li
                    v-for="(t,i) in tabs"
                    :key="i"
                    :class="[{'curr':i == current - 1}]"
                    @click="handleTab(i)">{{ t }}
            </li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: 'tab',
        props: {
            tabs: {
                type: Array,
                default(){
                    return []
                }
            },
            current: {
                type: [String, Number],
                default: 1
            }
        },
        methods: {
            handleTab(i){
                this.$emit('change', i + 1)
            }
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
