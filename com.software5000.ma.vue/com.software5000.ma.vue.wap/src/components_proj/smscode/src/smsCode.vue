<template>
    <div class="validateCode">
        <input type="tel" maxlength="4" id="telCodeInput" v-model="codesms" @keyup="smskeyUp">
        <label class="squareWrap" for="telCodeInput">
            <p class=""></p>
            <p class=""></p>
            <p class=""></p>
            <p class=""></p>
        </label>
    </div>
</template>
<script>
    import turnToBase from '../../../assets/js/imgTurnToBase';
    import {isCarNum} from '../../../assets/js/utils';
    export default {
        name: 'smsCode',
        props: {
        },
        data () {
            return {
                codesms: ''
            }
        },
        computed: {},
        methods: {
            smskeyUp(){
                let ps = document.getElementsByClassName("squareWrap")[0].getElementsByTagName("p");
                for(let j=0;j<ps.length;j++){
                    ps[j].className="";
                    ps[j].innerHTML="";
                }
                for(let i=0;i<this.codesms.length;i++){
                    ps[i].className='curr';
                    ps[i].innerHTML=this.codesms[i];
                }
                //每一次输入事件
                this.$emit('smskeyUp',this.codesms);
                //全部输入后事件
                if(this.codesms.length==4){
                    this.$emit('smsDoneInput',this.codesms);
                }
            },
        }
    };
</script>
<style scoped lang="less">
    @import '../smsCode.less';
</style>