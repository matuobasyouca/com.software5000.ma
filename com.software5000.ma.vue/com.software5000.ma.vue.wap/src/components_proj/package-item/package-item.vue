<template>
    <div>
        <div class="package-item" v-for="(item,index) in packageList" @click="selectedItem(index)">
            <div class="package-item__top">
                套餐
            </div>
            <div class="package-item__bottom">
                <p class="bottom__name">
                    {{item.packageName}}
                </p>
                <p class="bottom__price">
                    ￥{{item.salePrice}}
                </p>
                <div class="bottom__check-box" v-if="haveSelected">
                    <zs-icon :icon="item.selected ? 'order-check' : 'order-uncheck' " :size="20" ></zs-icon>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'package-item',
        props: {
            packageList: {
                type: Array,
                default: []
            },
            value : {
                type:[String, Number],
                default:''
            },
            haveSelected : {
                type: Boolean,
                default: true
            }

        },
        data () {
            return {
                myValue : this.value
            }
        },
        computed: {

        },
        watch: {
            value(val){

            }
        },
        methods: {
            selectedItem(index){
                if(this.haveSelected){
                    for (let i = 0; i < this.packageList.length; i++) {
                        this.packageList[i].selected=false;
                    }
                    this.packageList[index].selected=true;
                    this.myValue=this.packageList[index].id;
                    this.$emit("getIdList",this.myValue);
                }
            }

        },
        mounted() {
        }
    }
</script>
<style lang="less">
    @import 'package-item.less';
</style>