<template>
    <div class="search__wrap" :class="[{'is-focus' : isFocus,'has-title' : title}]" v-clickoutside="handleBlur">
        <p class="search-title" v-if="title">{{ title }}</p>
        <div class="search-input__wrap" :class="[{'is-focus' : isFocus}]">
            <div class="search-input__inner">
                <i class="search-input-icon"></i>
                <div>
                    <zs-input
                            class="search-input"
                            :placeholder="placeholder"
                            v-model="keyWord"
                            icon="circle-cross"
                            @focus="handleFocus"
                            @enter="handleControl"></zs-input>
                </div>
            </div>
        </div>
        <p v-show="isFocus" class="search-input-control" @click="handleControl">搜索</p>
    </div>
</template>

<script>
    import clickoutside from '../../components/src/directives/clickoutside';
    export default {
        name: 'search',
        directives: {
            clickoutside
        },
        props: {
            title: String,
            placeholder: String
        },
        data(){
            return {
                keyWord: '',
                isFocus: false
            }
        },
        watch: {
            keyWord(val){
                this.$emit('input', val);
            }
        },
        methods: {
            handleFocus(){
                this.isFocus = true
            },
            handleBlur(){
                this.isFocus = false
            },
            handleControl(){
                this.$emit('search', this.keyWord);
            }
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>