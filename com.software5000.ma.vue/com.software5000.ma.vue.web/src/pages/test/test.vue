<template>
    <div style="padding-top: 50px;">
        <span>{{ value }}</span>
        <zs-select
                v-model="mySelect"
                multiple
                placeholder="请选择">
            <zs-option
                    v-for="option in options"
                    :label="option.label"
                    :value="option.value"
                    :key="option.value">
            </zs-option>
        </zs-select>
    </div>
</template>

<script>
    export default {
        components: {},
        props: {
            value: {
                type: Array,
                default: []
            },
            tt: String
        },
        data(){
            return {
                test: '',
                options: [
                    {
                        value: '选项1',
                        label: '黄金糕1'
                    }, {
                        value: '选项2',
                        label: '双皮奶',
                        disabled: true
                    }, {
                        value: '选项3',
                        label: '蚵仔煎'
                    }, {
                        value: '选项4',
                        label: '龙须面'
                    }, {
                        value: '选项5',
                        label: '北京烤鸭'
                    }
                ],
                mySelect: ''
            };
        },
        methods: {},
        watch: {
            value: {
                handler(){
                    console.log('testchange');
                },
                deep: true
            }
        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>
