<template>
    <ul class="tab__wrap clr">
        <li v-for="(tab,index) in tabs" :class="[{curr : value == index + 1}]" :style="tabListStyle" @click="handleTabChange(index+1)">
            <div class="tab-item">
                <p class="title">{{ tab.title }}</p>
                <p class="count" v-if="tab.count || tab.count==0">{{ tab.count }}</p>
            </div>
        </li>
    </ul>
</template>

<script>
    export default {
        name: 'zsTab',
        props: {
            tabs: {
                type: Array,
                default(){
                    return []
                }
            },
            value: {
                type: [String, Number],
                default: 1
            }
        },
        computed: {
            tabListStyle(){
                let style = {};
                    if(this.tabs.length > 0){
                        style.width = `${100/this.tabs.length}%`;
                    }
                return style;
            }
        },
        methods: {
            handleTabChange(index){
                this.$emit('input',index);
                this.$emit('change', index)
            }
        }
    };
</script>