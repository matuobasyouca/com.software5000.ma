<template>
    <div class="pay-suc-page">
        <ma-header>{{ textMap[pageType] }}</ma-header>
        <div class="banner_css"></div>
        <div class="body_css">
            <div class="title_css" v-if="pageType!=2">
                <zs-icon
                        class="icon_css"
                        icon="circle-success"
                        icon-dis="10"
                        size="25"
                        :text="textMap[pageType]"></zs-icon>
            </div>
            <div class="hole" v-if="pageType==2"></div>
            <zs-icon class="icon-bell" icon="bell" size="50"></zs-icon>
            <div class="conent_css">
                <p class="p1_css"  v-if="pageType!=2">
                    提醒：
                    <i class="color-red">管理</i>
                    及
                    <i class="color-red">查看</i>
                    我的卡券
                </p>
                <p class="p1_css" v-if="pageType=2">关注公众号，邀请好友拼单</p>
                <p class="p2_css">长按二维码，关注“诚品好车网”公众号</p>
            </div>
            <div class="bar-code">
                <img :src="barCodeImg" style="height: 145px;width: 145px;"/>
            </div>
            <zs-icon class="cheng" icon="cheng" icon-dis="0" size="18" text="品社区生活"></zs-icon>
        </div>
        <img src="./assets/bar_code_regular.png" alt="" style="display: none">
    </div>
</template>
<script>
    import maHeader from '../../../../components_proj/ma_head/app.vue';
    import {getDataFromParam} from '../../../../assets/js/utils';
    export default {
        components: {
            maHeader
        },
        data(){
            return {
                pageType: 0,
                recodeType : 0,
                textMap: ['支付成功', '绑定成功','支付成功']
            }
        },
        //定义事件，从缓存上取数据，速度快，但事件不可以带参数，可在html区内直接运行
        computed : {
            barCodeImg(){
                let ret='/wap/static/img/bar_code.552f56c.png';
                let hostArr = window.location.host.split('.');  //获取地址栏
                let isLocal = hostArr[0] == '192' || (hostArr[0]).indexOf('localhost') > -1;    //判断是否本地
                if(this.recodeType==1){
                    if(isLocal){

                    }else if(hostArr[1]=='zhongshengchengpin'){
                        ret='/wap/static/img/bar_code_test.1ef9fda.png';
                    }else{
                        ret='/wap/static/img/bar_code_regular.47c1877.png';
                    }
                }
                return ret;
            }
        },
        created(){
            this.pageType = getDataFromParam('type') || 0;
            this.recodeType = getDataFromParam('recodeType') || 0;
        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>